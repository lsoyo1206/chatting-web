package com.example.chattingweb.api.repository;

import com.example.chattingweb.api.dto.PhotoDto;
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
    public int insertPhote(PhotoDto photoDto);
    public int updatePhoteUploadPhoteId(Map<String,Object> postDtoMap);
    public int updatePostId(PhotoDto photoDto);
    public Map<String,Object> selectPlaceInfo(int placeId);

}
