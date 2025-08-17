package sh4re_v2.sh4re_v2.domain.tenant;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Handout extends Base {
  @NotBlank
  private String title;

  @NotBlank
  private String description;

  @NotBlank
  private String author;

  @NotBlank
  private String fileUrl;

  @NotNull
  private Long subjectId;

  @NotNull
  private Long userId;
}