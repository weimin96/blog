package com.wiblog;

import com.wiblog.utils.WordFilterUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author pwm
 */
@SpringBootApplication
@EnableTransactionManagement
public class WiblogApplication {



	public static void main(String[] args) {

		SpringApplication.run(WiblogApplication.class, args);
	}

}
