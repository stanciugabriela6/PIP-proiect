package com.company.ragservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j      // Lombok genereaza automat un logger - folosit pentru log.info(), log.error() etc.
@Service    // Spune Spring ca aceasta clasa e un serviciu - il gestioneaza automat in memorie
public class IngestionService {

    // VectorStore e conexiunea la Chroma - Spring il injecteaza automat
    // pentru ca l-am declarat ca @Bean (sau Spring AI il creeaza automat din properties)
    private final VectorStore vectorStore;

    // Constructor injection - modul recomandat in Spring sa primesti dependinte
    // Spring vede ca IngestionService are nevoie de VectorStore si il "baga" automat
    public IngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    /**
     * Primeste un PDF de la endpoint, il proceseaza si il salveaza in Chroma.
     * Aceasta metoda este apelata doar de angajatul Senior.
     *
     * Fluxul complet:
     * PDF (bytes) -> text -> chunk-uri -> vectori -> Chroma
     */
    public void ingestPdf(MultipartFile file) throws IOException {

        log.info("Incepe procesarea fisierului: {}", file.getOriginalFilename());

        // -------------------------------------------------------
        // PASUL 1: Citim PDF-ul
        // -------------------------------------------------------
        // MultipartFile e fisierul primit prin HTTP upload
        // Il transformam in Resource (format pe care il intelege Spring AI)
        // ByteArrayResource citeste toti bytes din memorie - bun pentru fisiere mici/medii
        Resource pdfResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                // Pastram numele original al fisierului ca metadate
                return file.getOriginalFilename();
            }
        };

        // PagePdfDocumentReader citeste PDF-ul pagina cu pagina
        // Fiecare pagina devine un obiect Document cu continut text + metadate
        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(pdfResource);
        List<Document> pages = pdfReader.get();

        log.info("PDF citit cu succes: {} pagini gasite", pages.size());
        log.info("PAGE SAMPLE: {}", pages.get(0).getText());

        // -------------------------------------------------------
        // PASUL 2: Chunking - taiem documentul in bucati mici
        // -------------------------------------------------------
        // De ce? Modelul de embedding are o limita de tokeni (~512 sau ~8192)
        // Un document de 50 pagini nu incape intr-un singur vector
        // Solutia: taiem in chunk-uri si fiecare chunk devine un vector separat
        TokenTextSplitter splitter = new TokenTextSplitter(
                500,    // chunk size: fiecare bucata are max 500 tokeni (~375 cuvinte)
                100,    // overlap: 100 tokeni comuni intre bucati adiacente
                // overlap-ul exista pentru ca o idee poate fi la granita dintre 2 chunk-uri
                // fara overlap ai putea pierde contextul la granita
                5,      // min chunk size: ignora bucatile mai mici de 5 tokeni (zgomot)
                10000,  // max chunk size: limita de siguranta
                true    // pastreaza separatorii de propozitie pentru context
        );

        List<Document> chunks = splitter.apply(pages);

        log.info("Chunking finalizat: {} chunk-uri create din {} pagini",
                chunks.size(), pages.size());

        // -------------------------------------------------------
        // PASUL 3: Embedding + Salvare in Chroma
        // -------------------------------------------------------
        // vectorStore.add() face automat 2 lucruri in ordine:
        //   1. Trimite fiecare chunk la modelul de embedding (nomic-embed-text prin Groq)
        //      -> primeste un vector de numere (ex: [0.23, -0.87, 0.41, ...] ~768 numere)
        //   2. Salveaza perechea (text original + vector) in Chroma
        //
        // Chroma indexeaza vectorii intr-o structura speciala (HNSW graph)
        // care permite cautare ultra-rapida prin milioane de vectori
        vectorStore.add(chunks);

        log.info("Fisierul '{}' a fost indexat cu succes in Chroma. {} chunk-uri salvate.",
                file.getOriginalFilename(), chunks.size());
    }

    /**
     * Sterge toate documentele din Chroma.
     * Util daca vrei sa re-indexezi totul de la zero.
     */
    public void clearAllDocuments() {
        log.warn("Se sterg TOATE documentele din Chroma!");
        // Implementare optionala - o lasam simpla pentru acum
        log.warn("Pentru stergere, reporneste Chroma Docker cu volum nou.");
    }
}