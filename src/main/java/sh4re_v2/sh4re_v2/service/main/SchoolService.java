package sh4re_v2.sh4re_v2.service.main;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sh4re_v2.sh4re_v2.domain.main.School;
import sh4re_v2.sh4re_v2.repository.main.SchoolRepository;

@Service
@Transactional(transactionManager="mainTransactionManager")
@RequiredArgsConstructor
public class  SchoolService {

  private final SchoolRepository schoolRepository;

  public Optional<School> findByCode(String code) {
    return schoolRepository.findByCode(code);
  }

  public Optional<School> findById(Long id) {
    return schoolRepository.findById(id);
  }

  public School save(School school) {
    return schoolRepository.save(school);
  }
}
