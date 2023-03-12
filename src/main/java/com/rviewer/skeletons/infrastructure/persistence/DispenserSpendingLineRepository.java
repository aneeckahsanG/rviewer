package com.rviewer.skeletons.infrastructure.persistence;

import com.rviewer.skeletons.domain.entity.DispenserSpendingLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DispenserSpendingLineRepository extends JpaRepository<DispenserSpendingLine, Long> {
    List<DispenserSpendingLine> findAllByDispenserId(String id);
    Optional<DispenserSpendingLine> findFirstByDispenserIdOrderByIdDesc(String id);
}
