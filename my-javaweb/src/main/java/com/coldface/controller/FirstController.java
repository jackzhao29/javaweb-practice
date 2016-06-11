package com.coldface.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.coldface.service.MyService;

/**
 * 类FirstController.java的实现描述：TODO 类实现描述
 * @author coldface
 * @date   2016年2月21日下午6:57:32
 */
@Controller
public class FirstController {
    @Autowired  
    private MyService service;  
      
    public FirstController()  
    {  
        System.out.println("I am a controller.");  
    }  
  
    @RequestMapping("/mvc/first/hello.do")  
    @ResponseBody  
    public String hello(@RequestParam("userName") String userName) {  
        return service.process(userName);  
    }  
  
}
