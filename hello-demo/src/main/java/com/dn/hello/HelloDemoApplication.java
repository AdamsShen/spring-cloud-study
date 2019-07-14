package com.dn.hello;

import com.dn.hello.dao.UseDao;
import com.dn.hello.service.MovieService;
import com.dn.hello.service.QueryServiceRemoteCall;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.ribbon.proxy.annotation.Hystrix;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@RestController
@RefreshScope
@EnableHystrix
public class HelloDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloDemoApplication.class, args);
    }

    private Random random = new Random();

    @Value("${name}")
    private String name;

    @Value("${age}")
    private int age;

    @Value("${password}")
    private String passWorde;

    @Value("${header}")
    private String header;

    @Autowired
    private UseDao userdao;

    @Autowired
    MovieService batchService;


    //修改代码，让它降级
//    @HystrixCommand(
//            commandProperties = {
//                    @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value = "100")
//            },fallbackMethod = "getFallBackByIndex"
//    )
    @GetMapping("")
    public Object index()throws InterruptedException{
     int sleepTime = random.nextInt(200);
        System.out.println("休眠时间:"+sleepTime);
        Thread.sleep(sleepTime);
     Teacher teacher = new Teacher();
     teacher.setAge(this.age);
     teacher.setName(this.name);
     teacher.setOrder(this.header);
     teacher.setPassWord(this.passWorde);
     return teacher;
    }

    public Object getFallBackByIndex(){
        return "延迟了，所以降级";
    }

    @GetMapping("user")
    public Object getUser(@Param("id")String id){
        System.out.println("服务端已经接收到请求，开始休眠3秒");
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return userdao.get(id);
    }

    @PostMapping("get-teather")
    public String postForTeacher(@RequestBody Object msg){
        return  msg.toString();
    }


    public class Teacher implements Serializable {
        private String  name;
        private int age;
        private String passWord;
        private String header;

        public String getPassWord() {
            return passWord;
        }

        public void setPassWord(String passWord) {
            this.passWord = passWord;
        }

        public String getHeader() {
            return header;
        }

        public void setOrder(String header) {
            this.header = header;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "[name:"+name+",age:"+age+",password:"+passWord+",header:"+header+"]";
        }
    }

    @RequestMapping("/movie/query")
    public Map<String, Object> queryMovie(String movieCode) throws ExecutionException, InterruptedException {
        return batchService.queryMovie(movieCode);
    }



}
