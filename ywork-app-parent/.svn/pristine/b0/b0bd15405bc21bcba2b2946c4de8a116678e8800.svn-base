package me.ywork.salarybill;

import java.util.Map;
  
/**
 * 
 * @author 梁栋
 * @version 1.0
 * @since 1.0
 */
public class Test {
	
    private String publicKey;
    private String privateKey;
  
    public static void main(String[]args) throws Exception{
    	Test t = new Test();
    	t.setUp();
    	t.testSign();
    }
    public void setUp() throws Exception {
        Map<String, Object> keyMap = RSAUtil.initKey();
        publicKey = RSAUtil.getPublicKey(keyMap);
        privateKey = RSAUtil.getPrivateKey(keyMap);
        System.err.println("公钥: \n\r" + publicKey);
        System.err.println("私钥： \n\r" + privateKey);
    }
  
    public void test() throws Exception {
    	
        System.err.println("公钥加密——私钥解密");
        String inputStr = "abc";
        byte[] data = inputStr.getBytes();
        
        //公钥加密
        byte[] encodedData = RSAUtil.encryptByPublicKey(data, publicKey);
  
        //私钥解密
        byte[] decodedData = RSAUtil.decryptByPrivateKey(encodedData,
                privateKey);
  
        String outputStr = new String(decodedData);
        System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);
        //assertEquals(inputStr, outputStr);
  
    }
  
    public void testSign() throws Exception {
        System.err.println("私钥加密——公钥解密");
        String inputStr = "sign";
        byte[] data = inputStr.getBytes();
  
        //私钥加密
        byte[] encodedData = RSAUtil.encryptByPrivateKey(data, privateKey);
        String inputStrRSA = new String(encodedData);
        
  
        //公钥解密
        byte[] decodedData = RSAUtil
                .decryptByPublicKey(encodedData, publicKey);
  
        String outputStr = new String(decodedData);
        System.err.println("加密前: " + inputStrRSA + "\n\r" + "解密后: " + outputStr);
        
  
        System.err.println("私钥签名——公钥验证签名");
        // 产生签名
        String sign = RSAUtil.sign(encodedData, privateKey);
        System.err.println("签名:\r" + sign);
  
        // 验证签名
        boolean status = RSAUtil.verify(encodedData, publicKey, sign);
        System.err.println("状态:\r" + status);
  
    }
  
}