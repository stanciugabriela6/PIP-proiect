# SmartDocs

Aplicatie RAG de tip chatbot pentru gestionarea si interogarea documentelor, cu autentificare prin Face ID.

## Cerinte

- **Java 17+** — [download](https://adoptium.net/)
- **Python 3.10+** — [download](https://www.python.org/downloads/)

## Rulare

```bash
./mvnw clean javafx:run
```

Pe Windows:
```bash
mvnw.cmd clean javafx:run
```

Maven instaleaza automat dependintele Java si Python la primul build.

## Functionalitati

- Autentificare cu username/parola sau Face ID
- Incarcare si organizare documente
- Chatbot RAG pentru interogarea documentelor
- Inregistrare cont cu recunoastere faciala