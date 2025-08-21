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
import sh4re_v2.sh4re_v2.exception.status_code.HandoutStatusCode;
import sh4re_v2.sh4re_v2.exception.exception.HandoutException;
import sh4re_v2.sh4re_v2.repository.tenant.HandoutRepository;
import sh4re_v2.sh4re_v2.domain.tenant.Unit;
import sh4re_v2.sh4re_v2.service.main.UserService;
import sh4re_v2.sh4re_v2.security.AuthorizationService;

@Service
@Transactional(transactionManager = "tenantTransactionManager")
@RequiredArgsConstructor
public class HandoutService {
  private final HandoutRepository handoutRepository;
  private final UserAuthenticationHolder holder;
  private final SubjectService subjectService;
  private final UnitService unitService;
  private final UserService userService;
  private final AuthorizationService authorizationService;

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
    Subject subject = getSubjectById(subjectId);
    authorizationService.requireReadAccess(subject);
    List<Handout> handouts = handoutRepository.findAllBySubject(subject);
    return GetAllHandoutsRes.from(handouts);
  }

  public CreateHandoutResponse createHandout(CreateHandoutReq req) {
    User user = holder.current();
    
    // Subject 조회
    Subject subject = getSubjectById(req.subjectId());
    authorizationService.requireReadAccess(subject);

    // Unit 조회
    Unit unit = unitService.getUnitById(req.unitId());
    authorizationService.requireReadAccess(unit);
    
    Handout newHandout = req.toEntity(user.getId(), subject, unit);
    this.save(newHandout);
    return new CreateHandoutResponse(newHandout.getId());
  }

  public GetHandoutRes getHandout(Long id) {
    // handout 조회
    Handout handout = getHandoutById(id);
    authorizationService.requireReadAccess(handout);
    
    // 작성자 정보 조회
    User author = userService.getUserOrElseThrow(handout.getAuthorId());
    
    return GetHandoutRes.from(handout, author);
  }

  public void updateHandout(Long handoutId, UpdateHandoutReq req) {
    // 아래 함수를 통해 존재하는지 확인하고 권한까지 검증
    Handout handout = getHandoutById(handoutId);
    authorizationService.requireWriteAccess(handout);
    
    // Subject 조회
    Subject subject = getSubjectById(req.subjectId());
    authorizationService.requireReadAccess(subject);
    
    // Unit 조회 (optional)
    Unit unit = getUnitOrNull(req.unitId());
    
    Handout updatedHandout = req.toEntity(handout);
    updatedHandout.setSubject(subject);
    updatedHandout.setUnit(unit);
    this.save(updatedHandout);
  }

  public void deleteHandout(Long handoutId) {
    // get하면서 존재하는지 확인, 권한 검증
    Handout handout = getHandoutById(handoutId);
    authorizationService.requireWriteAccess(handout);
    this.deleteById(handoutId);
  }
  
  private Subject getSubjectById(Long subjectId) {
    if(subjectId == null) throw HandoutException.of(HandoutStatusCode.HANDOUT_NOT_FOUND);
    
    Optional<Subject> subjectOpt = subjectService.findById(subjectId);
    if(subjectOpt.isEmpty()) throw HandoutException.of(HandoutStatusCode.HANDOUT_NOT_FOUND);
    
    return subjectOpt.get();
  }
  
  private Unit getUnitOrNull(Long unitId) {
    return unitId != null ? unitService.findById(unitId).orElse(null) : null;
  }

  private Handout getHandoutById(Long handoutId) {
    Optional<Handout> handoutOpt = this.findById(handoutId);
    if (handoutOpt.isEmpty()) {
      throw HandoutException.of(HandoutStatusCode.HANDOUT_NOT_FOUND);
    }
    return handoutOpt.get();
  }
}