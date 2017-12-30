package nju.zxl.signalevent.dao;

import nju.zxl.signalevent.domain.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryDao extends JpaRepository<History,Integer>{
    @Query(value = "select h from History h where h.id < :id")
    public List<History> findAllWhereIdSmallerThan(@Param("id") int id);
}
