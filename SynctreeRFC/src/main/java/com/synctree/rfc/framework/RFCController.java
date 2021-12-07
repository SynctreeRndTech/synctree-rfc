package com.synctree.rfc.framework;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RFCController {

	private static final Logger logger = LogManager.getLogger(RFCController.class);

	/* param=JSONObject */
	@PostMapping("/synctree/rfc")
	public JSONObject remoteFunctionCall(@RequestHeader(value = "functionName") String functionName, @RequestHeader(value = "className") String className, @RequestHeader(value = "protocolUrl") String protocolUrl,
										 @RequestHeader(value = "X-Synctree-Secure-Key") String secKey, @RequestHeader(value = "X-Synctree-Verification-Code") String verifyCode) throws NoSuchFieldException, SecurityException {

			
		// 보안프로토콜로 통신하기
		JSONObject paramObj = new JSONObject();		
		paramObj = callWithSecureProtocol(functionName, className, protocolUrl, secKey, verifyCode, /*out*/ paramObj);		
		
		// 동적 매소드 호출하기 -handler method 호출
		return callDynamicMethod(functionName, className, paramObj);		
	}
	
	public JSONObject callWithSecureProtocol( String functionName, 
											  String className, 
											  String protocolUrl, 
											  String secKey, 
											  String verifyCode, 
											  /*out*/ JSONObject paramObj) {
		String responseData = "";
		HttpURLConnection conn = null;
		OutputStream os = null;

		/* 보안 프로토콜 */
		try {

			logger.info("[className]: " + className);
			logger.info("[functionName]: " + functionName);
			logger.info("[X-Synctree-Secure-Key]: " + secKey);
			logger.info("[X-Synctree-Verification-Code]: " + verifyCode);

			// URL url = new URL("https://seoul.synctreengine.com/secure/getCommand"); //실서버
			URL url = new URL(protocolUrl);
			
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
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				os.flush();
				os.close();
			}

			int statusCode = conn.getResponseCode();
			String responseMsg = String.valueOf(conn.getResponseMessage());
			logger.info("[Secure Protocol Response] Status Code: " + statusCode + ", Response Message: " + responseMsg);

			if (statusCode == 200) {
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
				StringBuffer sb = new StringBuffer();
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
			if (conn != null) {
				conn.disconnect();
			}
		}
		
		return paramObj;
	}
	
	public JSONObject callDynamicMethod(String functionName, String className, JSONObject paramObj) throws NoSuchFieldException {
	
		Object methodResult = new Object();
		JSONObject res = new JSONObject();
		RfcDTO rfcDto = new RfcDTO();
		
		/* 동적 호출 */
		try {
			Class<?> cls = Class.forName(className);
			Constructor<?> constructor = cls.getConstructor(new Class[] {});
			Object rfcObj = null;
			try {
				rfcObj = constructor.newInstance(new Object[] {});
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}

			Class<?> dtoCls = Class.forName("com.synctree.rfc.framework.RfcDTO");
			rfcDto.setReq((HashMap<String, Object>) paramObj.get("req"));
			Method m = cls.getDeclaredMethod(functionName, dtoCls);
			methodResult = m.invoke(rfcObj, rfcDto);

			res.put("res", methodResult);
			
		} catch (ClassNotFoundException e) {
			logger.error("ClassNotFoundException from Class.forName()");
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			logger.error("NoSuchMethodException from getDeclaredMethod()");
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			logger.error("InvocationTargetExceptionn from invoke()");
			e.printStackTrace();	
			String getCause = e.getCause().toString();
			String checkCause = getCause.substring(24, 36);
			//RMI로 SQLException이 여기서만 잡혀서 임시 처리, 방법 모색
			if (checkCause.equals("DuplicateKey")) {
				ArrayList<HashMap<String, Object>> temp = new ArrayList<HashMap<String, Object>>();
				HashMap<String, Object> hash = new HashMap<String, Object>();
				hash.put("resultCode", "E001");
				hash.put("resultMessage", "이미 가입한 상품입니다.");
				temp.add(0, hash);
				res.put("res", temp);
				logger.info(res.toJSONString());
			}
		} catch (IllegalAccessException e) {
			logger.error("IllegalAccessException from invoke()");
			e.printStackTrace();
		}		

		return res;
	}	

}
