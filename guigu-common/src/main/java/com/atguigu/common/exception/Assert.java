package com.atguigu.common.exception;

import com.atguigu.common.result.ResponseEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: admin
 * @date: 2021/10/28 14:49
 * @description:
 */
@Slf4j
public abstract class Assert {

    /**
     * 断言对象不为空
     * 如果对象obj为空，则抛出异常
     * @param obj 待判断对象
     */
    public static void notNull(Object obj, ResponseEnum responseEnum) {
        if (obj == null) {
            log.info("obj is null...............");
            throw new BusinessException(responseEnum);
        }
    }
}