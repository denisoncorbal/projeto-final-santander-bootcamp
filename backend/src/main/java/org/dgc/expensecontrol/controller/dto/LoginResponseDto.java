package org.dgc.expensecontrol.controller.dto;

public class LoginResponseDto {
    private String accessToken;

    private String refreshToken;

    public LoginResponseDto() {
    }

    public LoginResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public LoginResponseDto(String... tokens){
        this.accessToken = tokens[0];
        this.refreshToken = tokens[1];
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    
}
