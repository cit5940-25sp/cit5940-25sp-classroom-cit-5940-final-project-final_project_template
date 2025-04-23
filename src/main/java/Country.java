import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a country with its official languages
 */
public class Country {
    private final String name;
    private final Set<Language> languages;

    public Country(String name, Set<Language> languages) {
        this.name = name;
        this.languages = Set.copyOf(languages);
    }

    public String getName() {
        return name;
    }

    public Set<Language> getLanguages() {
        return languages;
    }

    public boolean hasLanguage(Language language) {
        return languages.contains(language);
    }

    public Set<Language> getSharedLanguages(Country otherCountry) {
        Set<Language> shared = new HashSet<>(languages);
        shared.retainAll(otherCountry.getLanguages());
        return shared;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return name.equalsIgnoreCase(country.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase());
    }

    @Override
    public String toString() {
        return name;
    }
}
