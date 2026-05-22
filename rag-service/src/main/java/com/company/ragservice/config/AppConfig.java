package com.company.ragservice.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ChatClient chatClient(OllamaChatModel ollamaChatModel) {
        return ChatClient.builder(ollamaChatModel)
                .defaultSystem("""
                Esti un asistent virtual prietenos pentru angajatii companiei.
                Poti purta conversatii normale (salutari, multumiri, intrebari generale).
                Cand ti se pun intrebari despre documentele companiei, raspunzi EXCLUSIV pe baza informatiilor furnizate in context.
                Cand nu ai context de documente disponibil, raspunzi conversational si natural.
                Daca o intrebare specifica despre companie nu se afla in documente, spui clar ca nu ai aceasta informatie in documentele disponibile.
                Raspunzi intotdeauna in limba in care ti se pune intrebarea.
                """)
                .build();
    }
}
