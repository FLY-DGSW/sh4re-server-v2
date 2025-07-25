package sh4re_v2.sh4re_v2.service.tenant;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sh4re_v2.sh4re_v2.common.UserAuthenticationHolder;
import sh4re_v2.sh4re_v2.domain.main.User;
import sh4re_v2.sh4re_v2.domain.tenant.ClassPlacement;
import sh4re_v2.sh4re_v2.domain.tenant.Subject;
import sh4re_v2.sh4re_v2.dto.createSubject.CreateSubjectReq;
import sh4re_v2.sh4re_v2.dto.createSubject.CreateSubjectRes;
import sh4re_v2.sh4re_v2.dto.getAllSubjects.GetAllSubjectsRes;
import sh4re_v2.sh4re_v2.dto.updateSubject.UpdateSubjectReq;
import sh4re_v2.sh4re_v2.dto.updateSubject.UpdateSubjectRes;
import sh4re_v2.sh4re_v2.exception.error_code.ClassPlacementStatusCode;
import sh4re_v2.sh4re_v2.exception.error_code.SubjectStatusCode;
import sh4re_v2.sh4re_v2.exception.exception.ClassPlacementException;
import sh4re_v2.sh4re_v2.exception.exception.SubjectException;
import sh4re_v2.sh4re_v2.repository.tenant.SubjectRepository;

@Service
@Transactional(transactionManager = "tenantTransactionManager")
@RequiredArgsConstructor
public class SubjectService {
  private final SubjectRepository subjectRepository;
  private final UserAuthenticationHolder holder;
  private final ClassPlacementService classPlacementService;

  public Subject save(Subject subject) {
    return subjectRepository.save(subject);
  }

  public Optional<Subject> findById(Long id) {
    return subjectRepository.findById(id);
  }

  public GetAllSubjectsRes getAllSubjects() {
    User user = holder.current();
    Optional<ClassPlacement> classPlacementOpt = classPlacementService.findLatestClassPlacementByUserId(user.getId());
    if(classPlacementOpt.isEmpty()) throw ClassPlacementException.of(ClassPlacementStatusCode.CLASS_PLACEMENT_NOT_FOUND);
    ClassPlacement classPlacement = classPlacementOpt.get();
    List<Subject> subjects = subjectRepository
        .findAllByGradeAndClassNumberAndSchoolYear(
            classPlacement.getGrade(),
            classPlacement.getClassNumber(),
            classPlacement.getSchoolYear()
        );
    return new GetAllSubjectsRes(subjects);
  }

  public CreateSubjectRes createSubject(CreateSubjectReq req) {
    User user = holder.current();
    Subject newSubject = req.toEntity(user.getId());
    this.save(newSubject);
    return new CreateSubjectRes(newSubject.getId());
  }

  public UpdateSubjectRes updateSubject(UpdateSubjectReq req) {
    Optional<Subject> subjectOpt = this.findById(req.id());
    if(subjectOpt.isEmpty()) throw SubjectException.of(SubjectStatusCode.SUBJECT_NOT_FOUND);
    Subject subject = subjectOpt.get();
    Subject newSubject = req.toEntity(subject);
    this.save(newSubject);
    return new UpdateSubjectRes(newSubject.getId());
  }
}
