package com.ecommerce.book_store.core.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final UserDetailsService userDetailsService;
    private final UserDetailsService adminDetailsService;

    public SecurityConfiguration(
            @Qualifier("userDetailsService") UserDetailsService userDetailsService,
            @Qualifier("adminDetailsService") UserDetailsService adminDetailsService
    ) {
        this.userDetailsService = userDetailsService;
        this.adminDetailsService = adminDetailsService;
    }

    @Bean
    public SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/user/**", "/login")
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/user/**").hasAuthority("ROLE_USER")
                        .requestMatchers("/login").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/user/dashboard")
                        .failureUrl("/login?error=true")
                )
                .authenticationProvider(userAuthenticationProvider());

        return http.build();
    }

//    @Bean
//    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .securityMatcher("/admin/**", "/login/admin")
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
//                        .requestMatchers("/login/admin").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .formLogin(form -> form
//                        .loginPage("/login/admin")
//                        .loginProcessingUrl("/admin/login")
//                        .defaultSuccessUrl("/admin/dashboard")
//                        .failureUrl("/login/admin?error=true")
//                )
//                .authenticationProvider(adminAuthenticationProvider());
//
//        return http.build();
//    }

    @Bean
    public AuthenticationProvider userAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationProvider adminAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(adminDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}