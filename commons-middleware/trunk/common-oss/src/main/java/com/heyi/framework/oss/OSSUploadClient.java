package com.heyi.framework.oss;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;

public class OSSUploadClient {	
	private static OSSUploadClient instance;
	private  static final Logger logger = LoggerFactory.getLogger(OSSUploadClient.class);
	private final OSSClient client;
	
	private static boolean isInternal = false;
	
	private OSSUploadClient(boolean isinternal) {
		if(isinternal)
			client = new OSSClient(Property.ENDPOINT_INTERNAL,Property.ACCESS_ID, Property.ACCESS_KEY);
		else
			client = new OSSClient(Property.ENDPOINT,Property.ACCESS_ID, Property.ACCESS_KEY);	
	}
	
	static {
		try {
			new Socket(Property.ENDPOINT_INTERNAL, 80).close();
			isInternal = true;
		} catch (ConnectException e) {
		} catch (Exception e) {
		}
	}
	
	public OSSClient getClient() {
		return client;
	}

	/**
	 * 获取oss访问实例
	 * @return
	 */
	public synchronized static OSSUploadClient getClientInstance() {
		if(instance==null)
			instance = new OSSUploadClient(isInternal);
		return instance;
	}
		
	 /**
	  * 上传文件
	  * @param filePath
	  * @param file
	  * @param userMetadata
	  * @return
	  * @throws OSSException
	  * @throws ClientException
	  * @throws FileNotFoundException
	  */
    public String uploadFile(String filePath,File file , Map<String, String> userMetadata)
            throws OSSException, ClientException, FileNotFoundException {
        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentLength(file.length());
        objectMeta.setUserMetadata(userMetadata);
        InputStream input = new FileInputStream(file);
        client.putObject(Property.BucketName, filePath, input, objectMeta);
        return filePath;
    }
    
    /**
     * 以输入流的方式上传文件
     * @param filePath
     * @param input
     * @param userMetadata
     * @return
     * @throws OSSException
     * @throws ClientException
     * @throws IOException
     */
    public String uploadFile(String filePath,InputStream input, Map<String, String> userMetadata)
    		throws OSSException, ClientException, IOException{
    	ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentLength(input.available()); 
        objectMeta.setUserMetadata(userMetadata);
        client.putObject(Property.BucketName, filePath, input, objectMeta);
        return filePath;
    }
    
    
    //下载文件
    public ObjectMetadata downloadFile(String fileId, File file){
    	return client.getObject(new GetObjectRequest(Property.BucketName, fileId), file);
    }
    
    /**
     * 文件是否存在
     * @param key
     * @return
     */
    public boolean doesObjectExist(String key) {
    	return client.doesObjectExist(Property.BucketName, key);
    }
    
    /**
     * 遍历已上传的文件
     * @param objectOperation
     * @param deleteObjectAfterOperation
     */
    public void operateFiles(OSSObjectOperation objectOperation ,boolean deleteObjectAfterOperation){
    	ObjectListing list = client.listObjects(Property.BucketName);
    	List<OSSObjectSummary> summaries = list.getObjectSummaries();
        for (int i = 0; i < summaries.size(); i++) {
        	OSSObjectSummary summary =  summaries.get(i);
        	try{
        		objectOperation.operation(summary);
        		if(deleteObjectAfterOperation){
            		client.deleteObject(Property.BucketName, summary.getKey());
            	}
        	}finally{
        	}
        }
    }
    
    /**
     * 获取指定文件夹下的所有文件列表
     * @param path
     * @return 获取到的文件夹下 的 文件路径
     */
    public  List<String> getPathByParent(String path){
    	List<String> allPath = new ArrayList<>();;
		try {
			ListObjectsRequest listObjectsRequest = new ListObjectsRequest(Property.BucketName);
			
			// "/" 为文件夹的分隔符
			listObjectsRequest.setDelimiter("/");
			// 列出fun目录下的所有文件和文件夹
			listObjectsRequest.setPrefix(path);
			ObjectListing listing = client.listObjects(listObjectsRequest);
			// 遍历所有Object
			for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
				allPath.add(objectSummary.getKey());
			}
		} catch (Exception e) {
			logger.error("",e);
		} 
    	return allPath;
    }
    
    /**
     * 
     * @param path
     * @return
     */
    public  List<OSSObjectSummary> listObjects(String path){
    	List<OSSObjectSummary> ret = new ArrayList<>(0);
		try {
			ListObjectsRequest listObjectsRequest = new ListObjectsRequest(Property.BucketName);
			
			// "/" 为文件夹的分隔符
			listObjectsRequest.setDelimiter("/");
			// 列出fun目录下的所有文件和文件夹
			listObjectsRequest.setPrefix(path);
			ObjectListing listing = client.listObjects(listObjectsRequest);
			return listing.getObjectSummaries();
		} catch (Exception e) {
			logger.error("",e);
		} 
    	return ret;
    }
    
    /**
     * 根据指定的 文件路径删除文件
     * @param key
     */
    public void deleteFile(String key){
    	client.deleteObject(Property.BucketName, key);
    }
    
    
		
	private static String generateID() {
		String rtnVal = Long.toHexString(System.currentTimeMillis());
		rtnVal += UUID.randomUUID();
		rtnVal = rtnVal.replaceAll("-", "");
		return rtnVal.substring(0, 32);
	}
}
