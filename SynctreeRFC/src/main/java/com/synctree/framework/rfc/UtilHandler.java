package com.synctree.framework.rfc;

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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import com.synctree.util.Aes256Util;

public class UtilHandler {
	
	private static final Logger logger = LogManager.getLogger(UtilHandler.class);
	
    private static volatile Aes256Util INSTANCE;
    final static String secretKey = "jtpcq128365ekdjalekfj57168578903"; //32bit
    private static String IV = secretKey.substring(0, 16);//16bit



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
    public static JSONObject AES256Func(RemoteFunctionDTO dto) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        
    	logger.info("AES_Encode/Decode Method Call Succeed");
        
        byte[] keyData = secretKey.getBytes();

        SecretKey secureKey = new SecretKeySpec(keyData, "AES");

        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes()));
        
        byte[] encrypted = c.doFinal(dto.str1.getBytes("UTF-8"));
        String enStr = new String(Base64.encodeBase64(encrypted));
        
        c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes("UTF-8")));
        byte[] byteStr = Base64.decodeBase64(dto.str2.getBytes());
        String deStr = new String(c.doFinal(byteStr), "UTF-8");

		HashMap<String, Object> hmap = new HashMap<String, Object>();
		hmap.put("encrypted", enStr);
		hmap.put("decrypted", deStr);
		
		JSONObject obj = new JSONObject(hmap);
		
    	logger.info("AES_Encode/Decode Method End");
    	
        return obj;
    }

}
