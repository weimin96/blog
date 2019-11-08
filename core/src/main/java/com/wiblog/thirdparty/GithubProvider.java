package com.wiblog.thirdparty;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.wiblog.entity.User;
import com.wiblog.entity.UserAuth;
import com.wiblog.mapper.UserAuthMapper;
import com.wiblog.mapper.UserMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
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

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserAuthMapper userAuthMapper;

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
            System.out.println(token);
        }
        return token;
    }

    /**
     * 获取用户信息
     * @param token token
     * @return Map
     */
    public Map getUser(String token){
        String url = "https://api.github.com/user?access_token=%s";
        url = String.format(url,token);
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url,String.class);
        Gson gson = new Gson();
        return gson.fromJson(response,Map.class);
    }

    /**
     * 用户注册 存入数据库
     * @param githubUser githubUser
     * @param token token
     */
    @Transactional(rollbackFor = Exception.class)
    public User registerGithub(Map githubUser,String token){
        UserAuth userAuth = userAuthMapper.selectOne(new QueryWrapper<UserAuth>()
                .eq("identity_type","github")
                .eq("identifier",githubUser.get("node_id"))
                .eq("state",1));
        User user = new User();
        // 未注册 直接插入数据
        if (userAuth == null){
            user.setAvatarImg((String) githubUser.get("avatar_url"));
            user.setUsername((String) githubUser.get("name"));
            user.setState(true);
            user.setSex("male");
            user.setCreateTime(new Date());
            userMapper.insertReturnId(user);

            userAuth = new UserAuth();
            userAuth.setIdentityType("github");
            userAuth.setIdentifier((String) githubUser.get("node_id"));
            userAuth.setPassword(token);
            userAuth.setUid(user.getUid());
            userAuth.setCreateTime(new Date());
            userAuthMapper.insert(userAuth);
        }else{ // 已经注册 使用原来帐号
            user = userMapper.selectOne(new QueryWrapper<User>().eq("uid",userAuth.getUid()));
        }
        return user;
    }
}
