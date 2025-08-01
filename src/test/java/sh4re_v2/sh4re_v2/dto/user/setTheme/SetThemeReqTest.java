package sh4re_v2.sh4re_v2.dto.user.setTheme;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sh4re_v2.sh4re_v2.common.Theme;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SetThemeReqTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validThemeName_noViolations() {
        // Test with valid theme names
        SetThemeReq req1 = new SetThemeReq("WHITE");
        Set<ConstraintViolation<SetThemeReq>> violations1 = validator.validate(req1);
        assertTrue(violations1.isEmpty());

        SetThemeReq req2 = new SetThemeReq("white");
        Set<ConstraintViolation<SetThemeReq>> violations2 = validator.validate(req2);
        assertTrue(violations2.isEmpty());

        SetThemeReq req3 = new SetThemeReq("DARK");
        Set<ConstraintViolation<SetThemeReq>> violations3 = validator.validate(req3);
        assertTrue(violations3.isEmpty());

        SetThemeReq req4 = new SetThemeReq("dark");
        Set<ConstraintViolation<SetThemeReq>> violations4 = validator.validate(req4);
        assertTrue(violations4.isEmpty());
    }

    @Test
    void invalidThemeName_hasViolations() {
        // Test with invalid theme name
        SetThemeReq req = new SetThemeReq("INVALID");
        Set<ConstraintViolation<SetThemeReq>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Invalid value. This is not permitted.", violations.iterator().next().getMessage());
    }

    @Test
    void nullThemeName_hasViolations() {
        // Test with null theme name
        SetThemeReq req = new SetThemeReq(null);
        Set<ConstraintViolation<SetThemeReq>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("테마 이름은 필수 입력 값입니다.", violations.iterator().next().getMessage());
    }

    @Test
    void getThemeEnum_validThemeName_returnsCorrectEnum() {
        // Test getThemeEnum with valid theme name
        SetThemeReq req1 = new SetThemeReq("WHITE");
        assertEquals(Theme.WHITE, req1.getThemeEnum());

        SetThemeReq req2 = new SetThemeReq("white");
        assertEquals(Theme.WHITE, req2.getThemeEnum());

        SetThemeReq req3 = new SetThemeReq("DARK");
        assertEquals(Theme.DARK, req3.getThemeEnum());

        SetThemeReq req4 = new SetThemeReq("dark");
        assertEquals(Theme.DARK, req4.getThemeEnum());
    }

    @Test
    void getThemeEnum_invalidThemeName_throwsException() {
        // Test getThemeEnum with invalid theme name
        SetThemeReq req = new SetThemeReq("INVALID");
        assertThrows(IllegalArgumentException.class, req::getThemeEnum);
    }
}