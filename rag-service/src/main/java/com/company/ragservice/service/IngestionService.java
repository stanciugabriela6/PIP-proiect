package com.company.ragservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class IngestionService {

    private final VectorStore vectorStore;

    @Value("${spring.ai.vectorstore.chroma.client.host:http://localhost}")
    private String chromaHost;

    @Value("${spring.ai.vectorstore.chroma.client.port:8001}")
    private int chromaPort;

    @Value("${spring.ai.vectorstore.chroma.collection-name:company-docs}")
    private String collectionName;

    public IngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void ingestPdf(MultipartFile file) throws IOException {
        log.info("Incepe procesarea PDF: {}", file.getOriginalFilename());

        Resource pdfResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };

        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(pdfResource);
        List<Document> pages = pdfReader.get();

        for (Document page : pages) {
            page.getMetadata().put("file_name", safeName(file.getOriginalFilename()));
            page.getMetadata().put("file_type", "pdf");
        }

        ingestDocuments(pages, file.getOriginalFilename());
    }

    public void ingestTxt(MultipartFile file) throws IOException {
        log.info("Incepe procesarea TXT: {}", file.getOriginalFilename());

        String text = new String(file.getBytes(), StandardCharsets.UTF_8);
        if (text.isBlank()) {
            throw new IOException("Fisier TXT gol sau fara continut text.");
        }

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("file_name", safeName(file.getOriginalFilename()));
        metadata.put("file_type", "txt");

        Document doc = new Document(text, metadata);
        ingestDocuments(List.of(doc), file.getOriginalFilename());
    }

    private void ingestDocuments(List<Document> docs, String originalFilename) {
        TokenTextSplitter splitter = new TokenTextSplitter(
                500,
                100,
                5,
                10000,
                true
        );

        List<Document> chunks = splitter.apply(docs);
        vectorStore.add(chunks);

        log.info("Fisierul '{}' a fost indexat cu succes in Chroma. {} chunk-uri salvate.",
                originalFilename, chunks.size());
    }

    private String safeName(String filename) {
        return filename == null || filename.isBlank() ? "document-necunoscut" : filename;
    }

    public void clearAllDocuments() throws Exception {
        String base = chromaHost + ":" + chromaPort;
        HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();

        // 1. Delete the collection
        HttpRequest deleteReq = HttpRequest.newBuilder()
                .uri(URI.create(base + "/api/v1/collections/" + collectionName))
                .DELETE()
                .build();
        HttpResponse<String> deleteResp = http.send(deleteReq, HttpResponse.BodyHandlers.ofString());
        log.info("Stergere colectie '{}': HTTP {}", collectionName, deleteResp.statusCode());

        // 2. Recreate empty collection so Spring AI can use it immediately
        String body = "{\"name\":\"" + collectionName + "\",\"metadata\":{}}";
        HttpRequest createReq = HttpRequest.newBuilder()
                .uri(URI.create(base + "/api/v1/collections"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                .build();
        HttpResponse<String> createResp = http.send(createReq, HttpResponse.BodyHandlers.ofString());
        log.info("Recreare colectie '{}': HTTP {}", collectionName, createResp.statusCode());
    }
}
