package com.ttknp.understandspringsecuritywithabstractauthenticationtokenandapplyjdbc.services.login;

import com.ttknp.understandspringsecuritywithabstractauthenticationtokenandapplyjdbc.entities.login.LoginModel;
import com.ttknp.understandspringsecuritywithabstractauthenticationtokenandapplyjdbc.helpers.jdbc.JdbcExecuteSQLHelper;
import com.ttknp.understandspringsecuritywithabstractauthenticationtokenandapplyjdbc.helpers.sql.Commands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginModelService {

    private final JdbcExecuteSQLHelper jdbcExecuteSQLHelper;

    @Autowired
    public LoginModelService(JdbcExecuteSQLHelper jdbcExecuteSQLHelper) {
        this.jdbcExecuteSQLHelper = jdbcExecuteSQLHelper;
    }

    public LoginModel getLoginModelByUsername(String username) {
        return jdbcExecuteSQLHelper.selectOneMapPropByBeanPropertyRowMapper(Commands.USERS_LOGIN_SELECT_ONE_BY,LoginModel.class,username);
    }

}
