package sh4re_v2.sh4re_v2.domain.tenant;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ClassPlacements extends Base {
  @NotNull
  Long userId;

  @NotNull
  int schoolYear;

  @NotNull
  int grade;

  @NotNull
  int classNumber;
}
