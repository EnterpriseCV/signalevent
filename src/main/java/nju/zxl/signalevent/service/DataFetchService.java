package nju.zxl.signalevent.service;


import nju.zxl.signalevent.model.OrRule;

import java.util.List;

public interface DataFetchService {
    public List<OrRule> getAllOrRules();
    public OrRule getOrRuleByOrid(int orid);

}
