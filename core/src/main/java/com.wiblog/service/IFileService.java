package com.wiblog.service;

import com.wiblog.common.ServerResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author pwm
 * @date 2019/10/1
 */
public interface IFileService {

    /**
     * 腾讯云对象存储 上传图片
     * @param file file
     * @return ServerResponse
     */
    ServerResponse uploadImage(MultipartFile file);

    /**
     * md编辑器 腾讯云对象存储 上传图片
     * @param file file
     * @return ServerResponse
     */
    Map<String, Object> uploadImageForEditorMd(MultipartFile file);
}
