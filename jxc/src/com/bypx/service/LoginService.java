package com.bypx.service;

import com.bypx.dao.LoginDao;
import com.bypx.page.UserPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//登录注册
@Service
@Transactional
public class LoginService {
    @Autowired
    private LoginDao loginDao;

    //勾选框选中后，使用cookie加密保存登录成功后的用户名、密码。再次打开登录页，直接从cookie中解密。
    // （如果一周内免登陆复选框在提交数据时有选中，则跳转前将用户信息存入Cookie，下一次打开登陆页的时候不需要提交用户名密码直接到成功页面。）
    //登录和记住密码，发送用户名和密码到后台进行验证
    public Map login(UserPage page, HttpServletRequest request, HttpServletResponse response){
        Map result=new HashMap();
        String lname=page.getName();
        long total= loginDao.login(page);
        if (total>0){
            //使用cookie加密保存登录成功后的用户名和密码
            Cookie cookie=new Cookie("name",lname);
            cookie.setMaxAge(60*60*24*7);
            cookie.setPath("/");
            response.addCookie(cookie);

            HttpSession session=request.getSession();
            session.setAttribute("name",lname);
            result.put("success",true);
            result.put("msg","登录成功");
            return  result;
        }
        result.put("success",false);
        result.put("msg","用户名或密码错误");
        return  result;
    }

    //注册新增
    //注册验证昵称，已经注册过的昵称不能使用，头像保存到磁盘，路径存入数据库中
    public Map register(MultipartFile img, UserPage page, HttpServletRequest request) {
        Map result = new HashMap();
        //查询账户是否存在
        long i= loginDao.register(page);
        if (i > 0) {
            result.put("success", false);
            result.put("msg", "账户已存在");
            return  result;
        }
        //上传头像
        String OriginalFilename=img.getOriginalFilename();
        String suffix=OriginalFilename.substring(OriginalFilename.lastIndexOf("."));
        if (!suffix.equals(".jpg")&&!suffix.equals(".gif")&&!suffix.equals("png")){
            result.put("success", false);
            result.put("msg", "文件类型错误");
            return result;
        }
        if (img.getSize()>1024*1024*2){
            result.put("success", false);
            result.put("msg", "文件超过2m");
            return result;
        }
        //上传文件夹地址
        String up_dir_path=request.getRealPath("/upload");
        File up_dir=new File(up_dir_path);//上传文件夹
        if (!up_dir.exists()){//如果文件夹不存在则创建文件夹
            up_dir.mkdirs();
        }
        //新文件名
        String file_name= UUID.randomUUID().toString()+suffix;
        //新文件路径
        String file_path=up_dir_path+"/"+file_name;
        File file=new File(file_path);
        try{
            //注册新增
            img.transferTo(file);
            page.setId(UUID.randomUUID().toString());
            page.setPhoto("upload/"+file_name);
            Integer add= loginDao.register_add(page);
            if (add>0){
                result.put("success", true);
                result.put("msg", "注册成功");
                return result;
            }else{
                result.put("success", false);
                result.put("msg", "数据有误");
                return result;
            }
        }catch (IOException e){
            e.printStackTrace();
            result.put("success",false);
            result.put("msg","文件上传失败");
            return result;
        }
    }
}
