package com.example.chattingweb.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhotoDto {
    private int photoId;            //시퀀스
    private int postId;             //게시글 시퀀스
    private int groupVOrder;        //화면표시순서 0 부터 시작
    private String filePath;        //이미지 서버 경로
                                    // /usr/local/nas/file_manage/2023-12/bf88e9a2- ~
    private String fileName;        //파일명
    private String fileExtension;   //파일확장자
    private long fileSize;        //파일 사이즈

    private Date createDate;
    private Date updateDate;
    private String useYn;
}
