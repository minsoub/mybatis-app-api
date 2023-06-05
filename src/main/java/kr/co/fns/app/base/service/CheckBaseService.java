package kr.co.fns.app.base.service;


import kr.co.fns.app.core.aop.exception.ErrorCode;
import kr.co.fns.app.core.aop.exception.custom.FantooException;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class CheckBaseService {

    private static final Pattern IP_PATTERN = Pattern.compile("((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})");

    //토큰에 있는 uid와 ingetUid를 체크하는 공통기능
    public void uidCheck(String tokenUserId, String integUid) {
        if (!tokenUserId.equals(integUid)) {
            throw new FantooException(ErrorCode.ERROR_FE5006);
        }
    }

    public String extractIpFromRequest(String userIp) {
        Matcher matcher = IP_PATTERN.matcher(userIp);
        if (matcher.find()) {
            return matcher.group();
        }
        return userIp;
    }
}
