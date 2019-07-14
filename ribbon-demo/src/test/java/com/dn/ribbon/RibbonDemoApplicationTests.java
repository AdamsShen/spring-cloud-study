package com.dn.ribbon;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RibbonDemoApplicationTests {

    long timed = 0l;

    @Before
    public void start() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        System.out.println("开始测试");
        timed = System.currentTimeMillis();

    }

    @After
    public void end() {
        System.out.println("结束测试,执行时长：" + (System.currentTimeMillis() - timed));
    }

    //注入webApplicationContext
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    //设置mockMvc




    // 模拟的请求数量
    private static final int THREAD_NUM = 300;

    // 倒计数器 juc包中常用工具类
    private CountDownLatch countDownLatch = new CountDownLatch(THREAD_NUM);



    @Test
    public void benchmark() throws IOException {
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
                    mockMvc.perform(MockMvcRequestBuilders.get("/api/login/auth?id="+ finalI)
                            .contentType(MediaType.APPLICATION_JSON)
                    ).andExpect(MockMvcResultMatchers.status().isOk())
                            .andDo(MockMvcResultHandlers.print());

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
    }

}
