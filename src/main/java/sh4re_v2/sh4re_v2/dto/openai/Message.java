package sh4re_v2.sh4re_v2.dto.openai;

import lombok.Data;

@Data
public class Message {
    private String role;
    private String content;
}