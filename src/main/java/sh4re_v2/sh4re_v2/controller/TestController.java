package sh4re_v2.sh4re_v2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sh4re_v2.sh4re_v2.security.UserPrincipal;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/public")
    public ResponseEntity<?> publicEndpoint() {
        return ResponseEntity.ok("Public endpoint - accessible to all");
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> userEndpoint(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User endpoint - accessible to authenticated users");
        response.put("user", userPrincipal.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> adminEndpoint(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Admin endpoint - accessible to admins only");
        response.put("user", userPrincipal.getUsername());
        return ResponseEntity.ok(response);
    }
}