package com.dry_code_snippets.api.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import com.dry_code_snippets.api.Repositories.SnippetRepository;

import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
public class AIService {
    private final WebClient webClient;
    private final SnippetRepository snippetRepository;

    public AIService(WebClient.Builder webClientBuilder, SnippetRepository snippetRepository) {
        this.webClient = webClientBuilder.baseUrl("https://api-inference.huggingface.co/models/tiiuae/falcon-7b-instruct")
                .defaultHeader("Authorization", "Bearer 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")
                .build();
        this.snippetRepository = snippetRepository;
    }

    public Mono<String> explainSnippet(Long id) {
        return Mono.fromCallable(() -> snippetRepository.findSnippetDtoById(id))
                .flatMap(optionalSnippet -> {
                    if (optionalSnippet.isEmpty()) {
                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Snippet not found with ID: " + id));
                    }
                    
                    String prompt = "User: I have this code snippet and I need a detailed explanation of what it does. Please analyze each function, method, and important lines, and explain their purpose. \n\n" +
                            "```\n" + optionalSnippet.get().getCode() + "\n```\n\n" +
                            "Assistant:";
                    
                    return callAIForExplanation(prompt);
                });
    }

    private Mono<String> callAIForExplanation(String prompt) {
        return webClient.post()
                .bodyValue(Map.of(
                        "inputs", prompt,
                        "parameters", Map.of(
                                "max_new_tokens", 800,
                                "temperature", 0.1,
                                "top_p", 0.95,
                                "do_sample", false,
                                "return_full_text", false
                        )
                ))
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(60))
                .map(this::processResponse)
                .doOnError(error -> {
                    System.err.println("Error calling AI service: " + error.getMessage());
                    error.printStackTrace();
                })
                .onErrorResume(error -> {
                    if (error instanceof ResponseStatusException) {
                        return Mono.error(error); 
                    } else if (error.toString().contains("timeout")) {
                        return Mono.error(new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "AI service timed out"));
                    } else {
                        return Mono.error(new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, 
                                "Error calling AI service: " + error.getMessage()));
                    }
                });
    }

    private String processResponse(String responseJson) {
        try {
            System.out.println("Raw response: " + responseJson);

            ObjectMapper mapper = new ObjectMapper();

            // Try to parse the JSON response
            if (responseJson.trim().startsWith("[")) {
                List<?> responseArray = mapper.readValue(responseJson, List.class);
                if (!responseArray.isEmpty()) {
                    Object firstItem = responseArray.get(0);
                    if (firstItem instanceof Map) {
                        Map<?, ?> firstItemMap = (Map<?, ?>) firstItem;
                        if (firstItemMap.containsKey("generated_text")) {
                            return cleanupResponse(firstItemMap.get("generated_text").toString());
                        }
                    } else {
                        return cleanupResponse(firstItem.toString());
                    }
                }
            } else {
                Map<?, ?> responseMap = mapper.readValue(responseJson, Map.class);
                if (responseMap.containsKey("generated_text")) {
                    return cleanupResponse(responseMap.get("generated_text").toString());
                }
            }

            // If we can't parse the expected structure, clean up the raw response
            return responseJson
                    .replace("\\n", "\n")
                    .replace("\\\"", "\"")
                    .replace("\\t", "\t");
        } catch (Exception e) {
            System.err.println("Error parsing AI response: " + e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing AI response: " + e.getMessage());
        }
    }

    private String cleanupResponse(String response) {
        String cleanedResponse = response;

        if (cleanedResponse.startsWith("Assistant:")) {
            cleanedResponse = cleanedResponse.substring("Assistant:".length()).trim();
        }

        int userPromptIndex = cleanedResponse.indexOf("User:");
        if (userPromptIndex >= 0) {
            cleanedResponse = cleanedResponse.substring(0, userPromptIndex).trim();
        }

        return cleanedResponse.trim();
    }
}
