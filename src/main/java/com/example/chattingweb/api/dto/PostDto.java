package com.example.chattingweb.api.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class PostDto{

    private int postId;
    private int locationId;
    private int photoId;
    private int userId;
    private String title;
    private String content;
    private MultipartFile file ;

    private String locationRegistered;    //장소 등록여부
    private String locationName;
    private String locationAddress;
    private String longitude;   //경도
    private String latitude;

    private Date modifyDt;
    private Date createDt;
    private String userYn;
    //private boolean locationRegistered; //위치등록여부
}


