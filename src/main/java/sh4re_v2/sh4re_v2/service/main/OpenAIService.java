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

import java.util.List;
import sh4re_v2.sh4re_v2.dto.openai.Message;
import sh4re_v2.sh4re_v2.dto.openai.OpenAIRequest;
import sh4re_v2.sh4re_v2.dto.openai.OpenAIResponse;

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
            
            // 타입 안전한 요청 객체 생성
            Message userMessage = new Message();
            userMessage.setRole("user");
            userMessage.setContent(prompt);
            
            OpenAIRequest requestBody = OpenAIRequest.builder()
                .model(model)
                .messages(List.of(userMessage))
                .maxTokens(300)
                .temperature(0.7)
                .build();
            
            HttpEntity<OpenAIRequest> entity = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<OpenAIResponse> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                entity,
                OpenAIResponse.class
            );
            
            // 타입 안전한 응답 처리
            OpenAIResponse responseBody = response.getBody();
            if (responseBody != null && responseBody.getChoices() != null && !responseBody.getChoices().isEmpty()) {
                String content = responseBody.getChoices().get(0).getMessage().getContent();
                if (content != null && !content.trim().isEmpty()) {
                    return content.trim();
                }
            }
            
            log.warn("OpenAI API response format unexpected or empty content");
            return generateFallbackDescription(code, language, assignment);
            
        } catch (Exception e) {
            log.error("Failed to generate description using OpenAI", e);
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