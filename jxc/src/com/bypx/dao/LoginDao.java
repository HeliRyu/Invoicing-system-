package com.bypx.dao;
import com.bypx.page.UserPage;

//登录注册
public interface LoginDao {
    //勾选框选中后，使用cookie加密保存登录成功后的用户名、密码。再次打开登录页，直接从cookie中解密。
    // （如果一周内免登陆复选框在提交数据时有选中，则跳转前将用户信息存入Cookie，下一次打开登陆页的时候不需要提交用户名密码直接到成功页面。）
    //登录和记住密码，发送用户名和密码到后台进行验证
    public Long login(UserPage page);
    //查询账户是否存在,注册验证昵称，已经注册过的昵称不能使用
    public Long register(UserPage page);
    //注册新增,头像保存到磁盘，路径存入数据库中
    public Integer register_add(UserPage page);
}
