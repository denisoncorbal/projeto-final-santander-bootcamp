package org.dgc.expensecontrol.controller;

import org.dgc.expensecontrol.controller.dto.LoginRequestDto;
import org.dgc.expensecontrol.controller.dto.LoginResponseDto;
import org.dgc.expensecontrol.controller.dto.RefreshRequestDto;
import org.dgc.expensecontrol.security.jwt.JwtService;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.lang.JoseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/auth")
public class LoginController {

    private UserDetailsService userDetailsService;
    private JwtService jwtService;

    public LoginController(UserDetailsService userDetailsService, JwtService jwtService) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto user) throws JoseException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        LoginResponseDto res = new LoginResponseDto(jwtService.generateToken(userDetails),
                jwtService.generateRefreshToken(userDetails));
        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(@RequestBody RefreshRequestDto tokens)
            throws UsernameNotFoundException, InvalidJwtException, JoseException {
        UserDetails userDetails = userDetailsService
                .loadUserByUsername(jwtService.extractUsername(tokens.getRefreshToken()));
        
        if (jwtService.isTokenValid(tokens.getRefreshToken(), userDetails)) {
            LoginResponseDto res = new LoginResponseDto(jwtService.generateToken(userDetails),
                    jwtService.generateRefreshToken(userDetails));
            return ResponseEntity.ok().body(res);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new LoginResponseDto());
    }
}
