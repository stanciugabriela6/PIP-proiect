package com.company.ragservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryResponse {

    // Raspunsul generat de LLM pe baza documentelor
    private String answer;

    // Numarul de documente gasite relevante (util pentru debug)
    private int documentsFound;

    // Daca vrei sa trimiti si sursa documentelor (optional, util pentru transparenta)
    private String sourcesUsed;
}