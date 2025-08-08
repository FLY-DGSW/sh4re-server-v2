package sh4re_v2.sh4re_v2.dto.user.getMyInfo;

import sh4re_v2.sh4re_v2.common.Theme;
import sh4re_v2.sh4re_v2.domain.main.School;
import sh4re_v2.sh4re_v2.domain.main.User;
import sh4re_v2.sh4re_v2.domain.tenant.ClassPlacement;
import sh4re_v2.sh4re_v2.security.Role;

public record UserWithClassInfo(
    Long id,
    String username,
    String email,
    String name,
    int admissionYear,
    Role role,
    Theme theme,
    Integer grade,
    Integer classNumber,
    Integer studentNumber,
    School school
) {
    public static UserWithClassInfo from(User user, ClassPlacement classPlacement) {
        return new UserWithClassInfo(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getName(),
            user.getAdmissionYear(),
            user.getRole(),
            user.getTheme(),
            classPlacement != null ? classPlacement.getGrade() : null,
            classPlacement != null ? classPlacement.getClassNumber() : null,
            classPlacement != null ? classPlacement.getStudentNumber() : null,
            user.getSchool()
        );
    }
}