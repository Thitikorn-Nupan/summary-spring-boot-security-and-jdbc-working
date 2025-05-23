package com.ttknp.understandspringsecuritywithabstractauthenticationtokenandapplyjdbc.entities.login;

public class LoginResponse {

    private String id;
    private String token;

    public LoginResponse(String id, String token) {
        this.id = id;
        this.token = token;
    }

    public LoginResponse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "id='" + id + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
