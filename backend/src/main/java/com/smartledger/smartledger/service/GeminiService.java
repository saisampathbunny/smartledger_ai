package com.smartledger.smartledger.service;

import com.smartledger.smartledger.model.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String explainFlag(Transaction transaction, int fraudScore) {
        String prompt = buildPrompt(transaction, fraudScore);

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(Map.of("text", prompt)))
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            String url = apiUrl + "?key=" + apiKey;
            Map response = restTemplate.postForObject(url, entity, Map.class);
            return extractText(response);
        } catch (RestClientException e) {
            return "Could not generate explanation right now.";
        }
    }

    private String buildPrompt(Transaction transaction, int fraudScore) {
        return """
                Explain in one plain-English sentence why this bank transaction was flagged.
                Amount: %s
                Sender: %s
                Receiver: %s
                Timestamp: %s
                Fraud score: %d out of 100 (50+ is suspicious)
                """.formatted(
                transaction.getAmount(),
                transaction.getSenderAccount(),
                transaction.getReceiverAccount(),
                transaction.getTimestamp(),
                fraudScore
        );
    }

    @SuppressWarnings("unchecked")
    private String extractText(Map response) {
        try {
            List<Map> candidates = (List<Map>) response.get("candidates");
            Map content = (Map) candidates.get(0).get("content");
            List<Map> parts = (List<Map>) content.get("parts");
            return (String) parts.get(0).get("text");
        } catch (Exception e) {
            return "Explanation unavailable.";
        }
    }
}
