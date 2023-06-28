package com.example.store.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class FirebaseConfig {
  @Bean
  public FirebaseApp firebaseApp() throws IOException {
    ClassPathResource serviceAccount = new ClassPathResource("firebase.json");
    FirebaseOptions options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream()))
        .setStorageBucket("store-389608.appspot.com")
        .build();
    return FirebaseApp.initializeApp(options);
  }

}
