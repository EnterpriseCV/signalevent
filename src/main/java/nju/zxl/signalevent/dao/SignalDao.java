package nju.zxl.signalevent.dao;

import nju.zxl.signalevent.domain.Signal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignalDao extends JpaRepository<Signal,Integer>{
    Signal findByXh(int xh);
}
