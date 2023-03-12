package com.rviewer.skeletons.domain.services;

import com.rviewer.skeletons.domain.entity.DispenserSpendingLine;
import com.rviewer.skeletons.domain.requests.DispenserSpendingLineRequest;
import com.rviewer.skeletons.domain.requests.DispenserRequest;
import com.rviewer.skeletons.domain.responses.DispenserSpendingLineResponse;
import com.rviewer.skeletons.domain.responses.DispenserResponse;
import com.rviewer.skeletons.domain.entity.Dispenser;
import com.rviewer.skeletons.infrastructure.persistence.DispenserRepository;
import com.rviewer.skeletons.infrastructure.persistence.DispenserSpendingLineRepository;
import com.rviewer.skeletons.utils.enums.Status;
import com.rviewer.skeletons.utils.exception.BadRequestException;
import com.rviewer.skeletons.utils.exception.DispenserDoesNotExistException;
import com.rviewer.skeletons.utils.exception.DispenserSameStatusException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DispenserService {
    @Autowired
    DispenserRepository dispenserRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    DispenserSpendingLineRepository dispenserSpendingLineRepository;

    public Dispenser findById(String id){
        return dispenserRepository.findById(id).orElseThrow(() -> new DispenserDoesNotExistException());

    }
    public DispenserResponse save(DispenserRequest dispenserRequest) {
        Dispenser _dispenser = dispenserRepository.save(modelMapper.map(dispenserRequest, Dispenser.class));
        return modelMapper.map(_dispenser, DispenserResponse.class);
    }
    public DispenserSpendingLineResponse findAllSpendingLineByDispenserId(String id){
        Dispenser dispenser = dispenserRepository.findById(id).orElseThrow(()->new DispenserDoesNotExistException());
        List<com.rviewer.skeletons.domain.entity.DispenserSpendingLine> dispenserSpendingLines = dispenser.getDispenserSpendingLines().stream().toList();
        BigDecimal amount = dispenserSpendingLines.stream().map(d -> {
            if(d.getClosedAt() == null){
                d.setTotalSpent(BigDecimal.valueOf(Duration.between(d.getOpenedAt(), LocalDateTime.now()).toSeconds() * d.getFlowVolume()).multiply(dispenser.getRate()));
            }
            return d.getTotalSpent();
        }).reduce(new BigDecimal(0), (i, j) -> i.add(j));
        DispenserSpendingLineResponse dispenserSpendingLineResponse = DispenserSpendingLineResponse.builder().amount(amount).usages(dispenserSpendingLines.stream().map(usage -> modelMapper.map(usage, DispenserSpendingLineResponse.DispenserLineUsage.class)).collect(Collectors.toList())).build();
        return dispenserSpendingLineResponse;
    }
    @Transactional
    public DispenserSpendingLineResponse.DispenserLineUsage saveDispenserSpendingLine(String id, DispenserSpendingLineRequest dispenserSpendingLineRequest) {
        Dispenser dispenser = dispenserRepository.findById(id).orElseThrow(()->new DispenserDoesNotExistException());
        if(dispenserSpendingLineRequest.getUpdatedAt().isAfter(LocalDateTime.now())) throw new BadRequestException();
        if(dispenserSpendingLineRequest.getStatus().equals(dispenser.getStatus())){
            throw new DispenserSameStatusException();
        }
        DispenserSpendingLine dispenserSpendingLine = new DispenserSpendingLine();
        if(dispenserSpendingLineRequest.getStatus().equals(Status.OPEN)){
            dispenserSpendingLine.setOpenedAt(dispenserSpendingLineRequest.getUpdatedAt());
            dispenserSpendingLine.setDispenser(dispenser);
            dispenserSpendingLine.setFlowVolume(dispenser.getFlowVolume());
            dispenser.setStatus(Status.OPEN);
        }
        else {
            dispenserSpendingLine = dispenserSpendingLineRepository.findFirstByDispenserIdOrderByIdDesc(id).orElseThrow(() -> new RuntimeException());
            if(dispenserSpendingLine.getOpenedAt().isAfter(dispenserSpendingLineRequest.getUpdatedAt())) throw new BadRequestException();
            dispenserSpendingLine.setClosedAt(dispenserSpendingLineRequest.getUpdatedAt());
            dispenserSpendingLine.setTotalSpent(BigDecimal.valueOf(Duration.between(dispenserSpendingLine.getOpenedAt(), dispenserSpendingLine.getClosedAt()).toSeconds() * dispenserSpendingLine.getFlowVolume()).multiply(dispenser.getRate()));
            dispenser.setStatus(Status.CLOSE);
        }
        return modelMapper.map(dispenserSpendingLineRepository.save(dispenserSpendingLine), DispenserSpendingLineResponse.DispenserLineUsage.class);
    }
}
