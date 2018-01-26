package nju.zxl.signalevent.service.impl;

import nju.zxl.signalevent.eo.HistoryData;
import nju.zxl.signalevent.eo.Signal;
import nju.zxl.signalevent.service.DataImportService;
import nju.zxl.signalevent.service.impl.workers.DataInit;
import nju.zxl.signalevent.service.impl.workers.DataOperation;
import nju.zxl.signalevent.service.impl.workers.ValueGenerator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class DataImportServiceImpl implements DataImportService{

    @Override
    public int importSignal(MultipartFile f) {
        List<Signal> list = new DataInit().getSignalsFromFile(f);
        boolean saveResult = false;
        saveResult=new DataOperation().saveList(list);
        System.out.println(saveResult);
        if(saveResult) {
            return 114514;
        }else{
            return 1;
        }
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

    @Override
    public int importHistoryData(MultipartFile f) {
        List<HistoryData> hdlist = new DataInit().getHistoryDataFromFile(f);
        new DataOperation().saveList(hdlist);
        return 0;
    }
}
