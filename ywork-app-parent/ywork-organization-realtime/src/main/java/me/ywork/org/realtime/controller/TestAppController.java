package me.ywork.org.realtime.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;

import me.ywork.controller.RestController;
import me.ywork.org.realtime.service.IDingOrgElementService;
import me.ywork.org.realtime.service.impl.DingOrgElementServiceImpl;
import me.ywork.org.realtime.service.impl.DingOrgModifyEventHandler;



@Controller
@RequestMapping("**/alid/org/real")
public class TestAppController extends RestController<IDingOrgElementService> {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public static Map<String,Map<String,String>> appMap = new HashMap<String,Map<String,String>>();
	
	public static Map<String, Map<String, String>> getAppMap() {
		return appMap;
	}
	
	@Autowired
	private DingOrgModifyEventHandler dingOrgModifyEventHandler;
	
	@Autowired
	private DingOrgElementServiceImpl dingOrgElementService;
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public void testorg(@RequestParam("suiteid") String sid,@RequestParam("corpId") String corpId,  HttpServletRequest request,
			HttpServletResponse response) {
		logger.info(JSONObject.toJSONString(sid+corpId));
		//dingOrgModifyEventHandler.test();
		//dingOrgElementService.syncElement(sid, corpId);
	}
	
	@Override
	protected IDingOrgElementService getService() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	protected String getHomePageUrl(String param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void autoAuthenticate(HttpServletRequest request, HttpServletResponse response) {
		//super.autoAuthenticate(request, response);
		try {
			JSONObject jo = new JSONObject();
			jo.put("msg", "success");
			jo.put("code", "200");
			// 向客户端返回，结束请求
			this.writeSuccessJsonToClient(response, jo);

		} catch (Exception e) {
			logger.error("autoAuthenticate", e);
		}
	}



	
}
