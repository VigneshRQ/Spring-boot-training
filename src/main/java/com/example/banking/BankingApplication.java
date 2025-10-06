package com.example.banking;

import com.example.banking.model.User;
import com.example.banking.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import io.github.cdimascio.dotenv.Dotenv;
@SpringBootApplication

public class BankingApplication {
//    @Autowired
//	private UserRepository repo;

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().load();
		SpringApplication.run(BankingApplication.class, args);
	}

//	@Transactional
//	@PostConstruct
//	public void init(){
//		User u1  = User.builder()
//				.id(1L)
//				.email("rivia@gmail.com")
//				.password("password")
//				.username("Geralt of Rivia")
//				.createdAt(LocalDateTime.now())
//				.updatedAt(LocalDateTime.now()).build();
//		repo.save(u1);

//	}
}
