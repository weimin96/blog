package com.wiblog.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author pwm
 */
@SpringBootApplication
@EnableTransactionManagement
public class WiblogApplication {



	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(WiblogApplication.class, args);



	}

}
