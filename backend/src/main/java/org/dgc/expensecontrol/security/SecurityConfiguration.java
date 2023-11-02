package org.dgc.expensecontrol.security;

import static org.dgc.expensecontrol.security.jwt.role.Role.ADMIN;
import static org.dgc.expensecontrol.security.jwt.role.Role.MANAGER;

import java.util.Arrays;

import org.dgc.expensecontrol.security.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	private static final String[] WHITE_LIST_URL = {
			"/api/v1/auth/**",
			"/v2/api-docs",
			"/v3/api-docs",
			"/v3/api-docs/**",
			"/swagger-resources",
			"/swagger-resources/**",
			"/configuration/ui",
			"/configuration/security",
			"/swagger-ui/**",
			"/webjars/**",
			"/swagger-ui.html",
			"/actuator/health",
			"/error"
	};

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final LogoutHandler logoutHandler;
	private final AuthenticationProvider authenticationProvider;

	public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter,
			LogoutHandler logoutHandler,
			AuthenticationProvider authenticationProvider){
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
		this.logoutHandler = logoutHandler;
		this.authenticationProvider = authenticationProvider;
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				.csrf((csrf) -> csrf.disable())
				.cors(Customizer.withDefaults())
				.headers(headers -> headers.frameOptions(Customizer.withDefaults()).disable())
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.logout((logout) -> logout.logoutUrl("/api/v1/auth/logout").addLogoutHandler(logoutHandler)
						.logoutSuccessHandler(
								(request, response, authentication) -> SecurityContextHolder.clearContext()))
				.authorizeHttpRequests(
						(authorizeHttpRequests) -> authorizeHttpRequests
								.requestMatchers(WHITE_LIST_URL).permitAll()
								.requestMatchers(HttpMethod.GET, "/api/v1/user").hasAnyRole(ADMIN.name(), MANAGER.name())
								.requestMatchers(HttpMethod.PUT, "/api/v1/user/**").hasAnyRole(ADMIN.name(), MANAGER.name())
								.requestMatchers(HttpMethod.DELETE, "/api/v1/user/**").hasAnyRole(ADMIN.name(), MANAGER.name())
								.anyRequest().authenticated())
				.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return httpSecurity.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedHeaders(Arrays.asList("Content-Type", "X-XSRF-TOKEN"));
		configuration.setAllowedMethods(Arrays.asList("OPTIONS", "GET", "PUT", "POST", "DELETE"));
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://localhost:8080",
				"http://127.0.0.1:8080", "127.0.0.1:8080", "http://expensecontrol-omptr9n0.b4a.run",
				"https://expensecontrol-omptr9n0.b4a.run", "http://expensecontrol-omptr9n0.b4a.run:8080",
				"https://expensecontrol-omptr9n0.b4a.run:8080"));
		configuration
				.setExposedHeaders(Arrays.asList("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}	
}
