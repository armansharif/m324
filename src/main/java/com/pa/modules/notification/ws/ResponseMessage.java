package com.pa.modules.notification.ws;

import lombok.Data;

@Data
public class ResponseMessage {
    private String content;

    public ResponseMessage(String htmlEscape) {
    }
}
