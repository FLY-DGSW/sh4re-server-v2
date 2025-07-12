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
@RequestMapping("/test")
public class TestController {

    @GetMapping("/public")
    public ResponseEntity<?> publicEndpoint() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Public endpoint - accessible to everyone");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/student")
    @PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER')")
    public ResponseEntity<?> studentEndpoint(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User endpoint - accessible to authenticated users");
        response.put("user", userPrincipal.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/teacher")
    public ResponseEntity<?> teacherEndpoint(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Teacher endpoint - accessible to teachers only");
        response.put("user", userPrincipal.getUsername());
        return ResponseEntity.ok(response);
    }
}