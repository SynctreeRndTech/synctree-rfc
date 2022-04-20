package com.synctree.rfc.handler;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONObject;

import com.synctree.rfc.framework.RfcDTO;
import com.synctree.util.enc.Aes256Util;
import com.synctree.util.logging.SynctreeLogger;

public class UtilHandler {
	
	private static final SynctreeLogger logger = new SynctreeLogger(UtilHandler.class.getName());
	
    private static volatile Aes256Util INSTANCE;
    final static String secretKey = "jtpcq128365ekdjalekfj57168578903"; //32bit
    private static String IV = secretKey.substring(0, 16); //16bit



    public static Aes256Util getInstance() {
        if (INSTANCE == null) {
            synchronized (Aes256Util.class) {
                if (INSTANCE == null)
                    INSTANCE = new Aes256Util();
            }
        }
        return INSTANCE;
    }
    
    //암복호화
    public static JSONObject AES256Func(RfcDTO dto) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        
    	logger.info("AES_Encode/Decode Method Call Succeed");
        
        String str1 = (String) dto.req.get("str1");
        String str2 = (String) dto.req.get("str2");
        
        byte[] keyData = secretKey.getBytes();

        SecretKey secureKey = new SecretKeySpec(keyData, "AES");

        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes()));
               
        byte[] encrypted = c.doFinal(str1.getBytes("UTF-8"));
        String enStr = new String(Base64.encodeBase64(encrypted));
        
        c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes("UTF-8")));
        byte[] byteStr = Base64.decodeBase64(str2.getBytes());
        String deStr = new String(c.doFinal(byteStr), "UTF-8");

		HashMap<String, Object> hmap = new HashMap<String, Object>();
		hmap.put("Encrypted", enStr);
		hmap.put("Decrypted", deStr);
		
		JSONObject obj = new JSONObject(hmap);
		
    	logger.info("AES_Encode/Decode Method End");
    	
        return obj;
    }

}
