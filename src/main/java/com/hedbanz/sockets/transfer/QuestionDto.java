package com.hedbanz.sockets.transfer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hedbanz.sockets.constant.MessageType;
import com.hedbanz.sockets.deserializer.QuestionDtoDeserializer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonDeserialize(using = QuestionDtoDeserializer.class)
public class QuestionDto extends MessageDto{
    private Long questionId;
    private List<PlayerDto> yesVoters = new ArrayList<>();
    private List<PlayerDto> noVoters = new ArrayList<>();
    private List<PlayerDto> winVoters = new ArrayList<>();
    private Integer attempt;
    private Integer vote;

    public QuestionDto() {
    }

    private QuestionDto(Long questionId, Long clientMessageId, UserDto senderUser, Long roomId, String text,
                        Integer type, Date createDate, List<PlayerDto> yesVoters, List<PlayerDto> noVoters, List<PlayerDto> winVoters, Integer attempt, Integer vote, String securityToken) {
        super(clientMessageId, senderUser, roomId, text, type, createDate, securityToken);
        this.questionId = questionId;
        this.yesVoters = yesVoters;
        this.noVoters = noVoters;
        this.winVoters = winVoters;
        this.attempt = attempt;
        this.vote = vote;
    }

    /*public QuestionDto(Long senderId, Long questionId, Long clientMessageId, Long roomId, String text,
                       Integer type, Date createDate, List<PlayerDto> yesVoters, List<PlayerDto> noVoters, Integer vote) {
        super(clientMessageId, new UserDto.Builder().setUserId(senderId).build(), roomId, text, type, createDate);
        this.questionId = questionId;
        this.yesVoters = yesVoters;
        this.noVoters = noVoters;
        this.vote = vote;
    }*/

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public List<PlayerDto> getYesVoters() {
        return yesVoters;
    }

    public void setYesVoters(List<PlayerDto> yesVoters) {
        this.yesVoters = yesVoters;
    }

    public List<PlayerDto> getNoVoters() {
        return noVoters;
    }

    public void setNoVoters(List<PlayerDto> noVoters) {
        this.noVoters = noVoters;
    }

    public Integer getVote() {
        return vote;
    }

    public void setVote(Integer vote) {
        this.vote = vote;
    }

    public Integer getAttempt() {
        return attempt;
    }

    public void setAttempt(Integer attempt) {
        this.attempt = attempt;
    }

    public List<PlayerDto> getWinVoters() {
        return winVoters;
    }

    public void setWinVoters(List<PlayerDto> winVoters) {
        this.winVoters = winVoters;
    }

    @Override
    public String toString() {
        return "QuestionDto{" +
                "questionId=" + questionId +
                ", yesVoters=" + yesVoters +
                ", noVoters=" + noVoters +
                ", winVoters=" + winVoters +
                ", attempt=" + attempt +
                ", vote=" + vote +
                '}';
    }

    public static class QuestionDTOBuilder {
        private Long id;
        private UserDto senderUser;
        private Long roomId;
        private List<PlayerDto> yesVoters;
        private List<PlayerDto> noVoters;
        private List<PlayerDto> winVoters;
        private Integer vote;
        private String text;
        private Integer type;
        private Date createDate;
        private Long clientMessageId;
        private Long questionId;
        private Integer attempt;
        private String securityToken;

        public QuestionDTOBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public QuestionDTOBuilder setYesVoters(List<PlayerDto> yesVoters) {
            this.yesVoters = yesVoters;
            return this;
        }

        public QuestionDTOBuilder setNoVoters(List<PlayerDto> noVoters) {
            this.noVoters = noVoters;
            return this;
        }

        public QuestionDTOBuilder setWinVoters(List<PlayerDto> winVoters) {
            this.winVoters = winVoters;
            return this;
        }

        public QuestionDTOBuilder setSenderUser(UserDto senderUser) {
            this.senderUser = senderUser;
            return this;
        }

        public QuestionDTOBuilder setRoomId(Long roomId) {
            this.roomId = roomId;
            return this;
        }

        public QuestionDTOBuilder setVoteType(Integer vote) {
            this.vote = vote;
            return this;
        }

        public QuestionDTOBuilder setText(String text) {
            this.text = text;
            return this;
        }

        public QuestionDTOBuilder setType(MessageType type) {
            this.type = type.getCode();
            return this;
        }

        public QuestionDTOBuilder setCreateDate(Date createDate) {
            this.createDate = createDate;
            return this;
        }

        public QuestionDTOBuilder setClientId(Long clientId) {
            this.clientMessageId = clientId;
            return this;
        }

        public QuestionDTOBuilder setQuestionId(Long questionId) {
            this.questionId = questionId;
            return this;
        }

        public QuestionDTOBuilder setAttempt(Integer attempt){
            this.attempt = attempt;
            return this;
        }

        public QuestionDTOBuilder setSecurityToken(String securityToken){
            this.attempt = attempt;
            return this;
        }

        public QuestionDto build() {
            return new QuestionDto(questionId, clientMessageId, senderUser, roomId, text, type, createDate, yesVoters, noVoters, winVoters, attempt, vote, securityToken);
        }
    }

}
