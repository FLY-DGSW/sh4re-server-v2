package sh4re_v2.sh4re_v2.repository.tenant;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sh4re_v2.sh4re_v2.domain.tenant.Announcement;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
  List<Announcement> findAllByGradeAndClassNumberAndSchoolYear(int grade, int classNumber, int schoolYear);
}