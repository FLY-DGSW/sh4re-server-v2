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
import sh4re_v2.sh4re_v2.common.ApiResponse;
import sh4re_v2.sh4re_v2.dto.unit.CreateUnitResponse;
import sh4re_v2.sh4re_v2.dto.unit.createUnit.CreateUnitReq;
import sh4re_v2.sh4re_v2.dto.unit.deleteUnit.DeleteUnitReq;
import sh4re_v2.sh4re_v2.dto.unit.getAllUnits.GetAllUnitsRes;
import sh4re_v2.sh4re_v2.dto.unit.getUnit.GetUnitRes;
import sh4re_v2.sh4re_v2.dto.unit.updateUnit.UpdateUnitReq;
import sh4re_v2.sh4re_v2.service.tenant.UnitService;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/units")
@RequiredArgsConstructor
public class UnitController {
  private final UnitService unitService;

  @GetMapping
  public ResponseEntity<ApiResponse<GetAllUnitsRes>> getAllUnitsBySubjectId(@RequestParam(required = true) Long subjectId) {
    GetAllUnitsRes response = unitService.getAllUnitsBySubjectId(subjectId);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<GetUnitRes>> getUnit(@PathVariable Long id) {
    GetUnitRes response = unitService.getUnit(id);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @PostMapping
  public ResponseEntity<ApiResponse<CreateUnitResponse>> createUnit(@Valid @RequestBody CreateUnitReq req) {
    CreateUnitResponse response = unitService.createUnit(req);
    return ResponseEntity.created(URI.create("/api/v1/units/" + response.id()))
        .body(ApiResponse.success(response));
  }

  @PatchMapping
  public ResponseEntity<ApiResponse<Void>> updateUnit(@Valid @RequestBody UpdateUnitReq req) {
    unitService.updateUnit(req);
    return ResponseEntity.ok(ApiResponse.success(null));
  }

  @DeleteMapping
  public ResponseEntity<ApiResponse<Void>> deleteUnit(@Valid @RequestBody DeleteUnitReq req) {
    unitService.deleteUnit(req);
    return ResponseEntity.ok(ApiResponse.success(null));
  }
}