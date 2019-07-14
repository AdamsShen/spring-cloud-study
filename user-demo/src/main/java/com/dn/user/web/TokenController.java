package com.dn.user.web;

import com.dn.user.jwt.JwtConfiguration;
import com.dn.user.jwt.JwtTokenProvider;

import com.dn.user.jwt.UAAClaims;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 * 获取token
 * 
 */
@RestController
@RequestMapping("/")
public class TokenController {
	@Autowired
	JwtConfiguration jwtConfiguration;

	@Autowired
	JwtTokenProvider jwtTokenProvider;

	// 获取一个根据手机号和密码获取token
	@PostMapping("/token/byPhone")
	public ResponseEntity<?> getTokenByPhone(@RequestBody User user) {
		// 这个实例中没有加入其它逻辑
		// TODO 你可以去数据库里面查有没有这个用户，密码对不对。如果密码不对你就不给他返回token。
		//伪代码: User user = user.selectByUsername(user);
		//if(user == null){
		//   return ResultModel;  **ResultModel为我们封装的结果类
		//
		// }
//		//return ResponseEntity.ok(new JWTToken(jwtTokenProvider.createToken(parseClaims(user)))); **同样的我们可以将我们这一套逻辑封装到result结果集中
//		try {
//			Thread.sleep(3000L);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		
		return ResponseEntity.ok(new JWTToken(jwtTokenProvider.createToken(parseClaims(user))));
	}

	// 将token反解出来，看看里面的内容；
	// 仅用于开发场景
	@RequestMapping("/token/parse")
	public Claims parseToken(String token) {
		return jwtTokenProvider.parseToken(token);
	}

	// UAAClaims这个对象就是token中的内容
	private UAAClaims parseClaims(User user) {
		UAAClaims uaaClaims = new UAAClaims();
		uaaClaims.setIssuer(jwtConfiguration.getIss());
		uaaClaims.setIssuedAt(new Date());
		uaaClaims.setAudience(String.valueOf(user.getAccountId()));
		uaaClaims.setId(UUID.randomUUID().toString());
		uaaClaims.setUserName(user.getUserName());
		uaaClaims.setExpiration(new Date(System.currentTimeMillis() + jwtConfiguration.getExpm() * 1000 * 60));
		uaaClaims.setEmail(user.getEmail());
		uaaClaims.setPhone(user.getPhone());
		uaaClaims.setSubject(String.valueOf(user.getAccountId()));
		uaaClaims.setNotBefore(new Date());
		return uaaClaims;

	}
}
