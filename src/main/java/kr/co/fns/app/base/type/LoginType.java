package kr.co.fns.app.base.type;

public enum LoginType {

    /**
     * 회원가입 Type (sns,email 이 기본.)
     * etc는 몽고DB에서 분류불가능의 경우 EX)admin 등등
     */
    email,
    apple,
    facebook,
    google,
    kakao,
    line,
    twitter,
    phone,
    etc;

}
