package com.example.simpleWebApp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @RequestMapping("/")
    public String greet(){
        return "namaskara";
    }
    @RequestMapping("/about")
    public String welcome(){
        return "welcome";
    }
}
