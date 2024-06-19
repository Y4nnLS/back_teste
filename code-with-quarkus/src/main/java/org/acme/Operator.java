package org.acme;

// import java.util.HashSet;
// import java.util.Set;

import org.hibernate.envers.Audited;
// import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.Length;

// import com.fasterxml.jackson.annotation.JsonManagedReference;

// import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.smallrye.common.constraint.NotNull;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
// import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
// import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "operator")
@Audited
@Getter
@Setter
@Cacheable
public class Operator extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operator_id")
    private Integer id;

    @Column(name = "name")
    @NotNull
    @Length(max = 150)
    private String name;

    @Column(name = "email")
    @NotNull
    @Length(max = 150)
    @Email
    private String email;

    @Column(name = "email_active")
    @Enumerated(EnumType.ORDINAL)
    private StatusActive emailActive;

    @Column(name = "telephone")
    @NotNull
    @Length(max = 15)
    private String telephone;

    @Column(name = "sms_active")
    @Enumerated(EnumType.ORDINAL)
    private StatusActive smsActive;

    @Column(name = "tts_active")
    @Enumerated(EnumType.ORDINAL)
    private StatusActive ttsActive;

    // @OneToMany(fetch = FetchType.LAZY, mappedBy = "operator")
    // @NotAudited
    // @JsonManagedReference
    // private Set<TeamOperator> teamOperators = new HashSet<TeamOperator>();
    
    // Construtor vazio é necessário para JPA

    @Override
    public String toString() {
        return "Operator{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", emailActive=" + emailActive +
                ", telephone='" + telephone + '\'' +
                ", smsActive=" + smsActive +
                ", ttsActive=" + ttsActive +
                '}';
    }
    public enum StatusActive {
        INACTIVE,
        ACTIVE
    }
}
