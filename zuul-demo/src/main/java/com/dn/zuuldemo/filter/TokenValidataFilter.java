package com.dn.zuuldemo.filter;

import com.alibaba.fastjson.JSONObject;
import com.dn.zuuldemo.NickConfigurationBean;
import com.dn.zuuldemo.jwt.JwtTokenProvider;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 权限验证
 * */
@Component
public class TokenValidataFilter extends ZuulFilter {

    // token工具
    JwtTokenProvider jwtTokenProvider;
    // 自定义的配置
    NickConfigurationBean nickConfigurationBean;

    //初始化时，将我们的自定义配置加入
    public TokenValidataFilter(JwtTokenProvider jwtTokenProvider, NickConfigurationBean nickConfigurationBean) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.nickConfigurationBean = nickConfigurationBean;
    }

    //定义拦截器类型,我们有多种方式可以去对网关进行拦截
    @Override
    public String filterType() {
        //这里表示 在发起请求之前会执行这个filter
        return "pre";
    }

    //定义拦截顺序 数字越小拦截越靠前
    @Override
    public int filterOrder() {
        return 6;
    }

    @Override
    public boolean shouldFilter() {
//        new Exception().printStackTrace();
        RequestContext ctx = RequestContext.getCurrentContext();
        // 根据routeId，过滤掉不需要做权限校验的请求
        return !nickConfigurationBean.getNoAuthenticationRoutes().contains(ctx.get("proxy"));
    }

    @Override
    public Object run() throws ZuulException {
        // zuul中，将当前请求的上下文信息存在线程变量中。取出来
        RequestContext ctx = RequestContext.getCurrentContext();
        // 从上下文中获取httprequest对象
        HttpServletRequest request = ctx.getRequest();
        // 从头部信息中获取Authentication的值，也就是我们的token
        String token = request.getHeader("Authorization");
        if(token == null) {
            forbidden();
            return null;
        }
        // 检验token是否正确
        // 这里只是通过使用key对token进行解码是否成功，并没有对有效期、已经token里面的内容进行校验。
        Claims claims = jwtTokenProvider.parseToken(token);
        if (claims == null) {
            forbidden();
            return null;
        }
        // 可以将token内容输出出来看看
        System.out.println("当前请求的token内容是："+ JSONObject.toJSONString(claims));
        // 拓展：可以在token里面塞一些其他的值，用来做路由验权。
        // 比如在UAAClaims对象中，存储这个token能访问哪些路由。如果当前这个请求对应的route，不在token中，就代表没有请求权限
        // 示例：uaaclaim中有一个scope数组值为[oschia,lession-6-sms-interface],那么就代表这个token只能用于这两个路由的访问
        return null;
    }

    // 设置response的状态码为403
    void forbidden() {
        // zuul中，将请求附带的信息存在线程变量中。
        RequestContext.getCurrentContext().setResponseStatusCode(HttpStatus.FORBIDDEN.value());
        ReflectionUtils.rethrowRuntimeException(new ZuulException("无访问权限", HttpStatus.FORBIDDEN.value(), "token校验不通过"));
    }
}
