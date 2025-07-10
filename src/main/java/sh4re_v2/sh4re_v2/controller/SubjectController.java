package sh4re_v2.sh4re_v2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sh4re_v2.sh4re_v2.dto.getAllSubjects.GetAllSubjectsRes;
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
}
