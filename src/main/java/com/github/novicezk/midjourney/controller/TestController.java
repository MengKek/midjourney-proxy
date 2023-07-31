package com.github.novicezk.midjourney.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*") // 允许所有源访问
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "CORS Test Successful";
    }
}