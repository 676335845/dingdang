package me.ywork.salarybill.service.impl;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.heyi.utils.IdGenerator;
import com.heyi.utils.MD5Util;

import me.ywork.context.CallContext;
import me.ywork.oss.OSSObjectService;
import me.ywork.salarybill.ListUtils;
import me.ywork.salarybill.base.SalarySmsModeEnum;
import me.ywork.salarybill.entity.SalaryBillMobileNo;
import me.ywork.salarybill.entity.SalaryBillSms;
import me.ywork.salarybill.model.CacheSalaryMobileModel;
import me.ywork.salarybill.model.OrgDeptModel;
import me.ywork.salarybill.model.SalaryBillCommitModel;
import me.ywork.salarybill.model.SalaryBillMobileModel;
import me.ywork.salarybill.model.SalarySmsMode;
import me.ywork.salarybill.model.SalarySmsSendMode;
import me.ywork.salarybill.model.SmsReportModel;
import me.ywork.salarybill.model.UserModel;
import me.ywork.salarybill.repository.SalaryBillLogRepository;
import me.ywork.salarybill.repository.SalaryBillMobileNoRepository;
import me.ywork.salarybill.repository.SalaryBillRepository;
import me.ywork.salarybill.repository.SalaryBillSmsRepository;
import me.ywork.salarybill.service.SalaryBillSmsService;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class SalaryBillSmsServiceImpl implements SalaryBillSmsService{
	
	private static Logger logger = LoggerFactory.getLogger(SalaryBillSmsServiceImpl.class);
	
	
	@Autowired
	private SalaryBillSmsRepository salaryBillSmsRepository;
	
	@Autowired
	private SalaryBillLogRepository salaryBillLogRepository;
	
	@Autowired
	private SalaryBillMobileNoRepository salaryBillMobileNoRepository;

	@Autowired
	private SalaryBillRepository salaryBillRepository;

	@Autowired
	private CacheableService cacheableService;
	



	@Override
	public boolean setSMSMode(String companyId, String userId, SalarySmsMode smsMode) {
		// TODO Auto-generated method stub
		int count = 0;
		SalaryBillSms salaryBillSms = salaryBillSmsRepository.findByCorpId(companyId);
		if(salaryBillSms == null){
			logger.error("setSMSMode fail ,sms is null,companyId:"+companyId);
//			Date d = new Date();
//			salaryBillSms = new SalaryBillSms();
//			salaryBillSms.setCompanyId(companyId);
//			salaryBillSms.setId(IdGenerator.newId());
//			salaryBillSms.setCreateDate(d);
//			salaryBillSms.setEnable(true);
//			salaryBillSms.setModifiedDate(d);
//			salaryBillSms.setSmsMode(smsMode.getSmsMode());
//			salaryBillSms.setSmsShow(smsMode.getSmsShow());
//			salaryBillSms.setTotal(0);
//			salaryBillSms.setUsedCount(0);
//			salaryBillSms.setUserId(userId);
//			count = salaryBillSmsRepository.insert(salaryBillSms);
		}else{
			salaryBillSms.setSmsMode(smsMode.getSmsMode());
			salaryBillSms.setSmsShow(smsMode.getSmsShow());
			count = salaryBillSmsRepository.update(salaryBillSms);
		}
		if(count>0){
			return true;
		}
		return false;
	}


	@Override
	public HSSFWorkbook exportMobileTempToExcel(String companyId) {
		HSSFWorkbook wb = null;
		List<UserModel>  userList = salaryBillRepository.getUserInfoByCompany(companyId);
		//list 排序
		String [] sortNameArr = {"parentId","userId"};
		boolean [] isAscArr = {true,true};
		ListUtils.sort(userList,sortNameArr,isAscArr);
		//获取公司所有部门
		List<OrgDeptModel> deptList = salaryBillRepository.getDeptInfoByCompany(companyId);
		//转map
		Map<String,String> deptMap = new HashMap<String,String>();
		for(OrgDeptModel dept : deptList){
			deptMap.put(dept.getId(), dept.getDeptName());
		}
		
		Map<String,UserModel> f = new LinkedHashMap<String,UserModel>();
		//处理部门
		for(UserModel user : userList){
			String hierarchy = user.getHierarchy();
			String[] hierarchys = hierarchy.split("x");
			String deptName = "";
			for(String h : hierarchys){
				if(deptMap.get(h)!=null){
					deptName += deptMap.get(h)+"/";
				}
			}
			if(deptName.length()>1){
				deptName = deptName.substring(0, deptName.length()-1);
			}
			if(f.get(user.getUserId())!=null){
				//标明多部门
				user = f.get(user.getUserId());
				user.setDeptName(user.getDeptName()+";"+deptName);
			}else{
				user.setDeptName(deptName);
			}
			f.put(user.getUserId(), user);
		}
		//map转list
		List<UserModel> ulist = new ArrayList<UserModel>();
		for(String key : f.keySet()){
			ulist.add(f.get(key));
		}
		try {
			//修改Excel模板
			InputStream instream = ClassLoader.getSystemResourceAsStream("smstemplate.xls");
			POIFSFileSystem fs=new POIFSFileSystem(instream);
			
			wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			
			int size = ulist.size();
			//填充员工信息
			HSSFRow userRows = null;
			UserModel userModel = null;
			HSSFCell userIdCell = null;
			HSSFCell deptCell = null;
			HSSFCell userNameCell = null;
			HSSFCell userJobNumCell = null;
			HSSFCell userMobileNoCell = null;
			for(int i = 0 ; i < size ; i ++){
				userRows = sheet.getRow(i+2);
				if(userRows==null){
					userRows = sheet.createRow(i+2);
				}
				userModel = ulist.get(i);
				userIdCell = userRows.getCell(0);
				if(userIdCell == null){
					userIdCell = userRows.createCell(0);
				}
				userIdCell.setCellValue(userModel.getUserId()); //员工ID
				
				deptCell = userRows.getCell(1);
				if(deptCell == null){
					deptCell = userRows.createCell(1);
				}
				deptCell.setCellValue(userModel.getDeptName());//部门名
				
				userNameCell = userRows.getCell(2);
				if(userNameCell == null){
					userNameCell = userRows.createCell(2);
				}
				userNameCell.setCellValue(userModel.getUserName());//名字
				
				userJobNumCell = userRows.getCell(3);
				if(userJobNumCell == null){
					userJobNumCell = userRows.createCell(3);
				}
				userJobNumCell.setCellValue(userModel.getUserJobNum());//工号
				
				userMobileNoCell = userRows.getCell(4);
				if(userMobileNoCell == null){
					userMobileNoCell = userRows.createCell(4);
				}
				userMobileNoCell.setCellValue(userModel.getMobileNo());//手机
			}
			return wb;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}   catch(Exception e){
			e.printStackTrace();
		}
		
		
		return null;
	}



	@Override
	public CacheSalaryMobileModel parserMobileTempExcel(String domainName, String companyId, String managerId,
			String bucketName, String fileId) {
		
		CacheSalaryMobileModel salaryModel = new CacheSalaryMobileModel();
		List<SalaryBillMobileModel> successSalaryBills = new ArrayList<SalaryBillMobileModel>();
		List<SalaryBillMobileModel> errorSalaryBills = new ArrayList<SalaryBillMobileModel>();
		List<SalaryBillMobileModel> tempSalaryBills = new ArrayList<SalaryBillMobileModel>();
		String cachekey = "";
		salaryModel.setFileKey(fileId);
		try{
			InputStream instream = OSSObjectService.getObject(bucketName, fileId);
			Workbook wb = null;
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
			
			Sheet sheet = wb.getSheetAt(0);
			int rsColumns = sheet.getLastRowNum();
			
			Row daterow = null;
			Cell cell = null;
			
			String userId = "";
			String userDept = "";
			String userName = "";
			String userJobNo = "";
			String mobileNo = "";
			//员工UserID必须在第一列 作为标志
			SalaryBillMobileModel salaryBillMobileModel = null;
			for(int i = 2 ; i < rsColumns+1 ; i ++){
				daterow = sheet.getRow(i);
				if(daterow==null){
					continue;
				}
				cell = daterow.getCell(0);
				userId =  getCellValue(cell);
				if(StringUtils.isBlank(userId)){
					continue;
				}
				//dept列
				cell = daterow.getCell(1);
				userDept = getCellValue(cell);
				//userName列
				cell = daterow.getCell(2);
				userName =  getCellValue(cell);
				//userJobNo列
				cell = daterow.getCell(3);
				userJobNo =  getCellValue(cell);
				cell = daterow.getCell(4);
				mobileNo  =  getCellValue(cell);
				
				salaryBillMobileModel = new SalaryBillMobileModel();
				salaryBillMobileModel.setId(IdGenerator.newId());
				salaryBillMobileModel.setCompanyId(companyId);
				salaryBillMobileModel.setUserDept(userDept);
				salaryBillMobileModel.setUserId(userId);
				salaryBillMobileModel.setUserJobNo(userJobNo);
				salaryBillMobileModel.setUserName(userName);
				salaryBillMobileModel.setMobileNo(mobileNo);
				tempSalaryBills.add(salaryBillMobileModel);
			}
			
			cachekey = companyId+managerId+System.currentTimeMillis();
			//key加密
			salaryModel.setCacheKey(cachekey);
			
			
			List<String> notExistUsers = null;
			if(tempSalaryBills != null && tempSalaryBills.size()>0){
				notExistUsers = salaryBillSmsRepository.notExistsUser(companyId, tempSalaryBills);
			}
			boolean allRight = false;
			if(notExistUsers == null || notExistUsers.size() == 0){
				allRight = true;
			}
			if(tempSalaryBills != null && tempSalaryBills.size()>0){
				for(SalaryBillMobileModel salaryBillModel : tempSalaryBills){
					String reason = "";
					boolean strResult = checkPhone(salaryBillModel.getMobileNo());
					//校验手机号
					if(!strResult){
						reason += "手机格式有误.";
					}
					if(!allRight && !notExistUsers.contains(salaryBillModel.getUserId())){
						reason += "userId不存在.";
					}
					if(StringUtils.isBlank(reason)){
						successSalaryBills.add(salaryBillModel);
					}else{
						salaryBillModel.setReason(reason);
						errorSalaryBills.add(salaryBillModel);
					}
				}
			}
			
			salaryModel.setErrorSalaryMobiles(errorSalaryBills);
			salaryModel.setSuccessSalaryMobiles(successSalaryBills);
			salaryModel.setTips(successSalaryBills.size()+"");
			//存缓存
			if(StringUtils.isNotBlank(salaryModel.getCacheKey())){
				cacheableService.setRawObjectInCache(salaryModel.getCacheKey(), salaryModel, 1, TimeUnit.DAYS);
				logger.info("save cache success,cahcekey:"+salaryModel.getCacheKey());
			}
		}catch(Exception e){
			e.printStackTrace();
			salaryModel.setTips("sorry,系统异常");
			logger.error(e.toString());
			logger.error("解析excel异常，",e);
		}
		return salaryModel;
	}
	

	@Override
	public boolean commitMobile(CallContext callContext, SalaryBillCommitModel salaryBillCommitModel) {
		String cacheKey = salaryBillCommitModel.getCachekey();
		String companyId = callContext.getCorpId();
		String userId = callContext.getUserId();
		
		CacheSalaryMobileModel cacheSalaryModel = (CacheSalaryMobileModel) this.cacheableService.getRawObjectFromCache(cacheKey);
		
		if(cacheSalaryModel == null){
			logger.error("get cache data is null,cachekey:"+cacheKey);
			return false;
		}
	
		//存
		List<SalaryBillMobileModel> successSalaryBills = cacheSalaryModel.getSuccessSalaryMobiles();
		if(successSalaryBills==null || successSalaryBills.size() == 0){
			return false;
		}
		
		List<SalaryBillMobileNo> salaryBills = new ArrayList<SalaryBillMobileNo>();
		
		Date d = new Date();
		for(SalaryBillMobileModel salaryBillModel : successSalaryBills){
			SalaryBillMobileNo salaryBill = new SalaryBillMobileNo();
			String IMPORTKEY = "ding_user_" + salaryBillModel.getUserId();
			String fdId = MD5Util.getMD5String(companyId.concat(IMPORTKEY));
			salaryBill.setId(fdId);
			salaryBill.setCompanyId(companyId);
			salaryBill.setUserId(salaryBillModel.getUserId());
			salaryBill.setDept(salaryBillModel.getUserDept());
			salaryBill.setName(salaryBillModel.getUserName());
			salaryBill.setJobNo(salaryBillModel.getUserJobNo());
			salaryBill.setCreateUserId(userId);
			salaryBill.setMobileNo(salaryBillModel.getMobileNo());
			salaryBill.setCreateDate(d);
			salaryBill.setModifiedDate(d);
			salaryBills.add(salaryBill);
		}
		try{
			salaryBillMobileNoRepository.batchSaveSalaryBillMobileNos(salaryBills);
			return true;
		}catch(Exception e){
			logger.error("save error,main data:"+JSONObject.toJSONString(salaryBills),e);
			return false;
		}
	
		//return false;
	}

	

	private static boolean checkPhone(String phone){
		if(phone == null){
			return false;
		}
        String regExp = "^((13[0-9])|(14[5|7|9])|(15[^4])|(18[0-9])|(17[0-8]))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(phone);
        return m.matches();
	}

	@Override
	public boolean deleteById(CallContext callContext, String id) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private String getCellValue(Cell cell) {
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
	
	@Override
	public SalarySmsMode getCorpSmsInfo(String companyId,String userId){
		SalarySmsMode smsMode = new SalarySmsMode();
		try{
			//是否已经存在
			SalaryBillSms smsInfo = salaryBillSmsRepository.findByCorpId(companyId);
			Integer count = 0;
			if(smsInfo == null){
				//初始化短信设置
				//企业用户数
				count = salaryBillSmsRepository.countCorpUsers(companyId);
				int total = 1000;
				if(count == null){
					total = 1000;
				}else{
					total = count * 5 < 1000 ? 1000 : count * 5;
				}
				smsInfo = new SalaryBillSms();
				smsInfo.setId(IdGenerator.newId());
				smsInfo.setCompanyId(companyId);
				smsInfo.setUserId(userId);
				smsInfo.setTotal(total>5000?5000:total);
				smsInfo.setUsedCount(0);
				smsInfo.setEnable(false);
				smsInfo.setSmsMode(SalarySmsModeEnum.SMS_SEND_Default.getCode());
				smsInfo.setSmsShow(SalarySmsModeEnum.SMS_SHOW_SHORT.getCode());
				salaryBillSmsRepository.insert(smsInfo);
			}
			//查找没有手机的员工userid
			List<String> l = salaryBillMobileNoRepository.findNoMobiles(companyId);
			
			//查找操作记录
			List<SalarySmsSendMode> ssms = salaryBillLogRepository.getSendSmsInfo(companyId);
			int free = 0;
			if(ssms!=null){
				for(SalarySmsSendMode ssm : ssms){
					int batchCount = ssm.getBatchCount();
					int freeCount = ssm.getCount();
					free +=batchCount-freeCount;
				}
			}
			
			smsMode.setNoMobleUser(l==null?0:l.size());
			smsMode.setSmsMode(smsInfo.getSmsMode());
			smsMode.setSmsShow(smsInfo.getSmsShow());
			smsMode.setFreeTotal(smsInfo.getTotal());
			smsMode.setUsedCount(smsInfo.getUsedCount());
			smsMode.setSalarySmsSendMode(ssms);
			smsMode.setSaveMoney((free/10D)+"");
			
			return smsMode;
			
		}catch(Exception e){
			logger.error("init corp sms fail,company:"+companyId+",userId:"+userId,e);
		}
		return null;
	}


	@Override
	public List<SmsReportModel> getSmsReport(Date st, Date et) {
		return salaryBillSmsRepository.getSmsReport(st, et);
	}
	
	
	
	
}
