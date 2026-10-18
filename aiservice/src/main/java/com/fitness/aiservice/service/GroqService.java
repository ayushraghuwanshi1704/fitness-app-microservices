package com.fitness.aiservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.util.Map;

@Service
public class GroqService {

    private final WebClient webClient;

    @Value("${groq.api.url:https://api.groq.com/openai/v1/chat/completions}")
    private String groqApiUrl;

    @Value("${groq.api.key:}")
    private String groqApiKey;

    @Value("${groq.api.model:openai/gpt-oss-20b}")
    private String model;

    public GroqService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String getAnswer(String question) {
        if (groqApiKey == null || groqApiKey.isBlank()) {
            throw new IllegalStateException("GROQ_API_KEY is not configured; AI recommendations are unavailable.");
        }

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "temperature", 0.2,
                "max_tokens", 1200,
                "messages", new Object[] {
                        Map.of("role", "system", "content", "Return only valid JSON matching the requested structure. Do not wrap it in markdown."),
                        Map.of("role", "user", "content", question)
                }
        );

        try {
            String response = webClient.post()
                    .uri(groqApiUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + groqApiKey)
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(Duration.ofSeconds(30));

            if (response == null || response.isBlank()) {
                throw new IllegalStateException("Groq returned an empty response.");
            }
            return response;
        } catch (WebClientResponseException e) {
            throw new IllegalStateException("Groq API request failed with HTTP " + e.getStatusCode().value() +
                    (e.getStatusCode().value() == 429 ? " (rate limit exceeded)." : "."), e);
        } catch (RuntimeException e) {
            if (e instanceof IllegalStateException) throw e;
            throw new IllegalStateException("Groq API request failed or timed out.", e);
        }
    }
}
