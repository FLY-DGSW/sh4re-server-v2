package sh4re_v2.sh4re_v2.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sh4re_v2.sh4re_v2.common.ApiResponse;
import sh4re_v2.sh4re_v2.dto.user.getMyInfo.GetMyInfoRes;
import sh4re_v2.sh4re_v2.dto.user.setTheme.SetThemeReq;
import sh4re_v2.sh4re_v2.service.main.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
  private final UserService userService;

  @GetMapping("/me")
  public ResponseEntity<ApiResponse<GetMyInfoRes>> getMyInfo() {
    GetMyInfoRes response = userService.getMyInfo();
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @PostMapping("/theme")
  public ResponseEntity<ApiResponse<Void>> setTheme(@Valid @RequestBody SetThemeReq setThemeReq) {
    userService.setTheme(setThemeReq);
    return ResponseEntity.ok(ApiResponse.success(null));
  }
}