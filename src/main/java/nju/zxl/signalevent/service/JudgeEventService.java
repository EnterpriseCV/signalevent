package nju.zxl.signalevent.service;

import nju.zxl.signalevent.bean.JudgeResultBean;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface JudgeEventService {
    public List<JudgeResultBean> judgeEvent(MultipartFile file);

}
