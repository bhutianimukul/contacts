package com.project.contacts.contacts.security;

import com.project.contacts.contacts.security.Filters.AuthorizationFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
   @Autowired
   UserDetailsService usersService;

   @Autowired
   BCryptPasswordEncoder bcrypt;
   @Autowired
   AuthorizationFilter authFilter;

   @Override
   protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.userDetailsService(usersService).passwordEncoder(bcrypt);
   }

   @Override
   protected void configure(HttpSecurity http) throws Exception {
      http.csrf().disable();

      http.authorizeRequests().antMatchers(HttpMethod.POST, "/users/signup").permitAll();
      http.authorizeRequests().antMatchers(HttpMethod.POST, "/users/signin").permitAll();
      http.authorizeRequests().antMatchers("/h2/**").permitAll();

      http.authorizeRequests().anyRequest()
            .authenticated();
      http.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
      http.headers().frameOptions().disable();

   }

   @Override
   @Bean
   protected AuthenticationManager authenticationManager() throws Exception {
      return super.authenticationManager();
   }

}
