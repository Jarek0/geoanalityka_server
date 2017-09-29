package pl.gisexpert.cms.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.envers.AuditJoinTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity

@lombok.Getter
@lombok.Setter
@lombok.EqualsAndHashCode
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.ToString(exclude = "account")
public class Address implements Serializable {

    private static final long serialVersionUID = -6442068216931841076L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 6)
    @NotNull
    @Size(max = 6, min = 6)
    private String zipcode;

    @Column(nullable = false, length = 30)
    @NotNull
    @Size(min = 3, max = 30)
    private String city;

    @Column(length = 30)
    @Size(min = 3, max = 30)
    private String street;

    @Column(name = "house_number", nullable = false, length = 5)
    @NotNull
    private String houseNumber;

    @Column(name = "flat_number", length = 5)
    private String flatNumber;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "user_id")
    private Account account;

    public Address(String zipcode, String city, String street, String houseNumber, String flatNumber) {
        this.zipcode = zipcode;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.flatNumber = flatNumber;
    }
}
