package sh4re_v2.sh4re_v2.service.tenant;

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
import sh4re_v2.sh4re_v2.exception.status_code.SubjectStatusCode;
import sh4re_v2.sh4re_v2.exception.exception.AssignmentException;
import sh4re_v2.sh4re_v2.exception.exception.AuthException;
import sh4re_v2.sh4re_v2.exception.exception.SubjectException;
import sh4re_v2.sh4re_v2.repository.tenant.AssignmentRepository;
import sh4re_v2.sh4re_v2.security.Role;
import sh4re_v2.sh4re_v2.dto.assignment.CreateAssignmentResponse;
import sh4re_v2.sh4re_v2.dto.assignment.createAssignment.CreateAssignmentReq;
import sh4re_v2.sh4re_v2.dto.assignment.getAllAssignments.GetAllAssignmentsRes;
import sh4re_v2.sh4re_v2.dto.assignment.getAssignment.GetAssignmentRes;
import sh4re_v2.sh4re_v2.dto.assignment.updateAssignment.UpdateAssignmentReq;
import sh4re_v2.sh4re_v2.dto.assignment.deleteAssignment.DeleteAssignmentReq;
import sh4re_v2.sh4re_v2.domain.tenant.Unit;
import sh4re_v2.sh4re_v2.service.tenant.UnitService;
import sh4re_v2.sh4re_v2.exception.status_code.UnitStatusCode;
import sh4re_v2.sh4re_v2.exception.exception.UnitException;

@Service
@Transactional(transactionManager = "tenantTransactionManager")
@RequiredArgsConstructor
public class AssignmentService {
  private final AssignmentRepository assignmentRepository;
  private final UserAuthenticationHolder holder;
  private final SubjectService subjectService;
  private final UnitService unitService;

  public Assignment save(Assignment assignment) {
    return assignmentRepository.save(assignment);
  }

  public Optional<Assignment> findById(Long id) {
    return assignmentRepository.findById(id);
  }

  public List<Assignment> findAllBySubjectId(Long subjectId) {
    return assignmentRepository.findAllBySubjectId(subjectId);
  }

  public List<Assignment> findAllByUserId(Long userId) {
    return assignmentRepository.findAllByUserId(userId);
  }

  public List<Assignment> findAllByUnitId(Long unitId) {
    return assignmentRepository.findAllByUnitIdOrderByCreatedAtDesc(unitId);
  }

  public boolean canAccessAssignment(Assignment assignment, User user) {
    if(user.getRole() == Role.TEACHER || user.getRole() == Role.ADMIN) return true;
    if(assignment.getUserId().equals(user.getId())) return true;
    return subjectService.canAccessSubject(assignment.getSubject(), user);
  }

  public void deleteById(Long id) {
    assignmentRepository.deleteById(id);
  }

  public Assignment createAssignment(Long subjectId, String title, String description, String inputExample, String outputExample, java.time.LocalDateTime deadline, Long unitId) {
    User user = holder.current();
    Optional<Subject> subjectOpt = subjectService.findById(subjectId);
    if(subjectOpt.isEmpty()) throw SubjectException.of(SubjectStatusCode.SUBJECT_NOT_FOUND);
    Subject subject = subjectOpt.get();
    
    if(!subjectService.canAccessSubject(subject, user)) {
      throw AuthException.of(AuthStatusCode.PERMISSION_DENIED);
    }
    
    // Unit 조회 (optional)
    Unit unit = null;
    if(unitId != null) {
      Optional<Unit> unitOpt = unitService.findById(unitId);
      if(unitOpt.isPresent()) {
        unit = unitOpt.get();
      }
    }
    
    Assignment assignment = Assignment.builder()
        .title(title)
        .description(description)
        .inputExample(inputExample)
        .outputExample(outputExample)
        .deadline(deadline)
        .subject(subject)
        .unit(unit)
        .userId(user.getId())
        .build();
    
    return this.save(assignment);
  }

  public Assignment updateAssignment(Long id, String title, String description, String inputExample, String outputExample, java.time.LocalDateTime deadline, Long unitId) {
    User user = holder.current();
    Optional<Assignment> assignmentOpt = this.findById(id);
    if(assignmentOpt.isEmpty()) throw AssignmentException.of(AssignmentStatusCode.ASSIGNMENT_NOT_FOUND);
    Assignment assignment = assignmentOpt.get();
    
    if(!assignment.getUserId().equals(user.getId())) throw AuthException.of(AuthStatusCode.PERMISSION_DENIED);
    
    // Unit 조회 (optional)
    Unit unit = null;
    if(unitId != null) {
      Optional<Unit> unitOpt = unitService.findById(unitId);
      if(unitOpt.isPresent()) {
        unit = unitOpt.get();
      }
    }
    
    assignment.setTitle(title);
    assignment.setDescription(description);
    assignment.setInputExample(inputExample);
    assignment.setOutputExample(outputExample);
    assignment.setDeadline(deadline);
    assignment.setUnit(unit);
    
    return this.save(assignment);
  }

  public void deleteAssignment(Long id) {
    User user = holder.current();
    Optional<Assignment> assignmentOpt = this.findById(id);
    if(assignmentOpt.isEmpty()) throw AssignmentException.of(AssignmentStatusCode.ASSIGNMENT_NOT_FOUND);
    Assignment assignment = assignmentOpt.get();
    
    if(!assignment.getUserId().equals(user.getId())) throw AuthException.of(AuthStatusCode.PERMISSION_DENIED);
    
    this.deleteById(id);
  }

  public GetAllAssignmentsRes getAllAssignmentsBySubjectId(Long subjectId) {
    User user = holder.current();
    Optional<Subject> subjectOpt = subjectService.findById(subjectId);
    if(subjectOpt.isEmpty()) throw SubjectException.of(SubjectStatusCode.SUBJECT_NOT_FOUND);
    Subject subject = subjectOpt.get();
    
    if(!subjectService.canAccessSubject(subject, user)) {
      throw AuthException.of(AuthStatusCode.PERMISSION_DENIED);
    }
    
    List<Assignment> assignments = this.findAllBySubjectId(subjectId);
    return new GetAllAssignmentsRes(assignments);
  }

  public GetAssignmentRes getAssignment(Long id) {
    User user = holder.current();
    Optional<Assignment> assignmentOpt = this.findById(id);
    if(assignmentOpt.isEmpty()) throw AssignmentException.of(AssignmentStatusCode.ASSIGNMENT_NOT_FOUND);
    Assignment assignment = assignmentOpt.get();
    
    if(!this.canAccessAssignment(assignment, user)) {
      throw AuthException.of(AuthStatusCode.PERMISSION_DENIED);
    }
    
    return new GetAssignmentRes(assignment);
  }

  public CreateAssignmentResponse createAssignment(CreateAssignmentReq req) {
    Assignment assignment = this.createAssignment(
        req.subjectId(),
        req.title(),
        req.description(),
        req.inputExample(),
        req.outputExample(),
        req.deadline(),
        req.unitId()
    );
    return new CreateAssignmentResponse(assignment.getId());
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

  public GetAllAssignmentsRes getAllAssignmentsByUnitId(Long unitId) {
    User user = holder.current();
    Optional<Unit> unitOpt = unitService.findById(unitId);
    if(unitOpt.isEmpty()) throw UnitException.of(UnitStatusCode.UNIT_NOT_FOUND);
    Unit unit = unitOpt.get();
    
    if(!unitService.canAccessUnit(unit, user)) {
      throw AuthException.of(AuthStatusCode.PERMISSION_DENIED);
    }
    
    List<Assignment> assignments = this.findAllByUnitId(unitId);
    return new GetAllAssignmentsRes(assignments);
  }
}