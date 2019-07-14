package com.dn.hello.hystrix;

import com.dn.hello.service.QueryServiceRemoteCall;
import com.netflix.hystrix.*;

import java.util.*;
import java.util.stream.Collectors;

public class MovieCollapserCommand extends HystrixCollapser<List<Map<String,Object>>,Map<String,Object>,String> {


    private QueryServiceRemoteCall queryServiceRemoteCall;

    private String movieCode;

    public MovieCollapserCommand( QueryServiceRemoteCall queryServiceRemoteCall,String movieCode){
        super(Setter.withCollapserKey(HystrixCollapserKey.Factory.asKey("movieCollapseCommand"))
        .andCollapserPropertiesDefaults(
                //定义多久 10毫秒内的请求进行合并
                HystrixCollapserProperties.Setter().withTimerDelayInMilliseconds(10)
                        //定义最多合并200个请求
                // .withMaxRequestsInBatch(200)

        ).andScope(Scope.GLOBAL));

        this.queryServiceRemoteCall = queryServiceRemoteCall;
        this.movieCode =movieCode;
    }

    @Override
    public String getRequestArgument() {
        return movieCode;
    }

    /**
     * 创建实际发起请求的命令
     *
     * */
    @Override
    protected HystrixCommand<List<Map<String, Object>>> createCommand(
            Collection<CollapsedRequest<Map<String, Object>, String>> collapsedRequests) {
        //取出这个时间窗口下的所有请求
        //将参数定义为一个集合
        List<String> movieIds = new ArrayList<>(collapsedRequests.size());
        movieIds.addAll(collapsedRequests.stream().map(CollapsedRequest::getArgument).
                collect(Collectors.toList()));
        System.out.println("查询数据:"+movieIds);
        return new QueryBatchMovieCommand(queryServiceRemoteCall,movieIds) ;
    }

    /**
              * 将结果和请求一一映射
              * */
        @Override
        protected void mapResponseToRequests(List<Map<String, Object>> batchResponse,
                Collection<CollapsedRequest<Map<String, Object>, String>>
                collapsedRequests) {
            Map<String,Object> responseMap = new HashMap<>();
      for(Map<String,Object> rsmap :batchResponse){
          responseMap.put(rsmap.get("code").toString(),rsmap);
      }

      for(CollapsedRequest<Map<String,Object>,String> collapsedRequest : collapsedRequests){
          Object o = responseMap.get(collapsedRequest.getArgument());
          collapsedRequest.setResponse((Map<String, Object>) o);

      }





    }

    private class QueryBatchMovieCommand extends  HystrixCommand<List<Map<String,Object>>>{

        private QueryServiceRemoteCall queryServiceRemoteCall;

        private List<String> movieIds;

        public QueryBatchMovieCommand(QueryServiceRemoteCall queryServiceRemoteCall,List<String> movieIds){
            super(Setter
                    .withGroupKey(HystrixCommandGroupKey.Factory.asKey("goodsPriceBatchCommand"))
                    .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                            .withExecutionTimeoutInMilliseconds(10000))); // 10秒超时
              this.movieIds = movieIds;
              this.queryServiceRemoteCall = queryServiceRemoteCall;
        }

        @Override
        protected List<Map<String, Object>> run() throws Exception {
            System.out.println("调用批量查询，查询数量:"+movieIds.size());
            return queryServiceRemoteCall.queryMovieInfoByCodeBatch(movieIds);
        }
    }
}
