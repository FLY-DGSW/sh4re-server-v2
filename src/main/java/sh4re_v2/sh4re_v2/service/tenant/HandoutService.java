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
import sh4re_v2.sh4re_v2.exception.status_code.AuthStatusCode;
import sh4re_v2.sh4re_v2.exception.status_code.HandoutStatusCode;
import sh4re_v2.sh4re_v2.exception.exception.AuthException;
import sh4re_v2.sh4re_v2.exception.exception.HandoutException;
import sh4re_v2.sh4re_v2.repository.tenant.HandoutRepository;
import sh4re_v2.sh4re_v2.domain.tenant.Unit;
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
    Subject subject = subjectService.getSubjectOrElseThrow(subjectId);
    List<Handout> handouts = handoutRepository.findAllBySubject(subject);
    return GetAllHandoutsRes.from(handouts);
  }

  public CreateHandoutResponse createHandout(CreateHandoutReq req) {
    User user = holder.current();
    
    // Subject 조회
    Subject subject = subjectService.getSubjectOrElseThrow(req.subjectId());

    // Unit 조회
    Unit unit = unitService.getUnitOrElseThrow(req.unitId());
    
    Handout newHandout = req.toEntity(user.getId(), subject, unit);
    this.save(newHandout);
    return new CreateHandoutResponse(newHandout.getId());
  }

  public GetHandoutRes getHandout(Long id) {
    // handout 조회
    Handout handout = getHandoutOrElseThrow(id);
    
    // 작성자 정보 조회
    User author = userService.getUserOrElseThrow(handout.getAuthorId());
    
    return GetHandoutRes.from(handout, author);
  }

  public void updateHandout(Long handoutId, UpdateHandoutReq req) {
    // 아래 함수를 통해 존재하는지 확인하고 권한까지 검증
    Handout handout = getHandoutOrElseThrow(handoutId, true);
    
    // Subject 조회
    Subject subject = subjectService.getSubjectOrElseThrow(req.subjectId());
    
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

  public void deleteHandout(Long handoutId) {
    // get하면서 존재하는지 확인, 권한 검증
    getHandoutOrElseThrow(handoutId, true);
    this.deleteById(handoutId);
  }

  public Handout getHandoutOrElseThrow(Long handoutId) {
    User user = holder.current();
    Optional<Handout> handoutOpt = this.findById(handoutId);
    if (handoutOpt.isEmpty()) {
      throw HandoutException.of(HandoutStatusCode.HANDOUT_NOT_FOUND);
    }

    return handoutOpt.get();
  }

  public Handout getHandoutOrElseThrow(Long handoutId, boolean withAuthorization) {
    User user = holder.current();
    Optional<Handout> handoutOpt = this.findById(handoutId);
    if (handoutOpt.isEmpty()) {
      throw HandoutException.of(HandoutStatusCode.HANDOUT_NOT_FOUND);
    }
    Handout handout = handoutOpt.get();

    if(withAuthorization) {
      if(!subjectService.canAccessSubject(handout.getSubject(), user)) {
        throw AuthException.of(AuthStatusCode.PERMISSION_DENIED);
      }
    }

    return handout;
  }
}