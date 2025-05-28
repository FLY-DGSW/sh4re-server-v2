package sh4re_v2.sh4re_v2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import sh4re_v2.sh4re_v2.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;
}
