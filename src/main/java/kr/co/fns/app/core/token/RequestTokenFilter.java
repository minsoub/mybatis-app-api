package kr.co.fns.app.core.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.fns.app.config.ExcludePathPatterns;
import kr.co.fns.app.config.ExternalUrlProperties;
import kr.co.fns.app.core.common.CommonService;
import kr.co.fns.app.core.aop.exception.ErrorCode;
import kr.co.fns.app.core.aop.exception.custom.FantooException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class RequestTokenFilter extends OncePerRequestFilter {

    private ExternalUrlProperties properties;

    private final CommonService commonService;

    private RestTemplate restTemplate;
    private Map<String, Object> clientInfoMap;

    public RequestTokenFilter(ExternalUrlProperties properties) {
        this.commonService = new CommonService();
        this.restTemplate = new RestTemplate();

        this.properties = properties;

        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            public boolean hasError(ClientHttpResponse response) throws IOException {
                HttpStatus statusCode = response.getStatusCode();
                return statusCode.series() == HttpStatus.Series.SERVER_ERROR;
            }
        });
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        restTemplate.getMessageConverters().add(converter);

        clientInfoMap = new HashMap<>();
        clientInfoMap.put("clientId", properties.getClientId());   // Fantoo API SSO, clinetId key
        clientInfoMap.put("clientSecret", properties.getClientSecret());  // Fantoo API SSO, clientSecret key
    }



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // 헤더의 토큰
        String requestAccessToken = request.getHeader("access_token");
        String reqUri = request.getRequestURI();
        String methodType = request.getMethod();
        log.info("request methodType => {}", methodType);

        if(ObjectUtils.isEmpty(requestAccessToken)) {
            // 비로그인 uri 체크
            AntPathMatcher pathMatcher = new AntPathMatcher();
            boolean isMatched = false; // Arrays.stream(ExcludePathPatterns.excludePathPatternsMap.keySet().toArray()).anyMatch(v -> pathMatcher.match(v.toString(), reqUri));
            String matchMethodType = "NONE";
            String matchUri = null;

            Object[] macthList = ExcludePathPatterns.excludePathPatternsMap.keySet().toArray();
            for (Object key : macthList) {
                if (pathMatcher.match(key.toString(), reqUri)) {
                    isMatched = true;
                    matchUri = key.toString();
                    matchMethodType = ExcludePathPatterns.excludePathPatternsMap.get(key);
                    break;
                }
            }
            log.info("isMatched => {}, {}, {}, {}", reqUri, isMatched, matchUri, matchMethodType);
            if (isMatched && (matchMethodType.equals("NONE") || matchMethodType.contains(methodType))) {
                //비로그인시 호출
                chain.doFilter(request, response);
                return;
            }
        }
        
        //토큰이 없을때 에러 발생
        if(ObjectUtils.isEmpty(requestAccessToken)) {
            //토큰이 없으면 에러 401?
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write("{\n" +
                    "                        \"code\": \"401\",\n" +
                    "                            \"msg\": \"Unauthorized!! (Token authentication failed)\",\n" +
                    "                            \"dataObj\": {\"uri\":\"" + reqUri + "\"}\n" +
                    "                    }");
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("access_token", requestAccessToken);

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(null, headers);

        try {
            // SSO에서 토큰정보와 ingegUid를 얻는다.
            ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(properties.getAuthUrl(), HttpMethod.POST, httpEntity, JSONObject.class);

            if(responseEntity.getStatusCode() == HttpStatus.OK) {
//                RequestModifiWrapper requestModifiWrapper = new RequestModifiWrapper(request);
//                JSONObject jsonObj = commonService.getStringToJson(String.valueOf(responseEntity.getBody()));
////                requestModifiWrapper.setParameter("integUid", (String) jsonObj.get("integUid"));
////                requestModifiWrapper.setParameter("tokenInfo", String.valueOf(responseEntity.getBody()));
//                request = requestModifiWrapper;

                chain.doFilter(request, response);
            } else if(responseEntity.getStatusCode() == HttpStatus.UNAUTHORIZED && "AE5102".equals(responseEntity.getBody().get("code"))) {

                // sso 전달 받은 갱신된 access_token
                ObjectMapper objectMapper = new ObjectMapper();
                response.setStatus(responseEntity.getStatusCode().value());
                response.setContentType("application/json");
                response.getWriter().write(new ObjectMapper().writeValueAsString(responseEntity.getBody()));

            } else {
                throw new FantooException(ErrorCode.ERROR_401);
                //this.getReissue(HttpStatus.UNAUTHORIZED, response, null);
            }
        } catch (Exception e) {
            throw new FantooException(ErrorCode.ERROR_401);
            //this.getReissue(HttpStatus.UNAUTHORIZED, response, null);
        }
    }
}
