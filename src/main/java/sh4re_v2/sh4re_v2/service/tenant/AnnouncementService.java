package sh4re_v2.sh4re_v2.service.tenant;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sh4re_v2.sh4re_v2.common.UserAuthenticationHolder;
import sh4re_v2.sh4re_v2.domain.main.User;
import sh4re_v2.sh4re_v2.domain.tenant.Announcement;
import sh4re_v2.sh4re_v2.domain.tenant.ClassPlacement;
import sh4re_v2.sh4re_v2.dto.announcement.createAnnouncement.CreateAnnouncementReq;
import sh4re_v2.sh4re_v2.dto.announcement.CreateAnnouncementResponse;
import sh4re_v2.sh4re_v2.dto.announcement.getAllAnnouncements.GetAllAnnouncementsRes;
import sh4re_v2.sh4re_v2.dto.announcement.getAnnouncement.GetAnnouncementRes;
import sh4re_v2.sh4re_v2.dto.announcement.updateAnnouncement.UpdateAnnouncementReq;
import sh4re_v2.sh4re_v2.exception.status_code.ClassPlacementStatusCode;
import sh4re_v2.sh4re_v2.exception.status_code.AnnouncementStatusCode;
import sh4re_v2.sh4re_v2.exception.exception.ClassPlacementException;
import sh4re_v2.sh4re_v2.exception.exception.AnnouncementException;
import sh4re_v2.sh4re_v2.repository.tenant.AnnouncementRepository;
import sh4re_v2.sh4re_v2.security.AuthorizationService;

@Service
@Transactional(transactionManager = "tenantTransactionManager")
@RequiredArgsConstructor
public class AnnouncementService {
  private final AnnouncementRepository announcementRepository;
  private final UserAuthenticationHolder holder;
  private final ClassPlacementService classPlacementService;
  private final AuthorizationService authorizationService;

  public Announcement save(Announcement announcement) {
    return announcementRepository.save(announcement);
  }

  public Optional<Announcement> findById(Long id) {
    return announcementRepository.findById(id);
  }

  public void deleteById(Long id) {
    announcementRepository.deleteById(id);
  }

  public GetAllAnnouncementsRes getAllAnnouncements() {
    User user = holder.current();
    Optional<ClassPlacement> classPlacementOpt = classPlacementService.findLatestClassPlacementByUserId(user.getId());
    if(classPlacementOpt.isEmpty()) throw ClassPlacementException.of(ClassPlacementStatusCode.CLASS_PLACEMENT_NOT_FOUND);
    ClassPlacement classPlacement = classPlacementOpt.get();
    List<Announcement> announcements = announcementRepository
        .findAllByGradeAndClassNumberAndSchoolYear(
            classPlacement.getGrade(),
            classPlacement.getClassNumber(),
            classPlacement.getSchoolYear()
        );
    return GetAllAnnouncementsRes.from(announcements);
  }

  public CreateAnnouncementResponse createAnnouncement(CreateAnnouncementReq req) {
    User user = holder.current();
    Announcement newAnnouncement = req.toEntity(user.getId());
    this.save(newAnnouncement);
    return new CreateAnnouncementResponse(newAnnouncement.getId());
  }

  public GetAnnouncementRes getAnnouncement(Long id) {
    Announcement announcement = getAnnouncementById(id);
    authorizationService.requireReadAccess(announcement);
    return GetAnnouncementRes.from(announcement);
  }

  public void updateAnnouncement(Long id, UpdateAnnouncementReq req) {
    Announcement announcement = getAnnouncementById(id);
    authorizationService.requireWriteAccess(announcement);
    Announcement newAnnouncement = req.toEntity(announcement);
    this.save(newAnnouncement);
  }

  public void deleteAnnouncement(Long id) {
    Announcement announcement = getAnnouncementById(id);
    authorizationService.requireWriteAccess(announcement);
    this.deleteById(id);
  }
  
  private Announcement getAnnouncementById(Long id) {
    Optional<Announcement> announcementOpt = this.findById(id);
    if(announcementOpt.isEmpty()) throw AnnouncementException.of(AnnouncementStatusCode.ANNOUNCEMENT_NOT_FOUND);
    return announcementOpt.get();
  }
}