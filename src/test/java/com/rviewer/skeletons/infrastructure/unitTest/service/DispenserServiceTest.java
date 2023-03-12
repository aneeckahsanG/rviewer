package com.rviewer.skeletons.infrastructure.unitTest.service;

import com.rviewer.skeletons.domain.entity.Dispenser;
import com.rviewer.skeletons.domain.entity.DispenserSpendingLine;
import com.rviewer.skeletons.domain.requests.DispenserRequest;
import com.rviewer.skeletons.domain.requests.DispenserSpendingLineRequest;
import com.rviewer.skeletons.domain.responses.DispenserResponse;
import com.rviewer.skeletons.domain.responses.DispenserSpendingLineResponse;
import com.rviewer.skeletons.domain.services.DispenserService;
import com.rviewer.skeletons.infrastructure.persistence.DispenserRepository;
import com.rviewer.skeletons.infrastructure.persistence.DispenserSpendingLineRepository;
import com.rviewer.skeletons.utils.enums.Status;
import com.rviewer.skeletons.utils.exception.DispenserDoesNotExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class DispenserServiceTest {
    @Mock
    DispenserRepository dispenserRepository;
    @Mock
    DispenserSpendingLineRepository dispenserSpendingLineRepository;
    @Spy
    ModelMapper modelMapper;
    @InjectMocks
    DispenserService dispenserService;
    DispenserRequest dispenserRequest;
    Dispenser dispenser;
    DispenserSpendingLine dispenserSpendingLine;
    DispenserSpendingLineRequest dispenserSpendingLineRequest;
    String dispenserId = "38e0e4d4-a480-44da-8910-c43ea560e2401";
    @BeforeEach
    public void setUp() {
        dispenserRequest = DispenserRequest.builder().id("38e0e4d4-a480-44da-8910-c43ea560e240").flowVolume(0.64).rate(new BigDecimal(12.25)).build();
        dispenserSpendingLineRequest = DispenserSpendingLineRequest.builder().status(Status.OPEN).updatedAt(LocalDateTime.parse("2022-01-01T02:40:00")).build();
        dispenser = modelMapper.map(dispenserRequest, Dispenser.class);
        dispenserSpendingLine = DispenserSpendingLine.builder()
                .id(1L)
                .openedAt(LocalDateTime.parse("2022-01-01T02:40:00"))
                .flowVolume(0.64)
                .totalSpent(new BigDecimal(2352))
                .build();
    }
    @Test
    public void givenDispenserObject_whenSaveDispenser_thenReturnDispenserObject() {
        when(dispenserRepository.save(dispenser)).thenReturn(dispenser);
        DispenserResponse dispenserResponse = dispenserService.save(dispenserRequest);
        assertThat(dispenserResponse).isNotNull();
    }
    @Test
    public void given_dispenserLine_saveDispenserLine_return_Ok() {
        when(dispenserRepository.findById(dispenserId)).thenReturn(Optional.ofNullable(dispenser));
        when(dispenserSpendingLineRepository.save(any(DispenserSpendingLine.class))).thenReturn(dispenserSpendingLine);
        DispenserSpendingLineResponse.DispenserLineUsage _dispenserLineUsage = dispenserService.saveDispenserSpendingLine(dispenserId, dispenserSpendingLineRequest);
        assertThat(_dispenserLineUsage).isNotNull();
        assertThat(_dispenserLineUsage.getOpenedAt().equals(LocalDateTime.parse("2022-01-01T02:40:00")));
    }
    @Test
    public void given_invalidValueForDispenserId_saveDispenserLine_ThrowsException() {
        when(dispenserRepository.findById(dispenserId)).thenReturn(Optional.ofNullable(null));
        org.junit.jupiter.api.Assertions.assertThrows(DispenserDoesNotExistException.class, () -> {
            dispenserService.saveDispenserSpendingLine(dispenserId, dispenserSpendingLineRequest);
        });
    }
    @Test
    public void given_dispenserId_getSpending(){
        DispenserSpendingLine dispenserLine1 = DispenserSpendingLine.builder()
                .openedAt(LocalDateTime.parse("2022-01-01T02:40:00"))
                .flowVolume(0.64)
                .totalSpent(new BigDecimal(2352))
                .build();
        dispenser.setDispenserSpendingLines(Arrays.asList(dispenserSpendingLine, dispenserLine1).stream().collect(Collectors.toSet()));
        when(dispenserRepository.findById(dispenserId)).thenReturn(Optional.ofNullable(dispenser));
        DispenserSpendingLineResponse dispenserSpendingLineResponse = dispenserService.findAllSpendingLineByDispenserId(dispenserId);
        assertThat(dispenserSpendingLineResponse).isNotNull();
        assertThat(dispenserSpendingLineResponse.getUsages().size()).isEqualTo(2);
    }
    @Test
    public void given_invalidValueForDispenserId_getSpending_ThrowsException() {
        when(dispenserRepository.findById(dispenserId)).thenReturn(Optional.ofNullable(null));
        org.junit.jupiter.api.Assertions.assertThrows(DispenserDoesNotExistException.class, () -> {
            dispenserService.findById(dispenserId);
        });
    }
}
