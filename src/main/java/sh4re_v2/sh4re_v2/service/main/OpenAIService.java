package sh4re_v2.sh4re_v2.service.main;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAIService {
    
    @Value("${openai.api.key}")
    private String apiKey;
    
    @Value("${openai.model}")
    private String model;
    
    @Value("${openai.api.url}")
    private String apiUrl;
    
    private final RestTemplate restTemplate;
    
    public String generateCodeDescription(String code, String language, String assignment) {
        try {
            String prompt = createPrompt(code, language, assignment);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            requestBody.put("messages", List.of(
                Map.of("role", "user", "content", prompt)
            ));
            requestBody.put("max_tokens", 300);
            requestBody.put("temperature", 0.7);
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(
                "https://api.openai.com/v1/chat/completions",
                HttpMethod.POST,
                entity,
                Map.class
            );
            
            if (response.getBody() != null && response.getBody().containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                }
            }
            
            log.warn("OpenAI API response format unexpected");
            return generateFallbackDescription(code, language, assignment);
            
        } catch (Exception e) {
            log.error("Failed to generate description using OpenAI: {}", e.getMessage());
            return generateFallbackDescription(code, language, assignment);
        }
    }
    
    private String createPrompt(String code, String language, String assignment) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("다음 ").append(language).append(" 코드를 분석하여 간단하고 명확한 설명을 한국어로 작성해주세요.\n\n");
        
        if (assignment != null && !assignment.trim().isEmpty()) {
            prompt.append("과제 내용: ").append(assignment).append("\n\n");
        }
        
        prompt.append("코드:\n").append(code).append("\n\n");
        prompt.append("다음 형식으로 설명해주세요:\n");
        prompt.append("- 코드의 주요 기능과 목적\n");
        prompt.append("- 사용된 주요 알고리즘이나 기법\n");
        prompt.append("- 특징적인 구현 방법\n\n");
        prompt.append("설명은 200자 이내로 간결하게 작성해주세요.");
        
        return prompt.toString();
    }
    
    private String generateFallbackDescription(String code, String language, String assignment) {
        StringBuilder description = new StringBuilder();
        description.append(language).append("로 작성된 코드입니다.");
        
        if (assignment != null && !assignment.trim().isEmpty()) {
            description.append(" 과제: ").append(assignment);
        }
        
        // 코드 길이에 따른 간단한 분석
        int codeLength = code.length();
        if (codeLength > 1000) {
            description.append(" 비교적 복잡한 구조를 가진 코드입니다.");
        } else if (codeLength > 500) {
            description.append(" 중간 복잡도의 코드입니다.");
        } else {
            description.append(" 간단한 구조의 코드입니다.");
        }
        
        return description.toString();
    }
}