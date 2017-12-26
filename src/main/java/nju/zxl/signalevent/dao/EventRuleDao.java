package nju.zxl.signalevent.dao;

import nju.zxl.signalevent.domain.EventRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRuleDao extends JpaRepository<EventRule,Integer>{
}
