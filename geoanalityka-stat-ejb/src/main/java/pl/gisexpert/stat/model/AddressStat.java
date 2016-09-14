package pl.gisexpert.stat.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "dane2015")
@NamedNativeQueries({
	@NamedNativeQuery(name="AddressStat.SumAllInRadius", query="SELECT sum(liczbaosobzamwlokalach)\\:\\:int FROM dane2015 "
			+ "WHERE ST_Within(geom,ST_Buffer(ST_Transform(ST_GeomFromText('POINT(' || :x || ' ' || :y || ')', :epsg), 3857), :radius))")
})
public class AddressStat implements Serializable {

    private static final long serialVersionUID = 1033705321916453635L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column
    protected Integer liczbaLokali;
    
    @Column(name="liczbaosobzamwlokalach")
    protected Integer liczbaOsobZamWLokalach;
    
    @Column(name="liczbakobietzamwlokalach")
    protected Integer liczbaKobietZamWLokalach;
    
    @Column(name="liczbamezczyznzamwlokalach")
    protected Integer liczbaMezczyznZamWLokalach;
    
    @Column(name="przedzialwiekuod0do4k")
    protected Integer przedzialWiekuOd0Do4K;
    
    @Column(name="przedzialwiekuod5do9k")
    protected Integer przedzialWiekuOd5Do9K;
    
    @Column(name="przedzialwiekuod10do14k")
    protected Integer przedzialWiekuOd10Do14K;
    
    @Column(name="przedzialwiekuod15do19k")
    protected Integer przedzialWiekuOd15Do19K;
    
    @Column(name="przedzialwiekuod20do24k")
    protected Integer przedzialWiekuOd20Do24K;
    
    @Column(name="przedzialwiekuod25do29k")
    protected Integer przedzialWiekuOd25Do29K;
    
    @Column(name="przedzialwiekuod30do34k")
    protected Integer przedzialWiekuOd30Do34K;
    
    @Column(name="przedzialwiekuod35do39k")
    protected Integer przedzialWiekuOd35Do39K;
    
    @Column(name="przedzialwiekuod40do44k")
    protected Integer przedzialWiekuOd40Do44K;
    
    @Column(name="przedzialwiekuod45do49k")
    protected Integer przedzialWiekuOd45Do49K;
    
    @Column(name="przedzialwiekuod50do54k")
    protected Integer przedzialWiekuOd50Do54K;
    
    @Column(name="przedzialwiekuod55do59k")
    protected Integer przedzialWiekuOd55Do59K;
    
    @Column(name="przedzialwiekuod60do64k")
    protected Integer przedzialWiekuOd60Do64K;
    
    @Column(name="przedzialwiekuod65do69k")
    protected Integer przedzialWiekuOd65Do69K;
    
    @Column(name="przedzialwiekuod70do74k")
    protected Integer przedzialWiekuOd70Do74K;
    
    @Column(name="przedzialwiekuod75k")
    protected Integer przedzialWiekuOd75K;
    
    @Column(name="przedzialwiekuod0do4m")
    protected Integer przedzialWiekuOd0Do4M;
    
    @Column(name="przedzialwiekuod5do9m")
    protected Integer przedzialWiekuOd5Do9M;
    
    @Column(name="przedzialwiekuod10do14m")
    protected Integer przedzialWiekuOd10Do14M;
    
    @Column(name="przedzialwiekuod15do19m")
    protected Integer przedzialWiekuOd15Do19M;
    
    @Column(name="przedzialwiekuod20do24m")
    protected Integer przedzialWiekuOd20Do24M;
    
    @Column(name="przedzialwiekuod25do29m")
    protected Integer przedzialWiekuOd25Do29M;
    
    @Column(name="przedzialwiekuod30do34m")
    protected Integer przedzialWiekuOd30Do34M;
    
    @Column(name="przedzialwiekuod35do39m")
    protected Integer przedzialWiekuOd35Do39M;
    
    @Column(name="przedzialwiekuod40do44m")
    protected Integer przedzialWiekuOd40Do44M;
    
    @Column(name="przedzialwiekuod45do49m")
    protected Integer przedzialWiekuOd45Do49M;
    
    @Column(name="przedzialwiekuod50do54m")
    protected Integer przedzialWiekuOd50Do54M;
    
    @Column(name="przedzialwiekuod55do59m")
    protected Integer przedzialWiekuOd55Do59M;
    
    @Column(name="przedzialwiekuod60do64m")
    protected Integer przedzialWiekuOd60Do64M;
    
    @Column(name="przedzialwiekuod65do69m")
    protected Integer przedzialWiekuOd65Do69M;
    
    @Column(name="przedzialwiekuod70do74m")
    protected Integer przedzialWiekuOd70Do74M;
    
    @Column(name="przedzialwiekuod75m")
    protected Integer przedzialWiekuOd75M;
    
    @Column(name="geom")
    protected byte[] geometry;

    public Long getId() {
        return id;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AddressStat other = (AddressStat) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getLiczbaLokali() {
		return liczbaLokali;
	}

	public Integer getLiczbaOsobZamWLokalach() {
		return liczbaOsobZamWLokalach;
	}

	public Integer getLiczbaKobietZamWLokalach() {
		return liczbaKobietZamWLokalach;
	}

	public Integer getLiczbaMezczyznZamWLokalach() {
		return liczbaMezczyznZamWLokalach;
	}

	public Integer getPrzedzialWiekuOd0Do4K() {
		return przedzialWiekuOd0Do4K;
	}

	public Integer getPrzedzialWiekuOd5Do9K() {
		return przedzialWiekuOd5Do9K;
	}

	public Integer getPrzedzialWiekuOd10Do14K() {
		return przedzialWiekuOd10Do14K;
	}

	public Integer getPrzedzialWiekuOd15Do19K() {
		return przedzialWiekuOd15Do19K;
	}

	public Integer getPrzedzialWiekuOd20Do24K() {
		return przedzialWiekuOd20Do24K;
	}

	public Integer getPrzedzialWiekuOd25Do29K() {
		return przedzialWiekuOd25Do29K;
	}

	public Integer getPrzedzialWiekuOd30Do34K() {
		return przedzialWiekuOd30Do34K;
	}

	public Integer getPrzedzialWiekuOd35Do39K() {
		return przedzialWiekuOd35Do39K;
	}

	public Integer getPrzedzialWiekuOd40Do44K() {
		return przedzialWiekuOd40Do44K;
	}

	public Integer getPrzedzialWiekuOd45Do49K() {
		return przedzialWiekuOd45Do49K;
	}

	public Integer getPrzedzialWiekuOd50Do54K() {
		return przedzialWiekuOd50Do54K;
	}

	public Integer getPrzedzialWiekuOd55Do59K() {
		return przedzialWiekuOd55Do59K;
	}

	public Integer getPrzedzialWiekuOd60Do64K() {
		return przedzialWiekuOd60Do64K;
	}

	public Integer getPrzedzialWiekuOd65Do69K() {
		return przedzialWiekuOd65Do69K;
	}

	public Integer getPrzedzialWiekuOd70Do74K() {
		return przedzialWiekuOd70Do74K;
	}

	public Integer getPrzedzialWiekuOd75K() {
		return przedzialWiekuOd75K;
	}

	public Integer getPrzedzialWiekuOd0Do4M() {
		return przedzialWiekuOd0Do4M;
	}

	public Integer getPrzedzialWiekuOd5Do9M() {
		return przedzialWiekuOd5Do9M;
	}

	public Integer getPrzedzialWiekuOd10Do14M() {
		return przedzialWiekuOd10Do14M;
	}

	public Integer getPrzedzialWiekuOd15Do19M() {
		return przedzialWiekuOd15Do19M;
	}

	public Integer getPrzedzialWiekuOd20Do24M() {
		return przedzialWiekuOd20Do24M;
	}

	public Integer getPrzedzialWiekuOd25Do29M() {
		return przedzialWiekuOd25Do29M;
	}

	public Integer getPrzedzialWiekuOd30Do34M() {
		return przedzialWiekuOd30Do34M;
	}

	public Integer getPrzedzialWiekuOd35Do39M() {
		return przedzialWiekuOd35Do39M;
	}

	public Integer getPrzedzialWiekuOd40Do44M() {
		return przedzialWiekuOd40Do44M;
	}

	public Integer getPrzedzialWiekuOd45Do49M() {
		return przedzialWiekuOd45Do49M;
	}

	public Integer getPrzedzialWiekuOd50Do54M() {
		return przedzialWiekuOd50Do54M;
	}

	public Integer getPrzedzialWiekuOd55Do59M() {
		return przedzialWiekuOd55Do59M;
	}

	public Integer getPrzedzialWiekuOd60Do64M() {
		return przedzialWiekuOd60Do64M;
	}

	public Integer getPrzedzialWiekuOd65Do69M() {
		return przedzialWiekuOd65Do69M;
	}

	public Integer getPrzedzialWiekuOd70Do74M() {
		return przedzialWiekuOd70Do74M;
	}

	public Integer getPrzedzialWiekuOd75M() {
		return przedzialWiekuOd75M;
	}

	public byte[] getGeometry() {
		return geometry;
	}


}
