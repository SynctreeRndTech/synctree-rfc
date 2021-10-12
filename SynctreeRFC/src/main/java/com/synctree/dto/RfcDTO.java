package com.synctree.dto;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;

public class RfcDTO {

	private String param1;
	private HashMap<String, Object> param2;
	private ArrayList<String> param3;
	private JSONObject jsonObj; 
	private String key;
	
	public String getParam1() {
		return param1;
	}
	public void setParam1(String param1) {
		this.param1 = param1;
	}
	public HashMap<String, Object> getParam2() {
		return param2;
	}
	public void setParam2(HashMap<String, Object> param2) {
		this.param2 = param2;
	}
	public ArrayList<String> getParam3() {
		return param3;
	}
	public void setParam3(ArrayList<String> param3) {
		this.param3 = param3;
	}
	public JSONObject getJsonObj() {
		return jsonObj;
	}
	public void setJsonObj(JSONObject jsonObj) {
		this.jsonObj = jsonObj;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	@Override
	public String toString() {
		return "RfcDTO [param1=" + param1 + ", param2=" + param2 + ", param3=" + param3 + ", jsonObj=" + jsonObj
				+ ", key=" + key + "]";
	}
	
}
