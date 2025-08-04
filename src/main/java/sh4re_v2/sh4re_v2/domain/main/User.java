package sh4re_v2.sh4re_v2.domain.main;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sh4re_v2.sh4re_v2.common.Theme;
import sh4re_v2.sh4re_v2.security.Role;

@Entity(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User extends Base {

  @Column(nullable = false, length = 12, unique = true)
  private String username;

  @JsonIgnore
  @Column(nullable = false, length = 255)
  private String password;

  @Column(nullable = false, length = 255, unique = true)
  private String email;

  @Column(nullable = false, length = 10)
  private String name;

  @Column(nullable = false)
  private int admissionYear;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role = Role.STUDENT; // Default role

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Theme theme = Theme.LIGHT;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  private School school;

  public User(
      String username,
      String password,
      String email,
      String name,
      School school
  ) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.name = name;
    this.school = school;
    this.admissionYear = LocalDate.now().getYear();
  }
}
