package nju.zxl.signalevent.controller;

import nju.zxl.signalevent.bean.DataSignalBean;
import nju.zxl.signalevent.service.JudgeEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/judge")
public class JudgeEventController {
    @Autowired
    JudgeEventService jes;
    @RequestMapping(value="/",method = RequestMethod.POST)
    public List<DataSignalBean> judgeEvent(@RequestParam("file") MultipartFile file){
        return jes.judgeEvent(file);
    }
}
