/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cmc.configs;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.cmc.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

/**
 *
 * @author FPTSHOP
 */
@Configuration
@EnableWebSecurity
@EnableTransactionManagement
@ComponentScan(basePackages = {
    "com.cmc.controllers",
    "com.cmc.repository",
    "com.cmc.service",
    "com.cmc.configs",
    "com.cmc.components"
})
@Order(2)
public class SpringSecurityConfig {

    @Autowired
    private Environment env;
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }   
            
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/admin/login", "/admin/register").permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/api/**").permitAll()
                    .requestMatchers("/js/**").permitAll()
                    .anyRequest().authenticated()
                )
                .formLogin(form -> form
                    .loginPage("/admin/login")
                    .loginProcessingUrl("/admin/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/admin/dashboard", true)
                    .failureUrl("/admin/login?error")
                    .permitAll()
                )
                .logout(logout -> logout
                    .logoutUrl("/admin/logout") 
                    .logoutSuccessUrl("/admin/login?logout=true")
                    .deleteCookies("JSESSIONID")
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .permitAll()
                )
//                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception
                    .accessDeniedPage("/403")
                );

        return http.build();
    }
    
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", env.getProperty("cloudinary.cloud_name"),
                "api_key", env.getProperty("cloudinary.api_key"),
                "api_secret", env.getProperty("cloudinary.api_secret"),
                "secure", true
        ));
    }

}
