package me.ywork.salarybill.TimeTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heyi.framework.messagebus.kafka.KafkaProducer;
import com.heyi.utils.CollectionUtils;

import me.ywork.message.topic.KafkaTopics;
import me.ywork.salarybill.base.SalarySmsModeEnum;
import me.ywork.salarybill.model.SalaryBillItemModel;
import me.ywork.salarybill.model.SalaryBillLogModel;
import me.ywork.salarybill.model.SalaryBillReadRecordModel;
import me.ywork.salarybill.repository.SalaryBillItemRepository;
import me.ywork.salarybill.repository.SalaryBillLogRepository;
import me.ywork.salarybill.repository.SalaryBillSmsRepository;
import me.ywork.sms.message.ShortMessageBody;
import me.ywork.util.AESUtil;

@Service
public class SalaryBillTimeTask implements InitializingBean{
	private static Logger logger = LoggerFactory.getLogger(SalaryBillTimeTask.class);

	@Autowired
	private SalaryBillLogRepository salaryLogRepository;
	
	@Autowired
	private SalaryBillItemRepository salaryBillItemRepository;
	
	@Autowired
	private SalaryBillSmsRepository salaryBillSmsRepository;

	@Override
	public void afterPropertiesSet() throws Exception {
		Thread thread = sendSmsStart();
		thread.start();
	}
	
	public Thread sendSmsStart(){
		Thread thread = new Thread() {
			@Override
			public void run() {
				do{
					try {
						Thread.sleep(1000*60);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					sendSms();
				}while(true);
			}
		};
		return thread;
	}
	
	public void sendSms(){
		//log 里面 status = 0的
		List<SalaryBillLogModel> logs = salaryLogRepository.getSmsLog();
		if(logs == null || logs.size()==0){
			return;
		}
		Map<String,Integer> smsCountMap = new HashMap<String,Integer>();
		Long curr = System.currentTimeMillis();
		for(SalaryBillLogModel log : logs){
			if(curr - log.getCreateTime().getTime() > 1000*60*60){ //1个小时
				
				Integer smsCount = smsCountMap.get(log.getCompanyId());
				if(smsCount == null){
					smsCount = log.getSmsCount();
				}
				
				if(smsCount<1){ //短信量不足
					continue;
				}
				
				try{
				List<SalaryBillReadRecordModel> notReaders = salaryLogRepository.getSalaryLogUsers(log.getCompanyId(), log.getId(), SalarySmsModeEnum.SMS_SEND_Default.getCode());
				if(notReaders==null || notReaders.size()==0){
					 return;
				}
				//发送手机短信
				ShortMessageBody msg = new ShortMessageBody();
				msg.setExtend(null);
				msg.setSmsFreeSignName("悦通知");
				msg.setSmsTemplateCode(log.getSmsShow()==SalarySmsModeEnum.SMS_SHOW_ALL.getCode()?"SMS_50360017":"SMS_50255007");
				Map<String, Object> smsParams = new HashMap<>();
				
				smsParams.put("salary", log.getSalaryType());
				
				//总计字数
				 int nameCount = 0; int companyCount = 0 ; int salaryCount = 0 ;
		         int tempCount = 23; int totalCount = 0 ;int bachCount = 0;
		         
				for(SalaryBillReadRecordModel notReader :notReaders){
					
					if(StringUtils.isBlank(notReader.getMobileNo())){
						continue;
					}
					
					if(smsCount<1){ //短信量不足
						continue;
					}
					
					//查明细
					smsParams.put("name", notReader.getUserName());
					smsParams.put("company", notReader.getCorpName());
					if(log.getSmsShow()==SalarySmsModeEnum.SMS_SHOW_ALL.getCode()){
						//查此人在这个批次的明细- 可能有多笔
						List<SalaryBillItemModel> items =salaryBillItemRepository.getSalaryItemsBySalaryBillLogId(log.getCompanyId(), log.getId(), notReader.getUserId());
						//按salaryBillId分组
						Map<String, List<SalaryBillItemModel>> map = CollectionUtils.groupCollection(items);
						msg.setRecNum(notReader.getMobileNo());//,号隔开
						
						
						//分组后每条发送一次短信，
						for (Map.Entry<String, List<SalaryBillItemModel>> entry : map.entrySet()) {
							
							if(smsCount<1){ //短信量不足
								continue;
							}
							
							List<SalaryBillItemModel> itemsModel= entry.getValue();
							String smsContent = "";
							for(SalaryBillItemModel itemModel : itemsModel){
								try {
									smsContent += itemModel.getItemName()+":"+AESUtil.decrypt(itemModel.getItemValue(), itemModel.getId())+" ";
								} catch (Exception e) {
								}
							}
							
							try {
								smsContent+=",总计:"+AESUtil.decrypt(itemsModel.get(0).getRealSalary(), itemsModel.get(0).getBillId());
							} catch (Exception e) {
								e.printStackTrace();
							}
							smsParams.put("content", smsContent);
							msg.setSmsParam(smsParams);
							try{
								 KafkaProducer.getInstance().sendMessage(KafkaTopics.YWORK_DING_SMS.getTopic(), msg);
								 nameCount = notReader.getUserName().length();
					        	 companyCount = notReader.getCorpName().length();
					        	 salaryCount = log.getSalaryType().length();
					        	 tempCount = 27 + smsContent.length();
					        	 //总字数
					        	 totalCount = tempCount+nameCount+companyCount+salaryCount;
					        	 bachCount += totalCount%70 == 0 ? totalCount/70:totalCount/70+1; //计算短信数
					        	 
					        	 smsCount = smsCount-(totalCount%70 == 0 ? totalCount/70:totalCount/70+1);
							}catch(Exception e){
								logger.error("logid："+log.getId(),e);
							}
							
						}
					}else{
						try{
							msg.setRecNum(notReader.getMobileNo());//,号隔开
							msg.setSmsParam(smsParams);
							KafkaProducer.getInstance().sendMessage(KafkaTopics.YWORK_DING_SMS.getTopic(), msg);
							 nameCount = notReader.getUserName().length();
				        	 companyCount = notReader.getCorpName().length();
				        	 salaryCount = log.getSalaryType().length();
				        	 //总字数
				        	 totalCount = tempCount+nameCount+companyCount+salaryCount;
				        	 bachCount += totalCount%70 == 0 ? totalCount/70:totalCount/70+1;
				        	 
				        	 smsCount = smsCount-(totalCount%70 == 0 ? totalCount/70:totalCount/70+1);
						}catch(Exception e){
							logger.error("logid："+log.getId(),e);
						}
					}
				}
				
				smsCountMap.put(log.getCompanyId(), smsCount);
				salaryLogRepository.updateSmsStatus(log.getCompanyId(),log.getId(),bachCount);
				salaryBillSmsRepository.updateSms(log.getCompanyId(), bachCount); // 更新公司的短信数据
				}catch(Exception e){
					logger.error("error sms,log:"+log.getId(),e);
				}
				logger.info("success batch,log:"+log.getId());
			}
		}
	}
	
}




