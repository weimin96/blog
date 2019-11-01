package com.wiblog.thirdparty;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * github登录
 *
 * @author pwm
 * @date 2019/11/1
 */
@Component
public class GithubProvider {

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String secret;

    @Value("${github.redirect.uri}")
    private String redirectUrl;

    @Value("${github.state}")
    private String state;

    /**
     * 获取accessToken
     * @param code code
     * @return String
     */
    public String getAccessToken(String code){
        String url = "https://github.com/login/oauth/access_token?client_id=%s&client_secret=%s&code=%s&redirect_uri=%s&state=%s";
        url = String.format(url,clientId,secret,code,redirectUrl,state);
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url,String.class);
        String token = null;
        if (response != null) {
            token = response.split("&")[0].split("=")[1];
        }
        return token;
    }

    public Map getUser(String token){
        String url = "https://api.github.com/user?token=%s";
        url = String.format(url,token);
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url,String.class);
        return null;
    }
}
