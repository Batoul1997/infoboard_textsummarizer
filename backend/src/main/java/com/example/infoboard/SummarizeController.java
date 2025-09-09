package com.example.infoboard;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class SummarizeController {

    private static final Logger logger = LoggerFactory.getLogger(SummarizeController.class);

    @Value("${OPENAI_API_KEY}")
    private String apiKey;

    @PostMapping("/summarize")
    public ResponseEntity<?> summarize(@RequestBody Map<String, String> payload) {
        String input = payload.get("text");

        // Entferne alle leeren Zeilen oder Zeilenumbrüche
        input = input.replaceAll("[\\r\\n]+", " ").trim(); // Ersetzt alle Zeilenumbrüche durch ein Leerzeichen

        // Stelle sicher, dass der Text nicht leer ist
        if (input == null || input.isEmpty()) {
            logger.error("Empfangener Text ist leer.");
            return ResponseEntity.status(400).body(Map.of("error", "Der Text darf nicht leer sein."));
        }

        // Optional: Länge des Textes überprüfen
        if (input.length() < 10) {
            logger.error("Empfangener Text ist zu kurz.");
            return ResponseEntity.status(400).body(Map.of("error", "Der Text ist zu kurz, bitte gib mehr Inhalt an."));
        }

        // JSON-Body als String zusammengebaut
        String body = "{\n" +
                "  \"model\": \"gpt-4o\",\n" +
                "  \"messages\": [\n" +
                "    {\"role\": \"system\", \"content\": \"Du bist ein hilfreicher Assistent, der Texte zusammenfasst.\"},\n" +
                "    {\"role\": \"user\", \"content\": \"Fasse den folgenden Text kurz und kompakt zusammen:\\n" + input + "\"}\n" +
                "  ]\n" +
                "}";


        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            Map response = restTemplate.postForObject("https://api.openai.com/v1/chat/completions", entity, Map.class);
            String summary = ((Map)((Map)((java.util.List)response.get("choices")).get(0)).get("message")).get("content").toString();
            return ResponseEntity.ok(Map.of("summary", summary));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Fehler bei der OpenAI-Anfrage."));
        }
    }
}
