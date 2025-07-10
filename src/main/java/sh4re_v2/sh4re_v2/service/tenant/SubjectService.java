package sh4re_v2.sh4re_v2.service.tenant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sh4re_v2.sh4re_v2.dto.getAllSubjects.GetAllSubjectsRes;
import sh4re_v2.sh4re_v2.repository.tenant.SubjectRepository;

@Service
@Transactional(transactionManager = "tenantTransactionManager")
@RequiredArgsConstructor
public class SubjectService {
  private final SubjectRepository subjectRepository;

  public GetAllSubjectsRes getAllSubjects() {
    return new GetAllSubjectsRes(subjectRepository.findAll());
  }
}
