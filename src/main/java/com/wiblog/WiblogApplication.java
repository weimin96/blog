package com.wiblog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author pwm
 */
@MapperScan("com.wiblog.dao")
@SpringBootApplication
public class WiblogApplication {

	public static void main(String[] args) {
		SpringApplication.run(WiblogApplication.class, args);
	}

}
