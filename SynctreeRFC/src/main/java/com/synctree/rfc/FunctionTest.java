package com.synctree.rfc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import com.synctree.framework.rfc.RFCController;
import com.synctree.util.Aes256Util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class FunctionTest implements RemoteFunction{

	static final Logger logger = LogManager.getLogger(RFCController.class);
	
	public JSONObject remoteFunction(JSONObject obj) {

		logger.info("[RemoteFunction Start!");
		
		JSONObject result = new JSONObject();
		
		/*ex) 파라미터 셋팅*/
		String param1 = (String) obj.get("param1");
		String param2 = (String) obj.get("param2");
		
		/*ex) 라이브러리 호출*/
		try {
			result = Aes256Util.AES256Func(param1, param2);
		} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		
		logger.info("[RemoteFucnction End!]");
		
		return result;
	}   
}
