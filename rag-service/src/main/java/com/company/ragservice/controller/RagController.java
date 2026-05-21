package com.company.ragservice.controller;

import com.company.ragservice.dto.QueryRequest;
import com.company.ragservice.dto.QueryResponse;
import com.company.ragservice.service.IngestionService;
import com.company.ragservice.service.QueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import java.util.List;

@Slf4j
@RestController                 // Combina @Controller + @ResponseBody
// Spune Spring ca aceasta clasa trateaza cereri HTTP
// si returneaza automat JSON (nu pagini HTML)
@RequestMapping("/api/rag")     // Toate endpoint-urile din aceasta clasa incep cu /api/rag
@CrossOrigin(origins = "*")     // Permite cereri din orice origine (frontend-ul colegului tau)
// Fara asta browser-ul blocheaza cererile din alte porturi
public class RagController {

    private final QueryService queryService;
    private final IngestionService ingestionService;
    private final VectorStore vectorStore;

    public RagController(QueryService queryService, IngestionService ingestionService, VectorStore vectorStore) {
        this.queryService = queryService;
        this.ingestionService = ingestionService;
        this.vectorStore = vectorStore;
    }

    // -------------------------------------------------------
    // ENDPOINT 1: Intrebare catre chatbot
    // -------------------------------------------------------
    // URL complet: POST http://localhost:8080/api/rag/query
    // Cine il apeleaza: colegul cu chatbot-ul, pentru orice angajat (Junior sau Senior)
    // Ce primeste: { "question": "care este politica de concediu?" }
    // Ce returneaza: { "answer": "...", "documentsFound": 3, "sourcesUsed": "..." }
    @PostMapping("/query")
    public ResponseEntity<QueryResponse> query(@RequestBody QueryRequest request) {

        // Validare simpla - nu trimitem intrebari goale la Groq
        if (request.getQuestion() == null || request.getQuestion().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        log.info("Request /query primit: '{}'", request.getQuestion());

        QueryResponse response = queryService.query(request);
        return ResponseEntity.ok(response);
    }

    // -------------------------------------------------------
    // ENDPOINT 2: Upload document (doar Senior)
    // -------------------------------------------------------
    // URL complet: POST http://localhost:8080/api/rag/ingest
    // Cine il apeleaza: colegul cu upload, DOAR pentru angajatul Senior
    // Ce primeste: un fisier PDF prin multipart/form-data
    // Ce returneaza: mesaj de confirmare sau eroare
    @PostMapping("/ingest")
    public ResponseEntity<String> ingest(@RequestParam("file") MultipartFile file) {

        // Validare: fisierul exista?
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Fisierul este gol.");
        }

        // Validare: e PDF?
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase().endsWith(".pdf")) {
            return ResponseEntity.badRequest().body("Doar fisierele PDF sunt acceptate.");
        }

        log.info("Request /ingest primit pentru fisierul: '{}'", filename);

        try {
            ingestionService.ingestPdf(file);
            return ResponseEntity.ok(
                    "Documentul '" + filename + "' a fost indexat cu succes in baza de date."
            );
        } catch (Exception e) {
            log.error("Eroare la indexarea fisierului '{}': {}", filename, e.getMessage());
            return ResponseEntity.internalServerError()
                    .body("Eroare la procesarea documentului: " + e.getMessage());
        }
    }

    // -------------------------------------------------------
    // ENDPOINT 3: Health check
    // -------------------------------------------------------
    // URL complet: GET http://localhost:8080/api/rag/health
    // Folosit pentru a verifica rapid daca aplicatia ruleaza
    // Util pentru colegii tai sa stie daca backend-ul e pornit
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("RAG Service ruleaza corect!");
    }


    // Endpoint de diagnostic - vedem ce e stocat in Chroma
    @GetMapping("/debug")
    public ResponseEntity<String> debug() {
        try {
            // Cautam cu threshold 0 si topK mare ca sa vedem ORICE e in Chroma
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
