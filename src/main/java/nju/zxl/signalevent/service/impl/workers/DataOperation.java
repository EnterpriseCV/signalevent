package nju.zxl.signalevent.service.impl.workers;

import nju.zxl.signalevent.bean.Rule;
import nju.zxl.signalevent.model.*;
import nju.zxl.signalevent.util.DaoUtils;

import java.util.List;


public class DataOperation {
    DaoUtils daoUtils;

    public DataOperation(){
        daoUtils = new DaoUtils();
    }

    public int getSidbySignalinfo(String s){
        String[] a = s.split(",");
        int sid = -1;
        String sql = "select sid from `signal` where area_id =? and equip_id=? and info_id=? and act_id=?";
        if(daoUtils.getForValue(sql, a[0], a[1], a[2], a[3])==null)
            sid = 0;
        else
            sid = daoUtils.getForValue(sql, a[0], a[1], a[2], a[3]);
        return sid;
    }

    public void insertValue(int orid,String value){
        String sql = "update or_rule set signal_value =? where orid =?";
        daoUtils.update(sql, value,orid);
    }

    public int getAndruleAmount(){
        int amount = 0;
        String sql = "select count(*) from `and_rule`";
        amount = Integer.parseInt(String.valueOf(daoUtils.getForValue(sql).toString()));
        return amount;
    }

    public List<OrRule> getOrrulelistbyArid(int arid){
        String sql = "select * from or_rule where arid=?";
        List<OrRule> list = daoUtils.getForList(OrRule.class,sql,arid);
        //System.out.println(list.size());
        return list;
    }

    public List<OrRule> getOrrulelistbyOrid(int orid){
        String sql = "select * from or_rule where orid=?";
        List<OrRule> list = daoUtils.getForList(OrRule.class,sql,orid);
        //System.out.println(list.size());
        return list;
    }

    public List<OrRule> getOrrulelistbyEid(int eid){
        String sql = "select * from or_rule where eid=?";
        List<OrRule> list = daoUtils.getForList(OrRule.class,sql,eid);
        //System.out.println(list.size());
        return list;

    }

    public List<OrRule> getOrrulelist(){
        String sql = "select * from or_rule";
        List<OrRule> list = daoUtils.getForList(OrRule.class,sql);
        //System.out.println(list.size());
        return list;

    }

    public List<AndRule> getAndrulelist(){
        String sql = "select * from and_rule";
        List<AndRule> list = daoUtils.getForList(AndRule.class,sql);
        //System.out.println(list.size());
        return list;

    }

    public int getEventamount() {
        String sql = "select count(distinct eid) from `or_rule`";
        int res = Integer.parseInt((daoUtils.getForValue(sql).toString()));
        //System.out.println(res);
        return res;
    }

    public List<TriggerSignal> getTriggerSiganls() {
        String sql = "select * from `trigger_signal`";
        List<TriggerSignal>	list = daoUtils.getForList(TriggerSignal.class, sql);
        //System.out.println(list.size());
        return list;
    }

    public List<TriggerSet> getTriggerSet() {
        String sql = "select * from `trigger_set`";
        List<TriggerSet> list = daoUtils.getForList(TriggerSet.class, sql);
        //System.out.println(list.size());
        return list;
    }

    public void addTriggerSet(TriggerSet tso){
        int eid = tso.getEid();
        int sid = tso.getSid();
        int order = tso.getOrder();
        String sql = "insert trigger_set (eid,sid,`order`) values(?,?,?)";
        daoUtils.update(sql, eid,sid,order);
    }

    public List<TriggerSet> getTriggerSetbyEid(int eid){
        String sql = "select * from trigger_set where eid=?";
        List<TriggerSet> list = daoUtils.getForList(TriggerSet.class,sql,eid);
        //System.out.println(list.size());
        return list;

    }

    public List<Event> getEvent_notFailure(){
        String sql = "select * from event where type_id<>1";
        List<Event> list = daoUtils.getForList(Event.class, sql);
        return list;
    }

    public int[] getTriggerEidlist(){
        String sql = "select distinct eid from trigger_set";
        List<Integer> list = daoUtils.getListForValue(sql);
        int[] res = new int[list.size()];
        int index = 0;
        for(Integer i:list){
            res[index] = i;
            index++;
        }
        return res;
    }
	public boolean saveList(List list){
		return daoUtils.insertList(list);
	}
	public boolean saveHDList(List<HistoryData> list){
        return daoUtils.insertHistoryDataList(list);
    }

    public boolean saveRule(Rule r){
        String orSql = "INSERT INTO `grid_event`.`or_rule`\n" +
                "(\n" +
                "`arid`,\n" +
                "`eid`,\n" +
                "`sid`,\n" +
                "`signal_type`,\n" +
                "`order`,\n" +
                "`signal_value`)\n" +
                "VALUES\n" +
                "(?,?,?,?,-1,-1);";
        String arSql = "INSERT INTO `grid_event`.`and_rule`\n" +
                "(\n" +
                "`eid`,\n" +
                "`order`)\n" +
                "VALUES\n" +
                "(?,-1)";

        try {
            daoUtils.update(arSql, r.getEventId());
            int aid = daoUtils.getForValue("select max(arid) from and_rule");
            String[] signals = r.getSignals().split("\\|");
            for(String signal:signals) {
                System.out.println(signal);
                daoUtils.update(orSql, aid, r.getEventId(), getSidbySignalinfo(signal), r.getSignal_type());
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean clearHistoryData(){
        try {
            daoUtils.update("delete from `grid_event`.`history_data`");
            daoUtils.update("truncate `grid_event`.`history_data`");
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
