package com.dn.ribbon.futrue;

import java.util.Random;
import java.util.concurrent.*;

public class TimeOutFallBack {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        RandomCommand command = new RandomCommand();

        Future<String> future = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return command.run();
            }
        });

        String result = null;

        //如果在100毫秒内返回，我们就返回一个正常结果，不然，返回降级方法
        try {
            result = future.get(100, TimeUnit.MILLISECONDS);

        } catch (Exception e) {
            //返回降级方法.
           result =command.fallBack();
        }

        System.out.println("执行的结果是:"+result);
        executorService.shutdown();

    }

    //1：修改为请求服务端。
    //2:定义熔断机制，你可以根据失败数量来确定是否开启熔断
    //3:定义一个定时任务的线程池，然后开始按照固定时间去判断熔断器的状态，然后确定是否打开
    //4:定义一个队列去完成半开启状态。

    private  static Random random = new Random();
    public static  class RandomCommand implements  Command<String>{
        @Override
        public String run() {
            //超时降级 休眠时间
            int sleepTime = random.nextInt(200);
            System.out.println("休眠时间:"+sleepTime);

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "正常返回结果为:nick的微服务专题七";
        }

        @Override
        public String fallBack() {
            return "出错了，我要降级";
        }
    }

}
