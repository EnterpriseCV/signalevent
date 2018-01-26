package nju.zxl.signalevent.service;

import nju.zxl.signalevent.eo.JudgeResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface JudgeEventService {
    public List<JudgeResult> judgeEvent(MultipartFile file);

}
