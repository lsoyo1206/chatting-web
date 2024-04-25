package com.example.chattingweb.api.repository;

import com.example.chattingweb.api.dto.PostDto;
import com.example.chattingweb.api.dto.SpaceDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PostRepository {

    public PostDto insertPostDto(PostDto postDto);
    public SpaceDto insertSpaceDto(SpaceDto spaceDto);
}
