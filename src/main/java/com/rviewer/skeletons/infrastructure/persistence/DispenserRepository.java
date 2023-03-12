package com.rviewer.skeletons.infrastructure.persistence;

import com.rviewer.skeletons.domain.entity.Dispenser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DispenserRepository extends JpaRepository<Dispenser, String> {

}
