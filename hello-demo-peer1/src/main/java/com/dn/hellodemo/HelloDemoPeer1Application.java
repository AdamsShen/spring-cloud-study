package com.dn.hellodemo;

import com.dn.hellodemo.dao.UseDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@SpringBootApplication
@EnableDiscoveryClient
@EnableEurekaClient
@RestController
@RefreshScope
public class HelloDemoPeer1Application {

    public static void main(String[] args) {
        SpringApplication.run(HelloDemoPeer1Application.class, args);
    }

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


    @GetMapping("")
    public Object index(){
        Teacher teacher = new Teacher();
        teacher.setAge(this.age);
        teacher.setName(this.name);
        teacher.setOrder(this.header);
        teacher.setPassWord(this.passWorde);
        return teacher;
    }

    @GetMapping("user")
    public Object getUser(@Param("id")String id){
        System.out.println("已经接到了客户端发来的请求:");
//        try {
//            Thread.sleep(3000L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        System.out.println("这是客户端2返回的请求");
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

}
