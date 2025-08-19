package sh4re_v2.sh4re_v2.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import sh4re_v2.sh4re_v2.common.ApiResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import sh4re_v2.sh4re_v2.dto.announcement.CreateAnnouncementResponse;
import sh4re_v2.sh4re_v2.dto.announcement.createAnnouncement.CreateAnnouncementReq;
import sh4re_v2.sh4re_v2.dto.announcement.getAllAnnouncements.GetAllAnnouncementsRes;
import sh4re_v2.sh4re_v2.dto.announcement.getAnnouncement.GetAnnouncementRes;
import sh4re_v2.sh4re_v2.dto.announcement.updateAnnouncement.UpdateAnnouncementReq;
import sh4re_v2.sh4re_v2.service.tenant.AnnouncementService;


@RestController
@RequestMapping("/api/v1/announcements")
@RequiredArgsConstructor
public class AnnouncementController {
  private final AnnouncementService announcementService;

  @GetMapping
  public ResponseEntity<ApiResponse<GetAllAnnouncementsRes>> getAllAnnouncements() {
    GetAllAnnouncementsRes response = announcementService.getAllAnnouncements();
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @GetMapping("/{announcementId}")
  public ResponseEntity<ApiResponse<GetAnnouncementRes>> getAnnouncement(@PathVariable Long announcementId) {
    GetAnnouncementRes response = announcementService.getAnnouncement(announcementId);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @PostMapping
  public ResponseEntity<ApiResponse<CreateAnnouncementResponse>> createAnnouncement(@Valid @RequestBody CreateAnnouncementReq req) {
    CreateAnnouncementResponse response = announcementService.createAnnouncement(req);
    return ResponseEntity.created(URI.create("/api/v1/announcements/" + response.id()))
        .body(ApiResponse.success(response));
  }

  @PatchMapping("/{announcementId}")
  public ResponseEntity<ApiResponse<Void>> updateAnnouncement(@PathVariable Long announcementId, @Valid @RequestBody UpdateAnnouncementReq req) {
    announcementService.updateAnnouncement(announcementId, req);
    return ResponseEntity.ok(ApiResponse.success(null));
  }

  @DeleteMapping("/{announcementId}")
  public ResponseEntity<ApiResponse<Void>> deleteAnnouncement(@PathVariable Long announcementId) {
    announcementService.deleteAnnouncement(announcementId);
    return ResponseEntity.ok(ApiResponse.success(null));
  }
}