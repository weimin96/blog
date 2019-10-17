package com.wiblog.controller;

import com.wiblog.aop.AuthorizeCheck;
import com.wiblog.common.ServerResponse;
import com.wiblog.service.IFileService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author pwm
 * @date 2019/10/1
 */
@Log
@RestController
public class FileController {

    @Autowired
    private IFileService fileService;

    @PostMapping("/uploadImage")
    @AuthorizeCheck(grade = "2")
    public ServerResponse uploadImage(MultipartFile file){
        return fileService.uploadImage(file);
    }

    @PostMapping("/uploadImageForEditorMd")
    @AuthorizeCheck(grade = "2")
    public Map<String,Object> uploadImageForEditorMd(@RequestParam(value = "editormd-image-file", required = true)MultipartFile file){
        return fileService.uploadImageForEditorMd(file);
    }

    @GetMapping("/getImageList")
    @AuthorizeCheck(grade = "2")
    public ServerResponse getImageList(@RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize){
        return fileService.getImageList(pageNum,pageSize);
    }
}
