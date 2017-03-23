package me.ywork.salary.util;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.heyi.utils.StringTokenizerUtils;


@Component
public class WorkBookUtil {
	/**
	 * 得到列的值
	 * @param cell
	 * @return
	 */
	public  String getCellValue(Cell cell) {
		if(cell==null){
			return "";
		}
        String cellValue = "";
        switch (cell.getCellType()) {
        case HSSFCell.CELL_TYPE_STRING:
            cellValue = cell.getRichStringCellValue().getString().trim();
            break;
        case HSSFCell.CELL_TYPE_NUMERIC:
        	//cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            //cellValue = cell.getStringCellValue();
            HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
            cellValue = dataFormatter.formatCellValue(cell);

//            if (HSSFDateUtil.isCellDateFormatted(cell)) {
//            	Date d = cell.getDateCellValue();
//            }
            break;
        case HSSFCell.CELL_TYPE_BOOLEAN:
            cellValue = String.valueOf(cell.getBooleanCellValue()).trim();
            break;
        case HSSFCell.CELL_TYPE_FORMULA:
           // cellValue = cell.getCellFormula();
        	cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        	cellValue = cell.getStringCellValue();
           // cellValue = cell.getNumericCellValue()+"";
            break;
        default:
            cellValue = "";
        }
      //  cellValue = HtmlEscaper.escapeHTML(cellValue);
        return cellValue;
    }
	
	public static Double getIntegerNum(String value , Short type)throws Exception{
		Double t1 = null;
		if(type == 0){//解析double
			Double t =Double.parseDouble(value);
			t1 = t*100;
		}else if(type == 1){//解析小时 分钟
			String[] buffer =value.split("小时|分钟");
			int i=0;
			for(String s :buffer){
				i++;
				if(buffer.length ==1){
					Integer u=Integer.parseInt(s);
					String st1=String.valueOf(u);
					Integer endIndex=value.indexOf(st1)+st1.length();
					if(endIndex!=value.length()){
						String endString=value.substring(endIndex, value.length());
                        if("小时".equals(endString)){
                        	t1=(double)u*60;
                        }else{
                        	t1=Double.parseDouble(s);
                        }
					}else{
						t1=Double.parseDouble(s);
					}
				}else if(buffer.length == 2){
					if(i == 1){
						t1=Double.parseDouble(s);
					}else{
						t1 = t1*60+Integer.parseInt(s);
					}					
				}
		}
		}else if(type == 2){//解析小时
			String[] buffer =value.split("小时");
			Double t =Double.parseDouble(buffer[0]);
			t1 = t*100;
		}else{//解析天
			String[] buffer =value.split("天");
			Double t =Double.parseDouble(buffer[0]);
			t1 = t*100;
		}
		return t1;
	}
	public static Double getDoubleNum(String value , Short type){
		Double t1 = null;
		if(type == 0){//解析double
			Double t =Double.parseDouble(value);
			t1 = t*100;
		}
		return t1;
	}
//	analyzeActualRowNum
	public static Boolean  analyzeLastRow(WorkBookUtil workBookUtil,Row row){		
		Iterator<Cell> cellIterator = row.cellIterator();
		String value="";
		while(cellIterator.hasNext()){
			Cell cell=cellIterator.next();
			if(cell!=null){
			    String cellValue=workBookUtil.getCellValue(cell);
			    if(cellValue!=null){
			    	value+=cellValue;
			    }
			}
		}
		if(StringUtils.isBlank(value)||value.contains("每天时长=审批单时长/审批单跨越天数")){
            return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	public static void main(String[] args){
		Short type=1;
		try {
			System.out.println(getIntegerNum("2小时",type));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
