package com.study.spring.springsecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SecurityController {

    @ResponseBody
    @GetMapping("/security")
    public String getSecurityString() {
        return "security";
    }

    @ResponseBody
    @GetMapping("/nothing")
    public String getNothingString() {
        return "nothing";
    }
}
