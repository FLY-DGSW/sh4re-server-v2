package sh4re_v2.sh4re_v2.service.tenant;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sh4re_v2.sh4re_v2.domain.tenant.ClassPlacement;
import sh4re_v2.sh4re_v2.exception.exception.ClassPlacementException;
import sh4re_v2.sh4re_v2.exception.status_code.ClassPlacementStatusCode;
import sh4re_v2.sh4re_v2.repository.tenant.ClassPlacementRepository;

@Service
@Transactional(
    transactionManager = "tenantTransactionManager"
)
@RequiredArgsConstructor
public class ClassPlacementService {
  private final ClassPlacementRepository classPlacementRepository;

  public ClassPlacement save(ClassPlacement classPlacement) { return classPlacementRepository.save(classPlacement); }

  public Optional<ClassPlacement> findLatestClassPlacementByUserId(Long userId) {
    return classPlacementRepository.findTop1ByUserIdOrderBySchoolYearDesc(userId);
  }

  public ClassPlacement findLatestClassPlacementByUserIdOrElseThrow(Long userId) {
    if(userId == null) throw ClassPlacementException.of(ClassPlacementStatusCode.CLASS_PLACEMENT_NOT_FOUND);

    Optional<ClassPlacement> classPlacementOpt = classPlacementRepository.findTop1ByUserIdOrderBySchoolYearDesc(userId);
    if(classPlacementOpt.isEmpty()) throw ClassPlacementException.of(ClassPlacementStatusCode.CLASS_PLACEMENT_NOT_FOUND);

    return classPlacementOpt.get();
  }

  public List<ClassPlacement> findAllByUserId(Long userId) {
    return classPlacementRepository.findAllByUserId(userId);
  }

  public Optional<ClassPlacement> findById(Long id) {
    return classPlacementRepository.findById(id);
  }
}
