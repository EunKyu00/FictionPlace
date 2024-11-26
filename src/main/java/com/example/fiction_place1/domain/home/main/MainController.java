package com.example.fiction_place1.domain.home.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String webToonListPage(){
        return "webtoon_list";
    }

}
