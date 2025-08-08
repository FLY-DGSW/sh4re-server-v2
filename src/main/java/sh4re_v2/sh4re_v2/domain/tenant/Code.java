package sh4re_v2.sh4re_v2.domain.tenant;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
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
public class Code extends Base {
  @NotBlank
  private String title;

  @NotBlank
  private String student;

  @NotBlank
  private String language;

  private String description;

  @NotBlank
  @Lob
  private String code;


  @NotBlank
  private String className;

  private String assignment;

  @NotNull
  private Integer schoolYear;

  @NotNull
  private Long userId;
}