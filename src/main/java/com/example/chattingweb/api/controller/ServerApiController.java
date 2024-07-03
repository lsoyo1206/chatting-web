package com.example.chattingweb.api.controller;



import com.example.chattingweb.api.dto.PostDto;
import com.example.chattingweb.api.repository.ServerApiRepository;
import com.example.chattingweb.api.service.ServerApiService;
import com.example.chattingweb.main.dto.UserDto;
import com.example.chattingweb.main.service.impl.MainService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j  //로그
@Controller
@RequestMapping("/api/server")
public class ServerApiController {

    private static final Logger logger = LoggerFactory.getLogger(ServerApiController.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ServerApiService serverApiService;
    @Autowired
    private MainService mainService;
    @Autowired
    private ServerApiRepository serverApiRepository;

    @GetMapping("/memorySave.do")
    public String memorySave(Model model){
        UserDto userDto = serverApiService.userInfo();  //사용자 정보
        logger.info("/memorySave.do userInfo = {}",  userDto.toString());

        //model.addAttribute("userDto",userDto);
        return "user/memorySave";
    }

    @ResponseBody
    @GetMapping("/selectPlaceInfo.do")
    public Map<String,Object> selectPlaceInfo(@RequestParam Map<String,Object> data){
        int placeId = (int) data.get("placeId");
        Map<String,Object> result = serverApiRepository.selectPlaceInfo(placeId);  //사용자 정보
        System.out.printf("result ===>"+result);
        return result;
    }

    @GetMapping("/map.do")
    public String map(Model model, @RequestParam(defaultValue = "0", value="page") int page){

        UserDto userDto = serverApiService.userInfo();

        //페이징 처리
        int totalPages = serverApiRepository.selectPostsByUserIdTotalPage(userDto);
        userDto.setCurrentPage(page);
        userDto.setPageSize(5);
        userDto.setTotalPages(totalPages);
        List<Map<String,Object>> postList = serverApiService.settingPostList(userDto);

        model.addAttribute("postList", postList);
        model.addAttribute("userDto",userDto);      //사용자 정보

        return "user/map";
    }

    @ResponseBody
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
                .queryParam("query", "제일곱창 왕십리")
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

    @ResponseBody
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

    @RequestMapping(value = "/insertPost.do", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String,Object> insertPost (HttpServletRequest request, HttpServletResponse response,
                                          @ModelAttribute PostDto postDto) throws Exception {
        Map<String,Object> result = new HashMap<>();

        UserDto userDto = serverApiService.userInfo();  //로그인한 사용자 정보
        logger.info("postDto : {}", postDto);

        Map<String,Object> insertResult = serverApiService.insertPostDto(postDto, userDto);

       return result;
    }

//    @ResponseBody
//    @GetMapping("/insertPost")
//    public ResponseEntity<Void> insertPost (@RequestParam Map<String,Object> param) throws IOException {
//        UserDto userDto = serverApiService.userInfo();  //로그인한 사용자 정보
//        Map<String,Object> result = serverApiService.settingParamsAndInsert(param, userDto);
//
//        if(Integer.parseInt(result.get("postDtoInsertResult").toString()) != 1){
//            return ResponseEntity.badRequest().build();
//        }
//
//        return ResponseEntity.ok().build();
//    }


}
