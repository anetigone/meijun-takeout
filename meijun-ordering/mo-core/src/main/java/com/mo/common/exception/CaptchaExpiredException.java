package com.mo.common.exception;

public class CaptchaExpiredException extends BaseException {
    public CaptchaExpiredException() {
        super();
    }

    public CaptchaExpiredException(String message) {
        super(message);
    }
}
