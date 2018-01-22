package nju.zxl.signalevent.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="`signal`")
public class Signal {
    @Id
    private int xh;
    private int jg_bh;
    private int sb_bh;
    private int info_bh;
    private int act_bh;

    public int getXh() {
        return xh;
    }

    public void setXh(int xh) {
        this.xh = xh;
    }

    public int getJg_bh() {
        return jg_bh;
    }

    public void setJg_bh(int jg_bh) {
        this.jg_bh = jg_bh;
    }

    public int getSb_bh() {
        return sb_bh;
    }

    public void setSb_bh(int sb_bh) {
        this.sb_bh = sb_bh;
    }

    public int getInfo_bh() {
        return info_bh;
    }

    public void setInfo_bh(int info_bh) {
        this.info_bh = info_bh;
    }

    public int getAct_bh() {
        return act_bh;
    }

    public void setAct_bh(int act_bh) {
        this.act_bh = act_bh;
    }
}
