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
  public ResponseEntity<GetAllAssignmentsRes> getAllAssignmentsBySubjectId(@RequestParam Long subjectId) {
    GetAllAssignmentsRes response = assignmentService.getAllAssignmentsBySubjectId(subjectId);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<GetAssignmentRes> getAssignment(@PathVariable Long id) {
    GetAssignmentRes response = assignmentService.getAssignment(id);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<CreateAssignmentResponse> createAssignment(@Valid @RequestBody CreateAssignmentReq req) {
    CreateAssignmentResponse response = assignmentService.createAssignment(req);
    return ResponseEntity.created(URI.create("/api/v1/assignments/" + response.id()))
        .body(response);
  }

  @PatchMapping
  public ResponseEntity<Void> updateAssignment(@Valid @RequestBody UpdateAssignmentReq req) {
    assignmentService.updateAssignment(req);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping
  public ResponseEntity<Void> deleteAssignment(@Valid @RequestBody DeleteAssignmentReq req) {
    assignmentService.deleteAssignment(req);
    return ResponseEntity.noContent().build();
  }
}