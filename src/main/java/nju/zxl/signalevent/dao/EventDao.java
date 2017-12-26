package nju.zxl.signalevent.dao;

import nju.zxl.signalevent.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventDao extends JpaRepository<Event,Integer>{
    public Event findById(int id);
}
