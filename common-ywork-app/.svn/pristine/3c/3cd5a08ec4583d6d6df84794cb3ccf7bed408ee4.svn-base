package me.ywork.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import me.ywork.message.base.AbstractDingMessage;

/**
 * OA类型消息
 * 
 * @author TangGang 2015年8月2日
 * 
 */
public class DingOAMessage extends AbstractDingMessage {
	private static final long serialVersionUID = 4016522359085904466L;

	/**
	 * 必须，客户端点击消息时跳转到的H5地址
	 */
	private String message_url;
	
	private String pc_message_url;

	/**
	 * 必须，消息头部内容
	 */
	private DingOAHead head;

	/**
	 * 必须，消息体
	 */
	private DingOABody body;

	public DingOAMessage() {
		super();
	}

	public String getMessage_url() {
		return message_url;
	}

	public void setMessage_url(String message_url) {
		this.message_url = message_url;
	}

	public String getPc_message_url() {
		return pc_message_url;
	}

	public void setPc_message_url(String pc_message_url) {
		this.pc_message_url = pc_message_url;
	}

	public DingOAHead getHead() {
		return head;
	}

	/*
	 * public void setHead(Map<String, String> head) { this.head = head; }
	 */

	/**
	 * 设置消息头部内部
	 * 
	 * @param bgcolor
	 *            必填， 消息头部的背景颜色。长度限制为8个英文字符，其中前2为表示透明度，后6位表示颜色值。不要添加0x
	 * @param text
	 *            必填，消息的头部标题
	 */
	public void setHead(String bgcolor, String text) {
		if (StringUtils.isBlank(bgcolor)) {
			throw new IllegalArgumentException(
					"setHead - parameter bgcolor is null or empty.");
		}

		if (StringUtils.isBlank(text)) {
			throw new IllegalArgumentException(
					"setHead - parameter text is null or empty.");
		}

		this.head = new DingOAHead(bgcolor, text);
	}

	public DingOABody getBody() {
		if (body == null) {
			body = new DingOABody();
		}
		
		return body;
	}

	/**
	 * 追加OA消息表单数据
	 * @param key
	 * @param value
	 */
	public void appendBodyFormItem(String key, String value) {
		this.getBody().appendFormItem(key, value);
	}

	public void setBodyTitle(String bodyTitle) {
		this.getBody().setTitle(bodyTitle);
	}

	public void setBody(DingOABody body) {
		this.body = body;
	}

	/**
	 * OA消息头部信息
	 * 
	 * @author TangGang 2015年8月2日
	 * 
	 */
	public static class DingOAHead implements Serializable {
		private static final long serialVersionUID = -7020052661219494589L;

		/**
		 * 消息头部的背景颜色。长度限制为8个英文字符，其中前2为表示透明度，后6位表示颜色值。不要添加0x
		 */
		private String bgcolor;
		/**
		 * 消息的头部标题
		 */
		private String text;

		public DingOAHead(String bgcolor, String text) {
			super();
			this.bgcolor = bgcolor;
			this.text = text;
		}

		public String getBgcolor() {
			return bgcolor;
		}

		public void setBgcolor(String bgcolor) {
			this.bgcolor = bgcolor;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

	}

	/**
	 * OA消息表单项类
	 */
	public static class BodyFormItem implements  Serializable {
		private static final long serialVersionUID = 8816307260825665558L;

		private String key;
		private String value;

		public BodyFormItem() {
			super();
		}

		public BodyFormItem(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}
	}

	/**
	 * OA消息体内容
	 * 
	 * @author TangGang 2015年8月2日
	 * 
	 */
	public static class DingOABody implements Serializable {
		private static final long serialVersionUID = -4530678382842227866L;

		/**
		 * 消息体的标题
		 */
		private String title;

		/**
		 * 消息体的表单，最多显示6个，超过会被隐藏
		 */
		private List<BodyFormItem> form;

		/**
		 * 消息体的内容，最多显示3行
		 */
		private String content;
		/**
		 * 消息体中的图片media_id
		 */
		private String image;
		/**
		 * 自定义的附件数目。此数字仅供显示，钉钉不作验证
		 */
		private String file_count;
		/**
		 * 自定义的作者名字
		 */
		private String author;
		
		/**
		 * 单行富文本信息
		 */
		private DingOABodyRich rich;

		public DingOABody() {

		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public List<BodyFormItem> getForm() {
			if (form == null) {
				form = new ArrayList<BodyFormItem>();
			}

			return form;
		}

		/*public void setForm(Map<String, String> form) {
			this.form = form;
		}*/
		
		public void appendFormItem(String key, String value) {
			if (StringUtils.isBlank(key)) {
				throw new IllegalArgumentException("setForm - parameter key is null or empty.");			}
			
			this.getForm().add(new BodyFormItem(key, value));
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public String getFile_count() {
			return file_count;
		}

		public void setFile_count(String file_count) {
			this.file_count = file_count;
		}

		public String getAuthor() {
			return author;
		}

		public void setAuthor(String author) {
			this.author = author;
		}

		public DingOABodyRich getRich() {
			return rich;
		}

	/*	public void setRich(DingOABodyRich rich) {
			this.rich = rich;
		}*/
		
		public void setRich(String num, String unit) {
			this.rich = new DingOABodyRich(num, unit);
		}
	}

	/**
	 *
	 * @author TangGang 2015年8月3日
	 *
	 */
	public static class DingOABodyRich implements Serializable {
		private static final long serialVersionUID = -997828607769370156L;

		/**
		 * 单行富文本信息的数目
		 */
		private String num;
		/**
		 * 单行富文本信息的单位
		 */
		private String unit;

		public DingOABodyRich(String num, String unit) {
			super();
			this.num = num;
			this.unit = unit;
		}

		public String getNum() {
			return num;
		}

		public void setNum(String num) {
			this.num = num;
		}

		public String getUnit() {
			return unit;
		}

		public void setUnit(String unit) {
			this.unit = unit;
		}

	}
}
