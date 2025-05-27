package com.ttknp.understandspringsecuritywithabstractauthenticationtokenandapplyjdbc.entities.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ttknp.understandspringsecuritywithabstractauthenticationtokenandapplyjdbc.entities.common_primery.PrimaryKey;
import com.ttknp.understandspringsecuritywithabstractauthenticationtokenandapplyjdbc.services.UsefulService;


public class Student extends PrimaryKey {

    private String fullName;
    private Integer age;

    public Student(@JsonProperty("fullName") String fullName, @JsonProperty("age") Integer age) {
        super(UsefulService.genNewUuID());
        this.fullName = fullName;
        this.age = age;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
