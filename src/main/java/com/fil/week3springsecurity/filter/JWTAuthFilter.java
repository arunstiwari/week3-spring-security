package com.fil.week3springsecurity.filter;

import com.fil.week3springsecurity.util.JWTUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    private JWTUtil jwtUtil;

    public JWTAuthFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String jwtToken = authHeader.substring(7);
            String username = jwtUtil.extractUserName(jwtToken);

            if (username != null  && SecurityContextHolder.getContext().getAuthentication() == null) {
                //Extract the claims
                Claims claims = jwtUtil.parseClaimValue(jwtToken);
                // extract roles from the token
                List<String> roles = claims.get("roles", List.class);

                //Build granted authority from the extracted roles
//                List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
//                for (String role : roles) {
//                    grantedAuthorities.add(new SimpleGrantedAuthority(role));
//                }

                var grantedAuthorities = roles.stream().map(SimpleGrantedAuthority::new).toList();

                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        username, null, grantedAuthorities);
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(token);

            }

        }
        filterChain.doFilter(request, response);
    }
}
