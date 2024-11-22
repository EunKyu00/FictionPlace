package com.example.fiction_place1.domain.home.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class MainController {
    @GetMapping("/")
    public String webToonListPage(){
        return "webtoon_list";
    }


    @GetMapping("/data")
    @ResponseBody
    public Map<String, String> getData() {
        // 서버에서 클라이언트로 전달할 데이터 생성
        Map<String, String> data = new HashMap<>();
        data.put("username", "홍길동");
        data.put("message", "서버ddddd");
        return data; // JSON 형식으로 반환하여 React에서 활용 가능
    }
}
