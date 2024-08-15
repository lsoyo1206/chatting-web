package com.example.chattingweb.api.controller;



import com.example.chattingweb.api.common.ApiExplorer;
import com.example.chattingweb.api.dto.PhotoDto;
import com.example.chattingweb.api.dto.PostDto;
import com.example.chattingweb.api.repository.ServerApiRepository;
import com.example.chattingweb.api.service.ServerApiService;
import com.example.chattingweb.main.dto.UserDto;
import com.example.chattingweb.main.service.impl.MainService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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
        model.addAttribute("userDto",userDto);
        return "user/memorySave";
    }

    @GetMapping("/memoryEdit.do")
    public String memoryEdit(@RequestParam("postId") String postId, Model model){
        UserDto userDto = serverApiService.userInfo();
        Map<String,Object> selectParam = new HashMap<>();
        selectParam.put("postId", postId);

        List<PhotoDto> photoDto = new ArrayList<>();
        PostDto postDto = serverApiRepository.selectPostDetailInfo(selectParam);

         int photoId = postDto.getPhotoId() != 0 ? postDto.getPhotoId() : 0 ;
         if(photoId != 0 ){
            photoDto = serverApiRepository.selectPhotoDetailInfo(Integer.parseInt(postId));
             for(PhotoDto photo : photoDto){
                 String filePath = "http://3.27.210.64:8081/files" + File.separator +
                         photo.getFilePath() + File.separator + photo.getFileName() + photo.getFileExtension();

                photo.setFilePath(filePath);

             }
         }


        model.addAttribute("userDto", userDto);
        model.addAttribute("photoDto", photoDto);
        model.addAttribute("postDto", postDto);
        model.addAttribute("postId", postDto.getPostId());

        return "user/memorySave";
    }

    @ResponseBody
    @PostMapping("/selectLocationInfo.do")
    public Map<String,Object> selectLocationInfo(@RequestBody Map<String,Object> data){
        Map<String,Object> result = new HashMap<>();

        int locationId = (int) data.get("locationId");
        result = serverApiRepository.selectLocationInfo(locationId);  //사용자 정보
        System.out.printf("result ===>"+result);

        return result;
    }

    @GetMapping("/map.do")
    public String map(Model model, @RequestParam(defaultValue = "0", value="page") int page) throws IOException {

        UserDto userDto = serverApiService.userInfo();
        userDto.setPageSize(5);

        //페이징 처리
        int totalPages = serverApiRepository.selectPostsByUserIdTotalPage(userDto);
        userDto.setCurrentPage(page);
        userDto.setTotalPages(totalPages);
        List<Map<String,Object>> postList = serverApiService.settingPostList(userDto);

        for(int i=0 ; i<postList.size() ; i++){
            String htmlString = String.valueOf(postList.get(i).get("content"));
            String textOnlyContent = Jsoup.parse(htmlString).text();
            postList.get(i).put("textOnlyContent", textOnlyContent); //content html 부분 제외한 텍스트 부분만 추출

            /* if(postList.get(i).get("filePath") != null){    //사진 pullPath 추출
                StringBuilder fileBuilder = new StringBuilder();
                fileBuilder.append("/usr/local/toyproject/nas/file_manage/");
                fileBuilder.append(String.valueOf(postList.get(i).get("filePath")));
                fileBuilder.append(File.separator);
                fileBuilder.append(String.valueOf(postList.get(i).get("fileName")));
                fileBuilder.append(String.valueOf(postList.get(i).get("fileExtension")));
                String fileFullPath = fileBuilder.toString();
                postList.get(i).put("filePullPath",fileFullPath);
            } */

            if (postList.get(i).get("filePath") != null) {    // 사진 pullPath 추출
                String filePullPath = "http://3.27.210.64:8081/files" + File.separator +
                        String.valueOf(postList.get(i).get("filePath")) + File.separator +
                        String.valueOf(postList.get(i).get("fileName")) +
                        String.valueOf(postList.get(i).get("fileExtension"));
                //filePullPathString filePullPath = File.separator + "images"+ File.separator + fileName;
                postList.get(i).put("filePullPath", filePullPath);
            }
        }


        model.addAttribute("postList", postList);
        model.addAttribute("userDto",userDto);      //사용자 정보

        return "user/map";
    }

    @ResponseBody
    @GetMapping("/foodSearch")
    public String foodSearch(@RequestParam Map<String,Object> data){
        String query = String.valueOf(data.get("searchQuery"));
        log.info("query : {}", query);

        if (query == null || query.trim().isEmpty()) {
            return "{\"response\": []}";
        }

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
                .queryParam("query", query)
                .queryParam("display", 10)
                .queryParam("start", 1)
                .queryParam("sort", "sim")
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
        String msg = "";
        String code = "";

        UserDto userDto = serverApiService.userInfo();  //로그인한 사용자 정보
        logger.info("postDto : {}", postDto);

        Map<String,Object> insertResult = serverApiService.insertPostDto(postDto, userDto);

        Integer postResult = (Integer) insertResult.get("postDtoInsertResult");
        Integer locationResult = (Integer) insertResult.get("LocationInsertResult") != null ? (Integer) insertResult.get("LocationInsertResult") : 0;

        if(postResult == 1 || locationResult == 1){
            code = "R000";
            msg = "SUCCESS";
        }else{
            code = "R001";
            msg ="FAIL";
        }

        result.put("type", "insert");
        result.put("postId", Integer.parseInt(String.valueOf(insertResult.get("postId"))));
        result.put("msg", msg);
        result.put("code", code);

       return result;
    }

    @RequestMapping(value = "/insertAndUploadPhoto.do", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String,Object> insertAndUploadPhoto (HttpServletRequest request, HttpServletResponse response,
                                                    @RequestParam("postId") String postId,
                                                    @RequestParam("files") List<MultipartFile> files) throws Exception {
        Map<String,Object> result = new HashMap<>();
        String msg = "";
        String code = "";

        UserDto userDto = serverApiService.userInfo();  //로그인한 사용자 정보
        logger.info("postId : {}", postId);
        for (MultipartFile file : files) {
            logger.info("file : {}", file.getOriginalFilename());
        }

        Map<String,Object> insertResult = serverApiService.insertAndUploadPhoto(postId, files);

        boolean photoUploadResult = (boolean) insertResult.get("allUploadFile");
        Integer photoInsertResult = (Integer) insertResult.get("photoInsert") ;

        if(photoUploadResult && photoInsertResult == 1){
            code = "R000";
            msg = "SUCCESS";
        }else{
            code = "R001";
            msg ="FAIL";
        }

        result.put("msg", msg);
        result.put("code", code);

        return result;
    }

    @RequestMapping(value = "/deletePost.do", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String,Object> deletePost(@RequestParam("postId") String postId, Model model){
        Map<String,Object> result = new HashMap<>();
        String msg = "";
        String code = "";
        int updatePostResult = 0;
        int updateLocationResult = 0;
        int updatePhotoResult = 0;

        UserDto userDto = serverApiService.userInfo();
        Map<String,Object> selectParam = new HashMap<>();
        selectParam.put("postId", postId);
        selectParam.put("userId", userDto.getUserId());

        PostDto postDto = serverApiRepository.selectPostDetailInfo(selectParam);
        updatePostResult = serverApiRepository.deletePostTable(Integer.parseInt(String.valueOf(postId)));

        if(updatePostResult == 1 && "Y".equals(postDto.getLocationRegistered())){
            updateLocationResult = serverApiRepository.deleteLocationTable(Integer.parseInt(String.valueOf(postId)));
        }else{
            updateLocationResult = 1;
        }

        if(updatePostResult == 1 && postDto.getPhotoId() != 0){
            updatePhotoResult = serverApiRepository.deletePhotoTable(Integer.parseInt(String.valueOf(postId)));
        }else{
            updatePhotoResult = 1;
        }

        if(updatePostResult == 1 && updateLocationResult == 1 && updatePhotoResult >= 1){
            code = "R000";
            msg = "SUCCESS";
        }else{
            code = "R001";
            msg ="FAIL";
        }

        result.put("msg", msg);
        result.put("code", code);

        return result;
    }

    @RequestMapping(value = "/EditPost.do", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String,Object> EditPost(HttpServletRequest request, HttpServletResponse response,
                                          @ModelAttribute PostDto postDto) throws Exception {
        Map<String,Object> result = new HashMap<>();
        String msg = "";
        String code = "";

        UserDto userDto = serverApiService.userInfo();  //로그인한 사용자 정보
        logger.info("postDto : {}", postDto);

        Map<String,Object> insertResult = serverApiService.updatePostDto(postDto, userDto);

        Integer postResult = (Integer) insertResult.get("postDtoUpdateResult");
        Integer locationResult = (Integer) insertResult.get("locationUpdateResult") != null ? (Integer) insertResult.get("locationUpdateResult") : 0;

        if(postResult == 1 || locationResult == 1){
            code = "R000";
            msg = "SUCCESS";
        }else{
            code = "R001";
            msg ="FAIL";
        }

        result.put("type", "edit");
        result.put("postId", Integer.parseInt(String.valueOf(postDto.getPostId())));
        result.put("msg", msg);
        result.put("code", code);

        return result;
    }
}
