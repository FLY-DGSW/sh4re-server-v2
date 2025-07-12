package sh4re_v2.sh4re_v2.security;

import java.util.function.Supplier;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import sh4re_v2.sh4re_v2.config.SecurityPathConfig.EndpointConfig;

@Component
public class AuthorityChecker {
  public static AuthorizationDecision check(
      Supplier<Authentication> auth,
      RequestAuthorizationContext context,
      EndpointConfig endpoint
  ) {
    boolean isAdmin = auth.get().getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals(Role.ADMIN.getAuthority()));
    if(isAdmin) return new AuthorizationDecision(true);
    if (!endpoint.getIsAuthenticated()) {
      return new AuthorizationDecision(true); // permitAll
    } else if (!endpoint.getRoles().isEmpty()) {
      boolean hasRole = auth.get().getAuthorities().stream()
          .map(GrantedAuthority::getAuthority)
          .anyMatch(role -> endpoint.getRoles().stream()
              .map(Enum::name)
              .map(r -> "ROLE_" + r)
              .anyMatch(r -> r.equals(role)));
      return new AuthorizationDecision(hasRole);
    } else {
      return new AuthorizationDecision(auth.get().isAuthenticated());
    }
  };
}
