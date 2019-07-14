package com.dn.feigndemo.model;

import java.io.Serializable;

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
