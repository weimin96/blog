package com.wiblog.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nls.client.AccessToken;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * TODO 描述
 *
 * @author pwm
 * @date 2019/7/26
 */
public class WeixinUtil {

    private static String access_token;

    private static String access_token_expires;

    private static String jsapi_ticket;

    private static String jsapi_ticket_expires;

    private static final String APPID = "wxe81529641b636020";

    private static final String APPSECRET = "9e66e5a7d182ed9a947f085308bbb2f0";

    private static void setJsApiTicket() {
        if (StringUtils.isBlank(access_token)) {
            setAccessToken();
        }
        if (Long.valueOf(access_token_expires) + 7200 >= Long.valueOf(createTimestamp())){
            setAccessToken();
        }
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
        url = url.replace("ACCESS_TOKEN", access_token);
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        System.out.println(response);
        JSONObject jsonObject = JSONObject.parseObject(response);
        String errcode = jsonObject.getString("errcode");
        if ("0".equals(errcode)) {
            jsapi_ticket = jsonObject.getString("ticket");
            jsapi_ticket_expires = createTimestamp();
        }
    }

    public static String getJsApiTicket() {
        return jsapi_ticket;
    }

    private static void setAccessToken() {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
        url = url.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        access_token = (String) JSONObject.parseObject(response).get("access_token");
        access_token_expires = createTimestamp();
    }

    public static String getAccessToken() {
        return access_token;
    }

    public static Map<String, String> sign(String url) {
        if (StringUtils.isBlank(jsapi_ticket)) {
            setJsApiTicket();
        }
        // 当jsapi_ticket过期
        if (Long.valueOf(jsapi_ticket) + 7200 >= Long.valueOf(createTimestamp())){
            setJsApiTicket();
        }
        Map<String, String> ret = new HashMap<>(5);
        String nonce_str = createNonceStr();
        String timestamp = createTimestamp();
        String string1;
        String signature = "";
        // 注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket +
                "&noncestr=" + nonce_str +
                "&timestamp=" + timestamp +
                "&url=" + url;
        System.out.println(string1);
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ret.put("url", url);
        //注意这里 要加上自己的appId
        ret.put("appId", APPID);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);
        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String createNonceStr() {
        return UUID.randomUUID().toString();
    }

    private static String createTimestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

    public static String getWeixinFile(String mediaId){
        String url = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
        url = url.replace("ACCESS_TOKEN", access_token)
                .replace("MEDIA_ID", mediaId);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> rsp = restTemplate.getForEntity(url, byte[].class);
        return rsp.getBody();
    }

}
