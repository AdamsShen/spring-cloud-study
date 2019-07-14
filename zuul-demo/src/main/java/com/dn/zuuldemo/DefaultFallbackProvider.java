
package com.dn.zuuldemo;

import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

// 写一个默认的hystrix降级策略
@Component
public  class DefaultFallbackProvider implements FallbackProvider {
	@Override
	public String getRoute() {
		// null 或者 *  代表为默认的fallback
		// route对应eureka中的服务名 或者 你自己在配置文件中 配置的serviceId
		return "*";
	}

	@Override
	public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
		return new ClientHttpResponse() {

			@Override
			public HttpHeaders getHeaders() {
				HttpHeaders headers = new HttpHeaders();
				return headers;
			}

			@Override
			public InputStream getBody() throws IOException {
				return new ByteArrayInputStream("hystrix发现有问题啦".getBytes());
			}

			@Override
			public String getStatusText() throws IOException {
				return "服务有问题啦";
			}

			@Override
			public HttpStatus getStatusCode() throws IOException {
				return HttpStatus.BAD_GATEWAY;
			}

			@Override
			public int getRawStatusCode() throws IOException {
				return 502;
			}

			@Override
			public void close() {

			}
		};
	}


}
