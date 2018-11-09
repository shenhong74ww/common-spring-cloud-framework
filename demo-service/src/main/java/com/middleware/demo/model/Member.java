package com.middleware.demo.model;

import com.middleware.common.model.BaseModel;
import com.middleware.demo.constant.enums.member.MemberType;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.util.Date;

@Entity
public class Member extends BaseModel {
    private Long companySid;
    @OneToOne
    @JoinColumn(name = "companySid", insertable = false, updatable = false)
    private Company company;
    private MemberType type;
    private String username;
    private String password;
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

    public MemberType getType() {
        return type;
    }

    public void setType(MemberType type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
