package com.ttknp.understandspringsecuritywithabstractauthenticationtokenandapplyjdbc.controls;

import com.ttknp.understandspringsecuritywithabstractauthenticationtokenandapplyjdbc.configs.security_config.jwt.JwtAuthenticateToken;
import com.ttknp.understandspringsecuritywithabstractauthenticationtokenandapplyjdbc.configs.security_config.jwt.JwtService;
import com.ttknp.understandspringsecuritywithabstractauthenticationtokenandapplyjdbc.entities.data.Student;
import com.ttknp.understandspringsecuritywithabstractauthenticationtokenandapplyjdbc.entities.login.LoginModel;
import com.ttknp.understandspringsecuritywithabstractauthenticationtokenandapplyjdbc.entities.login.LoginRequest;
import com.ttknp.understandspringsecuritywithabstractauthenticationtokenandapplyjdbc.entities.login.LoginResponse;
import com.ttknp.understandspringsecuritywithabstractauthenticationtokenandapplyjdbc.helpers.jwt.JwtSpringSecurityContextHelper;
import com.ttknp.understandspringsecuritywithabstractauthenticationtokenandapplyjdbc.services.UsefulService;
import com.ttknp.understandspringsecuritywithabstractauthenticationtokenandapplyjdbc.services.login.LoginModelService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
public class CompoundControls {

    private static final Logger log = LoggerFactory.getLogger(CompoundControls.class);
    private final JwtService jwtService;

    private final LoginModelService loginModelService;
    private final AuthenticationManager authenticationManager; // Optional it works as JwtSpringSecurityContextHelper

    @Autowired
    public CompoundControls(JwtService jwtService,
                            LoginModelService loginModelService,
                            AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.loginModelService = loginModelService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    private LoginResponse login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = new LoginResponse();
        LoginModel loginModel = loginModelService.getLoginModelByUsername(loginRequest.getUsername());
        log.debug("loginModel : {}", loginModel);
        if (loginModel != null) { // if found
            if (UsefulService.validatePasswordStringWithPasswordBase64(loginRequest.getPassword(), loginModel.getPassword())) { // check password string with password base64 from database
                log.debug("login : success");
                // generate token and set all details as claims,issue&expired token,...
                JwtBuilder jwtBuilder = jwtService.generateToken(null, loginModel);
                loginResponse.setToken(jwtBuilder.compact());
            } else {
                log.debug("login : failed");
            }
        }
        return loginResponse;
    }


    @GetMapping("/data-a")
    private String dataA() {
        return "data-a";
    }

    @PostMapping("/demo-login/reads")
    private List<Student> demoResponse(@RequestBody LoginRequest loginRequest) { // Internally Spring MVC uses a component called a HttpMessageConverter to convert the Http request to an object representation and back.
        return loginRequest.getUsername().equals("Admin") ?
                List.of(
                        new Student("Alex Slider", 19),
                        new Student("Jason Born", 20)
                ) :
                null;
    }

    @PostMapping("/demo-login/read")
    private Student demoResponse(@RequestBody Student student) {
        return student;
    }

    // do after logged in success
    @GetMapping("/check-claims")
    private Map<String, Object> checkClaims() throws IllegalAccessException {
        // Claims claims = JwtSpringSecurityContextHelper.getAuthenticationFromContext().getClaim();
        Claims claims = JwtSpringSecurityContextHelper.getClaim();
        log.debug("claims : {}", claims);
        return UsefulService.convertObjectToMap(claims);
    }

    @GetMapping("/check-is-token-expired")
    private Boolean checkToken() {
        // JwtAuthenticateToken jwtAuthenticateToken = JwtSpringSecurityContextHelper.getAuthenticationFromContext();
        Boolean isTokenExpired = JwtSpringSecurityContextHelper.isTokenExpired();
        log.debug("isTokenExpired : {}", isTokenExpired);
        return isTokenExpired;
    }

    @GetMapping("/check-expiration-date")
    private Date checkExpirationDate() {
        // JwtAuthenticateToken jwtAuthenticateToken = JwtSpringSecurityContextHelper.getAuthenticationFromContext();
        return JwtSpringSecurityContextHelper.getExpirationDate();
    }

    @GetMapping("/check-issue-date")
    private Date checkIssueDate() {
        // JwtAuthenticateToken jwtAuthenticateToken = JwtSpringSecurityContextHelper.getAuthenticationFromContext();
        return JwtSpringSecurityContextHelper.getIssueDate();
    }

    @GetMapping("/check-principle")
    private Object checkPrinciple(@RequestParam Character key) {
        JwtAuthenticateToken jwtAuthenticateToken = JwtSpringSecurityContextHelper.getAuthenticationFromContext();
        if (key.equals('A')) {
            return authenticationManager.authenticate(jwtAuthenticateToken).getPrincipal();
        } else if (key.equals('J')) {
            return JwtSpringSecurityContextHelper.getPrincipal();
        }
        return null;
    }

    /*@GetMapping("/check-is-username-valid")
    private Boolean checkUsername()  {
        JwtAuthenticateToken jwtAuthenticateToken = JwtSpringSecurityContextHelper.getAuthenticationFromContext();
        Boolean isUserValid = JwtSpringSecurityContextHelper.validateToken(jwtAuthenticateToken.getToken(), jwtAuthenticateToken.getClaim().getSubject());
        log.debug("isUserValid : {}", isUserValid);
        return isUserValid;
    }*/
}
