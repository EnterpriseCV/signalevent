package nju.zxl.signalevent.service.impl.workers;

import java.io.File;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import nju.zxl.signalevent.eo.JudgeResult;

public class DataOutput {
	
	public void dataOutput(List<JudgeResult> jrlist) {
		String excelName = "E:judgeeventoutput.xls";  
		try {  
			File excelFile = new File(excelName);  
			// 如果文件存在就删除它  
			if (excelFile.exists())  
				excelFile.delete();  
			// 打开文件  
			WritableWorkbook book = Workbook.createWorkbook(excelFile);  
			// 生成名为“第一页”的工作表，参数0表示这是第一页  
			WritableSheet sheet = book.createSheet(" 第一页 ", 0);  
			// 以及单元格内容为 
			Label label0,label1,label2,label3,label4,label5,label6,label7;
			label0 = new Label(0, 0, "触发信号源文件中序号"); 
			label1 = new Label(1, 0, "触发信号编号"); 
			label2 = new Label(2, 0, "该事件相关信号编号集合");
			label3 = new Label(3, 0, "可能事件编号");
			label4 = new Label(4, 0, "可能事件权值");
			label5 = new Label(5, 0, "最终预测事件编号");
			label6 = new Label(6, 0, "最终预测事件权值");
			label7 = new Label(7, 0, "实际事件");
			
			// 将定义好的单元格添加到工作表中  
			sheet.addCell(label0);
			sheet.addCell(label1);
			sheet.addCell(label2);
			sheet.addCell(label3);
			sheet.addCell(label4);
			sheet.addCell(label5);
			sheet.addCell(label6);
			sheet.addCell(label7);
			 
			int index = 1;
			for(JudgeResult jr:jrlist){
				if(Double.parseDouble(jr.getPossible_event_value())!=0){
					label0 = new Label(0, index, String.valueOf(jr.getTrigger_signal_source_id()));
					label1 = new Label(1, index, String.valueOf(jr.getTrigger_signal_id()));
					label2 = new Label(2, index, jr.getRelated_signals());
					label3 = new Label(3, index, String.valueOf(jr.getPossible_event_id()));
					label4 = new Label(4, index, String.valueOf(jr.getPossible_event_value()));
					label5 = new Label(5, index, String.valueOf(jr.getFinal_event_id()));
					label6 = new Label(6, index, String.valueOf(jr.getFinal_event_value()));
					label7 = new Label(7, index, jr.getActual_event_info());
					sheet.addCell(label0);
					sheet.addCell(label1);
					sheet.addCell(label2);
					sheet.addCell(label3);
					sheet.addCell(label4);
					sheet.addCell(label5);
					sheet.addCell(label6);
					sheet.addCell(label7);
					index++;
				}		
			}
			  			
			// 写入数据并关闭文件  
			book.write();  
			book.close();  
			System.out.println("Excel创建成功");  
		} catch (Exception e) {  
			System.out.println(e);  
		}  
	}  

}
