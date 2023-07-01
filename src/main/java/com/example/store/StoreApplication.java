package com.example.store;

import com.example.store.services.implement.EmailSenderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class StoreApplication {
	@Autowired private EmailSenderServiceImpl emailSenderService;

	public static void main(String[] args) {
		SpringApplication.run(StoreApplication.class, args);
	}

//	@EventListener(ApplicationReadyEvent.class)
//	public void sendEmail(){
//		emailSenderService.sendEmail("long290901@gmail.com",
//				"Email Subject",
//				"GEAR STORE is running");
//	}
}
