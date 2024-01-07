package br.com.erudio.erudioapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class ErudioApplication {

	public static void main(String[] args) {

		SpringApplication.run(ErudioApplication.class, args);

		Map<String, PasswordEncoder> encoders = new HashMap<>();
		Pbkdf2PasswordEncoder pbkdf2PasswordEncoder =
				new Pbkdf2PasswordEncoder(
						"",
						8,
						185000,
						Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256
				);
		encoders.put("pbkdf2", pbkdf2PasswordEncoder);
		DelegatingPasswordEncoder delegatingPasswordEncoder =
				new DelegatingPasswordEncoder(
						"pbkdf2",
						encoders
				);
		delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2PasswordEncoder);
		var result = delegatingPasswordEncoder.encode("admin123");
		System.out.println("My hash " + result);
	}

}
