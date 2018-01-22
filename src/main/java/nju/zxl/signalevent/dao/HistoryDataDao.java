package nju.zxl.signalevent.dao;

import nju.zxl.signalevent.domain.HistoryData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryDataDao extends JpaRepository<HistoryData,Integer>{
    @Query(value = "select h from HistoryData h where h.id < :id")
    public List<HistoryData> findAllWhereIdSmallerThan(@Param("id") int id);
}
