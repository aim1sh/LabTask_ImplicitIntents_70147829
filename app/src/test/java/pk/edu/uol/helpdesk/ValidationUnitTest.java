package pk.edu.uol.helpdesk;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests verifying validation logic specified in Course Lab Manual (Labs 3–5)
 * for Scenario 1 and Scenario 2.
 */
public class ValidationUnitTest {

    @Test
    public void testNameValidation_NonEmptyAccepted() {
        String name = "Muhammad Ali";
        assertFalse("Name should not be empty", name.trim().isEmpty());
    }

    @Test
    public void testNameValidation_EmptyRejected() {
        String emptyName = "   ";
        assertTrue("Whitespace should be detected as empty", emptyName.trim().isEmpty());
    }

    @Test
    public void testPhoneValidation_Valid11DigitsStartingWith03() {
        String validNumber1 = "03001234567";
        String validNumber2 = "03459876543";
        String validNumber3 = "03120000000";

        assertTrue("03001234567 should be valid", validNumber1.matches("^03[0-9]{9}$"));
        assertTrue("03459876543 should be valid", validNumber2.matches("^03[0-9]{9}$"));
        assertTrue("03120000000 should be valid", validNumber3.matches("^03[0-9]{9}$"));
    }

    @Test
    public void testPhoneValidation_InvalidNumbersRejected() {
        // Less than 11 digits
        assertFalse("Too short number must be rejected", "0300123456".matches("^03[0-9]{9}$"));
        // More than 11 digits
        assertFalse("Too long number must be rejected", "030012345678".matches("^03[0-9]{9}$"));
        // Does not start with 03
        assertFalse("Must start with 03", "04212345678".matches("^03[0-9]{9}$"));
        assertFalse("Must start with 03", "05112345678".matches("^03[0-9]{9}$"));
        assertFalse("International +92 must be rejected by 03 rule", "+923001234567".matches("^03[0-9]{9}$"));
        // Non-digits
        assertFalse("Alpha characters must be rejected", "0300123abcd".matches("^03[0-9]{9}$"));
    }

    @Test
    public void testLinkFormatting_HttpOrHttpsPreserved() {
        String httpsLink = "https://www.uol.edu.pk";
        String formattedHttps = formatLink(httpsLink);
        assertEquals("https://www.uol.edu.pk", formattedHttps);

        String httpLink = "http://slate.uol.edu.pk";
        String formattedHttp = formatLink(httpLink);
        assertEquals("http://slate.uol.edu.pk", formattedHttp);
    }

    @Test
    public void testLinkFormatting_PrependHttpsWhenMissing() {
        String plainDomain = "www.uol.edu.pk";
        String formatted = formatLink(plainDomain);
        assertEquals("https://www.uol.edu.pk", formatted);

        String pathOnly = "uol.edu.pk/admissions";
        String formattedPath = formatLink(pathOnly);
        assertEquals("https://uol.edu.pk/admissions", formattedPath);
    }

    private String formatLink(String raw) {
        String lower = raw.toLowerCase();
        if (!lower.startsWith("http://") && !lower.startsWith("https://")) {
            return "https://" + raw;
        }
        return raw;
    }
}
