package com.ftsd.folio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class FolioApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(FolioApplication.class, args);
	}

	//@Bean
	//public WebMvcConfigurer corsConfigurer() {
		//return new WebMvcConfigurer() {
			//@Override
			//public void addCorsMappings(CorsRegistry registry) {
				//registry.addMapping("/**").allowedOriginPatterns("http://localhost:3000").allowCredentials(true).allowedOrigins("http://localhost:8080");
			//}
		//};
	//}

}
