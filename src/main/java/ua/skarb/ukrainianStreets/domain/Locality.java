package ua.skarb.ukrainianStreets.domain;

import javax.persistence.*;

/**
 *  Class {@code Locality} stores information about localities (towns, cities or villages) of district.
 *  Some localities in Ukraine has role of admin centers, in this case this localities doesn't belong to district,
 *  so {@param district} can be null.
 *  Also class used as JPA entity.
 *  Before using {@code Locality} class make sure that you create "LOCALITIES" table
 *  and "LOCALITIES_SEQ" sequence in you Oracle schema.
 *
 * @author Vynohradov Evgeniy
 * @see OverpassNode
 * @see Region
 * @see District
 */
@Entity
@Table(name = "LOCALITIES")
public class Locality {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "localities_sequence")
    @SequenceGenerator(name = "localities_sequence", sequenceName = "LOCALITIES_SEQ", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "REGION_ID")
    private Region region;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DISTRICT_ID")
    private District district;

    @Column(name = "OVERPASS_ID")
    private Long overpassId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "LOCALITY_TYPE")
    private String localityType;

    @Column(name = "OVERPASS_TYPE")
    private String overpassType;

    public Locality() {
    }

    public Locality(Long overpassId, String name, String localityType, String overpassType) {
        this.overpassId = overpassId;
        this.name = name;
        this.localityType = localityType;
        this.overpassType = overpassType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
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

    public String getLocalityType() {
        return localityType;
    }

    public void setLocalityType(String localityType) {
        this.localityType = localityType;
    }

    public String getOverpassType() {
        return overpassType;
    }

    public void setOverpassType(String overpassType) {
        this.overpassType = overpassType;
    }

    @Override
    public String toString() {
        return "Locality{" +
                "id=" + id +
                ", region=" + region +
                ", district=" + district +
                ", overpassId=" + overpassId +
                ", name='" + name + '\'' +
                ", localityType='" + localityType + '\'' +
                ", overpassType='" + overpassType + '\'' +
                '}';
    }
}
