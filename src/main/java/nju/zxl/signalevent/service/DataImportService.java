package nju.zxl.signalevent.service;

import org.springframework.web.multipart.MultipartFile;

public interface DataImportService {
    public int importSignal(MultipartFile f);
    public int importEvent(MultipartFile f);
    public int importAndOrRule(MultipartFile f);
    public int importHistoryData(MultipartFile f);
}
