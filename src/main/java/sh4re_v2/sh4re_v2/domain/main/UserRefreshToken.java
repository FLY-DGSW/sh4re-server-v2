package sh4re_v2.sh4re_v2.domain.main;

import jakarta.persistence.Entity;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserRefreshToken extends Base {
  private String username;
  private String refreshToken;
  private Date expiryDate;

  public UserRefreshToken(String username, String refreshToken, Date expiryDate) {
    this.username = username;
    this.refreshToken = refreshToken;
    this.expiryDate = expiryDate;
  }
}