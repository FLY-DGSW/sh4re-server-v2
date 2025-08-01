package sh4re_v2.sh4re_v2.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sh4re_v2.sh4re_v2.dto.user.getMyInfo.GetMyInfoRes;
import sh4re_v2.sh4re_v2.dto.user.setTheme.SetThemeReq;
import sh4re_v2.sh4re_v2.dto.user.setTheme.SetThemeRes;
import sh4re_v2.sh4re_v2.service.main.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
  private final UserService userService;

  @GetMapping("/me")
  public GetMyInfoRes getMyInfo() {
    return userService.getMyInfo();
  }

  @PostMapping("/theme")
  public SetThemeRes setTheme(@Valid @RequestBody SetThemeReq setThemeReq) {
    return userService.setTheme(setThemeReq);
  }
}
