package com.rviewer.skeletons.domain.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DispenserResponse {
    private String id;
    private Double flowVolume;
    private BigDecimal rate;
}
