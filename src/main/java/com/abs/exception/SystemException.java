package com.abs.exception;

public class SystemException extends Exception {
    private static final long serialVersionUID = 1L;

    private ExceptionEnum exceptionEnum;

    public SystemException() {
        this.exceptionEnum = ExceptionEnum.SYSTEM_ERROR;
    }

    public SystemException(ExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }

    public ExceptionEnum getExceptionEnum() {
        return exceptionEnum;
    }

    public String getErrorMsg() {
        return this.exceptionEnum.getErrorMsg();
    }

    public String getErrorCode() {
        return this.exceptionEnum.getErrorCode();
    }
}

