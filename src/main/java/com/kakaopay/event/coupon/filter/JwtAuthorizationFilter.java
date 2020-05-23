package com.kakaopay.event.coupon.filter;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kakaopay.event.coupon.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final String tokenHeader = "Authorization";
    private final String tokenPrefix = "Bearer ";
    private final JwtUtil jwtUtil;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(tokenHeader);

        if (header == null || !header.startsWith(tokenPrefix)) {
            chain.doFilter(request, response);
        } else {
            SecurityContextHolder.getContext().setAuthentication(getUsernamePasswordAuthentication(request));
            chain.doFilter(request, response);
        }
    }

    private Authentication getUsernamePasswordAuthentication(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        if (token == null) {
            return null;
        }
        try {
            DecodedJWT result = jwtUtil.decode(token.replace(tokenPrefix, ""));
            String username = result.getSubject();
            if (username == null) {
                return null;
            }
            return new UsernamePasswordAuthenticationToken(username, result.getPayload(), new ArrayList<>());
        } catch (SignatureVerificationException e2) {
            logger.warn(String.format("%s, Token=%s", e2.getMessage(), token));
        } catch (TokenExpiredException e) {
            return null;
        }
        return null;
    }
}
