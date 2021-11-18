package com.synctree.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synctree.framewok.rfc.RFCController;

@Service
public class Temp {
	
	private static final Logger logger = LogManager.getLogger(RFCController.class);
	
	 //보험료 조회
    public static JSONObject retrievePremium(JSONObject obj) {
        
    	logger.info("=====retrievePremium RFC Succeed=====");

		//보험료 조회 로직 here
    	int premiumPrice = 9840;
    	
    	//응답값 셋팅
		HashMap<String, Object> hmap = new HashMap<String, Object>();
		hmap.put("startDate", obj.get("startDate"));
		hmap.put("startTime", obj.get("startTime"));
		hmap.put("endDate", obj.get("endDate"));
		hmap.put("endTime", obj.get("endTime"));
		hmap.put("period", obj.get("period"));
		hmap.put("birthDate", obj.get("birthDate"));
		hmap.put("age", obj.get("age"));
		hmap.put("price", premiumPrice); //보험료 조회 결과 Demo용 하드코딩
				
		JSONObject result = new JSONObject(hmap);
		logger.info(result.toJSONString());
		
    	logger.info("=====retrievePremium RFC End=====");
    	
        return result;
    }
    
	 //보험료 가입
    public static JSONObject takeOutInsurance (JSONObject obj) {
        
    	logger.info("=====takeOutInsurance RFC Succeed=====");

    	Map<String, Object> userInfoMap = null;
    	Map<String, Object> newUserInfo = null;
    	String registNum = null;
    	StringBuffer sb = new StringBuffer();
    	ArrayList registNumArr = null;
    	
    	
    	try {
    		userInfoMap = new ObjectMapper().readValue(obj.get("userInfo").toString(), Map.class);
			registNumArr = (ArrayList) userInfoMap.get("registNum");
			if(registNumArr != null) {
				registNum = registNumArr.get(1).toString();
				if(registNum.length() == 7) {
					sb.append(registNum.subSequence(0, 1)).append("******");
					registNum = sb.toString();
				}
			}
			registNumArr.set(1, registNum);
			userInfoMap.replace("registNum", registNumArr);
			userInfoMap.remove("phoneNum");
			userInfoMap.remove("email");
    	} catch (JsonParseException e) {
	        e.printStackTrace();
	    } catch (JsonMappingException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
    			
		//보험료 가입 로직 here
    	String productName = "참좋은 여행자 보험"; //보험상품 Demo용 임시셋팅
    	Object userName = obj.get("userInfo");
    	
    	//응답값 셋팅
		HashMap<String, Object> hmap = new HashMap<String, Object>();
		hmap.put("productName ", productName);
		hmap.put("startDate", obj.get("startDate"));
		hmap.put("startTime", obj.get("startTime"));
		hmap.put("endDate", obj.get("endDate"));
		hmap.put("endTime", obj.get("endTime"));
		hmap.put("period", obj.get("period"));
		hmap.put("age", obj.get("age"));
		hmap.put("price", obj.get("price"));
		hmap.put("userInfo", userInfoMap);
				
		JSONObject result = new JSONObject(hmap);
		logger.info(result.toJSONString());
		
    	logger.info("=====takeOutInsurance RFC End=====");
    	
        return result;
    }
}
