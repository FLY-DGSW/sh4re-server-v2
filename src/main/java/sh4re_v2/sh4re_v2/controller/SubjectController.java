package sh4re_v2.sh4re_v2.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sh4re_v2.sh4re_v2.dto.subject.createSubject.CreateSubjectReq;
import sh4re_v2.sh4re_v2.dto.subject.createSubject.CreateSubjectRes;
import sh4re_v2.sh4re_v2.dto.subject.deleteSubject.DeleteSubjectReq;
import sh4re_v2.sh4re_v2.dto.subject.deleteSubject.DeleteSubjectRes;
import sh4re_v2.sh4re_v2.dto.subject.getAllSubjects.GetAllSubjectsRes;
import sh4re_v2.sh4re_v2.dto.subject.updateSubject.UpdateSubjectReq;
import sh4re_v2.sh4re_v2.dto.subject.updateSubject.UpdateSubjectRes;
import sh4re_v2.sh4re_v2.service.tenant.SubjectService;

@RestController
@RequestMapping("/subject")
@RequiredArgsConstructor
public class SubjectController {
  private final SubjectService subjectService;

  @GetMapping
  public GetAllSubjectsRes getAllSubjects() {
    return subjectService.getAllSubjects();
  }

  @PostMapping
  public CreateSubjectRes createSubject(@Valid @RequestBody CreateSubjectReq req) {
    return subjectService.createSubject(req);
  }

  @PatchMapping
  public UpdateSubjectRes updateSubject(@Valid @RequestBody UpdateSubjectReq req) {
    return subjectService.updateSubject(req);
  }

  @DeleteMapping
  public DeleteSubjectRes deleteSubject(@Valid @RequestBody DeleteSubjectReq req) {
    return subjectService.deleteSubject(req);
  }
}
