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
public class Subject extends Base {
  @NotBlank
  private String name;

  @NotBlank
  private String description;

  @NotNull
  private Integer schoolYear;

  @NotNull
  private Integer grade;

  @NotNull
  private Integer classNumber;

  @NotNull
  private Long userId; // flyway 추가 및 createSubject 마저 만들기
}
