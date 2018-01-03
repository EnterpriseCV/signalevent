package nju.zxl.signalevent.service;

import nju.zxl.signalevent.bean.EventRuleBean;

import java.util.List;

public interface DataFetchService {
    public List<EventRuleBean> getAllEventRule();
    public EventRuleBean getEventRuleById(int id);
}
