package com.rviewer.skeletons.domain.entity;

import com.rviewer.skeletons.utils.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "dispenser")
public class Dispenser {
    @Id
    @Column(name = "id", nullable = false)
    private String id;
    @Column(name = "flow_volume", nullable = false)
    private Double flowVolume;
    @Column(name = "rate", nullable = false)
    private BigDecimal rate;
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private Status status;
    @OneToMany(mappedBy = "dispenser", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<DispenserSpendingLine> dispenserSpendingLines = new HashSet<>();

}
