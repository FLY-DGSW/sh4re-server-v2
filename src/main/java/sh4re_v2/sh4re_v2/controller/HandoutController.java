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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sh4re_v2.sh4re_v2.dto.handout.CreateHandoutResponse;
import sh4re_v2.sh4re_v2.dto.handout.createHandout.CreateHandoutReq;
import sh4re_v2.sh4re_v2.dto.handout.getAllHandouts.GetAllHandoutsRes;
import sh4re_v2.sh4re_v2.dto.handout.getHandout.GetHandoutRes;
import sh4re_v2.sh4re_v2.dto.handout.updateHandout.UpdateHandoutReq;
import sh4re_v2.sh4re_v2.service.tenant.HandoutService;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/handouts")
@RequiredArgsConstructor
public class HandoutController {
  private final HandoutService handoutService;

  @GetMapping
  public ResponseEntity<GetAllHandoutsRes> getAllHandouts(@RequestParam(required = false) Long subjectId) {
    GetAllHandoutsRes response = handoutService.getAllHandouts(subjectId);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{handoutId}")
  public ResponseEntity<GetHandoutRes> getHandout(@PathVariable Long handoutId) {
    GetHandoutRes response = handoutService.getHandout(handoutId);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<CreateHandoutResponse> createHandout(@Valid @RequestBody CreateHandoutReq req) {
    CreateHandoutResponse response = handoutService.createHandout(req);
    return ResponseEntity.created(URI.create("/api/v1/handouts/" + response.id()))
        .body(response);
  }

  @PatchMapping("/{handoutId}")
  public ResponseEntity<Void> updateHandout(@PathVariable Long handoutId, @Valid @RequestBody UpdateHandoutReq req) {
    handoutService.updateHandout(handoutId, req);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{handoutId}")
  public ResponseEntity<Void> deleteHandout(@PathVariable Long handoutId) {
    handoutService.deleteHandout(handoutId);
    return ResponseEntity.noContent().build();
  }
}