package nju.zxl.signalevent.controller;

import nju.zxl.signalevent.service.JudgeEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/judge")
public class JudgeEventController {
    @Autowired
    JudgeEventService jes;
    @RequestMapping(value="/",method = RequestMethod.GET)
    public String judgeEvent(){
        return jes.sayHi();
    }
}
