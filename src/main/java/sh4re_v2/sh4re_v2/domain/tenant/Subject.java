package sh4re_v2.sh4re_v2.domain.tenant;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
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
}
