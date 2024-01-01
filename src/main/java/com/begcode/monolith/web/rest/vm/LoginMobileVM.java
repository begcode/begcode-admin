package com.begcode.monolith.web.rest.vm;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * View Model object for storing a user's credentials.
 */
public class LoginMobileVM {

    @NotNull
    @Size(min = 10, max = 20)
    private String mobile;

    @NotNull
    @Size(min = 4, max = 8)
    private String code;

    private Boolean rememberMe;

    private String imageCode;

    public Boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImageCode() {
        return imageCode;
    }

    public void setImageCode(String imageCode) {
        this.imageCode = imageCode;
    }

    @Override
    public String toString() {
        return "LoginMobileVM{" + "mobile='" + mobile + '\'' + ", rememberMe=" + rememberMe + '}';
    }
}
