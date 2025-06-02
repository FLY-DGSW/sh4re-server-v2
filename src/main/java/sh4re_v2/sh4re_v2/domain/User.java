package sh4re_v2.sh4re_v2.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sh4re_v2.sh4re_v2.security.Role;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User extends Base {
  @NotBlank
  private String username;

  @NotBlank
  private String password;

  @NotBlank
  private String email;

  @NotBlank
  private String name;

  @NotNull
  private int grade;

  @NotNull
  private int classNo;

  @NotNull
  private int studentNo;

  @Enumerated(EnumType.STRING)
  private Role role = Role.USER; // Default role

//  School school

  public User(String username, String password, String email, String name, int grade, int classNo, int studentNo) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.name = name;
    this.grade = grade;
    this.classNo = classNo;
    this.studentNo = studentNo;
  }

}
