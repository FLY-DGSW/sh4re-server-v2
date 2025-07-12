package sh4re_v2.sh4re_v2.domain.tenant;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Subject extends Base {
  @NotBlank
  private String name;

  @NotBlank
  private String description;

  @NotNull
  private int schoolYear;

  @NotNull
  private int classNumber;
}
