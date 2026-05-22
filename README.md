# SmartDocs

An intelligent document assistant that lets employees upload company documents and query them through a conversational RAG chatbot. Authentication is handled via username/password or Face ID.

## Features

- **Face ID login** — register and sign in using facial recognition (Python + `face_recognition` library)
- **Document upload** — supports PDF and TXT files, ingested into a local vector store
- **RAG chatbot** — ask questions about uploaded documents; falls back to conversational AI when no relevant chunks are found
- **Conversation history** — the last 3 message pairs are sent with each query for contextual answers
- **User profiles** — editable name, position, age, performance score, and avatar
- **Persistent storage** — user accounts in SQLite, document registry in `~/.smartdocs/`

## Architecture

```
SmartDocs
├── src/main/java/
│   ├── fileUploader/              # Core application
│   │   ├── ui/                    # Swing UI (ChatBot, UploadPanel, MyProfile, WelcomeFX)
│   │   ├── ai/                    # RAG client & document registry
│   │   ├── account/               # UserAccount model
│   │   ├── model/                 # FileItem, FileCategory
│   │   └── organizr/              # File categorization logic
│   └── org.example.proiectpip2/   # Auth & entry point
│       ├── infra/                 # Service bootstrapping (Ollama, Chroma, RAG, Face API)
│       ├── HelloApplication       # JavaFX login screen
│       ├── LoginController        # FXML controller
│       ├── RegisterController     # FXML controller
│       └── UserService            # User CRUD facade
│
└── rag-service/                   # Spring Boot RAG backend
    └── src/main/java/
        ├── controller/            # REST endpoints (/query, /ingest, /documents, /health)
        ├── service/               # QueryService (RAG pipeline), IngestionService
        ├── dto/                   # QueryRequest, QueryResponse
        └── config/                # Spring AI + ChromaDB configuration
```

**Tech stack:**
- **Java 17** + **JavaFX** (UI) + **Swing** (chat & upload panels)
- **Spring Boot 3** + **Spring AI** (RAG service)
- **Ollama** — local LLM inference (`llama3.2` for chat, `nomic-embed-text` for embeddings)
- **ChromaDB** — local vector store (started via Python or Docker)
- **SQLite** — local user database
- **Python** + `face_recognition` + `FastAPI` — facial recognition server

## Requirements

| Tool | Version |
|------|---------|
| Java | 17+ |
| Python | 3.10+ |
| Ollama | latest |
| ChromaDB | via `pip install chromadb` or Docker |

```bash
pip install chromadb face-recognition fastapi uvicorn
ollama pull llama3.2
ollama pull nomic-embed-text
```

## Running

```bash
# Windows
mvnw.cmd clean javafx:run

# Linux / macOS
./mvnw clean javafx:run
```

Maven installs Java and Python dependencies automatically on first build. Ollama, ChromaDB, and the Face API server are started automatically in the background when the app launches.

To override the default Python executable:
```bash
set SMARTDOCS_PYTHON=C:\path\to\python.exe   # Windows
export SMARTDOCS_PYTHON=/usr/bin/python3      # Linux/macOS
```

To point to a remote RAG service:
```bash
set RAG_SERVICE_BASE_URL=http://my-server:8080/api/rag
```