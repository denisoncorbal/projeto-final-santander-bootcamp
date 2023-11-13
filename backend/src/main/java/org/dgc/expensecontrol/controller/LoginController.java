package org.dgc.expensecontrol.controller;

import org.dgc.expensecontrol.controller.dto.LoginRequestDto;
import org.dgc.expensecontrol.controller.dto.LoginResponseDto;
import org.dgc.expensecontrol.controller.dto.RefreshRequestDto;
import org.dgc.expensecontrol.security.jwt.JwtService;
import org.dgc.expensecontrol.security.jwt.token.TokenType;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.lang.JoseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/auth")
public class LoginController {

    private JwtService jwtService;

    public LoginController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto user) throws JoseException {
        return ResponseEntity.ok().body(new LoginResponseDto(jwtService.login(user.getEmail())));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(@RequestBody RefreshRequestDto tokens)
            throws UsernameNotFoundException, InvalidJwtException, JoseException {
//TODO só criar access e não novo refresh
        if (jwtService.isTokenValid(tokens.getRefreshToken(), TokenType.REFRESH)) {
            return ResponseEntity.ok().body(new LoginResponseDto(jwtService.refresh(tokens.getRefreshToken())));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new LoginResponseDto());

    }
}
