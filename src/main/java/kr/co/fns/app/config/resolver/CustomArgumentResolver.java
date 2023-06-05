package kr.co.fns.app.config.resolver;

import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Slf4j
public class CustomArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtDecoder jwtDecoder;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        var token = "";

        final String AUTHORIZATION = "access_token";
        final String USER_IP = "user_ip";

        if (!StringUtils.isEmpty(webRequest.getHeader(AUTHORIZATION))) {
            token = webRequest.getHeader(AUTHORIZATION);
        }

        // token parsing
        log.debug("resolveArgument token data => {}", token);

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String clientIp = getClientIp(request);
        log.debug("resolveArgument client-ip => {}", clientIp);
        try {
            Jwt jwt = jwtDecoder.decode(token);  // token이 없을 수도 있다.

            if (StringUtils.isAnyEmpty(jwt.getClaimAsString("integUid")) || StringUtils.isAnyEmpty(jwt.getClaimAsString("roles"))) {
                return Account.builder()
                        .integUid("")
                        .roles("")
                        .userIp(clientIp)
                        .loginType(LoginType.notlogin)
                        .tokenInfo(token)
                        .build();
            } else {
                return Account.builder()
                        .integUid(jwt.getClaimAsString("integUid"))
                        .roles(jwt.getClaimAsString("roles"))
                        .userIp(clientIp)
                        .loginType(LoginType.valueOf(jwt.getClaimAsString("loginType")))
                        .tokenInfo(token)
                        .build();
            }
        }catch (JwtException jwtException) {
            log.debug("resolveArgument exception => {}", jwtException);
            return Account.builder()
                    .integUid("")
                    .roles("")
                    .userIp(clientIp)
                    .loginType(LoginType.notlogin)
                    .tokenInfo(token)
                    .build();
        }
    }

    /**
     * Client IP를 리턴한다.
     * java.net.preferIPv4Stack=true => 로컬인 경우 테스트 할 때 옵션을 추가해야 함.
     * @param request
     * @return
     */
    private String getClientIp(HttpServletRequest request){
        String clientIp = request.getHeader("X-Forwarded-For");
        if (StringUtil.isNullOrEmpty(clientIp)|| "unknown".equalsIgnoreCase(clientIp)) {
            //Proxy 서버인 경우
            clientIp = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtil.isNullOrEmpty(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtil.isNullOrEmpty(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtil.isNullOrEmpty(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtil.isNullOrEmpty(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("X-Real-IP");
        }
        if (StringUtil.isNullOrEmpty(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getRemoteAddr();
        }
        return clientIp;
    }
}
