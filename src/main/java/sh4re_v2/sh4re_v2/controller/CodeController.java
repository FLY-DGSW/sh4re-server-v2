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
import sh4re_v2.sh4re_v2.common.ApiResponse;
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
  public ResponseEntity<ApiResponse<GetAllCodesRes>> getAllCodes() {
    GetAllCodesRes response = codeService.getAllCodes();
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @GetMapping("/{codeId}")
  public ResponseEntity<ApiResponse<GetCodeRes>> getCode(@PathVariable Long codeId) {
    GetCodeRes response = codeService.getCode(codeId);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @PostMapping
  public ResponseEntity<ApiResponse<CreateCodeResponse>> createCode(@Valid @RequestBody CreateCodeReq req) {
    CreateCodeResponse response = codeService.createCode(req);
    return ResponseEntity.created(URI.create("/api/v1/codes/" + response.id()))
        .body(ApiResponse.success(response));
  }

  @PatchMapping("/{codeId}")
  public ResponseEntity<ApiResponse<Void>> updateCode(@PathVariable Long codeId, @Valid @RequestBody UpdateCodeReq req) {
    codeService.updateCode(codeId, req);
    return ResponseEntity.ok(ApiResponse.success(null));
  }

  @DeleteMapping("/{codeId}")
  public ResponseEntity<ApiResponse<Void>> deleteCode(@PathVariable Long codeId) {
    codeService.deleteCode(codeId);
    return ResponseEntity.ok(ApiResponse.success(null));
  }

  @PostMapping("/{codeId}/like")
  public ResponseEntity<ApiResponse<ToggleLikeRes>> toggleLike(@PathVariable Long codeId) {
    ToggleLikeRes response = codeService.toggleLike(codeId);
    return ResponseEntity.ok(ApiResponse.success(response));
  }
}