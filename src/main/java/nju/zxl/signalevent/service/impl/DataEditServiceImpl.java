package nju.zxl.signalevent.service.impl;

import nju.zxl.signalevent.eo.OrRule;
import nju.zxl.signalevent.service.DataEditService;
import nju.zxl.signalevent.service.impl.workers.DataOperation;
import org.springframework.stereotype.Service;

@Service
public class DataEditServiceImpl implements DataEditService{

    @Override
    public boolean editOrRuleValue(int orid,String signal_value) {
        try{
            new DataOperation().insertValue(orid,signal_value);
            return true;
        }catch (Exception e) {
            return false;
        }
    }
}
