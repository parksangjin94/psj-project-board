package com.psj.projectboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String root(){    // 홈 버튼을 눌렀을 때 리다이렉션
        return "forward:/articles";
    }
}
