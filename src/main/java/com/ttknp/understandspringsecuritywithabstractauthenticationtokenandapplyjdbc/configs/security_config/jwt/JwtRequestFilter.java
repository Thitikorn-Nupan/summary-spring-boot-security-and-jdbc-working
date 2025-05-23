package com.ttknp.understandspringsecuritywithabstractauthenticationtokenandapplyjdbc.configs.security_config.jwt;


import com.ttknp.understandspringsecuritywithabstractauthenticationtokenandapplyjdbc.configs.security_config.SecurityConfig;
import com.ttknp.understandspringsecuritywithabstractauthenticationtokenandapplyjdbc.entities.login.LoginModel;
import com.ttknp.understandspringsecuritywithabstractauthenticationtokenandapplyjdbc.services.UsefulService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
    The JwtRequestFilter extends the Spring Web Filter OncePerRequestFilter class.
    For any incoming request this Filter class gets executed.
    It checks if the request has a valid JWT token.
    If it has a valid JWT Token then it sets the Authentication in the context,
    to specify that the current user is authenticated.
*/
// Here, this filter class extends the OncePerRequestFilter class to guarantee(v. รับประกัน) a single execution per request.
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtRequestFilter.class);
    private final JwtService jwtService;

    @Autowired
    public JwtRequestFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    // do every time in security's api
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String token = request.getHeader("Authorization");
        Map<String,Object> user = null;
        Claims claims = null;

        if (request.getRequestURL().toString().contains("/api/login")) { // case login
            chain.doFilter(request, response);
            return;
        }


        if (token == null || !token.startsWith("Bearer ")) {
            StringBuilder stringBuilder = getErrorStringBuilder(request, new RuntimeException(),401 ,"Error authenticating token");
            response.setStatus(401);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter()
                    .print(stringBuilder);
            response.getWriter()
                    .flush();
            // chain.doFilter(request, response);
            return;
        }

        else {
            // token = token.split(" ")[1]; same
            token = token.substring(7); // split Bearer
            try {
                claims = jwtService.getClaimsFromToken(token);
                // log.debug("claims: {}", claims.get("user")); // {uid=59aef92e-3d50-45d2-bdfb-19051c6df16f, password=MQ==, createBy=Admin, role=ADMIN, email=Admin@hotmail.com, username=Admin}
                user  = UsefulService.convertObjectToMap(claims.get("user"));
                log.debug("user {}",user);
            } catch (IllegalAccessException | SignatureException e) {
                StringBuilder stringBuilder = getErrorStringBuilder(request, e,403 ,"Error while parsing token");
                response.setStatus(403);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter()
                        .print(stringBuilder);
                response.getWriter()
                        .flush();
                return;
            }

        }

        String uuid = (String) user.get("uuid");
        String username = (String) user.get("username");
        String email = (String) user.get("email");
        String role = (String) user.get("role");
        String createBy = (String) user.get("createBy");
        LoginModel loginModel = new LoginModel();
        loginModel.setUuid(UsefulService.convertStringToUuid(uuid));
        loginModel.setUsername(username);
        loginModel.setEmail(email);
        loginModel.setRole(role);
        loginModel.setCreateBy(createBy);

        log.debug("authenticating user {}", loginModel);

        // *** JwtAuthenticateToken very useful with abs clas
        // abs class work will be a data after SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticateToken(loginModel,null,claims,token));
        // JwtAuthenticateToken authentication = new JwtAuthenticateToken(loginModel,null,claims,token);
        // can reduce
        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticateToken(loginModel,null,claims,token));
        chain.doFilter(request, response); // continute req/res

        if ( SecurityContextHolder.getContext().getAuthentication() == null ) {
            log.debug("*** JwtAuthenticateToken is null");
        } else {
            log.debug("*** JwtAuthenticateToken isn't null");
        }
    }

    private static StringBuilder getErrorStringBuilder(HttpServletRequest request, Exception e,int status , String message) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"requestURL\": \"" + request.getRequestURL().toString() + "\",");
        stringBuilder.append("\"requestMethod\": \"" + request.getMethod() + "\",");
        stringBuilder.append("\"errorMessage\": \"" + e.getMessage() + "\",");
        stringBuilder.append("\"errorClassName\": \"" + e.getClass().getName() + "\",");
        stringBuilder.append("\"status\":" + status + ",");
        stringBuilder.append("\"message\": \"" + message + "\"");
        stringBuilder.append("}");
        return stringBuilder;
    }

}