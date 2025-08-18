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
public class Code extends Base {
  @NotBlank
  private String title;

  @NotBlank
  private String language;

  @Column(columnDefinition = "TEXT")
  private String description;

  @NotBlank
  @Column(columnDefinition = "TEXT")
  private String code;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "class_placement_id")
  @NotNull
  private ClassPlacement classPlacement;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "assignment_id")
  private Assignment assignment;

  @NotNull
  private Integer schoolYear;

  @NotNull
  private Long authorId;
}