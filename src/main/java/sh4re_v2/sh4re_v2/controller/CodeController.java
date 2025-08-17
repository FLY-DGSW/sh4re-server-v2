package sh4re_v2.sh4re_v2.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sh4re_v2.sh4re_v2.dto.code.CreateCodeResponse;
import sh4re_v2.sh4re_v2.dto.code.createCode.CreateCodeReq;
import sh4re_v2.sh4re_v2.dto.code.getAllCodes.GetAllCodesRes;
import sh4re_v2.sh4re_v2.dto.code.getCode.GetCodeRes;
import sh4re_v2.sh4re_v2.dto.code.toggleLike.ToggleLikeRes;
import sh4re_v2.sh4re_v2.dto.code.updateCode.UpdateCodeReq;
import sh4re_v2.sh4re_v2.service.tenant.CodeService;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/codes")
@RequiredArgsConstructor
public class CodeController {
  private final CodeService codeService;

  @GetMapping
  public ResponseEntity<GetAllCodesRes> getAllCodes() {
    GetAllCodesRes response = codeService.getAllCodes();
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{codeId}")
  public ResponseEntity<GetCodeRes> getCode(@PathVariable Long codeId) {
    GetCodeRes response = codeService.getCode(codeId);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<CreateCodeResponse> createCode(@Valid @RequestBody CreateCodeReq req) {
    CreateCodeResponse response = codeService.createCode(req);
    return ResponseEntity.created(URI.create("/api/v1/codes/" + response.id()))
        .body(response);
  }

  @PatchMapping("/{codeId}")
  public ResponseEntity<Void> updateCode(@PathVariable Long codeId, @Valid @RequestBody UpdateCodeReq req) {
    codeService.updateCode(codeId, req);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{codeId}")
  public ResponseEntity<Void> deleteCode(@PathVariable Long codeId) {
    codeService.deleteCode(codeId);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{codeId}/like")
  public ResponseEntity<ToggleLikeRes> toggleLike(@PathVariable Long codeId) {
    ToggleLikeRes response = codeService.toggleLike(codeId);
    return ResponseEntity.ok(response);
  }
}