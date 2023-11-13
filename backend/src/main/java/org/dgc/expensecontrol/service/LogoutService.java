package org.dgc.expensecontrol.service;

import java.io.IOException;

import org.dgc.expensecontrol.security.jwt.token.Token;
import org.dgc.expensecontrol.security.jwt.token.TokenRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class LogoutService implements LogoutHandler {

  private final TokenRepository tokenRepository;

  public LogoutService(TokenRepository tokenRepository) {
    this.tokenRepository = tokenRepository;
  }

  @Override
  public void logout(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication) {
    String jwt = "";
    try {
      jwt = request.getReader().readLine();
      jwt = jwt.substring(1, jwt.length() - 1);
    } catch (IOException e) {
      jwt = "";
    }

    Token storedToken = tokenRepository.findByToken(jwt)
        .orElse(null);

    if (storedToken != null) {
      storedToken.setExpired(true);
      storedToken.setRevoked(true);
      tokenRepository.save(storedToken);
    }
    SecurityContextHolder.clearContext();
  }
}