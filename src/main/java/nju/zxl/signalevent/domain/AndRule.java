package nju.zxl.signalevent.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="and_rule")
public class AndRule {
    @Id
    private int arid;
    private int eid;
    private int order;

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

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
