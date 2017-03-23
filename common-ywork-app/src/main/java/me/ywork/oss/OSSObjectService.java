package me.ywork.oss;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;

/**
 * 该示例代码展示了如果在OSS中创建和删除一个Bucket，以及如何上传和下载一个文件。
 * 
 * 该示例代码的执行过程是： 1. 创建一个Bucket（如果已经存在，则忽略错误码）； 2. 上传一个文件到OSS； 3. 下载这个文件到本地； 4.
 * 清理测试资源：删除Bucket及其中的所有Objects。
 * 
 * 尝试运行这段示例代码时需要注意： 1. 为了展示在删除Bucket时除了需要删除其中的Objects,
 * 示例代码最后为删除掉指定的Bucket，因为不要使用您的已经有资源的Bucket进行测试！ 2.
 * 请使用您的API授权密钥填充ACCESS_ID和ACCESS_KEY常量； 3.
 * 需要准确上传用的测试文件，并修改常量uploadFilePath为测试文件的路径； 修改常量downloadFilePath为下载文件的路径。 4.
 * 该程序仅为示例代码，仅供参考，并不能保证足够健壮。
 * 
 */
public class OSSObjectService {
	private static Logger logger = LoggerFactory.getLogger(OSSObjectService.class);
	private static OSSClient client = null;
	private static OSSClient clientGet = null;

	static {
		logger.debug(
				"创建连接到阿里OSS，参数：ACCESS_ID:{}, ACCESS_KEY:{}, OSS_ENDPOINT:{}",
				OSSConstant.ACCESS_ID, OSSConstant.ACCESS_KEY,OSSConstant.OSS_ENDPOINT);
		client = new OSSClient(OSSConstant.OSS_ENDPOINT,
				OSSConstant.ACCESS_ID, OSSConstant.ACCESS_KEY);
		clientGet = new OSSClient(OSSConstant.OSS_ENDPOINT_WEB,
				OSSConstant.ACCESS_ID, OSSConstant.ACCESS_KEY);

	}

	// 上传文件
	public static void uploadFile(String bucketName, String key, String filename)
			throws OSSException, ClientException, FileNotFoundException {
		File file = new File(filename);
		ObjectMetadata objectMeta = new ObjectMetadata();
		//objectMeta.setContentLength(file.length());
		// 可以在metadata中标记文件类型
		objectMeta.setContentType("application/vnd.ms-excel;charset=UTF-8");
		InputStream input = new FileInputStream(file);
		client.putObject(bucketName, key, input, objectMeta);
	}

	// 上传文件
	public static void uploadFile(String bucketName, String key, InputStream input, String contentType)
			throws OSSException, ClientException, FileNotFoundException {
		// File file = new File(filename);
		ObjectMetadata objectMeta = new ObjectMetadata();
		//objectMeta.setContentLength(size);
		// 可以在metadata中标记文件类型
		if(StringUtils.isNotBlank(contentType)){
			objectMeta.setContentType(contentType);
		}else{
			objectMeta.setContentType("application/vnd.ms-excel;charset=UTF-8");
		}
		// InputStream input = new FileInputStream(file);
		logger.debug("开始向OSS发送资源，Key:{}", key);
		PutObjectResult result = client.putObject(bucketName,
				key, input, objectMeta);

		logger.debug("OSS返回结果:{}", result.getETag());

		return;
	}
	
	// 上传文件
	public static void uploadExcelFile(String bucketName, String key, InputStream input)
			throws OSSException, ClientException, FileNotFoundException {
		// File file = new File(filename);
		ObjectMetadata objectMeta = new ObjectMetadata();
		//objectMeta.setContentLength(size);
		// 可以在metadata中标记文件类型
		objectMeta.setContentType("application/vnd.ms-excel;charset=UTF-8");
		// InputStream input = new FileInputStream(file);
		logger.debug("开始向OSS发送资源，Key:{}", key);
		PutObjectResult result = client.putObject(bucketName,
				key, input, objectMeta);

		logger.debug("OSS返回结果:{}", result.getETag());

		return;
	}


	// 下载文件
	public static void downloadFile(OSSClient client, String bucketName,
			String key, String filename) throws OSSException, ClientException {
		client.getObject(new GetObjectRequest(bucketName, key), new File(
				filename));
	}
	
	public static InputStream getObject(String bucketName, String key) throws IOException {
	    // 获取Object，返回结果为OSSObject对象
	    OSSObject object = client.getObject(bucketName, key);
	    // 获取Object的输入流
	    InputStream objectContent = object.getObjectContent();
	    return objectContent;
	}

	/**
	 * 获取文件URL地址
	 * @param bucketName
	 * @param key
	 * @return
	 */
	public static String getFileUrl(String bucketName, String key) {
		Date expires = new Date(new Date().getTime() + 1000 * 60 * 60); // 60 minute
		// to expire
		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(
				bucketName, key);
		generatePresignedUrlRequest.setExpiration(expires);
		URL url = clientGet.generatePresignedUrl(generatePresignedUrlRequest);
		logger.info("获取OSS文件URL地址{}", url);
		return url.toString();
	}
}
