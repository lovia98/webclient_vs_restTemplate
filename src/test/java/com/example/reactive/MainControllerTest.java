package com.example.reactive;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

class MainControllerTest {

    @Test
    void profile() {

        String profile_str = "real";

        //given
        MockEnvironment env = new MockEnvironment();
        env.addActiveProfile(profile_str);

        MainController controller = new MainController(env);

        //when
        ResponseEntity<String> profile = controller.profile();

        assertThat(profile.getBody()).isEqualTo(profile_str);
    }

    @Test
    void port() {

        String serverPort = "8080";

        //given
        MockEnvironment env = new MockEnvironment();
        env.setProperty("server.port", serverPort);

        MainController controller = new MainController(env);

        //when
        ResponseEntity<String> port = controller.port();

        assertThat(port.getBody()).isEqualTo(serverPort);
    }
}