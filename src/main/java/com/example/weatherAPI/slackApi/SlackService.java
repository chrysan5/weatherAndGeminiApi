package com.example.weatherAPI.slackApi;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Slf4j
@Service
public class SlackService {

    @Value("${slack.api.url}")
    private String slackApiUrl;
    
    @Value("${slack.api.token}")
    private String slackApitocken;


    public void sendSlack(String msg) throws Exception {
        String urlStr = slackApiUrl + "?";
        urlStr += "channel=C07N15NSWLD&"; //tms(채녈명)를 인코딩한 값
        urlStr += "text="+ URLEncoder.encode(msg, "UTF-8");


        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + slackApitocken);
        //System.out.println("Response code: " + conn.getResponseCode());

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }

        rd.close();
        conn.disconnect();
        String result = sb.toString();
        //System.out.println(result);
    }

}
