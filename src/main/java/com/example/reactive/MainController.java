package com.example.reactive;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final Environment env;

    /**
     * 현재 profile 응답
     *
     * @return
     */
    @GetMapping("/profile")
    public ResponseEntity<String> profile() {
        return new ResponseEntity<>(env.getActiveProfiles()[0], HttpStatus.OK);
    }

    /**
     * 현재 profile 응답
     *
     * @return
     */
    @GetMapping("/port")
    public ResponseEntity<String> port() {
        return new ResponseEntity<>(env.getProperty("server.port"), HttpStatus.OK);
    }
}
