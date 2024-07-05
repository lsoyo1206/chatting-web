package com.example.chattingweb.api.service;

import com.example.chattingweb.api.common.FileUtil;
import com.example.chattingweb.api.controller.ServerApiController;
import com.example.chattingweb.api.dto.PhotoDto;
import com.example.chattingweb.api.dto.PostDto;
import com.example.chattingweb.api.repository.ServerApiRepository;
import com.example.chattingweb.main.dto.UserDto;
import com.example.chattingweb.main.service.impl.MainService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j  //로그
@Service
public class ServerApiService implements ServerApiServiceIf{

    private static final Logger Logger = LoggerFactory.getLogger(ServerApiService.class);

    @Autowired
    private MainService mainService;
    @Autowired
    private ServerApiRepository serverApiRepository;
    @Autowired
    private FileUtil fileUtil;

    @Override
    public String foodSearch(Map<String, Object> param) {
        return null;
    }

    @Override
    public String foodSearchImage(Map<String, Object> param) {
        return null;
    }

    //세션정보로 사용자 정보 가져오기
    @Override
    public UserDto userInfo() {
        UserDto userDto = null;
        String email = SecurityContextHolder.getContext().getAuthentication().getName(); //email
        if (email != null){
            userDto = mainService.findByEmail(email);
            System.out.println(userDto);
        }
        return userDto;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> insertAndUploadPhoto(String postId, List<MultipartFile> files) throws Exception {
        Map<String, Object> result = new HashMap<>();
        int photoInsert = 0;

        Map<String,Object> path = fileUtil.createDirectory();	//디렉토리 생성 함수
        String month = path.get("month").toString();    //파일이 들어갈 날짜 경로
        String uuid  = path.get("uuid").toString();     //파일이 들어갈 uuid 경로

        Logger.info("month : {}", month);
        Logger.info("uuid : {}", uuid);

        boolean allUploadFile = true;
        int index = 0;

        for (MultipartFile file : files) {
            try {
                String fileName = fileUtil.uploadFile(file, month, uuid);    //파일 업로드

                // photoDto 파라미터 세팅
                int lastDotIndex = fileName.lastIndexOf('.');
                String baseFileName = (lastDotIndex == -1) ? fileName : fileName.substring(0, lastDotIndex);
                String fileExtension = (lastDotIndex == -1) ? "" : fileName.substring(lastDotIndex);
                long fileSize = file.getSize();
                String nasPath = "C:" + File.separator + "nas" + File.separator + month + File.separator + uuid;

                PhotoDto photoDto = PhotoDto.builder()
                        .postId(Integer.parseInt(String.valueOf(postId)))
                        .groupVOrder(index)
                        .filePath(nasPath)
                        .fileName(baseFileName)
                        .fileExtension(fileExtension)
                        .fileSize(fileSize)
                        .build();
                index += 1;

                photoInsert = serverApiRepository.insertPhote(photoDto);    //tb_photo insert
                photoInsert = serverApiRepository.updatePostId(photoDto);   //tb_post  update

            } catch (IOException e) {
                Logger.error("File save error: " + e.getMessage());
                allUploadFile = false;
                photoInsert = 0;
                break;
            }
        }

        result.put("allUploadFile", allUploadFile);
        result.put("photoInsert", photoInsert);

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> insertPostDto(PostDto postDto, UserDto userDto) {
        Map<String, Object> insertResult = new HashMap<>();
        postDto.setUserId(userDto.getUserId());

        //location 테이블에 넣어줄 파라미터 세팅
        Map<String, Object> LocationMap = new HashMap<>();
        LocationMap.put("locationName", postDto.getLocationName());
        LocationMap.put("address", postDto.getAddress());
        LocationMap.put("roadAddress", postDto.getRoadAddress());
        LocationMap.put("latitude", postDto.getLatitude());
        LocationMap.put("longitude", postDto.getLongitude());

        System.out.println("postDto ===>"+postDto);
        System.out.println("LocationMap ===>" + LocationMap);

        int postDtoInsertResult = serverApiRepository.insertPostDto(postDto);
        insertResult.put("postDtoInsertResult", postDtoInsertResult);
        insertResult.put("postId", postDto.getPostId());

        if(postDtoInsertResult == 1 && postDto.getLocationName() != null ) {
            LocationMap.put("postId", postDto.getPostId());
            int LocationInsertResult = serverApiRepository.insertLocation(LocationMap);
            insertResult.put("LocationInsertResult", LocationInsertResult);

            if (LocationInsertResult == 1) {
                postDto.setLocationId(Integer.parseInt(String.valueOf(LocationMap.get("locationId"))));                 //post 테이블의 locationId 업데이트
                LocationInsertResult = serverApiRepository.updatePostLocationId(postDto);
                insertResult.put("LocationInsertResult", LocationInsertResult);
            }
        }

        return insertResult;
    }

    @Override
    public List<Map<String, Object>> settingPostList(UserDto userDto) {
        int start = userDto.getCurrentPage() * userDto.getPageSize();
        userDto.setStart(start);
        List<Map<String,Object>> postList = (List<Map<String, Object>>) serverApiRepository.selectPostsByUserId(userDto);
        return postList;
    }

}
