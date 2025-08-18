package sh4re_v2.sh4re_v2.repository.tenant;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sh4re_v2.sh4re_v2.domain.tenant.Assignment;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
  List<Assignment> findAllBySubjectId(Long subjectId);
  List<Assignment> findAllByUserId(Long userId);
  List<Assignment> findAllBySubjectIdAndUserId(Long subjectId, Long userId);
  List<Assignment> findAllByUnitId(Long unitId);
  List<Assignment> findAllByUnitIdOrderByCreatedAtDesc(Long unitId);
}