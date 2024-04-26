package com.example.chattingweb.api.service;

import com.example.chattingweb.api.repository.ServerApiRepository;
import com.example.chattingweb.main.dto.UserDto;
import com.example.chattingweb.main.service.impl.MainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j  //로그
@Service
public class ServerApiService implements ServerApiServiceIf{

    @Autowired
    private MainService mainService;
    @Autowired
    private ServerApiRepository serverApiRepository;

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
    public Map<String, Object> settingParamsAndInsert(Map<String, Object> param, UserDto userDto) throws IOException {
        Map<String, Object> result = new HashMap<>();

        //리스트로 변환할 결과를 담을 list 생성
        List<Map<String,Object>> resultList = new ArrayList<>();

        //주어진 데이터를 순회하면서 post와 space를 분리하여 각각의 Dto로 만들어줌
        Map<String, Object> postDtoMap = new HashMap<>();
        Map<String, Object> placeDtoMap = new HashMap<>();
        Map<String, Object> photeUploadMap = new HashMap<>();
        for(Map.Entry<String,Object> entry : param.entrySet()){
            String key = entry.getKey();    //각각의 key 가져오기
            Object value = entry.getValue();

            //POST와 SPACE 구분하여 각  DTO에 넘겨줌
            if(key.startsWith("postDto")){
                String pdKey = key.substring("postDto".length()+1, key.length()-1); //postDto[~]부분 제외시키고 안의 내용 가져
                postDtoMap.put(pdKey, value);
            }else if(key.startsWith("spaceDto")){
                String sdKey = key.substring("spaceDto".length()+1, key.length()-1);
                placeDtoMap.put(sdKey, value);
            }else if(key.startsWith("photoUpload")){
                String sdKey = key.substring("photoUpload".length()+1, key.length()-1);
                photeUploadMap.put(sdKey, value);
            }
        }

        postDtoMap.put("userId", userDto.getUserId());

        System.out.println("postDto ===>"+postDtoMap);
        System.out.println("spaceDto ===>"+placeDtoMap);
        System.out.println("photoUpload ===>"+photeUploadMap);

        int postDtoInsertResult = serverApiRepository.insertPostDto(postDtoMap);
        result.put("postDtoInsertResult", postDtoInsertResult);

        //post에 잘 저장이 됐고 위치등록 했을 경우에만 -> place 테이블 insert
        if(postDtoInsertResult == 1 && postDtoMap.get("locationRegistered").equals("true")){
            placeDtoMap.put("postId", postDtoMap.get("postId"));
            int placeDtoInsertResult = serverApiRepository.insertPlaceDto(placeDtoMap);
            result.put("placeDtoInsertResult", placeDtoInsertResult);

            if(placeDtoInsertResult == 1){
                postDtoMap.put("placeId", placeDtoMap.get("placeId")); //post 테이블의 placeID 업데이트
                placeDtoInsertResult = serverApiRepository.updatePostPlaceId(postDtoMap);
            }
        }

        //post에 잘 저장이 됐고 사진 업로드 했을경우
        if(postDtoInsertResult == 1 && !photeUploadMap.isEmpty()){
            photeUploadMap.put("postId", postDtoMap.get("postId"));
            int photoUploadDtoInsertResult = serverApiRepository.insertPhoteUpload(photeUploadMap);
            result.put("photoUploadDtoInsertResult", photoUploadDtoInsertResult);

            if(photoUploadDtoInsertResult == 1){
                postDtoMap.put("photoId", photeUploadMap.get("photoId")); //post 테이블의 photoUpload 업데이트
                photoUploadDtoInsertResult = serverApiRepository.updatePhoteUploadPhoteId(postDtoMap);
                //photoUploadDtoInsertResult = insertCollectionTeacher(photeUploadMap);
            }
        }

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public int insertCollectionTeacher(Map<String,Object> photeUploadMap) throws IOException {
        int result = 0;
        Map<String, Object> path = createDirectory();    //디렉토리 생성
        String month = path.get("month").toString();
        String uuid = path.get("uuid").toString();

        for (int i = 0; i < photeUploadMap.size(); i++) {
            MultipartFile file = (MultipartFile) photeUploadMap.get("fileName"+(i+1));
            // windows
            String fullPath = "C:" + File.separator + "nas" + File.separator + month + File.separator + uuid + File.separator + photeUploadMap.get("fileName"+(i+1));
            // linux
    //        String fullPath = "/usr/local/nas/file_manage" + File.separator +month+ File.separator +uuid+ File.separator +file.getOriginalFilename();

            // windows
            String filePath1 = "C:" + File.separator + "nas" + File.separator + month + File.separator + uuid;
            photeUploadMap.put("path",filePath1);
            // linux
    //        String filePath = "/usr/local/nas/file_manage" + File.separator +month+ File.separator +uuid;
            try {
                //파일 업로드
                file.transferTo(new File(fullPath));
                //path 업데이트
                result = serverApiRepository.updatePhoteUpfilePath(photeUploadMap);
            } catch (Exception e) {
                throw new RuntimeException("file Save Error!!!!!!!!!!!!!");
            }
        }
        return result;
    }

    //파일 업로드 폴더 생성
    public Map<String,Object> createDirectory() throws IOException {
        HashMap<String,Object> result = new HashMap<>();

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        String nowTime1 = dateFormat.format(date);

        //1.디렉토리 생성 ex. 2023-11

        // windows
		String nasPath = "C:" + File.separator + "nas" + File.separator;

        // linux
//        String nasPath = "/usr/local/nas/file_manage" + File.separator;
        //LOGGER.info("nasPath = {}", nasPath);

        Path path = Paths.get(nasPath+nowTime1);

        if(!Files.exists(path)) 			Files.createDirectory(path);

        //2. 파일 uuid 디렉토리 생성
        String uuid = UUID.randomUUID().toString();

        // windows
		String filePath = "C:" + File.separator + "nas" + File.separator + nowTime1 + File.separator + uuid;

        // linux
//        String filePath = "/usr/local/nas/file_manage" + File.separator + nowTime1 + File.separator + uuid;
        //LOGGER.info("nasFilePath = {}", filePath);

        Path path1 = Paths.get(filePath);
        Files.createDirectory(path1);

        result.put("month", nowTime1);
        result.put("uuid", uuid);

        return result;
    }

    @Override
    public List<Map<String, Object>> settingPostList(UserDto userDto) {
        int start = userDto.getCurrentPage() * userDto.getPageSize();
        userDto.setStart(start);
        List<Map<String,Object>> postList = (List<Map<String, Object>>) serverApiRepository.selectPostsByUserId(userDto);
        System.out.printf("postList ===<>"+postList);

        return postList;
    }


}
