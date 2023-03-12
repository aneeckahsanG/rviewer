package com.rviewer.skeletons.domain.requests;

import com.rviewer.skeletons.utils.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DispenserRequest {
    @Builder.Default
    private String id = UUID.randomUUID().toString();
    @NotNull(message = "flow_volume is mandatory")
    private Double flowVolume;
    @Builder.Default
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal rate = new BigDecimal(12.25);
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Status status = Status.CLOSE;
}
