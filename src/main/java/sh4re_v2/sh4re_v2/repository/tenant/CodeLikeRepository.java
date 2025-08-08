package sh4re_v2.sh4re_v2.repository.tenant;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sh4re_v2.sh4re_v2.domain.tenant.CodeLike;

@Repository
public interface CodeLikeRepository extends JpaRepository<CodeLike, Long> {
  Optional<CodeLike> findByCodeIdAndUserId(Long codeId, Long userId);
  Long countByCodeId(Long codeId);
  boolean existsByCodeIdAndUserId(Long codeId, Long userId);
}