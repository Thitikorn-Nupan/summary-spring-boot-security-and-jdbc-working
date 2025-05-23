package com.ttknp.understandspringsecuritywithabstractauthenticationtokenandapplyjdbc.configs.security_config.jwt;

import com.ttknp.understandspringsecuritywithabstractauthenticationtokenandapplyjdbc.entities.login.LoginModel;
import com.ttknp.understandspringsecuritywithabstractauthenticationtokenandapplyjdbc.services.UsefulService;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.*;
import java.util.function.Function;

// @Service
// Lockton uses the AbstractAuthenticationToken abs with Custom abs class like below ** very useful
// you can call JwtAuthenticateToken class tru SecurityContextHolder as : (JwtAuthenticateToken) SecurityContextHolder.getContext().getAuthentication() it will give this class
public class JwtAuthenticateToken extends AbstractAuthenticationToken {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticateToken.class);
    private final LoginModel loginModel;
    private final Object credentials;
    private final Claims claim;
    private final String token;
    private final String subject;

    public JwtAuthenticateToken(LoginModel loginModel, Object credentials,Claims claims, String token) {
        super(UsefulService.convertRolesStringToGrantedAuthorityList(loginModel.getRole())); // role
        this.loginModel = loginModel;
        this.credentials = credentials; // can be null
        this.subject = null;
        this.claim = claims;
        this.token = token;
        super.setAuthenticated(true);
    }

    public JwtAuthenticateToken(Claims claims, String token) {
        super(null);
        this.credentials = null;
        this.loginModel = null;
        this.subject = null;
        this.claim = claims;
        this.token = token;
        super.setAuthenticated(true);
    }

    public Claims getClaim() {
        return claim;
    }

    public String getToken() {
        return token;
    }

    public String getSubject() {
        return subject;
    }

    // not working
    public String getSubjectFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getIssueFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    public Date getExpirationFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private  <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        // log.debug("this.getClaim() {}", this.getClaim()); // {exp=1747977424, user={password=MQ==, createBy=Admin, role=ADMIN, uuid=59aef92e-3d50-45d2-bdfb-19051c6df16f, email=Admin@hotmail.com, username=Admin}, iat=1747973824}
        log.debug("claimsResolver.apply(claim) {}", claimsResolver.apply(claim));
        return claimsResolver.apply(claim);
    }

    /**
    ก่อนอื่นเราต้องรู้จักกับของ 5 อย่างที่สำคัญของ AutheticationToken นั้นก็คือ
    Principal -> เป็นของที่เราเอาไว้ระบุตัวตนเช่น name , email, id
    GrantedAuthorities -> เป็นของที่เอาไว้บอกสิทธิ์การเข้าถึงเช่น Roles
    isAuthenticated -> คือ Flag ที่เอาไว้บอกว่า Authenticated แล้ว
    Detail -> เป็นที่เก็บข้อมูลเพิ่มเติม คำอธิบายเพิ่มเติม
    Credentials -> password
    */
    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.loginModel;
    }

    /*@Override
    public String getName() {
        return super.getName();
    }*/
    /*
    @Override
    public boolean isAuthenticated() {
        return true;
    }
    */


    /* private Claims getAllClaimsFromToken(String token) {
        return Jwts
                .parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
*/

    /*public Boolean validateToken(String token, UserDetails userDetails) {
        logging.logBack.info("validateToken() works");
        final String username = getUsernameFromToken(token);
        if (username.equals(userDetails.getUsername()) && !isTokenExpired(token)) {
            logging.logBack.info("User exists");
            return true;
        } else {
            logging.logBack.warn("User do not exists");
            return false;
        }
    }

    // retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // **
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver
                .apply(claims);
    }

    // retrieve any information from token we will need the secret key


    // check if the token has expired (v. หมดอายุแล้ว)


    // retrieve expiration(การหมดอายุ) date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token,
                Claims::getExpiration);
    }

    // generate token for user
    public String generateToken(UserDetails userDetails) {
        logging.logBack.info("generateToken() works");
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(
                claims,
                userDetails.getUsername()
        );
    }
    private String doGenerateToken(Map<String, Object> claims, String subject) {
       *//*
           Here, the doGenerateToken() method creates a JSON Web Token
        *//*
        logging.logBack.info("doGenerateToken() works");
        // issue (v. ออก)
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(subject) // Subject is combination of the username
                .setIssuedAt(new Date()) // The token is issued at the current date and time
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000)) // The token should expire after 24 hours
                .signWith(SignatureAlgorithm.HS512, secret) // The token is signed using a secret key, which you can specify in the application.properties file or from system environment variable
                .compact();
    }*/

}
