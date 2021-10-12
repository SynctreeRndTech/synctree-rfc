package com.synctree.rfc;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.fasterxml.jackson.core.JsonProcessingException;

@org.springframework.web.bind.annotation.RestController
public class TestController {
	
	private static final Logger logger = LogManager.getLogger(TestController.class);
	
	
	@GetMapping("/") 
	public String main(String[] args) {
		logger.info("Call succeed");
		return "Hello";
	}
	
	@GetMapping("/test") 
	public JSONObject testJsonObject() throws JsonProcessingException {
		
		logger.info("Call succeed");
		
		HashMap<String, String> hmap = new HashMap<String, String>();
		hmap.put("test", "hello");
		
		JSONObject jsonObj = new JSONObject(hmap);

		return jsonObj;
	}
	
	@GetMapping("/test2") 
	public HashMap<String, Object> testObject(){
		
		logger.info("Call succeed");
		
		HashMap<String, Object> hmap = new HashMap<String, Object>();
		hmap.put("param1", "hello");
		
		return hmap;
	}
	
	static class CallFunc {
		
		public void functionName()
		{
			logger.info("funcA(): " + this.getClass().getName());
		}
		
		public boolean functionName(boolean param1, int param2)
		{
			boolean ret = param1;

			logger.info("funcB(): " + this.getClass().getName());
			logger.info("param1: " + param1);
			logger.info("param2: " + param2);
			return ret;
		}
	}

	  @PostMapping("/time-sleep-test")
	  public String delayTest() {
		  logger.info("time-sleep-test began");
		  try{
			  TimeUnit.SECONDS.sleep(5);
		  } catch (InterruptedException e) {
			  
		  }
		  logger.info("time-sleep-test successed");
		  return "Hello";
	  }  	 
	  
}

