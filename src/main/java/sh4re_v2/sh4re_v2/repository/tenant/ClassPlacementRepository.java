package sh4re_v2.sh4re_v2.repository.tenant;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sh4re_v2.sh4re_v2.domain.tenant.ClassPlacement;

@Repository
public interface ClassPlacementRepository extends JpaRepository<ClassPlacement, Long> {
  Optional<ClassPlacement> findTop1ByUserIdOrderBySchoolYearDesc(long userId);

  List<ClassPlacement> findAllByUserId(Long userId);
}
