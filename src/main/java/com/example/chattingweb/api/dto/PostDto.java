package com.example.chattingweb.api.dto;

import com.example.chattingweb.main.dto.UserDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PostDto extends UserDto {

    private int postId;
    private String title;
    private String content;
    private String visitedFriends;
    private int locationId;
    private String locationRegistered;
    private Date modifyDt;
    private Date createDt;
    private String userYn;
}

