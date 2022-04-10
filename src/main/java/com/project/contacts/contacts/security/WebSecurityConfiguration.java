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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
      http.cors();
      http.csrf().disable();

      http.authorizeRequests().antMatchers("/v3/api-docs",
            "/swagger-ui/**",
            "/swagger-resources",
            "/swagger-resources/configuration/ui",
            "/swagger-resources/configuration/security").permitAll();
      // http.authorizeRequests().antMatchers("/swagger-ui/**").permitAll();
      http.authorizeRequests().antMatchers("/users/signup/**").permitAll();
      http.authorizeRequests().antMatchers(HttpMethod.POST, "/users/signin").permitAll();
      http.authorizeRequests().antMatchers("/h2/**").permitAll();
      http.authorizeRequests().antMatchers("/password/**").permitAll();

      http.authorizeRequests().anyRequest()
            .authenticated();
      http.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

      http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
      http.headers().frameOptions().disable();

   }

   @Override
   @Bean
   protected AuthenticationManager authenticationManager() throws Exception {
      return super.authenticationManager();
   }

   @Bean
   public CorsConfigurationSource corsConfigurationSource() {
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      CorsConfiguration config = new CorsConfiguration();
      config.addAllowedOrigin("*");
      config.setAllowCredentials(true);
      config.addAllowedHeader("X-Requested-With");
      config.addAllowedHeader("Content-Type");
      config.addAllowedMethod(HttpMethod.POST);
      config.addAllowedMethod(HttpMethod.GET);
      config.addAllowedMethod(HttpMethod.PUT);
      config.addAllowedMethod(HttpMethod.DELETE);
      source.registerCorsConfiguration("/**", config);
      return source;
   }
}
