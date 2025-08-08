package sh4re_v2.sh4re_v2.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sh4re_v2.sh4re_v2.dto.code.createCode.CreateCodeReq;
import sh4re_v2.sh4re_v2.dto.code.createCode.CreateCodeRes;
import sh4re_v2.sh4re_v2.dto.code.deleteCode.DeleteCodeRes;
import sh4re_v2.sh4re_v2.dto.code.getAllCodes.GetAllCodesRes;
import sh4re_v2.sh4re_v2.dto.code.getCode.GetCodeRes;
import sh4re_v2.sh4re_v2.dto.code.updateCode.UpdateCodeReq;
import sh4re_v2.sh4re_v2.dto.code.updateCode.UpdateCodeRes;
import sh4re_v2.sh4re_v2.dto.code.toggleLike.ToggleLikeRes;
import sh4re_v2.sh4re_v2.service.tenant.CodeService;

@RestController
@RequestMapping("/codes")
@RequiredArgsConstructor
public class CodeController {
  private final CodeService codeService;

  @GetMapping
  public GetAllCodesRes getAllCodes() {
    return codeService.getAllCodes();
  }

  @GetMapping("/{codeId}")
  public GetCodeRes getCode(@PathVariable Long codeId) {
    return codeService.getCode(codeId);
  }

  @PostMapping
  public CreateCodeRes createCode(@Valid @RequestBody CreateCodeReq req) {
    return codeService.createCode(req);
  }

  @PatchMapping("/{codeId}")
  public UpdateCodeRes updateCode(@PathVariable Long codeId, @Valid @RequestBody UpdateCodeReq req) {
    return codeService.updateCode(codeId, req);
  }

  @DeleteMapping("/{codeId}")
  public DeleteCodeRes deleteCode(@PathVariable Long codeId) {
    return codeService.deleteCode(codeId);
  }

  @PostMapping("/{codeId}/like")
  public ToggleLikeRes toggleLike(@PathVariable Long codeId) {
    return codeService.toggleLike(codeId);
  }
}