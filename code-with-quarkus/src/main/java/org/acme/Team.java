package org.acme;

import java.util.List;

import org.hibernate.envers.Audited;
// import org.hibernate.envers.NotAudited;
// import org.hibernate.mapping.Set;
import org.hibernate.validator.constraints.Length;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
// import io.quarkus.panache.common.impl.GenerateBridge;
import io.smallrye.common.constraint.NotNull;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
// import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
// import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


// @EqualsAndHashCode(of = "id")
@Entity
@Table(name = "team", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@Audited
@Getter
@Setter
@NoArgsConstructor
@ToString(of = "id")
@Cacheable
public class Team  extends PanacheEntityBase{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Integer id;

    @Column(name = "name")
    @NotNull
    @Length(max = 150)
    private String name;

    @ManyToMany
    @JoinTable(name = "team_operator", joinColumns = @JoinColumn(name = "team_id"), inverseJoinColumns = @JoinColumn(name = "operator_id"))
    private List<Operator> operators;

    // @OneToMany(fetch = FetchType.LAZY, mappedBy = "team")
    // @NotAudited
    // private Set<CommunicationConfig> communicationConfig = new HashSet<CommunicationConfig>(0);
    
    // @OneToMany(fetch = FetchType.LAZY, mappedBy = "team")
    // @NotAudited
    // private Set<PointConfig> pointConfigs = new HashSet<PointConfig>(0);

    
}
