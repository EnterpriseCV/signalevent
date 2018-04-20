package nju.zxl.signalevent.service.impl.workers;


import nju.zxl.signalevent.eo.AndRule;
import nju.zxl.signalevent.eo.Event;
import nju.zxl.signalevent.eo.HistoryData;
import nju.zxl.signalevent.eo.OrRule;
import nju.zxl.signalevent.util.DaoUtils;

import java.util.*;

public class RuleOptimizer {
    DaoUtils daoUtils;

    public RuleOptimizer() {
        daoUtils = new DaoUtils();
    }

    /**
     * 获得未处理过的故障类历史数据
     *
     * @return
     */
    public List<HistoryData> getUnhandleFaultData() {

        List<HistoryData> list = daoUtils.getForList(HistoryData.class, "select * from history_data where handle_tag=0 and event_detail like '%故障%' and signal_fid<>'' and event_mark<>'' order by id desc");
        if (list==null || list.size()==0) return null;
        System.out.println(list.size());
        //System.out.println(list.get(0).getEvent_type());
        return list;
    }

    /**
     * 获得未处理过的非故障类历史数据
     *
     * @return
     */
    public List<HistoryData> getUnhandleUnfaultData() {

        List<HistoryData> list = daoUtils.getForList(HistoryData.class, "select * from history_data where handle_tag=0 and event_detail not like '%故障%' and signal_fid<>'' and event_mark<>'' order by id desc");
        if (list==null || list.size()==0) return null;
        System.out.println(list.size());
        return list;
    }

    public Event getEvent(int eid) {
        List<Event> list = daoUtils.getForList(Event.class, "select * from event where eid=" + eid);
        if (list==null || list.size()==0) return null;
        System.out.println(list.size());
        return list.get(0);
    }

    public Event getEventByInfo(int area_id, int voltage_id, int equipment_id, int info_id,int type_id) {
        List<Event> list = daoUtils.getForList(Event.class, "select * from event where area_id=? and voltage_id=? and equipment_id=? and info_id=? and type_id=?", area_id, voltage_id, equipment_id, info_id,type_id);
        if (list==null || list.size()==0) return null;
        System.out.println(list.size());
        return list.get(0);
    }

    public List<AndRule> getAndRuleByEid(int eid) {
        List<AndRule> list = daoUtils.getForList(AndRule.class, "select * from and_rule where eid=" + eid);
        if (list==null || list.size()==0) return null;
        System.out.println(list.size());
        return list;
    }

    public List<OrRule> getOrRuleByEid(int eid) {
        List<OrRule> list = daoUtils.getForList(OrRule.class, "select * from or_rule where eid=" + eid);
        if (list==null || list.size()==0) return null;
        System.out.println(list.size());
        return list;
    }

    public int getSidByFid(String[] strings) {
        String sql = "SELECT sid FROM grid_event.`signal` where area_id=? and equip_id=? and info_id=? and act_id=?";
        int sid = -1;
        try {
            Integer integer = daoUtils.getForValue(sql, strings);
            if (integer==null){
                sid=-1;
            }else {
                sid=integer.intValue();
            }
            System.out.println(sid);
            return sid;
        } catch (Exception e) {
            System.out.println(Arrays.toString(strings));
            e.printStackTrace();
        }
        return -1;
    }


    /**
     * 根据历史数据列表获取同一个事件以及其对应的历史数据
     *
     * @param hdlist
     * @return
     */
    public Map<Event, List<HistoryData>> buildEvent(List<HistoryData> hdlist) {
        if (hdlist == null) return null;
        Map<Event, List<HistoryData>> map = new HashMap<>();
        List<HistoryData> tmpList = new ArrayList<HistoryData>();

        String event_mark = hdlist.get(0).getEvent_mark();
        tmpList.add(hdlist.get(0));
        Event event = getEventFromHd(hdlist.get(0));

        for (int i = 1; i < hdlist.size(); i++) {
            if (hdlist.get(i).getEvent_mark().equals(event_mark)) {
                tmpList.add(hdlist.get(i));
                continue;
            } else {
                map.put(event, tmpList);
                event_mark = hdlist.get(i).getEvent_type();
                event = getEventFromHd(hdlist.get(i));
                tmpList = new ArrayList<HistoryData>();
                tmpList.add(hdlist.get(i));
            }
        }
        System.out.println(map.size());
        return map;
    }

    public int getIdByValue(String tableName,String attributeName,String attributeValue){
        int id;
        Integer integer = daoUtils.getForValue("SELECT active_id FROM grid_event.attribute_name where table_name=? and attribute_name=? and attribute_value=?",tableName,attributeName,attributeValue);
        if (integer==null){
            id=-1;
        }else {
            id=integer.intValue();
        }
        System.out.println(id);
        return id;
    }

    /**
     * 根据历史数据中的事件信息返回对应的事件
     * 如果是故障事件，根据事件信息找到对应的event对象
     * 如果是异常事件创建新的event对象，确定event各字段值并插入新event数据到event表
     *
     * @param historyData
     * @return Event
     */
    public Event getEventFromHd(HistoryData historyData) {

        String eventType = historyData.getEvent_type();
        String eventArea = historyData.getEvent_area();
        String eventVol = historyData.getEvent_vol();
        String eventEquip = historyData.getEvent_equip();
        String eventInfo = historyData.getEvent_info();

        int type_id = getIdByValue("event","type_id",eventType);
        int area_id = getIdByValue("event","area_id",eventArea);
        int voltage_id =getIdByValue("event","voltage_id",eventVol);
        int equipment_id=getIdByValue("event","equipment_id",eventEquip);
        int info_id=getIdByValue("event","info_id",eventInfo);

        Event event = getEventByInfo(area_id, voltage_id, equipment_id, info_id,type_id);
        if (event==null){
            daoUtils.update("INSERT INTO `grid_event`.`event`\n" +
                    "(\n" +
                    "`area_id`,\n" +
                    "`voltage_id`,\n" +
                    "`equipment_id`,\n" +
                    "`info_id`,\n" +
                    "`type_id`,\n" +
                    "`rank_id`)\n" +
                    "VALUES\n" +
                    "(?,?,?,?,?,-1)",area_id,voltage_id,equipment_id,info_id,type_id);
            event = getEventByInfo(area_id, voltage_id, equipment_id, info_id,type_id);
        }
        return event;
    }

    /**
     * 处理故障事件的规则
     */
    public List<Integer> dealFaultRule() {
        List<Integer> newOrRuleIdList = new ArrayList<Integer>();

        Map<Event, List<HistoryData>> map = buildEvent(getUnhandleFaultData());

        for (Map.Entry<Event, List<HistoryData>> entry :
                map.entrySet()) {
            Event event = entry.getKey();
            List<HistoryData> historyDataList = entry.getValue();
            //List<Integer> sidList = new ArrayList<>();
            //List<AndRule> arlist = getAndRuleByEid(event.getEid());

            List<OrRule> orlist = getOrRuleByEid(event.getEid());

            for (int i = 0; i < historyDataList.size(); i++) {
                String[] signalInfo = historyDataList.get(i).getSignal_fid().split(",");
                int sid = getSidByFid(signalInfo);
                if (sid != -1) {
                    if (orlist!=null) {
                        boolean isContains = false;
                        for (int j = 0; j < orlist.size(); j++) {
                            if (orlist.get(j).getSid() == sid) {
                                isContains = true;
                                if(!newOrRuleIdList.contains(orlist.get(j).getOrid()))newOrRuleIdList.add(orlist.get(j).getOrid());
                                break;
                            }
                        }
                        if (!isContains) {
                            newOrRuleIdList.add( createNewRule(event.getEid(), sid, 4));
                        }
                    }else {
                        //如果故障类事件原先不存在，新建后or规则如何设置
                    }
                }
                System.out.println(sid);
            }

        }
        return newOrRuleIdList;
    }

    /**
     * 根据参数创建新规则
     * @param eid
     * @param sid
     * @param signal_type
     */
    public int createNewRule(int eid, int sid, int signal_type) {
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

        daoUtils.update(arSql, eid);
        System.out.println("new and rule");
        int aid = daoUtils.getForValue("select max(arid) from and_rule");
        System.out.println("new aid: " + aid);

        daoUtils.update(orSql, aid, eid, sid, signal_type);
        System.out.println("new or rule");
        int orid = daoUtils.getForValue("select max(orid) from or_rule");
        return orid;
    }

    /**
     * 处理历史数据中未处理过的非故障类事件
     * 并添加新异常事件规则
     */
    public List<Integer> dealUnfaultRule() {
        List<Integer> newOrRuleIdList = new ArrayList<Integer>();

        Map<Event, List<HistoryData>> map = buildEvent(getUnhandleUnfaultData());

        for (Map.Entry<Event, List<HistoryData>> entry :
                map.entrySet()) {
            Event event = entry.getKey();
            List<HistoryData> historyDataList = entry.getValue();
            for (int i = 0; i < historyDataList.size(); i++) {

                int signal_type = -1;
                //根据信号属性判断信号类型
                //由于异常信号所有信号都属于必要信号，只有1和2两种值
                if (historyDataList.get(i).getAction_attr().contains("动作") || historyDataList.get(i).getAction_attr().contains("复归")) {
                    signal_type = 1;
                } else {
                    signal_type = 2;
                }

                String[] signalInfo = historyDataList.get(i).getSignal_fid().split(",");
                int sid = getSidByFid(signalInfo);
                if (containsSidInOr(sid)) {
                    int orid = getOrid(event.getEid(),sid,signal_type);
                    if(orid!=-1&&!newOrRuleIdList.contains(orid)){
                        newOrRuleIdList.add(orid);
                    }
                    continue;
                }
                newOrRuleIdList.add(createNewRule(event.getEid(), sid, signal_type));
            }
        }
        return newOrRuleIdList;
    }

    /**
     * 判断信号是否已经存在or_rule表中
     *
     * @param sid
     * @return
     */
    public boolean containsSidInOr(int sid) {
        String sql = "select count(*) from or_rule where sid=?";
        long count = daoUtils.getForValue(sql, sid);
        return count != 0;
    }
    public int getOrid(int eid,int sid,int signal_type){
        String sql = "select * from or_rule where eid=? and sid=? and signal_type=?";
        OrRule or = daoUtils.get(OrRule.class,sql,eid,sid,signal_type);
        if(or!=null)return or.getOrid();
        else return -1;
    }
}
