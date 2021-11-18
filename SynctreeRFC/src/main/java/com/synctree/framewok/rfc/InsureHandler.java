package com.synctree.framewok.rfc;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import com.synctree.dto.InsureDTO;
import com.synctree.service.impl.InsureServiceImpl;

@Service
public class InsureHandler{

	private static final Logger logger = LogManager.getLogger(RFCController.class);
	
	//RMI 방식으로 인해 Bean을 수동으로 조회
	static InsureServiceImpl insureService = ApplicationContextHolder.getContext().getBean(InsureServiceImpl.class);
	
	//보험료 조회 서비스 호출
	public JSONObject retrievePremium(RemoteFunctionDTO dto) {
		
		logger.info("[RetrievePremium Method Call suceed]");

		InsureDTO insureDto = new InsureDTO();
		
		//인자값 셋팅
		insureDto.setStartDate(dto.req.get("startDate").toString());
		insureDto.setEndDate(dto.req.get("endDate").toString());
		insureDto.setBirthDate(dto.req.get("birthDate").toString());
		insureDto.setGender(dto.req.get("gender").toString());
		insureDto.setProdType(dto.req.get("prodType").toString());
		
		// 비즈니스 로직 호출
		insureDto.setResult(insureService.retrievePremium(insureDto));

		//응답 셋팅
		JSONObject result = new JSONObject();
		result.put("res", insureDto.getResult());
		
		logger.info(insureDto.toString());
		logger.info("[RetrievePremium Method End]");

		return result;
	}

	// 보험료 가입 서비스 호출
	public static JSONObject takeOutInsurance(RemoteFunctionDTO dto) {

		logger.info("[TakeOutInsurance Method Call suceed]");

		//인자값 셋팅
		InsureDTO insureDto = new InsureDTO();
		insureDto.setStartDate(dto.req.get("startDate").toString());
		insureDto.setEndDate(dto.req.get("endDate").toString());
		insureDto.setPeriod((long)dto.req.get("period"));
		insureDto.setProdType(dto.req.get("prodType").toString());
		insureDto.setTotalPrice(dto.req.get("totalPrice").toString());
		insureDto.setPayMethod(dto.req.get("payMethod").toString());
	
		//고객정보 추출
		HashMap<String, Object> customerInfo = (HashMap<String, Object>) dto.req.get("customerInfo");
		HashMap<String, Object> custName = (HashMap<String, Object>) customerInfo.get("custName");
		insureDto.setCustNameKo(custName.get("ko").toString());
		insureDto.setCustNameEn(custName.get("en").toString());
		
		ArrayList registNum = (ArrayList) customerInfo.get("registNum");
		String rgNum = registNum.get(0).toString();
		rgNum += registNum.get(1).toString();
		insureDto.setRegistNum(rgNum);
		
		insureDto.setPhoneNum(customerInfo.get("phoneNum").toString());
		insureDto.setEmail(customerInfo.get("email").toString());
		insureDto.setAge(Integer.parseInt(customerInfo.get("age").toString()));
		insureDto.setGender(customerInfo.get("gender").toString());
		
		logger.info(insureDto.toString());
		
		//데이터 정제
		
		// 비즈니스 로직 호출
		insureDto.setResult(insureService.takeOutInsurance(insureDto));
		
		// 응답값 셋팅

		JSONObject result = new JSONObject();
		result.put("res", insureDto.getResult());
		
		logger.info(insureDto.toString());
		logger.info("[TakeOutInsurance Method End]");

		return result;
	}

}
