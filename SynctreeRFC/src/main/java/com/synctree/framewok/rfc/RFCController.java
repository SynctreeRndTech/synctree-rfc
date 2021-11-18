package com.synctree.framewok.rfc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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

		String responseData = "";
		HttpURLConnection conn = null;
		OutputStream os = null;
		JSONObject paramObj = new JSONObject();
		JSONObject result = new JSONObject();
		RemoteFunctionDTO rfcDto = new RemoteFunctionDTO();
		ArrayList<String> paramArr = new ArrayList<>();

		/* 보안 프로토콜 */
		try {

			logger.info("[className]: " + className);
			logger.info("[functionName]: " + functionName);
			logger.info("[X-Synctree-Secure-Key]: " + secKey);
			logger.info("[X-Synctree-Verification-Code]: " + verifyCode);

			// URL url = new URL("https://seoul.synctreengine.com/secure/getCommand"); //실서버
			URL url = new URL(protocolUrl); // 임시셋팅

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
					Set<?> key = paramObj.keySet();
					Iterator<?> iter = key.iterator();

					while (iter.hasNext()) {
						String paramName = iter.next().toString();
						paramArr.add(paramName);
					}

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

		/* RMI */
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

			Class<?> dtoCls = Class.forName("com.synctree.framewok.rfc.RemoteFunctionDTO");

			for (int i = 0; i < paramArr.size(); i++) {
				String paramName = paramArr.get(i);
				Field field = dtoCls.getField(paramName);
				field.set(rfcDto, paramObj.get(paramName));
			}

			Method m = cls.getDeclaredMethod(functionName, dtoCls);
			result = (JSONObject) m.invoke(rfcObj, rfcDto);

		} catch (ClassNotFoundException e) {
			logger.error("ClassNotFoundException from Class.forName()");
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			logger.error("NoSuchMethodException from getDeclaredMethod()");
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			logger.error("InvocationTargetExceptionn from invoke()");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			logger.error("IllegalAccessException from invoke()");
			e.printStackTrace();
		}

		return result;
	}

	/*
	 * 1st Ver. param=JSONObject
	 * 
	 * @PostMapping("/synctree/rfc") public JSONObject
	 * remoteFunctionCallUtil(@RequestHeader(value="functionName") String
	 * functionName, @RequestHeader(value="className") String
	 * className, @RequestHeader(value="protocolUrl") String protocolUrl,
	 * 
	 * @RequestHeader(value="X-Synctree-Secure-Key") String
	 * secKey, @RequestHeader(value="X-Synctree-Verification-Code") String
	 * verifyCode) {
	 * 
	 * String responseData = ""; HttpURLConnection conn = null; OutputStream os =
	 * null; JSONObject paramObj = new JSONObject(); JSONObject result = new
	 * JSONObject();
	 * 
	 * 보안 프로토콜 try {
	 * 
	 * logger.info("[className]: " + className); logger.info("[functionName]: " +
	 * functionName); logger.info("[X-Synctree-Secure-Key]: " + secKey);
	 * logger.info("[X-Synctree-Verification-Code]: " + verifyCode);
	 * 
	 * //URL url = new URL("https://seoul.synctreengine.com/secure/getCommand");
	 * //실서버 URL url = new URL(protocolUrl); //임시셋팅
	 * 
	 * conn = (HttpURLConnection) url.openConnection();
	 * 
	 * conn.setRequestMethod("POST"); conn.setDoOutput(true);
	 * conn.setConnectTimeout(10000); conn.setRequestProperty("Content-Type",
	 * "application/json"); conn.setRequestProperty("Accept", "application/json");
	 * conn.setRequestProperty("X-Synctree-Service", "managed.redis");
	 * conn.setRequestProperty("X-Synctree-Service-Method", "loader");
	 * conn.setRequestProperty("X-Synctree-Secure-Key", secKey);
	 * conn.setDoOutput(true); conn.setDoInput(true);
	 * 
	 * try { os = conn.getOutputStream(); byte[] verifyKey =
	 * secKey.getBytes(StandardCharsets.UTF_8); os.write(verifyKey); }
	 * catch(Exception e) { e.printStackTrace(); } finally { os.flush(); os.close();
	 * }
	 * 
	 * int statusCode = conn.getResponseCode(); String responseMsg =
	 * String.valueOf(conn.getResponseMessage());
	 * logger.info("[Secure Protocol Response] Status Code: "+ statusCode +
	 * ", Response Message: " + responseMsg);
	 * 
	 * if (statusCode == 200) { BufferedReader br = new BufferedReader(new
	 * InputStreamReader(conn.getInputStream(), "UTF-8")); StringBuffer sb = new
	 * StringBuffer(); while ((responseData = br.readLine()) != null) {
	 * sb.append(responseData); } logger.info("[params] " + sb);
	 * 
	 * if (br != null) { br.close(); }
	 * 
	 * JSONParser parser = new JSONParser(); try { paramObj = (JSONObject)
	 * parser.parse(sb.toString()); } catch (ParseException e) {
	 * e.printStackTrace(); }
	 * 
	 * } else { logger.error("[ErrorCode] " + statusCode + ", [ErrorMsg] " +
	 * responseMsg); }
	 * 
	 * } catch (IOException e) { e.printStackTrace(); } finally { if(conn!= null) {
	 * conn.disconnect(); } }
	 * 
	 * RMI try { Class<?> cls = Class.forName(className); Constructor<?> constructor
	 * = cls.getConstructor(new Class[]{}); Object rfcObj = null; try { rfcObj =
	 * constructor.newInstance(new Object[]{}); } catch (InstantiationException e) {
	 * e.printStackTrace(); } catch (IllegalArgumentException e) {
	 * e.printStackTrace(); }
	 * 
	 * Method m = cls.getDeclaredMethod(functionName, JSONObject.class); result =
	 * (JSONObject) m.invoke(rfcObj, paramObj);
	 * 
	 * } catch (ClassNotFoundException e) {
	 * logger.error("ClassNotFoundException from Class.forName()"); } catch
	 * (NoSuchMethodException e) {
	 * logger.error("NoSuchMethodException from getDeclaredMethod()"); } catch
	 * (InvocationTargetException e) {
	 * logger.error("InvocationTargetExceptionn from invoke()"); } catch
	 * (IllegalAccessException e) {
	 * logger.error("IllegalAccessException from invoke()"); }
	 * 
	 * return result; }
	 */

//	Temp Ver.
//	/*param=JSONObject*/
//	@PostMapping("/synctree/rfc-test")
//	public JSONObject remoteFunctionCallTest(@RequestHeader(value="functionName") String functionName, @RequestHeader(value="className") String className, @RequestHeader(value="protocolUrl") String protocolUrl,
//			                                 @RequestHeader(value="X-Synctree-Secure-Key") String secKey, @RequestHeader(value="X-Synctree-Verification-Code") String verifyCode, @RequestHeader(value="paramType") String[] paramType) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
//			
//		String responseData = "";
//	    HttpURLConnection conn = null;
//		OutputStream os = null;
//		JSONObject paramObj = new JSONObject();
//		JSONObject result = new JSONObject();
//		RemoteFunctionDto rfcDto = new RemoteFunctionDto();
//		
//		/*보안 프로토콜*/
//		try {
//			
//			logger.info("[className]: " + className);
//			logger.info("[functionName]: " + functionName);
//			logger.info("[X-Synctree-Secure-Key]: " + secKey);
//			logger.info("[X-Synctree-Verification-Code]: " + verifyCode);
//			
//			//URL url = new URL("https://seoul.synctreengine.com/secure/getCommand"); //실서버
//			URL url = new URL(protocolUrl); //실서버
//
//	        conn = (HttpURLConnection) url.openConnection();
//	            
//	        conn.setRequestMethod("POST");
//	        conn.setDoOutput(true);
//	        conn.setConnectTimeout(10000);
//	        conn.setRequestProperty("Content-Type", "application/json");
//	        conn.setRequestProperty("Accept", "application/json");
//	        conn.setRequestProperty("X-Synctree-Service", "managed.redis");
//	        conn.setRequestProperty("X-Synctree-Service-Method", "loader");
//	        conn.setRequestProperty("X-Synctree-Secure-Key", secKey);
//	        conn.setDoOutput(true);
//	        conn.setDoInput(true);
//	
//	        try {
//	            os = conn.getOutputStream();
//	            byte[] verifyKey = secKey.getBytes(StandardCharsets.UTF_8);
//	            os.write(verifyKey);
//	        } catch(Exception e) {
//					e.printStackTrace();
//		    } finally {
//				os.flush();
//	            os.close();
//			}
//	        
//			int statusCode = conn.getResponseCode();
//			String responseMsg = String.valueOf(conn.getResponseMessage());
//			logger.info("[Secure Protocol Response] Status Code: "+ statusCode + ", Response Message: " + responseMsg);
//				
//			if (statusCode == 200) {
//				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));	
//				StringBuffer sb =  new StringBuffer();	       
//				while ((responseData = br.readLine()) != null) {
//					sb.append(responseData);
//				}
//				logger.info("[params] " + sb); 
//
//				if (br != null) {
//					br.close();
//				}
//				
//				JSONParser parser = new JSONParser();
//				try {
//					paramObj = (JSONObject) parser.parse(sb.toString());
//				} catch (ParseException e) { 
//					e.printStackTrace();
//				}
//				
//			} else {
//				logger.error("[ErrorCode] " + statusCode + ", [ErrorMsg] " + responseMsg);
//			}
//				
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			if(conn!= null) {
//				conn.disconnect();
//			}
//		}
//	
//		/*RMI*/
//		try {
//			  Class<?> cls = Class.forName(className);			  
//			  Constructor<?> constructor = cls.getConstructor(new Class[]{});
//			  Object rfcObj = null;
//				try {
//					rfcObj = constructor.newInstance(new Object[]{});
//				} catch (InstantiationException e) {
//					e.printStackTrace();
//				} catch (IllegalArgumentException e) {
//					e.printStackTrace();
//				}
//							
//				
//				//수정부분	
//				int arrayLength = paramObj.size();
//				Class<?>[] classList = new Class<?>[arrayLength];
//				
//				try {
//					Class<?> dtoCls = Class.forName("com.synctree.framewok.rfc.RemoteFunctionDTO");
//					for (int i=0; i<paramObj.size(); i++) {
//						Field field = dtoCls.getField("param"+i);
//						String tmp = "param" + i;
//						field.set(rfcDto, paramObj.get(tmp));
//					
//						logger.info(rfcDto.toString());
//			    	    logger.info("여기 :: " + field.getType());
//			    	    
//						classList[i] = field.getType();
//					}
//				} catch (ClassNotFoundException e) {
//					e.printStackTrace();
//				}
//		        
//			Method m = cls.getDeclaredMethod(functionName, rfcDto.getClass());
//				logger.info("method 찍나 보자: " + m.toString());
//			result = (JSONObject) m.invoke(rfcObj, rfcDto);
//			  
//		} catch (ClassNotFoundException e) {
//			logger.error("ClassNotFoundException from Class.forName()");
//		} catch (NoSuchMethodException e) {
//			logger.error("NoSuchMethodException from getDeclaredMethod()");
//		} catch (InvocationTargetException e) {
//			logger.error("InvocationTargetExceptionn from invoke()"); 
//		} catch (IllegalAccessException e) {
//			logger.error("IllegalAccessException from invoke()");
//		}
//	
//		return result;
//	}    

}
