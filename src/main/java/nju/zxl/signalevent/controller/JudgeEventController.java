package nju.zxl.signalevent.controller;

import nju.zxl.signalevent.service.JudgeEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("/judge")
public class JudgeEventController {
    @Autowired
    JudgeEventService jes;
    @RequestMapping(value="/",method = RequestMethod.POST)
    public String judgeEvent(@RequestParam("file") MultipartFile file){
        return file.getOriginalFilename();
    }
}
