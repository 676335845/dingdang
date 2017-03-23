package me.ywork.org.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * 钉钉通用接口工具类
 * 
 */
public class DingAPIHttpUtil {

	private static Logger logger = LoggerFactory.getLogger(DingAPIHttpUtil.class);

	/**
	 * 发起https请求并获取结果
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * @param outputStr
	 *            提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpRequest(String requestUrl,
			String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		HttpsURLConnection httpUrlConn = null;
		logger.trace("httRequest start:----" + requestUrl);
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			httpUrlConn = (HttpsURLConnection) url
					.openConnection();

			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			httpUrlConn.setRequestProperty("Content-Type", "application/json");

			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;			
			jsonObject = JSON.parseObject(buffer.toString());
		} catch (ConnectException ce) {
			logger.error("Ali Ding server connection timed out.");
		} catch (JSONException e) {
			if(httpUrlConn.getHeaderField("Location")!=null){
				logger.error("302 Found:"  + httpUrlConn.getHeaderField("Location"));
			}
			logger.error("处理JSON错误,requestUrl:{},outputStr:{},content:{}"  ,requestUrl,outputStr,buffer.toString());
		} catch (Exception e) {
			logger.error("https request error:{}", e);
		}finally{
			if(httpUrlConn!=null){
				httpUrlConn.disconnect();
			}
		}
		logger.trace("httRequest end:----" + requestUrl);
		return jsonObject;
	}

	/**
	 * 获取请求的body信息
	 * 
	 * @param br
	 * @return
	 */
	public static JSONObject getBody(BufferedReader br) {
		String inputLine;
		String str = "";
		JSONObject rtnObj = new JSONObject();
		try {
			while ((inputLine = br.readLine()) != null) {
				str += inputLine;
			}
			br.close();
			rtnObj = JSONObject.parseObject(str);
		} catch (Exception e) {
			System.out.println("IOException: " + e);
			logger.error("",e);
		}

		return rtnObj;
	}

	public static String httpPost(String url, String requestJsonObject,
			String resultJsonKey) throws UnsupportedEncodingException {
		HttpPost httpPost = new HttpPost(url);
		CloseableHttpResponse response = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(8000).setConnectTimeout(3000).build();
		httpPost.setConfig(requestConfig);
		httpPost.addHeader("Content-Type", "application/json");

		StringEntity requestEntity = new StringEntity(requestJsonObject,
				"utf-8");
		httpPost.setEntity(requestEntity);

		try {
			response = httpClient.execute(httpPost, new BasicHttpContext());
			if (response.getStatusLine().getStatusCode() != 200) {

				return null;
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String resultStr = EntityUtils.toString(entity, "utf-8");

				JSONObject result = JSON.parseObject(resultStr);
				// 0-成功;错误码为-1:系统繁忙,40014-不合法的access_token,40017-不能跨企业发送消息
				System.out.println("=====消息返回结果=====" + result);
				if (result != null) {
					return result.toString();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("",e);
		} finally {
			if (response != null)
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		return null;
	}
}
