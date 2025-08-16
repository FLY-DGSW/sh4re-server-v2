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

@Service
@Transactional(transactionManager = "tenantTransactionManager")
@RequiredArgsConstructor
public class HandoutService {
  private final HandoutRepository handoutRepository;
  private final UserAuthenticationHolder holder;
  private final SubjectService subjectService;

  public Handout save(Handout handout) {
    return handoutRepository.save(handout);
  }

  public Optional<Handout> findById(Long id) {
    return handoutRepository.findById(id);
  }

  public void deleteById(Long id) {
    handoutRepository.deleteById(id);
  }

  public GetAllHandoutsRes getAllHandouts(Long subjectId) {
    User user = holder.current();
    Optional<Subject> subjectOpt = subjectService.findById(subjectId);
    if (subjectOpt.isEmpty()) throw SubjectException.of(SubjectStatusCode.SUBJECT_NOT_FOUND);
    Subject subject = subjectOpt.get();
    if(!subjectService.canAccessSubject(subject, user)) throw AuthException.of(AuthStatusCode.PERMISSION_DENIED);
    List<Handout> handouts = handoutRepository.findAllBySubjectId(subjectId);
    return GetAllHandoutsRes.from(handouts);
  }

  public CreateHandoutResponse createHandout(CreateHandoutReq req) {
    User user = holder.current();
    
    Handout newHandout = req.toEntity(user.getId());
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
    Optional<Subject> subjectOpt = subjectService.findById(handout.getSubjectId());
    if (subjectOpt.isEmpty()) throw SubjectException.of(SubjectStatusCode.SUBJECT_NOT_FOUND);
    Subject subject = subjectOpt.get();
    if(!subjectService.canAccessSubject(subject, user)) throw AuthException.of(AuthStatusCode.PERMISSION_DENIED);
    return GetHandoutRes.from(handout);
  }

  public void updateHandout(Long id, UpdateHandoutReq req) {
    Optional<Handout> handoutOpt = this.findById(id);
    if (handoutOpt.isEmpty()) {
      throw HandoutException.of(HandoutStatusCode.HANDOUT_NOT_FOUND);
    }
    Handout handout = handoutOpt.get();
    
    Handout newHandout = req.toEntity(handout);
    this.save(newHandout);
  }

  public void deleteHandout(Long id) {
    Optional<Handout> handoutOpt = this.findById(id);
    if (handoutOpt.isEmpty()) {
      throw HandoutException.of(HandoutStatusCode.HANDOUT_NOT_FOUND);
    }
    Handout handout = handoutOpt.get();
    
    this.deleteById(id);
  }
}