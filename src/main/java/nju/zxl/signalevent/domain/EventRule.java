package nju.zxl.signalevent.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="`eventrule`")
public class EventRule {
    @Id
    private int id;
    private int eventid;
    private String signals;

    @Column(name="`values`")
    private String values;
    private int type;
    //private int order;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventId() {
        return eventid;
    }

    public void setEventId(int eventId) {
        this.eventid = eventId;
    }

    public String getSignals() {
        return signals;
    }

    public void setSignals(String signals) {
        this.signals = signals;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

//    public int getOrder() {
//        return order;
//    }
//
//    public void setOrder(int order) {
//        this.order = order;
//    }

    public void setSignalsIntArray(int[] signals){
        StringBuilder sb = new StringBuilder();
        if(signals.length>0){
            sb.append(signals[0]);
        }
        for(int i=1;i<signals.length;i++){
            sb.append('|');
            sb.append(signals[i]);
        }
        this.signals=sb.toString();
    }

    public int[] getSignalsIntArray(){
        String[] singalsStringArray=this.signals.split("\\|");
        int[] signalsIntArray = new int[singalsStringArray.length];
        for(int i=0;i<singalsStringArray.length;i++){
            signalsIntArray[i]=Integer.parseInt(singalsStringArray[i]);
        }
        return signalsIntArray;
    }

    public void setValuesDoubleArray(double[] values){
        StringBuilder sb = new StringBuilder();
        if(values.length>0){
            sb.append(values[0]);
        }
        for(int i=1;i<values.length;i++){
            sb.append('|');
            sb.append(values[i]);
        }
        this.values=sb.toString();
    }

    public double[] getValuesDoubleArray(){
        String[] valuesStringArray=this.values.split("\\|");
        double[] valuesDoubleArray = new double[valuesStringArray.length];
        for(int i=0;i<valuesStringArray.length;i++){
            valuesDoubleArray[i]=Double.parseDouble(valuesStringArray[i]);
        }
        return valuesDoubleArray;
    }

    public boolean isNecessary(){
        return this.type==1||this.type==2;
    }
}
