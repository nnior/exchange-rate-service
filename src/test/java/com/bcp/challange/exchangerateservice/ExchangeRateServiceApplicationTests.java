package com.bcp.challange.exchangerateservice;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

class ExchangeRateServiceApplicationTests {

	@Test
	void generateJWTs() {
		for(int i = 1; i<=10; i++) {
			SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
			System.out.println(Encoders.BASE64.encode(key.getEncoded()));
		}
	}

}
