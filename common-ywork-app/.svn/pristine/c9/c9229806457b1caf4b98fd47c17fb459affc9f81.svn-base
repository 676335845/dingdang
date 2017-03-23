package me.ywork.message.base;

import me.ywork.enums.IStatusEnum;

/**
 * 钉消息类型
 * @author TangGang  2015年8月2日
 *
 */
public enum DingMessageType implements IStatusEnum<String> {
	text("text", "text消息", "DingMessageType.text"),
	image("image", "image消息", "DingMessageType.image"),
	voice("voice", "voice消息", "DingMessageType.voice"),
	file("file", "file消息", "DingMessageType.file"),
	link("link", "link消息", "DingMessageType.link"),
	oa("oa", "oa消息", "DingMessageType.oa");

	private final String code;
	private final String defaultLabel;
	private final String resourceKey;
	
		
	private DingMessageType(String code, String defaultLabel, String resourceKey) {
		this.code = code;
		this.defaultLabel = defaultLabel;
		this.resourceKey = resourceKey;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getDefaultLabel() {
		return defaultLabel;
	}

	@Override
	public String getResourceKey() {
		return resourceKey;
	}

}
