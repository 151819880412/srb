package com.atguigu.srb.core.controller.admin;

import com.atguigu.common.result.R;
import com.atguigu.srb.core.pojo.entity.BorrowInfo;
import com.atguigu.srb.core.service.BorrowInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: admin
 * @date: 2021/11/17 12:56
 * @description:
 */
@Api(tags = "借款管理")
@RestController
@RequestMapping("/admin/core/borrowInfo")
@Slf4j
public class AdminBorrowInfoController {

    @Resource
    private BorrowInfoService borrowInfoService;

    @ApiOperation("借款信息列表")
    @GetMapping("/list")
    public R list() {
        List<BorrowInfo> borrowInfoList = borrowInfoService.selectList();
        return R.ok().data("list", borrowInfoList);
    }
}