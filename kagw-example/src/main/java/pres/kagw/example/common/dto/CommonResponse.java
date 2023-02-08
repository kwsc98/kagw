package pres.kagw.example.common.dto;

import lombok.Getter;

/**
 * @author kwsc98
 */
@Getter
public class CommonResponse {

    private String code;

    private String infoStr;

    public static CommonResponse build(){
        return new CommonResponse();
    }

    public CommonResponse setCode(String code) {
        this.code = code;
        return this;
    }

    public CommonResponse setInfoStr(String infoStr) {
        this.infoStr = infoStr;
        return this;
    }
}
