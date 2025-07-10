package sh4re_v2.sh4re_v2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sh4re_v2.sh4re_v2.dto.getMyInfo.GetMyInfoRes;
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
}
