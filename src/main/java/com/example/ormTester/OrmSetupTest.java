package com.example.ormTester;

import jakarta.annotation.PostConstruct;
//import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
		Address a1 = new Address();
		a1.setCity("Chennai");
		a1.setState("Tamil Nadu");
		a1.setPincode("600061");

      BankAccountDetails bankObj = new BankAccountDetails();
		BankAccountDetails bankObj1 = new BankAccountDetails();
		bankObj1.setAccountId("ACSK738990");
		bankObj1.setBranchName("CSK 2 Branch");
		bankObj1.setAccountBalance(new BigDecimal("333.89"));
		bankObj1.setCreatedDate(new Date());
		bankObj1.setLastUpdated(new Date());
		bankObj1.setAddress(a1);

	  bankObj.setAccountId("ACSK738990");
	  bankObj.setBranchName("CSK Branch");
	  bankObj.setAccountBalance(new BigDecimal("7777.21"));
	  bankObj.setCreatedDate(new Date());
	  bankObj.setLastUpdated(new Date());
	  bankObj.setAddress(a1);

		a1.setBankAccounts(List.of(bankObj,bankObj1));
//      bankObj.setAddress(a1);
//	  BankAccountDetails bankObj1 = new BankAccountDetails();

	  // Under the hood Working of hibernate for Reading and writing data from DB
//	  SessionFactory config = new Configuration()
//			  .addAnnotatedClass(com.example.ormTester.BankAccountDetails.class)
//			  .addAnnotatedClass(com.example.ormTester.Address.class)
//			  .configure().buildSessionFactory();
//
//	  Session session = config.openSession();
//
//	  try {
//            //Write the object
//			Transaction writeTransaction = session.beginTransaction();
//            session.persist(bankObj);
//			writeTransaction.commit();
//			// Retrieve the object
////			Transaction readTransaction = session.beginTransaction();
////			bankObj1 = session.get(BankAccountDetails.class, "AC10899");
////			readTransaction.commit();
////			Update the object
////		 	 Transaction writeTransaction = session.beginTransaction();
////           session.merge(bankObj);
//		  // Delete the object
////		     bankObj1 = session.find(BankAccountDetails.class,"ACKR4533");
////		     session.remove(bankObj1);
////				writeTransaction.commit();
//
//			System.out.println("Magically ----- : "+ bankObj);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			session.close();
//			config.close();
//		}

		//Update by JPA
//		bankObj1 = repository.findById("ACKR45380").orElse(null);
//		if(bankObj1 != null){
//			bankObj1.setAccountBalance(bankObj.getAccountBalance());
//			bankObj1.setBranchName(bankObj.getBranchName());
//			bankObj1.setLastUpdated(bankObj1.getLastUpdated());
//			bankObj1.setCreatedDate((bankObj.getCreatedDate()));
//			repository.save(bankObj1);
//		}else{
			repository.save(bankObj);
			repository.save(bankObj1);
//		}
//
//		//Delete by JPA
//		repository.deleteById("ACKR45380");
//
//		System.out.println("Magically ----- : "+ bankObj);
	}
}
