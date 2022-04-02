package com.project.contacts.contacts.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter  {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
 super.configure(auth);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http.csrf().disable().
       authorizeRequests().antMatchers(HttpMethod.POST, "/users/**").permitAll();
       http.authorizeRequests().antMatchers("/h2/**").permitAll();
       ;
       http.authorizeRequests().anyRequest()
                .authenticated();
                http.headers().frameOptions().disable();
        
    }
    



}
