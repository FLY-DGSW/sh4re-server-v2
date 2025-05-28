package sh4re_v2.sh4re_v2.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sh4re_v2.sh4re_v2.domain.User;
import sh4re_v2.sh4re_v2.dto.login.LoginReq;
import sh4re_v2.sh4re_v2.dto.login.LoginRes;
import sh4re_v2.sh4re_v2.dto.register.RegisterReq;
import sh4re_v2.sh4re_v2.security.Jwt.JwtTokenProvider;
import sh4re_v2.sh4re_v2.security.UserPrincipal;
import sh4re_v2.sh4re_v2.service.AuthService;
import sh4re_v2.sh4re_v2.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    @PostMapping("/login")
    public LoginRes authenticateUser(@Valid @RequestBody LoginReq loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterReq registerRequest) {
        // Check if username already exists
        if (userService.findByUsername(registerRequest.username()).isPresent()) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        // Create new user
        User user = new User();
        user.setUsername(registerRequest.username());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setEmail(registerRequest.email());
        user.setName(registerRequest.name());
        user.setGrade(registerRequest.grade());
        user.setClassNo(registerRequest.classNo());
        user.setStudentNo(registerRequest.studentNo());

        userService.save(user);

        return ResponseEntity.ok("User registered successfully");
    }
}