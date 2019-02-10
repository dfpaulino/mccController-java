package com.farmtec.mcc.repositories;


import com.farmtec.mcc.models.modules.timer.Timer;
import com.farmtec.mcc.models.modules.timer.TimerMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface TimerRepository  extends JpaRepository<Timer,Integer>, JpaSpecificationExecutor<Timer> {

    @Modifying(clearAutomatically = true)
    @Query("update Timer t set t.outPutCompareRegister=:value where t.id=:id" )
    void updateTimerValueById(@Param("id") int id, @Param("value") byte outPutCompareRegister);

    @Modifying(clearAutomatically = true)
    @Query("update Timer t set t.mode=:mode where t.id=:id" )
    void updateTimerModeById(@Param("id") int id, @Param("mode") TimerMode mode);


}
