package kr.co.fns.app.api.common.type;

import lombok.Getter;
import java.util.Arrays;

@Getter
public enum LangCodeType {
    PT("pt"),
    IN("in"),
    VI("vi"),
    TH("th"),
    AR("ar"),
    KO("ko"),
    JA("ja"),
    EN("en"),
    ES("es"),
    FR("fr"),
    PL("pl"),
    IT("it"),
    RU("ru"),
    DE("de"),
    HI("hi"),
    CH("cn"),
    TW("tw");

    private String code;
    LangCodeType(String langCode){
        this.code = langCode;
    }
    public static String getLangCode(String langCode){
        String newLangCode = langCode.replace("_", "-");
        LangCodeType langCodeType  = Arrays.stream(LangCodeType.values())
                .filter(v -> v.getCode().equals(newLangCode))
                .findFirst()
                .orElse(LangCodeType.EN);
        return langCodeType.getCode();
    }
}
