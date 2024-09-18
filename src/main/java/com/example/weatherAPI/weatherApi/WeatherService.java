package com.example.weatherAPI.weatherApi;


import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class WeatherService {

    @Value("${public.portal.url}")
    private String apiUrl;

    @Value("${public.portal.key}")
    private String serviceKey;


    public String getWeather() throws IOException {

        //이걸 파라미터로 받아올 예정
        String nx = "60";    //위도
        String ny = "125";    //경도

        LocalDate now = LocalDate.now();         // 포맷 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd"); // 포맷 적용
        String formatedNow = now.format(formatter);

        String baseDate = formatedNow;    //조회하고싶은 날짜 - 오늘 날짜 넣어주기
        String baseTime = "0600";    //조회하고싶은 시간
        String type = "JSON";    //조회하고 싶은 type(json, xml 중 고름)


        StringBuilder urlBuilder = new StringBuilder(apiUrl);
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + serviceKey);
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); //페이지번호
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("20", "UTF-8")); //한 페이지 결과 수
        urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); //경도
        urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); //위도
        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); /* 조회하고싶은 날짜*/
        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8")); /* 조회하고싶은 시간 AM 02시부터 3시간 단위 */
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8"));    /* 타입 */


        //GET방식으로 전송해서 파라미터 받아오기
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());

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
        System.out.println(result);
        //return result;


        //=======json에서 데이터 파싱=============

        // response 키를 가지고 데이터를 파싱
        JSONObject resultObj = new JSONObject(result);
        JSONObject response = resultObj.getJSONObject("response");
        JSONObject body = response.getJSONObject("body");
        JSONObject items = body.getJSONObject("items");

        // "item" 배열을 추출
        JSONArray itemArray = items.getJSONArray("item");


        String observeResult = "오늘의 날씨입니다. ";
        for (int i = 0; i < itemArray.length(); i++) {
            JSONObject item = itemArray.getJSONObject(i);
            String category = item.getString("category");

            if (category.equals("REH")) {
                observeResult += "오늘의 습도는 " + item.getString("obsrValue") + " 이고, ";
                System.out.println("Category REH, 습도(%): " + observeResult);
            }

            if (category.equals("T1H")) {
                observeResult += "오늘의 기온은 " + item.getString("obsrValue") + " 이고, ";
                System.out.println("Category T1H, 기온(도): " + observeResult);
            }

            if (category.equals("WSD")) {
                observeResult += "오늘의 풍속은 " + item.getString("obsrValue") + " 입니다.";
                System.out.println("Category WSD, 풍속(m/s): " + observeResult);
            }
        }

        return observeResult;
    }

}
