package sh4re_v2.sh4re_v2.service.tenant;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sh4re_v2.sh4re_v2.common.UserAuthenticationHolder;
import sh4re_v2.sh4re_v2.domain.main.User;
import sh4re_v2.sh4re_v2.domain.tenant.ClassPlacement;
import sh4re_v2.sh4re_v2.domain.tenant.Subject;
import sh4re_v2.sh4re_v2.dto.subject.createSubject.CreateSubjectReq;
import sh4re_v2.sh4re_v2.dto.subject.CreateSubjectResponse;
import sh4re_v2.sh4re_v2.dto.subject.deleteSubject.DeleteSubjectReq;
import sh4re_v2.sh4re_v2.dto.subject.getAllSubjects.GetAllSubjectsRes;
import sh4re_v2.sh4re_v2.dto.subject.updateSubject.UpdateSubjectReq;
import sh4re_v2.sh4re_v2.exception.status_code.AuthStatusCode;
import sh4re_v2.sh4re_v2.exception.status_code.ClassPlacementStatusCode;
import sh4re_v2.sh4re_v2.exception.status_code.SubjectStatusCode;
import sh4re_v2.sh4re_v2.exception.exception.AuthException;
import sh4re_v2.sh4re_v2.exception.exception.ClassPlacementException;
import sh4re_v2.sh4re_v2.exception.exception.SubjectException;
import sh4re_v2.sh4re_v2.repository.tenant.SubjectRepository;
import sh4re_v2.sh4re_v2.security.Role;

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


  public void deleteById(Long id) {
    subjectRepository.deleteById(id);
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

  public CreateSubjectResponse createSubject(CreateSubjectReq req) {
    User user = holder.current();
    Subject newSubject = req.toEntity(user.getId());
    this.save(newSubject);
    return new CreateSubjectResponse(newSubject.getId());
  }

  public void updateSubject(UpdateSubjectReq req) {
    Subject subject = this.getSubjectOrElseThrow(req.id());
    Subject newSubject = req.toEntity(subject);
    this.save(newSubject);
  }

  public void deleteSubject(DeleteSubjectReq req) {
    User user = holder.current();
    Optional<Subject> subjectOpt = this.findById(req.id());
    if(subjectOpt.isEmpty()) throw SubjectException.of(SubjectStatusCode.SUBJECT_NOT_FOUND);
    Subject subject = subjectOpt.get();
    if(!subject.getAuthorId().equals(user.getId())) throw AuthException.of(AuthStatusCode.PERMISSION_DENIED);
    this.deleteById(req.id());
  }

  public Subject getSubjectOrElseThrow(@NotNull Long subjectId) {
    User user = holder.current();
    if(subjectId == null) throw SubjectException.of(SubjectStatusCode.SUBJECT_NOT_FOUND);
    
    Optional<Subject> subjectOpt = this.findById(subjectId);
    if(subjectOpt.isEmpty()) throw SubjectException.of(SubjectStatusCode.SUBJECT_NOT_FOUND);
    Subject subject = subjectOpt.get();

    if(user.getRole() != Role.TEACHER && user.getRole() != Role.ADMIN) {
      boolean hasAccess = classPlacementService.findAllByUserId(user.getId()).stream()
        .anyMatch(cp -> 
          subject.getGrade().equals(cp.getGrade()) && 
          subject.getClassNumber().equals(cp.getClassNumber()) && 
          subject.getSchoolYear().equals(cp.getSchoolYear())
        );
      if (!hasAccess) {
        throw AuthException.of(AuthStatusCode.PERMISSION_DENIED);
      }
    }

    return subject;
  }
}
