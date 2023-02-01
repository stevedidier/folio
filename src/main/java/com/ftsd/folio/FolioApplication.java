package com.ftsd.folio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.ftsd.folio.image.FileStorageService;

import java.util.Properties;
import java.io.File;


//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@ComponentScan({"com.ftsd.folio", "image"})
// @EnableConfigurationProperties({
// 	FileStorageProperties.class
// })
public class FolioApplication {

	public static void main(String[] args) {
		new File(FileStorageService.uploadDirectory).mkdir();
		SpringApplication.run(FolioApplication.class, args);
	}

	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);
		
		mailSender.setUsername("your email");
		mailSender.setPassword("your password");
		
		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");
		
		return mailSender;
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
