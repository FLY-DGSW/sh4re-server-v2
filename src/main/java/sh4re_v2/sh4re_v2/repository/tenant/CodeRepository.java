package sh4re_v2.sh4re_v2.repository.tenant;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sh4re_v2.sh4re_v2.domain.tenant.Code;

@Repository
public interface CodeRepository extends JpaRepository<Code, Long> {
  List<Code> findAllBySchoolYear(Integer schoolYear);
}