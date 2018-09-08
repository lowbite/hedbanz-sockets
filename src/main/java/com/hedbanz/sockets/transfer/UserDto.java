package com.hedbanz.sockets.transfer;

import java.sql.Timestamp;

public class UserDto {

    private Long id;
    private String login;
    private String password;
    private Integer money;
    private Long registrationDate;
    private Integer iconId;
    private String email;
    private String securityToken;
    private String fcmToken;

    public UserDto(){

    }

    private UserDto(Long id, String login, Integer money, Long registrationDate, Integer iconId, String email, String securityToken, String fcmToken) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.money = money;
        this.registrationDate = registrationDate;
        this.iconId = iconId;
        this.email = email;
        this.securityToken = securityToken;
        this.fcmToken = fcmToken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Long getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Long registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Integer getIconId() {
        return iconId;
    }

    public void setIconId(Integer iconId) {
        this.iconId = iconId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public static class Builder {
        private Long id;
        private String login;
        private Integer money;
        private Long registrationDate;
        private Integer iconId;
        private String email;
        private String token;
        private String fcmToken;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setLogin(String login) {
            this.login = login;
            return this;
        }

        public Builder setMoney(Integer money) {
            this.money = money;
            return this;
        }

        public Builder setIconId(Integer iconId) {
            this.iconId = iconId;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setRegistrationDate(Long registrationDate){
            this.registrationDate = registrationDate;
            return this;
        }

        public Builder setToken(String token) {
            this.token = token;
            return this;
        }

        public Builder setFcmToken(String fcmToken) {
            this.fcmToken = fcmToken;
            return this;
        }

        public UserDto build() {
            return new UserDto(id, login, money, registrationDate, iconId, email, token, fcmToken);
        }
    }
}
