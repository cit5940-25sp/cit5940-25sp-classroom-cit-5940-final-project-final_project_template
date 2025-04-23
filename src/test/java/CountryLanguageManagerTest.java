import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CountryLanguageManagerTest {
    @Test
    public void testInitializeData() throws CsvValidationException, IOException {
        CountryLanguageManager clm = new CountryLanguageManager();
        URL dataResource = getClass().getClassLoader().getResource("data.csv");
        assertNotNull(dataResource);
        clm.initializeData(dataResource);
        assertEquals(28, clm.getAllCountries().size());
        System.out.println(clm.getAllLanguages());
        assertEquals(3, clm.getCountry("Libya").getLanguages().size());
        System.out.println(clm.getCountry("Libya").getLanguages());
    }
}
