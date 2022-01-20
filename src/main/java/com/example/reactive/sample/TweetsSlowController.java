package com.example.reactive.sample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
public class TweetsSlowController {

    /**
     * 시간이 오래 걸리는 api
     *
     * @return
     * @throws InterruptedException
     */
    @GetMapping("/slow-service-tweets")
    private List<Tweet> getAllTweets() throws InterruptedException {

        Thread.sleep(2000L); // 2초 delay

        return Arrays.asList(
                new Tweet("RestTemplate rules", "@user1"),
                new Tweet("WebClient is better", "@user2"),
                new Tweet("OK, both are useful", "@user1"));
    }
}
