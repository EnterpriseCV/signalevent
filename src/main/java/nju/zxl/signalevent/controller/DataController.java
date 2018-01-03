package nju.zxl.signalevent.controller;

import nju.zxl.signalevent.bean.EventRuleBean;
import nju.zxl.signalevent.service.DataEditService;
import nju.zxl.signalevent.service.DataFetchService;
import nju.zxl.signalevent.service.DataImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    DataFetchService dfs;

    @Autowired
    DataEditService des;

    @Autowired
    DataImportService dis;

    @RequestMapping(value = "/fetch/eventrules",method = RequestMethod.GET)
    public List<EventRuleBean> getEventRules(){
        return dfs.getAllEventRule();
    }

    @RequestMapping(value="/fetch/eventrule/{id}",method = RequestMethod.GET)
    public EventRuleBean getEventRule(@PathVariable int id){
        return dfs.getEventRuleById(id);
    }

    @RequestMapping(value = "/edit/eventrule",method = RequestMethod.POST)
    public boolean editEventRule(@RequestBody EventRuleBean erb){
        return des.editEventRule(erb);
    }

    @RequestMapping(value="/import/signal",method = RequestMethod.POST)
    public int importSignal(@RequestParam(value = "file") MultipartFile file){
        return dis.importSignal(file);
    }

    @RequestMapping(value="/import/event",method = RequestMethod.POST)
    public int importEvent(@RequestParam(value = "file") MultipartFile file){
        return dis.importEvent(file);
    }

    @RequestMapping(value="/import/eventrule",method = RequestMethod.POST)
    public int importEventRule(@RequestParam(value = "file")MultipartFile file){
        return dis.importEventRule(file);
    }
}
