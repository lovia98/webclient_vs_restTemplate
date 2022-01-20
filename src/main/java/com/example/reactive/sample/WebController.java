package com.example.reactive.sample;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
@RestController
public class WebController {

    private static final int DEFAULT_PORT = 8080;

    @Setter
    private int serverPort = DEFAULT_PORT;

    /**
     * restTemplate 으로 호출 (blocking)
     *
     * @return
     */
    @GetMapping("/tweets-blocking")
    public List<Tweet> getTweetsBlocking() {

        log.info("블럭킹 호출 시작");
        long start = System.currentTimeMillis();

        final String uri = getSlowServiceUri();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Tweet>> response = restTemplate.exchange(
                uri, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Tweet>>(){});

        List<Tweet> result = response.getBody();
        result.forEach(tweet -> log.info(tweet.toString()));

        log.info("블럭킹 호출 완료 - 걸린시간 : {}ms", System.currentTimeMillis() - start);
        return result;
    }

    /**
     * webclient 호출 (non-blockin)
     *
     * @return
     */
    @GetMapping(value = "/tweets-non-blocking", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Tweet> getTweetsNonBlocking() {

        log.info("none-블럭킹 호출 시작");
        long start = System.currentTimeMillis();

        Flux<Tweet> tweetFlux = WebClient.create()
                .get()
                .uri(getSlowServiceUri())
                .retrieve()
                .bodyToFlux(Tweet.class);

        tweetFlux.subscribe(tweet -> log.info(tweet.toString()));
        log.info("none-블럭킹 호출 완료 - 걸린시간 : {}ms", System.currentTimeMillis() - start);
        return tweetFlux;
    }

    private String getSlowServiceUri() {
        return "http://localhost:" + serverPort + "/slow-service-tweets";
    }
}
