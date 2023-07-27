package com.github.novicezk.midjourney;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import spring.config.BeanConfig;
import spring.config.WebMvcConfig;

@EnableScheduling
@Import({BeanConfig.class, WebMvcConfig.class})
@MapperScan("com.github.novicezk.midjourney.mapper")
@SpringBootApplication(exclude = {JdbcTemplateAutoConfiguration.class})
public class ProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProxyApplication.class, args);
	}

}
