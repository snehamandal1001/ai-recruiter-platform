package com.airecruiter.backend.service;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class LlmService {

    @Value("${groq.api.key}")
    private String apiKey;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final JsonMapper mapper = JsonMapper.builder().build();

    public List<String> extractSkills(String resumeText) {
        try {
            String prompt = "Extract a JSON array of technical and soft skills from this resume text. "
                    + "Return ONLY a valid JSON array of strings, nothing else, no markdown formatting. "
                    + "Resume text: " + resumeText;

            // Build the request body as a JSON tree (safer than string concatenation)
            var body = mapper.createObjectNode();
            body.put("model", "llama-3.1-8b-instant");
            var messages = mapper.createArrayNode();
            var message = mapper.createObjectNode();
            message.put("role", "user");
            message.put("content", prompt);
            messages.add(message);
            body.set("messages", messages);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.groq.com/openai/v1/chat/completions"))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            JsonNode root = mapper.readTree(response.body());

            if (root.has("error")) {
                System.err.println("Groq API error: " + root.get("error"));
                return new ArrayList<>();
            }

            String content = root.get("choices").get(0).get("message").get("text") != null
                    ? root.get("choices").get(0).get("message").get("text").asText()
                    : root.get("choices").get(0).get("message").get("content").asText();

            // Clean up in case the model wraps it in markdown code fences
            content = content.replace("```json", "").replace("```", "").trim();

            JsonNode skillsArray = mapper.readTree(content);
            List<String> skills = new ArrayList<>();
            skillsArray.forEach(node -> skills.add(node.asText()));
            return skills;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<String> generateInterviewQuestions(String jobTitle, String jobDescription, List<String> candidateSkills) {
        try {
            String skillsList = String.join(", ", candidateSkills);
            String prompt = "Generate 5 targeted technical interview questions for a candidate with these skills: "
                    + skillsList + ", who is applying for this role: " + jobTitle + " - " + jobDescription + ". "
                    + "Return ONLY a valid JSON array of 5 question strings, nothing else, no markdown formatting.";

            var body = mapper.createObjectNode();
            body.put("model", "llama-3.1-8b-instant");
            var messages = mapper.createArrayNode();
            var message = mapper.createObjectNode();
            message.put("role", "user");
            message.put("content", prompt);
            messages.add(message);
            body.set("messages", messages);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.groq.com/openai/v1/chat/completions"))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode root = mapper.readTree(response.body());

            if (root.has("error")) {
                System.err.println("Groq API error: " + root.get("error"));
                return new ArrayList<>();
            }

            String content = root.get("choices").get(0).get("message").get("content").asText();
            content = content.replace("```json", "").replace("```", "").trim();

            JsonNode questionsArray = mapper.readTree(content);
            List<String> questions = new ArrayList<>();
            questionsArray.forEach(node -> {
                if (node.isTextual()) {
                    // Model returned a plain string, e.g. "What is polymorphism?"
                    questions.add(node.asText());
                } else if (node.isObject() && node.has("question")) {
                    // Model wrapped it in an object, e.g. {"question": "..."}
                    questions.add(node.get("question").asText());
                } else {
                    // Fallback: just stringify whatever came back so nothing is silently dropped
                    questions.add(node.toString());
                }
            });
            return questions;
            
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}