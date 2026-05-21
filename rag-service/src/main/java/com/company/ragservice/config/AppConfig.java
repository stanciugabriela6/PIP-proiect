package com.company.ragservice.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    // ChatClient - trimite intrebari la Groq si primeste raspunsuri
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem("""
                Esti un asistent virtual pentru angajatii companiei.
                Raspunzi DOAR pe baza documentelor din baza de date.
                Daca informatia nu se afla in documente, spui clar ca nu stii.
                Raspunzi intotdeauna in limba in care ti se pune intrebarea.
                """)
                .build();
    }
}