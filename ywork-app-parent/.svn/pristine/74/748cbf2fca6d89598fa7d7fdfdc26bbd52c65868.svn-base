package me.ywork.salary.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
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
import me.ywork.page.PageData;
import me.ywork.page.PageDataImpl;
import me.ywork.page.Pageable;
import me.ywork.salary.entity.SalElementInfoEntity;
import me.ywork.salary.entity.SalStaffBaseInfoEntity;
import me.ywork.salary.entity.SalStaffSalReportEntity;
import me.ywork.salary.entity.SalSysFieldItemEntity;
import me.ywork.salary.enumeration.SalExcelTimeType;
import me.ywork.salary.enumeration.SalDetailShowType;
import me.ywork.salary.exception.ExcelUnNormalException;
import me.ywork.salary.exception.StaffNotExistException;
import me.ywork.salary.model.SalInfoDetailModel;
import me.ywork.salary.model.SalInfoExcelModel;
import me.ywork.salary.model.SalStaffBaseInfoModel;
import me.ywork.salary.model.SalSysFieldItemModel;
import me.ywork.salary.repository.SalInfoRepository;
import me.ywork.salary.repository.SalReportRepository;
import me.ywork.salary.repository.SalStaffBaseInfoRepository;
import me.ywork.salary.repository.SalSynCorpInfoRepository;
import me.ywork.salary.service.SalBaseService;
import me.ywork.salary.service.SalCalcuSalService;
import me.ywork.salary.service.SalInfoService;
import me.ywork.salary.util.WorkBookUtil;

/**
 * Created by xiaobai on 2017/1/11.
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class SalInfoServiceImpl implements SalInfoService {
	@Autowired
	private SalInfoRepository staffSalInfoRepository;
	@Autowired
	private SalStaffBaseInfoRepository salStaffBaseInfoRepository;
	@Autowired
	private SalReportRepository salReportRepository;
	@Autowired 
	private WorkBookUtil workBookUtil;
	@Autowired
	private SalSynCorpInfoRepository salSynCorpInfoRepository;
	// 缓存机制
	@Autowired
	private CacheableService cacheableService;
	@Autowired
	private SalCalcuSalService saCalcuSalService;
	@Autowired
	private SalBaseService salBaseService;
	private static final Logger logger = LoggerFactory.getLogger(SalInfoServiceImpl.class);

	@Override
	public PageData<SalStaffBaseInfoModel> getStaffSalInfos(String corpId, Pageable pageable) {
		if (StringUtils.isBlank(corpId)) {
			throw new IllegalArgumentException("getStaffSalInfos param corpId is null or empty.");
		}
		if(pageable == null){
			throw new IllegalArgumentException("getStaffSalInfos param pageable is null.");
		}
		List<SalStaffBaseInfoModel> staffSalInfoModels = null;
		int pageNo = pageable.getPageNo();// 当前页数
		int pageSize = pageable.getPageSize();//一页显示的最大数量 
	
		staffSalInfoModels = salStaffBaseInfoRepository.getStaffSalInfos(corpId, (pageNo - 1) * pageSize, pageSize);
		
		String staffId = null;
		Integer updateShouldPaySalRs =null;
		for(SalStaffBaseInfoModel salStaffBaseInfoModel:staffSalInfoModels){
			staffId = salStaffBaseInfoModel.getDingStaffId();
			Double shouldPaySal =salStaffBaseInfoModel.getShouldPaySal();
			if( shouldPaySal==null){
				//找到该员工所有的的薪资字段
				List<SalSysFieldItemModel>  salSysFieldItemList = salStaffBaseInfoRepository.getStaffFieldItems(corpId,staffId);
				
				if(salSysFieldItemList.isEmpty() ==Boolean.FALSE){
					shouldPaySal =0.0;
				}

					for(SalSysFieldItemModel salSysFieldItem:salSysFieldItemList){
						if(salSysFieldItem.getItemValue()!=null){
					     	shouldPaySal+=salSysFieldItem.getItemValue();//得到该人员的工资组成部分的值
						}
					}
				if(shouldPaySal != null){
					//存储该员工的工资总数
					updateShouldPaySalRs = staffSalInfoRepository.saveStaffShouldPaySal(corpId , staffId,shouldPaySal);
					if(updateShouldPaySalRs >0){
						salStaffBaseInfoModel.setShouldPaySal(shouldPaySal);
					}
				}			
			}
			salStaffBaseInfoModel.setDeptName(salBaseService.getStaffAllDeptName(corpId, staffId));
		}
		
		Integer totalCount =salStaffBaseInfoRepository.getStaffSalInfosCount(corpId);
		pageable.setTotalCount(totalCount);
		return new PageDataImpl<SalStaffBaseInfoModel>(staffSalInfoModels, pageable);
	}

	@Override
	public SalInfoDetailModel getSalInfosByStaffId(String corpId ,String reportId, Short showType,String dingStaffId) {
		if (StringUtils.isBlank(corpId)) {
			throw new IllegalArgumentException("getSalaryInfosByStaffId param corpId is null or empty.");
		}
		if(StringUtils.isBlank(dingStaffId)){
			throw new IllegalArgumentException("getSalaryInfosByStaffId param dingStaffId is null or empty.");
		}
		SalInfoDetailModel staffSalInfoDetailModel = new SalInfoDetailModel();
		List<SalSysFieldItemModel> salSysFieldItemList = salStaffBaseInfoRepository.getStaffFieldItems(corpId , dingStaffId);//得到员工的薪资条目
		
		/**
		 * 为员工薪资组成部分的条目进行封装
		 */
		if(showType == SalDetailShowType.MonthSalType.getCode()){//若是月度薪资报表中展示的员工的薪资,则要展示更为详细的信息
			SalStaffSalReportEntity  salStaffSalReportEntity =	salReportRepository.getStaffMonthSalEntity(reportId, dingStaffId);
			salBaseService.initCorpStaffReportSalItem(salStaffSalReportEntity,salSysFieldItemList);//对组成部分的条目进行封装
		}
		
		staffSalInfoDetailModel.setItemModels(salSysFieldItemList);
		
		return staffSalInfoDetailModel;
	}

	@Override
	public List<SalInfoDetailModel> getMutiStaffSalInfosDetail(String corpId, List<String> staffids) {
		if (StringUtils.isBlank(corpId)) {
			throw new IllegalArgumentException("getMutiStaffSalInfosDetail  param corpId is null or empty.");
		}
		List<SalInfoDetailModel> staffSalInfoDetailModels = null;
		staffSalInfoDetailModels = staffSalInfoRepository.getMutiStaffSalInfosDetail(corpId, staffids);
		for (SalInfoDetailModel staffSalInfoDetailModel : staffSalInfoDetailModels) {
			String staffId = staffSalInfoDetailModel.getDingStaffId();
			List<SalSysFieldItemModel> fieldItemModels = salStaffBaseInfoRepository.getStaffFieldItems(corpId,staffId);
			staffSalInfoDetailModel.setItemModels(fieldItemModels);
		}

		return staffSalInfoDetailModels;
	}


	@Override
	public Integer calcuUnSetSalStaffNum(String corpId) {
		if (StringUtils.isBlank(corpId)) {
			throw new IllegalArgumentException("calcuUnSetSalStaffNum  param corpId is null or empty.");
		}
		Integer num = staffSalInfoRepository.calcuUnSetSalStaffNum(corpId);
		return num;
	}

	@Override
	public SalInfoExcelModel parseSalInfoExcel(CallContext callContext, String fileId) throws StaffNotExistException, ExcelUnNormalException{
		if(callContext == null){
			throw new IllegalArgumentException("parseSalInfoExcel  param callContext is null.");
		}
		if(StringUtils.isBlank(fileId)){
			throw new IllegalArgumentException("parseSalInfoExcel  param fileId is null.");
		}
		String corpId = callContext.getCorpId();
		String userId = callContext.getUserId();
		if(StringUtils.isBlank(corpId)){
			throw new IllegalArgumentException("parseSalInfoExcel  param corpId is null.");
		}
		InputStream instream;
		Workbook wb = null;
		try {
			instream = OSSObjectService.getObject("alidsalarybill", fileId);		
			if(fileId.endsWith(".xlsx")){
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
			}
		} catch (IOException e1) {
			//e1.printStackTrace();
			logger.error("parseSalInfoExcel--".concat(corpId).concat("--fileId:").concat(fileId).concat("--不存在！"));
			return new SalInfoExcelModel();
		}
       if(wb==null){//若excel文件为空
    	   logger.warn("parseSalInfoExcel --".concat(corpId).concat("--XSSFWorkbook is null"));
    	   return new SalInfoExcelModel();
       }
		
		List<SalStaffBaseInfoModel> salStaffInfos=new ArrayList<SalStaffBaseInfoModel>();
		List<String> detailSalTitle=new ArrayList<>();//存储员工自定义字段的集合
		SalInfoExcelModel salInfoExcelModel =new SalInfoExcelModel();//封装员工薪资报表信息的数据包s
		SalStaffBaseInfoModel salStaffBaseInfoModel =null;//封装员工薪资信息的数据包
		int rowCount=0; //记录行数的标志器
		int failCount=0;//记录失败数目的标志器

		Map<String,Integer> titleAddress=new HashMap<String,Integer>();//存储字段标题的map
		for(int sheetCount=0;sheetCount<wb.getNumberOfSheets();sheetCount++){//循环所有的sheet
			if(sheetCount>0){//如果文件中表格的数目大于1，则跳出循环
				break;
			}
			Sheet sheet = wb.getSheetAt(sheetCount);//得到文件中的表格
			int totalRowNum = sheet.getLastRowNum();	// 表格的行总数		
			   logger.info("parseSalInfoExcel --".concat(corpId).concat("--总行数是:").concat(""+totalRowNum));
//			   totalRowNum=WorkBookUtil.analyzeActualRowNum(sheet, totalRowNum);
//			   logger.info("parseSalInfoExcel --".concat(corpId).concat("--总行数是:").concat(""+totalRowNum));
			for(rowCount=0;rowCount<=totalRowNum;rowCount++){//循环表格中所有的行
               try{
				if(rowCount<=1){
					if(rowCount==1){//如果是表格中的第二行，得到所有的标题，并把标题加入到存储标题的集合中，同时也把标题的名字和列的下标值加入到map中
						Row titleSalRow =sheet.getRow(rowCount);
					   for(int k=4;k<titleSalRow.getLastCellNum();k++){
							 String cellValue=  workBookUtil.getCellValue(titleSalRow.getCell(k));
							 if(StringUtils.isNotBlank(cellValue)){
							     detailSalTitle.add(cellValue);
							 }else{
								 break;
							 }
					   }
					   if(detailSalTitle.size()==0){
						   break;
					   }
					   Iterator<Cell> cellIterator = titleSalRow.cellIterator();
					   int beginNum=0;
					   while(cellIterator.hasNext()){
							Cell cell = cellIterator.next();
							String cellTitle = workBookUtil.getCellValue(cell);
							if(StringUtils.isNotBlank(cellTitle)){							
								titleAddress.put(cellTitle , beginNum);
								beginNum++;
							}else{
								break;
							}
					   }
					   if(titleAddress.size()<=4){
						   break;
					   }
					}
					continue;
				}
               }catch(Exception e){
   				throw new ExcelUnNormalException("上传excel不符合规范！");
   			  }
               
				Row row = sheet.getRow(rowCount);
				if(row==null){
					 logger.info("parseSalInfoExcel --row 为null:".concat("行数是："+rowCount+1));
					break;
				}
				String dingStaffId =null;		
				salStaffBaseInfoModel = new SalStaffBaseInfoModel();
				salStaffBaseInfoModel.setRowNum(rowCount+1);
			   dingStaffId = workBookUtil.getCellValue(row.getCell(titleAddress.get("员工UserID")));
				if(StringUtils.isBlank(dingStaffId)){
					if(WorkBookUtil.analyzeLastRow(workBookUtil,row)==Boolean.FALSE){
					   logger.info("parseSalInfoExcel 钉钉ID为:".concat(dingStaffId).concat("行数是："+(rowCount+1)));
					throw new StaffNotExistException("员工USERID不能为空(第".concat(""+(rowCount+1)+"行)"));
					}else{
						logger.info("parseSalInfoExcel --该企业".concat(corpId).
								concat("导入的薪资模板有问题，最后一行变为上一行").concat("--总行数是：").concat(""+(rowCount)));
						rowCount=rowCount-1;
								break;
					}
				}
				try{
				SalStaffBaseInfoModel salStaff =salStaffBaseInfoRepository.getOrgStaffInfo(corpId, dingStaffId);
               if(salStaff ==null){
					throw new StaffNotExistException();
				}
	                salStaffBaseInfoModel.setId(salStaff.getId());
					salStaffBaseInfoModel.setDeptName(workBookUtil.getCellValue(row.getCell(titleAddress.get("部门"))));
					salStaffBaseInfoModel.setStaffName(workBookUtil.getCellValue(row.getCell(titleAddress.get("姓名"))));
					salStaffBaseInfoModel.setJobNum(workBookUtil.getCellValue(row.getCell(titleAddress.get("工号"))));
					salStaffBaseInfoModel.setDingStaffId(dingStaffId);
				    
					 List<SalSysFieldItemModel> sysFields = new  ArrayList<SalSysFieldItemModel>();
					 SalSysFieldItemModel salSysFieldItemModel =null;
					 if(titleAddress!=null){
						for(int k=4;k<titleAddress.size();k++){//循环得到员工所有薪资字段的值
							Cell  cell=	row.getCell(k);
							String cellName = workBookUtil.getCellValue(sheet.getRow(1).getCell(k));
							String cellValue  = workBookUtil.getCellValue(cell);
							if(StringUtils.isBlank(cellValue)){
								cellValue="0";
							}
							salSysFieldItemModel = new SalSysFieldItemModel();
							salSysFieldItemModel.setItemName(cellName);
	
							salSysFieldItemModel.setItemValue(workBookUtil.getDoubleNum(cellValue,SalExcelTimeType.DOUBLE.getCode()));
							sysFields.add(salSysFieldItemModel);
						}
					 }
					salStaffBaseInfoModel.setSalFields(sysFields);			
				}
				catch(StaffNotExistException e){
					logger.warn("parseSalInfoExcel --".concat(corpId).concat("--").concat(dingStaffId).concat("--该人员不在组织架构！"));
					salStaffBaseInfoModel.setFailReason("该人员不在组织架构！");
					salStaffBaseInfoModel.setStaffName(workBookUtil.getCellValue(row.getCell(titleAddress.get("姓名"))));
					salStaffBaseInfoModel.setDeptName(workBookUtil.getCellValue(row.getCell(titleAddress.get("部门"))));
					salStaffBaseInfoModel.setJobNum(workBookUtil.getCellValue(row.getCell(titleAddress.get("工号"))));
					failCount++;
				}catch(Exception e){
					logger.warn("parseSalInfoExcel --".concat(corpId).concat(dingStaffId).concat("--上传薪资单位不合格或员工钉钉ID为空！"));
					salStaffBaseInfoModel.setFailReason("单位不合格或员工钉钉ID为空！");
					salStaffBaseInfoModel.setStaffName(workBookUtil.getCellValue(row.getCell(titleAddress.get("姓名"))));
					salStaffBaseInfoModel.setDeptName(workBookUtil.getCellValue(row.getCell(titleAddress.get("部门"))));
					salStaffBaseInfoModel.setJobNum(workBookUtil.getCellValue(row.getCell(titleAddress.get("工号"))));
					failCount++;
				}
				salStaffInfos.add(salStaffBaseInfoModel);
			}}
			
	    	String  cachekey = corpId+userId+System.currentTimeMillis();
	    	Integer totalCount =salStaffInfos.size();
		    salInfoExcelModel.setDetailTitle(detailSalTitle);
		    salInfoExcelModel.setCacheKey(cachekey);
		    salInfoExcelModel.setPageNo(1);
	     
		    salInfoExcelModel.setTotalCount(totalCount);
		    salInfoExcelModel.setFailCount(failCount);
		    Integer successCount = totalCount-failCount;
		    salInfoExcelModel.setSuccessCount(successCount);
		    salInfoExcelModel.setPageSize(50);
		    logger.info("parseSalInfoExcel --".concat(corpId).concat("--totalCount:"+totalCount+"")
		    		.concat("--successCount:"+successCount+"").concat("failCount"+failCount+""));
		    if(totalCount<=50&&totalCount>0){
		    	salInfoExcelModel.setTotalPages(1);
		    }else{
		    	salInfoExcelModel.setTotalPages(totalCount/50+1);
		    }
			salInfoExcelModel.setSalInfos(salStaffInfos);
			if(StringUtils.isNotBlank(salInfoExcelModel.getCacheKey())){//存缓存
				salInfoExcelModel.setSalInfos(salStaffInfos);
				cacheableService.setRawObjectInCache(cachekey , salInfoExcelModel, 1, TimeUnit.DAYS);
				logger.info("parseSalInfoExcel -- ".concat(corpId).concat("REDIS 存储薪资数据成功,cahcekey是:"+salInfoExcelModel.getCacheKey()));
			}
			 if(totalCount>50){
				  logger.info("parseSalInfoExcel -- ".concat(corpId).concat("--上传薪资数据 总数大于50，截取50条数据"));
				 salStaffInfos= salStaffInfos.subList(0, 50);
				 salInfoExcelModel.setSalInfos(salStaffInfos);
			 }
		    return salInfoExcelModel;
	}

	@Override
	public Boolean commitSalInfoExcel(CallContext callContext, String cacheKey) {
		if(callContext==null){
			throw new IllegalArgumentException("commitSalInfoExcel param callContext is null");
		}
		if(StringUtils.isBlank(cacheKey)){
			throw new IllegalArgumentException("commitSalInfoExcel param cacheKey is null or empty");
		}
		
		String corpId=callContext.getCorpId();
	   if(StringUtils.isBlank(corpId)){
			throw new IllegalArgumentException("commitSalInfoExcel param corpId is null or empty");
		}
		Date nowDate= new Date();
		SalInfoExcelModel salInfoExcelModel = (SalInfoExcelModel) cacheableService.getRawObjectFromCache(cacheKey);
		List<SalStaffBaseInfoModel> salInfos = salInfoExcelModel.getSalInfos();
		SalStaffBaseInfoEntity salStaffBaseInfoEntity = null;
		SalStaffBaseInfoModel salStaffBaseInfoModel = null;
		String id=null;
		String staffId=null;
		for(SalStaffBaseInfoModel salInfo:salInfos){
			staffId = salInfo.getDingStaffId();
			salStaffBaseInfoModel = salStaffBaseInfoRepository.getStaffBaseInfo(corpId, salInfo.getDingStaffId());//查找该人员是否在应用内存在
			if(salStaffBaseInfoModel == null){
				logger.info("commitSalInfoExcel -- ".concat(corpId).concat("-- 抓取员工ID为：").concat(staffId).concat("到应用下！"));
				//向数据库中初始化员工的基本信息
			    salStaffBaseInfoEntity =new SalStaffBaseInfoEntity();
			    id =IdGenerator.newId();
			    salStaffBaseInfoEntity.setId(id);
			    salStaffBaseInfoEntity.setCorpId(corpId);
			    salStaffBaseInfoEntity.setCreateDate(nowDate);
			    salStaffBaseInfoEntity.setDingStaffId(staffId);
			    salStaffBaseInfoEntity.setModifiedDate(nowDate);
			    salSynCorpInfoRepository.synchStaffBaseInfo(salStaffBaseInfoEntity);
			}else{		
		    	//根据员工的信息删除与员工原先的薪资信息
			   salStaffBaseInfoRepository.deleteStaffSalField(salStaffBaseInfoModel.getId());
			}
			//新增员工的薪资信息
			List<SalSysFieldItemModel> salFields = salInfo.getSalFields();
			for(SalSysFieldItemModel salF:salFields){
				if(salF.getItemValue()==0.0){
					continue;
				}
				salF.setCreateDate(nowDate);
				salF.setModifiedDate(nowDate);
				salF.setItemId(IdGenerator.newId());
				if(salStaffBaseInfoModel == null){
				   salF.setRelativeId(id);
				}else{
					salF.setRelativeId(salStaffBaseInfoModel.getId());
				}
				salStaffBaseInfoRepository.addNewSalInfoField(salF);
			}
			//计算该员工的应发薪资总数
			staffId = salInfo.getDingStaffId();
			Double shouldPaySal =salInfo.getShouldPaySal();
			if(shouldPaySal==null){
				shouldPaySal=0.0;
				Integer updateShouldPaySalRs =null;
				//找到该员工所有的的薪资字段
				List<SalSysFieldItemModel>  salSysFieldItemList = salStaffBaseInfoRepository.getStaffFieldItems(corpId,staffId);
				if(salSysFieldItemList.isEmpty()){
					shouldPaySal =0.0;
				}
				for(SalSysFieldItemModel salSysFieldItem:salSysFieldItemList){
					if(salSysFieldItem.getItemValue()!=null){
				     	shouldPaySal+=salSysFieldItem.getItemValue();//得到该人员的工资组成部分的值
					}
				}
				if(shouldPaySal != null){
					//存储该员工的工资总数
					updateShouldPaySalRs = staffSalInfoRepository.saveStaffShouldPaySal(corpId , staffId,shouldPaySal);
				}		
			}
		}
		/**
		 * 重新计算企业员工的工资和薪资报表的总工资
		 */		
		saCalcuSalService.calcuSalReportAutomatic(corpId);
		return Boolean.TRUE;
	}
	

	@Override
	public boolean deleteById(CallContext callContext, String id) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public HSSFWorkbook exportToExcel(String corpId) {
		if(StringUtils.isBlank(corpId)){
			throw new IllegalArgumentException();
		}
		HSSFWorkbook wb = null;
		List<SalElementInfoEntity>   userList = salStaffBaseInfoRepository.getOrgStaffInfosUnderDept(corpId, "1");
		String staffId=null;
		for(SalElementInfoEntity salElementInfoEntity:userList){
			staffId=salElementInfoEntity.getFdDingId();
			salElementInfoEntity.setDeptName(salBaseService.getStaffAllDeptName(corpId, staffId));
		}  
		
		try {
			//修改Excel模板
			InputStream instream = ClassLoader.getSystemResourceAsStream("Dingsal.xls");
			POIFSFileSystem fs=new POIFSFileSystem(instream);
			
			wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			
	
			
			int size = userList.size();
			//填充员工信息
			HSSFRow userRows = null;
			SalElementInfoEntity userModel = null;
			HSSFCell userIdCell = null;
			HSSFCell deptCell = null;
			HSSFCell userNameCell = null;
			HSSFCell userJobNumCell = null;
			for(int i = 0 ; i < size ; i ++){
				userRows = sheet.getRow(i+2);
				if(userRows==null){
					userRows = sheet.createRow(i+2);
				}
				userModel = userList.get(i);
				userIdCell = userRows.getCell(0);
				if(userIdCell == null){
					userIdCell = userRows.createCell(0);
				}
				userIdCell.setCellValue(userModel.getFdDingId()); //员工ID
				deptCell = userRows.getCell(1);
				if(deptCell == null){
					deptCell = userRows.createCell(1);
				}
				deptCell.setCellValue(userModel.getDeptName());
				userNameCell = userRows.getCell(2);
				if(userNameCell == null){
					userNameCell = userRows.createCell(2);
				}
				userNameCell.setCellValue(userModel.getFdName());
				userJobNumCell = userRows.getCell(3);
				if(userJobNumCell == null){
					userJobNumCell = userRows.createCell(3);
				}
				userJobNumCell.setCellValue(userModel.getUserJobNum());
			}
			return wb;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}   catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}


}
