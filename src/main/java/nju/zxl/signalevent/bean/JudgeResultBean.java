package nju.zxl.signalevent.bean;

import nju.zxl.signalevent.eo.JudgeResult;
import nju.zxl.signalevent.eo.Signal;

import java.util.List;

public class JudgeResultBean {
    String trigger_signal_id;
    List<JudgeResult> judgeResults;
    int final_event_id;
    int actual_event_id;

}
