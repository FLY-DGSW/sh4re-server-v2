package sh4re_v2.sh4re_v2.validation.validator;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sh4re_v2.sh4re_v2.common.Theme;
import sh4re_v2.sh4re_v2.validation.annotation.ValidEnum;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class EnumValidatorTest {

    private EnumValidator validator;

    @Mock
    private ValidEnum validEnum;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new EnumValidator();
        when(validEnum.enumClass()).thenReturn((Class) Theme.class);
        validator.initialize(validEnum);
    }

    @Test
    void isValid_validValue_returnsTrue() {
        // Test with valid values
        assertTrue(validator.isValid("LIGHT", context));
        assertTrue(validator.isValid("light", context));
        assertTrue(validator.isValid("DARK", context));
        assertTrue(validator.isValid("dark", context));
    }

    @Test
    void isValid_invalidValue_returnsFalse() {
        // Test with invalid values
        assertFalse(validator.isValid("INVALID", context));
        assertFalse(validator.isValid("d", context));
        assertFalse(validator.isValid("", context));
    }

    @Test
    void isValid_nullValue_returnsTrue() {
        // Test with null value
        assertTrue(validator.isValid(null, context));
    }
}
