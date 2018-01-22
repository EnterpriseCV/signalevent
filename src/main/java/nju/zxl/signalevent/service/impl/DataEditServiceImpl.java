package nju.zxl.signalevent.service.impl;

import nju.zxl.signalevent.bean.EventRuleBean;
import nju.zxl.signalevent.bean.SignalValueBean;
import nju.zxl.signalevent.service.DataEditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataEditServiceImpl implements DataEditService{
    @Autowired
    EventRuleDao erd;
    @Override
    public boolean editEventRule(EventRuleBean erb) {
        EventRule er = new EventRule();
        er.setId(erb.getId());
        er.setEventId(erb.getEventId());
        int[] signals = new int[erb.getSignalValues().size()];
        double[] values = new double[erb.getSignalValues().size()];
        int count=0;
        for(SignalValueBean svb:erb.getSignalValues()){
            signals[count]=svb.getSignalId();
            values[count]=svb.getValue();
            count++;
        }
        er.setSignalsIntArray(signals);
        er.setValuesDoubleArray(values);
        er.setType(erb.getType());
        erd.save(er);
        return true;
    }
}
