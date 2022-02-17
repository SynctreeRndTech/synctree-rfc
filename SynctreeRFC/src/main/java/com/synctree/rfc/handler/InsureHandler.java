package com.synctree.rfc.handler;

import java.util.ArrayList;
import java.util.HashMap;

import com.synctree.dto.InsureDTO;
import com.synctree.rfc.framework.ApplicationContextHolder;
import com.synctree.rfc.framework.RfcDTO;
import com.synctree.service.impl.InsureServiceImpl;
import com.synctree.util.logging.SynctreeLogger;

public class InsureHandler{

	private static final SynctreeLogger logger = new SynctreeLogger(InsureHandler.class.getName());
	
	//RMI 방식으로 인해 Bean을 수동으로 조회
	static InsureServiceImpl insureService = ApplicationContextHolder.getContext().getBean(InsureServiceImpl.class);
	
	//보험료 조회 서비스 호출
	public static Object retrievePremium(RfcDTO dto) {
		
		logger.info("[RetrievePremium Method Call suceed]");

		InsureDTO insureDto = new InsureDTO();
		
		//인자값 셋팅
		insureDto.setStartDate(dto.req.get("startDate").toString());
		insureDto.setEndDate(dto.req.get("endDate").toString());
		insureDto.setBirthDate(dto.req.get("birthDate").toString());
		insureDto.setGender(dto.req.get("gender").toString());
		insureDto.setProdType(dto.req.get("prodType").toString());
		
		// 비즈니스 로직 호출
		ArrayList<HashMap<String, String>> retreiveResult = insureService.retrievePremium(insureDto);
		
		HashMap<String, Object> tempHash = new HashMap<String, Object>();
		tempHash.put("resultCode", "S001");
		tempHash.put("startDate", insureDto.getStartDate());
		tempHash.put("startTime", dto.req.get("startTime").toString());
		tempHash.put("endDate", insureDto.getEndDate());
		tempHash.put("endTime", dto.req.get("endTime").toString());
		tempHash.put("period", insureDto.getPeriod());
		tempHash.put("birthDate", insureDto.getBirthDate());
		tempHash.put("age", insureDto.getAge());
		tempHash.put("premium_value", retreiveResult);
		
		logger.info(insureDto.toString());
		logger.info("[RetrievePremium Method End]");

		return tempHash;
	}

	// 보험료 가입 서비스 호출
	public static Object takeOutInsurance(RfcDTO dto) {

		logger.info("[TakeOutInsurance Method Call succeed]");

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
		insureDto.setRegistNum(customerInfo.get("registNum").toString());
		insureDto.setPhoneNum(customerInfo.get("phoneNum").toString());
		insureDto.setEmail(customerInfo.get("email").toString());
		insureDto.setAge(Integer.parseInt(customerInfo.get("age").toString()));
		insureDto.setGender(customerInfo.get("gender").toString());
		
		logger.info(insureDto.toString());
		
		//데이터 정제
		
		// 비즈니스 로직 호출
		insureDto.setResult(insureService.takeOutInsurance(insureDto));
		
		logger.info(insureDto.toString());
		logger.info("[TakeOutInsurance Method End]");

		return insureDto.getResult();
	}

}
