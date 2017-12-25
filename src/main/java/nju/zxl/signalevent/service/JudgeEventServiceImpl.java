package nju.zxl.signalevent.service;

import nju.zxl.signalevent.dao.SignalDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JudgeEventServiceImpl implements JudgeEventService{
    @Autowired
    SignalDao sd;
    @Override
    public String sayHi() {
        return Integer.toBinaryString(sd.findByXh(1).getAct_bh());
    }
}
