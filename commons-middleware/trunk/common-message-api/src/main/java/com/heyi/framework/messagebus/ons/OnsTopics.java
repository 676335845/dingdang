package com.heyi.framework.messagebus.ons;

/**
 * ONS消息频道
 * @author sulta
 *
 */
public enum OnsTopics {
	
	/**
	 * ONS频道
	 */
	ONLINE_ONS_CHANNEL("ons"),
	
	;
	
	private final String topic;
	private final String defaultProducer;
	private final String consumer_paygetway;
	private final String consumer_erp;
	private final String consumer_shop;
	private final String consumer_sale;
	
	OnsTopics(String name) {
		String topic = OnsConfig.readValue("channel."+ name +".topic");
		String producer = OnsConfig.readValue("channel."+ name +".producer");
		String paygetway = OnsConfig.readValue("channel."+ name +".paygetway");
		String erp = OnsConfig.readValue("channel."+ name +".erp");
		String shop = OnsConfig.readValue("channel."+ name +".shop");
		String sale = OnsConfig.readValue("channel."+ name +".sale");
		
		this.topic = topic;
		this.defaultProducer = producer;
		this.consumer_paygetway = paygetway;
		this.consumer_erp = erp;
		this.consumer_shop = shop;
		this.consumer_sale = sale;
	}

	public String getTopic() {
		return topic;
	}

	public String getDefaultProducer() {
		return defaultProducer;
	}

	public String getConsumer_paygetway() {
		return consumer_paygetway;
	}

	public String getConsumer_erp() {
		return consumer_erp;
	}

	public String getConsumer_shop() {
		return consumer_shop;
	}

	public String getConsumer_sale() {
		return consumer_sale;
	}
}
