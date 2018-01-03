package nju.zxl.signalevent.dao;

import nju.zxl.signalevent.domain.Signal;

import java.util.List;

public interface SignalNativeDao {
    public boolean saveSignalList(List<Signal> slist);
}
