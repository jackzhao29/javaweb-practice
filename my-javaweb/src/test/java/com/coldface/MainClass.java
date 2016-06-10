package com.coldface;

import com.alibaba.fastjson.JSON;
import com.coldface.entity.UserBo;

/**
 * 类MainClass.java的实现描述：TODO 类实现描述
 * @author coldface
 * @date   2016年6月4日下午8:30:07
 */
public class MainClass {
    
    public static void main(String[] args){
        UserBo bo=null;
        for(int i=0;i<20;i++){
            bo=new UserBo();
            bo.setId("0000"+i);
            bo.setName("张三"+i);
            bo.setAge("10");
            bo.setAddress("北京市海淀区"+i);
            System.out.println(JSON.toJSON(bo));
        }
    }

}
