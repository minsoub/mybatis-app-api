package kr.co.fns.app.core.token;


import kr.co.fns.app.core.aop.exception.ErrorCode;
import kr.co.fns.app.core.aop.exception.custom.FantooException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Map;

@Slf4j
@Service("TokenService")
public class TokenService {


    /**
     * SSO Token 인증 및 정보 획득
     */
    public String getUserSSOToken(String token) {
        try {
            final String payloadJWT = token.split("\\.")[1];
            Base64.Decoder decoder = Base64.getUrlDecoder();

            final String payload = new String(decoder.decode(payloadJWT));
            JsonParser jsonParser = new BasicJsonParser();
            Map<String, Object> jsonArray = jsonParser.parseMap(payload);

            if (!jsonArray.containsKey("sub")) {
                throw new FantooException(ErrorCode.ERROR_FE1005);
            }
            return jsonArray.get("sub").toString();
        } catch (Exception e) {
            throw new FantooException(ErrorCode.ERROR_FE1005);
        }
    }


}
