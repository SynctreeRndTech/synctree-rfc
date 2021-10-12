package com.synctree.rfc;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

public class TestFunction {
	
	private static final Logger logger = LogManager.getLogger(TestFunction.class);

	public JSONObject testFunc(Object param) {
		
		HashMap<String, Object> hmap = new HashMap<String, Object>();
		hmap.put("1st function", "call succeed");
		hmap.put("param1", param);
		
		JSONObject obj = new JSONObject(hmap);
		
		return obj;
	}
	
	public JSONObject testFunc(Object param1, Object param2) {
		
		HashMap<String, Object> hmap = new HashMap<String, Object>();
		hmap.put("2nd function", "call succeed");
		hmap.put("param1", param1);
		hmap.put("param2", param2);
		
		JSONObject obj = new JSONObject(hmap);
		
		return obj;
	}

	public JSONObject testFunc(Object param1, Object param2, Object param3) {
				
		HashMap<String, Object> hmap = new HashMap<String, Object>();
		hmap.put("3rd function", "call succeed");
		hmap.put("param1", param1);
		hmap.put("param2", param2);
		hmap.put("param3", param3);
		
		JSONObject obj = new JSONObject(hmap);
		
		return obj;
	}
		 
}
