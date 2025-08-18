package sh4re_v2.sh4re_v2.repository.tenant;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sh4re_v2.sh4re_v2.domain.tenant.Unit;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {
  List<Unit> findAllBySubjectIdOrderByOrderIndex(Long subjectId);
  List<Unit> findAllByUserId(Long userId);
  List<Unit> findAllBySubjectIdAndUserId(Long subjectId, Long userId);
}