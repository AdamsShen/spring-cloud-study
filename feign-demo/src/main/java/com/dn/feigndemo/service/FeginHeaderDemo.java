package com.dn.feigndemo.service;

import com.dn.feigndemo.model.Teacher;
import feign.Headers;
import feign.Param;
import org.springframework.web.bind.annotation.RequestMapping;

@Headers("Accept:appliaction/json") //当前接口下，所有的feign请求都会被设置这个头
public interface FeginHeaderDemo {

    @Headers("Content-Type:appliaction/json")
    @RequestMapping("")
    int addOrder(Teacher teacher);

    @Headers("Token:{token}")//设置动态值
    @RequestMapping("")
    Teacher getOrder(@Param("token")String token);
}
