package com.abs.exception;

public class BusinessException extends Exception {
    private static final long serialVersionUID = 1L;

    private ExceptionEnum exceptionEnum;

    public BusinessException() {}

    public BusinessException(ExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }

    public ExceptionEnum getExceptionEnum() {
        return exceptionEnum;
    }

    public void setExceptionEnum(ExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }

    public String getErrorMsg() {
        return this.exceptionEnum.getErrorMsg();
    }

    public String getErrorCode() {
        return this.exceptionEnum.getErrorCode();
    }

}

