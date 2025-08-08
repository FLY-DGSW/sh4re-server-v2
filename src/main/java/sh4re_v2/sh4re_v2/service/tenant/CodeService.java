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
import sh4re_v2.sh4re_v2.dto.code.createCode.CreateCodeRes;
import sh4re_v2.sh4re_v2.dto.code.deleteCode.DeleteCodeRes;
import sh4re_v2.sh4re_v2.dto.code.getAllCodes.GetAllCodesRes;
import sh4re_v2.sh4re_v2.dto.code.getCode.GetCodeRes;
import sh4re_v2.sh4re_v2.dto.code.updateCode.UpdateCodeReq;
import sh4re_v2.sh4re_v2.dto.code.updateCode.UpdateCodeRes;
import sh4re_v2.sh4re_v2.dto.code.toggleLike.ToggleLikeRes;
import sh4re_v2.sh4re_v2.exception.status_code.AuthStatusCode;
import sh4re_v2.sh4re_v2.exception.status_code.ClassPlacementStatusCode;
import sh4re_v2.sh4re_v2.exception.status_code.CodeStatusCode;
import sh4re_v2.sh4re_v2.exception.exception.AuthException;
import sh4re_v2.sh4re_v2.exception.exception.ClassPlacementException;
import sh4re_v2.sh4re_v2.exception.exception.CodeException;
import sh4re_v2.sh4re_v2.repository.tenant.CodeRepository;
import sh4re_v2.sh4re_v2.repository.tenant.CodeLikeRepository;

@Service
@Transactional(transactionManager = "tenantTransactionManager")
@RequiredArgsConstructor
public class CodeService {
  private final CodeRepository codeRepository;
  private final CodeLikeRepository codeLikeRepository;
  private final UserAuthenticationHolder holder;
  private final ClassPlacementService classPlacementService;

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
    List<Code> codes = codeRepository.findAllBySchoolYear(classPlacement.getSchoolYear());
    return GetAllCodesRes.from(codes, this::getLikeCount);
  }

  public CreateCodeRes createCode(CreateCodeReq req) {
    User user = holder.current();
    Code newCode = req.toEntity(user.getId(), user.getName());
    this.save(newCode);
    return new CreateCodeRes(newCode.getId());
  }

  public GetCodeRes getCode(Long id) {
    User user = holder.current();
    Optional<Code> codeOpt = this.findById(id);
    if(codeOpt.isEmpty()) throw CodeException.of(CodeStatusCode.CODE_NOT_FOUND);
    Code code = codeOpt.get();
    Long likeCount = getLikeCount(id);
    boolean isLikedByUser = isLikedByUser(id, user.getId());
    return GetCodeRes.from(code, likeCount, isLikedByUser);
  }

  public UpdateCodeRes updateCode(Long id, UpdateCodeReq req) {
    User user = holder.current();
    Optional<Code> codeOpt = this.findById(id);
    if(codeOpt.isEmpty()) throw CodeException.of(CodeStatusCode.CODE_NOT_FOUND);
    Code code = codeOpt.get();
    if(!code.getUserId().equals(user.getId())) throw AuthException.of(AuthStatusCode.PERMISSION_DENIED);
    Code newCode = req.toEntity(code);
    this.save(newCode);
    return new UpdateCodeRes(newCode.getId());
  }

  public DeleteCodeRes deleteCode(Long id) {
    User user = holder.current();
    Optional<Code> codeOpt = this.findById(id);
    if(codeOpt.isEmpty()) throw CodeException.of(CodeStatusCode.CODE_NOT_FOUND);
    Code code = codeOpt.get();
    if(!code.getUserId().equals(user.getId())) throw AuthException.of(AuthStatusCode.PERMISSION_DENIED);
    this.deleteById(id);
    return new DeleteCodeRes(code.getId());
  }

  public ToggleLikeRes toggleLike(Long codeId) {
    User user = holder.current();
    Optional<Code> codeOpt = this.findById(codeId);
    if(codeOpt.isEmpty()) throw CodeException.of(CodeStatusCode.CODE_NOT_FOUND);
    
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
}