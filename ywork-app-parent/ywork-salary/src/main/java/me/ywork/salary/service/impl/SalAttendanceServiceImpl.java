package me.ywork.salary.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.redisson.cache.CacheableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.heyi.utils.IdGenerator;

import me.ywork.context.CallContext;
import me.ywork.oss.OSSObjectService;
import me.ywork.page.Pageable;
import me.ywork.salary.entity.SalCorpAttenEntity;
import me.ywork.salary.entity.SalCorpDeductEntity;
import me.ywork.salary.entity.SalStaffAttenDayEntity;
import me.ywork.salary.enumeration.SalDeductType;
import me.ywork.salary.enumeration.SalExcelTimeType;
import me.ywork.salary.enumeration.SalUsePageType;
import me.ywork.salary.exception.ExcelUnNormalException;
import me.ywork.salary.exception.MonthUnNormalException;
import me.ywork.salary.exception.NumOutBoundsException;
import me.ywork.salary.exception.SalFieldRepeatedException;
import me.ywork.salary.exception.StaffExistException;
import me.ywork.salary.exception.StaffNotExistException;
import me.ywork.salary.model.SalAttenExcelModel;
import me.ywork.salary.model.SalCorpAttenModel;
import me.ywork.salary.model.SalCorpDeductModel;
import me.ywork.salary.model.SalCustomizedAttenFieldModel;
import me.ywork.salary.model.SalStaffAttenDayModel;
import me.ywork.salary.model.SalStaffAttendanceModel;
import me.ywork.salary.model.SalStaffBaseInfoModel;
import me.ywork.salary.model.SalSysFieldItemModel;
import me.ywork.salary.model.SalUpdateMutiStaffModel;
import me.ywork.salary.repository.SalAttendanceRepository;
import me.ywork.salary.repository.SalRuleRepository;
import me.ywork.salary.repository.SalStaffBaseInfoRepository;
import me.ywork.salary.service.SalAttendanceService;
import me.ywork.salary.service.SalBaseService;
import me.ywork.salary.service.SalCalcuSalService;
import me.ywork.salary.util.DateUtils;
import me.ywork.salary.util.WorkBookUtil;

/**
 * Created by xiaobai on 2017/1/11.
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class SalAttendanceServiceImpl   implements SalAttendanceService {

	@Autowired
	private SalAttendanceRepository salAttendanceRepository;
	@Autowired
	private SalStaffBaseInfoRepository salStaffBaseInfoRepository;
	@Autowired
	private SalRuleRepository salRuleRepository;
	@Autowired 
	private WorkBookUtil workBookUtil;
	// 缓存机制
	@Autowired
	private CacheableService cacheableService;
	@Autowired
	private SalCalcuSalService saCalcuSalService;
	@Autowired
	private SalBaseService salBaseService;
	private static final Logger logger = LoggerFactory.getLogger(SalAttendanceServiceImpl.class);

	@Override
	public List<SalCorpAttenModel> getAllMonthesAttendanceData(String corpId) {
		if (StringUtils.isBlank(corpId)) {
			throw new IllegalArgumentException("getAllMonthesAttendanceData param corpId is null or empty");
		}	 
		// 去持久层获得所有月度的考勤数据
		List<SalCorpAttenModel> attendanceModels  = salAttendanceRepository.getAllMonthesAttendanceData(corpId);
		return attendanceModels;
	}

	@Override
	public SalAttenExcelModel getAllStaffAttendanceByReportId(String corpId ,String reportId, Pageable pageable) {
		if (StringUtils.isBlank(reportId)) {
			throw new IllegalArgumentException
			("getAllStaffAttendanceByMonthId param reportId is null or empty");
		}
		if (pageable == null) {
			throw new IllegalArgumentException("getAllStaffAttendanceByMonthId param pageable is null.");
		}
       SalAttenExcelModel salAttenExcelModel =new SalAttenExcelModel();
       List<String> attenTitles=new ArrayList<>();
		List<SalStaffAttendanceModel> staffAttendanceModels = null;
		// 去持久层获得当月度所有员工分页的考勤数据
		Integer pageSize = pageable.getPageSize();
		Integer pageNo = pageable.getPageNo();
		Integer totalCount =0;
		Integer beginNum = (pageNo-1)*pageSize;
		Integer endNum =pageSize;
		List<SalSysFieldItemModel> salSysFieldItemModels = salStaffBaseInfoRepository.getCorpVacations(corpId);//得到企业所有的假期
		for(SalSysFieldItemModel salModel:salSysFieldItemModels){
			attenTitles.add(salModel.getItemName());
		}
		staffAttendanceModels = salAttendanceRepository.getAllStaffAttendanceByReportId(reportId,corpId,null, beginNum ,endNum,SalUsePageType.UsePage.getCode());		
		for(SalStaffAttendanceModel attenModel:staffAttendanceModels){		
           Map<String,Double> attenDayList=new HashMap<String,Double>();
			List<SalCustomizedAttenFieldModel> attenDays = new ArrayList<SalCustomizedAttenFieldModel>();
			SalCustomizedAttenFieldModel salCustomizedAttenFieldModel = null;
			String staffId = attenModel.getDingStaffId();		
			//得到员工所有的假期
			List<SalStaffAttenDayModel> salStaffAttenDayModels = salAttendanceRepository.getSalStaffAttenDayInfos(corpId, reportId, staffId);
			for(SalSysFieldItemModel m:salSysFieldItemModels ){
				for(SalStaffAttenDayModel day:salStaffAttenDayModels){
					if(m.getItemId().equals(day.getFieldId())){
						attenDayList.put(m.getItemId(),day.getFieldDay());
					}
				}
			}
			for(SalSysFieldItemModel m:salSysFieldItemModels){
				salCustomizedAttenFieldModel = new SalCustomizedAttenFieldModel();
				Double attenDay=0.0;
				if(attenDayList.containsKey(m.getItemId())){
					attenDay=attenDayList.get(m.getItemId());
				}

				salCustomizedAttenFieldModel.setFieldDay(attenDay);
				attenDays.add(salCustomizedAttenFieldModel);
			}
			attenModel.setDetails(attenDays);
			attenModel.setDeptName(salBaseService.getStaffAllDeptName(corpId, staffId));
		}
		// 去持久层得到当月度所有员工的考勤数据的总数
		 totalCount = salAttendanceRepository.getAllStaffAttendanceCountByReportId(reportId);
		salAttenExcelModel.setDetailTitle(attenTitles);
		salAttenExcelModel.setStaffAttens(staffAttendanceModels);
        salAttenExcelModel.setTotalCount(totalCount);
        salAttenExcelModel.setPageSize(pageable.getPageSize());
        salAttenExcelModel.setPageNo(pageable.getPageNo());
        if(totalCount<pageSize&&totalCount>0){
	    	  salAttenExcelModel.setTotalPages(1);
	    }else{
	    	  salAttenExcelModel.setTotalPages((int)Math.ceil((double)totalCount/(double)pageSize));
	    }
		return salAttenExcelModel;
	}

	@Override
	public List<SalStaffAttendanceModel> getMutiStaffAttendanceDetail(List<String> staffIds, String reportId) {
		if (staffIds == null) {
			throw new IllegalArgumentException("getMutiStaffAttendanceDetail param staffIds is null.");
		}
		if (staffIds.isEmpty()) {
			throw new IllegalArgumentException("getMutiStaffAttendanceDetail param staffIds is empty.");
		}
		if (StringUtils.isBlank(reportId)) {
			throw new IllegalArgumentException("getMutiStaffAttendanceDetail param reportId is null.");
		}

		List<SalStaffAttendanceModel> staffAttendanceModels = null;
		staffAttendanceModels = salAttendanceRepository.getMutiStaffAttendanceDetail(staffIds, reportId);
		return staffAttendanceModels;
	}

	@Override
	public Boolean updateMutiStaffAttendance(List<SalStaffAttendanceModel> staffAttendances) {
		if (staffAttendances == null) {
			throw new IllegalArgumentException("updateMutiStaffAttendance param staffAttendances is null.");
		}
		if (staffAttendances.isEmpty()) {
			throw new IllegalArgumentException("updateMutiStaffAttendance param staffAttendances is empty.");
		}
		int size = 0;
		SalCorpAttenEntity staffAttendance = null;
		for (SalStaffAttendanceModel staffAttendanceModel : staffAttendances) {
			staffAttendance = salAttendanceRepository.getStaffAttendanceEntityById(staffAttendanceModel.getId());
			if (staffAttendance != null) {
				staffAttendance.setId(staffAttendanceModel.getId());
				staffAttendance.setAttendanceDays(staffAttendanceModel.getAttendanceDays());
				staffAttendance.setEarlyLeaveHours(staffAttendanceModel.getEarlyLeaveHours());
				staffAttendance.setEarlyLeaveTimes(staffAttendanceModel.getEarlyLeaveTimes());
				staffAttendance.setLateHours(staffAttendanceModel.getLateHours());
				staffAttendance.setLateTimes(staffAttendanceModel.getLateTimes());
				staffAttendance.setRestDays(staffAttendanceModel.getRestDays());
				staffAttendance.setSeriousLateHours(staffAttendanceModel.getSeriousLateHours());
				staffAttendance.setWorkAbsenceTimes(staffAttendanceModel.getWorkAbsenceTimes());
				staffAttendance.setSeriousLateTimes(staffAttendanceModel.getSeriousLateTimes());
				staffAttendance.setOffWorkAbsenceTimes(staffAttendanceModel.getOffWorkAbsenceTimes());
				staffAttendance.setWorkHours(staffAttendanceModel.getWorkHours());
				staffAttendance.setUnWorkDays(staffAttendanceModel.getUnWorkDays());
				staffAttendance.setUnWorkLateDays(staffAttendanceModel.getUnWorkLateDays());
			}
			if (salAttendanceRepository.updateMutiStaffAttendance(staffAttendance) > 0) {
				size++;
			}
		}
		if (size == staffAttendances.size()) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	@Override
	public boolean deleteById(CallContext callContext, String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public SalAttenExcelModel parseAttenExcel(CallContext callContext , String fileId) throws MonthUnNormalException, 
	StaffNotExistException, ExcelUnNormalException, SalFieldRepeatedException {
		  if(callContext==null){
			  throw new IllegalArgumentException("parseAttenExcel -- param callContext is null ");
		  }
		 if(StringUtils.isBlank(fileId)){
			throw new IllegalArgumentException();
		 }
		 
	      String corpId = callContext.getCorpId();
	      String userId = callContext.getUserId();
	      
			if(StringUtils.isBlank(corpId)){
				throw new IllegalArgumentException();
			}
			if(StringUtils.isBlank(userId)){
				throw new IllegalArgumentException();				
			}
			
			Date monthTime = null;//考勤报表的月份
			Workbook wb = null;
		  try {
				InputStream instream = OSSObjectService.getObject("alidsalarybill", fileId);		//得到用户上传文件的字节流
				if(fileId.endsWith(".xlsx")){//必须要符合excel表的类型
					try{
						wb = new XSSFWorkbook(instream);
					}catch(Exception e){
						wb = new HSSFWorkbook(instream);
					}
				}else{
					try{
						wb = new HSSFWorkbook(instream);
					}catch(Exception e){
						wb = new XSSFWorkbook(instream);
					}
				}}catch(Exception e){
					logger.error("parseAttenExcel--".concat(corpId).concat("--fileId:").concat(fileId).concat("--不存在！"));
					return new SalAttenExcelModel();
				}
		       if(wb==null){//若excel文件为空
		    	   logger.warn("parseAttenExcel --".concat(corpId).concat("--XSSFWorkbook is null"));
		    	   return new SalAttenExcelModel();
		       }
				List<SalStaffAttendanceModel> staffAttendanceModels = new ArrayList<SalStaffAttendanceModel>();//用于存储用户上传的所有的员工的考勤数据
				List<SalCustomizedAttenFieldModel> salCustomizedFields =null;//用于存储企业上传的excel表中请假字段的员工的天数
				SalStaffAttendanceModel attendance = null; //存储员工考勤的数据包
				List<String> cellNames =new ArrayList<String>();//用于存储用户上传的考勤字段的名称
				Integer successCount =0;//上传成功的个数
				Integer failCount =0; //上传失败的个数
				Integer rowNum=0;//excel表的行数
				Map<String,Integer> titleAddress=new HashMap<String,Integer>();//字段的地址，一个字段对应一个cell下标
				Integer vocationDayIndex=null;//请假字段所在的下标值
				Set<String> staffIdSet = new HashSet<String>();//在一次上传的过程中，不允许出现重复的员工钉钉ID
				for(int sheetCount=0;sheetCount<wb.getNumberOfSheets();sheetCount++){//遍历所有的excel表
					if(sheetCount>0){//仅仅限于一张excel表存在一个sheet
						break;
					}
					Sheet sheet = wb.getSheetAt(sheetCount);//定位到对应的sheet
					int totalRowCount = sheet.getLastRowNum();//excel表中sheet的row的总数
					logger.info("parseAttenExcel".concat(corpId).concat("--解析考勤数据的总行数是：".concat(""+totalRowCount)));
			
                    Boolean findVocationIndex=Boolean.FALSE;
					for( rowNum=0;rowNum<=totalRowCount;rowNum++){//遍历所有的excel中所在的行
							if(rowNum<=3){
								if(rowNum==1){//若是下标为1的行，分析出该excel表所在的月份
									Date endDate=null;
									try{
										Row row0 = sheet.getRow(rowNum);								
										String cellName=workBookUtil.getCellValue(row0.getCell(0));
										 StringBuffer buffer = new StringBuffer(cellName);
										 buffer.replace(13, 15, "01");
										 cellName=buffer.toString();
										 
										String dateValue = cellName.substring(5, 15);
										String endDateValue=cellName.substring(16,26);
										monthTime =DateUtils.parseDate(dateValue);			  
										 endDate=DateUtils.parseDate(endDateValue);
									}catch(Exception e){
										 throw new MonthUnNormalException("excel表格不符合规范！");
									}
									if(DateUtils.analyseDateIsOneMonthBeginAndEnd(monthTime, endDate)==Boolean.FALSE){//判断这两个日期是否是同一个月且是一月当中的开始和截止时间
									    throw new MonthUnNormalException("统计日期时间段必须是同一个月!");
									}
								}
					            try{
								if(rowNum==2){//若是下标为2的行，分析出该excel表所在请假字段所在的cell下标值
									Row theSecondRow =sheet.getRow(2);
									Iterator<Cell> cellIterator = theSecondRow.cellIterator();
									   int beginNum=0;
									    while(cellIterator.hasNext()){
											Cell cell = cellIterator.next();
											String cellTitle =workBookUtil.getCellValue(cell);
											titleAddress.put(cellTitle , beginNum);
											if("请假".equals(cellTitle)&&findVocationIndex==Boolean.FALSE){
												vocationDayIndex =beginNum;
												findVocationIndex=Boolean.TRUE;
											}
											beginNum++;
									    }
								}
								if(rowNum==3){//若是下标为3的行，将请假字段的名字都依次放入cellNames集合中
									Row titleRow = sheet.getRow(rowNum);
									Iterator<Cell> cellIterator = titleRow.cellIterator();
								    int beginNum=0;
								    while(cellIterator.hasNext()){
										Cell cell = cellIterator.next();
										String cellTitle = workBookUtil.getCellValue(cell);
										titleAddress.put(cellTitle , beginNum);
									   beginNum++;
								   }
								    if(vocationDayIndex !=null){
										for(int z=vocationDayIndex;z<titleRow.getLastCellNum();z++){//将请假字段的名字都依次放入此集合中
											String cellName=workBookUtil.getCellValue(titleRow.getCell(z));
											if(StringUtils.isNotBlank(cellName)){
												if(cellNames.contains(cellName)){
													throw new SalFieldRepeatedException();
												}
											   cellNames.add(cellName);//得到名字											   
											}
										}				
								    }
								}
					            }
								catch(SalFieldRepeatedException e){
									throw new SalFieldRepeatedException("上传excel不符合规范,考勤请假字段重复！");
								}
					        	catch(Exception e){
									throw new  ExcelUnNormalException("上传excel不符合规范！");
								}
								continue;
							}
						
							Row row = sheet.getRow(rowNum);				
							if(row==null){
								break;
							}
							SalStaffBaseInfoModel salStaff = null;//存储员工信息的数据包
							String staffId =null;//员工钉钉ID	
						    attendance = new SalStaffAttendanceModel();//新建一个存储员工考勤的数据包

						    attendance.setRowNum(rowNum+1);
							staffId = workBookUtil.getCellValue(row.getCell(titleAddress.get("userId")));//找到员工钉钉ID
							if(StringUtils.isBlank(staffId)){
								if(WorkBookUtil.analyzeLastRow(workBookUtil,row)==Boolean.FALSE){
								    throw new StaffNotExistException("员工USERID不能为空(第".concat(""+rowNum+"行)"));
								}else{
									logger.info("parseAttenExcel --该企业".concat(corpId).
									concat("导入的考勤数据有问题，最后一行变为上一行").concat("--总行数是：").concat(""+(rowNum)));
									rowNum=rowNum-1;
									break;
								}
							}
							try{		
								salCustomizedFields = new ArrayList<SalCustomizedAttenFieldModel>();
								SalCustomizedAttenFieldModel salCus =null;			
								if(vocationDayIndex!=null){
									for(int u =vocationDayIndex;u<vocationDayIndex+cellNames.size();u++){//循环得到员工的请假天数
										salCus= new SalCustomizedAttenFieldModel();
										String field=workBookUtil.getCellValue(row.getCell(u));
										Double fieldDay=null;
										String fieldName=null;
										if(StringUtils.isNotBlank(field)){
										  fieldDay=workBookUtil.getIntegerNum(field,SalExcelTimeType.DAY.getCode());
										}else{
											fieldDay=0.0;
										}										
										salCus.setFieldDay(fieldDay);//得到天数	
										 salCus.setFieldName(workBookUtil.getCellValue(sheet.getRow(3).getCell(u)));
										salCustomizedFields.add(salCus);
									}
								}
							salStaff =salStaffBaseInfoRepository.getOrgStaffInfo(corpId, staffId);//得到员工的基本信息的数据包
			               if(salStaff ==null){//若员工基本信息的数据包为空，则抛出员工不存在的异常
								throw new StaffNotExistException();
							}
			               if(staffIdSet.contains(staffId)){//若在存储员工钉钉ID的集合中存在该钉钉ID，则抛出员工存在的异常
			            	   throw new StaffExistException();
			               }else{
			                  staffIdSet.add(staffId);//把员工的钉钉ID加入到存储员工钉钉ID的集合中
			               }
							attendance.setStaffName(workBookUtil.getCellValue(row.getCell(titleAddress.get("姓名"))));								
							attendance.setDeptName(workBookUtil.getCellValue(row.getCell(titleAddress.get("部门"))));
							Double attenDays= workBookUtil.getIntegerNum(workBookUtil.getCellValue(row.getCell(titleAddress.get("出勤天数"))),SalExcelTimeType.DOUBLE.getCode());
							if(attenDays!=null){
								if(attenDays<0||attenDays>3100){
									throw  new NumOutBoundsException("出勤天数");
								}
							}
							attendance.setAttendanceDays(attenDays);//出勤天数			
							Double restDays=workBookUtil.getIntegerNum(workBookUtil.getCellValue(row.getCell(titleAddress.get("休息天数"))),SalExcelTimeType.DOUBLE.getCode());
							if(restDays!=null){
								if(restDays<0||restDays>3100){
									throw  new NumOutBoundsException("休息天数");
								}
							}
									attendance.setRestDays(restDays);//休息天数
									attendance.setWorkHours(workBookUtil.getIntegerNum(workBookUtil.getCellValue(row.getCell(titleAddress.get("工作时长"))),SalExcelTimeType.HOURMINUTE.getCode()));
									attendance.setLateTimes(Double.parseDouble(workBookUtil.getCellValue(row.getCell(titleAddress.get("迟到(次)")))));//迟到次数
									attendance.setLateHours(workBookUtil.getIntegerNum(workBookUtil.getCellValue(row.getCell(titleAddress.get("迟到时长"))),SalExcelTimeType.HOURMINUTE.getCode()));//迟到时长
									attendance.setSeriousLateTimes(Double.parseDouble(workBookUtil.getCellValue(row.getCell(titleAddress.get("严重迟到(次)")))));//严重迟到次数
									attendance.setSeriousLateHours(workBookUtil.getIntegerNum(workBookUtil.getCellValue(row.getCell(titleAddress.get("严重迟到时长"))),SalExcelTimeType.HOURMINUTE.getCode()));//严重迟到时长
									attendance.setEarlyLeaveTimes(Double.parseDouble(workBookUtil.getCellValue(row.getCell(titleAddress.get("早退(次)")))));//早退次数
									attendance.setEarlyLeaveHours(workBookUtil.getIntegerNum(workBookUtil.getCellValue(row.getCell(titleAddress.get("早退时长"))),SalExcelTimeType.HOURMINUTE.getCode()));//早退时长
									attendance.setWorkAbsenceTimes(Double.parseDouble(workBookUtil.getCellValue(row.getCell(titleAddress.get("上班缺卡")))));//上班缺卡次数
									attendance.setOffWorkAbsenceTimes(Double.parseDouble(workBookUtil.getCellValue(row.getCell(titleAddress.get("下班缺卡")))));//下班缺卡次数
									attendance.setUnWorkDays(Double.parseDouble(workBookUtil.getCellValue(row.getCell(titleAddress.get("一天未打卡")))));//一天未打卡次数
									Integer unWorkLateDayIndex=titleAddress.get("旷工迟到");
									attendance.setUnWorkLateDays(Double.parseDouble(workBookUtil.getCellValue(row.getCell(unWorkLateDayIndex))));//旷工迟到次数
									attendance.setCorpId(corpId);
									attendance.setDingStaffId(staffId);							
									attendance.setJobNum(workBookUtil.getCellValue(row.getCell(titleAddress.get("工号"))));
							}
							catch(StaffNotExistException e){
								attendance.setFailReason("该人员不在组织架构！");
								logger.warn("parseAttenExcel --".concat(corpId).concat("--").concat(staffId).concat("--该人员不在组织架构！"));
								attendance.setStaffName(workBookUtil.getCellValue(row.getCell(titleAddress.get("姓名"))));
								attendance.setDeptName(workBookUtil.getCellValue(row.getCell(titleAddress.get("部门"))));
								attendance.setJobNum(workBookUtil.getCellValue(row.getCell(titleAddress.get("工号"))));
								failCount++;
							}
							catch(StaffExistException e){
  					           attendance.setFailReason("导入了"+salStaff.getStaffName()+"的重复数据！");
  					        	logger.warn("parseAttenExcel --".concat(corpId).concat("--").concat(staffId).concat("--导入了"+salStaff.getStaffName()+"的重复数据！"));
								attendance.setStaffName(workBookUtil.getCellValue(row.getCell(titleAddress.get("姓名"))));
								attendance.setDeptName(workBookUtil.getCellValue(row.getCell(titleAddress.get("部门"))));
								attendance.setJobNum(workBookUtil.getCellValue(row.getCell(titleAddress.get("工号"))));
							    failCount++;
							}
							catch(NumOutBoundsException e){
					            attendance.setFailReason(e.getMessage()+"超过了范围(0-31)");
  					        	logger.warn("parseAttenExcel --".concat(corpId).concat("--").concat(staffId).concat(e.getMessage()+"超过了范围(0-31)"));
								attendance.setStaffName(workBookUtil.getCellValue(row.getCell(titleAddress.get("姓名"))));
								attendance.setDeptName(workBookUtil.getCellValue(row.getCell(titleAddress.get("部门"))));
								attendance.setJobNum(workBookUtil.getCellValue(row.getCell(titleAddress.get("工号"))));
							    failCount++;
							}
							catch(Exception e){
								attendance.setFailReason("考勤字段的单位不符合规定！");
								logger.warn("parseAttenExcel --".concat(corpId).concat("--").concat(staffId).concat("考勤字段的单位不符合规定！"));
								attendance.setStaffName(workBookUtil.getCellValue(row.getCell(titleAddress.get("姓名"))));
								attendance.setDeptName(workBookUtil.getCellValue(row.getCell(titleAddress.get("部门"))));
								attendance.setJobNum(workBookUtil.getCellValue(row.getCell(titleAddress.get("工号"))));
								attendance.setIsUploadSuccess(Boolean.FALSE);
								failCount++;
							}
							if(salCustomizedFields!=null){
							   attendance.setDetails(salCustomizedFields);
							}
							staffAttendanceModels.add(attendance);
					}
				}		

				SalAttenExcelModel salAttenExcelModel =new SalAttenExcelModel();
				salAttenExcelModel.setDetailTitle(cellNames);
				salAttenExcelModel.setStaffAttens(staffAttendanceModels);
			    salAttenExcelModel.setFailCount(failCount);
			    successCount = rowNum-failCount-4;
			    Integer   totalCount = successCount + failCount;
			    salAttenExcelModel.setSuccessCount(successCount);
			    String  cachekey = corpId+userId+System.currentTimeMillis();
			    salAttenExcelModel.setCacheKey(cachekey);
			    salAttenExcelModel.setPageSize(50);//初始化考勤的信息的一页最多显示50条数据
			    salAttenExcelModel.setPageNo(1);;//初始化考勤的信息的进入显示第一页的数据
			    salAttenExcelModel.setTotalCount(totalCount);//设置考勤信息的总数
		    	logger.info("parseAttenExcel -- ".concat(corpId).concat("--上传考勤数据的总数为:").concat(""+totalCount+"")
		    			.concat("--成功：").concat(""+successCount+"").concat("--失败：").concat(""+failCount+""));
			    if(totalCount<50&&totalCount>0){//若总数<50,则设置总的页数为1
			    	  salAttenExcelModel.setTotalPages(1);
			    }else{
			    	  salAttenExcelModel.setTotalPages(totalCount/50+1);
			    }
			  
			    //去数据库查询是否存在该月的考勤数据
			    SalCorpAttenModel  attenRs = salAttendanceRepository.getAttenByMonthTime(corpId  , monthTime);
			    salAttenExcelModel.setIsExist(attenRs == null?Boolean.FALSE:Boolean.TRUE);//向前端传输是否存在的标志器
			    if(attenRs != null){//存在，则在数据包中设置考勤报表的ID
			     	 String reportId = attenRs.getId();
			    	logger.info("parseAttenExcel -- ".concat(corpId).
			    			concat("-- 上传考勤数据 中数据库中不存在".concat(""+monthTime+"").concat("的考勤表，初始化该考勤表的ID为：".concat(reportId))));		
			    	salAttenExcelModel.setReportId(reportId);
			    }
			    salAttenExcelModel.setMonthTime(monthTime);//存储考勤报表所在的月份
				/**
				 *  先把其缓存到redis中
				 */
				if(StringUtils.isNotBlank(salAttenExcelModel.getCacheKey())){
					 salAttenExcelModel.setStaffAttens(staffAttendanceModels);
					cacheableService.setRawObjectInCache(cachekey , salAttenExcelModel, 1, TimeUnit.DAYS);
					logger.info("parseAttenExcel -- ".concat(corpId).concat("REDIS 存储考勤数据成功,cahcekey是:"+salAttenExcelModel.getCacheKey()));
				}
			    if(totalCount>50){//若总数大于50，则截取50条数据
			          logger.info("parseAttenExcel -- ".concat(corpId).concat("--上传考勤数据 总数大于50，截取50条数据"));
				  	  staffAttendanceModels = salAttenExcelModel.getStaffAttens().subList(0, 50);
				  	  salAttenExcelModel.setStaffAttens(staffAttendanceModels);
			    }
	        	return salAttenExcelModel;
		  }

	@Override
	public Boolean commitAttenExcel(CallContext callContext, String cacheKey) {
		if(callContext == null){
			throw new IllegalArgumentException("commitAttenExcel param  callContext is null");
		}
		if(StringUtils.isBlank(cacheKey)){
			throw new IllegalArgumentException("commitAttenExcel param  cacheKey is null or empty");
		}
		String corpId = callContext.getCorpId();
		if(StringUtils.isBlank(corpId)){
			throw new IllegalArgumentException("commitAttenExcel param  corpId  is null or empty");
		}
		SalAttenExcelModel salAttenExcelModel = (SalAttenExcelModel) cacheableService.getRawObjectFromCache(cacheKey);
		if(salAttenExcelModel==null){//若缓存文件不存在
			logger.error("commitAttenExcel".concat(corpId).concat("根据").concat(cacheKey).concat("找到的缓存文件为空！"));
			return Boolean.FALSE;
		}
		Date monthTime =salAttenExcelModel.getMonthTime();
	    Date nowDate = new Date();
		List<SalStaffAttendanceModel> staffAttens = salAttenExcelModel.getStaffAttens();
		String reportId=salAttenExcelModel.getReportId();
		Boolean isExist=salAttenExcelModel.getIsExist();
		if(isExist==Boolean.TRUE){//如果已经存在
			logger.info("commitAttenExcel --".concat(corpId).concat("已经存在").concat(reportId).concat("的考勤表！"));;
		}else{
			logger.info("commitAttenExcel --".concat(corpId).concat("不存在")
					.concat(""+monthTime).concat("该月份的考勤表，开始对考勤报表进行初始化！"));;
			/**
			 *   执行插入企业报表的操作
			 */
		    SalCorpAttenModel salCorpAttenModel =new SalCorpAttenModel();
		    reportId = IdGenerator.newId();
		    salCorpAttenModel.setCorpId(corpId);
		    salCorpAttenModel.setCreateDate(nowDate);
		    salCorpAttenModel.setModifiedDate(nowDate);
		    salCorpAttenModel.setMonthTime(salAttenExcelModel.getMonthTime());
		    salCorpAttenModel.setId(reportId);
    	    if(salAttendanceRepository.insertCorpAttenReportData(salCorpAttenModel)==0){//若初始化考勤表失败
    	    	logger.error("commitAttenExcel".concat(corpId).concat("插入").concat(reportId).concat("考勤表失败！"));
    			return Boolean.FALSE;
    	    }
			}
	        SalCorpAttenEntity salCorpAttenEntity =null;
		    List<String> detailVocationTitles = salAttenExcelModel.getDetailTitle();//员工的请假字段
		    //去数据库查询该企业相关的请假类型
		    List<SalSysFieldItemModel> corpTitles = salStaffBaseInfoRepository.getCorpVacations(corpId);
		    List<String> titles = new ArrayList<String>();
		    Map<String,String> titleMap =new HashMap<String,String>();
		    for(SalSysFieldItemModel salSysFieldItemModel:corpTitles){
		    	String itemName =salSysFieldItemModel.getItemName();
		    	titles.add(itemName);
		    	titleMap.put(itemName, salSysFieldItemModel.getItemId());
		    }
		    HashSet<String> hashNotExistVocationSets=new HashSet<String>();
		    Map<String,String> existVocationMaps=new HashMap<String,String>();
		    for(String s :detailVocationTitles){//判断员工的请假类型是否在企业的请假类型中存在，不存在则加入到要新增企业考勤请假字段的集合中
		    	if(!titles.contains(s)){
		    		hashNotExistVocationSets.add(s);
		    	}else{
		    		existVocationMaps.put(s, titleMap.get(s));
		    	}
		    }           
			   SalSysFieldItemModel salSysFieldItemModel = null;
			    if(hashNotExistVocationSets.isEmpty()==Boolean.FALSE){//如果存在新增的请假类型，数据库中增加考勤请假字段
			    	logger.error("commitAttenExcel".concat(corpId).concat("准备新增考勤请假的字段"));
			        Iterator<String> iterator=	hashNotExistVocationSets.iterator();
			    	while(iterator.hasNext()){
			         	String itemName =	iterator.next();
			        	//向企业自定义的字段中增加请假类型的字段
				    	salSysFieldItemModel =new SalSysFieldItemModel();
				    	String fieldId=IdGenerator.newId();
				    	salSysFieldItemModel.setItemId(fieldId);
				    	salSysFieldItemModel.setItemName(itemName);
				    	salSysFieldItemModel.setItemValue(0.0);
				    	salSysFieldItemModel.setCreateDate(nowDate);
				    	salSysFieldItemModel.setModifiedDate(nowDate);
				    	salSysFieldItemModel.setCorpId(corpId);
				    	salSysFieldItemModel.setDeductType(SalDeductType.PERCENT.getCode());
				    	if(salStaffBaseInfoRepository.addNewAttendanceVacationField(salSysFieldItemModel)>0)//增加字段 
				    	{
				    		existVocationMaps.put(itemName, fieldId);
				    	}
			    	}
			    }
			    SalStaffAttenDayEntity salStaffAttenDay = null;
			    logger.error("commitAttenExcel".concat(corpId).concat("准备覆盖或新增员工的考勤数量：".concat(""+staffAttens.size()+"")));
		    	for(SalStaffAttendanceModel staffAtten:staffAttens){
		    		if(staffAtten.getFailReason() !=null){
		    			continue;
		    		}
		    		 String dingStaffId=staffAtten.getDingStaffId();
		    		/**
		    		 * 先删除该月该员工的考勤数据和对应的天数
		    		 */
		    		if(salAttendanceRepository.getStaffAttenDataUnderReportId(reportId,dingStaffId)!=null){
		    			/**
		    			 * 删除员工的考勤数据
		    			 */
		    			salAttendanceRepository.deleteStaffAttenDataUnderReportId(reportId, dingStaffId);
		    			/**
		    			 * 删除员工的天数
		    			 */
		    			salAttendanceRepository.deleteStaffAttenDayUnderReportId(reportId, dingStaffId);
		    		}
		    		salCorpAttenEntity = new SalCorpAttenEntity();
		    		salCorpAttenEntity.setId(IdGenerator.newId());
		    		salCorpAttenEntity.setAttendanceDays(staffAtten.getAttendanceDays());
		    		salCorpAttenEntity.setEarlyLeaveHours(staffAtten.getEarlyLeaveHours());
		    		salCorpAttenEntity.setEarlyLeaveTimes(staffAtten.getEarlyLeaveTimes());
		    		salCorpAttenEntity.setLateHours(staffAtten.getLateHours());
		    		salCorpAttenEntity.setLateTimes(staffAtten.getLateTimes());
		    		salCorpAttenEntity.setRestDays(staffAtten.getRestDays());
		    		salCorpAttenEntity.setSeriousLateHours(staffAtten.getSeriousLateHours());
		    		salCorpAttenEntity.setWorkAbsenceTimes(staffAtten.getWorkAbsenceTimes());
		    		salCorpAttenEntity.setSeriousLateTimes(staffAtten.getSeriousLateTimes());
		    		salCorpAttenEntity.setOffWorkAbsenceTimes(staffAtten.getOffWorkAbsenceTimes());
		    		salCorpAttenEntity.setWorkHours(staffAtten.getWorkHours());
		    		salCorpAttenEntity.setUnWorkDays(staffAtten.getUnWorkDays());
		    		salCorpAttenEntity.setUnWorkLateDays(staffAtten.getUnWorkLateDays());
		    		salCorpAttenEntity.setMonthTime(monthTime);
		    		salCorpAttenEntity.setModifiedDate(nowDate);
		    		salCorpAttenEntity.setCreateDate(nowDate);
		    		salCorpAttenEntity.setAttenReportId(reportId);
		    		salCorpAttenEntity.setDingStaffId(dingStaffId);
		    		salCorpAttenEntity.setCorpId(corpId);
		    		List<SalCustomizedAttenFieldModel>  salCustomizedAttenFieldModels =staffAtten.getDetails();//给员工考勤请假类型的字段赋值
		    		if(salCustomizedAttenFieldModels!=null){
			    		for(int i=0;i<salCustomizedAttenFieldModels.size();i++){
			    			SalCustomizedAttenFieldModel  salCustomizedAttenFieldModel =	salCustomizedAttenFieldModels.get(i);
			    			Double attenDay=salCustomizedAttenFieldModel.getFieldDay();
			    			if(attenDay==null||attenDay==0){
			    				continue;
			    			}
			    			String fieldId = existVocationMaps.get(salCustomizedAttenFieldModel.getFieldName());//得到企业请假字段的主键
			    			//增加企业员工的考勤请假字段的天数值
			    			salStaffAttenDay = new SalStaffAttenDayEntity();
			    			salStaffAttenDay.setId(IdGenerator.newId());
			    			salStaffAttenDay.setModifiedDate(nowDate);
			    			salStaffAttenDay.setCorpId(corpId);
			    			salStaffAttenDay.setCreateDate(nowDate);
			    			salStaffAttenDay.setDingStaffId(dingStaffId);
			    			salStaffAttenDay.setFieldId(fieldId);
			    			salStaffAttenDay.setFieldDay(attenDay);
			    			salStaffAttenDay.setAttenReportId(reportId);
	       	    			salAttendanceRepository.insertStaffAttenDay(salStaffAttenDay);
			    		}
		    		}
		  	      salAttendanceRepository.insertStaffAttenData(salCorpAttenEntity);
		    	}
	    	    /**
				 * 重新计算企业员工的工资和薪资报表的总工资
				 */		
				saCalcuSalService.calcuSalReportAutomatic(corpId);    
			   return Boolean.TRUE;
	}

	@Override
	public SalUpdateMutiStaffModel getCorpApproveFieldList(String corpId) {
		if(StringUtils.isBlank(corpId)){
			throw new IllegalArgumentException("getCorpApproveFieldList param corpId is null or empty!");
		}
		SalCorpDeductModel salCorpDeduct=salRuleRepository.getCorpdeductRule(corpId);
		if(salCorpDeduct == null){//若扣款规则为空，则要初始化扣款规则
			salBaseService.initCorpAttenDeduct(corpId);
			salCorpDeduct =salRuleRepository.getCorpdeductRule(corpId);
		}
		List<SalSysFieldItemModel>  saList = salStaffBaseInfoRepository.getCorpVacations(corpId);
		SalUpdateMutiStaffModel salCorpApproveList = new SalUpdateMutiStaffModel();
		salCorpApproveList.setSaFieldList(saList);
		salCorpApproveList.setSalDeduct(salCorpDeduct);
		return salCorpApproveList;
	}

	@Override
	public Boolean updateCorpApproveField(String corpId ,SalUpdateMutiStaffModel salUpdateMutiStaffModel) {
		if(StringUtils.isBlank(corpId)){
			throw new IllegalArgumentException("updateCorpApproveField param corpId is null or empty!");
		}
		if(salUpdateMutiStaffModel==null){
			throw new IllegalArgumentException("updateCorpApproveField param salUpdateMutiStaffModel is null.");
		}
		SalCorpDeductModel corpDeduct =	salUpdateMutiStaffModel.getSalDeduct();
		if(validateCorpAttenDeduct(corpDeduct)==Boolean.FALSE){
			logger.warn("updateCorpApproveField --".concat(corpId).concat("-- 考勤扣款参数的某个字段为空或数字小于0"));
			throw new IllegalArgumentException("updateCorpApproveField-- 考勤扣款参数的某个字段为空或数字小于0");
		}
		List<SalSysFieldItemModel> salSysFields = salUpdateMutiStaffModel.getSaFieldList();
		if(validateCorpVocationDeduct(salSysFields)==Boolean.FALSE){
			logger.warn("updateCorpApproveField --".concat(corpId).concat("-- 考勤请假数字小于0"));
			throw new IllegalArgumentException("updateCorpApproveField-- 考勤请假数字小于0");
		}
		SalCorpDeductModel  salCorpDeductModel=	salRuleRepository.getCorpdeductRule(corpId);
		Boolean updateCorpDeductRs =null;
		Date nowDate=new Date();
		if(salCorpDeductModel!=null&&corpDeduct!=null){//若考勤扣款信息不为空，则为其所有的数字*100
			Short hasSet =salCorpDeductModel.getHasSet();
			corpDeduct.setLackDeduct(corpDeduct.getLackDeduct()*100);
			corpDeduct.setLateEarlyDeduct(corpDeduct.getLateEarlyDeduct()*100);
			corpDeduct.setStayAwayDeduct(corpDeduct.getStayAwayDeduct()*100);
			corpDeduct.setSeriousLateDeduct(corpDeduct.getSeriousLateDeduct()*100);
			corpDeduct.setModifiedDate(nowDate);
			corpDeduct.setHasSet(hasSet);
			updateCorpDeductRs =salRuleRepository.updateCorpDeductRule(corpDeduct)>0.;
		}
		
		int updateSalFieldRs=0;
		for(SalSysFieldItemModel salField:salSysFields){//存储请假的字段
			salField.setItemValue(salField.getItemValue()*100);
			salField.setModifiedDate(nowDate);
			if(salStaffBaseInfoRepository.updateField(salField)>0){
				updateSalFieldRs++;
			}
		}
		if(updateCorpDeductRs == Boolean.TRUE){//如果保存成功，则要更新预估状态下的月度工资表
			/**
			 * 重新计算企业员工的工资和薪资报表的总工资
			 */		
			saCalcuSalService.calcuSalReportAutomatic(corpId);
		}
		if(salSysFields==null){//防止出现空指针异常
			salSysFields =new ArrayList<SalSysFieldItemModel>();
		}
		return updateCorpDeductRs==Boolean.TRUE&&updateSalFieldRs==salSysFields.size();
	}
	
	/**
	 * 对考勤扣款方面的基本字段进行验证
	 */
	private Boolean validateCorpAttenDeduct(SalCorpDeductModel corpDeduct){
		if(corpDeduct==null){
			return Boolean.FALSE;
		}
		Double lackDeduct=corpDeduct.getLackDeduct();
		Double lateEarlyDeduct=corpDeduct.getLateEarlyDeduct();
		Double stayAwayDeduct=corpDeduct.getStayAwayDeduct();
		Double seriousLateDeduct=corpDeduct.getSeriousLateDeduct();
		if(lackDeduct!=null&&lateEarlyDeduct!=null&&stayAwayDeduct!=null&&seriousLateDeduct!=null){
			if(lackDeduct<0||lateEarlyDeduct<0||seriousLateDeduct<0||stayAwayDeduct<0){
				return Boolean.FALSE;
			}else{
				return Boolean.TRUE;
			}
		}else{
			return Boolean.FALSE;
		}
	}
	/**
	 * 对考勤扣款的请假字段进行验证
	 */
	private Boolean validateCorpVocationDeduct(List<SalSysFieldItemModel> salSysFields){
		Boolean validateRs=Boolean.TRUE;	
		for(SalSysFieldItemModel salSysFieldItemModel:salSysFields){
			if(salSysFieldItemModel.getItemValue()==null){
				validateRs=Boolean.FALSE;
				break;
			}else{
				if(salSysFieldItemModel.getItemValue()<0){
					validateRs=Boolean.FALSE;	
					break;
				}
			}				
		}
		
		return validateRs;
	}
}
