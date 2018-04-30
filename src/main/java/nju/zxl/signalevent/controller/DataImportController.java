package nju.zxl.signalevent.controller;

import nju.zxl.signalevent.bean.HttpReturnObj;
import nju.zxl.signalevent.model.TriggerSignal;
import nju.zxl.signalevent.service.DataImportService;
import nju.zxl.signalevent.util.JxlUtils;
import nju.zxl.signalevent.util.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping(value = "/data/import")
public class DataImportController {
    @Autowired
    private DataImportService dis;

    @RequestMapping(value = "/{className}")
    public HttpReturnObj importDataToDB(@Param("file")MultipartFile file, @PathVariable("className")String className){
        HttpReturnObj hro = null;
        try{
            String[][] excelArray = JxlUtils.jxlExlToList(file);
            if(excelArray==null){
                return new HttpReturnObj(-1,"文件解析失败");
            }
            Class targetClass = Class.forName(PropertyUtils.getModelPackageName()+"."+className);
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return hro;
    }
}
