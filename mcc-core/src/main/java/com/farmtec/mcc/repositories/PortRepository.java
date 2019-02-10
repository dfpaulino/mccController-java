package com.farmtec.mcc.repositories;

import com.farmtec.mcc.models.modules.IO.PORTn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortRepository extends JpaRepository<PORTn,Integer> {
}
