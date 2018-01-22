package nju.zxl.signalevent.dao;

import nju.zxl.signalevent.domain.EventRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRuleDao extends JpaRepository<EventRule,Integer>{
    public EventRule findById(int id);
    public List<EventRule> findByEventid(int eventid);
    public List<EventRule> findByType(int type);
}
