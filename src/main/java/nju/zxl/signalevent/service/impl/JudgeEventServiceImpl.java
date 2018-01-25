package nju.zxl.signalevent.service.impl;

import nju.zxl.signalevent.bean.JudgeResultBean;
import nju.zxl.signalevent.eo.JudgeResult;
import nju.zxl.signalevent.service.JudgeEventService;
import nju.zxl.signalevent.service.impl.workers.EventDeterminer;
import nju.zxl.signalevent.service.impl.workers.ValueGenerator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@Service
public class JudgeEventServiceImpl implements JudgeEventService {
    @Override
    public List<JudgeResultBean> judgeEvent(MultipartFile file) {
        List<JudgeResultBean> list = new ArrayList<JudgeResultBean>();
        String current_batch_id ="....";
        List<JudgeResult> jrl = new EventDeterminer().judgeEvent(file);
        JudgeResultBean jrb = null;
        List<JudgeResult> tempjrl = null;

        for(JudgeResult jr : jrl){
            if(!jr.getBatch_id().equals(current_batch_id)){
                if(jrb!=null){
                    list.add(jrb);
                }
                jrb = new JudgeResultBean();
                tempjrl = new ArrayList<JudgeResult>();
                jrb.setTrigger_signal(new ValueGenerator().getSignalBySid(jr.getTrigger_signal_id()).get(0));
                jrb.setFinal_event_id(jr.getFinal_event_id());
                jrb.setActual_event_id(jr.getActual_event_id());
                jrb.setJudgeResults(tempjrl);
            }
            tempjrl.add(jr);
        }
        return list;
    }
}
