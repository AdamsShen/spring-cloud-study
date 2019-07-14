package com.dn.hello.service;

import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author nick QQ:3353113843
 * @company 动脑学院
 * @Date 2019/3/21  17:10
 * @description 动脑学院VIP课代码
 */
@Service
public class QueryServiceRemoteCall {

    public HashMap<String, Object> queryMovieInfoByCode(String code) {
        try {
            Thread.sleep(50L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("movieId", new Random().nextInt(999999999));
        hashMap.put("code", code);
        hashMap.put("star", "nick");
        hashMap.put("isHandsome", "true");
        return hashMap;
    }


    /**
     * 批量查询 - 调用远程的电影信息查询接口
     *
     * @param codes 多个电影编码
     * @return 返回多个电影信息
     */
    public List<Map<String, Object>> queryMovieInfoByCodeBatch(List<String> codes) {
        // 不支持批量查询 http://moviewapi.com/query.do?id=10001     --> {code:10001, star：xxxx.....}
        // http://moviewapi.com/query.do?ids=10001,10002,10003,10004   --> [{code:10001, star///}, {...},{....}]
        List<Map<String, Object>> result = new ArrayList<>();
        for (String code : codes) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("movieId", new Random().nextInt(999999999));
            hashMap.put("code", code);
            hashMap.put("star", "nick");
            hashMap.put("isHandsome", "true");
            result.add(hashMap);
        }
        return result;
    }
}
