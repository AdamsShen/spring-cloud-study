package com.dn.ribbon.controller;

//import com.dn.ribbon.command.CommandForIndex;
import com.dn.ribbon.command.CommandForIndex;
import com.dn.ribbon.domain.Teacher;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.ribbon.proxy.annotation.Hystrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
public class ConsumerController {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("")
    public Object index(@RequestParam("id")String id){
//        return restTemplate.getForObject("http://localhost:8001/user?id="+id+"",Object.class);
//        return restTemplate.getForObject("http://helloclient/user?id="+id+"",Object.class);
        return new CommandForIndex(id,restTemplate).execute();
    }

    @HystrixCommand(fallbackMethod = "callTimeoutFallback",
            // 配置线程池
            threadPoolProperties = { @HystrixProperty(name = "coreSize", value = "1"),
                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "1") },
            // 配置命令执行相关的，示例：超时时间
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3500") })
    @GetMapping("/get-teacher")
    public Object getTeacher(){
//        return restTemplate.getForEntity("http://localhost:8001",Teacher.class);
        return restTemplate.getForEntity("http://helloclient",Teacher.class);
    }

    public Object callTimeoutFallback(){
        return "查询超时啦，我降级了。";
    }

    @GetMapping("/get-for-teacher")
    public String getStringForTeacher(){
        Teacher teacher = new Teacher();
        teacher.setAge(17);
        teacher.setName("nick");
        teacher.setPassWord("1234");
        teacher.setHeader("good");
        return restTemplate.postForObject("http://localhost:8001/get-teather",teacher,String.class);
//        return restTemplate.postForObject("http://spring-cloud-ribbon-provider/get-teather",teacher,String.class);
    }




}
