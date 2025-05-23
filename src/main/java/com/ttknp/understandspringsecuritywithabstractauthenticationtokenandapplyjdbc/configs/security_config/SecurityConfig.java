package com.ttknp.understandspringsecuritywithabstractauthenticationtokenandapplyjdbc.configs.security_config;

import com.ttknp.understandspringsecuritywithabstractauthenticationtokenandapplyjdbc.configs.security_config.jwt.JwtRequestFilter;
import com.ttknp.understandspringsecuritywithabstractauthenticationtokenandapplyjdbc.entities.login.LoginModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
    private final JwtRequestFilter jwtRequestFilter;
    private final CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    public SecurityConfig(JwtRequestFilter jwtRequestFilter, CustomAuthenticationProvider customAuthenticationProvider) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.customAuthenticationProvider = customAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        log.info("Configuring SecurityFilterChain");
        // work after run auth micro
        httpSecurity
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity
                .csrf()
                .disable();
        // all req authen
        httpSecurity.authorizeHttpRequests((authorizationManagerRequestMatcherRegistry) -> {
            authorizationManagerRequestMatcherRegistry.requestMatchers("/api/login").permitAll();
            authorizationManagerRequestMatcherRegistry.anyRequest().authenticated();
        }).httpBasic();
        // Add a filter to validate the tokens with every request
        httpSecurity.addFilterBefore(this.jwtRequestFilter, BasicAuthenticationFilter.class);
        return httpSecurity.build();
    }

    /**
       ** Optional it's just working for get claims
       For Web Application specific authentication and authorization process I will come up with another article.
       AuthenticationManager It is a core interface that spring security uses for the authentication process.
       It has only one method authenticate which when implemented in a class that implements an Authentication Manager has all the logic for authenticating a user request.
       Spring security has a default implementation for Authentication Manager that is “ProviderManager”
    */
    @Bean
    public AuthenticationManager authManager(HttpSecurity httpSecurity) throws Exception {
        log.info("Configuring AuthenticationManager");
        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider);
        return authenticationManagerBuilder.build();
    }

    @Component
    public static class CustomAuthenticationProvider implements AuthenticationProvider {

        private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

        // it will do if you inject AuthenticationManager class
        @Override
        public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
            // log.debug("authenticating user getPrincipal {}", authentication.getPrincipal()); // LoginModel{uuid='59aef92e-3d50-45d2-bdfb-19051c6df16f', createBy='null', createDatetime=null, modifyDatetime=null, username='Admin', password='null', email='Admin@hotmail.com', role='ADMIN'}
            LoginModel loginModel = (LoginModel) authentication.getPrincipal();
            if (loginModel!=null && loginModel.getRole()!=null) {
                log.debug("Authenticating user: {}" , loginModel.getUsername());
                return authentication;
            }
            return null;
        }

        @Override
        public boolean supports(Class<?> authentication) {
            return AbstractAuthenticationToken.class.isAssignableFrom(authentication);
        }

    }

}
