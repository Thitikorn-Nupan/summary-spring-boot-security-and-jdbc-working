package com.ttknp.understandspringsecuritywithabstractauthenticationtokenandapplyjdbc.helpers.sql;

// Note H2 version 2.3.2
public class Commands {
    // NamedParam Jdbc
    public static final String STUDENT_INSERT = "INSERT INTO TTKNP_SCHOOL.STUDENTS( FULL_NAME, BIRTHDAY, LEVEL) VALUES(:fullName, :birthday, :level)"; // map property of object/pojo
    public static final String STUDENT_SELECT_COUNT_LEVEL = "SELECT COUNT(*) FROM TTKNP_SCHOOL.STUDENTS WHERE level = :level"; // map property of object/pojo
    // Jdbc
    public static final String USERS_LOGIN_SELECT_ONE_BY = "SELECT * FROM AUTH.USERS_LOGIN WHERE USERNAME = ?;";



}