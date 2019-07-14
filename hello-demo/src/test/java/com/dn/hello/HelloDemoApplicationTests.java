package com.dn.hello;

import com.dn.hello.service.MovieService;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest
public class HelloDemoApplicationTests {

    long timed = 0l;

    @Before
    public void start() {
        System.out.println("开始测试");
        timed = System.currentTimeMillis();

    }

    @After
    public void end() {
        System.out.println("结束测试,执行时长：" + (System.currentTimeMillis() - timed));
    }

    // 模拟的请求数量
    private static final int THREAD_NUM = 1000;

    // 倒计数器 juc包中常用工具类
    private CountDownLatch countDownLatch = new CountDownLatch(THREAD_NUM);

@Autowired
    MovieService movieService;

    @Test
    public void benchmark() throws IOException {

        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        // 创建 并不是马上发起请求
        for (int i = 0; i < THREAD_NUM; i++) {
            final String code = "code-" + (i + 1); // 番号
            // 多线程模拟用户查询请求
            int finalI = i;
            Thread thread = new Thread(() -> {
                try {
                    // 代码在这里等待，等待countDownLatch为0，代表所有线程都start，再运行后续的代码
                    countDownLatch.await();
                    // http请求，实际上就是多线程调 用这个方法
                    Map<String, Object> result = movieService.queryMovie(code);
                    System.out.println(Thread.currentThread().getName() + " 查询结束，结果是：" + result);
                } catch (Exception e) {
                    System.out.println(Thread.currentThread().getName() + " 线程执行出现异常:" + e.getMessage());
                }
            });
            thread.setName("price-thread-" + code);
            thread.start();
            // 田径。启动后，倒计时器倒计数 减一，代表又有一个线程就绪了
            countDownLatch.countDown();
        }

        // 输入任意内容退出
        System.in.read();
        context.shutdown();
    }

}
