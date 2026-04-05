import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class CountryInfo {
    private final String name;
    private final Map<String, String> attributes = new LinkedHashMap<>();
    private Path outlineImage;
    private Path locationImage;
    private Path flagImage;

    public CountryInfo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addAttribute(String key, String value) {
        attributes.put(key, value);
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public Path getOutlineImage() {
        return outlineImage;
    }

    public void setOutlineImage(Path outlineImage) {
        this.outlineImage = outlineImage;
    }

    public Path getLocationImage() {
        return locationImage;
    }

    public void setLocationImage(Path locationImage) {
        this.locationImage = locationImage;
    }

    public Path getFlagImage() {
        return flagImage;
    }

    public void setFlagImage(Path flagImage) {
        this.flagImage = flagImage;
    }
}
