package sh4re_v2.sh4re_v2.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sh4re_v2.sh4re_v2.domain.School;
import sh4re_v2.sh4re_v2.repository.SchoolRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class SchoolService {

  private final SchoolRepository schoolRepository;

  @Transactional(readOnly = true)
  public Optional<School> findByCode(String code) {
    return schoolRepository.findByCode(code);
  }

  @Transactional(readOnly = true)
  public Optional<School> findById(Long id) {
    return schoolRepository.findById(id);
  }

  @Transactional
  public School save(School school) {
    return schoolRepository.save(school);
  }
}
