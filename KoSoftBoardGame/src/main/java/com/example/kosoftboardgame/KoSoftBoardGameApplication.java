package com.example.kosoftboardgame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class KoSoftBoardGameApplication {

    public static void main(String[] args) {
        SpringApplication.run(KoSoftBoardGameApplication.class, args);
    }

    @GetMapping("/")
    public String hello() {
        return "Hello, World!";
    }

}
