package sh4re_v2.sh4re_v2.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sh4re_v2.sh4re_v2.dto.subject.CreateSubjectResponse;
import sh4re_v2.sh4re_v2.dto.subject.createSubject.CreateSubjectReq;
import sh4re_v2.sh4re_v2.dto.subject.deleteSubject.DeleteSubjectReq;
import sh4re_v2.sh4re_v2.dto.subject.getAllSubjects.GetAllSubjectsRes;
import sh4re_v2.sh4re_v2.dto.subject.updateSubject.UpdateSubjectReq;
import sh4re_v2.sh4re_v2.service.tenant.SubjectService;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/subjects")
@RequiredArgsConstructor
public class SubjectController {
  private final SubjectService subjectService;

  @GetMapping
  public ResponseEntity<GetAllSubjectsRes> getAllSubjects() {
    GetAllSubjectsRes response = subjectService.getAllSubjects();
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<CreateSubjectResponse> createSubject(@Valid @RequestBody CreateSubjectReq req) {
    CreateSubjectResponse response = subjectService.createSubject(req);
    return ResponseEntity.created(URI.create("/api/v1/subjects/" + response.id()))
        .body(response);
  }

  @PatchMapping
  public ResponseEntity<Void> updateSubject(@Valid @RequestBody UpdateSubjectReq req) {
    subjectService.updateSubject(req);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping
  public ResponseEntity<Void> deleteSubject(@Valid @RequestBody DeleteSubjectReq req) {
    subjectService.deleteSubject(req);
    return ResponseEntity.noContent().build();
  }
}