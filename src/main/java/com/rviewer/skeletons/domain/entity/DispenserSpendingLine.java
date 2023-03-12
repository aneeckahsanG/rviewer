package com.rviewer.skeletons.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table
@JsonIgnoreProperties(ignoreUnknown = true,
        value = {"dispenser"})
@ToString(exclude = {"dispenser"})
public class DispenserSpendingLine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "opened_at")
    private LocalDateTime openedAt;
    @Column(name = "closed_at")
    private LocalDateTime closedAt;
    @Column(name = "flow_volume")
    private Double flowVolume;
    @Column(name = "total_spent")
    private BigDecimal totalSpent;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dispenser_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Dispenser dispenser;
}