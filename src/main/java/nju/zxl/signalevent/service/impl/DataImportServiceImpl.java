package nju.zxl.signalevent.service.impl;

import nju.zxl.signalevent.model.HistoryData;
import nju.zxl.signalevent.bean.Rule;
import nju.zxl.signalevent.model.*;
import nju.zxl.signalevent.service.DataImportService;
import nju.zxl.signalevent.service.impl.workers.DataInit;
import nju.zxl.signalevent.service.impl.workers.DataOperation;
import nju.zxl.signalevent.service.impl.workers.ValueGenerator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DataImportServiceImpl implements DataImportService{
    @Override
    public int importSignal(MultipartFile f) {
        List<Signal> list = new DataInit().getSignalsFromFile(f);
        boolean saveResult = false;
        saveResult=new DataOperation().saveList(list);
        System.out.println(saveResult);
        if(saveResult) {
            return 114514;
        }else{
            return 1;
        }
    }

    @Override
    public int importEvent(MultipartFile f) {
        List<Event> list = new DataInit().getEventsFromFile(f);
        boolean saveResult = false;
        saveResult=new DataOperation().saveList(list);
        System.out.println(saveResult);
        if(saveResult) {
            return 114514;
        }else{
            return 1;
        }
    }

    @Override
    public int importAndOrRule(MultipartFile f) {
        DataOperation dataOperation = new DataOperation();
        List<Rule> ruleList = new DataInit().getRulesFromFile(f);
        for(int i=0;i<ruleList.size();i++){
            System.out.println(ruleList.get(i).getSignals()+" "+isRuleCovered(ruleList.get(i)));
            if(isRuleCovered(ruleList.get(i))){
                ruleList.remove(i);
                i--;
                continue;
            }
        }
        for(Rule r:ruleList){
            dataOperation.saveRule(r);
        }
        new ValueGenerator().getValues();
        return 0;
    }

    private boolean isRuleCovered(Rule r){
        DataOperation dataOperation = new DataOperation();
        Set<Integer> checkingSignalSet = new HashSet<Integer>();
        String[] signals = r.getSignals().split("\\|");
        for(String signal:signals){
            int sid = dataOperation.getSidbySignalinfo(signal);
            if(sid==0){
                return true;
            }else{
                checkingSignalSet.add(sid);
            }
        }

        List<AndRule> arList = dataOperation.getAndrulelist();
        if(arList==null||arList.size()==0){
            return false;
        }
        for(AndRule ar:arList){
            List<OrRule> orList = dataOperation.getOrrulelistbyArid(ar.getArid());
            if(orList==null||orList.size()==0){
                continue;
            }
            if(orList.get(0).getEid()!=r.getEventId()){
                continue;
            }
            if(orList.get(0).getSignal_type()!=r.getSignal_type()){
                continue;
            }
            Set<Integer> andRuleSignalSet = new HashSet<Integer>();
            for(OrRule or:orList){
                andRuleSignalSet.add(or.getSid());
            }
            if(andRuleSignalSet.equals(checkingSignalSet)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int importHistoryData(MultipartFile f) {
        new DataOperation().clearHistoryData();
        List<HistoryData> hdlist = new DataInit().getHistoryDataFromFile(f);
        for(HistoryData hd:hdlist){
            System.out.println(hd.getTransf()+" "+hd.getOccur_time()+" "+hd.getSignal_type()+" "+hd.getSignal_fid());
        }
        boolean saveResult = false;
        saveResult=new DataOperation().saveHDList(hdlist);
        System.out.println(saveResult);
        if(saveResult) {
            return 114514;
        }else{
            return 1;
        }
    }
}
