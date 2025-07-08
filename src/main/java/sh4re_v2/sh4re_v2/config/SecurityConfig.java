package sh4re_v2.sh4re_v2.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sh4re_v2.sh4re_v2.security.CustomAccessDeniedHandler;
import sh4re_v2.sh4re_v2.security.Jwt.JwtAuthenticationEntryPoint;
import sh4re_v2.sh4re_v2.security.Jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final SecurityPathConfig securityPathConfig;
  private final CustomAccessDeniedHandler accessDeniedHandler;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .exceptionHandling(exception -> exception
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .accessDeniedHandler(accessDeniedHandler)
        )
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .authorizeHttpRequests(authorize -> {
          securityPathConfig.getAuthenticatedEndpoints().forEach(endpoint -> {
            if (endpoint.getMethod() != null) {
              authorize.requestMatchers(endpoint.getMethod(), endpoint.getPattern()).authenticated();
            } else {
              authorize.requestMatchers(endpoint.getPattern()).authenticated();
            }
          });
          securityPathConfig.getPublicEndpoints().forEach(endpoint -> {
            if (endpoint.getMethod() != null) {
              authorize.requestMatchers(endpoint.getMethod(), endpoint.getPattern()).permitAll();
            } else {
              authorize.requestMatchers(endpoint.getPattern()).permitAll();
            }
          });
          authorize.requestMatchers("/test/teacher").hasRole("TEACHER").anyRequest().authenticated();
        });

    // Add JWT filter
    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
