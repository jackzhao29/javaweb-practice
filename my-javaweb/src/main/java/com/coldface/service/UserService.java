package com.coldface.service;

import java.util.List;

import com.coldface.entity.UserBo;

/**
 * 类UserService.java的实现描述:UserService接口
 * @author coldface
 * @date   2016年5月30日下午7:27:46
 */
public interface UserService {

    public boolean saveUserBo(UserBo userBo);
    
    public boolean batchSaveUserBo(List<UserBo> userBos);
    
}
