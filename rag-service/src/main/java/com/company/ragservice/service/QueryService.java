package com.company.ragservice.service;

import com.company.ragservice.dto.QueryRequest;
import com.company.ragservice.dto.QueryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class QueryService {

    private final VectorStore vectorStore;
    private final ChatClient chatClient;

    public QueryService(VectorStore vectorStore, ChatClient chatClient) {
        this.vectorStore = vectorStore;
        this.chatClient = chatClient;
    }

    /**
     * Primeste intrebarea angajatului si returneaza un raspuns generat
     * pe baza documentelor din Chroma.
     *
     * Fluxul complet:
     * intrebare -> vector -> similarity search -> top chunks -> prompt -> LLM -> raspuns
     */
    public QueryResponse query(QueryRequest request) {

        String question = request.getQuestion();
        log.info("Interogare primita: '{}'", question);

        // -------------------------------------------------------
        // PASUL 1: Similarity Search in Chroma
        // -------------------------------------------------------
        // Spring AI transforma automat intrebarea in vector (cu nomic-embed-text)
        // apoi cauta in Chroma vectorii cei mai apropiati geometric
        // "apropiati geometric" = texte cu sens similar
        List<Document> relevantDocs = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(question)        // intrebarea utilizatorului
                        .topK(5)               // returneaza top 5 chunk-uri cele mai relevante
                        .similarityThreshold(0.2) // ignora chunk-urile cu scor de similaritate < 0.5
                        // scorul e intre 0 (total diferit) si 1 (identic)
                        // 0.5 e un prag rezonabil - nu prea strict, nu prea lax
                        .build()
        );

        log.info("Gasit {} chunk-uri relevante in Chroma", relevantDocs.size());

        // -------------------------------------------------------
        // PASUL 2: Verificam daca am gasit ceva
        // -------------------------------------------------------
        if (relevantDocs.isEmpty()) {
            log.warn("Nu s-au gasit documente relevante pentru: '{}'", question);
            return new QueryResponse(
                    "Nu am gasit informatii relevante in documentele companiei pentru aceasta intrebare.",
                    0,
                    "Niciun document relevant"
            );
        }

        // -------------------------------------------------------
        // PASUL 3: Construim contextul din chunk-urile gasite
        // -------------------------------------------------------
        // Luam textul din fiecare chunk si le unim intr-un singur bloc de context
        // "---" intre ele ajuta LLM-ul sa inteleaga ca sunt surse separate
        String context = relevantDocs.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n---\n\n"));

        // Extragem numele fisierelor sursa (din metadate) pentru transparenta
        String sources = relevantDocs.stream()
                .map(doc -> doc.getMetadata().getOrDefault("file_name", "document necunoscut").toString())
                .distinct()     // eliminam duplicatele (mai multe chunk-uri din acelasi fisier)
                .collect(Collectors.joining(", "));

        log.info("Context construit din sursele: {}", sources);

        // -------------------------------------------------------
        // PASUL 4: Construim prompt-ul complet si trimitem la LLM
        // -------------------------------------------------------
        // Aceasta e "Augmentarea" din RAG - augmentam intrebarea cu context specific
        // Prompt-ul are 3 parti:
        //   1. System prompt (definit in AppConfig - personalitatea chatbot-ului)
        //   2. Context (chunk-urile gasite in Chroma)
        //   3. Intrebarea utilizatorului
        String augmentedPrompt = """
            Foloseste EXCLUSIV informatiile din contextul de mai jos pentru a raspunde.
            Daca raspunsul nu se gaseste in context, spune explicit ca nu detii aceasta informatie.
            Nu inventa informatii care nu sunt in context.
            
            CONTEXT DIN DOCUMENTELE COMPANIEI:
            %s
            
            INTREBAREA ANGAJATULUI:
            %s
            """.formatted(context, question);

        // Trimitem la Groq si asteptam raspunsul
        String answer = chatClient.prompt()
                .user(augmentedPrompt)
                .call()
                .content();

        log.info("Raspuns generat cu succes pentru: '{}'", question);

        // -------------------------------------------------------
        // PASUL 5: Returnam raspunsul structurat
        // -------------------------------------------------------
        return new QueryResponse(answer, relevantDocs.size(), sources);
    }
}
