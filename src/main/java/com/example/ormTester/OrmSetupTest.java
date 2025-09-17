package com.example.ormTester;

import jakarta.annotation.PostConstruct;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.Transaction;
//import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.util.Date;

@SpringBootApplication
public class OrmSetupTest {

	@Autowired
	private BankAccountRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(OrmSetupTest.class, args);
	}
    @PostConstruct
	@Transactional /* Transactional basically resolves all the config , config factory , and session handling and does hbm 2 ddl conversions defined in config file*/
	public void init(){
      BankAccountDetails bankObj = new BankAccountDetails();

	  bankObj.setAccountId("ACKR4533");
	  bankObj.setBranchName("SRH Branch");
	  bankObj.setAccountBalance(new BigDecimal("705.56"));
	  bankObj.setCreatedDate(new Date());
	  bankObj.setLastUpdated(new Date());


	  // Under the hood Working of hibernate
//	  Configuration config = new Configuration();
//	  config.addAnnotatedClass(com.example.ormTester.BankAccountDetails.class);
//      config.configure();
//
//	  SessionFactory sf = config.buildSessionFactory();
//	  Session session = sf.openSession();
//
//	  Transaction transaction = session.beginTransaction();
//
//      session.persist(bankObj);
//      transaction.commit();
//	  session.flush(); // Explicitly flush changes
//		session.close(); // Explicitly close session
//		sf.close(); // Close session factory

		// Alternate approach
//		entityManager.persist(bankObj);

		repository.save(bankObj);
	  System.out.println("Magically ----- : "+ bankObj);
	}
}
