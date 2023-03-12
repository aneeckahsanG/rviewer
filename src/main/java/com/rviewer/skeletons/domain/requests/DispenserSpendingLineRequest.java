package com.rviewer.skeletons.domain.requests;

import com.rviewer.skeletons.utils.enums.Status;
import lombok.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DispenserSpendingLineRequest {
    @NotNull(message = "status is mandatory")
    private Status status;
    @NotNull(message = "update time is mandatory")
    private LocalDateTime updatedAt;
}
