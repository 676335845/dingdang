package com.aliyun.upload_down_delete;

import java.util.List;

import com.heyi.framework.oss.OSSUploadClient;

public class DownloadFileTest {
	public static void main(String[] args) {
		//遍历所有附件,挨个下载,然后删除
		final OSSUploadClient ossInstance = OSSUploadClient.getClientInstance();
//		ossInstance.operateFiles(new OSSObjectOperation() {
//			@Override
//			public void operation(OSSObjectSummary objectSummary) {
//				
//				String fileId = objectSummary.getKey(); //文件id
//				//下载并获取文件对象的元数据
//				ObjectMetadata metadata = ossInstance.downloadFile(
//							fileId,
//							new File("E:\\"+fileId) //下载的目标文件
//						);
//				
//				//获取用户设置的元数据
//				System.out.println(metadata.getUserMetadata().get("mytest"));
//			}
//		}, 
//		false  //下载完成之后删除云上面的附件
//		);
		
		List<String> list = ossInstance.getPathByParent("provider/");
		for(String str:list){
			System.out.println(str);
			ossInstance.deleteFile(str);
		}
		
		
		
	}
}
