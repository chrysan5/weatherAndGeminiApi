package com.example.weatherAPI.slackApi;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@AllArgsConstructor
@Controller
public class SlackController {
    public final SlackService slackService;

    @GetMapping("/slack")
    public ResponseEntity<String> sendSlack(@RequestParam String msg) throws Exception {
        slackService.sendSlack(msg);
        return ResponseEntity.ok().build();
    }
}
