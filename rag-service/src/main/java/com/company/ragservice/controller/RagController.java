package com.company.ragservice.controller;

import com.company.ragservice.dto.QueryRequest;
import com.company.ragservice.dto.QueryResponse;
import com.company.ragservice.service.IngestionService;
import com.company.ragservice.service.QueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/rag")
@CrossOrigin(origins = "*")
public class RagController {

    private final QueryService queryService;
    private final IngestionService ingestionService;
    private final VectorStore vectorStore;

    public RagController(QueryService queryService, IngestionService ingestionService, VectorStore vectorStore) {
        this.queryService = queryService;
        this.ingestionService = ingestionService;
        this.vectorStore = vectorStore;
    }

    @PostMapping("/query")
    public ResponseEntity<QueryResponse> query(@RequestBody QueryRequest request) {
        if (request.getQuestion() == null || request.getQuestion().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        log.info("Request /query primit: '{}'", request.getQuestion());
        QueryResponse response = queryService.query(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/ingest")
    public ResponseEntity<String> ingest(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Fisierul este gol.");
        }

        String filename = file.getOriginalFilename();
        if (filename == null) {
            return ResponseEntity.badRequest().body("Fisier fara nume.");
        }

        String lower = filename.toLowerCase();
        boolean isPdf = lower.endsWith(".pdf");
        boolean isTxt = lower.endsWith(".txt");

        if (!isPdf && !isTxt) {
            return ResponseEntity.badRequest().body("Doar fisierele PDF si TXT sunt acceptate.");
        }

        log.info("Request /ingest primit pentru fisierul: '{}'", filename);

        try {
            if (isPdf) {
                ingestionService.ingestPdf(file);
            } else {
                ingestionService.ingestTxt(file);
            }
            return ResponseEntity.ok("Documentul '" + filename + "' a fost indexat cu succes in baza de date.");
        } catch (Exception e) {
            log.error("Eroare la indexarea fisierului '{}': {}", filename, e.getMessage());
            return ResponseEntity.internalServerError()
                    .body("Eroare la procesarea documentului: " + e.getMessage());
        }
    }

    @DeleteMapping("/documents")
    public ResponseEntity<String> clearDocuments() {
        try {
            ingestionService.clearAllDocuments();
            return ResponseEntity.ok("Toate documentele au fost sterse din baza de date.");
        } catch (Exception e) {
            log.error("Eroare la stergerea documentelor: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Eroare: " + e.getMessage());
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("RAG Service ruleaza corect!");
    }

    @GetMapping("/debug")
    public ResponseEntity<String> debug() {
        try {
            List<org.springframework.ai.document.Document> docs = vectorStore.similaritySearch(
                    org.springframework.ai.vectorstore.SearchRequest.builder()
                            .query("test")
                            .topK(10)
                            .similarityThreshold(0.0)
                            .build()
            );

            if (docs.isEmpty()) {
                return ResponseEntity.ok("CHROMA E GOL - nu exista niciun document indexat!");
            }

            StringBuilder sb = new StringBuilder();
            sb.append("Gasit ").append(docs.size()).append(" documente in Chroma:\n\n");
            for (var doc : docs) {
                sb.append("--- Document ---\n");
                sb.append("Text (primele 200 caractere): ")
                        .append(doc.getText().substring(0, Math.min(200, doc.getText().length())))
                        .append("\n");
                sb.append("Metadata: ").append(doc.getMetadata()).append("\n\n");
            }
            return ResponseEntity.ok(sb.toString());

        } catch (Exception e) {
            return ResponseEntity.ok("Eroare la debug: " + e.getMessage());
        }
    }
}
