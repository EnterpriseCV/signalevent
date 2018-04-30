package nju.zxl.signalevent.service.impl;

import nju.zxl.signalevent.model.JudgeResult;
import nju.zxl.signalevent.service.JudgeEventService;
import nju.zxl.signalevent.service.impl.workers.EventDeterminer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
public class JudgeEventServiceImpl implements JudgeEventService {
    @Override
    public List<JudgeResult> judgeEvent(MultipartFile file) {
        return new EventDeterminer().judgeEvent(file);
    }
}
