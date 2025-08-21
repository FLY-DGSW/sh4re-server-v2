package sh4re_v2.sh4re_v2.service.tenant;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sh4re_v2.sh4re_v2.common.UserAuthenticationHolder;
import sh4re_v2.sh4re_v2.domain.main.User;
import sh4re_v2.sh4re_v2.domain.tenant.Code;
import sh4re_v2.sh4re_v2.domain.tenant.CodeLike;
import sh4re_v2.sh4re_v2.domain.tenant.ClassPlacement;
import sh4re_v2.sh4re_v2.dto.code.createCode.CreateCodeReq;
import sh4re_v2.sh4re_v2.dto.code.CreateCodeResponse;
import sh4re_v2.sh4re_v2.dto.code.getAllCodes.GetAllCodesRes;
import sh4re_v2.sh4re_v2.dto.code.getCode.GetCodeRes;
import sh4re_v2.sh4re_v2.dto.code.updateCode.UpdateCodeReq;
import sh4re_v2.sh4re_v2.dto.code.toggleLike.ToggleLikeRes;
import sh4re_v2.sh4re_v2.exception.exception.AssignmentException;
import sh4re_v2.sh4re_v2.exception.status_code.AssignmentStatusCode;
import sh4re_v2.sh4re_v2.exception.status_code.AuthStatusCode;
import sh4re_v2.sh4re_v2.exception.status_code.ClassPlacementStatusCode;
import sh4re_v2.sh4re_v2.exception.status_code.CodeStatusCode;
import sh4re_v2.sh4re_v2.exception.exception.AuthException;
import sh4re_v2.sh4re_v2.exception.exception.ClassPlacementException;
import sh4re_v2.sh4re_v2.exception.exception.CodeException;
import sh4re_v2.sh4re_v2.repository.tenant.CodeRepository;
import sh4re_v2.sh4re_v2.repository.tenant.CodeLikeRepository;
import sh4re_v2.sh4re_v2.service.main.OpenAIService;
import sh4re_v2.sh4re_v2.domain.tenant.Assignment;
import sh4re_v2.sh4re_v2.service.tenant.AssignmentService;
import sh4re_v2.sh4re_v2.service.main.UserService;
import sh4re_v2.sh4re_v2.security.AuthorizationService;

@Service
@Transactional(transactionManager = "tenantTransactionManager")
@RequiredArgsConstructor
public class CodeService {
  private final CodeRepository codeRepository;
  private final CodeLikeRepository codeLikeRepository;
  private final UserAuthenticationHolder holder;
  private final ClassPlacementService classPlacementService;
  private final OpenAIService openAIService;
  private final AssignmentService assignmentService;
  private final UserService userService;
  private final SubjectService subjectService;
  private final AuthorizationService authorizationService;

  public Code save(Code code) {
    return codeRepository.save(code);
  }

  public Optional<Code> findById(Long id) {
    return codeRepository.findById(id);
  }

  public void deleteById(Long id) {
    codeRepository.deleteById(id);
  }

  public GetAllCodesRes getAllCodes() {
    User user = holder.current();
    Optional<ClassPlacement> classPlacementOpt = classPlacementService.findLatestClassPlacementByUserId(user.getId());
    if(classPlacementOpt.isEmpty()) throw ClassPlacementException.of(ClassPlacementStatusCode.CLASS_PLACEMENT_NOT_FOUND);
    ClassPlacement classPlacement = classPlacementOpt.get();
    
    // 같은 반의 마감 기한이 지난 과제의 코드만 조회
    List<Code> codes = codeRepository.findAllBySchoolYearAndGradeAndClassNumberWithExpiredAssignments(
        classPlacement.getSchoolYear(),
        classPlacement.getGrade(),
        classPlacement.getClassNumber(),
        java.time.LocalDateTime.now()
    );
    
    // 필요한 데이터를 서비스 레이어에서 미리 조회
    List<GetAllCodesRes.CodeWithDetails> codeDetails = codes.stream()
        .map(code -> {
          Long likeCount = getLikeCount(code.getId());
          User author = userService.getUserOrElseThrow(code.getAuthorId());
          ClassPlacement authorClass = classPlacementService.findLatestClassPlacementByUserIdOrElseThrow(code.getAuthorId());
          return new GetAllCodesRes.CodeWithDetails(
              code,
              likeCount,
              author,
              authorClass
          );
        })
        .toList();
    
    return GetAllCodesRes.from(codeDetails);
  }

  public CreateCodeResponse createCode(CreateCodeReq req) {
    User user = holder.current();
    
    // ClassPlacement 조회
    ClassPlacement classPlacement = classPlacementService.findLatestClassPlacementByUserIdOrElseThrow(user.getId());

    Assignment assignment = null;
    if(req.assignmentId() != null){
      // Assignment 조회
      Optional<Assignment> assignmentOpt = assignmentService.findById(req.assignmentId());
      if(assignmentOpt.isEmpty()) throw AssignmentException.of(AssignmentStatusCode.ASSIGNMENT_NOT_FOUND);
      assignment = assignmentOpt.get();
      if(!authorizationService.canAccessSubject(assignment.getSubject())) throw AuthException.of(AuthStatusCode.PERMISSION_DENIED);
    }
    
    String finalDescription = req.description();
    
    // AI 자동 생성 옵션이 true인 경우 OpenAI로 설명 생성
    if (Boolean.TRUE.equals(req.useAiDescription())) {
      try {
        String assignmentTitle = assignment != null ? assignment.getTitle() : "";
        finalDescription = openAIService.generateCodeDescription(
            req.code(),
            req.language(),
            assignmentTitle
        );
      } catch (Exception e) {
        // AI 생성 실패시 기존 description 사용 (fallback은 OpenAIService에서 처리)
        finalDescription = req.description();
      }
    }
    
    // 수정된 toEntity 메서드 호출
    Code newCode = req.toEntityWithDescription(user.getId(), classPlacement, assignment, finalDescription);
    this.save(newCode);
    return new CreateCodeResponse(newCode.getId());
  }

  public GetCodeRes getCode(Long id) {
    User user = holder.current();
    Code code = getCodeById(id);
    authorizationService.requireReadAccess(code);
    Long likeCount = getLikeCount(id);
    boolean isLikedByUser = isLikedByUser(id, user.getId());
    
    // 작성자 정보 조회
    User author = userService.getUserOrElseThrow(code.getAuthorId());
    ClassPlacement authorClass = classPlacementService.findLatestClassPlacementByUserIdOrElseThrow(code.getAuthorId());
    
    return GetCodeRes.from(code, likeCount, isLikedByUser, author, authorClass);
  }

  public void updateCode(Long codeId, UpdateCodeReq req) {
    Code code = getCodeById(codeId);
    authorizationService.requireWriteAccess(code);

    Code newCode = req.toEntity(code);
    this.save(newCode);
  }

  public void deleteCode(Long codeId) {
    Code code = getCodeById(codeId);
    authorizationService.requireWriteAccess(code);
    this.deleteById(codeId);
  }

  public ToggleLikeRes toggleLike(Long codeId) {
    User user = holder.current();
    Code code = getCodeById(codeId);
    authorizationService.requireReadAccess(code);
    
    Optional<CodeLike> codeLikeOpt = codeLikeRepository.findByCodeIdAndUserId(codeId, user.getId());
    boolean isLiked;
    
    if(codeLikeOpt.isPresent()) {
      codeLikeRepository.delete(codeLikeOpt.get());
      isLiked = false;
    } else {
      CodeLike newCodeLike = CodeLike.builder()
          .codeId(codeId)
          .userId(user.getId())
          .build();
      codeLikeRepository.save(newCodeLike);
      isLiked = true;
    }
    
    Long likeCount = codeLikeRepository.countByCodeId(codeId);
    return new ToggleLikeRes(isLiked, likeCount);
  }

  public Long getLikeCount(Long codeId) {
    return codeLikeRepository.countByCodeId(codeId);
  }

  public boolean isLikedByUser(Long codeId, Long userId) {
    return codeLikeRepository.existsByCodeIdAndUserId(codeId, userId);
  }

  private Code getCodeById(Long codeId) {
    if(codeId == null) throw CodeException.of(CodeStatusCode.CODE_NOT_FOUND);
    Optional<Code> codeOpt = this.findById(codeId);
    if(codeOpt.isEmpty()) throw CodeException.of(CodeStatusCode.CODE_NOT_FOUND);
    return codeOpt.get();
  }


}