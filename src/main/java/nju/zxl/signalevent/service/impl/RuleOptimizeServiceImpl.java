package nju.zxl.signalevent.service.impl;

import nju.zxl.signalevent.service.RuleOptimizeService;
import nju.zxl.signalevent.service.impl.workers.RuleOptimizer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleOptimizeServiceImpl implements RuleOptimizeService{
    @Override
    public List ruleOptimize() {
        return new RuleOptimizer().correctRule();
    }
}
