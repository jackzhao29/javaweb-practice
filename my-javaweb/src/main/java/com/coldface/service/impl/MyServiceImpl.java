package com.coldface.service.impl;

import org.springframework.stereotype.Service;

import com.coldface.service.MyService;

/**
 * 类MyServiceImpl.java的实现描述：WebSocket业务逻辑类
 * @author coldface
 * @date   2016年6月11日下午4:58:59
 */
@Service
public class MyServiceImpl implements MyService {

    /* (non-Javadoc)
     * @see com.coldface.service.MyService#process(java.lang.String)
     */
    @Override
    public String process(String name) {
        return "MyService"+name.toLowerCase();
    }

}
