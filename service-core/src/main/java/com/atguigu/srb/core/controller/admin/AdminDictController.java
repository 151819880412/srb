package com.atguigu.srb.core.controller.admin;

import com.atguigu.common.exception.BusinessException;
import com.atguigu.common.result.R;
import com.atguigu.common.result.ResponseEnum;
import com.atguigu.srb.core.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author: admin
 * @date: 2021/10/29 9:07
 * @description:
 */
@Api(tags = "数据字典管理")
@RestController
@RequestMapping("/admin/core/dict")
@Slf4j
@CrossOrigin
public class AdminDictController {

    @Resource
    DictService dictService;

    @PostMapping("/import")
    @ApiOperation("Excel数据的批量导入")
    public R batchImport(
            @ApiParam(value = "Excel数据字典文件",required = true)
            @RequestParam("file") MultipartFile file){
        try {
            InputStream inputStream = file.getInputStream();
            dictService.importData(inputStream);
            return R.ok().message("数据字段批量导入成功");
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ResponseEnum.UPLOAD_ERROR,e);
        }
    }

}