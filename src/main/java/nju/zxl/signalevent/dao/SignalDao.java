package nju.zxl.signalevent.dao;

import nju.zxl.signalevent.domain.Signal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SignalDao extends JpaRepository<Signal,Integer>{
    public Signal findByXh(int xh);

    @Query(value = "select s from Signal s where s.jg_bh = :jg_bh and s.sb_bh = :sb_bh and s.info_bh = :info_bh and s.act_bh = :act_bh")
    public Signal findByInfo(@Param("jg_bh") int jg_bh, @Param("sb_bh")int sb_bh,@Param("info_bh") int info_bh, @Param("act_bh")int act_bh);
}
