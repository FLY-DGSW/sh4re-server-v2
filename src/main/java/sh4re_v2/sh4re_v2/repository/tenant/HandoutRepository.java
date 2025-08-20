package sh4re_v2.sh4re_v2.repository.tenant;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sh4re_v2.sh4re_v2.domain.tenant.Handout;
import sh4re_v2.sh4re_v2.domain.tenant.Subject;

@Repository
public interface HandoutRepository extends JpaRepository<Handout, Long> {
  @Query("SELECT h FROM Handout h JOIN FETCH h.unit WHERE h.subject = :subject")
  List<Handout> findAllBySubject(Subject subject);
  List<Handout> findAllByUnitIdOrderByCreatedAtDesc(Long unitId);
}