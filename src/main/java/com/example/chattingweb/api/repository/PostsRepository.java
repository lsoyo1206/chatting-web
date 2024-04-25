package com.example.chattingweb.api.repository;

import com.example.chattingweb.api.dto.PostDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PostsRepository {

    public PostDto insertPostDto(PostDto postDto);

}
