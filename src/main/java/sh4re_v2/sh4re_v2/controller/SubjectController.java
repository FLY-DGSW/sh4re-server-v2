package sh4re_v2.sh4re_v2.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sh4re_v2.sh4re_v2.dto.createSubject.CreateSubjectReq;
import sh4re_v2.sh4re_v2.dto.createSubject.CreateSubjectRes;
import sh4re_v2.sh4re_v2.dto.getAllSubjects.GetAllSubjectsRes;
import sh4re_v2.sh4re_v2.dto.updateSubject.UpdateSubjectReq;
import sh4re_v2.sh4re_v2.dto.updateSubject.UpdateSubjectRes;
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
}
