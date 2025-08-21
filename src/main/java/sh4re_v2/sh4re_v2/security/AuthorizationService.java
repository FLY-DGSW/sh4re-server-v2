package sh4re_v2.sh4re_v2.security;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sh4re_v2.sh4re_v2.common.UserAuthenticationHolder;
import sh4re_v2.sh4re_v2.domain.main.User;
import sh4re_v2.sh4re_v2.domain.tenant.Announcement;
import sh4re_v2.sh4re_v2.domain.tenant.Assignment;
import sh4re_v2.sh4re_v2.domain.tenant.ClassPlacement;
import sh4re_v2.sh4re_v2.domain.tenant.Code;
import sh4re_v2.sh4re_v2.domain.tenant.Handout;
import sh4re_v2.sh4re_v2.domain.tenant.Subject;
import sh4re_v2.sh4re_v2.domain.tenant.Unit;
import sh4re_v2.sh4re_v2.exception.exception.AuthException;
import sh4re_v2.sh4re_v2.exception.status_code.AuthStatusCode;
import sh4re_v2.sh4re_v2.service.tenant.ClassPlacementService;

@Service
@RequiredArgsConstructor
public class AuthorizationService {
    
    private final UserAuthenticationHolder holder;
    private final ClassPlacementService classPlacementService;
    
    // =============== Subject 권한 검사 ===============
    public void requireReadAccess(Subject subject) {
        User user = holder.current();
        if (!canAccessSubject(subject, user)) {
            throw AuthException.of(AuthStatusCode.PERMISSION_DENIED);
        }
    }
    
    public void requireWriteAccess(Subject subject) {
        requireReadAccess(subject);
        User user = holder.current();
        if (!subject.getAuthorId().equals(user.getId()) && !isAdminOrTeacher(user)) {
            throw AuthException.of(AuthStatusCode.PERMISSION_DENIED);
        }
    }
    
    // =============== Assignment 권한 검사 ===============
    public void requireReadAccess(Assignment assignment) {
        User user = holder.current();
        if (!canAccessAssignment(assignment, user)) {
            throw AuthException.of(AuthStatusCode.PERMISSION_DENIED);
        }
    }
    
    public void requireWriteAccess(Assignment assignment) {
        requireReadAccess(assignment);
        User user = holder.current();
        if (!assignment.getAuthorId().equals(user.getId()) && !isAdminOrTeacher(user)) {
            throw AuthException.of(AuthStatusCode.PERMISSION_DENIED);
        }
    }
    
    // =============== Unit 권한 검사 ===============
    public void requireReadAccess(Unit unit) {
        User user = holder.current();
        if (!canAccessUnit(unit, user)) {
            throw AuthException.of(AuthStatusCode.PERMISSION_DENIED);
        }
    }
    
    public void requireWriteAccess(Unit unit) {
        requireReadAccess(unit);
        User user = holder.current();
        if (!unit.getAuthorId().equals(user.getId()) && !isAdminOrTeacher(user)) {
            throw AuthException.of(AuthStatusCode.PERMISSION_DENIED);
        }
    }
    
    // =============== Code 권한 검사 ===============
    public void requireReadAccess(Code code) {
        User user = holder.current();
        if (!canAccessCode(code, user)) {
            throw AuthException.of(AuthStatusCode.PERMISSION_DENIED);
        }
    }
    
    public void requireWriteAccess(Code code) {
        requireReadAccess(code);
        User user = holder.current();
        if (!code.getAuthorId().equals(user.getId()) && !isAdminOrTeacher(user)) {
            throw AuthException.of(AuthStatusCode.PERMISSION_DENIED);
        }
    }
    
    // =============== Handout 권한 검사 ===============
    public void requireReadAccess(Handout handout) {
        User user = holder.current();
        if (!canAccessHandout(handout, user)) {
            throw AuthException.of(AuthStatusCode.PERMISSION_DENIED);
        }
    }
    
    public void requireWriteAccess(Handout handout) {
        requireReadAccess(handout);
        User user = holder.current();
        if (!handout.getAuthorId().equals(user.getId()) && !isAdminOrTeacher(user)) {
            throw AuthException.of(AuthStatusCode.PERMISSION_DENIED);
        }
    }
    
    // =============== ClassPlacement 권한 검사 ===============
    public void requireAccess(ClassPlacement classPlacement) {
        User user = holder.current();
        if (!canAccessClassPlacement(classPlacement, user)) {
            throw AuthException.of(AuthStatusCode.PERMISSION_DENIED);
        }
    }
    
    // =============== Announcement 권한 검사 ===============
    public void requireReadAccess(Announcement announcement) {
        User user = holder.current();
        if (!canAccessAnnouncement(announcement, user)) {
            throw AuthException.of(AuthStatusCode.PERMISSION_DENIED);
        }
    }
    
    public void requireWriteAccess(Announcement announcement) {
        requireReadAccess(announcement);
        User user = holder.current();
        if (!announcement.getUserId().equals(user.getId()) && !isAdminOrTeacher(user)) {
            throw AuthException.of(AuthStatusCode.PERMISSION_DENIED);
        }
    }
    
    // =============== 내부 권한 검사 로직 ===============
    private boolean isAdminOrTeacher(User user) {
        return user.getRole() == Role.ADMIN || user.getRole() == Role.TEACHER;
    }
    
    private boolean canAccessSubject(Subject subject, User user) {
        if (isAdminOrTeacher(user)) return true;
        if (subject.getAuthorId().equals(user.getId())) return true;
        
        List<ClassPlacement> classPlacements = classPlacementService.findAllByUserId(user.getId());
        for(ClassPlacement cp : classPlacements) {
            if (subject.getGrade().equals(cp.getGrade()) && 
                subject.getClassNumber().equals(cp.getClassNumber()) && 
                subject.getSchoolYear().equals(cp.getSchoolYear())) {
                return true;
            }
        }
        return false;
    }
    
    private boolean canAccessAssignment(Assignment assignment, User user) {
        if (isAdminOrTeacher(user)) return true;
        return canAccessSubject(assignment.getSubject(), user);
    }
    
    private boolean canAccessUnit(Unit unit, User user) {
        if (isAdminOrTeacher(user)) return true;
        if (unit.getAuthorId().equals(user.getId())) return true;
        return canAccessSubject(unit.getSubject(), user);
    }
    
    private boolean canAccessCode(Code code, User user) {
        if (isAdminOrTeacher(user)) return true;
        if (code.getAuthorId().equals(user.getId())) return true;
        if (code.getAssignment() != null && code.getAssignment().isOverdue()) return true;
        return false;
    }
    
    private boolean canAccessHandout(Handout handout, User user) {
        if (isAdminOrTeacher(user)) return true;
        return canAccessSubject(handout.getSubject(), user);
    }
    
    private boolean canAccessClassPlacement(ClassPlacement classPlacement, User user) {
        if (isAdminOrTeacher(user)) return true;
        return classPlacement.getUserId().equals(user.getId());
    }
    
    private boolean canAccessAnnouncement(Announcement announcement, User user) {
        if (isAdminOrTeacher(user)) return true;
        if (announcement.getUserId().equals(user.getId())) return true;
        
        // 같은 학년/반 학생들은 공지사항 읽기 가능
        List<ClassPlacement> classPlacements = classPlacementService.findAllByUserId(user.getId());
        for(ClassPlacement cp : classPlacements) {
            if (announcement.getGrade().equals(cp.getGrade()) && 
                announcement.getClassNumber().equals(cp.getClassNumber()) && 
                announcement.getSchoolYear().equals(cp.getSchoolYear())) {
                return true;
            }
        }
        return false;
    }
    
    // =============== Public 권한 체크 메소드들 ===============
    public boolean canAccessSubject(Subject subject) {
        return canAccessSubject(subject, holder.current());
    }
    
    public boolean canAccessAssignment(Assignment assignment) {
        return canAccessAssignment(assignment, holder.current());
    }
    
    public boolean canAccessUnit(Unit unit) {
        return canAccessUnit(unit, holder.current());
    }
    
    public boolean canAccessCode(Code code) {
        return canAccessCode(code, holder.current());
    }
    
    public boolean canAccessHandout(Handout handout) {
        return canAccessHandout(handout, holder.current());
    }
    
    public boolean canAccessAnnouncement(Announcement announcement) {
        return canAccessAnnouncement(announcement, holder.current());
    }
}