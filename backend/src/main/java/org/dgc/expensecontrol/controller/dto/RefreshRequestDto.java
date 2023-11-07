package org.dgc.expensecontrol.controller.dto;

public class RefreshRequestDto {
    private String accessToken;
    private String refreshToken;
    public RefreshRequestDto() {
    }
    public RefreshRequestDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
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
