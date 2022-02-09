package com.example.reactive.config;

import com.example.reactive.sample.Tweet;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
public class WebFluxHandler {

    public Mono<ServerResponse> index(ServerRequest request){
        final Map<String, Tweet> data = new HashMap<>();
        data.put("data", new Tweet("index", "juhee"));
        return ServerResponse.ok().contentType(MediaType.TEXT_HTML).render("index", data);
    }

    public Mono<ServerResponse> hello(ServerRequest request){
        final Map<String, Tweet> data = new HashMap<>();
        data.put("data", new Tweet("hello", "khw"));
        return ServerResponse.ok().contentType(MediaType.TEXT_HTML).render("index", data);
    }
}
