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
        new EndpointConfig("/api/auth/login", HttpMethod.POST),
        new EndpointConfig("/api/auth/register", HttpMethod.POST),
        new EndpointConfig("/api/auth/refresh", HttpMethod.POST),
        new EndpointConfig("/api/auth/logout", HttpMethod.POST, true),
        // Test
        new EndpointConfig("/test/public"),
        new EndpointConfig("/test/student", HttpMethod.GET, true),
        new EndpointConfig("/test/teacher", HttpMethod.GET, true, teacher),
        // School
        new EndpointConfig("/school/**", null, true, teacher),
        // User
        new EndpointConfig("/user/me", HttpMethod.GET, true),
        new EndpointConfig("/user/theme", HttpMethod.POST, true),
        // Subject
        new EndpointConfig("/subject", HttpMethod.GET, true),
        new EndpointConfig("/subject", HttpMethod.POST, true, teacher),
        new EndpointConfig("/subject", HttpMethod.PATCH, true, teacher),
        new EndpointConfig("/subject", HttpMethod.DELETE, true, teacher),
        // Announcements
        new EndpointConfig("/announcements", HttpMethod.GET, true),
        new EndpointConfig("/announcements/{announcementId}", HttpMethod.GET, true),
        new EndpointConfig("/announcements", HttpMethod.POST, true, teacher),
        new EndpointConfig("/announcements/{announcementId}", HttpMethod.PATCH, true, teacher),
        new EndpointConfig("/announcements/{announcementId}", HttpMethod.DELETE, true, teacher),
    };
}
