package com.ohdocha.cu.kprojectcu.domain;

import org.apache.ibatis.type.Alias;

@Alias("adminUserRequest")
public class DochaAdminUserRequest {

    private String adIdx;
    private String adminId;
    private String adminPassword;
    private String adminName;
    private String userContact1;
    private String userContact2;
    private String userRole;
    private String rtIdx;
    private String regDt;
    private String regId;
    private String modDt;
    private String modId;
    private String delYn;

    public String getAdIdx() {
        return adIdx;
    }

    public void setAdIdx(String adIdx) {
        this.adIdx = adIdx;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getUserContact1() {
        return userContact1;
    }

    public void setUserContact1(String userContact1) {
        this.userContact1 = userContact1;
    }

    public String getUserContact2() {
        return userContact2;
    }

    public void setUserContact2(String userContact2) {
        this.userContact2 = userContact2;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getRtIdx() {
        return rtIdx;
    }

    public void setRtIdx(String rtIdx) {
        this.rtIdx = rtIdx;
    }

    public String getRegDt() {
        return regDt;
    }

    public void setRegDt(String regDt) {
        this.regDt = regDt;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getModDt() {
        return modDt;
    }

    public void setModDt(String modDt) {
        this.modDt = modDt;
    }

    public String getModId() {
        return modId;
    }

    public void setModId(String modId) {
        this.modId = modId;
    }

    public String getDelYn() {
        return delYn;
    }

    public void setDelYn(String delYn) {
        this.delYn = delYn;
    }
}
