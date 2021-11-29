package com.synctree.controller;


import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.fasterxml.jackson.core.JsonProcessingException;

@org.springframework.web.bind.annotation.RestController
public class TestController {
	
	private static final Logger logger = LogManager.getLogger(TestController.class);
	
	//제휴사계약번호 임시생성
	@PostMapping("/partner/retreive/contractid") 
	public HashMap<String, Object> partnerRetrieveContractId(){
		
		logger.info("[PartnerRetrieveContractId Start]");
		
        GenerateCertNumber num = new GenerateCertNumber();
        num.setCertNumLength(5);
 		String contractID = "H00";
 		contractID += num.excuteGenerate();
 		contractID += "VS";
 		
		logger.info("[partner_contract_id generated] :: " + contractID);
		
		HashMap<String, Object> hmap = new HashMap<String, Object>();
		hmap.put("partner_contract_id", contractID);
		
		logger.info("[PartnerRetrieveContractId End]");
		
		return hmap;
	}
	
	public class GenerateCertNumber{
	    private int certNumLength = 10;
	    
	    public String excuteGenerate() {
	        Random random = new Random(System.currentTimeMillis());
	        
	        int range = (int)Math.pow(7,certNumLength);
	        int trim = (int)Math.pow(7, certNumLength-1);
	        int result = random.nextInt(range)+trim;
	         
	        if(result>range){
	            result = result - trim;
	        }
	        
	        return String.valueOf(result);
	    }

	    public int getCertNumLength() {
	        return certNumLength;
	    }

	    public void setCertNumLength(int certNumLength) {
	        this.certNumLength = certNumLength;
	    }
	}
	    
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

