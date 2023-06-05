package kr.co.fns.app.core.token.interceptor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import kr.co.fns.app.core.aop.exception.ErrorCode;
import kr.co.fns.app.core.aop.exception.custom.FantooException;
import kr.co.fns.app.core.common.CommonService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



@Component
@Slf4j
@DependsOn({"sqlSessionTemplate"})
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    CommonService commonService;

    private Gson gsonExpose;

    public TokenInterceptor() {
        gsonExpose = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                .serializeNulls()
                .create();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // TODO 개발기간만 log 처리. token 검증 들어오는 url 확인용.
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        log.info("-------- Token 검증 시작 O --------");


        /**
         * 1. 대부분의 URL을 검사하지만 예외가 필요할 경우 추가.
         */
        // TODO 일단은 허용. 추후 고민필수
        String url = request.getRequestURI();
        log.info(url);
        if (url.contains("swagger") || url.contains("api-docs") || url.contains("webjars") || url.contains("actuator")) {
            return true;
        }


        /**
         * 2. integUid 회원 검증
         */
//        if(ObjectUtils.isEmpty(request.getParameter("integUid"))) {
//            stopWatch.stop();
//            log.info("-------- Token 검증 실패 K -------- 소요시간 Millis: {}", stopWatch.getTotalTimeMillis());
//            throw new FantooException(ErrorCode.ERROR_401, "Token authentication failed");
//        }

//        // TODO 추후 회원정보는 캐시로 담아내는 작업 추가 예정.
//        Users users = userInfoService.isJoinIntegUidUser(request.getParameter("integUid"));
//        JSONObject jsonObj = commonService.getStringToJson(request.getParameter("tokenInfo"));
//        if(ObjectUtils.isEmpty(users)) {
//
//            // 회원가입.
//            UserInfoDto userInfoDto = UserInfoDto.builder()
//                    .integUid(request.getParameter("integUid"))
//                    .loginType(LoginType.valueOf((String)jsonObj.get("loginType")))
//                    .birthDay((String)jsonObj.get("birthDay"))
//                    .userNick((String)jsonObj.get("userNick"))
//                    .userPhoto((String)jsonObj.get("userPhoto"))
//                    .email((String)jsonObj.get("email"))
//                    .cellCode((String)jsonObj.get("cellCode"))
//                    .cellNum((String)jsonObj.get("cellNum"))
//                    .countryIsoTwo((String)jsonObj.get("countryCode"))
//                    .build();
//
//            // 회원가입
//            userJoinService.userJoin(userInfoDto);
//        } else {
//            Map<String, Object> map = new HashMap<>();
//            String integUid = (String) jsonObj.get("integUid");
//
//            // 비교대상에서 제외.
//            jsonObj.remove("createDate");
//            jsonObj.remove("integUid");
//            jsonObj.remove("loginType");
//            jsonObj.remove("cellCode");
//            jsonObj.remove("cellNum");
//            jsonObj.remove("loginId");
//
//            JSONObject getUserInfo = commonService.getStringToJson(gsonExpose.toJson(users));
//            jsonObj.keySet().forEach(keyStr -> {
//                Object newObj = jsonObj.get(keyStr);
//                Object oldObj = getUserInfo.get(keyStr);
//
//                Object newObjVal = ObjectUtils.isEmpty(newObj) ? "": newObj;
//                Object oldObjVal = ObjectUtils.isEmpty(oldObj) ? "": oldObj;
//                if(!newObjVal.equals(oldObjVal)) {
//                    map.put(keyStr.toString(), newObjVal);
//                }
//            });
//
//            if(!ObjectUtils.isEmpty(map)) {
//                // 회원정보 update
//                userInfoService.updateUser(integUid, jsonObj);
//                // 회원 캐시 삭제
//                redisService.redisDelete(ClubCacheType.USER.getKey(integUid));
//            }
//
//            // Update LastLoginDate
//            /*if(!ObjectUtils.isEmpty(valueOperations.get(RedisKeyType.userLastLogin.getType() + integUid))) {
//                userInfoService.updateLastLoginDate(integUid);
//            }*/
//
//
//            // 1.0 이관 회원중 추천인코드 누락자 대상, 추천인코드 생성
//            if(ObjectUtils.isEmpty(users.getReferralCode())) {
//                userJoinService.userCreateReferralCode(integUid);
//            }
//        }

        stopWatch.stop();
        log.info("-------- Token 검증 완료 K -------- 소요시간 Millis: {}", stopWatch.getTotalTimeMillis());
        return true;
    }
}
