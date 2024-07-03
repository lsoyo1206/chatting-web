package com.example.chattingweb.api.repository;

import com.example.chattingweb.api.dto.PostDto;
import com.example.chattingweb.main.dto.UserDto;
import org.apache.catalina.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ServerApiRepository {

    public int insertPostDto(PostDto postDto);
    public int insertLocation(Map<String,Object> LocationMap);
    public int updatePostLocationId(PostDto postDto);
    public List<Map<String, Object>> selectPostsByUserId(UserDto userDto);
    public int selectPostsByUserIdTotalPage(UserDto userDto);
    public int insertPhoteUpload(Map<String,Object> photeUploadMap);
    public int updatePhoteUploadPhoteId(Map<String,Object> postDtoMap);
    //파일pah 업데이트
    public int updatePhoteUpfilePath(Map<String,Object> postDtoMap);
    public Map<String,Object> selectPlaceInfo(int placeId);

}
