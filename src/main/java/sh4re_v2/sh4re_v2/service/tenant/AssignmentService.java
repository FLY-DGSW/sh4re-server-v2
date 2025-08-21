package sh4re_v2.sh4re_v2.service.tenant;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sh4re_v2.sh4re_v2.common.UserAuthenticationHolder;
import sh4re_v2.sh4re_v2.domain.main.User;
import sh4re_v2.sh4re_v2.domain.tenant.Assignment;
import sh4re_v2.sh4re_v2.domain.tenant.Subject;
import sh4re_v2.sh4re_v2.exception.status_code.AssignmentStatusCode;
import sh4re_v2.sh4re_v2.exception.status_code.AuthStatusCode;
import sh4re_v2.sh4re_v2.exception.exception.AssignmentException;
import sh4re_v2.sh4re_v2.exception.exception.AuthException;
import sh4re_v2.sh4re_v2.security.AuthorizationService;
import sh4re_v2.sh4re_v2.repository.tenant.AssignmentRepository;
import sh4re_v2.sh4re_v2.security.Role;
import sh4re_v2.sh4re_v2.dto.assignment.CreateAssignmentResponse;
import sh4re_v2.sh4re_v2.dto.assignment.createAssignment.CreateAssignmentReq;
import sh4re_v2.sh4re_v2.dto.assignment.getAllAssignments.GetAllAssignmentsRes;
import sh4re_v2.sh4re_v2.dto.assignment.getAssignment.GetAssignmentRes;
import sh4re_v2.sh4re_v2.dto.assignment.updateAssignment.UpdateAssignmentReq;
import sh4re_v2.sh4re_v2.dto.assignment.deleteAssignment.DeleteAssignmentReq;
import sh4re_v2.sh4re_v2.domain.tenant.Unit;

@Service
@Transactional(transactionManager = "tenantTransactionManager")
@RequiredArgsConstructor
public class AssignmentService {
  private final AssignmentRepository assignmentRepository;
  private final UserAuthenticationHolder holder;
  private final SubjectService subjectService;
  private final UnitService unitService;
  private final AuthorizationService authorizationService;

  public Assignment save(Assignment assignment) {
    return assignmentRepository.save(assignment);
  }

  public Optional<Assignment> findById(Long id) {
    return assignmentRepository.findById(id);
  }


  public void deleteById(Long id) {
    assignmentRepository.deleteById(id);
  }

  public void updateAssignment(Long assignmentId, String title, String description, String inputExample, String outputExample, LocalDateTime deadline, Long unitId) {
    Assignment assignment = getAssignmentById(assignmentId);
    authorizationService.requireWriteAccess(assignment);
    
    // Unit 조회 (required)
    Unit unit = unitService.getUnitById(unitId);
    authorizationService.requireReadAccess(unit);
    
    // Unit이 해당 Assignment의 Subject에 속하는지 검증
    validateUnitBelongsToSubject(unit, assignment.getSubject());
    
    assignment.setTitle(title);
    assignment.setDescription(description);
    assignment.setInputExample(inputExample);
    assignment.setOutputExample(outputExample);
    assignment.setDeadline(deadline);
    assignment.setUnit(unit);

    this.save(assignment);
  }

  public void deleteAssignment(Long assignmentId) {
    Assignment assignment = getAssignmentById(assignmentId);
    authorizationService.requireWriteAccess(assignment);
    
    this.deleteById(assignmentId);
  }

  public GetAllAssignmentsRes getAllAssignmentsBySubjectId(Long subjectId) {
    Subject subject = subjectService.getSubjectOrElseThrow(subjectId);
    List<Assignment> assignments = assignmentRepository.findAllBySubject(subject);
    return GetAllAssignmentsRes.from(assignments);
  }

  public GetAssignmentRes getAssignment(Long assignmentId) {
    Assignment assignment = getAssignmentById(assignmentId);
    authorizationService.requireReadAccess(assignment);
    
    return new GetAssignmentRes(assignment);
  }

  public CreateAssignmentResponse createAssignment(CreateAssignmentReq req) {
    User user = holder.current();

    // Subject 조회
    Subject subject = subjectService.getSubjectOrElseThrow(req.subjectId());

    // Unit 조회 (required)
    Unit unit = unitService.getUnitById(req.unitId());
    authorizationService.requireReadAccess(unit);
    
    // Unit이 해당 Subject에 속하는지 검증
    validateUnitBelongsToSubject(unit, subject);

    Assignment assignment = Assignment.builder()
        .title(req.title())
        .description(req.description())
        .inputExample(req.inputExample())
        .outputExample(req.outputExample())
        .deadline(req.deadline())
        .subject(subject)
        .unit(unit)
        .authorId(user.getId())
        .build();
    
    Assignment savedAssignment = this.save(assignment);
    return new CreateAssignmentResponse(savedAssignment.getId());
  }

  public void updateAssignment(UpdateAssignmentReq req) {
    this.updateAssignment(
        req.id(),
        req.title(),
        req.description(),
        req.inputExample(),
        req.outputExample(),
        req.deadline(),
        req.unitId()
    );
  }

  public void deleteAssignment(DeleteAssignmentReq req) {
    this.deleteAssignment(req.id());
  }

  private Assignment getAssignmentById(Long assignmentId) {
    if(assignmentId == null) throw AssignmentException.of(AssignmentStatusCode.ASSIGNMENT_NOT_FOUND);

    Optional<Assignment> assignmentOpt = this.findById(assignmentId);
    if(assignmentOpt.isEmpty()) throw AssignmentException.of(AssignmentStatusCode.ASSIGNMENT_NOT_FOUND);
    
    return assignmentOpt.get();
  }
  
  private void validateUnitBelongsToSubject(Unit unit, Subject subject) {
    if (!unit.getSubject().getId().equals(subject.getId())) {
      throw AssignmentException.of(AssignmentStatusCode.ASSIGNMENT_NOT_FOUND); // 적절한 상태 코드로 변경 필요
    }
  }
  
}