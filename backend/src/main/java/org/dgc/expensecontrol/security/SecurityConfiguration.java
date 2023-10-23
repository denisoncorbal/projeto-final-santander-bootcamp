package org.dgc.expensecontrol.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Supplier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	@Bean
	SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {		
		httpSecurity
				.csrf((csrf) -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler()))
				.cors(Customizer.withDefaults())
				.headers(headers -> headers.frameOptions(Customizer.withDefaults()).disable())
				.authorizeHttpRequests(
						(authorizeHttpRequests) -> authorizeHttpRequests.anyRequest().permitAll())
				.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return httpSecurity.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedHeaders(Arrays.asList("Content-Type", "X-XSRF-TOKEN"));
		configuration.setAllowedMethods(Arrays.asList("OPTIONS", "GET", "PUT", "POST", "DELETE"));
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://localhost:8080", "http://127.0.0.1:8080", "127.0.0.1:8080", "http://expensecontrol-omptr9n0.b4a.run", "https://expensecontrol-omptr9n0.b4a.run", "http://expensecontrol-omptr9n0.b4a.run:8080", "https://expensecontrol-omptr9n0.b4a.run:8080"));
		configuration.setExposedHeaders(Arrays.asList("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	final class SpaCsrfTokenRequestHandler extends CsrfTokenRequestAttributeHandler{
		private final CsrfTokenRequestHandler delegate = new XorCsrfTokenRequestAttributeHandler();

		@Override
		public void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> csrfToken){
			this.delegate.handle(request, response, csrfToken);
		}

		@Override
		public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken){
			if(StringUtils.hasText(request.getHeader(csrfToken.getHeaderName()))){
				return super.resolveCsrfTokenValue(request, csrfToken);
			}
			return this.delegate.resolveCsrfTokenValue(request, csrfToken);
		}
	}

	final class CsrfTokenFilter extends OncePerRequestFilter{
		@Override
		protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
			CsrfToken csrfToken = new DefaultCsrfToken("X-XSRF-TOKEN", "_csrf",request.getHeader("X-XSRF-TOKEN"));
			csrfToken.getToken();
			filterChain.doFilter(request, response);
		}
	}
}
