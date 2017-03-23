package me.ywork.ticket.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;




/**
 * @author lizh
 * @data 2013-12-24 下午02:37:14
 */
public class MessageUtils {
	
	public static String trans2Xml(Map<String,String> map){
		String xml = "<xml>";
		for(String key : map.keySet()){
			xml+="<"+key+">";
			xml+= map.get(key);
			xml+="</"+key+">";
		}
		xml+="</xml>";
		return xml;
	}
	
	
	public static Map<String, String> xml2Map(HttpServletRequest request)
			throws Exception {
		// 将解析结果存储在HashMap中
		Map<String, String> map = new HashMap<String, String>();
		InputStream inputStream = request.getInputStream();
          
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		Element root = document.getRootElement();
		List<Element> elementList = root.elements();

		for(Iterator i = root.elementIterator(); i.hasNext();){   
			Element employee = (Element) i.next(); 
			map.put(employee.getName(), employee.getText());
			for(Iterator j = employee.elementIterator(); j.hasNext();){   
				Element node=(Element) j.next();   
				map.put(node.getName(), node.getText());
			}   
		}
		inputStream.close();
		inputStream = null;
		return map;
	}
	
	//企业号
//	public static Map<String, String> tranMessage(HttpServletRequest request,WePublic wePublic)
//			throws Exception {
//		//xml请求解析
//		Map<String, String> requestMap  = xml2Map(request);
//		//根据是否含有AgentID判断，是否为企业号
//		if(null!=requestMap.get("AgentID")){
//			//开始解密
//			Result result2 = Prpcrypt.fromTencent(trans2Xml(requestMap), wePublic.getFdEncodingAESKey(), wePublic.getMsgSignature(), wePublic.getFdIntToken(),
//					wePublic.getTimestamp(), wePublic.getNonce(),  wePublic.getFdAppId());
//			
//			if(result2.getCode()!=0){
//				System.out.println(result2.getResult());
//				return null;
//			}
//			//将获取的xml字符串转map
//			requestMap = new HashMap<String,String>();
//			Document docu = DocumentHelper.parseText(result2.getResult());
//			Element root = docu.getRootElement();
//			List<Element> elementList = root.elements();
//			for (Element element : elementList) {
//				requestMap.put(element.getName(), element.getText());
//			}
//		}
//		return requestMap;
//	}
	
	
}
