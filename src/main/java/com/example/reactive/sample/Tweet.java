package com.example.reactive.sample;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Tweet {
    private String message;
    private String to;

    public Tweet(String message, String to) {
        this.message = message;
        this.to = to;
    }
}
