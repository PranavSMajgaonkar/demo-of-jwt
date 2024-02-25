package com.sample.token.security;

import java.io.IOException;

import com.sample.token.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;
//    @Autowired
//    AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (requestTokenHeader == null || requestTokenHeader == "" || !requestTokenHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String jwtToken = requestTokenHeader.split(" ")[1].trim();
        String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
        UserDetails userValueObject = userService.loadUserByUsername(username);

        if (jwtTokenUtil.validateToken(jwtToken, userValueObject)) {

            Authentication usernamePasswordAuthenticationToken
                    = new UsernamePasswordAuthenticationToken(
                    userValueObject, userValueObject, userValueObject.getAuthorities());
//            usernamePasswordAuthenticationToken
//                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
        chain.doFilter(request, response);
    }
}
