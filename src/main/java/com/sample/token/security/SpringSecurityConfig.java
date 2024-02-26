package com.sample.token.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JWTRequestFilter jwtRequestFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception{
        final List<GlobalAuthenticationConfigurerAdapter> configures = new ArrayList<>();
        configures.add(new GlobalAuthenticationConfigurerAdapter() {
            @Override
            public void configure(AuthenticationManagerBuilder auth) throws Exception {
                auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
            }
        });
        return authConfig.getAuthenticationManager();
    }
    private void sharedSecurityConfiguration(HttpSecurity httpSecurity) throws Exception{
    httpSecurity.cors(c -> c.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(httpSecuritySessionManagementConfigurer -> {
                    httpSecuritySessionManagementConfigurer.sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS);
            });
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        sharedSecurityConfiguration(httpSecurity);
        httpSecurity.securityMatcher("admin").authorizeHttpRequests(auth -> {
            auth.anyRequest().permitAll();//.hasAnyRole("ADMIN","USER");
        }).addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
    //this is for the admin access, any request can access.
    @Bean
    public SecurityFilterChain securityFilterChainAdmin(HttpSecurity httpSecurity) throws Exception{
        sharedSecurityConfiguration(httpSecurity);
        httpSecurity.securityMatcher("admin/user-list").authorizeHttpRequests(auth -> {
            auth.anyRequest().hasAuthority("ADMIN");
        }).addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
    //this filter for user profile API
    @Bean
    public SecurityFilterChain securityFilterChainProfile(HttpSecurity httpSecurity) throws Exception{
        sharedSecurityConfiguration(httpSecurity);
        httpSecurity.securityMatcher("/user/profile").authorizeHttpRequests(auth -> {
            auth.anyRequest().hasAnyAuthority("ADMIN","USER");
        }).addFilterBefore(jwtRequestFilter,UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
    //this is for login page and registration page
    @Bean
    public SecurityFilterChain securityFilterChainRegistrationAPI(HttpSecurity httpSecurity) throws  Exception{
        sharedSecurityConfiguration(httpSecurity);
        httpSecurity.securityMatcher("/user/register", "/user/authenticated")
                .authorizeHttpRequests(auth -> {
                    auth.anyRequest().permitAll();
                }).addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
     final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));

        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);
        return source;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
