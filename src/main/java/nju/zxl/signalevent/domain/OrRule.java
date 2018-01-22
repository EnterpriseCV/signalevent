package nju.zxl.signalevent.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="or_rule")
public class OrRule {
    @Id
    private int orid;
    private int arid;
    private int eid;
    private int sid;
    private int signal_type;
    private int order;
    private String signal_value;

    public int getOrid() {
        return orid;
    }

    public void setOrid(int orid) {
        this.orid = orid;
    }

    public int getArid() {
        return arid;
    }

    public void setArid(int arid) {
        this.arid = arid;
    }

    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getSignal_type() {
        return signal_type;
    }

    public void setSignal_type(int signal_type) {
        this.signal_type = signal_type;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getSignal_value() {
        return signal_value;
    }

    public void setSignal_value(String signal_value) {
        this.signal_value = signal_value;
    }
}
