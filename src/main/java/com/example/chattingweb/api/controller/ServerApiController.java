package com.example.chattingweb.api.controller;



import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Slf4j  //로그
@RestController
@RequestMapping("/api/server")
public class ServerApiController {

    @GetMapping("/foodSearch")
    public String foodSearch(@RequestParam Map<String,Object> data){
        String query = String.valueOf(data.get("searchQuery"));
        log.info("query : {}", query);
        String encode = Base64.getEncoder().encodeToString(query.getBytes(StandardCharsets.UTF_8));

        // https://openapi.naver.com
        // /v1/search/local.json
        // ?query=%EA%B0%88%EB%B9%84%EC%A7%91
        // &display=10
        // &start=1
        // &sort=random
        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path("/v1/search/local.json")
                .queryParam("query", "서울특별시 중구")
                .queryParam("display", 10)
                .queryParam("start", 1)
                .queryParam("sort", "random")
                .encode()
                .build()
                .toUri();

        log.info("url : {}", uri);
        RestTemplate restTemplate = new RestTemplate();

        //header 추가위해 RequestEntity > get으로 보내니까 void 사용
        RequestEntity<Void> req = RequestEntity.get(uri)
                .header("X-Naver-Client-Id", "g7_cTbYofdvOxLWv9_9L")
                .header("X-Naver-Client-Secret", "AKpfKr7qhn")
                .build();

        //header 사용하기 위한 exchange 사용
        ResponseEntity<String> response = restTemplate.exchange(req, String.class);

        return response.getBody();
    }

    @GetMapping("/foodSearchImage")
    public String foodSearchImage(){
        String query = "곱창";
        String encode = Base64.getEncoder().encodeToString(query.getBytes(StandardCharsets.UTF_8));

        // https://openapi.naver.com
        // /v1/search/local.json
        // ?query=%EA%B0%88%EB%B9%84%EC%A7%91
        // &display=10
        // &start=1
        // &sort=random
        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path("/v1/search/image")
                .queryParam("query", "서울")
                .queryParam("display", 10)
                .queryParam("sort", "sim")
                .queryParam("filter", "all")
                .encode()
                .build()
                .toUri();

        log.info("url : {}", uri);
        RestTemplate restTemplate = new RestTemplate();

        //header 추가위해 RequestEntity > get으로 보내니까 void 사용
        RequestEntity<Void> req = RequestEntity.get(uri)
                .header("X-Naver-Client-Id", "g7_cTbYofdvOxLWv9_9L")
                .header("X-Naver-Client-Secret", "AKpfKr7qhn")
                .build();

        //header 사용하기 위한 exchange 사용
        ResponseEntity<String> response = restTemplate.exchange(req, String.class);

        return response.getBody();
    }

}
