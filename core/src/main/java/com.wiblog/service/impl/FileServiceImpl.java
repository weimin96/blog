package com.wiblog.service.impl;

import com.wiblog.common.ServerResponse;
import com.wiblog.service.IFileService;
import com.wiblog.thirdparty.CosApi;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author pwm
 * @date 2019/10/1
 */
@Service
@PropertySource(value = "classpath:/config/wiblog.properties", encoding = "utf-8")
public class FileServiceImpl implements IFileService {

    @Value("${qcloud-oss-secret-id}")
    private String secretId;

    @Value("${qcloud-oss-secret-key}")
    private String secretkey;

    @Value("${qcloud-oss-bucket-site}")
    private String bucketSite;

    @Value("${qcloud-oss-bucket-name}")
    private String bucketName;

    @Value("${qcloud-oss-path}")
    private String path;

    @Override
    public ServerResponse uploadImage(MultipartFile file) {
        if(StringUtils.isBlank(file.getOriginalFilename())){
            return ServerResponse.error("文件为空",40001);
        }
        CosApi cosApi = new CosApi(secretId,secretkey,bucketSite);
        // 生成文件名
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss-");
        String folder = sf.format(date)+file.getOriginalFilename();
        try {
            String eTag = cosApi.uploadFile(file.getInputStream(),folder,bucketName);
            if(eTag != null){
                return ServerResponse.success(path+folder,"图片上传成功");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ServerResponse.error("图片上传失败",40002);
    }

    @Override
    public Map<String, Object> uploadImageForEditorMd(MultipartFile file) {
        // 返回的数据结果
        Map<String, Object> result = new HashMap<>(3);
        if(StringUtils.isNotBlank(file.getOriginalFilename())){
            CosApi cosApi = new CosApi(secretId,secretkey,bucketSite);
            // 生成文件名
            Date date = new Date();
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss-");
            String folder = sf.format(date)+file.getOriginalFilename();
            try {
                String eTag = cosApi.uploadFile(file.getInputStream(),folder,bucketName);
                if(eTag != null){
                    result.put("success", 1);
                    result.put("message", "图片上传成功");
                    result.put("url", path+folder);
                    return result;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        result.put("success", 0);
        result.put("message", "图片上传失败");
        result.put("url", "");
        return result;
    }
}