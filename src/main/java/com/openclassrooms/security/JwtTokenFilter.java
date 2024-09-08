package com.openclassrooms.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filter that checks the validity of JWT tokens in each request. This filter is
 * applied once per request.
 * It retrieves the token, validates it, and sets the authentication context if
 * the token is valid.
 */
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    /**
     * Constructor for JwtTokenFilter.
     *
     * @param jwtTokenProvider   A provider that generates and validates JWT tokens.
     * @param userDetailsService Service to load user details from the username in
     *                           the token.
     */
    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Filters incoming requests to check for a valid JWT token.
     * If a valid token is found, it sets the authentication in the security
     * context.
     *
     * @param request  The HTTP request object.
     * @param response The HTTP response object.
     * @param chain    The filter chain to continue the request.
     * @throws ServletException if there is a servlet-specific issue.
     * @throws IOException      if there is an input or output error during the
     *                          process.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Resolve JWT token from the request
        String token = jwtTokenProvider.resolveToken(request);

        if (token != null) {
            // Validate the token
            if (jwtTokenProvider.validateToken(token)) {
                // Retrieve username from the token
                String username = jwtTokenProvider.getUsername(token);

                // Load user details using the username
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Create an authentication token and set it in the SecurityContext
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authentication in the security context
                SecurityContextHolder.getContext().setAuthentication(authentication);

                logger.info("Authentication successful for user: " + username + " with token: " + token);
            } else {
                logger.info("Invalid JWT token: " + token);
            }
        } else {
            logger.info("No JWT token found in request headers");
        }

        // Continue with the filter chain
        chain.doFilter(request, response);
    }

}
