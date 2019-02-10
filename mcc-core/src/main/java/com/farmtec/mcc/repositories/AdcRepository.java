package com.farmtec.mcc.repositories;

import com.farmtec.mcc.models.modules.adc.ADC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdcRepository extends JpaRepository<ADC,Integer> {
}
