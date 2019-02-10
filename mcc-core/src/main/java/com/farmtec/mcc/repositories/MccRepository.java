package com.farmtec.mcc.repositories;

import com.farmtec.mcc.models.Atmega;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Cacheable;
import java.util.List;
@Repository
public interface MccRepository extends JpaRepository<Atmega,Integer>{
    Atmega findByAddress(String addr);

}