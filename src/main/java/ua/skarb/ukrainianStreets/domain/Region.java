package ua.skarb.ukrainianStreets.domain;

import javax.persistence.*;

/**
 *  Class {@code Region} stores information about regions of Ukraine.
 *  Also class used as JPA entity.
 *  Before using {@code Region} class make sure that you create "REGIONS" table
 *  and "REGIONS_SEQ" sequence in you Oracle schema.
 *
 * @author Vynohradov Evgeniy
 * @see OverpassNode
 */

@Entity
@Table(name = "REGIONS")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "region_sequence")
    @SequenceGenerator(name = "region_sequence", sequenceName = "REGIONS_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "OVERPASS_ID")
    private Long overpassId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "OVERPASS_TYPE")
    private String overpassType;

    public Region(){}

    public Region(Long overpassId, String name, String overpassType) {
        this.overpassId = overpassId;
        this.name = name;
        this.overpassType = overpassType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOverpassId() {
        return overpassId;
    }

    public void setOverpassId(Long overpassId) {
        this.overpassId = overpassId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverpassType() {
        return overpassType;
    }

    public void setOverpassType(String overpassType) {
        this.overpassType = overpassType;
    }

    @Override
    public String toString() {
        return "Region{" +
                "id=" + id +
                ", overpassId=" + overpassId +
                ", name='" + name + '\'' +
                ", overpassType='" + overpassType + '\'' +
                '}';
    }
}
