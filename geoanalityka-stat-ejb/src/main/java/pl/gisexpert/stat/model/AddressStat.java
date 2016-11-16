package pl.gisexpert.stat.model;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.Immutable;
import pl.gisexpert.cms.model.analysis.demographic.PeopleByWorkingAgeSums;



@MappedSuperclass
@Immutable
@SqlResultSetMapping(name = "PeopleByWorkingAge",
        classes = @ConstructorResult(
                targetClass = PeopleByWorkingAgeSums.class,
                columns = {
                        @ColumnResult(name = "przedprod", type = Double.class),
                        @ColumnResult(name = "prod", type = Double.class),
                        @ColumnResult(name = "poprod", type = Double.class),
                        @ColumnResult(name = "przedprodk", type = Double.class),
                        @ColumnResult(name = "prodk", type = Double.class),
                        @ColumnResult(name = "poprodk", type = Double.class),
                        @ColumnResult(name = "przedprodm", type = Double.class),
                        @ColumnResult(name = "prodm", type = Double.class),
                        @ColumnResult(name = "poprodm", type = Double.class)
                }))

@NamedNativeQueries(value = {
       // @NamedNativeQuery(name = "AddressStat.PeopleByWorkingAgeSums", query = ,
       // @NamedNativeQuery(name = "AddressStat.PeopleByWorkingAgeSumsPolygon", query = , resultSetMapping = "PeopleByWorkingAge"),
        //@NamedNativeQuery(name = "AddressStat.SumAllInRadius", query = ),
        //@NamedNativeQuery(name = "AddressStat.SumAllPremisesInRadius", query = ,
        //@NamedNativeQuery(name = "AddressStat.SumAllInPolygon", query = ),
       // @NamedNativeQuery(name = "AddressStat.SumAllPremisesInPolygon", query = )
})
public class AddressStat implements Serializable {

    private static final long serialVersionUID = 1033705321916453635L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    protected Integer liczbaLokali;

    @Column(name = "liczbaosobzamwlokalach")
    protected Integer liczbaOsobZamWLokalach;

    @Column(name = "liczbakobietzamwlokalach")
    protected Integer liczbaKobietZamWLokalach;

    @Column(name = "liczbamezczyznzamwlokalach")
    protected Integer liczbaMezczyznZamWLokalach;

    @Column(name = "przedzialwiekuod0do4k")
    protected Integer przedzialWiekuOd0Do4K;

    @Column(name = "przedzialwiekuod5do9k")
    protected Integer przedzialWiekuOd5Do9K;

    @Column(name = "przedzialwiekuod10do14k")
    protected Integer przedzialWiekuOd10Do14K;

    @Column(name = "przedzialwiekuod15do19k")
    protected Integer przedzialWiekuOd15Do19K;

    @Column(name = "przedzialwiekuod20do24k")
    protected Integer przedzialWiekuOd20Do24K;

    @Column(name = "przedzialwiekuod25do29k")
    protected Integer przedzialWiekuOd25Do29K;

    @Column(name = "przedzialwiekuod30do34k")
    protected Integer przedzialWiekuOd30Do34K;

    @Column(name = "przedzialwiekuod35do39k")
    protected Integer przedzialWiekuOd35Do39K;

    @Column(name = "przedzialwiekuod40do44k")
    protected Integer przedzialWiekuOd40Do44K;

    @Column(name = "przedzialwiekuod45do49k")
    protected Integer przedzialWiekuOd45Do49K;

    @Column(name = "przedzialwiekuod50do54k")
    protected Integer przedzialWiekuOd50Do54K;

    @Column(name = "przedzialwiekuod55do59k")
    protected Integer przedzialWiekuOd55Do59K;

    @Column(name = "przedzialwiekuod60do64k")
    protected Integer przedzialWiekuOd60Do64K;

    @Column(name = "przedzialwiekuod65do69k")
    protected Integer przedzialWiekuOd65Do69K;

    @Column(name = "przedzialwiekuod70do74k")
    protected Integer przedzialWiekuOd70Do74K;

    @Column(name = "przedzialwiekuod75k")
    protected Integer przedzialWiekuOd75K;

    @Column(name = "przedzialwiekuod0do4m")
    protected Integer przedzialWiekuOd0Do4M;

    @Column(name = "przedzialwiekuod5do9m")
    protected Integer przedzialWiekuOd5Do9M;

    @Column(name = "przedzialwiekuod10do14m")
    protected Integer przedzialWiekuOd10Do14M;

    @Column(name = "przedzialwiekuod15do19m")

    protected Integer przedzialWiekuOd15Do19M;

    @Column(name = "przedzialwiekuod20do24m")
    protected Integer przedzialWiekuOd20Do24M;

    @Column(name = "przedzialwiekuod25do29m")
    protected Integer przedzialWiekuOd25Do29M;

    @Column(name = "przedzialwiekuod30do34m")
    protected Integer przedzialWiekuOd30Do34M;

    @Column(name = "przedzialwiekuod35do39m")
    protected Integer przedzialWiekuOd35Do39M;

    @Column(name = "przedzialwiekuod40do44m")
    protected Integer przedzialWiekuOd40Do44M;

    @Column(name = "przedzialwiekuod45do49m")
    protected Integer przedzialWiekuOd45Do49M;

    @Column(name = "przedzialwiekuod50do54m")
    protected Integer przedzialWiekuOd50Do54M;

    @Column(name = "przedzialwiekuod55do59m")
    protected Integer przedzialWiekuOd55Do59M;

    @Column(name = "przedzialwiekuod60do64m")
    protected Integer przedzialWiekuOd60Do64M;

    @Column(name = "przedzialwiekuod65do69m")
    protected Integer przedzialWiekuOd65Do69M;

    @Column(name = "przedzialwiekuod70do74m")
    protected Integer przedzialWiekuOd70Do74M;

    @Column(name = "przedzialwiekuod75m")
    protected Integer przedzialWiekuOd75M;

    @Column(name = "geom")
    protected byte[] geometry;

    @Column(name = "przedprod")
    protected double przedprod;

    @Column(name = "prod")
    protected double prod;

    @Column(name = "poprod")
    protected double poprod;
    
    @Column(name = "przedprodk")
    protected double przedprodk;
    
    @Column(name = "prodk")
    protected double prodk;
    
    @Column(name = "poprodk")
    protected double poprodk;
    
    @Column(name = "przedprodm")
    protected double przedprodm;
    
    @Column(name = "prodm")
    protected double prodm;
    
    @Column(name = "poprodm")
    protected double poprodm;

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

    public void setPrzedprod(double przedprod) {
        this.przedprod = przedprod;
    }

    public void setProd(double prod) {
        this.prod = prod;
    }

    public void setPoprod(double poprod) {
        this.poprod = poprod;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public double getPrzedprod() {
        return przedprod;
    }

    public double getProd() {
        return prod;
    }

    public double getPoprod() {
        return poprod;
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

    public double getPrzedprodk() {
		return przedprodk;
	}


	public void setPrzedprodk(double przedprodk) {
		this.przedprodk = przedprodk;
	}


	public double getProdk() {
		return prodk;
	}


	public void setProdk(double prodk) {
		this.prodk = prodk;
	}


	public double getPoprodk() {
		return poprodk;
	}


	public void setPoprodk(double poprodk) {
		this.poprodk = poprodk;
	}


	public double getProdm() {
		return prodm;
	}


	public void setProdm(double prodm) {
		this.prodm = prodm;
	}


	public double getPoprodm() {
		return poprodm;
	}


	public void setPoprodm(double poprodm) {
		this.poprodm = poprodm;
	}


	public byte[] getGeometry() {
        return geometry;
    }


}
