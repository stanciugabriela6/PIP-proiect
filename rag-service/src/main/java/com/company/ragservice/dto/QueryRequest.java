package com.company.ragservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data               // Lombok genereaza automat: getteri, setteri, toString, equals
@NoArgsConstructor  // Lombok genereaza: constructor fara parametri (necesar pentru JSON)
@AllArgsConstructor // Lombok genereaza: constructor cu toti parametrii
public class QueryRequest {

    // Intrebarea trimisa de angajat prin chatbot
    // Exemplu JSON primit: { "question": "care este politica de concediu?" }
    private String question;
}