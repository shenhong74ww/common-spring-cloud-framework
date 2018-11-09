package com.middleware.demo.request;

import com.middleware.demo.constant.enums.member.MemberType;

import java.util.Date;

public class SignUpRequest {
    private Long companySid;
    private MemberType memberType;
    private Integer regionCode;
    private String phone;
    private String password;
    private String clientId;
    private String username;
    private Date birthday;
    private String email;

    public Long getCompanySid() {
        return companySid;
    }

    public void setCompanySid(Long companySid) {
        this.companySid = companySid;
    }

    public MemberType getMemberType() {
        return memberType;
    }

    public void setMemberType(MemberType memberType) {
        this.memberType = memberType;
    }

    public Integer getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(Integer regionCode) {
        this.regionCode = regionCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
