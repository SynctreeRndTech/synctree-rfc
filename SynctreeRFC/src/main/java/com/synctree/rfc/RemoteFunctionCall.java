package com.synctree.rfc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;


@org.springframework.web.bind.annotation.RestController
public class RemoteFunctionCall {
	
	private static final Logger logger = LogManager.getLogger(RemoteFunctionCall.class);
	
	/*param=JSONObject*/
	@PostMapping("/synctree/rfc")
	public JSONObject remoteFunctionCallUtil(@RequestHeader(value="functionName") String functionName, @RequestHeader(value="className") String className,
			                                 @RequestHeader(value="X-Synctree-Secure-Key") String secKey, @RequestHeader(value="X-Synctree-Verification-Code") String verifyCode) {
			
		String responseData = "";
	    HttpURLConnection conn = null;
		OutputStream os = null;
		JSONObject paramObj = new JSONObject();
		JSONObject result = new JSONObject();
		
		/*보안 프로토콜*/
		try {
			
			logger.info("[className]: " + className);
			logger.info("[functionName]: " + functionName);
			logger.info("[X-Synctree-Secure-Key]: " + secKey);
			logger.info("[X-Synctree-Verification-Code]: " + verifyCode);
			
			URL url = new URL("https://seoul.synctreengine.com/secure/getCommand");
	        conn = (HttpURLConnection) url.openConnection();
	            
	        conn.setRequestMethod("POST");
	        conn.setDoOutput(true);
	        conn.setConnectTimeout(10000);
	        conn.setRequestProperty("Content-Type", "application/json");
	        conn.setRequestProperty("Accept", "application/json");
	        conn.setRequestProperty("X-Synctree-Service", "managed.redis");
	        conn.setRequestProperty("X-Synctree-Service-Method", "loader");
	        conn.setRequestProperty("X-Synctree-Secure-Key", secKey);
	        conn.setDoOutput(true);
	        conn.setDoInput(true);
	
	        try {
	            os = conn.getOutputStream();
	            byte[] verifyKey = secKey.getBytes(StandardCharsets.UTF_8);
	            os.write(verifyKey);
	        } catch(Exception e) {
					e.printStackTrace();
		    } finally {
				os.flush();
	            os.close();
			}
	
			int statusCode = conn.getResponseCode();
			String responseMsg = String.valueOf(conn.getResponseMessage());
			logger.info("[Status Code] "+ statusCode + ", [Response Message] " + responseMsg);
				
			if (statusCode == 200) {
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));	
				StringBuffer sb =  new StringBuffer();	       
				while ((responseData = br.readLine()) != null) {
					sb.append(responseData);
				}
				logger.info("[params] " + sb); 
					 
				if (br != null) {
					br.close();
				}
				
				JSONParser parser = new JSONParser();
				try {
					paramObj = (JSONObject) parser.parse(sb.toString());
				} catch (ParseException e) { 
					e.printStackTrace();
				}
				
			} else {
				logger.error("[ErrorCode] " + statusCode + ", [ErrorMsg] " + responseMsg);
			}
				
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(conn!= null) {
				conn.disconnect();
			}
		}
	
		try {
			  Class<?> cls = Class.forName(className);
			  Constructor<?> constructor = cls.getConstructor(new Class[]{});
			  Object rfcObj = null;
				try {
					rfcObj = constructor.newInstance(new Object[]{});
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
				
			  Method m = cls.getDeclaredMethod(functionName, JSONObject.class);
			  result = (JSONObject) m.invoke(rfcObj, paramObj);
			  
		} catch (ClassNotFoundException e) {
			logger.error("ClassNotFoundException from Class.forName()");
		} catch (NoSuchMethodException e) {
			logger.error("NoSuchMethodException from getDeclaredMethod()");
		} catch (InvocationTargetException e) {
			logger.error("InvocationTargetExceptionn from invoke()"); 
		} catch (IllegalAccessException e) {
			logger.error("IllegalAccessException from invoke()");
		}
	
		return result;
	}    
}

