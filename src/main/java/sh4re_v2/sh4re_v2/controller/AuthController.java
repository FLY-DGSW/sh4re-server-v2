package sh4re_v2.sh4re_v2.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sh4re_v2.sh4re_v2.dto.auth.RegisterResponse;
import sh4re_v2.sh4re_v2.dto.auth.login.LoginReq;
import sh4re_v2.sh4re_v2.dto.auth.login.LoginRes;
import sh4re_v2.sh4re_v2.dto.auth.refreshToken.RefreshTokenRes;
import sh4re_v2.sh4re_v2.dto.auth.register.RegisterReq;
import sh4re_v2.sh4re_v2.dto.auth.resetPassword.ResetPasswordReq;
import sh4re_v2.sh4re_v2.service.main.AuthService;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginRes> login(@Valid @RequestBody LoginReq loginRequest, HttpServletResponse response) {
        LoginRes loginResponse = authService.login(loginRequest, response);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@Valid @RequestBody RegisterReq registerRequest) {
        RegisterResponse response = authService.registerUser(registerRequest);
        return ResponseEntity.created(URI.create("/api/v1/users/" + response.userId()))
            .body(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordReq resetPasswordReq) {
        authService.resetPassword(resetPasswordReq);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenRes> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        RefreshTokenRes refreshResponse = authService.refreshToken(request, response);
        return ResponseEntity.ok(refreshResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal UserDetails userDetails) {
        authService.logout(userDetails);
        return ResponseEntity.noContent().build();
    }
}