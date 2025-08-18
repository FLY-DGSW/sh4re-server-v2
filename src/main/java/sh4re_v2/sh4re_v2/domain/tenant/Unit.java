package sh4re_v2.sh4re_v2.domain.tenant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Unit extends Base {
  @NotBlank
  private String title;

  @Column(columnDefinition = "TEXT")
  private String description;

  @NotNull
  private Integer orderIndex;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "subject_id")
  @NotNull
  private Subject subject;

  @NotNull
  private Long userId;
}