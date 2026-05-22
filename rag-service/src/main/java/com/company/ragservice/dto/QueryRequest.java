package com.company.ragservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryRequest {
    private String question;
    private String selectedFile;
    private List<MessageEntry> history = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageEntry {
        private String role;    // "user" sau "assistant"
        private String content;
    }
}
