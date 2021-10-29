package com.atguigu.srb.sms.client.fallback;

import com.atguigu.srb.sms.client.CoreUserInfoClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 服务熔断，当sms远程调用core服务时core挂掉的备用方案
 * @author: admin
 * @date: 2021/10/29 14:33
 * @description:
 */

@Service
@Slf4j
public class CoreUserInfoClientFallback implements CoreUserInfoClient {

    @Override
    public boolean checkMobile(String mobile) {
        log.error("远程调用失败，服务熔断");
        return false; // 手机号不重复
    }
    
}
