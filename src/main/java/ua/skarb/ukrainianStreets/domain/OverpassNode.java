package ua.skarb.ukrainianStreets.domain;

import java.util.Map;

/**
 * Class {@code OverpassNode} is use for mapping data, retrieved from Overpass API.
 *
 * @author Vynohradov Evgeniy
 * @see <a href="http://wiki.openstreetmap.org/wiki/Overpass_API">Overpass API</>
 */
public class OverpassNode {

    /**
     * Type from Overpass API (node, way or relation).
     */
    private String type;

    /**
     * ID from Overpass API.
     */
    private Long id;

    /**
     * Map of tags, that describe current node
     */
    private Map<String, String> tags;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "OverpassNode{" +
                "type='" + type + '\'' +
                ", id=" + id +
                ", tags=" + tags +
                '}';
    }
}
