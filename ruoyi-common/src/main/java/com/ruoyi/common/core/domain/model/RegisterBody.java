package com.ruoyi.common.core.domain.model;

/**
 * 用户注册对象
 * 
 * @author ruoyi
 */

public class RegisterBody extends LoginBody
{
    private String email;

    private String EmailCode;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailCode() {
        return EmailCode;
    }

    public void setEmailCode(String emailCode) {
        EmailCode = emailCode;
    }
}
