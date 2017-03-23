package me.ywork.salary.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.codec.binary.StringUtils;

public class DateUtils {
    public static final String PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";
    public static final String PATTERN_RFC1036 = "EEE, dd-MMM-yy HH:mm:ss zzz";
	
	/**
	 *   将时间戳转化为Date类型
	 * @param monthId 时间戳
	 * @return 转化后的Date类型
	 * @throws ParseException 抛出转化异常
	 */
	public static Date   formatTimeStampToDate(Long monthId) throws ParseException{
		    SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");  
			String d =format.format(monthId);
			Date date = null;
			date = format.parse(d);
	        return date;
	}
	 public static Date parseDate(String dateValue) {
		 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟  
		 Date date = null;
		try {
			date = sdf.parse(dateValue);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		 return date;
	    }
	 
	 
	 
	 /**
	  * 判断两个日期是否在同一个月且是一个月的开始时间和截止时间
	  * 
	  * @param beginDate
	  * @param endDate
	  * @return
	  */
	 public static Boolean analyseDateIsOneMonthBeginAndEnd(Date beginDate,Date endDate){
		 Boolean rs=Boolean.FALSE;
		 //首先判断两个日期是否是同一月，且开始日期要小于截至日期
		 if(beginDate.getMonth()==endDate.getMonth()&&beginDate.getTime()<endDate.getTime()){
			 //再得到该月有多少天
			//int monthDay=getDaysOfMonth(beginDate);
     	//	 if((monthDay-1)==daysOfTwo(beginDate, endDate)){
     			 rs=Boolean.TRUE;
     	//	 }
		 }
		 return rs;
	 }
	 


	 /** 
	  * 取得当月天数 
	  * */  
	 public static int getDaysOfMonth(Date date) {  
	        Calendar calendar = Calendar.getInstance();  
	        calendar.setTime(date);  
	        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);  
	    }  
	  
	 /**
	  * 取得两个日期的间隔时间
	  * 
	  * @param fDate
	  * @param oDate
	  * @return
	  */
	 public static int daysOfTwo(Date fDate, Date oDate) {
	       Calendar aCalendar = Calendar.getInstance();
           aCalendar.setTime(fDate);
	       int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
	       aCalendar.setTime(oDate);
	       int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
	       return day2 - day1;
	    }
	 
	 /**
	  *  将日期重新设置为每月的第一天
	  */
	 public static Date parseToTheFirstDayOfTheYear(Date date){

		 return null;
	 }
	 
	 
	 public static void main(String[] args){
		 String vlaue1="  ";
		 String value2="";
		 if(org.apache.commons.lang.StringUtils.isBlank(value2+vlaue1)){
			 System.out.println("sf");
		 }
//		 Integer totalCount=21;
//		 Integer pageSize=10;
//		System.out.println("pageSize:"+Math.ceil((double)totalCount/(double)pageSize));
//		 String cellName="统计日期：2017-02-02~2017-02-28    报表生成时间：2017-03-09 23:31:50";
//		 StringBuffer buffer = new StringBuffer(cellName);
//		 buffer.replace(13, 15, "01");
//		 cellName=buffer.toString();
//		 String dateValue = cellName.substring(5, 15);
//		 System.out.println(dateValue);
//		 String endDateValue=cellName.substring(16,26);
//		 System.out.println(endDateValue);
//		 Date 	monthTime =DateUtils.parseDate(dateValue);			  
//
//		 Date endDate=DateUtils.parseDate(endDateValue);

		//	System.out.println(analyseDateIsOneMonthBeginAndEnd(monthTime,endDate));

//		 System.out.println(endDate.getMonth()+1);
//		 System.out.println(monthTime.getTime());
//		 System.out.println(endDate.getTime());
		//	int day=daysOfTwo(monthTime, endDate);
	//		Boolean rs=equals(monthTime,endDate);
		// System.out.println(rs);

	 }

}
