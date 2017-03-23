package me.ywork.salarybill.service.impl;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.heyi.framework.messagebus.kafka.KafkaProducer;
import com.heyi.framework.spring.context.AppContext;
import com.heyi.utils.CollectionUtils;
import com.heyi.utils.IdGenerator;

import me.ywork.context.CallContext;
import me.ywork.message.DingOAMessage;
import me.ywork.message.DingOAMessage.DingOABody;
import me.ywork.message.base.DingCorpMessageHeader;
import me.ywork.message.base.DingCorpMessageResult;
import me.ywork.message.base.DingMessageType;
import me.ywork.message.topic.KafkaTopics;
import me.ywork.oss.OSSObjectService;
import me.ywork.salarybill.ListUtils;
import me.ywork.salarybill.SalaryBillConfigure;
import me.ywork.salarybill.base.SalaryBillConstant;
import me.ywork.salarybill.base.SalarySmsModeEnum;
import me.ywork.salarybill.entity.SalaryBill;
import me.ywork.salarybill.entity.SalaryBillItem;
import me.ywork.salarybill.entity.SalaryBillLog;
import me.ywork.salarybill.entity.SalaryBillSms;
import me.ywork.salarybill.model.CacheSalaryModel;
import me.ywork.salarybill.model.CorpAdminMessage;
import me.ywork.salarybill.model.OrgDeptModel;
import me.ywork.salarybill.model.OrgTreeModel;
import me.ywork.salarybill.model.SalaryBillCommitModel;
import me.ywork.salarybill.model.SalaryBillItemModel;
import me.ywork.salarybill.model.SalaryBillModel;
import me.ywork.salarybill.model.SalaryBillNoPwdModel;
import me.ywork.salarybill.model.SalaryBillReadRecordModel;
import me.ywork.salarybill.model.SalaryBillTemplateModel;
import me.ywork.salarybill.model.SalaryHistoryDispalyModel;
import me.ywork.salarybill.model.UserModel;
import me.ywork.salarybill.repository.SalaryBillItemRepository;
import me.ywork.salarybill.repository.SalaryBillLogRepository;
import me.ywork.salarybill.repository.SalaryBillRepository;
import me.ywork.salarybill.repository.SalaryBillSmsRepository;
import me.ywork.salarybill.service.SalaryBillService;
import me.ywork.sms.message.ShortMessageBody;
import me.ywork.suite.api.rpc.IDingAPIRpcService;
import me.ywork.util.AESUtil;
import me.ywork.util.DateUtils;


@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class SalaryBillServiceImpl implements SalaryBillService{
	
	
	private final Logger  logger = LoggerFactory.getLogger(SalaryBillServiceImpl.class);
	
	@Autowired
	private SalaryBillRepository salaryBillRepository;
	
	@Autowired
	private SalaryBillItemRepository salaryItemRepository;
	
	@Autowired
	private SalaryBillLogRepository salaryLogRepository;
	
	@Autowired
	private SalaryBillSmsRepository salaryBillSmsRepository;
	
	// 缓存机制
	@Autowired
	private CacheableService cacheableService;
	
	// 钉钉接口
	private IDingAPIRpcService dingAPIRpcService;
	
	private IDingAPIRpcService getDingAPIRpcService() {
		if (dingAPIRpcService == null) {
			dingAPIRpcService = (IDingAPIRpcService) AppContext.getBean("dingAPIRpcService");
		}
		return dingAPIRpcService;
	}
	
	@Override
	public SalaryBillModel viewSalary(CallContext callContext, Integer salaryMonth, boolean loadItem,String salaryLogid,String templateId) {
		
		if(StringUtils.isNotBlank(salaryLogid)){
			salaryMonth = -1;
		}else{
			salaryLogid = null;
			if(salaryMonth == null){
				salaryMonth = 0;
			}
		}
		
		SalaryBillModel salaryBillModel = salaryBillRepository.viewSalary(callContext.getCorpId(), callContext.getUserId(),salaryMonth,salaryLogid,templateId);
		
		if(salaryBillModel == null && salaryMonth != -1 && salaryMonth !=0){
			salaryBillModel = salaryBillRepository.viewSalary4other(callContext.getCorpId(), callContext.getUserId(), salaryMonth, templateId);
		}
		
		if(salaryBillModel == null){
			return null;
		}
		
		if(loadItem){
			List<SalaryBillItemModel> salaryItems = salaryItemRepository.getSalaryItemsBySalaryBillId(callContext.getCorpId(),salaryBillModel.getId());
			if(salaryItems != null && salaryItems.size()>0){
				for(SalaryBillItemModel salaryItem : salaryItems){
					try {
						salaryItem.setItemValue(AESUtil.decrypt(salaryItem.getItemValue(), salaryItem.getId()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				salaryBillModel.setSalaryItems(salaryItems);
			}
			try {
				salaryBillModel.setRealSalary(AESUtil.decrypt(salaryBillModel.getRealSalary(), salaryBillModel.getId()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return salaryBillModel;
	}
	


	@Override
	public List<SalaryHistoryDispalyModel> getHistorySalary(CallContext callContext) {
		// TODO Auto-generated method stub
		
		List<SalaryHistoryDispalyModel> historySalarys = salaryBillRepository.historySalary(callContext.getCorpId(), callContext.getUserId());
		
		return historySalarys;
	}
	

	@Override
	public Integer setReaded(String companyId, String id) {
		return salaryBillRepository.setReaded(companyId, id);
	}



	@Override
	public HSSFWorkbook exportToExcel(String companyId) {
		HSSFWorkbook wb = null;
		//获取该公司下的所有员工信息
//		//一次获取100条
//	    int pageNo = 1;
//	    int pageSize = 100;
//	    int startPage = 0;
//		boolean ef = true;
//		List<UserModel> userListPage = null;
//		do{
//		   startPage = pageNo <= 1 ? 0 : (pageNo - 1) * pageSize;
//
//		   logger.info("======page:"+pageNo+"size:"+userListPage.size());
//		   if(userListPage!=null && userListPage.size()>0){
//			   userList.addAll(userListPage);
//			   pageNo++;
//			   if(userListPage.size()<pageSize){
//				   ef = false;
//			   }
//		   }else{
//			   ef = false;
//		   }
//		 }while(ef);
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
			InputStream instream = ClassLoader.getSystemResourceAsStream("salarybill.xls");
			POIFSFileSystem fs=new POIFSFileSystem(instream);
			
			wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			
			HSSFRow row = sheet.getRow(3);
			HSSFCell cell = row.getCell(0);
			//模板设置为当前月
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(""+sdf.format(new Date()));
			
			int size = ulist.size();
			//填充员工信息
			HSSFRow userRows = null;
			UserModel userModel = null;
			HSSFCell userIdCell = null;
			HSSFCell deptCell = null;
			HSSFCell userNameCell = null;
			HSSFCell userJobNumCell = null;
			for(int i = 0 ; i < size ; i ++){
				userRows = sheet.getRow(i+6);
				if(userRows==null){
					userRows = sheet.createRow(i+6);
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
				deptCell.setCellValue(userModel.getDeptName());
				userNameCell = userRows.getCell(2);
				if(userNameCell == null){
					userNameCell = userRows.createCell(2);
				}
				userNameCell.setCellValue(userModel.getUserName());
				userJobNumCell = userRows.getCell(3);
				if(userJobNumCell == null){
					userJobNumCell = userRows.createCell(3);
				}
				userJobNumCell.setCellValue(userModel.getUserJobNum());
			}
			
//			String path = "C:\\Users\\Think\\Desktop\\dingding\\应用\\工资条\\"+companyId+".xls";
//			FileOutputStream fileOut = new FileOutputStream(path);
//			wb.write(fileOut);
//			fileOut.close();
			return wb;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}   catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public boolean commitData(CallContext callContext,SalaryBillCommitModel salaryBillCommitModel){
		String cacheKey = salaryBillCommitModel.getCachekey();
		String fileKey = salaryBillCommitModel.getFilekey();
		String template = salaryBillCommitModel.getTemplate();
		String title = salaryBillCommitModel.getTitle();
		String signs = salaryBillCommitModel.getSigns();
		
		String companyId = callContext.getCorpId();
		String corpName =  callContext.getCorpName();
		String userId =   callContext.getUserId();
		String userName = callContext.getUserName();
		String domainName = "https://"+callContext.getDomainName();
		
		CacheSalaryModel cacheSalaryModel = (CacheSalaryModel) this.cacheableService.getRawObjectFromCache(cacheKey);
		
		if(cacheSalaryModel == null){
			logger.error("get cache data is null,cachekey:"+cacheKey);
			return false;
		}
	
		//有效的通知数据
		List<SalaryBillModel> successSalaryBills = cacheSalaryModel.getSuccessSalaryBills();
		if(successSalaryBills==null || successSalaryBills.size() == 0){
			return false;
		}
		
		List<SalaryBill> salaryBills = new ArrayList<SalaryBill>();
		List<SalaryBillItem> salaryBillItems = new ArrayList<SalaryBillItem>();
		
		String salaryMonth = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
		try {
			Date d = sdf.parse(cacheSalaryModel.getSalaryMonth());
			int year = DateUtils.getYear(d);
			int month = DateUtils.getMonth(d,true);
			salaryMonth = ""+year+ (month<10?"0"+month:month);
		} catch (ParseException e) {
			e.printStackTrace();
			Date d = new Date();
			int year = DateUtils.getYear(d);
			int month = DateUtils.getMonth(d,true);
			salaryMonth = ""+year+ (month<10?"0"+month:month);
		}
		
		String logid = IdGenerator.newId();
		
		//公司短信数据  短信量不足就不发了。
		SalaryBillSms salaryBillSms = salaryBillSmsRepository.findByCorpId(companyId);
		
		List<String> userids = new ArrayList<String>();
		
		int batchCount = 0; //该批次需要发送的短信条数，1个人可能耗用多条
		int nameCount =0; //名字长度
		int companyCount =0; //公司长度
		int salaryCount = 0; //类型长度
		int totalCount = 0; //统计字数
		
		for(SalaryBillModel salaryBillModel : successSalaryBills){
			userids.add(salaryBillModel.getUserId());

			SalaryBill salaryBill = new SalaryBill();
			salaryBill.setId(salaryBillModel.getId());
			salaryBill.setSalaryMonth(salaryMonth);
			salaryBill.setCompanyId(salaryBillModel.getCompanyId());
			salaryBill.setCreateTime(salaryBillModel.getCreateTime());
			salaryBill.setCreateUserId(salaryBillModel.getCreateUserId());
			salaryBill.setDeptId(salaryBillModel.getUserDept());
			salaryBill.setSalaryBillLogId(logid);
			salaryBill.setColumnName(salaryBillModel.getColumnName());
			try {
				salaryBill.setRealSalary(AESUtil.encrypt(salaryBillModel.getRealSalary(), salaryBillModel.getId()));
			} catch (Exception e) {
				salaryBill.setRealSalary(""+0);
				e.printStackTrace();
			}
			salaryBill.setRemark(salaryBillModel.getRemark());
			salaryBill.setSalaryType(salaryBillModel.getSalaryType());
			salaryBill.setUserId(salaryBillModel.getUserId());
			salaryBill.setUserJobNum(salaryBillModel.getUserJobNum());
			salaryBill.setUserName(salaryBillModel.getUserName());
			
			List<SalaryBillItemModel> salaryBillItemModels = salaryBillModel.getSalaryItems();
			
			String smsContent = ""; //短信内容，类型是全部展示时需要
			
			for(SalaryBillItemModel salaryBillItemModel : salaryBillItemModels){
				if(StringUtils.isBlank(salaryBillItemModel.getItemValue())){
					continue;
				}
				SalaryBillItem salaryBillItem = new SalaryBillItem();
				salaryBillItem.setId(salaryBillItemModel.getId());
				salaryBillItem.setCompanyId(salaryBillItemModel.getCompanyId());
				salaryBillItem.setItemName(salaryBillItemModel.getItemName());
				try {
					salaryBillItem.setItemValue(AESUtil.encrypt(salaryBillItemModel.getItemValue(), salaryBillItemModel.getId()));
				} catch (Exception e) {
					salaryBillItem.setItemValue("0");
					e.printStackTrace();
				}
				salaryBillItem.setSalaryBillId(salaryBillModel.getId());
				salaryBillItem.setSerNo(salaryBillItemModel.getSerNo());
				if(salaryBillSms!=null && salaryBillSms.getSmsShow() == SalarySmsModeEnum.SMS_SHOW_ALL.getCode() ){
					smsContent += salaryBillItemModel.getItemName()+":"+salaryBillItemModel.getItemValue()+" ";
				}
				salaryBillItems.add(salaryBillItem);
			}
			if(StringUtils.isNotBlank(smsContent)){
				salaryBill.setSmsContent(smsContent+"，总计："+salaryBillModel.getRealSalary());
			}
			
			if(salaryBillSms!=null){
				nameCount = salaryBillModel.getUserName()==null?2:salaryBillModel.getUserName().length();
				companyCount = corpName.length();
				salaryCount = cacheSalaryModel.getSalrayType().length();
				int tempCount = 23;
				if(salaryBillSms.getSmsShow() == SalarySmsModeEnum.SMS_SHOW_ALL.getCode()){
					int cl = salaryBill.getSmsContent()==null?0:salaryBill.getSmsContent().length();
					tempCount = 27+cl;
				}
				
				totalCount = tempCount+nameCount+companyCount+salaryCount;
				batchCount += totalCount%70 == 0 ? totalCount/70:totalCount/70+1;
			}
			
			salaryBills.add(salaryBill);
		}
		if(salaryBillItems.size()==0){
			salaryBillItems = null;
		}
		try{
			salaryBillRepository.saveSalaryBills(salaryBills, salaryBillItems);
		}catch(Exception e){
			logger.error("save error,main data:"+JSONObject.toJSONString(salaryBills),e);
			return false;
		}
		//log
		//操作人 操作时间 薪资时间 薪资类型 操作
		SalaryBillLog log = new SalaryBillLog();
		log.setId(logid);
		log.setCompanyId(companyId);
		log.setCorpName(corpName);
		log.setCreateTime(new Date());
		log.setFileKey(fileKey);
		log.setSalaryMonth(salaryMonth);
		log.setSalaryType(cacheSalaryModel.getSalrayType());
		log.setUserId(userId);
		log.setUserName(userName);
		log.setTemplate(template);
		log.setTitle(title);
		log.setSigns(signs);
		log.setBatchCount(batchCount); //本次需要发送的短信量
		
		//默认发钉钉消息，1个小时未读发送短信通知
		if(salaryBillSms!=null && salaryBillSms.getSmsMode()==1){
			log.setSmsStatus(0); //设置短信状态未发送
		}
		
		salaryLogRepository.insert(log);
		
		
		//发钉钉消息
		String appid = SalaryBillConfigure.appId;
		String suiteid = SalaryBillConfigure.suiteId ;
		
		String url = domainName + "/alid/app/salarybill/salarybillbiz/autologin?corpid="+companyId +
				"&appid=" + appid + "&suiteid=" + suiteid +
				"&dd_nav_bgcolor=FF3B81F5&dd_share=false&sid="+logid+"&sm="+salaryMonth;
		
		String text = "您有一条新的 "+cacheSalaryModel.getSalrayType()+" 信息，点击查看详情";
		
		//分批发消息
		int size = userids.size();
		
		if(size<=100){
			 sendDingOAMessage(companyId,userids,url,text);
		}else{
			List<String> l1 = new ArrayList<String>();
			for(int i = 0 ; i < size ; i ++){
				l1.add(userids.get(i));
				if(i!= 0 && i%100 == 0){
					//发消息
					sendDingOAMessage(companyId,l1,url,text);
					//清空l1
					l1 = new ArrayList<String>();
				}
				else if(i == size-1){
					//发消息
					sendDingOAMessage(companyId,l1,url,text);
					//清空l1
					l1 = new ArrayList<String>();
				}
			}
		}
		
		//立即发送短信
		if(salaryBillSms!=null && (salaryBillSms.getSmsMode()==2 ||salaryBillSms.getSmsMode() == 3) && salaryBillSms.getTotal()>0){
			Thread thread = startSendSms(companyId,corpName, cacheSalaryModel.getSalrayType(),logid, salaryBills, salaryBillSms);
			thread.start();
		}
	
		return true;
	}
	
	public Thread startSendSms(final String companyId,final String corpName,final String salaryType,final String logid,final List<SalaryBill> salaryBills,final SalaryBillSms salaryBillSms){
		Thread thread = new Thread() {
			@Override
			public void run() {
				sendSms(companyId,corpName,salaryType,logid,salaryBills,salaryBillSms);
			}
		};
		return thread;
	}
	
	public void sendSms(String companyId,String corpName,String salaryType,String logid,List<SalaryBill> salaryBills ,SalaryBillSms salaryBillSms) {
		 //查找本次发送人员的信息
		 
		 List<SalaryBillReadRecordModel> salaryLogUsers = salaryLogRepository.getSendSmsUsers(companyId,salaryBills,salaryBillSms.getSmsMode());
		 
		 if(salaryLogUsers==null || salaryLogUsers.size()==0){
			 return;
		 }
		 //list to map
		 Map<String,String> map = new HashMap<String,String>();
		 for(SalaryBillReadRecordModel salaryLogUser : salaryLogUsers){
			 map.put(salaryLogUser.getUserId(), salaryLogUser.getMobileNo());
		 }
		 
		 //发送手机短信
         ShortMessageBody msg = new ShortMessageBody();
         msg.setExtend(null);
         msg.setSmsFreeSignName("悦通知");
         Map<String, Object> smsParams = new HashMap<>();
         smsParams.put("company", corpName);
         smsParams.put("salary", salaryType);
         msg.setSmsTemplateCode(salaryBillSms.getSmsShow()==SalarySmsModeEnum.SMS_SHOW_ALL.getCode()?"SMS_50360017":"SMS_50255007");
         
         int total = salaryBillSms.getTotal(); //发送之前剩余的短信量
         
         int nameCount = 0; int companyCount = 0 ; int salaryCount = 0 ;
         int tempCount = 23; int totalCount = 0 ;int bachCount = 0;
         for(SalaryBill salaryBill : salaryBills){
        	 
        	 if(total<1){ //短信量为0 或允许为一个负数 则该批次不发送短信了。
        		 break;
        	 }
        	 
        	 smsParams.put("name", salaryBill.getUserName());
        	 if(salaryBillSms.getSmsShow()==SalarySmsModeEnum.SMS_SHOW_ALL.getCode()){
				smsParams.put("content", salaryBill.getSmsContent());
				tempCount = 27 + salaryBill.getSmsContent().length();
			 }
        	 msg.setRecNum(map.get(salaryBill.getUserId()));//,号隔开
	         msg.setSmsParam(smsParams);
	         if(StringUtils.isBlank(msg.getRecNum())){
	        	 logger.error(corpName+","+salaryBill.getUserId()+","+salaryBill.getId()+",send sms fail,no mobile");
	         }else{
	        	 KafkaProducer.getInstance().sendMessage(KafkaTopics.YWORK_DING_SMS.getTopic(), msg);
	        	 nameCount = salaryBill.getUserName().length();
	        	 companyCount = corpName.length();
	        	 salaryCount = salaryType.length();
	        	 //总字数
	        	 totalCount = tempCount+nameCount+companyCount+salaryCount;
	        	 bachCount += totalCount%70 == 0 ? totalCount/70:totalCount/70+1;
	        	 
	        	 total = total - (totalCount%70 == 0 ? totalCount/70:totalCount/70+1);
	         }
         }
         salaryLogRepository.updateSmsStatus(companyId,logid, bachCount);
         salaryBillSmsRepository.updateSms(companyId, bachCount); // 更新公司的短信数据
         
	}
	//private final static ShortMessageService shortMessageService = SMSFactory.getShortMessageService();
	
	public static void main(String[] args){
		int totalCount = 190;
		int bachCount = totalCount%70 == 0 ? totalCount/70:totalCount/70+1;
		System.out.println(bachCount);
	}
	
	@Override
	public List<SalaryBillTemplateModel> hasDataSystemTemplate(String companyId, String userId,String salaryMonth) {
		
		List<SalaryBillTemplateModel> templateList = salaryBillRepository.hasDataSystemTemplate(companyId, userId,salaryMonth);
		if(templateList == null){
			templateList = new ArrayList<SalaryBillTemplateModel>();
		}
		return templateList;
	
	}
	


	@Override
	public List<SalaryBillTemplateModel> myTemplates(String companyId) {
		List<SalaryBillTemplateModel> templateList = salaryBillRepository.myTemplates(companyId);
		if(templateList == null){
			templateList = new ArrayList<SalaryBillTemplateModel>();
		}
		return templateList;
	}


	@Override
	public CacheSalaryModel parserExcel(CallContext callContext,String fileId) {
		
		CacheSalaryModel salaryModel = new CacheSalaryModel();
		List<String> mainTitles = new ArrayList<String>();
		List<String> detailTitles = new ArrayList<String>();
		List<SalaryBillModel> successSalaryBills = new ArrayList<SalaryBillModel>();
		List<SalaryBillModel> errorSalaryBills = new ArrayList<SalaryBillModel>();
		List<SalaryBillModel> tempSalaryBills = new ArrayList<SalaryBillModel>();
		String tips = "";
		String cachekey = "";
		salaryModel.setFileKey(fileId);
		try{
			InputStream instream = OSSObjectService.getObject(SalaryBillConstant.BUCK_NAME, fileId);
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
			
			//HSSFWorkbook wb  = new HSSFWorkbook(fs);
			Sheet sheet = wb.getSheetAt(0);
			int rsColumns = sheet.getLastRowNum();
			//找时间和类型
			String salaryMonth = "";
			String type = "";
			Row daterow = null;
			Cell cell = null;
			String cellValue = "";
			
			int temp = 0 ; //数据行定位
			int itemTemp = 0 ; // 数据明细列定位
			String columnName = "";
			
			//员工UserID必须在第一列 作为标志
			//明细列key intger列 String值
			Map<Integer,String> items = new TreeMap<Integer,String>();
			
			int remark = 0; // 备注列
			
			for(int i = 0 ; i < 10 ; i ++){
				daterow = sheet.getRow(i);
				if(daterow == null){
					continue;
				}
				cell = daterow.getCell(0);
				cellValue = getCellValue(cell);//cell.getStringCellValue().trim();
				
				if("时间".equals(cellValue)){
					daterow = sheet.getRow(i+1);
					cell = daterow.getCell(0);
					salaryMonth = getCellTimeValue(cell);
					boolean b = salaryMonth.matches("[0-9]{4}年(0\\d{1}|1[0-2]|[1-9]{1})月");
				    if(!b){
				    	//格式不正确 直接跳过 不解析数据
				    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
				    	try{
					    	Date d = cell.getDateCellValue();
					    	salaryMonth = sdf.format(d);
				    	}catch(Exception e){
				    		e.printStackTrace();
				    	}
				    	b = salaryMonth.matches("[0-9]{4}年(0\\d{1}|1[0-2]|[1-9]{1})月");
				    	if(!b){
				    		tips = "excel填写的时间格式不正确。正确格式如:"+ sdf.format(new Date());
				    		salaryModel.setTips(tips);
				    		return salaryModel;
				    	}
				    }
				    salaryModel.setSalaryMonth(salaryMonth);
				    
					cell = daterow.getCell(1);
					type = getCellValue(cell);
					if(StringUtils.isBlank(type)){
						tips = "您未填写类型";
				    	salaryModel.setTips(tips);
				    	return salaryModel;
					}
					salaryModel.setSalrayType(type);
				}
				
				//定位到title行
				else if("员工UserID".equals(cellValue)){
					mainTitles.add(cellValue);
					for(int j = 1; j<daterow.getLastCellNum();j++){
						cell = daterow.getCell(j);
						if(cell==null){
							continue;
						}
						String title = getCellValue(cell);
						if(StringUtils.isBlank(title)){
							continue;
						}
						if("备注".equals(title)){
							remark = j;
						}
						else if (j == 5){
							itemTemp = j + 1;
							columnName = title;
						}
						//else if("总计".equals(title)){
						//	itemTemp = j+1;
						//}
						
						if(itemTemp>0 && itemTemp<j+1){
							detailTitles.add(title);
							items.put(j, title);
						}else{
							mainTitles.add(title);
						}
					}
					salaryModel.setDetailTitles(detailTitles);
					salaryModel.setMainTitles(mainTitles);
					temp = i+1;
					break;
				}
			}
			
			cachekey = callContext.getCorpId()+callContext.getUserId()+System.currentTimeMillis();
			//key加密
			salaryModel.setCacheKey(cachekey);
			
			//数据
			String userId = "";
			String deptId = "";
			String userName = "";
			String userJobNum = "";
			String reMark = "";
			String realSalary = "";
			
			for(int k = temp ; k < rsColumns+1 ; k++){
				
				daterow = sheet.getRow(k);
				if(daterow==null){
					continue;
				}
				//UserID列
				cell = daterow.getCell(0);
				userId =  getCellValue(cell);
				if(StringUtils.isBlank(userId)){
					continue;
				}
				
				SalaryBillModel salaryBillModel = new SalaryBillModel();
				
				salaryBillModel.setId(IdGenerator.newId());
				salaryBillModel.setCompanyId(callContext.getCorpId());
				salaryBillModel.setCreateTime(new Date());
				salaryBillModel.setSalaryMonth(salaryMonth);
				salaryBillModel.setSalaryType(type);
				salaryBillModel.setCreateUserId("");
				salaryBillModel.setUserId(userId);
				
				//deptId列
				cell = daterow.getCell(1);
				deptId =  getCellValue(cell);
				salaryBillModel.setUserDept(deptId);
				//userName列
				cell = daterow.getCell(2);
				userName =  getCellValue(cell);
				salaryBillModel.setUserName(userName);
				//userJobNum列
				cell = daterow.getCell(3);
				userJobNum =  getCellValue(cell);
				salaryBillModel.setUserJobNum(userJobNum);
				//总计
				cell = daterow.getCell(itemTemp-1);
				realSalary = getCellValue(cell);
				try{
					int ind = realSalary.indexOf(".");
					if(ind>0 && ind+3<realSalary.length()){
						realSalary = realSalary.substring(0, ind+3);
						if("0".equals(realSalary.substring(realSalary.length()-1,realSalary.length()))){
							realSalary = realSalary.substring(0,ind+2);
						}
					}
				}catch(Exception e){
					//e.printStackTrace();
				}
				salaryBillModel.setRealSalary(realSalary);
				//备注列
				if(remark>0){
					cell = daterow.getCell(remark);
					reMark = getCellValue(cell);
					salaryBillModel.setRemark(reMark);
				}
				//“总计”列，名称可修改
				salaryBillModel.setColumnName(columnName);
				
				//明细列数据
				List<SalaryBillItemModel> salaryBillItems = new ArrayList<SalaryBillItemModel>();
				Set<Integer> set = items.keySet();
				String itemValue = "";
				int serNo = 1;
				for(Iterator<Integer> iter = set.iterator(); iter.hasNext();){
					 Integer key = iter.next();
					 cell = daterow.getCell(key);
				   itemValue = getCellValue(cell);
//				   if(StringUtils.isBlank(itemValue)){ //明细字段为空的不展示也不保存
//					   continue;
//				   }
				   SalaryBillItemModel salaryBillItem = new SalaryBillItemModel();
				   salaryBillItem.setId(IdGenerator.newId());
				   salaryBillItem.setCompanyId(callContext.getCorpId());
				   salaryBillItem.setSerNo(serNo++);
				   String itemKey = items.get(key);
				   salaryBillItem.setItemName(itemKey);
				  
				   salaryBillItem.setItemValue(itemValue);
				   //salaryBillItem.setSalaryBillId(salaryBillModel.getId());
				   salaryBillItems.add(salaryBillItem);
				}
				salaryBillModel.setSalaryItems(salaryBillItems);
				
				//成功or失败数据？ 2个校验点   1总计为空或不为数字  2userid不在系统里
				
//				else{
//					successSalaryBills.add(salaryBillModel);
//				}
				tempSalaryBills.add(salaryBillModel);
			}
			List<String> notExistUsers = null;
			if(tempSalaryBills != null && tempSalaryBills.size()>0){
				notExistUsers = salaryBillRepository.notExistsUser(callContext.getCorpId(), tempSalaryBills);
			}
			boolean allRight = false;
			if(notExistUsers == null || notExistUsers.size() == 0){
				allRight = true;
			}
			if(tempSalaryBills != null && tempSalaryBills.size()>0){
				for(SalaryBillModel salaryBillModel : tempSalaryBills){
					String reason = "";
	//				boolean strResult = salaryBillModel.getRealSalary().matches("-?\\d+\\.?\\d*");
	//				if(!strResult){
	//					reason += "总计为空.";
	//				}
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
			
			salaryModel.setErrorSalaryBills(errorSalaryBills);
			salaryModel.setSuccessSalaryBills(successSalaryBills);
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
	
	
	private String getCellTimeValue(Cell cell) {
		if(cell==null){
			return "";
		}
        String cellValue = "";
        switch (cell.getCellType()) {
        case HSSFCell.CELL_TYPE_STRING:
            cellValue = cell.getRichStringCellValue().getString().trim();
            break;
        case HSSFCell.CELL_TYPE_NUMERIC:
            cellValue = cell.getNumericCellValue()+"";
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
            	Date d = cell.getDateCellValue();
            }
            break;
        default:
            cellValue = "";
        }
        return cellValue;
    }
	
	/**
	 * cassandra缓存appKey
	 */
	public static final String canssandraCacheAppKey = "lanyun-salarybill-" + SalaryBillConfigure.suiteId + "-"
			+ SalaryBillConfigure.appId;


	@Override
	public boolean deleteById(CallContext callContext, String id)  {
		return false;
	}
	
	
	private int sendDingOAMessage(String companyId, List<String> userIds,String url,String text) {
		DingCorpMessageHeader messageHeader = null;
		DingOAMessage dingOAMessage = null;
		DingCorpMessageResult result = null;
		messageHeader = new DingCorpMessageHeader();
		//oa消息类型
		messageHeader.setMsgtype(DingMessageType.oa);
		messageHeader.appendReceiveUserIds(userIds);
		dingOAMessage = new DingOAMessage();
		dingOAMessage.setHead("FF3B81F5", "悦通知");
		dingOAMessage.setBodyTitle(text);
//		DingOABody body = new DingOABody();
//		body.setContent(text);
//		dingOAMessage.setBody(body);
		dingOAMessage.setMessage_url(url);
		
		String appid = SalaryBillConfigure.appId;
		String suiteid = SalaryBillConfigure.suiteId ;
		
		result = getDingAPIRpcService().sendDingCorpMessage(companyId,
				suiteid, appid, messageHeader, dingOAMessage);
		logger.info("send vote message，corpid:{},result:{}",companyId ,JSON.toJSONString(result));
		if(result==null){
			return 0 ;
		}
		return result.getErrcode();
	}
	

	private static final String agent_key = "app_agent_redis";
	
	@Override
	public String findAgentId(String corpId, String suiteId, String appId) {
		//redis存储agentid
		String agentId = "";
		String cacheKey = corpId + suiteId + appId + agent_key;
		Object cache = cacheableService.getRawObjectFromCache(cacheKey);
		
		if (cache != null) {
			agentId = (String) cache;
		}
		
		if(StringUtils.isBlank(agentId)){
			agentId = salaryBillRepository.findAgentId(corpId, suiteId, appId);
			if(StringUtils.isNotBlank(agentId)){
				cacheableService.setRawObjectInCache(cacheKey, agentId, 1, TimeUnit.DAYS);
			}else{
				logger.error("agentid cache error,corpid:"+corpId+",suiteid:"+suiteId+",appid:"+appId);
			}
		}
		
		return agentId;
	}


	@Override
	public void sendVote(List<String> appids) {
		//投票 263 134
		String suiteId = "573";
		String appid = "134";
		List<CorpAdminMessage> corpList = salaryBillRepository.getCorpByAppId(appid);
		
		Map<String, List<CorpAdminMessage>> map = CollectionUtils
				.groupCollection(corpList);
		String vurl = "http://b.mashort.cn/Y.ZXLH";
		String vText = "感谢您对悦投票的支持，我们设计了一份小问卷，来发一分钟填写，得到更好的悦投票。";
		
		
		for (Map.Entry<String, List<CorpAdminMessage>> entry : map.entrySet()) {
			try{
				String company = entry.getKey();
				List<CorpAdminMessage> mlist = entry.getValue();
				List<String> userIds = new ArrayList<String>();
				for(CorpAdminMessage m:mlist ){
					userIds.add(m.getUserId());
				}
				sendDingOAMessage2(company,suiteId,appid,userIds,vurl,vText,"悦投票调查问卷","悦投票");
			}catch(Exception e){
				e.printStackTrace();
			}
        }
		
		
		//任务339 482
		String appid2 = "482";
		String tText = "感谢您对悦任务的支持，我们设计了一份小问卷，来发一分钟填写，得到更好的悦任务。";
		List<CorpAdminMessage> corpList2 = salaryBillRepository.getCorpByAppId(appid2);
		Map<String, List<CorpAdminMessage>> map2 = CollectionUtils
				.groupCollection(corpList2);
		String turl = "http://b.mashort.cn/Y.Z2ZB";
		for (Map.Entry<String, List<CorpAdminMessage>> entry : map2.entrySet()) {
			try{
				String company = entry.getKey();
				List<CorpAdminMessage> mlist = entry.getValue();
				List<String> userIds = new ArrayList<String>();
				for(CorpAdminMessage m:mlist ){
					userIds.add(m.getUserId());
				}
				sendDingOAMessage2(company,suiteId,appid,userIds,turl,tText,"悦任务调查问卷","悦任务");
			}catch(Exception e2){
				e2.printStackTrace();
			}
        }
		
	}
	
	
	private int sendDingOAMessage2(
			String companyId,
			String suiteId,
			String appId,
			List<String> userIds,
			String url,
			String text,
			String title,
			String appName
			) {
		DingCorpMessageHeader messageHeader = null;
		DingOAMessage dingOAMessage = null;
		DingCorpMessageResult result = null;
		messageHeader = new DingCorpMessageHeader();
		//oa消息类型
		messageHeader.setMsgtype(DingMessageType.oa);
		messageHeader.appendReceiveUserIds(userIds);
		dingOAMessage = new DingOAMessage();
		dingOAMessage.setHead("FF3B81F5", appName);
		dingOAMessage.setBodyTitle(title);
		dingOAMessage.setMessage_url(url);
		DingOABody body = new DingOABody();
		body.setTitle(title);
		body.setImage("@lADOBSKOOc0BXc0CbA");
		body.setContent(text);
		dingOAMessage.setBody(body);
		dingOAMessage.setPc_message_url(url);
		
		result = getDingAPIRpcService().sendDingCorpMessage(companyId,
				suiteId, appId, messageHeader, dingOAMessage);
		logger.error("sendvote li message，result:{}", JSON.toJSONString(result));
		if(result==null){
			return 0 ;
		}
		return result.getErrcode();
	}

	@Override
	public SalaryBillNoPwdModel findDingElementById(String companyId, String id,String type) {
		return salaryBillRepository.findDingElementById(companyId,id,type);
	}


	@Override
	public List<OrgTreeModel> getOrgSubDetpByDeptId(String companyId,String deptId) {
		List<OrgTreeModel> orgTreeModels = salaryBillRepository.findOrgSubDetpByDeptId(companyId,deptId);
		return orgTreeModels;
	}

	@Override
	public List<OrgTreeModel> getOrgUserByDeptId(String companyId, String deptId) {
		List<OrgTreeModel> orgTreeModels = salaryBillRepository.findOrgUserByDeptId(companyId,deptId);
		return orgTreeModels;
	}

	@Override
	public String getCorpName(String companyId) {
		// TODO Auto-generated method stub
		String name = salaryBillRepository.findCorpName(companyId);
		return name;
	}
	
}
