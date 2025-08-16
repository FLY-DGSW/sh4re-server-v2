package sh4re_v2.sh4re_v2.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sh4re_v2.sh4re_v2.dto.user.getMyInfo.GetMyInfoRes;
import sh4re_v2.sh4re_v2.dto.user.setTheme.SetThemeReq;
import sh4re_v2.sh4re_v2.service.main.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
  private final UserService userService;

  @GetMapping("/me")
  public ResponseEntity<GetMyInfoRes> getMyInfo() {
    GetMyInfoRes response = userService.getMyInfo();
    return ResponseEntity.ok(response);
  }

  @PostMapping("/theme")
  public ResponseEntity<Void> setTheme(@Valid @RequestBody SetThemeReq setThemeReq) {
    userService.setTheme(setThemeReq);
    return ResponseEntity.noContent().build();
  }
}