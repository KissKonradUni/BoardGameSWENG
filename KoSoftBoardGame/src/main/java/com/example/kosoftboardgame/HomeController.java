package com.example.kosoftboardgame;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {


    @GetMapping("/")
    String hello() {
        return "Hello Wolrd";
    }

}
