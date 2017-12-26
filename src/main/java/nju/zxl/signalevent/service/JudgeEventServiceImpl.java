package nju.zxl.signalevent.service;

import nju.zxl.signalevent.bean.DataSignalBean;
import nju.zxl.signalevent.bean.SignalBean;
import nju.zxl.signalevent.dao.EventDao;
import nju.zxl.signalevent.dao.EventRuleDao;
import nju.zxl.signalevent.dao.SignalDao;
import nju.zxl.signalevent.domain.EventRule;
import nju.zxl.signalevent.domain.Signal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

@Service
public class JudgeEventServiceImpl implements JudgeEventService{
    @Autowired
    SignalDao sd;
    @Autowired
    EventDao ed;
    @Autowired
    EventRuleDao erd;

    public static List<EventRule> ers=null;
    @Override
    public List<DataSignalBean> judgeEvent(MultipartFile file) {
        List<DataSignalBean> dsList = StaticFileService.getDataSignalFromFile(file);
        for(int i=0;i<dsList.size();i++){
            if(dsList.get(i).getMark()==1)
                continue;

            if(!isSignalNecessary(dsList.get(i).getS()))
                continue;

            DataSignalBean ds = dsList.get(i);
            List<DataSignalBean> targetDataSignals = new ArrayList<DataSignalBean>();
            targetDataSignals.add(ds);
            for(int j=i+1;j<dsList.size();j++){
                DataSignalBean tempds = dsList.get(j);
                if(tempds.getMark()==1)continue;
                if(isSignalNecessary(tempds.getS()))break;
                if(isDataSignalsInOneGroup(ds,tempds)){
                    targetDataSignals.add(tempds);
                }
            }

            int stimulatedEventId = getStimulatedEventId(targetDataSignals);

            for(DataSignalBean targetds:targetDataSignals){
                targetds.setMark(1);
                targetds.setSimulatedEventId(stimulatedEventId);
                if(isSiganlProtected(targetds.getS())){
                    for(int j=targetds.getId()+1;j<dsList.size();j++){
                        if(!targetds.getTS_name().equals(dsList.get(j).getTS_name()))continue;
                        if(dsList.get(j).getMark()!=0)continue;
                        if(dsList.get(j).getS().getAct_bh()!=2)continue;
                        int jg1 = dsList.get(j).getS().getJg_bh();
                        int jg2 = targetds.getS().getJg_bh();
                        int sb1 = dsList.get(i).getS().getSb_bh();
                        int sb2 = targetds.getS().getSb_bh();
                        int info1 = dsList.get(i).getS().getInfo_bh();
                        int info2 = targetds.getS().getInfo_bh();
                        if((jg1==jg2)&&(sb1==sb2)&&(info1==info2)){
                            dsList.get(j).setMark(1);
                            dsList.get(j).setSimulatedEventId(stimulatedEventId);
                            break;
                        }
                    }
                }
            }
        }

        return dsList;
    }



    private boolean isSignalNecessary(SignalBean signal){
        if(ers==null)ers = erd.findAll();
        for(EventRule e:ers){
            if(e.getType()!=1&&e.getType()!=2)continue;
            String[] signals = e.getSignals().split("\\|");
            for(String s:signals){
                Signal orisignal = sd.findByXh(Integer.parseInt(s));
                if(isSignalTheSame(orisignal,signal)){
                    return true;
                }
            }
        }
        return false;
    }
    private boolean isSiganlProtected(SignalBean signal){
        if(ers==null)ers = erd.findAll();
        for(EventRule e:ers){
            if(e.getType()==2||e.getType()==4){
                String[] signals = e.getSignals().split("\\|");
                for(String s:signals){
                    Signal orisignal = sd.findByXh(Integer.parseInt(s));
                    if(isSignalTheSame(orisignal,signal)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isSignalTheSame(Signal orisignal,SignalBean signal){
        if(orisignal.getAct_bh()==signal.getAct_bh()&&
                orisignal.getInfo_bh()==signal.getInfo_bh()&&
                orisignal.getJg_bh()==signal.getJg_bh()&&
                orisignal.getSb_bh()==signal.getSb_bh()){
            return true;
        }
        return false;
    }

    private boolean isDataSignalsInOneGroup(DataSignalBean ds1,DataSignalBean ds2){
        if(!ds1.getTS_name().equals(ds2.getTS_name()))return false;
        if(ds1.getS().getJg_bh()!=ds2.getS().getJg_bh())return false;
        if(Math.abs(ds1.getTime().getTime()-ds2.getTime().getTime())>60000)return false;
        return false;
    }

    private int getStimulatedEventId(List<DataSignalBean> targetDataSignals){

        List<EventRule> ers = erd.findAll();
        int[] tsid = new int[targetDataSignals.size()];
        for(int i=0;i<targetDataSignals.size();i++){
            SignalBean s = targetDataSignals.get(i).getS();
            int id = sd.findByInfo(s.getJg_bh(),s.getSb_bh(),s.getInfo_bh(),s.getAct_bh()).getXh();
            tsid[i] = id;
        }
        double simulatevalue = 0;
        int  simulateeventid = -1;
        double infovalue = 0;
        int eventId = ers.get(0).getEventId();
        for(EventRule er:ers){
            int currentEventId = er.getEventId();
            if(eventId!=currentEventId){
                infovalue = 0;
                eventId = currentEventId;
            }

            String[] signals = er.getSignals().split("\\|");
            String[] values = er.getValues().split("\\|");

            for(int i=0;i<signals.length;i++){
                for(int j=0;j<tsid.length;j++){
                    if(signals[i]==Integer.toString(tsid[j]))
                        infovalue += Double.parseDouble(values[i]);
                }
            }

            if(infovalue > simulatevalue){
                simulatevalue = infovalue;
                simulateeventid = currentEventId;
            }
        }
        return simulateeventid;
    }
}
