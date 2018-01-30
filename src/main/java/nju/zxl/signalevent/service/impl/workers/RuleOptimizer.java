package nju.zxl.signalevent.service.impl.workers;


import nju.zxl.signalevent.eo.AndRule;
import nju.zxl.signalevent.eo.Event;
import nju.zxl.signalevent.eo.HistoryData;
import nju.zxl.signalevent.eo.OrRule;
import nju.zxl.signalevent.util.DaoUtils;

import java.math.BigInteger;
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

        List<HistoryData> list = daoUtils.getForList(HistoryData.class, "select * from history_data where handle_tag=0 and event_type like '%故障%' order by id desc");
        System.out.println(list.size());
        return list;
    }

    /**
     * 获得未处理过的异常类历史数据
     *
     * @return
     */
    public List<HistoryData> getUnhandleExceptionData() {

        List<HistoryData> list = daoUtils.getForList(HistoryData.class, "select * from history_data where handle_tag=0 and event_type like '%异常%' order by id desc");
        System.out.println(list.size());
        return list;
    }

    public Event getEvent(int eid) {
        List<Event> list = daoUtils.getForList(Event.class, "select * from event where eid=" + eid);
        System.out.println(list.size());
        return list.get(0);
    }

    public Event getEventByInfo(int area_id, int voltage_id, int equipment_id, int info_id) {
        List<Event> list = daoUtils.getForList(Event.class, "select * from event where area_id=? and voltage_id=? and equipment_id=? and info_id=?", area_id, voltage_id, equipment_id, info_id);
        System.out.println(list.size());
        return list.get(0);
    }

    public List<AndRule> getAndRuleByEid(int eid) {
        List<AndRule> list = daoUtils.getForList(AndRule.class, "select * from and_rule where eid=" + eid);
        System.out.println(list.size());
        return list;
    }

    public List<OrRule> getOrRuleByEid(int eid) {
        List<OrRule> list = daoUtils.getForList(OrRule.class, "select * from or_rule where eid=" + eid);
        System.out.println(list.size());
        return list;
    }

    public int getSidByFid(String[] strings) {
        String sql = "SELECT sid FROM grid_event.`signal` where area_id=? and equip_id=? and info_id=? and act_id=?";
        int sid = -1;
        try {
            sid = daoUtils.getForValue(sql, strings);
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

        String event_type = hdlist.get(0).getEvent_type();
        tmpList.add(hdlist.get(0));
        Event event = getEventFromHd(hdlist.get(0));

        for (int i = 1; i < hdlist.size(); i++) {
            if (hdlist.get(i).getEvent_type().equals(event_type)) {
                tmpList.add(hdlist.get(i));
                continue;
            } else {
                map.put(event, tmpList);
                event_type = hdlist.get(i).getEvent_type();
                event = getEventFromHd(hdlist.get(i));
                tmpList = new ArrayList<HistoryData>();
                tmpList.add(hdlist.get(i));
            }
        }
        System.out.println(map.size());
        return map;
    }

    /**
     * 根据历史数据中的事件信息返回对应的事件
     * 如果是故障事件，根据事件信息找到对应的event对象
     * 如果是异常事件创建新的event对象，确定event各字段值并插入新event数据到event表
     * 等待实现！！
     * @param historyData
     * @return Event
     */
    public Event getEventFromHd(HistoryData historyData) {

        int interval = -1;
        int equipment = -1, equipment1 = -1;
        int voltage = -1;
        int information = -1;


        String actionattr = historyData.getAction_attr().trim();
        if (actionattr.equals("合闸") || actionattr.equals("分闸")) {
            interval = Integer.parseInt(historyData.getBay_id());
        }

        if (interval == 2) {
            equipment = 2;
            equipment1 = 3;
        } else if (interval == 3) {
            equipment = 4;
            equipment1 = 5;
        } else {
            equipment = interval;
            equipment1 = interval;
        }


        if ((interval >= 1 && interval <= 9) || (interval >= 34 && interval <= 35))
            voltage = 1;
        else if ((interval >= 25 && interval <= 33) || (interval >= 36 && interval <= 37))
            voltage = 2;
        else if ((interval >= 10 && interval <= 16) || (interval >= 38 && interval <= 39) || (interval == 24))
            voltage = 3;

        String event_type = historyData.getEvent_type().trim();
        if (event_type.contains("永久性故障"))
            information = 6;
        else if (event_type.contains("瞬时性故障"))
            information = 5;
        else if (event_type.contains("相间故障"))
            information = 4;
        else if (event_type.contains("单相永久故障"))
            information = 3;
        else if (event_type.contains("单相瞬时故障"))
            information = 2;
        else if (event_type.contains("母线故障"))
            information = 1;
        else if (event_type.contains("主变电气量故障"))
            information = 7;
        else if (event_type.contains("本地故障"))
            information = 8;
        else if (event_type.contains("有载调压故障"))
            information = 9;

//        Event event = new Event();
//        event.setEid(4);
//        event.setArea_id(interval);
//        event.setEquipment_id(equipment);
//        event.setInfo_id(information);
//        event.setVoltage_id(voltage);
//        event.setType_id(type);
//        event.setRank_id(rank);
        Event event = getEventByInfo(interval, voltage, equipment, information);
        return event;
    }

    /**
     * 处理故障事件的规则
     */
    public List correctRule() {
        Map<Event, List<HistoryData>> map = buildEvent(getUnhandleFaultData());
        List resultList = new ArrayList<>();

        for (Map.Entry<Event, List<HistoryData>> entry :
                map.entrySet()) {
            Event event = entry.getKey();
            List<HistoryData> historyDataList = entry.getValue();
            List<Integer> sidList = new ArrayList<>();

            List<AndRule> arlist = getAndRuleByEid(event.getEid());
            List<OrRule> orlist = getOrRuleByEid(event.getEid());

            for (int i = 0; i < historyDataList.size(); i++) {
                String[] signalInfo = historyDataList.get(i).getSignal_fid().split(",");
                int sid = getSidByFid(signalInfo);
                if (sid != -1) {
                    boolean isContains = false;
                    for (int j = 0; j < orlist.size(); j++) {
                        if (orlist.get(j).getSid() == sid) {
                            isContains = true;
                            break;
                        }
                    }
                    if (!isContains) {
                        createNewRule(event.getEid(), sid, 4);
                        OrRule orRule = daoUtils.get(OrRule.class,"select * from `or_rule` where orid = (select max(orid) from `or_rule`)");
                        resultList.add(orRule);
                    }
                }
                System.out.println(sid);
            }

        }

        return resultList;
    }

    /**
     * 根据参数创建新规则
     * @param eid
     * @param sid
     * @param signal_type
     */
    public void createNewRule(int eid, int sid, int signal_type) {
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
    }

    /**
     * 处理历史数据中未处理过的异常事件
     * 并添加新异常事件规则
     */
    public void dealExceptionRule() {
        Map<Event, List<HistoryData>> map = buildEvent(getUnhandleExceptionData());

        for (Map.Entry<Event, List<HistoryData>> entry :
                map.entrySet()) {
            Event event = entry.getKey();
            List<HistoryData> historyDataList = entry.getValue();
            for (int i = 0; i < historyDataList.size(); i++) {

                String[] signalInfo = historyDataList.get(i).getSignal_fid().split(",");
                int sid = getSidByFid(signalInfo);
                if (containsSidInOr(sid)) {
                    continue;
                }

                int signal_type = -1;
                //根据信号属性判断信号类型
                //由于异常信号所有信号都属于必要信号，只有1和2两种值
                if (historyDataList.get(i).getAction_attr().contains("动作") || historyDataList.get(i).getAction_attr().contains("复归")) {
                    signal_type = 1;
                } else {
                    signal_type = 2;
                }
                createNewRule(event.getEid(), sid, signal_type);
            }
        }
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
}
