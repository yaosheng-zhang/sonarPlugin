package com.zhangys.carplugin.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestTemplateToInterface {
    @Autowired
    private RestTemplate restTemplate;

    public String doGetWith1(String url){
        String responseEntity = restTemplate.getForObject(url,String.class);

        return responseEntity;
    }
}
