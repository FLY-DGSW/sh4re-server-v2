package sh4re_v2.sh4re_v2.dto.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class OpenAIResponse {
    private String id;
    private String object;
    private Long created;
    private String model;
    private List<Choice> choices;
    private Usage usage;
    
    @JsonProperty("system_fingerprint")
    private String systemFingerprint;
}