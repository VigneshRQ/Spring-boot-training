package com.example.demo;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
    @PostConstruct
	public void init(){
		TestLombok test = new TestLombok();
		test.setId(1L);
		test.setName("Test");
		System.out.println("-----Results-------- : \n" + test.getId() +"\t"+test.getName()+"\n" + test.toString());
	}
}
