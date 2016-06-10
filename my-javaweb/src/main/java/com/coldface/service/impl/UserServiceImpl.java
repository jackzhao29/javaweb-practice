package com.coldface.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.coldface.entity.UserBo;
import com.coldface.service.UserService;

/**
 * 类UserServiceImpl.java的实现描述：TODO User接口的实现类
 * @author coldface
 * @date   2016年5月30日下午7:30:04
 */
@Service
public class UserServiceImpl implements UserService {

    private static Logger logger = LogManager.getLogger(UserServiceImpl.class);
    /* (non-Javadoc)
     * @see com.coldface.service.UserService#saveUserBo(com.coldface.entity.UserBo)
     */
    public boolean saveUserBo(UserBo userBo) {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.coldface.service.UserService#batchSaveUserBo(com.coldface.entity.UserBo)
     */
    public boolean batchSaveUserBo(List<UserBo> userBo) {
        // TODO Auto-generated method stub
        logger.info("batchSaveUserBo"+JSON.toJSONString(userBo));
        return true;
    }

}
