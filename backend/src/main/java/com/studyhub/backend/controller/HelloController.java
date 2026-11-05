package com.studyhub.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/api/hello")
    public HelloResponse hello(){
        return new HelloResponse(
                "你好！ StudyHub！",
                "StudyHub"
        );
    }
    public record HelloResponse(
            String message,
            String project
    ){}
}