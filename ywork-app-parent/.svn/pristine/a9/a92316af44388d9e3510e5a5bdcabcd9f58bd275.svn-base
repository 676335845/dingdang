package me.ywork.oss.web;

import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.ObjectListing;

import me.ywork.sts.web.StsController;

@Controller
@RequestMapping(value = "**/oss")
public class OssController implements InitializingBean{
	private static final Logger LOG = LoggerFactory.getLogger(OssController.class);
	
	public static final String REGION = readValue("ossconfig.properties", "oss.region");
	public static final String ACCESSID = readValue("ossconfig.properties", "oss.access_id");
	public static final String ACCESSSECRET = readValue("ossconfig.properties", "oss.access_secret");
	public static final String OSS_DEFAULT_BUCKET = readValue("ossconfig.properties", "oss.defaultbucket");
	
	private OSSClient ossClient;
	
	@ResponseBody
    @RequestMapping(value = "/listobjects", method = RequestMethod.GET)
    public ObjectListing getMessage(HttpServletRequest request,
			HttpServletResponse response) {
		int maxkeysInt = 100;
		String bucketName = request.getParameter("bucketName");
		String region = request.getParameter("region");
		String prefix = request.getParameter("prefix");
		String marker = request.getParameter("marker");
		String delimiter = request.getParameter("delimiter");
		String maxKeys = request.getParameter("maxKeys");
		
		if(StringUtils.isEmpty(bucketName))
			bucketName = OSS_DEFAULT_BUCKET;
		
		if(StringUtils.isEmpty(region))
			region = REGION;
		
		if(!StringUtils.isEmpty(maxKeys))
			maxkeysInt = Integer.valueOf(maxKeys);
		
		ListObjectsRequest lor = new ListObjectsRequest(bucketName, prefix, marker, delimiter, maxkeysInt);
		ObjectListing list = ossClient.listObjects(lor);
		return list;
	}
	
	protected static String readValue(String filePath, String key) {
		Properties props = new Properties();
		String value = "";
		try {
			InputStream in = StsController.class.getClassLoader().getResourceAsStream(filePath);
			props.load(in);
			value = props.getProperty(key);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		ossClient = new OSSClient("oss-"+REGION+".aliyuncs.com",ACCESSID, ACCESSSECRET);
	}
}
