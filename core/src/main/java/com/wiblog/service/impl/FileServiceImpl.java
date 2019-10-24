package com.wiblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wiblog.common.ServerResponse;
import com.wiblog.entity.Picture;
import com.wiblog.mapper.PictureMapper;
import com.wiblog.service.IFileService;
import com.wiblog.thirdparty.CosApi;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private PictureMapper pictureMapper;

    @Value("${qcloud-secret-id}")
    private String secretId;

    @Value("${qcloud-secret-key}")
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
        String fileName = sf.format(date)+file.getOriginalFilename();
        try {
            String eTag = cosApi.uploadFile(file.getInputStream(),fileName,bucketName);
            if(eTag != null){
                Picture picture = new Picture();
                picture.setName(fileName);
                picture.setType("img");
                picture.setUrl(path+fileName);
                picture.setCreateTime(date);
                picture.setUpdateTime(date);
                picture.setExtra("题图");
                pictureMapper.insert(picture);
                return ServerResponse.success(path+fileName,"图片上传成功");
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
            String fileName = sf.format(date)+file.getOriginalFilename();
            try {
                String eTag = cosApi.uploadFile(file.getInputStream(),fileName,bucketName);
                if(eTag != null){
                    result.put("success", 1);
                    result.put("message", "图片上传成功");
                    result.put("url", path+fileName);
                    Picture picture = new Picture();
                    picture.setName(fileName);
                    picture.setType("img");
                    picture.setUrl(path+fileName);
                    picture.setCreateTime(date);
                    picture.setUpdateTime(date);
                    picture.setExtra("内容");
                    pictureMapper.insert(picture);
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

    @Override
    public ServerResponse getImageList(Integer pageNum,Integer pageSize){
        Page<Picture> page = new Page<>(pageNum,pageSize);
        IPage<Picture> iPage = pictureMapper.selectPageList(page);
        return ServerResponse.success(iPage,"获取图片列表成功");
    }
}
