package com.project.contacts.contacts.security.Filters;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.project.contacts.contacts.Utilities.JWTUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    JWTUtils jwt;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        String token = "";
        System.out.println("*******************");
        System.out.println(header);
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;

        }
        // now authenticate the token and set securityContext
        else {
            token = header.substring(7);

            String email = jwt.getUsernameFromToken(token);
            if (email == null) {
                chain.doFilter(request, response);
                return;
            }
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(email, null,
                    new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(auth);
            chain.doFilter(request, response);
        }

    }

}
