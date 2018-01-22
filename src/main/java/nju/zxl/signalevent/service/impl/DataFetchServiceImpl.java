package nju.zxl.signalevent.service.impl;

import nju.zxl.signalevent.bean.EventRuleBean;
import nju.zxl.signalevent.bean.SignalValueBean;
import nju.zxl.signalevent.service.DataFetchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataFetchServiceImpl implements DataFetchService{

    @Autowired
    private EventRuleDao erd;

    @Override
    public List<EventRuleBean> getAllEventRule() {
        List<EventRuleBean> erbl = new ArrayList<EventRuleBean>();
        List<EventRule> erl = erd.findAll();
        for(EventRule er:erl){
            EventRuleBean erb = new EventRuleBean();
            erb.setId(er.getId());
            erb.setEventId(er.getEventId());
            List<SignalValueBean> svbl = new ArrayList<SignalValueBean>();
            for(int i=0;i<er.getSignalsIntArray().length;i++){
                SignalValueBean svb = new SignalValueBean();
                svb.setSignalId(er.getSignalsIntArray()[i]);
                svb.setValue(er.getValuesDoubleArray()[i]);
                svbl.add(svb);
            }
            erb.setSignalValues(svbl);
            erb.setType(er.getType());
            erbl.add(erb);
        }
        return erbl;
    }

    @Override
    public EventRuleBean getEventRuleById(int id) {
        EventRule er = erd.findById(id);
        EventRuleBean erb = new EventRuleBean();
        if(er==null){
            return erb;
        }
        erb.setId(er.getId());
        erb.setEventId(er.getEventId());
        List<SignalValueBean> svbl = new ArrayList<SignalValueBean>();
        for(int i=0;i<er.getSignalsIntArray().length;i++){
            SignalValueBean svb = new SignalValueBean();
            svb.setSignalId(er.getSignalsIntArray()[i]);
            svb.setValue(er.getValuesDoubleArray()[i]);
            svbl.add(svb);
        }
        erb.setSignalValues(svbl);
        erb.setType(er.getType());
        return erb;
    }
}
