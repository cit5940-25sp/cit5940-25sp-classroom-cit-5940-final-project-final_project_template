import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

public class CountryLanguageManagerTest {
    private CountryLanguageManager clm;

    @Test
    public void testInitializeData() throws CsvValidationException, IOException {
        clm = new CountryLanguageManager();
        URL dataResource = getClass().getClassLoader().getResource("data.csv");
        assertNotNull(dataResource);
        clm.initializeData(dataResource);
        assertEquals(37, clm.getAllCountries().size());
//        System.out.println(clm.getAllLanguages());
        assertEquals(3, clm.getCountry("Libya").getLanguages().size());
//        System.out.println(clm.getCountry("Libya").getLanguages());
    }
    @Test
    public void testFlexibleMatchingWithCountries() throws CsvValidationException, IOException {
        clm = new CountryLanguageManager();
        URL dataResource = getClass().getClassLoader().getResource("data.csv");
        assertNotNull(dataResource);
        clm.initializeData(dataResource);

        Country libya = clm.getCountryFlexibleMatch("Libya");
        assertNotNull(libya);
        assertEquals("Libya", libya.getName());

        // Test case insensitivity
        Country libyaLower = clm.getCountryFlexibleMatch("libya");
        assertNotNull(libyaLower);
        assertSame(libya, libyaLower);

        // Test with spaces and punctuation if applicable
        Country libyaWithSpace = clm.getCountryFlexibleMatch("Lib' ya");
        if (libyaWithSpace != null) {
            assertSame(libya, libyaWithSpace);
        }
    }

}
