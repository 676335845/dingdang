package me.ywork.sts.web;

import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;

/**
 * sts token service
 */
@Controller
@RequestMapping(value = "**/sts")
public class StsController{
	private static final Logger LOG = LoggerFactory.getLogger(StsController.class);
	
	public static final String REGION = readValue("ossconfig.properties", "oss.region");
    // 当前 STS API 版本
    public static final String STS_API_VERSION = readValue("stsconfig.properties", "sts.api.version");
    
    public static final String STS_RAM_ACCESSKEYID = readValue("stsconfig.properties", "sts.ram.accessKeyId");
    public static final String STS_RAM_ACCESSKEYSECRET = readValue("stsconfig.properties", "sts.ram.accessKeySecret");
    public static final String STS_RAM_ROLEARN = readValue("stsconfig.properties", "sts.ram.roleArn");
    public static final String STS_ROLE_SESSIONNAME = readValue("stsconfig.properties", "sts.roleSessionName");
    public static final String STS_RAM_USERID = readValue("stsconfig.properties", "sts.ram.aliyunuserid");
    public static final String OSS_DEFAULT_BUCKET = readValue("ossconfig.properties", "oss.defaultbucket");
    
    
    static AssumeRoleResponse assumeRole(String accessKeyId, String accessKeySecret,
                                         String roleArn, String roleSessionName, String policy,
                                         ProtocolType protocolType) throws ClientException {
        try {
            // 创建一个 Aliyun Acs Client, 用于发起 OpenAPI 请求
            IClientProfile profile = DefaultProfile.getProfile(REGION, accessKeyId, accessKeySecret);
            DefaultAcsClient client = new DefaultAcsClient(profile);

            // 创建一个 AssumeRoleRequest 并设置请求参数
            final AssumeRoleRequest request = new AssumeRoleRequest();
            //request.setVersion(STS_API_VERSION);
            request.setMethod(MethodType.POST);
            request.setProtocol(protocolType);

            request.setRoleArn(roleArn);
            request.setRoleSessionName(roleSessionName);
            request.setPolicy(policy);

            // 发起请求，并得到response
            final AssumeRoleResponse response = client.getAcsResponse(request);

            return response;
        } catch (ClientException e) {
            throw e;
        }
    }
	@ResponseBody
    @RequestMapping(value = "/gettoken**", method = RequestMethod.GET)
    public JSONObject getMessage(HttpServletRequest request,
			HttpServletResponse response) {
    	JSONObject msg = null;
    	String bucket = request.getParameter("bucketName");
    	if(StringUtils.isEmpty(bucket))
    		bucket = OSS_DEFAULT_BUCKET;
    		
    	// 只有 RAM用户（子账号）才能调用 AssumeRole 接口
        // 阿里云主账号的AccessKeys不能用于发起AssumeRole请求
        // 请首先在RAM控制台创建一个RAM用户，并为这个用户创建AccessKeys
        // 参考：https://docs.aliyun.com/#/pub/ram/ram-user-guide/user_group_management&create_user
        //String accessKeyId = "b7uMLQFN4dLrLqHS";
        //String accessKeySecret = "EZlFPpLvooh5SECOWW41SHlRDGJt47";

        // AssumeRole API 请求参数: RoleArn, RoleSessionName, Polciy, and DurationSeconds
        // 参考： https://docs.aliyun.com/#/pub/ram/sts-api-reference/actions&assume_role

        // RoleArn 需要在 RAM 控制台上获取
        // 参考: https://docs.aliyun.com/#/pub/ram/ram-user-guide/role&user-role
        //String roleArn = "acs:ram::1586223188789105:role/webossaccess";

        // RoleSessionName 是临时Token的会话名称，自己指定用于标识你的用户，主要用于审计，或者用于区分Token颁发给谁
        // 但是注意RoleSessionName的长度和规则，不要有空格，只能有'-' '.' '@' 字母和数字等字符
        // 具体规则请参考API文档中的格式要求
        //String roleSessionName = "lanyun";

        // 如何定制你的policy?
        // 参考: https://docs.aliyun.com/#/pub/ram/ram-user-guide/policy_reference&struct_def
        // OSS policy 例子: https://docs.aliyun.com/#/pub/oss/product-documentation/acl&policy-configure
        // OSS 授权相关问题的FAQ: https://docs.aliyun.com/#/pub/ram/faq/oss&basic
        String policy = "{\n" +
                "    \"Version\": \"1\", \n" +
                "    \"Statement\": [\n" +
                "        {\n" +
                "            \"Action\": [\n" +
                "                \"oss:GetBucket\", \n" +
                "                \"oss:PutObject\", \n" +
                "                \"oss:GetObject\" \n" +
                "            ], \n" +
                "            \"Resource\": [\n" +
                "                \"acs:oss:*:"+STS_RAM_USERID+":"+bucket+"\", \n" +
                "                \"acs:oss:*:"+STS_RAM_USERID+":"+bucket+"/*\" \n" +
                "            ], \n" +
                "            \"Effect\": \"Allow\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        // 此处必须为 HTTPS
        ProtocolType protocolType = ProtocolType.HTTPS;

        try {
            final AssumeRoleResponse arResponse = assumeRole(STS_RAM_ACCESSKEYID, STS_RAM_ACCESSKEYSECRET,
            		STS_RAM_ROLEARN, STS_ROLE_SESSIONNAME, policy, protocolType);
            
            response.setHeader("Access-Control-Allow-Origin", "*");
            
            msg = (JSONObject) JSON.toJSON(arResponse);
        } catch (ClientException e) {
        	LOG.error("Failed to get a token.Error code:{},Error message:",new Object[]{
        			e.getErrCode(),
        			e.getErrMsg()
        	});        }
        
    	return msg;
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
}
