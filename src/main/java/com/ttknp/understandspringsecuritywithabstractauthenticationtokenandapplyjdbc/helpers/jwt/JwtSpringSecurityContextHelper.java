package com.ttknp.understandspringsecuritywithabstractauthenticationtokenandapplyjdbc.helpers.jwt;

import com.ttknp.understandspringsecuritywithabstractauthenticationtokenandapplyjdbc.configs.security_config.jwt.JwtAuthenticateToken;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;

// Very perfect user didn't need pass token always on own just get it with SecurityContextHolder
public abstract class JwtSpringSecurityContextHelper {

    private static final Logger log = LoggerFactory.getLogger(JwtSpringSecurityContextHelper.class);

    public static JwtAuthenticateToken getAuthenticationFromContext() {
        JwtAuthenticateToken jwtAuthenticateToken = (JwtAuthenticateToken) SecurityContextHolder.getContext().getAuthentication();

        if (jwtAuthenticateToken == null) {
             log.debug("JwtAuthenticateToken is null on security context");
        }
        return jwtAuthenticateToken;
    }

    public static Boolean isTokenExpired() {
        JwtAuthenticateToken jwtAuthenticateToken = getAuthenticationFromContext();
        final Date expiration = jwtAuthenticateToken.getExpirationFromToken(jwtAuthenticateToken.getToken());
        return expiration
                .before(new Date());
    }

    public static Date getExpirationDate() {
        JwtAuthenticateToken jwtAuthenticateToken = getAuthenticationFromContext();
        return jwtAuthenticateToken.getExpirationFromToken(jwtAuthenticateToken.getToken());
    }

    public static Date getIssueDate() {
        JwtAuthenticateToken jwtAuthenticateToken = getAuthenticationFromContext();
        return jwtAuthenticateToken.getIssueFromToken(jwtAuthenticateToken.getToken());
    }

    public static Object getPrincipal() {
        // JwtAuthenticateToken jwtAuthenticateToken = getAuthenticationFromContext();
        return getAuthenticationFromContext().getPrincipal();
    }

    public static Claims getClaim() {
        return getAuthenticationFromContext().getClaim();
    }

    /*public static Boolean validateToken(String token,String username) {
        JwtAuthenticateToken jwtAuthenticateToken = getAuthenticationFromContext();
        final String subject = jwtAuthenticateToken.getSubjectFromToken(token);
        log.debug("subject : {} user : {}", subject,username);
        return ( subject.equals(username) && !isTokenExpired(token) );
    }*/




}
