package com.example.chattingweb.api.dto;

import lombok.Data;

@Data
public class SpaceDto {

    private int postId;
    private String spaceName;
    private String address;
    private String longitude;   //경도
    private String latitude;   //위도
}
