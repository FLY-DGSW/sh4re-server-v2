package sh4re_v2.sh4re_v2.config;

import java.util.List;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

/**
 * Configuration class that defines security path patterns for the application.
 * This class centralizes the management of which endpoints require authentication
 * and which ones are publicly accessible.
 */
@Configuration
public class SecurityPathConfig {

    /**
     * Class representing an endpoint with its HTTP method.
     */
    @Getter
    public static class EndpointConfig {
        private final String pattern;
        private final HttpMethod method;

        public EndpointConfig(String pattern, HttpMethod method) {
            this.pattern = pattern;
            this.method = method;
        }

        public EndpointConfig(String pattern) {
            this.pattern = pattern;
            this.method = null;
        }

    }

    /**
     * Array of endpoint patterns that are accessible without authentication.
     * For endpoints where the HTTP method is not specified, all methods are allowed.
     */
    private static final EndpointConfig[] PUBLIC_ENDPOINTS = {
        new EndpointConfig("/api-docs/**"),
        new EndpointConfig("/swagger-ui/**"),
        new EndpointConfig("/api/auth/**"),
    };

    private static final EndpointConfig[] AUTHENTICATED_ENDPOINTS = {
        new EndpointConfig("/api/auth/logout", HttpMethod.POST), // 예시
        // ...추가 가능
    };

    /**
     * Returns the array of endpoint configurations that are accessible without authentication.
     *
     * @return List of public endpoint configurations
     */
    public List<EndpointConfig> getPublicEndpoints() {
        return List.of(PUBLIC_ENDPOINTS);
    }

    public List<EndpointConfig> getAuthenticatedEndpoints() { return List.of(AUTHENTICATED_ENDPOINTS); }
}
