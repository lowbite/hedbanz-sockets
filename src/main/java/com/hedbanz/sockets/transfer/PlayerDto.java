package com.hedbanz.sockets.transfer;

import com.hedbanz.sockets.constant.PlayerStatus;

public class PlayerDto {
    private Long id;
    private String login;
    private String imagePath;
    private String word;
    private Integer attempt;
    private Integer status;
    private Boolean isFriend;
    private Boolean isPending;
    private Integer iconId;
    private Long userId;
    private Long wordSettingUserId;
    private Boolean isWinner;

    public PlayerDto(){}

    public PlayerDto(Long id, String login, String imagePath, String word, Integer attempt, PlayerStatus status){
        this.id = id;
        this.login = login;
        this.imagePath = imagePath;
        this.word = word;
        this.attempt = attempt;
        this.status = status.getCode();
    }

    private PlayerDto(Long id, String login, String imagePath, String word, Integer attempt, Integer status,
                      Boolean isFriend, Boolean isPending, Integer iconId, Long userId, Long wordSettingUserId, Boolean isWinner){
        this.id = id;
        this.login = login;
        this.imagePath = imagePath;
        this.word = word;
        this.attempt = attempt;
        this.status = status;
        this.isFriend = isFriend;
        this.isPending = isPending;
        this.iconId = iconId;
        this.userId = userId;
        this.wordSettingUserId = wordSettingUserId;
        this.isWinner = isWinner;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getAttempt() {
        return attempt;
    }

    public void setAttempt(Integer attempt) {
        this.attempt = attempt;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer AFK) {
        status = AFK;
    }

    public Boolean getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(Boolean friend) {
        isFriend = friend;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getWordSettingUserId() {
        return wordSettingUserId;
    }

    public void setWordSettingUserId(Long wordSettingUserId) {
        this.wordSettingUserId = wordSettingUserId;
    }

    public Boolean getIsPending() {
        return isPending;
    }

    public void setIsPending(Boolean pending) {
        isPending = pending;
    }

    public Boolean isWinner() {
        return isWinner;
    }

    public void setIsWinner(Boolean winner) {
        isWinner = winner;
    }

    public Integer getIconId() {
        return iconId;
    }

    public void setIconId(Integer iconId) {
        this.iconId = iconId;
    }

    @Override
    public String toString() {
        return "PlayerDto{" +
                "login='" + login + '\'' +
                '}';
    }

    public static class PlayerDTOBuilder {
        private Long id;
        private String login;
        private String imagePath;
        private String word;
        private Integer attempt;
        private Integer status;
        private Boolean isFriend;
        private Boolean isPending;
        private Long userId;
        private Long wordSettingUserId;
        private Boolean isWinner;
        private Integer iconId;

        public PlayerDTOBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public PlayerDTOBuilder setLogin(String login) {
            this.login = login;
            return this;
        }

        public PlayerDTOBuilder setImagePath(String imagePath) {
            this.imagePath = imagePath;
            return this;
        }

        public PlayerDTOBuilder setWord(String word) {
            this.word = word;
            return this;
        }

        public PlayerDTOBuilder setAttempt(Integer attempt) {
            this.attempt = attempt;
            return this;
        }

        public PlayerDTOBuilder setStatus(Integer status) {
            this.status = status;
            return this;
        }

        public PlayerDTOBuilder setIsFriend(Boolean isFriend) {
            this.isFriend = isFriend;
            return this;
        }

        public PlayerDTOBuilder setUserId(Long userId){
            this.userId = userId;
            return this;
        }

        public PlayerDTOBuilder setWordSettingUserId(Long wordSettingUserId){
            this.wordSettingUserId = wordSettingUserId;
            return this;
        }

        public PlayerDTOBuilder setIsWinner(Boolean isWinner){
            this.isWinner = isWinner;
            return this;
        }

        public PlayerDTOBuilder setIsPending(Boolean isPending){
            this.isPending = isPending;
            return this;
        }

        public PlayerDTOBuilder setIconId(Integer iconId) {
            this.iconId = iconId;
            return this;
        }

        public PlayerDto createPlayerDTO() {
            return new PlayerDto(id, login, imagePath, word, attempt, status, isFriend, isPending, iconId, userId, wordSettingUserId, isWinner);
        }
    }
}
