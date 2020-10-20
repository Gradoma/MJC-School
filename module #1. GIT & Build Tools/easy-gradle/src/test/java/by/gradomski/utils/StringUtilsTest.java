package by.gradomski.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {
    private StringUtils stringUtils = new StringUtils();

    @Test
    void isPositiveNumber() {
        assertTrue(stringUtils.isPositiveNumber("23.765"));
    }
}