package ua.skarb.ukrainianStreets.domain;

import javax.persistence.*;

/**
 *  Class {@code Street} stores information about streets of locality(town, city or village).
 *  Also class used as JPA entity.
 *  Before using {@code Street} class make sure that you create "STREETS" table
 *  and "STREETS_SEQ" sequence in you Oracle schema.
 *
 * @author Vynohradov Evgeniy
 * @see OverpassNode
 * @see Locality
 */
@Entity
@Table(name = "STREETS")
public class Street {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "street_sequence")
    @SequenceGenerator(name = "street_sequence", sequenceName = "STREETS_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "OVERPASS_ID")
    private Long overpassId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "LOCALITY_ID")
    private Locality locality;

    @Column(name = "NAME")
    private String name;

    @Column(name = "OVERPASS_TYPE")
    private String overpassType;

    public Street() {
    }

    public Street(Long overpassId, String name, String overpassType) {
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

    public Locality getLocality() {
        return locality;
    }

    public void setLocality(Locality locality) {
        this.locality = locality;
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
        return "Street{" +
                "id=" + id +
                ", overpassId=" + overpassId +
                ", locality=" + locality +
                ", name='" + name + '\'' +
                ", overpassType='" + overpassType + '\'' +
                '}';
    }
}
