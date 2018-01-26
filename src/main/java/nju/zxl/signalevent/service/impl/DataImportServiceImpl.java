package nju.zxl.signalevent.service.impl;

import nju.zxl.signalevent.service.DataImportService;
import nju.zxl.signalevent.service.impl.workers.ValueGenerator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DataImportServiceImpl implements DataImportService{

    @Override
    public int importSignal(MultipartFile f) {
        return 0;
    }

    @Override
    public int importEvent(MultipartFile f) {
        return 0;
    }

    @Override
    public int importAndOrRule(MultipartFile f) {

        new ValueGenerator().getValues();
        return 0;
    }
}
