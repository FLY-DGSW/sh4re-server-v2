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
  public ResponseEntity<GetAllUnitsRes> getAllUnitsBySubjectId(@RequestParam Long subjectId) {
    GetAllUnitsRes response = unitService.getAllUnitsBySubjectId(subjectId);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<GetUnitRes> getUnit(@PathVariable Long id) {
    GetUnitRes response = unitService.getUnit(id);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<CreateUnitResponse> createUnit(@Valid @RequestBody CreateUnitReq req) {
    CreateUnitResponse response = unitService.createUnit(req);
    return ResponseEntity.created(URI.create("/api/v1/units/" + response.id()))
        .body(response);
  }

  @PatchMapping
  public ResponseEntity<Void> updateUnit(@Valid @RequestBody UpdateUnitReq req) {
    unitService.updateUnit(req);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping
  public ResponseEntity<Void> deleteUnit(@Valid @RequestBody DeleteUnitReq req) {
    unitService.deleteUnit(req);
    return ResponseEntity.noContent().build();
  }
}