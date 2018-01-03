package nju.zxl.signalevent.bean;

import java.util.List;
import java.util.Map;

public class EventRuleBean {
    private int id;
    private int eventId;
    private List<SignalValueBean> signalValues;
    private int type;
    //private int order;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public List<SignalValueBean> getSignalValues() {
        return signalValues;
    }

    public void setSignalValues(List<SignalValueBean> signalValues) {
        this.signalValues = signalValues;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
