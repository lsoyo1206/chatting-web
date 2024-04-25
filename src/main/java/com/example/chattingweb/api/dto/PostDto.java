package com.example.chattingweb.api.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
public class PostDto{

    private int postId;
    private int spaceId;
    private int userId;
    private String title;
    private String content;
    private String visitedFriends;
    private boolean locationRegistered; //위치등록여부
    private Date modifyDt;
    private Date createDt;
    private String userYn;
}

