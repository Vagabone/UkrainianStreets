package ua.skarb.ukrainianStreets.domain;

import javax.persistence.*;

/**
 *  Class {@code District} stores information about districts of region.
 *  Also class used as JPA entity.
 *  Before using {@code District} class make sure that you create "DISTRICTS" table
 *  and "DISTRICTS_SEQ" sequence in you Oracle schema.
 *
 * @author Vynohradov Evgeniy
 * @see OverpassNode
 * @see Region
 */
@Entity
@Table(name = "DISTRICTS")
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "district_sequence")
    @SequenceGenerator(name = "district_sequence", sequenceName = "DISTRICTS_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "OVERPASS_ID")
    private Long overpassId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "REGION_ID")
    private Region region;

    @Column(name = "NAME")
    private String name;

    @Column(name = "OVERPASS_TYPE")
    private String overpassType;

    public District(){}

    public District(Long overpassId, String name, String overpassType) {
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

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
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
        return "District{" +
                "id=" + id +
                ", overpassId=" + overpassId +
                ", region=" + region +
                ", name='" + name + '\'' +
                ", overpassType='" + overpassType + '\'' +
                '}';
    }
}
