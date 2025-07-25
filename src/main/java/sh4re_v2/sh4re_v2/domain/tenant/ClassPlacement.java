package sh4re_v2.sh4re_v2.domain.tenant;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ClassPlacement extends Base {
  @NotNull
  Long userId;

  @NotNull
  int schoolYear;

  @NotNull
  int grade;

  @NotNull
  int classNumber;

  @NotNull
  int studentNumber;

  public ClassPlacement(
      Long userId,
      int grade,
      int classNumber,
      int studentNumber
  ) {
    this.userId = userId;
    this.schoolYear = LocalDate.now().getYear();
    this.grade = grade;
    this.classNumber = classNumber;
    this.studentNumber = studentNumber;
  }
}
