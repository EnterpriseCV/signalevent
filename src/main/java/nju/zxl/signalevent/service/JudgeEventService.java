package nju.zxl.signalevent.service;

import nju.zxl.signalevent.bean.DataSignalBean;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface JudgeEventService {
    public List<DataSignalBean> judgeEvent(MultipartFile file);
}
