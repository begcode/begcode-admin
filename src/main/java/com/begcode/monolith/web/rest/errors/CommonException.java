package com.begcode.monolith.web.rest.errors;

public class CommonException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public CommonException(String code, String msg) {
        super(msg, "SystemInfo", code);
    }
}
