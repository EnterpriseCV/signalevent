package nju.zxl.signalevent.dao;

import nju.zxl.signalevent.domain.Signal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SignalDao extends JpaRepository<Signal,Integer>{
    public Signal findBySid(int sid);

    @Query(value = "select s from Signal s where s.area_id = :area_id and s.equip_id = :equip_id and s.info_id = :info_id and s.act_id = :act_id")
    public Signal findByInfo(@Param("area_id") int area_id, @Param("equip_id")int equip_id,@Param("info_id") int info_id, @Param("act_id")int act_id);
}
