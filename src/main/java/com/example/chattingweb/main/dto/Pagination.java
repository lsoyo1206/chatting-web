package com.example.chattingweb.main.dto;

import lombok.Data;

@Data
public class Pagination {

    private int currentPage;       //현재페이지
    private int pageSize;   //한 페이지에 게시글 갯수
    private int start;      //page * pageSize -> 현재페이지의 시작 게시글 seq
    private int totalPages;

}
