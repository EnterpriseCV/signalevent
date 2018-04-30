package nju.zxl.signalevent.controller;

import nju.zxl.signalevent.model.OrRule;
import nju.zxl.signalevent.service.DataEditService;
import nju.zxl.signalevent.service.DataFetchService;
import nju.zxl.signalevent.service.DataImportService;
import nju.zxl.signalevent.service.RuleOptimizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/data")
public class DataController {

    /*
    @Autowired
    DataFetchService dfs;

    @Autowired
    DataEditService des;

    @Autowired
    DataImportService dis;

    @Autowired
    RuleOptimizeService ros;

    @RequestMapping(value="/import/signal",method = RequestMethod.POST)
    public int importSignal(@RequestParam(value = "file") MultipartFile file){
        int importResult = -1;
        importResult=dis.importSignal(file);

        return importResult;
    }

    @RequestMapping(value="/import/event",method = RequestMethod.POST)
    public int importEvent(@RequestParam(value = "file") MultipartFile file){
        return dis.importEvent(file);
    }

    @RequestMapping(value="/import/andorrule",method = RequestMethod.POST)
    public int importAndOrRule(@RequestParam(value = "file")MultipartFile file){
        return dis.importAndOrRule(file);
    }

    @RequestMapping(value="/import/historydata",method = RequestMethod.POST)
    public int importHistroyData(@RequestParam(value = "file")MultipartFile file){
        return dis.importHistoryData(file);
    }
    @RequestMapping(value="/edit/orrule",method = RequestMethod.POST)
    public int editOrRule(@RequestParam(value = "orid")int orid,@RequestParam(value = "signal_value")String signal_value) {
        if(des.editOrRuleValue(orid,signal_value)){
            return 1;
        }
        return 0;
    }

    @RequestMapping(value = "/fetch/orrules",method = RequestMethod.GET)
    public List<OrRule> getOrRules(){
        return dfs.getAllOrRules();
    }

    @RequestMapping(value = "/fetch/orrule",method = RequestMethod.GET)
    public OrRule getOrRule(@RequestParam(value = "orid")int orid){
        return dfs.getOrRuleByOrid(orid);
    }
    @RequestMapping(value="/rule/optimize",method = RequestMethod.POST)
    public List<OrRule> ruleOptimize(@RequestParam("file") MultipartFile file){
        List<OrRule> orList = new ArrayList<OrRule>();
        //todo
        System.out.println(file.getOriginalFilename());
        int importResult = -1;
        importResult=dis.importHistoryData(file);
        if(importResult!=114514)return orList;

        List<Integer> oridList = new ArrayList<Integer>();
        oridList.addAll(ros.ruleOptimize());
        for(int orid:oridList){
            orList.add(dfs.getOrRuleByOrid(orid));
        }
        return orList;
    }
    */
}
