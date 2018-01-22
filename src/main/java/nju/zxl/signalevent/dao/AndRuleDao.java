package nju.zxl.signalevent.dao;

import nju.zxl.signalevent.domain.AndRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AndRuleDao extends JpaRepository<AndRule,Integer>{

}
