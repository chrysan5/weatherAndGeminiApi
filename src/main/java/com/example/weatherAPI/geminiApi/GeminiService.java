package com.example.weatherAPI.geminiApi;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@AllArgsConstructor
@Slf4j
@Service
public class GeminiService {

    private final RestTemplate restTemplate;

    @Autowired
    public GeminiService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;


    public String geminiTest(String question) {
        String requestUrl = apiUrl + "?key=" + geminiApiKey;

        ChatRequest request = new ChatRequest(question);
        System.out.println(request.getContents().toString());
        System.out.println(request.getGenerationConfig().toString());

        ChatResponse response = restTemplate.postForObject(requestUrl, request, ChatResponse.class);
        System.out.println(response.getCandidates().toString());

        String message = response.getCandidates().get(0).getContent().getParts().get(0).getText().toString();



        return message;
    }
}
