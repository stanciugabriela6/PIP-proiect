# ADR 0001 - Organize repository as multi-component local system

- Status: Accepted
- Date: 2026-05-22

## Context

Proiectul contine client desktop Java, serviciu Face ID Python si serviciu RAG Spring. Structura era partial implicita si greu de urmarit.

## Decision

Stabilim structura canonica:
- `src/` pentru client desktop Java
- `rag-service/` pentru backend RAG
- `face_recognition_module/` pentru backend Face ID
- `docs/architecture/` pentru descriere arhitectura
- `docs/adr/` pentru decizii arhitecturale

Folderele duplicate (`Resources/`, `face_recognition/`) raman temporar marcate legacy pana la migrare completa.

## Consequences

Pozitive:
- onboarding mai rapid
- limite clare intre componente
- baza pentru refactor incremental

Negative:
- exista in continuare artefacte legacy pana la cleanup complet
