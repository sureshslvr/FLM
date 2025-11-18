package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/hi")
public class HomeController {

    @RequestMapping("/home")
    public String home(){
        return "home";
    }
    @RequestMapping("/home1")
    @ResponseBody
    public String home1(){
        return "home";
    }
}
