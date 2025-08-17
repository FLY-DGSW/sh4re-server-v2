package sh4re_v2.sh4re_v2.service.tenant;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sh4re_v2.sh4re_v2.domain.tenant.ClassPlacement;
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

  public List<ClassPlacement> findAllByUserId(Long userId) {
    return classPlacementRepository.findAllByUserId(userId);
  }
}
