package me.ywork.salarybill.controller;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.redisson.cache.CacheableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.heyi.framework.spring.context.AppContext;

import me.ywork.context.CallContext;
import me.ywork.controller.RestController;
import me.ywork.oss.OSSObjectService;
import me.ywork.page.PageData;
import me.ywork.page.PageRequest;
import me.ywork.page.Pageable;
import me.ywork.salarybill.SalaryBillConfigure;
import me.ywork.salarybill.base.SalaryBillConstant;
import me.ywork.salarybill.base.SalaryPwdType;
import me.ywork.salarybill.model.CacheSalaryMobileModel;
import me.ywork.salarybill.model.CacheSalaryModel;
import me.ywork.salarybill.model.OrgDataModel;
import me.ywork.salarybill.model.OrgItemResult;
import me.ywork.salarybill.model.OrgTreeModel;
import me.ywork.salarybill.model.PwdCheckResult;
import me.ywork.salarybill.model.SalaryBillAdminViewModel;
import me.ywork.salarybill.model.SalaryBillCommitModel;
import me.ywork.salarybill.model.SalaryBillLogModel;
import me.ywork.salarybill.model.SalaryBillMobileModel;
import me.ywork.salarybill.model.SalaryBillModel;
import me.ywork.salarybill.model.SalaryBillNoPwdSelectedItemViewMode;
import me.ywork.salarybill.model.SalaryBillPwdSetModel;
import me.ywork.salarybill.model.SalaryBillReadRecordModel;
import me.ywork.salarybill.model.SalaryBillTemplateModel;
import me.ywork.salarybill.model.SalarySmsMode;
import me.ywork.salarybill.model.SearchUserModel;
import me.ywork.salarybill.model.SmsReportModel;
import me.ywork.salarybill.model.UserModel;
import me.ywork.salarybill.service.SalaryBillAdminService;
import me.ywork.salarybill.service.SalaryBillLogService;
import me.ywork.salarybill.service.SalaryBillPwdSetService;
import me.ywork.salarybill.service.SalaryBillService;
import me.ywork.salarybill.service.SalaryBillSmsService;
import me.ywork.suite.api.model.CorpAdminRpcModel;
import me.ywork.suite.api.rpc.IDingAPIRpcService;
import me.ywork.util.AESUtil;

@Controller
@RequestMapping(value = { "**/salarybillpcbiz" })
public class SalaryBillPcController extends
		RestController<SalaryBillService> {

	private static Logger logger = LoggerFactory.getLogger(SalaryBillPcController.class);

	private static final String PC_TOKEN = "salarymanager";
	
	private static final String AES_KEY = "c5a1149f163dd7a072e235ccc2566c98";

	@Autowired
	protected SalaryBillPwdSetService salaryBillPwdSetService;
	
	@Autowired
	private CacheableService cacheableService;
	
	private IDingAPIRpcService dingAPIRpcService;
	
	private IDingAPIRpcService getDingAPIRpcService() {
		if (dingAPIRpcService == null) {
			dingAPIRpcService = (IDingAPIRpcService) AppContext.getBean("dingAPIRpcService");
		}
		return dingAPIRpcService;
	}
	
	@Autowired
	protected SalaryBillService salaryBillService;
	
	@Autowired
	protected SalaryBillLogService salaryBillLogService;
	
	@Autowired
	protected SalaryBillAdminService salaryBillAdminService;
	
	@Autowired
	private SalaryBillSmsService salaryBillSmsService;
	
	
	/**
	 * 进入主页面
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = { "/test**" }, method = RequestMethod.GET)
	public void test(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String cid = request.getParameter("cid");
	}

	public static void main(String[] args){
		CorpAdminRpcModel adminInfo = new CorpAdminRpcModel();
		adminInfo.setAdmin(false);
		adminInfo.setAvatar("ava");
		adminInfo.setBoss(false);
		adminInfo.setCorpId("cid");
		adminInfo.setCorpName("xxx");
		adminInfo.setSupAdmin(false);
		adminInfo.setUserId("uid");
		adminInfo.setUserName("un");
		String s = JSONObject.toJSONString(adminInfo);
		System.out.println(s);
		
		JSONObject jo = JSONObject.parseObject(s);
		boolean f = jo.getBoolean("boss");
		System.out.println(f);
				
	}
	/**
	 * 进入主页面
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = { "/index**" }, method = RequestMethod.GET)
	public void index(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String code = request.getParameter("code");
		String cookieValue = getUserCookie(request);
		String corpId = "";
		
		boolean isReLogin = false;
		CorpAdminRpcModel adminInfo = null;
		// 判断code是否为空，为空则取cookie里的用户信息
		if (StringUtils.isBlank(code) && StringUtils.isBlank(cookieValue)) {
			isReLogin = true;
		}else{
			try {
				adminInfo = getDingAPIRpcService().getPcAdminInfo(code,SalaryBillConfigure.suiteId);
				corpId = adminInfo.getCorpId();
			} catch (Exception e) {
				logger.error("PC后台免登发生错误", e);
			}
			
			if (adminInfo != null) {
				String cookie = AESUtil.encrypt(JSONObject.toJSONString(adminInfo),AES_KEY);
				Cookie TOKEN = new Cookie(PC_TOKEN, cookie);
				TOKEN.setPath("/");
				TOKEN.setMaxAge(30 * 60 * 6);
				response.addCookie(TOKEN);
			}
		}

		String domainName = request.getServerName();
		if (StringUtils.isBlank(domainName)) {
			domainName = SalaryBillConfigure.domainName;
		}

		if (isReLogin) {
			response.sendRedirect("//"+domainName + "/yw/pc/notify/web/" + "/relogin.html");
			return;
		}
		
		response.sendRedirect("//"+domainName + "/yw/pc/notify/web/index.html?corpid="+corpId);
		return;
	}


	/**
	 * 下载通知模板
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = { "/download**" }, method = RequestMethod.GET)
	public void download(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try{
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			response.setHeader("Content-disposition","attachment; filename=temple.xls");
			CallContext callContext = this.getPcCallContext(request);
			
			String companyId =callContext.getCorpId();
			if(StringUtils.isBlank(companyId)){
				this.writePermissionDeniedJsonToClient(response,null);
				return ;
			}
			HSSFWorkbook wb = salaryBillService.exportToExcel(companyId);
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			wb.write(out);
			InputStream inputStream = new ByteArrayInputStream(out.toByteArray());
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
			String fileId = companyId+"/"+sdf.format(new Date())+".xls";
			
			String buckname = "alidsalarybill";
			
			OSSObjectService.uploadExcelFile(buckname,fileId, inputStream);
			
			String fileUrl = OSSObjectService.getFileUrl(buckname, fileId);
	
			if(inputStream!=null){
				inputStream.close();
			}
			if(wb!=null){
				wb.close();
			}
			this.writeSuccessJsonToClient(response, fileUrl);
		}catch(Exception e){
			logger.error("download", e);
			this.writeFailJsonToClient(response);
		}
	}
	
	/**
	 * 上传通知信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = { "/upload**" }, method = RequestMethod.GET)
	public void upload(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("fileid") String fileId)
			throws Exception {
		
		fileId = URLDecoder.decode(fileId);
		
		CallContext callContext = this.getPcCallContext(request);
		if(StringUtils.isBlank(callContext.getCorpId())){
			this.writePermissionDeniedJsonToClient(response,null);
			return ;
		}
		
		try{
			CacheSalaryModel cacheSalaryModel = salaryBillService.parserExcel(callContext,fileId);
			//错误明细
			List<SalaryBillModel> errorSalaryBills = cacheSalaryModel.getErrorSalaryBills();
			int errCount = errorSalaryBills == null ? 0 : errorSalaryBills.size();
			cacheSalaryModel.setErrorCount(errCount);
			//成功明细
			List<SalaryBillModel> successSalaryBills = cacheSalaryModel.getSuccessSalaryBills();
			int successCount = successSalaryBills == null ? 0 : successSalaryBills.size();
			cacheSalaryModel.setSuccessCount(successCount);
			//50一页
			int pageSize = 50;
			if(errCount+successCount>pageSize){
				cacheSalaryModel.setHasNext(true);
			}else{
				cacheSalaryModel.setHasNext(false);
			}
			
			//取50 model
			int count = errCount+successCount > pageSize ? pageSize : errCount+successCount;
			List<SalaryBillModel> salarys = new ArrayList<SalaryBillModel>(count);
			for(int i = 0 ; i < count ; i ++){
				if(i<errCount){
					salarys.add(errorSalaryBills.get(i));
				}else{
					salarys.add(successSalaryBills.get(i-errCount));
				}
			}
			cacheSalaryModel.setErrorSalaryBills(null);
			cacheSalaryModel.setSuccessSalaryBills(null);
			cacheSalaryModel.setSalaryBills(salarys);
			this.writeSuccessJsonToClient(response, cacheSalaryModel);
		}catch(Exception e){
			logger.error("upload", e);
			this.writeFailJsonToClient(response);
		}
	}
	
	/**
	 * 分页展示通知信息
	 * @param request
	 * @param response
	 * @param cachekey
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = { "/nextrecord**" }, method = RequestMethod.GET)
	public void nextRecord(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("cachekey") String cachekey,@RequestParam("page") int page)
			throws Exception {
		
		CallContext callContext = this.getPcCallContext(request);
		if(StringUtils.isBlank(callContext.getCorpId())){
			this.writePermissionDeniedJsonToClient(response,null);
			return ;
		}
		try{
			CacheSalaryModel cacheSalaryModel = (CacheSalaryModel) this.cacheableService.getRawObjectFromCache(cachekey);
			//err
			List<SalaryBillModel> errorSalaryBills = cacheSalaryModel.getErrorSalaryBills();
			int errCount = errorSalaryBills == null ? 0 : errorSalaryBills.size();
			cacheSalaryModel.setErrorCount(errCount);
			//succ
			List<SalaryBillModel> successSalaryBills = cacheSalaryModel.getSuccessSalaryBills();
			int successCount = successSalaryBills == null ? 0 : successSalaryBills.size();
			cacheSalaryModel.setSuccessCount(successCount);
			
			//50一页
			int pageSize = 50;
			if(errCount+successCount > page*pageSize){
				cacheSalaryModel.setHasNext(true);
			}else{
				cacheSalaryModel.setHasNext(false);
			}
			//取50 model
			int start = pageSize*(page-1);
			int count = errCount+successCount > start+pageSize ? start+pageSize : errCount+successCount;
			List<SalaryBillModel> salarys = new ArrayList<SalaryBillModel>(count);
			for(int i = start ; i < count ; i ++){
				if(i<errCount){
					salarys.add(errorSalaryBills.get(i));
				}else{
					salarys.add(successSalaryBills.get(i-errCount));
				}
			}
			cacheSalaryModel.setErrorSalaryBills(null);
			cacheSalaryModel.setSuccessSalaryBills(null);
			cacheSalaryModel.setSalaryBills(salarys);
			this.writeSuccessJsonToClient(response, cacheSalaryModel);
		}catch(Exception e){
			logger.error("nextrecord", e);
			this.writeFailJsonToClient(response);
		}
	}
	
	
	/**
	 * 通知数据提交
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = { "/commit**" }, method = RequestMethod.POST)
	public void commit(HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		CallContext callContext = this.getPcCallContext(request);
		if(StringUtils.isBlank(callContext.getCorpId())){
			this.writePermissionDeniedJsonToClient(response,null);
			return ;
		}
		SalaryBillCommitModel salaryBillCommitModel = this.fromInputJson(request,SalaryBillCommitModel.class);
		try{
			boolean flag = salaryBillService.commitData(callContext,salaryBillCommitModel);
			this.writeSuccessJsonToClient(response, flag);
		}catch(Exception e){
			logger.error("commit", e);
			this.writeFailJsonToClient(response);
		}
	}
	

	
	/**
	 * 管理员是否设置密码
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "/eversetpwd**" }, method = RequestMethod.GET)
	public void eversetpwd(HttpServletRequest request, HttpServletResponse response) {
		try {
			CallContext callContext = this.getPcCallContext(request);
			String companyId = callContext.getCorpId();
			String userId = callContext.getUserId();
			if(StringUtils.isBlank(companyId)){
				this.writePermissionDeniedJsonToClient(response,null);
				return ;
			}
			Boolean result = salaryBillPwdSetService.evensetpwd(companyId,userId,SalaryPwdType.Manager.getCode());
			// 向客户端返回，结束请求
			this.writeSuccessJsonToClient(response, result);
		} catch (Exception e) {
			logger.error("setuserpwd", e);
			this.writeFailJsonToClient(response);
			return;
		}
	}
	
	/**
	 * 管理员设置后台密码
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "/setuserpwd**" }, method = RequestMethod.POST)
	public void setuserpwd(HttpServletRequest request, HttpServletResponse response) {
		try {
			CallContext callContext = this.getPcCallContext(request);
			String companyId = callContext.getCorpId();
			String userId = callContext.getUserId();
			if(StringUtils.isBlank(companyId)){
				this.writePermissionDeniedJsonToClient(response,null);
				return ;
			}
			SalaryBillPwdSetModel salaryBillPwdSetModel = this.fromInputJson(request, SalaryBillPwdSetModel.class);
			
			Boolean result = salaryBillPwdSetService.setUserPwd(companyId,userId,SalaryPwdType.Manager.getCode(),salaryBillPwdSetModel);
			// 向客户端返回，结束请求
			this.writeSuccessJsonToClient(response, result);
		} catch (Exception e) {
			logger.error("setuserpwd", e);
			this.writeFailJsonToClient(response);
			return;
		}
	}
	
	/**
	 * 后台管理员重设置密码
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "/resetuserpwd**" }, method = RequestMethod.POST)
	public void resetuserpwd(HttpServletRequest request, HttpServletResponse response) {
		try {
			CallContext callContext = this.getPcCallContext(request);
			String companyId = callContext.getCorpId();
			String userId = callContext.getUserId();
			if(StringUtils.isBlank(companyId)){
				this.writePermissionDeniedJsonToClient(response,null);
				return ;
			}
			SalaryBillPwdSetModel salaryBillPwdSetModel = this.fromInputJson(request, SalaryBillPwdSetModel.class);
			//salaryBillPwdSetModel.setPasswordType(SalaryPwdType.Manager.getCode());
			String result = salaryBillPwdSetService.resetUserPwd(companyId,userId,SalaryPwdType.Manager.getCode(),salaryBillPwdSetModel);
			JSONObject rejson = new JSONObject();
			if(StringUtils.isBlank(result)){
				rejson.put("update", true);
			}else{
				rejson.put("update", false);
				rejson.put("mess", result);
			}
			this.writeSuccessJsonToClient(response,rejson);
		} catch (Exception e) {
			logger.error("resetuserpwd", e);
			this.writeFailJsonToClient(response);
			return;
		}
	}
	
	/**
	 * 管理员密码校验
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "/comparepwd**" }, method = RequestMethod.POST)
	public void comparepwd(HttpServletRequest request, HttpServletResponse response) {
		try {
			CallContext callContext = this.getPcCallContext(request);
			String companyId = callContext.getCorpId();
			String userId = callContext.getUserId();
			if(StringUtils.isBlank(companyId)){
				this.writePermissionDeniedJsonToClient(response,null);
				return ;
			}
			
			SalaryBillPwdSetModel salaryBillPwdSetModel = this.fromInputJson(request, SalaryBillPwdSetModel.class);
			
			salaryBillPwdSetModel.setPasswordType(SalaryPwdType.Manager.getCode());
			
			PwdCheckResult pwdCheckResult = salaryBillPwdSetService.comparePwd(companyId,userId,salaryBillPwdSetModel);
			
			this.writeSuccessJsonToClient(response, pwdCheckResult);
		} catch (Exception e) {
			logger.error("comparepwd", e);
			this.writeFailJsonToClient(response);
			return;
		}
	}
	
	
	private String getUserCookie(HttpServletRequest request) throws Exception {
		String cookieToken = "";
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (PC_TOKEN.equals(cookie.getName())) {
					cookieToken = AESUtil.decrypt(cookie.getValue(), AES_KEY);//(,AES_KEY);
					break;
				}
			}
		}
		return cookieToken;
	}
	
	
	/**
	 * 以分页方式返回阅读记录
	 * @param pageable     分页信息
	 * @return
	 */
	@RequestMapping(value = { "/getDetails**" }, method = RequestMethod.GET)
	public void getSalaryBillReadRecord(
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam("pageSize") int pageSize,
			@RequestParam("pageNo") int pageNo,
			@RequestParam(required = false, defaultValue = "0") long totalCount,
			@RequestParam("id") String logid,@RequestParam("total") long total) {
		try {
			Pageable pageable = new PageRequest(pageNo,pageSize,totalCount);
			CallContext callContext = this.getPcCallContext(request);
			String companyId = callContext.getCorpId();
			if(StringUtils.isBlank(companyId)){
				this.writePermissionDeniedJsonToClient(response,null);
				return ;
			}
			pageable.setTotalCount(total);
			
			PageData<SalaryBillReadRecordModel> pageData = this.salaryBillLogService.getReadRecord(companyId,logid,pageable);
			
			this.writeSuccessJsonToClient(response, pageData);
		} catch (Exception e) {
			logger.error("getDetails", e);
			this.writeFailJsonToClient(response);
		}
	}
	
	
	/**
	 * 以分页方式返回历史操作记录
	 * @param pageable 分页信息
	 * @return
	 */
	@RequestMapping(value = { "/latest/page**" }, method = RequestMethod.GET)
	public void getSalaryBillLog(
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam("pageSize") int pageSize,
			@RequestParam("pageNo") int pageNo,
			@RequestParam(required = false, defaultValue = "0") long totalCount
			) {
		try {
			Pageable pageable = new PageRequest(pageNo,pageSize,totalCount);
			CallContext callContext = this.getPcCallContext(request);
			String companyId = callContext.getCorpId();
			if(StringUtils.isBlank(companyId)){
				this.writePermissionDeniedJsonToClient(response,null);
				return ;
			}
			
			String userId = callContext.getUserId();
			Boolean isBoss = callContext.isBoss();
			
			PageData<SalaryBillLogModel> pageData = this.salaryBillLogService
					.getSalaryBillLog(companyId, userId,isBoss==null?false:isBoss,pageable);
			
			this.writeSuccessJsonToClient(response, pageData);
		} catch (Exception e) {
			logger.error("latest/page", e);
			this.writeFailJsonToClient(response);
		}
	}
	
	
	/**
	 * 删除单个操作
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "/dellog**" }, method = RequestMethod.GET)
	public void dellog(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("logid") String logid) {
		try {
			CallContext callContext = this.getPcCallContext(request);
			String companyId = callContext.getCorpId();
			if(StringUtils.isBlank(companyId)){
				this.writePermissionDeniedJsonToClient(response,null);
				return ;
			}
			
			Boolean result = salaryBillLogService.delLog(companyId,logid);
			
			// 向客户端返回，结束请求
			this.writeSuccessJsonToClient(response, result);
			
		} catch (Exception e) {
			logger.error("dellog", e);
			this.writeFailJsonToClient(response);
		}
	}
	
	
	/**
	 * 管理员用户忘记密码 推送钉钉消息
	 * 
	 * @param request
	 */
	@RequestMapping(value = { "/forgetpwd**" }, method = RequestMethod.GET)
	public void forgetpwd(HttpServletRequest request, HttpServletResponse response) {
		try {
			CallContext callContext = this.getPcCallContext(request);
			String companyId = callContext.getCorpId();
			if(StringUtils.isBlank(companyId)){
				this.writePermissionDeniedJsonToClient(response,null);
				return ;
			}
			
			String userName = salaryBillPwdSetService.forgetPwd("https://"+request.getServerName(), companyId, "",SalaryPwdType.Manager.getCode());
			
			// 向客户端返回，结束请求
			this.writeSuccessJsonToClient(response, userName);
			
		} catch (Exception e) {
			logger.error("forgetpwd", e);
			this.writeFailJsonToClient(response);
		}
	}
	
	/**
	 * 验证随机码
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "/checkrandom**" }, method = RequestMethod.POST)
	public void checkrandom(HttpServletRequest request, HttpServletResponse response) {
		try {
			CallContext callContext = this.getPcCallContext(request);
			String companyId = callContext.getCorpId();
			if(StringUtils.isBlank(companyId)){
				this.writePermissionDeniedJsonToClient(response,null);
				return ;
			}
			
			SalaryBillPwdSetModel salaryBillPwdSetModel = this.fromInputJson(request, SalaryBillPwdSetModel.class);
			
			Boolean result = salaryBillPwdSetService.checkRandom(companyId, salaryBillPwdSetModel.getPassword());
			
			// 向客户端返回，结束请求
			this.writeSuccessJsonToClient(response, result);
			
		} catch (Exception e) {
			logger.error("checkrandom", e);
			this.writeFailJsonToClient(response);
			return;
		}
	}
	
	
	
	
	/**
	 * 下载log文件
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = { "/logfile**" }, method = RequestMethod.GET)
	public void logfile(HttpServletRequest request, HttpServletResponse response,@RequestParam("logid") String logid)
			throws Exception {
		response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		response.setHeader("Content-disposition",
				"attachment; filename=temple.xls");
		try{
			CallContext callContext = this.getPcCallContext(request);
			String companyId = callContext.getCorpId();
			if(StringUtils.isBlank(companyId)){
				this.writePermissionDeniedJsonToClient(response,null);
				return ;
			}
			
			String fileId = salaryBillLogService.getFileIdByLogId(companyId, logid);
	
			String fileUrl = "";
			if(StringUtils.isNotBlank(fileId)){
				fileUrl = OSSObjectService.getFileUrl(SalaryBillConstant.BUCK_NAME, fileId);
			}
	
			this.writeSuccessJsonToClient(response, fileUrl);
		}catch(Exception e){
			logger.error("logfile", e);
			this.writeFailJsonToClient(response);
			return;
		}
	}
	
	
	/**
	 * pc后台管理员对员工的密码重置 用户查询
	 * 
	 * @param request
	 * 
	 * @return
	 */
	@RequestMapping(value = { "/finduserforpwd**" }, method = RequestMethod.POST)
	public void findUserForPwd(HttpServletRequest request, HttpServletResponse response
			) {
		try {
			CallContext callContext = this.getPcCallContext(request);
			String companyId = callContext.getCorpId();
			if(StringUtils.isBlank(companyId)){
				this.writePermissionDeniedJsonToClient(response,null);
				return ;
			}
			
			SearchUserModel searchUserModel = this.fromInputJson(request, SearchUserModel.class);
			String searchkey = searchUserModel.getSearchkey();
			
			List<UserModel> salaryPwdPersonModel = null;
			if(StringUtils.isBlank(searchkey)){
				salaryPwdPersonModel = new ArrayList<UserModel>();
			}else{
				salaryPwdPersonModel = salaryBillPwdSetService.findSalaryPwdModelBySerach(companyId,searchkey);
			}
			// 向客户端返回，结束请求
			this.writeSuccessJsonToClient(response, salaryPwdPersonModel);
			
		} catch (Exception e) {
			logger.error("checkrandom", e);
			this.writeFailJsonToClient(response);
			return;
		}
	}
	
	/**
	 * 管理员清空用户密码
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "/clearpwd**" }, method = RequestMethod.GET)
	public void resetuserpwd(HttpServletRequest request, HttpServletResponse response
			,@RequestParam("userid") String userId) {
		try {
			
			CallContext callContext = this.getPcCallContext(request);
			String companyId = callContext.getCorpId();
			if(StringUtils.isBlank(companyId)){
				this.writePermissionDeniedJsonToClient(response,null);
				return ;
			}
			Boolean result = salaryBillPwdSetService.clearUserPwd("https://"+request.getServerName(),companyId,userId,SalaryPwdType.User.getCode());
			this.writeSuccessJsonToClient(response,result);
		} catch (Exception e) {
			logger.error("resetuserpwd", e);
			this.writeFailJsonToClient(response);
			return;
		}
	}
	
	/**
	 * 公司模板列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "/mytemplate**" }, method = RequestMethod.GET)
	public void mytemplate(HttpServletRequest request, HttpServletResponse response) {
		try {
			CallContext callContext = this.getPcCallContext(request);
			String companyId = callContext.getCorpId();
			if(StringUtils.isBlank(companyId)){
				this.writePermissionDeniedJsonToClient(response,null);
				return ;
			}
			List<SalaryBillTemplateModel> m  = salaryBillService.myTemplates(companyId);
			this.writeSuccessJsonToClient(response,m);
		} catch (Exception e) {
			logger.error("resetuserpwd", e);
			this.writeFailJsonToClient(response);
			return;
		}
	}

	

	/**
	 * 该部门下的子部门信息
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = { "/getChildDepts**" }, method = RequestMethod.GET)
	public void findOrgTree(HttpServletRequest request, HttpServletResponse response,
							@RequestParam(required = false) String id) {
		try {
			CallContext callContext = this.getPcCallContext(request);
			String companyId = callContext.getCorpId();
			if(StringUtils.isBlank(companyId)){
				this.writePermissionDeniedJsonToClient(response,null);
				return ;
			}
			OrgTreeModel m = new OrgTreeModel();
			if(StringUtils.isBlank(id)){
				id = "1";
				m.setName(callContext.getCorpName());
			}
			List<OrgTreeModel> orgDeptModels  = salaryBillService.getOrgSubDetpByDeptId(companyId,id);
			m.setId(id);
			m.setDatas(orgDeptModels);
			
			this.writeSuccessJsonToClient(response,m);
		} catch (Exception e) {
			logger.error("findOrgTree", e);
			this.writeFailJsonToClient(response);
			return;
		}
	}
	
	/**
	 * 该部门下的员工信息
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = { "/getChildUsers**" }, method = RequestMethod.GET)
	public void getChildUsers(HttpServletRequest request, HttpServletResponse response,
							@RequestParam(required = false) String id) {
		try {
			CallContext callContext = this.getPcCallContext(request);
			String companyId = callContext.getCorpId();
			if(StringUtils.isBlank(companyId)){
				this.writePermissionDeniedJsonToClient(response,null);
				return ;
			}
			OrgTreeModel m = new OrgTreeModel();
			if(StringUtils.isBlank(id)){
				id = "1";
				m.setName(callContext.getCorpName());
			}
			
			List<OrgTreeModel> orgDeptModels  = salaryBillService.getOrgUserByDeptId(companyId,id);
			m.setId(id);
			m.setDatas(orgDeptModels);
			
			this.writeSuccessJsonToClient(response,m);
		} catch (Exception e) {
			logger.error("findOrgTree", e);
			this.writeFailJsonToClient(response);
			return;
		}
	}

	
	/**
	 * 查询已经设置不需要密码的人员和部门
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = { "/findNoneedPwdItems**" }, method = RequestMethod.GET)
	public void findNoneedPwdItems(HttpServletRequest request, HttpServletResponse response) {
		try {
			CallContext callContext = this.getPcCallContext(request);
			String companyId = callContext.getCorpId();
			if(StringUtils.isBlank(companyId)){
				this.writePermissionDeniedJsonToClient(response,null);
				return ;
			}
			List<SalaryBillNoPwdSelectedItemViewMode> itemLists =salaryBillPwdSetService.findNoneedPwdItems(companyId);
			this.writeSuccessJsonToClient(response,itemLists);
		} catch (Exception e) {
			logger.error("findNoneedPwdItems", e);
			this.writeFailJsonToClient(response);
			return;
		}
	}
	
	
	/**
	 * 保存选择的部门或人员查看数据时不需要密码
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = { "/saveNoneedPwdItems**" }, method = RequestMethod.POST)
	public void saveNoneedPwdItems(HttpServletRequest request, HttpServletResponse response)throws Exception {
		CallContext callContext = this.getPcCallContext(request);
		String companyId = callContext.getCorpId();
		if(StringUtils.isBlank(companyId)){
			this.writePermissionDeniedJsonToClient(response,null);
			return ;
		}
		String userId = callContext.getUserId();
		
		List<OrgDataModel> orgDataModels = this.fromInputJsonToList(request,OrgDataModel.class);
		OrgItemResult orgItemResult =salaryBillPwdSetService.saveNoneedPwdItems(orgDataModels,companyId,userId);
		this.writeSuccessJsonToClient(response,orgItemResult);
		try{
			
		}catch(Exception e){
			logger.error("saveNoneedPwdItems", e);
			this.writeFailJsonToClient(response);
		}
	}

	
	/**
	 * 全部恢复
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = { "/doReset**" }, method = RequestMethod.GET)
	public void doReset(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) String id,
			@RequestParam(required = false) String type
			) {
		try {
			CallContext callContext = this.getPcCallContext(request);
			String companyId = callContext.getCorpId();
			if(StringUtils.isBlank(companyId)){
				this.writePermissionDeniedJsonToClient(response,null);
				return ;
			}
			OrgItemResult orgItemResult =salaryBillPwdSetService.doReset(companyId, id,type);
			this.writeSuccessJsonToClient(response,orgItemResult);
		} catch (Exception e) {
			logger.error("findNoneedPwdItems", e);
			this.writeFailJsonToClient(response);
			return;
		}
	}
	
	
	
	/**
	 * 获取管理员权限 超级管理员或者boss 子管理员需要判断是否是薪酬管理员
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = { "/isAccess**" }, method = RequestMethod.GET)
	public void isAccess(HttpServletRequest request, HttpServletResponse response) {
		try {
			String cookieValue = getUserCookie(request);
			JSONObject jo = null;
			boolean isSup = false;
			if(StringUtils.isNotBlank(cookieValue)){
				jo = JSONObject.parseObject(cookieValue);
				isSup = jo.getBoolean("supAdmin");
				if(!isSup){
					isSup = jo.getBoolean("boss");
				}
			}else{
				this.writePermissionDeniedJsonToClient(response,null);
				return;
			}
			boolean s = false;
			if(!isSup){
				String companyId = jo.getString("corpId");
				String userId = jo.getString("userId");
				s = salaryBillAdminService.isSalaryBillAdmin(companyId, userId);
			}
			JSONObject job = new JSONObject();
			job.put("c", isSup); //超管理或者boss
			job.put("s", true);  //薪酬管理员？
			this.writeSuccessJsonToClient(response,job);
		} catch (Exception e) {
			logger.error("adminInfo", e);
			this.writeFailJsonToClient(response);
			return;
		}
	}
	
	/**
	 * 获取子管理员列表
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = { "/getChildAdmins**" }, method = RequestMethod.GET)
	public void getAdmins(HttpServletRequest request, HttpServletResponse response) {
		try {
			CallContext callContext = this.getPcCallContext(request);
			String companyId = callContext.getCorpId();
			if(StringUtils.isBlank(companyId)){
				this.writePermissionDeniedJsonToClient(response,null);
				return ;
			}
			boolean isSupAdmin = callContext.isSupAdmin();
			if(!isSupAdmin){
				logger.error("不是主管理员：callContext:"+JSONObject.toJSONString(callContext));
				this.writeFailJsonToClient(response);
				return ;
			}
			List<SalaryBillAdminViewModel> l = salaryBillAdminService.getSalaryBillAdmins(companyId);
			logger.info("getChildAdmins:"+JSONObject.toJSONString(l));
			this.writeSuccessJsonToClient(response,l);
		} catch (Exception e) {
			logger.error("getAdmins", e);
			this.writeFailJsonToClient(response);
			return;
		}
	}
	
	
	/**
	 * 保存薪酬管理员
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = { "/saveChildAdmins**" }, method = RequestMethod.POST)
	public void saveAdmins(HttpServletRequest request, HttpServletResponse response) {
		try {
			CallContext callContext = this.getPcCallContext(request);
			String companyId = callContext.getCorpId();
			if(StringUtils.isBlank(companyId)){
				this.writePermissionDeniedJsonToClient(response,null);
				return ;
			}
			boolean isSupAdmin = callContext.isSupAdmin();
			if(!isSupAdmin){
				logger.error("不是主管理员：callContext:"+JSONObject.toJSONString(callContext));
				this.writeFailJsonToClient(response);
				return ;
			}
			
			SalaryBillAdminViewModel adminConfig = this.fromInputJson(request,SalaryBillAdminViewModel.class);
			
			boolean f = salaryBillAdminService.saveSalaryBillAdmins(companyId,adminConfig.getUserId(),adminConfig.isAdminFlag());
			this.writeSuccessJsonToClient(response,f);
		} catch (Exception e) {
			logger.error("saveAdmins", e);
			this.writeFailJsonToClient(response);
			return;
		}
	}
	
	
	
	/**
	 * 下载手机模板
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = { "/downloadModel**" }, method = RequestMethod.GET)
	public void downloadModel(HttpServletRequest request, HttpServletResponse response) {
		try{
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			response.setHeader("Content-disposition", "attachment; filename=smstemple.xls");
			String cookieValue = getUserCookie(request);
			String companyId ="";
			JSONObject jo = null;
			if(StringUtils.isNotBlank(cookieValue)){
				jo = JSONObject.parseObject(cookieValue);
				companyId = jo.getString("corpId");
			}
			if(StringUtils.isBlank(companyId)){
				this.writePermissionDeniedJsonToClient(response,null);
				return ;
			}
			HSSFWorkbook wb = salaryBillSmsService.exportMobileTempToExcel(companyId);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			wb.write(out);
			InputStream inputStream = new ByteArrayInputStream(out.toByteArray());
			
			String fileId = companyId+"/mobile_temp.xls";
			
			String buckname = SalaryBillConstant.BUCK_NAME;
			
			OSSObjectService.uploadExcelFile(buckname,fileId, inputStream);
			
			String fileUrl = OSSObjectService.getFileUrl(buckname, fileId);
	
			if(inputStream!=null){
				inputStream.close();
			}
			if(wb!=null){
				wb.close();
			}
			this.writeSuccessJsonToClient(response, fileUrl);
		}catch(Exception e){
			
		}
	}
	
	
	/**
	 * 上传手机模板
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = { "/getUploadData**" }, method = RequestMethod.GET)
	public void getUploadData(HttpServletRequest request, HttpServletResponse response,@RequestParam("fileid") String fileId)
			throws Exception {
		
		fileId = URLDecoder.decode(fileId);
		String cookieValue = getUserCookie(request);
		String companyId ="";
		String userId = "";
		JSONObject jo = null;
		
		if(StringUtils.isNotBlank(cookieValue)){
			jo = JSONObject.parseObject(cookieValue);
			companyId = jo.getString("corpId");
			userId = jo.getString("userId");
		}
		
		//获取该公司的员工信息
		if(StringUtils.isBlank(companyId)){
			this.writePermissionDeniedJsonToClient(response,null);
			return ;
		}
		
		try{
			CacheSalaryMobileModel cacheSalaryMobileModel = salaryBillSmsService.parserMobileTempExcel(request.getServerName(),companyId,userId,"alidsalarybill",fileId);
			//err
			List<SalaryBillMobileModel> errorSalaryBills = cacheSalaryMobileModel.getErrorSalaryMobiles();
			int errCount = errorSalaryBills == null ? 0 : errorSalaryBills.size();
			cacheSalaryMobileModel.setErrorCount(errCount);
			//succ
			List<SalaryBillMobileModel> successSalaryBills = cacheSalaryMobileModel.getSuccessSalaryMobiles();
			int successCount = successSalaryBills == null ? 0 : successSalaryBills.size();
			cacheSalaryMobileModel.setSuccessCount(successCount);
			
			this.writeSuccessJsonToClient(response, cacheSalaryMobileModel);
		}catch(Exception e){
			logger.error("upload", e);
			this.writeFailJsonToClient(response);
		}
	}
	
	@RequestMapping(value = { "/nextPage**" }, method = RequestMethod.GET)
	public void nextPage(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("cachekey") String cachekey,@RequestParam("page") int page)
			throws Exception {
		String cookieValue = getUserCookie(request);
		String companyId ="";
		JSONObject jo = null;
		if(StringUtils.isNotBlank(cookieValue)){
			jo = JSONObject.parseObject(cookieValue);
			companyId = jo.getString("corpId");
		}
		if(StringUtils.isBlank(companyId)){
			this.writeFailJsonToClient(response);
			return ;
		}
		try{
			CacheSalaryMobileModel cacheSalaryModel = (CacheSalaryMobileModel) this.cacheableService.getRawObjectFromCache(cachekey);
			//err
			List<SalaryBillMobileModel> errorSalaryBills = cacheSalaryModel.getErrorSalaryMobiles();
			int errCount = errorSalaryBills == null ? 0 : errorSalaryBills.size();
			cacheSalaryModel.setErrorCount(errCount);
			//succ
			List<SalaryBillMobileModel> successSalaryBills = cacheSalaryModel.getSuccessSalaryMobiles();
			int successCount = successSalaryBills == null ? 0 : successSalaryBills.size();
			cacheSalaryModel.setSuccessCount(successCount);
			
			//50一页
			int pageSize = 50;
			if(errCount+successCount > page*pageSize){
				cacheSalaryModel.setHasNext(true);
			}else{
				cacheSalaryModel.setHasNext(false);
			}
			
			//取50 model
			List<SalaryBillMobileModel> salarys = new ArrayList<SalaryBillMobileModel>();
			
			int start = pageSize*(page-1);
			
			int count = errCount+successCount > start+pageSize ? start+pageSize : errCount+successCount;
			
			for(int i = start ; i < count ; i ++){
				if(i<errCount){
					salarys.add(errorSalaryBills.get(i));
				}else{
					salarys.add(successSalaryBills.get(i-errCount));
				}
			}
			cacheSalaryModel.setErrorSalaryMobiles(null);
			cacheSalaryModel.setSuccessSalaryMobiles(null);
			cacheSalaryModel.setSalaryMobiles(salarys);
			this.writeSuccessJsonToClient(response, cacheSalaryModel);
		}catch(Exception e){
			logger.error("nextrecord", e);
			this.writeFailJsonToClient(response);
		}
	}
	
	/**
	 * 手机数据提交
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = { "/confirmUpload**" }, method = RequestMethod.POST)
	public void confirmUpload(HttpServletRequest request, HttpServletResponse response
			)throws Exception {
		CallContext callContext = this.getPcCallContext(request);
		String companyId = callContext.getCorpId();
		if(StringUtils.isBlank(companyId)){
			this.writePermissionDeniedJsonToClient(response,null);
			return ;
		}
		SalaryBillCommitModel salaryBillCommitModel = this.fromInputJson(request,SalaryBillCommitModel.class);
		try{
			boolean flag = salaryBillSmsService.commitMobile(callContext,salaryBillCommitModel);
			this.writeSuccessJsonToClient(response, flag);
		}catch(Exception e){
			logger.error("commit", e);
			this.writeFailJsonToClient(response);
		}

	}
	
	/**
	 * 短信设置信息展示
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = { "/getMsgInfo**" }, method = RequestMethod.GET)
	public void getMsgInfo(HttpServletRequest request, HttpServletResponse response) {
		try {
			String cookieValue = getUserCookie(request);
			String companyId ="";
			String userId = "";
			boolean isSupAdmin = false;
			JSONObject jo = null;
			if(StringUtils.isNotBlank(cookieValue)){
				jo = JSONObject.parseObject(cookieValue);
				companyId = jo.getString("corpId");
				userId = jo.getString("userId");
				//isSupAdmin = jo.getBoolean("supAdmin");
			}
			if(StringUtils.isBlank(companyId)){
				this.writePermissionDeniedJsonToClient(response,null);
				return ;
			}
			SalarySmsMode smsModel = salaryBillSmsService.getCorpSmsInfo(companyId, userId);
			this.writeSuccessJsonToClient(response,smsModel);
		}catch (Exception e) {
			logger.error("setSmsMode", e);
			this.writeFailJsonToClient(response);
			return;
		}
	}
	
	
	/**
	 * 设置短信发送模式
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = { "/setSmsMode**" }, method = RequestMethod.POST)
	public void setSmsTemp(HttpServletRequest request, HttpServletResponse response) {
		try {
			String cookieValue = getUserCookie(request);
			String companyId ="";
			String userId = "";
			//boolean isSupAdmin = false;
			JSONObject jo = null;
			if(StringUtils.isNotBlank(cookieValue)){
				jo = JSONObject.parseObject(cookieValue);
				companyId = jo.getString("corpId");
				userId = jo.getString("userId");
				//isSupAdmin = jo.getBoolean("supAdmin");
			}
			if(StringUtils.isBlank(companyId) ){
				this.writePermissionDeniedJsonToClient(response,null);
				return ;
			}
//			else if(!isSupAdmin){
//				this.writeFailJsonToClient(response);
//				return ;
//			}
			
			SalarySmsMode salarySmsMode = this.fromInputJson(request,SalarySmsMode.class);
			boolean f = salaryBillSmsService.setSMSMode(companyId, userId, salarySmsMode);
			this.writeSuccessJsonToClient(response,f);
		}catch (Exception e) {
			logger.error("setSmsMode", e);
			this.writeFailJsonToClient(response);
			return;
		}
	}
	
	/**
	 * 报表展示短信消费量
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = { "/rpsms**" }, method = RequestMethod.POST)
	public void smsList(HttpServletRequest request, HttpServletResponse response) {
		String s = request.getParameter("st"); //开始yyyyMMdd
		String e = request.getParameter("et"); //结束yyyyMMdd
		String key = request.getParameter("k");//k==密文
		if("i97hngrOI".equals(key)){
			
			
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		Date st = null;
		Date et = null;
		if(StringUtils.isNotBlank(s)){
			 try {
				st = sdf.parse(s);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if(StringUtils.isNotBlank(e)){
			 try {
				et = sdf.parse(e);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		//报表展示短信消费量
		List<SmsReportModel> l = salaryBillSmsService.getSmsReport(st, et);
		
		this.writeSuccessJsonToClient(response,l);
	}
	
	
	@Override
	protected String getHomePageUrl(String param) {
		return null;
	}


	@Override
	protected SalaryBillService getService() {
		return null;
	}
	
	
//	/**
//	 * 发调查接口
//	 *
//	 * @param request
//	 * @return
//	 */
//	@RestDocExport(exportNo="007")
//	@RestDoc(description = "发调查接口", returnMethodName = "",
//	smapleClassName = "",
//		requestBodyMethodName = "")
//	@RequestMapping(value = { "/sendVote**" }, method = RequestMethod.GET)
//	public void sendVote(HttpServletRequest request, HttpServletResponse response) {
//		try {
//			String appid = "263";
//			String appid2 = "339";
//			List<String> appids = new ArrayList<String>();
//			appids.add(appid);
//			appids.add(appid2);
//
//			salaryBillService.sendVote(appids);
//
//		} catch (Exception e) {
//			logger.error("resetuserpwd", e);
//			this.writeFailJsonToClient(response);
//			return;
//		}
//	}

}
