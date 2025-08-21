package sh4re_v2.sh4re_v2.repository.tenant;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sh4re_v2.sh4re_v2.domain.tenant.Code;

@Repository
public interface CodeRepository extends JpaRepository<Code, Long> {
  List<Code> findAllBySchoolYear(Integer schoolYear);
  
  @Query("SELECT c FROM Code c " +
         "JOIN FETCH c.assignment a " +
         "JOIN FETCH c.classPlacement cp " +
         "WHERE cp.schoolYear = :schoolYear " +
         "AND cp.grade = :grade " +
         "AND cp.classNumber = :classNumber " +
         "AND a.deadline < :currentTime")
  List<Code> findAllBySchoolYearAndGradeAndClassNumberWithExpiredAssignments(
      @Param("schoolYear") Integer schoolYear,
      @Param("grade") Integer grade, 
      @Param("classNumber") Integer classNumber,
      @Param("currentTime") LocalDateTime currentTime
  );
}