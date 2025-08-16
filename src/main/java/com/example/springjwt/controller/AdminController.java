package com.example.springjwt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody//웺 서버가 아닌 API서버로 동작->웹이 아닌 특정한 문자열 데이터로 응답
public class AdminController {

    @GetMapping("/admin")
    public String adminP() {
        return "admin Controller";
    }
}
