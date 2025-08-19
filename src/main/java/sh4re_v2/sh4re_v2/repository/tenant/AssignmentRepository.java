package sh4re_v2.sh4re_v2.repository.tenant;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sh4re_v2.sh4re_v2.domain.tenant.Assignment;
import sh4re_v2.sh4re_v2.domain.tenant.Subject;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
  @Query("SELECT a FROM Assignment a " +
      "JOIN FETCH a.unit " +
      "JOIN FETCH a.subject " +
      "WHERE a.subject = :subject")
  List<Assignment> findAllBySubject(Subject subject);
  List<Assignment> findAllByUserId(Long userId);
  List<Assignment> findAllByUnitIdOrderByCreatedAtDesc(Long unitId);
}