package com.example.weatherAPI.geminiApi;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;

@AllArgsConstructor
@Controller
public class GeminiController {
    private final GeminiService geminiService;

    //질문과 응답이 저장되도록 해야한다.
    @GetMapping("/geminiTest")
    public ResponseEntity<?> geminiTest(@RequestParam("question") String question) {
        try {
            return ResponseEntity.ok().body(geminiService.geminiTest(question));
        } catch (HttpClientErrorException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
