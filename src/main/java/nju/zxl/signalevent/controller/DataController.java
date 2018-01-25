package nju.zxl.signalevent.controller;

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

    @RequestMapping(value="/import/signal",method = RequestMethod.POST)
    public int importSignal(@RequestParam(value = "file") MultipartFile file){
        return dis.importSignal(file);
    }

    @RequestMapping(value="/import/event",method = RequestMethod.POST)
    public int importEvent(@RequestParam(value = "file") MultipartFile file){
        return dis.importEvent(file);
    }

    @RequestMapping(value="/import/andorrule",method = RequestMethod.POST)
    public int importAndOrRule(@RequestParam(value = "file")MultipartFile file){
        return dis.importAndOrRule(file);
    }
}
