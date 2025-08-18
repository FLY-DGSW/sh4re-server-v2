package sh4re_v2.sh4re_v2.repository.tenant;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sh4re_v2.sh4re_v2.domain.tenant.Handout;
import sh4re_v2.sh4re_v2.domain.tenant.Subject;

@Repository
public interface HandoutRepository extends JpaRepository<Handout, Long> {
  List<Handout> findAllBySubject(Subject subject);
  List<Handout> findAllByUnitId(Long unitId);
  List<Handout> findAllByUnitIdOrderByCreatedAtDesc(Long unitId);
}