package com.example.weatherAPI.weatherApi;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import java.io.IOException;

@AllArgsConstructor
@Controller
public class WeatherController {
    private final WeatherService weatherService;

    @GetMapping("/getWeather")
    public ResponseEntity<String> getWeather() throws IOException {
        try {
            String result = weatherService.getWeather();
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Error occurred while fetching weather data", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
