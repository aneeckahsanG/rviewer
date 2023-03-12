package com.rviewer.skeletons.infrastructure.unitTest.persistence;

import com.rviewer.skeletons.domain.entity.Dispenser;
import com.rviewer.skeletons.domain.entity.DispenserSpendingLine;
import com.rviewer.skeletons.infrastructure.persistence.DispenserRepository;
import com.rviewer.skeletons.infrastructure.persistence.DispenserSpendingLineRepository;
import com.rviewer.skeletons.utils.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class DispenserRepositoryTest {
    @Autowired
    DispenserRepository dispenserRepository;
    @Autowired
    DispenserSpendingLineRepository dispenserSpendingLineRepository;
    Dispenser dispenser;
    DispenserSpendingLine dispenserSpendingLine;
    String dispenserId = "38e0e4d4-a480-44da-8910-c43ea560e240";
    @BeforeEach
    public void setUp() {
        dispenser = Dispenser.builder().id(dispenserId).flowVolume(0.64).rate(new BigDecimal(12.25)).status(Status.CLOSE).build();
        dispenserSpendingLine = DispenserSpendingLine.builder()
                .id(1L)
                .openedAt(LocalDateTime.parse("2022-01-01T02:40:00"))
                .flowVolume(0.64)
                .totalSpent(new BigDecimal(2352))
                .build();
    }
    @Test
    void givenDispenserObject_whenSaveDispenser_thenReturnDispenserObject() {
        Dispenser _dispenser = dispenserRepository.save(dispenser);
        assertThat(_dispenser).isNotNull();
    }
    @Test
    public void given_dispenserLine_saveDispenserLine_return_Ok() {
        Dispenser _dispenser = dispenserRepository.save(dispenser);
        dispenserSpendingLine.setDispenser(_dispenser);
        DispenserSpendingLine _dispenserSpendingLine = dispenserSpendingLineRepository.save(dispenserSpendingLine);
        assertThat(_dispenserSpendingLine).isNotNull();
        assertThat(_dispenserSpendingLine.getDispenser().getId()).isEqualTo(dispenserId);
    }
    @Test
    public void given_dispenserId_getSpending() {
        Dispenser _dispenser = Dispenser.builder().id(dispenserId).flowVolume(0.64).rate(new BigDecimal(12.25)).status(Status.CLOSE).build();
        DispenserSpendingLine dispenserSpendingLine1 = DispenserSpendingLine.builder()
                .id(1L)
                .openedAt(LocalDateTime.parse("2022-01-01T02:40:00"))
                .flowVolume(0.64)
                .totalSpent(new BigDecimal(2352))
                .build();
        DispenserSpendingLine dispenserSpendingLine2 = DispenserSpendingLine.builder()
                .id(2L)
                .openedAt(LocalDateTime.parse("2022-01-01T02:40:00"))
                .flowVolume(0.64)
                .totalSpent(new BigDecimal(2352))
                .build();
        _dispenser.setDispenserSpendingLines(new HashSet<>());
        _dispenser.getDispenserSpendingLines().add(dispenserSpendingLine1);
        dispenserRepository.save(_dispenser);
        Optional<Dispenser> _dispenser1 = dispenserRepository.findById(dispenserId);
        assertThat(_dispenser1.get().getDispenserSpendingLines().size()).isEqualTo(1);
    }
}
