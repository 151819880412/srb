package com.atguigu.srb.sms.SmsService;

import java.util.Map;

/**
 * @author: admin
 * @date: 2021/10/29 10:39
 * @description:
 */
public interface SmsService {

    void send(String mobile, String templateCode, Map<String,Object> param);
}