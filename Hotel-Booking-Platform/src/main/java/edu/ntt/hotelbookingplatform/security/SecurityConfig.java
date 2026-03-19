package edu.ntt.hotelbookingplatform.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JWTAuthFilter jwtAuthFilter;

    public SecurityConfig(JWTAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/api-docs/**"
                        ).permitAll()

                        .requestMatchers("/api/auth/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/user/").hasRole("Admin")
                        .requestMatchers(HttpMethod.DELETE, "/api/user/**").hasRole("Admin")
                        .requestMatchers(HttpMethod.PATCH, "/api/user/changeRole").hasRole("Admin")
                        .requestMatchers(HttpMethod.PATCH, "/api/user/changeRole").hasRole("Admin")
                        .requestMatchers(HttpMethod.PATCH, "/api/user/changeName").hasAnyRole("Admin", "Customer")
                        .requestMatchers(HttpMethod.PATCH, "/api/user/changePassword").hasAnyRole("Admin", "Customer")
                        .requestMatchers(HttpMethod.GET, "/api/user/**").hasAnyRole("Admin", "Customer")



                        //Room
                        .requestMatchers(HttpMethod.POST, "/api/rooms/**").hasRole("Admin")
                        .requestMatchers(HttpMethod.PUT, "/api/rooms/**").hasRole("Admin")
                        .requestMatchers(HttpMethod.DELETE, "/api/rooms/**").hasRole("Admin")
                        .requestMatchers(HttpMethod.GET, "/api/rooms/**")
                        .hasAnyRole("Admin","Customer")


                        .anyRequest().authenticated()
                )

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}