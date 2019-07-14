package com.dn.zuuldemo.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;

@Component
@ConfigurationProperties("token.jwt")
public class JwtConfiguration {
	private String key;
	private String iss;
	/** 有效期：分钟 */
	private int expm;
	
	public int getExpm() {
		return expm;
	}
	public void setExpm(int expm) {
		this.expm = expm;
	}

	public String getIss() {
		return iss;
	}

	public void setIss(String iss) {
		this.iss = iss;
	}

	private String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public SecretKeySpec getSecretKeySpec() {
		SecretKeySpec secretKeySpec = new SecretKeySpec(this.getKey()
				.getBytes(), SignatureAlgorithm.HS512.getJcaName());
		return secretKeySpec;
	}

}
