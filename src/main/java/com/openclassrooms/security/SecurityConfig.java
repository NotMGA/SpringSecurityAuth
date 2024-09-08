package com.openclassrooms.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Configuration class for Spring Security.
 * This class defines the security settings for the application, including
 * authentication, authorization, JWT token handling, and password encoding.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Swagger-related URL patterns that are allowed without authentication
    private static final String[] WHITE_LIST_SWAGGER_URL = {
            "/api-docs",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/swagger-ui/index.html",
            "/swagger-ui/index.html/**"
    };

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    /**
     * Constructor to initialize JwtTokenProvider and UserDetailsService.
     *
     * @param jwtTokenProvider   JWT token provider for token creation and
     *                           validation.
     * @param userDetailsService Service for loading user-specific data.
     */
    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Configures the security filter chain, including the endpoints that require
     * authentication and those that do not.
     * 
     * @param http The HttpSecurity object to configure.
     * @return The configured SecurityFilterChain.
     * @throws Exception if an error occurs during the configuration process.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF protection (not needed for stateless REST APIs)
                .csrf().disable()
                // Authorization configuration
                .authorizeRequests()
                // Public endpoints (login and registration)
                .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                // Secured endpoints that require authentication
                .requestMatchers("/api/auth/me").authenticated()
                .requestMatchers("/api/rentals/**").authenticated()
                .requestMatchers("/api/messages/**").authenticated()
                // Swagger endpoints that are publicly accessible
                .requestMatchers(WHITE_LIST_SWAGGER_URL).permitAll()
                // Any other requests must be authenticated
                .anyRequest().authenticated()
                .and()
                // Session management set to stateless (no sessions)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // Adding the JWT token filter before the username-password authentication
                // filter
                .addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Bean definition for the JwtTokenFilter. This filter intercepts requests and
     * validates JWT tokens.
     *
     * @return A JwtTokenFilter instance.
     */
    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter(jwtTokenProvider, userDetailsService);
    }

    /**
     * Bean definition for the AuthenticationManager. This manager handles
     * authentication requests.
     *
     * @param authenticationConfiguration Configuration for the authentication
     *                                    manager.
     * @return An AuthenticationManager instance.
     * @throws Exception if an error occurs during the configuration process.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Bean definition for the PasswordEncoder. Uses BCrypt for hashing passwords.
     *
     * @return A BCryptPasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
