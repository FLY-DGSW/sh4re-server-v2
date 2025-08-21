package sh4re_v2.sh4re_v2.service.tenant;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sh4re_v2.sh4re_v2.common.UserAuthenticationHolder;
import sh4re_v2.sh4re_v2.domain.main.User;
import sh4re_v2.sh4re_v2.domain.tenant.Subject;
import sh4re_v2.sh4re_v2.domain.tenant.Unit;
import sh4re_v2.sh4re_v2.exception.status_code.AuthStatusCode;
import sh4re_v2.sh4re_v2.exception.status_code.SubjectStatusCode;
import sh4re_v2.sh4re_v2.exception.status_code.UnitStatusCode;
import sh4re_v2.sh4re_v2.exception.exception.AuthException;
import sh4re_v2.sh4re_v2.exception.exception.SubjectException;
import sh4re_v2.sh4re_v2.exception.exception.UnitException;
import sh4re_v2.sh4re_v2.repository.tenant.UnitRepository;
import sh4re_v2.sh4re_v2.security.Role;
import sh4re_v2.sh4re_v2.dto.unit.CreateUnitResponse;
import sh4re_v2.sh4re_v2.dto.unit.createUnit.CreateUnitReq;
import sh4re_v2.sh4re_v2.dto.unit.getAllUnits.GetAllUnitsRes;
import sh4re_v2.sh4re_v2.dto.unit.getUnit.GetUnitRes;
import sh4re_v2.sh4re_v2.dto.unit.updateUnit.UpdateUnitReq;
import sh4re_v2.sh4re_v2.dto.unit.deleteUnit.DeleteUnitReq;
import sh4re_v2.sh4re_v2.security.AuthorizationService;

@Service
@Transactional(transactionManager = "tenantTransactionManager")
@RequiredArgsConstructor
public class UnitService {
  private final UnitRepository unitRepository;
  private final UserAuthenticationHolder holder;
  private final SubjectService subjectService;
  private final AuthorizationService authorizationService;

  public Unit save(Unit unit) {
    return unitRepository.save(unit);
  }

  public Optional<Unit> findById(Long id) {
    return unitRepository.findById(id);
  }

  public List<Unit> findAllBySubjectId(Long subjectId) {
    return unitRepository.findAllBySubjectIdOrderByOrderIndex(subjectId);
  }

  public List<Unit> findAllByAuthorId(Long authorId) {
    return unitRepository.findAllByAuthorId(authorId);
  }


  public void deleteById(Long id) {
    unitRepository.deleteById(id);
  }

  public Unit createUnit(Long subjectId, String title, String description, Integer orderIndex) {
    User user = holder.current();
    Optional<Subject> subjectOpt = subjectService.findById(subjectId);
    if(subjectOpt.isEmpty()) throw SubjectException.of(SubjectStatusCode.SUBJECT_NOT_FOUND);
    Subject subject = subjectOpt.get();
    
    authorizationService.requireReadAccess(subject);
    
    Unit unit = Unit.builder()
        .title(title)
        .description(description)
        .orderIndex(orderIndex)
        .subject(subject)
        .authorId(user.getId())
        .build();
    
    return this.save(unit);
  }

  public Unit updateUnit(Long id, String title, String description, Integer orderIndex) {
    Unit unit = getUnitById(id);
    authorizationService.requireWriteAccess(unit);
    
    unit.setTitle(title);
    unit.setDescription(description);
    unit.setOrderIndex(orderIndex);
    
    return this.save(unit);
  }

  public void deleteUnit(Long id) {
    Unit unit = getUnitById(id);
    authorizationService.requireWriteAccess(unit);
    this.deleteById(id);
  }

  public GetAllUnitsRes getAllUnitsBySubjectId(Long subjectId) {
    Subject subject = subjectService.getSubjectOrElseThrow(subjectId);
    List<Unit> units = this.findAllBySubjectId(subject.getId());
    return new GetAllUnitsRes(units);
  }

  public GetUnitRes getUnit(Long id) {
    Unit unit = getUnitById(id);
    authorizationService.requireReadAccess(unit);
    return new GetUnitRes(unit);
  }

  public CreateUnitResponse createUnit(CreateUnitReq req) {
    Unit unit = this.createUnit(
        req.subjectId(),
        req.title(),
        req.description(),
        req.orderIndex()
    );
    return new CreateUnitResponse(unit.getId());
  }

  public void updateUnit(UpdateUnitReq req) {
    this.updateUnit(
        req.id(),
        req.title(),
        req.description(),
        req.orderIndex()
    );
  }

  public void deleteUnit(DeleteUnitReq req) {
    this.deleteUnit(req.id());
  }

  public Unit getUnitById(Long unitId) {
    if(unitId == null) throw UnitException.of(UnitStatusCode.UNIT_NOT_FOUND);
    Optional<Unit> unitOpt = this.findById(unitId);
    if(unitOpt.isEmpty()) throw UnitException.of(UnitStatusCode.UNIT_NOT_FOUND);
    return unitOpt.get();
  }
}