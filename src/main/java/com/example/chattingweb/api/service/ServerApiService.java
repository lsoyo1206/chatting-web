package com.example.chattingweb.api.service;

import com.example.chattingweb.api.repository.ServerApiRepository;
import com.example.chattingweb.main.dto.UserDto;
import com.example.chattingweb.main.service.impl.MainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Array;
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
    public Map<String, Object> settingParamsAndInsert(Map<String, Object> param, UserDto userDto) {
        Map<String, Object> result = new HashMap<>();
        System.out.printf("userDto ==1111=> ",userDto);

        //리스트로 변환할 결과를 담을 list 생성
        List<Map<String,Object>> resultList = new ArrayList<>();

        //주어진 데이터를 순회하면서 post와 space를 분리하여 각각의 Dto로 만들어줌
        Map<String, Object> postDtoMap = new HashMap<>();
        Map<String, Object> placeDtoMap = new HashMap<>();
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
            }
        }
        postDtoMap.put("userId", userDto.getUserId());

        System.out.println("postDto ===>"+postDtoMap);
        System.out.println("spaceDto ===>"+placeDtoMap);

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

        return result;
    }

    @Override
    public List<Map<String, Object>> settingPostList(UserDto userDto) {
        int start = userDto.getPage() * userDto.getPageSize();
        userDto.setStart(start);
        List<Map<String,Object>> postList = (List<Map<String, Object>>) serverApiRepository.selectPostsByUserId(userDto);
        System.out.printf("postList ===<>"+postList);

        return postList;
    }


}
