package sh4re_v2.sh4re_v2.config;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import sh4re_v2.sh4re_v2.security.Role;

/**
 * Configuration class that defines security path patterns for the application.
 * This class centralizes the management of which endpoints require authentication
 * and which ones are publicly accessible.
 */
@Configuration
public class SecurityPathConfig {
    private static final List<Role> student = List.of(Role.STUDENT, Role.TEACHER);
    private static final List<Role> teacher = List.of(Role.TEACHER);
    private static final List<Role> admin = List.of(Role.ADMIN);

    /**
     * Class representing an endpoint with its HTTP method.
     */
    @Getter
    public static class EndpointConfig {
        private final String pattern;
        private final HttpMethod method;
        private final Boolean isAuthenticated;
        private final List<Role> roles;

        public EndpointConfig(String pattern, HttpMethod method, Boolean isAuthenticated, List<Role> roles) {
            this.pattern = pattern;
            this.method = method;
            this.isAuthenticated = isAuthenticated;
            this.roles = roles;
        }

        public EndpointConfig(String pattern, HttpMethod method, Boolean isAuthenticated) {
            this.pattern = pattern;
            this.method = method;
            this.isAuthenticated = isAuthenticated;
            this.roles = new ArrayList<>();
        }

        public EndpointConfig(String pattern, HttpMethod method) {
            this.pattern = pattern;
            this.method = method;
            this.isAuthenticated = false;
            this.roles = new ArrayList<>();
        }

        public EndpointConfig(String pattern) {
            this.pattern = pattern;
            this.method = null;
            this.isAuthenticated = false;
            this.roles = new ArrayList<>();
        }
    }

    /**
     * Array of endpoint patterns that are accessible without authentication.
     * For endpoints where the HTTP method is not specified, all methods are allowed.
     * For endpoints that whether it is authenticated is not specified, it's considered public endpoint.
     * For endpoints that roles are not specified, any roles are allowed.
     */
    public static final EndpointConfig[] endpointConfigs = new EndpointConfig[] {
        // Docs
        new EndpointConfig("/api-docs/**"),
        new EndpointConfig("/api-docs"),
        new EndpointConfig("/swagger-ui/**"),
        new EndpointConfig("/swagger-ui.html"),
        // Auth
        new EndpointConfig("/api/v1/auth/login", HttpMethod.POST),
        new EndpointConfig("/api/v1/auth/register", HttpMethod.POST),
        new EndpointConfig("/api/v1/auth/refresh", HttpMethod.POST),
        new EndpointConfig("/api/v1/auth/reset-password", HttpMethod.POST, true),
        new EndpointConfig("/api/v1/auth/logout", HttpMethod.POST, true),
        // Test
        new EndpointConfig("/api/v1/test/public"),
        new EndpointConfig("/api/v1/test/student", HttpMethod.GET, true),
        new EndpointConfig("/api/v1/test/teacher", HttpMethod.GET, true, teacher),
        // User
        new EndpointConfig("/api/v1/users/me", HttpMethod.GET, true),
        new EndpointConfig("/api/v1/users/theme", HttpMethod.POST, true),
        // Subject
        new EndpointConfig("/api/v1/subjects", HttpMethod.GET, true),
        new EndpointConfig("/api/v1/subjects", HttpMethod.POST, true, teacher),
        new EndpointConfig("/api/v1/subjects", HttpMethod.PATCH, true, teacher),
        new EndpointConfig("/api/v1/subjects", HttpMethod.DELETE, true, teacher),
        // Announcements
        new EndpointConfig("/api/v1/announcements", HttpMethod.GET, true),
        new EndpointConfig("/api/v1/announcements", HttpMethod.POST, true, teacher),
        new EndpointConfig("/api/v1/announcements/{announcementId}", HttpMethod.GET, true),
        new EndpointConfig("/api/v1/announcements/{announcementId}", HttpMethod.PATCH, true, teacher),
        new EndpointConfig("/api/v1/announcements/{announcementId}", HttpMethod.DELETE, true, teacher),
        // Codes
        new EndpointConfig("/api/v1/codes", HttpMethod.GET, true),
        new EndpointConfig("/api/v1/codes", HttpMethod.POST, true),
        new EndpointConfig("/api/v1/codes/{codeId}", HttpMethod.GET, true),
        new EndpointConfig("/api/v1/codes/{codeId}", HttpMethod.PATCH, true),
        new EndpointConfig("/api/v1/codes/{codeId}", HttpMethod.DELETE, true),
        new EndpointConfig("/api/v1/codes/{codeId}/like", HttpMethod.POST, true),
        // Handouts
        new EndpointConfig("/api/v1/handouts", HttpMethod.GET, true),
        new EndpointConfig("/api/v1/handouts", HttpMethod.POST, true, teacher),
        new EndpointConfig("/api/v1/handouts/{handoutId}", HttpMethod.GET, true),
        new EndpointConfig("/api/v1/handouts/{handoutId}", HttpMethod.PATCH, true, teacher),
        new EndpointConfig("/api/v1/handouts/{handoutId}", HttpMethod.DELETE, true, teacher),
    };
}
