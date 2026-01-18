package com.supermarket.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bills")  // adds "/bills" after context-path
public class PingController {

    @GetMapping("/ping")
    public String ping() {
        return "PING OK";
    }
}
