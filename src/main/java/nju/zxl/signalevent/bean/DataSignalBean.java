package nju.zxl.signalevent.bean;

import java.util.Date;

public class DataSignalBean {
    private int id;
    private String TS_name;
    private Date time;
    private SignalBean s;
    private int simulatedEventId;
    private String actualEvent;
    private int mark; //标记状态分为0，1，2，0为未标记，1为已标记，2为不确定


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTS_name() {
        return TS_name;
    }

    public void setTS_name(String TS_name) {
        this.TS_name = TS_name;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public SignalBean getS() {
        return s;
    }

    public void setS(SignalBean s) {
        this.s = s;
    }

    public int getSimulatedEventId() {
        return simulatedEventId;
    }

    public void setSimulatedEventId(int simulatedEventId) {
        this.simulatedEventId = simulatedEventId;
    }

    public String getActualEvent() {
        return actualEvent;
    }

    public void setActualEvent(String actualEvent) {
        this.actualEvent = actualEvent;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }
}
