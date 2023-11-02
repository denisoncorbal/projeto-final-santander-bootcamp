package org.dgc.expensecontrol.security.jwt;

import java.io.IOException;

import org.dgc.expensecontrol.security.jwt.token.TokenRepository;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final TokenRepository tokenRepository;

  public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService,
      TokenRepository tokenRepository) {
    this.jwtService = jwtService;
    this.userDetailsService = userDetailsService;
    this.tokenRepository = tokenRepository;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    if (request.getServletPath().contains("/api/v1/auth")) {
      filterChain.doFilter(request, response);
      return;
    }
    final String authHeader = request.getHeader("Authorization");
    System.out.println(authHeader);
    final String jwt;    
    final String userEmail;
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      System.out.println("### sem token ###");
      filterChain.doFilter(request, response);
      return;
    }
    jwt = authHeader.substring(7);
    System.out.println(jwt);
    try {
      userEmail = jwtService.extractUsername(jwt);
      System.out.println(userEmail);
    } catch (InvalidJwtException e) {
      System.out.println("não extraiu email");
      throw new ServletException(e.getMessage());
    }
    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      System.out.println("tentar autenticar");
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
      var isTokenValid = tokenRepository.findByToken(jwt)
          .map(t -> !t.isExpired() && !t.isRevoked())
          .orElse(false);
      boolean isValid;
      System.out.println("Token não expirado: " + isTokenValid);
      try {
        isValid = jwtService.isTokenValid(jwt, userDetails);
      } catch (InvalidJwtException e) {
        // TODO Auto-generated catch block
        throw new ServletException(e.getMessage());
      }
      System.out.println("Token não invalido: " + isValid);
      if (isValid && isTokenValid) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities());
        authToken.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        System.out.println("Autenticou");
      }
    }
    filterChain.doFilter(request, response);
  }
}