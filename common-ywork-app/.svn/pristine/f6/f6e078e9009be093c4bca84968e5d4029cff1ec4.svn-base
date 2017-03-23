package me.ywork.message.topic;

public enum KafkaTopics {
	
	
	/**********分割线**********/
	/**
	 * 组织架构管理频道
	 */
	DING_ORG_MANAGER_REAL("pub_org_manage_real","组织架构管理频道"),
	
	YWORK_DING_HTTP_MESSAGE("pub_ywork_ding_http_message","封装钉钉消息体发送"),
	
	YWORK_DING_SMS("pub_ywork_ding_sms","短信消息发送")
	;
	
	private final String topic;
	private final String description;
	
	KafkaTopics(String name,String desc) {
		this.topic = name;
		this.description = desc;
	}

	public String getTopic() {
		return topic;
	}

	public String getDescription() {
		return description;
	}
	
}
