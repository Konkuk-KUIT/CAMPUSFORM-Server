package com.campusform.server.recruiting.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class SmsPreviewResponse {

    private int count;
    private List<PreviewMessage> messages;
    @Getter
    @Builder
    public static class PreviewMessage {
        private Long applicantId;
        private String name;
        private String info;
        private String phoneNumber;
        private String content;
    }
}
