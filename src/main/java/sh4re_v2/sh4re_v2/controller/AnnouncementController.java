package sh4re_v2.sh4re_v2.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sh4re_v2.sh4re_v2.dto.announcement.createAnnouncement.CreateAnnouncementReq;
import sh4re_v2.sh4re_v2.dto.announcement.createAnnouncement.CreateAnnouncementRes;
import sh4re_v2.sh4re_v2.dto.announcement.deleteAnnouncement.DeleteAnnouncementRes;
import sh4re_v2.sh4re_v2.dto.announcement.getAllAnnouncements.GetAllAnnouncementsRes;
import sh4re_v2.sh4re_v2.dto.announcement.getAnnouncement.GetAnnouncementRes;
import sh4re_v2.sh4re_v2.dto.announcement.updateAnnouncement.UpdateAnnouncementReq;
import sh4re_v2.sh4re_v2.dto.announcement.updateAnnouncement.UpdateAnnouncementRes;
import sh4re_v2.sh4re_v2.service.tenant.AnnouncementService;

@RestController
@RequestMapping("/announcements")
@RequiredArgsConstructor
public class AnnouncementController {
  private final AnnouncementService announcementService;

  @GetMapping
  public GetAllAnnouncementsRes getAllAnnouncements() {
    return announcementService.getAllAnnouncements();
  }

  @GetMapping("/{announcementId}")
  public GetAnnouncementRes getAnnouncement(@PathVariable Long announcementId) {
    return announcementService.getAnnouncement(announcementId);
  }

  @PostMapping
  public CreateAnnouncementRes createAnnouncement(@Valid @RequestBody CreateAnnouncementReq req) {
    return announcementService.createAnnouncement(req);
  }

  @PatchMapping("/{announcementId}")
  public UpdateAnnouncementRes updateAnnouncement(@PathVariable Long announcementId, @Valid @RequestBody UpdateAnnouncementReq req) {
    return announcementService.updateAnnouncement(announcementId, req);
  }

  @DeleteMapping("/{announcementId}")
  public DeleteAnnouncementRes deleteAnnouncement(@PathVariable Long announcementId) {
    return announcementService.deleteAnnouncement(announcementId);
  }
}