package com.sibros.api;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class SibrosApiBackendApplication {
    
	
	public static void main(String[] args) {
		SpringApplication.run(SibrosApiBackendApplication.class, args);
	}
	
	
	
    

}
