package sh4re_v2.sh4re_v2.domain.main;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class School extends Base {
  @NotBlank
  String name;

  @NotBlank
  String code;

  @NotBlank
  String dbUrl;

  @NotBlank
  String dbUsername;

  @NotBlank
  String dbPassword;

  @NotBlank
  String tenantId;
}
