package nju.zxl.signalevent.service.impl;

import nju.zxl.signalevent.model.OrRule;
import nju.zxl.signalevent.service.DataFetchService;
import nju.zxl.signalevent.service.impl.workers.DataOperation;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DataFetchServiceImpl implements DataFetchService{

    private static DataOperation dop;

    public DataFetchServiceImpl(){
        if(dop==null) {
            dop = new DataOperation();
        }
    }

    @Override
    public List<OrRule> getAllOrRules() {
        return dop.getOrrulelist();
    }

    @Override
    public OrRule getOrRuleByOrid(int orid) {
        return dop.getOrrulelistbyOrid(orid).get(0);
    }


}
