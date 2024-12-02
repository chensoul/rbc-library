package com.productdock.adapter.out.kafka.messages;


import lombok.*;

import java.io.Serializable;
import java.util.List;

@Builder
@AllArgsConstructor
@ToString
@Data
public class AddedBookMessage implements Serializable {
    private Long bookId;
    private String title;
    private String cover;
    private String author;
    private List<Topic> topics;
    private int bookCopies;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Topic implements Serializable {
        private String id;
        private String name;
    }
}
