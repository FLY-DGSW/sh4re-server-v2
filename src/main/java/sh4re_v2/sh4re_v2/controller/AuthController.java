package sh4re_v2.sh4re_v2.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sh4re_v2.sh4re_v2.dto.login.LoginReq;
import sh4re_v2.sh4re_v2.dto.login.LoginRes;
import sh4re_v2.sh4re_v2.dto.logout.LogOutRes;
import sh4re_v2.sh4re_v2.dto.refreshToken.RefreshTokenRes;
import sh4re_v2.sh4re_v2.dto.register.RegisterReq;
import sh4re_v2.sh4re_v2.dto.register.RegisterRes;
import sh4re_v2.sh4re_v2.service.main.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public LoginRes login(@Valid @RequestBody LoginReq loginRequest, HttpServletResponse response) {
        return authService.login(loginRequest, response);
    }

    @PostMapping("/register")
    public RegisterRes registerUser(@Valid @RequestBody RegisterReq registerRequest) {
        return authService.registerUser(registerRequest);
    }

    @PostMapping("/refresh")
    public RefreshTokenRes refreshToken(HttpServletRequest request, HttpServletResponse response) {
        return authService.refreshToken(request, response);
    }

    @PostMapping("/logout")
    public LogOutRes logout(@AuthenticationPrincipal UserDetails userDetails) {
        return authService.logout(userDetails);
    }
}