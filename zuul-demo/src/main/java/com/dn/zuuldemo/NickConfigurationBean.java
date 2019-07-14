package com.dn.zuuldemo;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties("nick.zuul.token-filter")
public class NickConfigurationBean {
    //将我们要放行路由放进来
    private List<String> noAuthenticationRoutes;

    public List<String> getNoAuthenticationRoutes() {
        return noAuthenticationRoutes;
    }

    public void setNoAuthenticationRoutes(List<String> noAuthenticationRoutes){
        this.noAuthenticationRoutes = noAuthenticationRoutes;
    }
}
