package com.bypx.controller;

import com.bypx.page.UserPage;
import com.bypx.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//登录注册
@Controller
@RequestMapping("/portal")
public class LoginController {

    //勾选框选中后，使用cookie加密保存登录成功后的用户名、密码。再次打开登录页，直接从cookie中解密。
    // （如果一周内免登陆复选框在提交数据时有选中，则跳转前将用户信息存入Cookie，下一次打开登陆页的时候不需要提交用户名密码直接到成功页面。）
    //登录和记住密码，发送用户名和密码到后台进行验证
    @Autowired
    private LoginService loginService;
    @RequestMapping("/login.do")
    @ResponseBody
    public Object login(UserPage page, HttpServletRequest request, HttpServletResponse response){
        return loginService.login(page,request,response);
    }

    //注册新增
    //注册验证昵称，已经注册过的昵称不能使用，头像保存到磁盘，路径存入数据库中
    @RequestMapping("/register.do")
    @ResponseBody
    public Object register(MultipartFile img, UserPage page, HttpServletRequest request){
        return loginService.register(img,page,request);
    }
}

