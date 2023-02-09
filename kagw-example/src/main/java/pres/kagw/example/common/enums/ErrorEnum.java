package pres.kagw.example.common.enums;

import lombok.Getter;
import pers.kagw.core.common.JsonUtils;
import pers.kagw.core.exception.ExceptionEnum;
import pers.kagw.core.exception.ExceptionInfo;

import java.util.Objects;

/**
 * @author kwsc98
 */
@Getter
public enum ErrorEnum implements ExceptionInfo {


    /**
     * TOKEN_ERROR
     */
    REQUEST_ERROR("9996","Request Error"),
    TOKEN_ERROR("9997","Token Error");

    public final String code;

    public final String infoStr;

    ErrorEnum(String code, String infoStr) {
        this.code = code;
        this.infoStr = infoStr;
    }

    @Override
    public String getExceptionInfoStr() {
        try {
            return JsonUtils.writeValueAsString(Info.build(this));
        } catch (Exception e) {
            return "{}";
        }
    }

    static class Info {
        public String code;

        public String infoStr;

        public static Info build(ErrorEnum errorEnum) {
            Info info = new Info();
            if (Objects.nonNull(errorEnum)) {
                info.code = errorEnum.code;
                info.infoStr = errorEnum.infoStr;
            }
            return info;
        }
    }
}
