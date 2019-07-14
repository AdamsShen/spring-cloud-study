package com.dn.hello.service;

import com.dn.hello.hystrix.MovieCollapserCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class MovieService {


    @Autowired
    QueryServiceRemoteCall queryServiceRemoteCall;

    public Map<String,Object> queryMovie(String movieCode) throws ExecutionException, InterruptedException {
//        Request request = new Request();
//        request.movieCode = movieCode;
//        queue.add(request);
//        //------ 开始使用我们的CompletableFuture-----------
//        CompletableFuture<Map<String,Object>> completableFuture = new CompletableFuture();
//        request.completableFuture = completableFuture;
//
//        return completableFuture.get();
        //所有请求不再即时处理


        /**
         * @author nick QQ:3353113843
         * @company 动脑学院
         * @Date 2019/3/21  17:41
         * @description 请求合并代码
         */

        MovieCollapserCommand command = new MovieCollapserCommand(queryServiceRemoteCall,movieCode);
        Map<String,Object> rs = command.queue().get();
        return rs;
    }
}
