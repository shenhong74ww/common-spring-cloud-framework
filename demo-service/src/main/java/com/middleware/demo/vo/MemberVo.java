package com.middleware.demo.vo;

import com.middleware.demo.constant.enums.member.MemberType;
import com.middleware.demo.model.Company;

import java.util.Date;

public class MemberVo {
    private Long companySid;
    private Company company;
    private MemberType memberType;
    private String username;
    private Date birthday;
    private String email;
    private Integer regionCode;
    private String phone;

    public Long getCompanySid() {
        return companySid;
    }

    public void setCompanySid(Long companySid) {
        this.companySid = companySid;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public MemberType getMemberType() {
        return memberType;
    }

    public void setMemberType(MemberType memberType) {
        this.memberType = memberType;
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
}
