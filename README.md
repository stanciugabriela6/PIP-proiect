# SmartDocs

An intelligent document assistant that lets employees upload company documents and query them through a conversational RAG chatbot. Authentication is handled via username/password or Face ID.

## Features

- **Face ID login** — register and sign in using facial recognition (Python + `face_recognition` library)
- **Classic login** — username/password authentication with PBKDF2-HMAC-SHA256 password hashing
- **Document upload** — supports PDF and TXT files, ingested into a local vector store; drag-and-drop supported
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
├── rag-service/                   # Spring Boot RAG backend
│   └── src/main/java/
│       ├── controller/            # REST endpoints (/query, /ingest, /documents, /health)
│       ├── service/               # QueryService (RAG pipeline), IngestionService
│       ├── dto/                   # QueryRequest, QueryResponse
│       └── config/                # Spring AI + ChromaDB configuration
│
└── face_recognition_module/       # Python FastAPI facial recognition server
    ├── app.py                     # REST endpoints (/health, /register, /login)
    ├── register.py                # Face embedding capture & storage
    ├── login.py                   # Live embedding comparison (Euclidean distance)
    └── storage.py                 # JSON persistence for face embeddings
```

**Tech stack:**
- **Java 17** + **JavaFX** (UI) + **Swing** (chat & upload panels)
- **Spring Boot 3** + **Spring AI** (RAG service)
- **Ollama** — local LLM inference (`llama3.2` for chat, `nomic-embed-text` for embeddings)
- **ChromaDB** — local vector store (started via Python or Docker)
- **SQLite** — local user database with PBKDF2-HMAC-SHA256 password hashing
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

## First-time Setup

1. **Run the app** — all background services start automatically
2. **Create an account** — click **"Create Account"** on the login screen and fill in username, email, and password
3. *(Optional)* **Register Face ID** — on the Register screen, click **"Register Face ID"** and look at the camera when prompted
4. **Login** — use username/password or click **"Face ID"** on the login screen
5. **Upload documents** — click the upload button in the chat screen, drag-and-drop PDF or TXT files
6. **Ask questions** — type in the chat; select a specific document from the dropdown or leave "All documents" for a global search

## Data Storage

All data is stored locally on your machine — nothing is sent to external servers.

| What | Where |
|------|-------|
| User accounts (SQLite) | `~/.smartdocs/smartdocs.db` |
| Uploaded document names | `~/.smartdocs/docs-registry.txt` |
| Face embeddings | `face_recognition_module/users.json` |
| Document vectors | ChromaDB on `localhost:8001`, collection `company-docs` |

> **Note:** Passwords are never stored in plain text. They are hashed with PBKDF2-HMAC-SHA256 (310,000 iterations, random salt per password).

## Configuration

To override the default Python executable:
```bash
set SMARTDOCS_PYTHON=C:\path\to\python.exe   # Windows
export SMARTDOCS_PYTHON=/usr/bin/python3      # Linux/macOS
```

To use a specific Ollama chat model:
```bash
set OLLAMA_CHAT_MODEL=mistral   # Windows
export OLLAMA_CHAT_MODEL=mistral  # Linux/macOS
```

To point to a remote RAG service:
```bash
set RAG_SERVICE_BASE_URL=http://my-server:8080/api/rag
```

## Testing

Unit tests are written with **JUnit 5** and cover the core components:

```bash
# Windows
mvnw.cmd test

# Linux / macOS
./mvnw test
```

Tested classes: `UserAccount`, `FileItem`, `RagApiClient`, `UploadedDocsRegistry`, `FaceApiClient`, `LoginController`, `RegisterController`, `UserService`, `User`, `HelloApplication`.

All public classes and methods are documented with **JavaDoc**.

## Troubleshooting

**App starts but Face ID button does nothing / returns error**
- Make sure Python is installed and `face-recognition`, `fastapi`, `uvicorn` are installed
- Set `SMARTDOCS_PYTHON` to your Python executable path if auto-detection fails
- Check that port `8000` is not already in use

**Chat returns "Serviciul RAG nu a raspuns"**
- The RAG service (Spring Boot) takes ~30–60 seconds to start on first launch
- Check `rag-service/rag-service.log` for errors
- Make sure Ollama is running (`ollama serve`) and models are pulled

**Ollama model not found**
- Run `ollama pull llama3.2` and `ollama pull nomic-embed-text` manually

**Login fails after updating to the latest version**
- Passwords are now stored as secure hashes. Accounts created with an older version (plain-text passwords) are no longer valid.
- Delete `~/.smartdocs/smartdocs.db` and register again.

**ChromaDB fails to start**
- Install via pip: `pip install chromadb`
- Or run via Docker: `docker run -d --name smartdocs-chroma -p 8001:8000 chromadb/chroma:0.4.24`