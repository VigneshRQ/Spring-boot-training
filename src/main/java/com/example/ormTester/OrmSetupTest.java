package com.example.ormTester;

import jakarta.annotation.PostConstruct;
//import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
//import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
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

	  BankAccountDetails bankObj1 = new BankAccountDetails();

	  // Under the hood Working of hibernate for Reading and writing data from DB
//	  SessionFactory config = new Configuration()
//			  .addAnnotatedClass(com.example.ormTester.BankAccountDetails.class)
//			  .configure().buildSessionFactory();
//
//	  Session session = config.openSession();
//
//	  try {
//            //Write the object
////			Transaction writeTransaction = session.beginTransaction();
////            session.persist(bankObj);
////			writeTransaction.commit();
//			// Retrieve the object
//			Transaction readTransaction = session.beginTransaction();
//			bankObj1 = session.get(BankAccountDetails.class, "AC10899");
//			readTransaction.commit();
//
//			System.out.println("Magically ----- : "+ bankObj1);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			session.close();
//			config.close();
//		}
		bankObj1 = repository.findById("AC10899").orElse(null);;
		System.out.println("Magically ----- : "+ bankObj1);
	}
}
