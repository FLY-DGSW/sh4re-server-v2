package sh4re_v2.sh4re_v2.domain.main;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
  @JsonIgnore
  String dbUrl;

  @NotBlank
  @JsonIgnore
  String dbUsername;

  @NotBlank
  @JsonIgnore
  String dbPassword;

  @NotBlank
  @JsonIgnore
  String tenantId;
}
