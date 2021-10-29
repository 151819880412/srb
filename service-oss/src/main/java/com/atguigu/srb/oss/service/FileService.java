package com.atguigu.srb.oss.service;

import java.io.InputStream;

/**
 * @author: admin
 * @date: 2021/10/29 9:54
 * @description:
 */
public interface FileService {

    /**
     * 文件上传至阿里云
     */
    String upload(InputStream inputStream, String module, String fileName);
}
