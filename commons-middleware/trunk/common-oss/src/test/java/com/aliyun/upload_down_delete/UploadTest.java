package com.aliyun.upload_down_delete;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.heyi.framework.oss.OSSUploadClient;

public class UploadTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File file = new File("E:/soft/20140831080351354.jpg");
		Map<String, String> userMetadata = new HashMap<String, String>();
		try {
			userMetadata.put("mytest", "testestsetset"+System.currentTimeMillis()); //每个上传的文件可以自定义一些用户元数据，将来在下载文件的时候可以得到这些数据
			String filePath = "soft/20140831080351354.jpg";
			String id = OSSUploadClient.getClientInstance().uploadFile(filePath, file, userMetadata);
			System.out.println("已上传："+id);
		} catch (OSSException e) {
			//该异常在对开放存储数据服务（Open Storage Service）访问失败时抛出。
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientException e) {
			//表示尝试访问阿里云服务时遇到的异常。
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
