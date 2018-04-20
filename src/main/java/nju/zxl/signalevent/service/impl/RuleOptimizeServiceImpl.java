package nju.zxl.signalevent.service.impl;

import nju.zxl.signalevent.service.RuleOptimizeService;
import nju.zxl.signalevent.service.impl.workers.RuleOptimizer;
import nju.zxl.signalevent.service.impl.workers.ValueGenerator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RuleOptimizeServiceImpl implements RuleOptimizeService{
    @Override
    public List ruleOptimize() {
        RuleOptimizer ro = new RuleOptimizer();
        List<Integer> oridList = new ArrayList<Integer>();
        oridList.addAll(ro.dealFaultRule());
        oridList.addAll(ro.dealUnfaultRule());
        new ValueGenerator().getValues();
        return oridList;
    }
}
