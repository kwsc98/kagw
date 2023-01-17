package pers.kagw.core.exception;

import java.util.Objects;

/**
 * @author kwsc98
 */
public class ApiGateWayException extends RuntimeException {

    ExceptionInfo exceptionInfo = null;

    public ApiGateWayException() {
    }

    public ApiGateWayException(String message) {
        super(message);
    }


    public ApiGateWayException(ExceptionInfo exceptionInfo) {
        this.exceptionInfo = exceptionInfo;
    }

    public String getInfoStr() {
        if (Objects.nonNull(exceptionInfo)) {
            return exceptionInfo.getExceptionInfoStr();
        }
        return null;
    }

}
