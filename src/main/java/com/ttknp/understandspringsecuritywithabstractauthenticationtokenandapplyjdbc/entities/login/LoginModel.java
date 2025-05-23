package com.ttknp.understandspringsecuritywithabstractauthenticationtokenandapplyjdbc.entities.login;

import com.ttknp.understandspringsecuritywithabstractauthenticationtokenandapplyjdbc.entities.common_primery.PrimaryKey;
import com.ttknp.understandspringsecuritywithabstractauthenticationtokenandapplyjdbc.services.UsefulService;

import java.sql.Timestamp;
import java.util.UUID;

public class LoginModel extends PrimaryKey {

    private String createBy;
    private Timestamp createDatetime;
    private Timestamp modifyDatetime;
    private String username;
    private String password; // this case i use Base64 Format before add to database
    private String email;
    private String role;


    public LoginModel( String createBy, Timestamp createDatetime, Timestamp modifyDatetime, String username, String password, String email, String role) {
        super(UsefulService.genNewUuID());
        this.createBy = createBy;
        this.createDatetime = createDatetime;
        this.modifyDatetime = modifyDatetime;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public LoginModel(UUID uuid, String createBy, Timestamp createDatetime, Timestamp modifyDatetime, String username, String password, String email, String role) {
        super(uuid);
        this.createBy = createBy;
        this.createDatetime = createDatetime;
        this.modifyDatetime = modifyDatetime;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public LoginModel() {
        super(null);
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Timestamp getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Timestamp createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Timestamp getModifyDatetime() {
        return modifyDatetime;
    }

    public void setModifyDatetime(Timestamp modifyDatetime) {
        this.modifyDatetime = modifyDatetime;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "LoginModel{" +
                "uuid='" + getUuid() + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createDatetime=" + createDatetime +
                ", modifyDatetime=" + modifyDatetime +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
