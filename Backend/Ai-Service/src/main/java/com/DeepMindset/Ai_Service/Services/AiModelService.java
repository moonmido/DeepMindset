package com.DeepMindset.Ai_Service.Services;

import com.DeepMindset.Ai_Service.Outputs.HuggingFaceModel;
import com.DeepMindset.Ai_Service.Outputs.KeywordOutput;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AiModelService {

    @Value("${huggingface.api.token}")
    private String huggingFaceApiToken;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String SEARCH_URL = "https://huggingface.co/api/models?search={query}";


    private final static String SYSTEM_PROMPT = """
            You are an intelligent assistant that receives natural language descriptions from users who are looking for machine learning models o 
            🎯 Your task:
            Extract only the **single most relevant keyword** that best represents the user's intent (e.g., "translate", "summarization", "text-generation", "image-cl 
            Return your answer as a **JSON object** in the following format:
            {
              "keyword": "your_keyword_here"
            
            🚫 Do not include any explanation, schema, markdown, or multiple keywords.
            🚫 Do not include commas or lists.
            ✅ Return ONLY one plain keyword  
            
            User: I wanna translation model \s
            AI:
            {
              "keyword": "translate"
            
            User: I'm looking for a model that can convert English to French \s
            AI:
            {
              "keyword": "translate"
            
            User: Need something for generating realistic human faces \s
            AI:
            {
              "keyword": "image-generation"
            
            User: I want to classify customer reviews by sentiment \s
            AI:
            {
              "keyword": "sentiment-analysis"
            
            User: Looking for a chatbot that can answer medical questions \s
            AI:
            {
              "keyword": "question-answering"
            
            User: Can you help me find a model that summarizes long legal documents? \s
            AI:
            {
              "keyword": "summarization"
            }
            """;
    private final ChatClient chatClient;

    public AiModelService(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem(SYSTEM_PROMPT)
                .build();
    }


    public List<HuggingFaceModel> searchModels(String query) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(huggingFaceApiToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        List<KeywordOutput> keywordOutputs = GetKeyword(query);
        String keyword = keywordOutputs.getFirst().keyword();
        ResponseEntity<HuggingFaceModel[]> response = restTemplate.exchange(
                SEARCH_URL,
                HttpMethod.GET,
                entity,
                HuggingFaceModel[].class,
                keyword
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return Arrays.stream(response.getBody())
                    .limit(5)  // limit result count
                    .collect(Collectors.toList());
        }

        return List.of();
    }

    public List<KeywordOutput> GetKeyword(String query) {
        return chatClient.prompt()
                .user(query)
                .call()
                .entity(new ParameterizedTypeReference<List<KeywordOutput>>() {
                });
    }
}
