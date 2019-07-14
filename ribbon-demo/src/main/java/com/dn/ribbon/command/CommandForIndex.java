package com.dn.ribbon.command;


import com.netflix.hystrix.*;
import org.springframework.web.client.RestTemplate;

public class CommandForIndex extends HystrixCommand<Object> {



    private final RestTemplate template;

    private String id;

    public CommandForIndex(String id, RestTemplate restTemplate) {
        // java代码配置， 只针对这个命令
        super(Setter
                // 必填项。指定命令分组名，主要意义是用于统计
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("DnHello-Group"))
                // 依赖名称（如果是服务调用，这里就写具体的接口名， 如果是自定义的操作，就自己命令），默认是command实现类的类名。 熔断配置就是根据这个名称
                .andCommandKey(HystrixCommandKey.Factory.asKey("ConsumerController"))
                // 线程池命名，默认是HystrixCommandGroupKey的名称。 线程池配置就是根据这个名称
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("DnUser-ThreadPool"))
                // command 熔断相关参数配置
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                                // 配置隔离方式：默认采用线程池隔离。还有一种信号量隔离方式,
                                // .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)
                                // 超时时间500毫秒
                                .withExecutionTimeoutInMilliseconds(100)

                        // 信号量隔离的模式下，最大的请求数。和线程池大小的意义一样
                        // .withExecutionIsolationSemaphoreMaxConcurrentRequests(2)
                        // 熔断时间（熔断开启后，各5秒后进入半开启状态，试探是否恢复正常）
                        // .withCircuitBreakerSleepWindowInMilliseconds(5000)
                )
                // 设置线程池参数
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                        // 线程池大小
                        .withCoreSize(1)
                        //允许最大的缓冲区大小
//                       .withMaxQueueSize(2)
                ));
        // super(HystrixCommandGroupKey.Factory.asKey("DnUser-command"),100);
        this.id = id;
        this.template = restTemplate;
    }

    @Override
    protected Object run() throws Exception {
        System.out.println("###command#######" + Thread.currentThread().toString());
        Object result =  template.getForObject("http://helloclient/user?id="+id+"",Object.class);
        System.out.println(
                "###command结束#######" + Thread.currentThread().toString() + ">><>>>执行结果:" + result.toString());
        return result;
    }

    @Override
    protected Object getFallback() {
        System.out.println("###降级啦####" + Thread.currentThread().toString());
        return "出錯了，我降級了";
        //降级的处理：
        //1.返回一个固定的值
        //2.去查询缓存
        //3.调用一个备用接口
    }
}
