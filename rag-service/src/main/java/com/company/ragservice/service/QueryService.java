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
import java.util.Locale;
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

    public QueryResponse query(QueryRequest request) {
        String question = request.getQuestion();
        String selectedFile = request.getSelectedFile();
        log.info("Interogare primita: '{}' (selectedFile='{}')", question, selectedFile);

        List<Document> relevantDocs = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(question)
                        .topK(8)
                        .similarityThreshold(0.2)
                        .build()
        );

        if (selectedFile != null && !selectedFile.isBlank()) {
            String selectedNorm = selectedFile.toLowerCase(Locale.ROOT);
            relevantDocs = relevantDocs.stream()
                    .filter(doc -> doc.getMetadata().getOrDefault("file_name", "")
                            .toString().toLowerCase(Locale.ROOT).contains(selectedNorm))
                    .collect(Collectors.toList());
        }

        log.info("Gasit {} chunk-uri relevante in Chroma", relevantDocs.size());

        if (relevantDocs.isEmpty()) {
            log.info("Niciun chunk relevant gasit, raspund conversational pentru: '{}'", question);
            try {
                String conversationalPrompt = buildHistoryPrefix(request) + question;
                String answer = chatClient.prompt().user(conversationalPrompt).call().content();
                return new QueryResponse(answer, 0, "");
            } catch (Exception e) {
                log.error("LLM indisponibil pentru query conversational '{}': {}", question, e.getMessage());
                return new QueryResponse(
                        "Serviciul AI este indisponibil momentan. Te rog incearca din nou.",
                        0,
                        ""
                );
            }
        }

        String context = relevantDocs.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n---\n\n"));

        String sources = relevantDocs.stream()
                .map(doc -> doc.getMetadata().getOrDefault("file_name", "document necunoscut").toString())
                .distinct()
                .collect(Collectors.joining(", "));

        String augmentedPrompt = """
            Foloseste EXCLUSIV informatiile din contextul de mai jos pentru a raspunde.
            Daca raspunsul nu se gaseste in context, spune explicit ca nu detii aceasta informatie.
            Nu inventa informatii care nu sunt in context.

            CONTEXT DIN DOCUMENTELE COMPANIEI:
            %s

            %sINTREBAREA ANGAJATULUI:
            %s
            """.formatted(context, buildHistoryPrefix(request), question);

        try {
            String answer = chatClient.prompt().user(augmentedPrompt).call().content();
            return new QueryResponse(answer, relevantDocs.size(), sources);
        } catch (Exception e) {
            log.error("LLM indisponibil pentru query '{}': {}", question, e.getMessage());
            String fallback = relevantDocs.stream()
                    .limit(3)
                    .map(Document::getText)
                    .map(this::shorten)
                    .collect(Collectors.joining("\n\n"));

            String answer = "Serviciul de generare AI este indisponibil momentan. " +
                    "Iata ce am gasit direct in documente:\n\n" + fallback;

            return new QueryResponse(answer, relevantDocs.size(), sources);
        }
    }

    private String buildHistoryPrefix(QueryRequest request) {
        if (request.getHistory() == null || request.getHistory().isEmpty()) return "";
        StringBuilder sb = new StringBuilder("CONVERSATIE ANTERIOARA:\n");
        for (QueryRequest.MessageEntry entry : request.getHistory()) {
            String label = "user".equalsIgnoreCase(entry.getRole()) ? "Angajat" : "Asistent";
            sb.append(label).append(": ").append(entry.getContent()).append("\n");
        }
        sb.append("\n");
        return sb.toString();
    }

    private String shorten(String text) {
        if (text == null) return "";
        String compact = text.replaceAll("\\s+", " ").trim();
        return compact.length() > 500 ? compact.substring(0, 500) + "..." : compact;
    }
}
