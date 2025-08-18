package sh4re_v2.sh4re_v2.service.tenant;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sh4re_v2.sh4re_v2.common.UserAuthenticationHolder;
import sh4re_v2.sh4re_v2.domain.main.User;
import sh4re_v2.sh4re_v2.domain.tenant.Handout;
import sh4re_v2.sh4re_v2.domain.tenant.Subject;
import sh4re_v2.sh4re_v2.dto.handout.createHandout.CreateHandoutReq;
import sh4re_v2.sh4re_v2.dto.handout.CreateHandoutResponse;
import sh4re_v2.sh4re_v2.dto.handout.getAllHandouts.GetAllHandoutsRes;
import sh4re_v2.sh4re_v2.dto.handout.getHandout.GetHandoutRes;
import sh4re_v2.sh4re_v2.dto.handout.updateHandout.UpdateHandoutReq;
import sh4re_v2.sh4re_v2.exception.exception.SubjectException;
import sh4re_v2.sh4re_v2.exception.status_code.AuthStatusCode;
import sh4re_v2.sh4re_v2.exception.status_code.HandoutStatusCode;
import sh4re_v2.sh4re_v2.exception.exception.AuthException;
import sh4re_v2.sh4re_v2.exception.exception.HandoutException;
import sh4re_v2.sh4re_v2.exception.status_code.SubjectStatusCode;
import sh4re_v2.sh4re_v2.security.Role;
import sh4re_v2.sh4re_v2.repository.tenant.HandoutRepository;
import sh4re_v2.sh4re_v2.domain.tenant.Unit;
import sh4re_v2.sh4re_v2.service.tenant.UnitService;
import sh4re_v2.sh4re_v2.exception.status_code.UnitStatusCode;
import sh4re_v2.sh4re_v2.exception.exception.UnitException;
import sh4re_v2.sh4re_v2.service.main.UserService;

@Service
@Transactional(transactionManager = "tenantTransactionManager")
@RequiredArgsConstructor
public class HandoutService {
  private final HandoutRepository handoutRepository;
  private final UserAuthenticationHolder holder;
  private final SubjectService subjectService;
  private final UnitService unitService;
  private final UserService userService;

  public Handout save(Handout handout) {
    return handoutRepository.save(handout);
  }

  public Optional<Handout> findById(Long id) {
    return handoutRepository.findById(id);
  }

  public void deleteById(Long id) {
    handoutRepository.deleteById(id);
  }

  public List<Handout> findAllByUnitId(Long unitId) {
    return handoutRepository.findAllByUnitIdOrderByCreatedAtDesc(unitId);
  }

  public GetAllHandoutsRes getAllHandouts(Long subjectId) {
    User user = holder.current();
    Optional<Subject> subjectOpt = subjectService.findById(subjectId);
    if (subjectOpt.isEmpty()) throw SubjectException.of(SubjectStatusCode.SUBJECT_NOT_FOUND);
    Subject subject = subjectOpt.get();
    if(!subjectService.canAccessSubject(subject, user)) throw AuthException.of(AuthStatusCode.PERMISSION_DENIED);
    List<Handout> handouts = handoutRepository.findAllBySubject(subject);
    return GetAllHandoutsRes.from(handouts);
  }

  public CreateHandoutResponse createHandout(CreateHandoutReq req) {
    User user = holder.current();
    
    // Subject 조회
    Optional<Subject> subjectOpt = subjectService.findById(req.subjectId());
    if(subjectOpt.isEmpty()) throw SubjectException.of(SubjectStatusCode.SUBJECT_NOT_FOUND);
    Subject subject = subjectOpt.get();
    
    if(!subjectService.canAccessSubject(subject, user)) {
      throw AuthException.of(AuthStatusCode.PERMISSION_DENIED);
    }
    
    // Unit 조회 (optional)
    Unit unit = null;
    if(req.unitId() != null) {
      Optional<Unit> unitOpt = unitService.findById(req.unitId());
      if(unitOpt.isPresent()) {
        unit = unitOpt.get();
      }
    }
    
    Handout newHandout = req.toEntity(user.getId(), subject, unit);
    this.save(newHandout);
    return new CreateHandoutResponse(newHandout.getId());
  }

  public GetHandoutRes getHandout(Long id) {
    User user = holder.current();
    Optional<Handout> handoutOpt = this.findById(id);
    if (handoutOpt.isEmpty()) {
      throw HandoutException.of(HandoutStatusCode.HANDOUT_NOT_FOUND);
    }
    Handout handout = handoutOpt.get();
    
    if(!subjectService.canAccessSubject(handout.getSubject(), user)) {
      throw AuthException.of(AuthStatusCode.PERMISSION_DENIED);
    }
    
    // 작성자 정보 조회
    Optional<User> authorOpt = userService.findById(handout.getAuthorId());
    User author = authorOpt.orElse(null);
    
    return GetHandoutRes.from(handout, author);
  }

  public void updateHandout(Long id, UpdateHandoutReq req) {
    User user = holder.current();
    Optional<Handout> handoutOpt = this.findById(id);
    if (handoutOpt.isEmpty()) {
      throw HandoutException.of(HandoutStatusCode.HANDOUT_NOT_FOUND);
    }
    Handout handout = handoutOpt.get();
    
    // 권한 검증
    if(!handout.getAuthorId().equals(user.getId())) {
      throw AuthException.of(AuthStatusCode.PERMISSION_DENIED);
    }
    
    // Subject 조회
    Optional<Subject> subjectOpt = subjectService.findById(req.subjectId());
    if(subjectOpt.isEmpty()) throw SubjectException.of(SubjectStatusCode.SUBJECT_NOT_FOUND);
    Subject subject = subjectOpt.get();
    
    // Unit 조회 (optional)
    Unit unit = null;
    if(req.unitId() != null) {
      Optional<Unit> unitOpt = unitService.findById(req.unitId());
      if(unitOpt.isPresent()) {
        unit = unitOpt.get();
      }
    }
    
    Handout updatedHandout = req.toEntity(handout);
    updatedHandout.setSubject(subject);
    updatedHandout.setUnit(unit);
    this.save(updatedHandout);
  }

  public void deleteHandout(Long id) {
    User user = holder.current();
    Optional<Handout> handoutOpt = this.findById(id);
    if (handoutOpt.isEmpty()) {
      throw HandoutException.of(HandoutStatusCode.HANDOUT_NOT_FOUND);
    }
    Handout handout = handoutOpt.get();
    
    // 권한 검증
    if(!handout.getAuthorId().equals(user.getId())) {
      throw AuthException.of(AuthStatusCode.PERMISSION_DENIED);
    }
    
    this.deleteById(id);
  }

  public GetAllHandoutsRes getAllHandoutsByUnitId(Long unitId) {
    User user = holder.current();
    Optional<Unit> unitOpt = unitService.findById(unitId);
    if(unitOpt.isEmpty()) throw UnitException.of(UnitStatusCode.UNIT_NOT_FOUND);
    Unit unit = unitOpt.get();
    
    if(!unitService.canAccessUnit(unit, user)) {
      throw AuthException.of(AuthStatusCode.PERMISSION_DENIED);
    }
    
    List<Handout> handouts = this.findAllByUnitId(unitId);
    return GetAllHandoutsRes.from(handouts);
  }
}