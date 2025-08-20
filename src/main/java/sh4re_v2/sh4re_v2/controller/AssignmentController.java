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
import sh4re_v2.sh4re_v2.dto.assignment.CreateAssignmentResponse;
import sh4re_v2.sh4re_v2.dto.assignment.createAssignment.CreateAssignmentReq;
import sh4re_v2.sh4re_v2.dto.assignment.deleteAssignment.DeleteAssignmentReq;
import sh4re_v2.sh4re_v2.dto.assignment.getAllAssignments.GetAllAssignmentsRes;
import sh4re_v2.sh4re_v2.dto.assignment.getAssignment.GetAssignmentRes;
import sh4re_v2.sh4re_v2.dto.assignment.updateAssignment.UpdateAssignmentReq;
import sh4re_v2.sh4re_v2.service.tenant.AssignmentService;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/assignments")
@RequiredArgsConstructor
public class AssignmentController {
  private final AssignmentService assignmentService;

  @GetMapping
  public ResponseEntity<ApiResponse<GetAllAssignmentsRes>> getAllAssignmentsBySubjectId(@RequestParam Long subjectId) {
    GetAllAssignmentsRes response = assignmentService.getAllAssignmentsBySubjectId(subjectId);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<GetAssignmentRes>> getAssignment(@PathVariable Long id) {
    GetAssignmentRes response = assignmentService.getAssignment(id);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @PostMapping
  public ResponseEntity<ApiResponse<CreateAssignmentResponse>> createAssignment(@Valid @RequestBody CreateAssignmentReq req) {
    CreateAssignmentResponse response = assignmentService.createAssignment(req);
    return ResponseEntity.created(URI.create("/api/v1/assignments/" + response.id()))
        .body(ApiResponse.success(response));
  }

  @PatchMapping
  public ResponseEntity<ApiResponse<Void>> updateAssignment(@Valid @RequestBody UpdateAssignmentReq req) {
    assignmentService.updateAssignment(req);
    return ResponseEntity.ok(ApiResponse.success(null));
  }

  @DeleteMapping
  public ResponseEntity<ApiResponse<Void>> deleteAssignment(@Valid @RequestBody DeleteAssignmentReq req) {
    assignmentService.deleteAssignment(req);
    return ResponseEntity.ok(ApiResponse.success(null));
  }
}