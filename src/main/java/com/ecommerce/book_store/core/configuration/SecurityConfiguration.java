package com.ecommerce.book_store.core.configuration;

import com.ecommerce.book_store.core.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final UserDetailsService userDetailsService;
    private final UserDetailsService adminDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(
            @Qualifier("userDetailsService") UserDetailsService userDetailsService,
            @Qualifier("adminDetailsService") UserDetailsService adminDetailsService,
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        this.userDetailsService = userDetailsService;
        this.adminDetailsService = adminDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Cấu hình CORS
                .csrf(AbstractHttpConfigurer::disable) // Tắt CSRF vì API dùng JWT
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").authenticated()
                        .requestMatchers("/admin/**").hasRole("ADMIN")  // Admin phải có role ADMIN
                        .anyRequest().permitAll() // Các request khác không cần xác thực
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)) // Giữ session-based cho admin
                .formLogin(Customizer.withDefaults()) // Giữ login form cho admin
                .httpBasic(Customizer.withDefaults()) // Giữ Basic Auth cho API
                .oauth2Login(Customizer.withDefaults()) // Giữ đăng nhập Google, Facebook
                .authenticationProvider(userAuthenticationProvider()) // Xác thực user qua database
                .authenticationProvider(adminAuthenticationProvider()) // Xác thực admin qua database
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Thêm JWT filter

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:5174", "https://book-store-react-beta.vercel.app")); // Frontend URLs
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Phương thức API cho phép
        configuration.setAllowedHeaders(List.of("*")); // Chấp nhận tất cả headers
        configuration.setAllowCredentials(true); // Cho phép gửi credentials (JWT, session)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

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

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(List.of(userAuthenticationProvider(), adminAuthenticationProvider()));
    }
}
