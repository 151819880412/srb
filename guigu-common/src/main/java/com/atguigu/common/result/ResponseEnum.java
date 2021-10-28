package com.atguigu.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author: admin
 * @date: 2021/10/28 14:01
 * @description:
 */
@Getter
@AllArgsConstructor
@ToString
public enum ResponseEnum {

    SUCCESS(0, "成功"),
    ERROR(-1, "服务器内部错误"),
    ;

    // 响应状态码
    private Integer code;
    // 响应信息
    private String message;
}