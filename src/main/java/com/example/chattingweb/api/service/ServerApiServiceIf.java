package com.example.chattingweb.api.service;

import java.util.Map;

public interface ServerApiServiceIf {

    public String foodSearch(Map<String,Object> param);
    public String foodSearchImage(Map<String,Object> param);
}
