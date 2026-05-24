# Architecture - SmartDocs

## 1. Context

SmartDocs este un sistem local-first format dintr-un client desktop Java si doua servicii locale (Face ID + RAG).

## 2. Container view

1. `desktop-client` (Java 17)
- JavaFX: autentificare si inregistrare
- Swing: chatbot, upload panel, profil
- HTTP clients:
  - catre Face API (`http://127.0.0.1:8000`)
  - catre RAG API (`http://localhost:8080/api/rag`)

2. `face-id-service` (Python/FastAPI)
- Endpoints: `/health`, `/register`, `/login`
- Stocare simpla in fisiere locale (`users.json`)

3. `rag-service` (Spring Boot 3)
- Endpoint query: `POST /api/rag/query`
- Endpoint ingest: `POST /api/rag/ingest`
- Endpoint health: `GET /api/rag/health`
- LLM chat: Groq (OpenAI-compatible)
- Embeddings: Ollama local
- Vector store: ChromaDB local

## 3. Fluxuri principale

### 3.1 Login clasic
1. User completeaza username/parola in JavaFX.
2. `UserService` valideaza in memorie.
3. Daca e valid, se deschide UI-ul Swing `ChatBot`.

### 3.2 Login Face ID
1. Clientul verifica/porneste `face-id-service`.
2. Apeleaza `POST /login`.
3. Daca raspunsul este OK, se deschide `ChatBot`.

### 3.3 RAG Query
1. User trimite intrebare din `ChatBot`.
2. Clientul apeleaza `POST /api/rag/query`.
3. `rag-service` face similarity search in Chroma.
4. Construieste prompt augmentat cu context.
5. Trimite la model LLM si returneaza raspunsul.

### 3.4 Ingest document
1. Senior upload-eaza PDF.
2. `rag-service` citeste PDF, split in chunks.
3. Calculeaza embeddings + persistenta in Chroma.

## 4. Structura pachete (client)

- `org.example.proiectpip2`
  - bootstrap JavaFX
  - controllere auth
  - `FaceApiClient`
- `fileUploader`
  - `ui`: chatbot, upload UI, profile UI
  - `ai`: `RagApiClient`
  - `model`: `FileItem`, `FileCategory`
  - `organizr`: mapping fisier -> categorie

## 5. Probleme structurale curente

1. UI mixt JavaFX + Swing (complexitate lifecycle/threading).
2. Doua directoare similare pentru Face ID (`face_recognition` si `face_recognition_module`).
3. `Resources/` este duplicat fata de `src/main/resources`.
4. Persistenta userilor in memorie (fara DB, fara hash parola).
5. Configurari hardcodate (ex. path Python in `FaceApiClient`).

## 6. Directii de organizare (faza urmatoare)

1. Unificare pe un singur toolkit UI (preferabil JavaFX).
2. Eliminare foldere legacy duplicate (`face_recognition`, `Resources`).
3. Externalizare configurari in `.env`/properties.
4. Separare explicita pe layere: `ui`, `application`, `infrastructure`, `domain`.
5. Contracte DTO/API comune intre client si servicii.
