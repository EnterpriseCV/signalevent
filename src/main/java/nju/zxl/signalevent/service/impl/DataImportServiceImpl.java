package nju.zxl.signalevent.service.impl;

import nju.zxl.signalevent.dao.EventDao;
import nju.zxl.signalevent.dao.EventRuleDao;
import nju.zxl.signalevent.dao.SignalDao;
import nju.zxl.signalevent.dao.SignalNativeDao;
import nju.zxl.signalevent.domain.Event;
import nju.zxl.signalevent.domain.EventRule;
import nju.zxl.signalevent.domain.Signal;
import nju.zxl.signalevent.service.DataImportService;
import nju.zxl.signalevent.service.StaticFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataImportServiceImpl implements DataImportService{
    @Autowired
    SignalDao sd;
    @Autowired
    EventDao ed;
    @Autowired
    EventRuleDao erd;
    @Autowired
    SignalNativeDao snd;

    @Override
    public int importSignal(MultipartFile f) {
        List<Signal> slist = StaticFileService.getSignalFromFile(f);
        //snd.saveSignalList(slist);
        for(Signal s:slist){
            sd.save(s);
        }
        return 0;
    }

    @Override
    public int importEvent(MultipartFile f) {
        List<Event> elist = StaticFileService.getEventFromFile(f);
        for(Event e:elist){
            ed.save(e);
        }
        return 0;
    }

    @Override
    public int importEventRule(MultipartFile f) {
        List<EventRule> erlist = StaticFileService.getEventRuleFromFile(f);
        for(EventRule er:erlist){
            erd.save(er);
        }

        double coefficient = 0.8;
        erlist = erd.findAll();
        for(EventRule er:erlist){
            List<EventRule> targetEventRuleList = erd.findByEventid(er.getEventId());
            for(int i=0;i<targetEventRuleList.size();i++){
                if((!er.isNecessary()&&(targetEventRuleList.get(i).isNecessary()))
                        ||(er.isNecessary()&&(!targetEventRuleList.get(i).isNecessary()))){
                    targetEventRuleList.remove(i);
                    i--;
                }
            }


            int[] signals = er.getSignalsIntArray();
            double[] values = new double[signals.length];

            List<EventRule> eventRuleListWithTheSameNecessarity=new ArrayList<EventRule>();
            if(er.isNecessary()){
                eventRuleListWithTheSameNecessarity.addAll(erd.findByType(1));
                eventRuleListWithTheSameNecessarity.addAll(erd.findByType(2));
            }else{
                eventRuleListWithTheSameNecessarity.addAll(erd.findByType(4));
            }

            for(int i=0;i<signals.length;i++){
                int signalappearscount=0,totalsignalcount=0;
                for(EventRule tempEventRule:eventRuleListWithTheSameNecessarity){
                    int[] tempEventRuleSignals=tempEventRule.getSignalsIntArray();
                    for(int j=0;j<tempEventRuleSignals.length;j++){
                        if(tempEventRuleSignals[j]==signals[i]){
                            signalappearscount++;
                        }
                        totalsignalcount++;
                    }
                }
                values[i]=-Math.log((double)signalappearscount/(double)totalsignalcount);
            }

            for(int i=0;i<values.length;i++){
                if(er.isNecessary()) {
                    values[i] = values[i] / (double) targetEventRuleList.size() / values.length * coefficient;
                }
                else {
                    values[i] = values[i] / (double) targetEventRuleList.size() / values.length * (1 - coefficient);
                }
            }

            er.setValuesDoubleArray(values);
            erd.save(er);
        }

        return 0;
    }
}
