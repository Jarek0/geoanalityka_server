package pl.gisexpert.cms.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "access_tokens", indexes = {@Index(name="token_index", columnList="token", unique=true)})

@lombok.Getter
@lombok.Setter
@lombok.EqualsAndHashCode(of = {"token"})
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.ToString
public class AccessToken implements Serializable {

    private static final long serialVersionUID = -1658846400249396866L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 36)
    @NotNull
    private String token;

    @Column(nullable = false)
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date expires;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "account_tokens",
    joinColumns = {
        @JoinColumn(name = "token_id", referencedColumnName = "id")},
    inverseJoinColumns = {
        @JoinColumn(name = "account_id", referencedColumnName = "id")})
    private Account account;
}
