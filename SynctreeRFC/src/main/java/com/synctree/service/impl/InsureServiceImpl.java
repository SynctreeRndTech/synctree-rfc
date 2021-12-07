package com.synctree.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synctree.dto.InsureDTO;
import com.synctree.mapper.InsureMapper;
import com.synctree.service.InsureService;

@Service
public class InsureServiceImpl implements InsureService {

	private static final Logger logger = LogManager.getLogger(InsureServiceImpl.class);

	@Autowired
	InsureMapper insureMapper;

	// 보험료 조회
	@Override
	public ArrayList<HashMap<String, String>> retrievePremium(InsureDTO dto) {

		logger.info("[RetrievePremium Business Logic Begin]");

		// 여행기간일 계산
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");

			Date date1 = format.parse(dto.getStartDate());
			Date date2 = format.parse(dto.getEndDate());

			// Date로 변환된 두 날짜를 계산한 뒤 그 리턴값으로 long type 변수를 초기화
			// 연산결과 -950400000. long type 으로 return
			long calDate = date2.getTime() - date1.getTime();

			// Date.getTime() 은 해당날짜를 기준으로 1970년 00:00:00 부터 몇 초가 흘렀는지를 반환
			// 24*60*60*1000(각 시간값에 따른 차이점) 을 나눠주면 일수
			long travelPeriod = calDate / (24 * 60 * 60 * 1000);

			dto.setPeriod(Math.abs(travelPeriod));

		} catch (ParseException e) {
			e.printStackTrace();
		}

		// 나이 계산
		LocalDate today = LocalDate.now();
		LocalDate birthday = LocalDate.parse(dto.getBirthDate());

		Period p = Period.between(birthday, today);
		dto.setAge(p.getYears());
		
		ArrayList<HashMap<String, String>> result = insureMapper.retrievePremium(dto);

		logger.info("[RetrievePremium Business Logic End]");
		
		return result;
	}

	// 보험료 가입
	@Override
	public ArrayList<HashMap<String, String>> takeOutInsurance(InsureDTO dto) {
		
		logger.info("[TakeOutInsurance Business Logic Begin]");
		
		int takeOutResult = 0;
		ArrayList<HashMap<String, String>> result = new ArrayList<>();
		HashMap<String, String> res = new HashMap<String, String>();
		
		//기가입여부 조회
		ArrayList<String> isExistCustomer = insureMapper.retrieveCustId(dto);
		
		//쿠폰사용여부 임시셋팅(Y/N)
		if(dto.getCouponUseYn() == null) {
			
			dto.setCouponUseYn("N");
		}
		
		//계약상태코드 셋팅 (001:정상, 002:만료, ...)
		dto.setContractStatusCode("001"); //
		
		if(isExistCustomer.size() > 0) {
			dto.setCustID(isExistCustomer.get(0));
			
			//계약상품원장 등록
			takeOutResult = insureMapper.takeOutInsurance(dto);
			
		} else {
			//고객정보 등록
			int insertResult = insureMapper.insertNewCustomerInfo(dto);
			
			if(insertResult == 1) {
				ArrayList<String> getCustId = insureMapper.retrieveCustId(dto);
				
				if(getCustId.size() > 0 ) {
					dto.setCustID(getCustId.get(0));
					
					//계약상품원장 등록
					takeOutResult = insureMapper.takeOutInsurance(dto);
				}
			}
		}

		//가입상품명, 계약식별번호 조회
		if(takeOutResult > 0) {
			ArrayList<HashMap<String, Object>> prodInfo = insureMapper.retrieveProdName(dto);
			
			logger.debug(prodInfo.get(0).get("contract_id").toString());
			logger.debug(prodInfo.get(0).get("prod_name").toString());
			
			res.put("resultCode", "S001");
			res.put("contractID", prodInfo.get(0).get("contract_id").toString());
			res.put("productName", prodInfo.get(0).get("prod_name").toString());
			res.put("contractStatusCode", dto.getContractStatusCode());
			
			result.add(res);
			
		} else {
			res.put("resultCode", "E001");
			res.put("resultMessage", "가입에 실패하였습니다.");
			
			result.add(res);
		}
		
		logger.info("[TakeOutInsurance Business Logic End]");
		
		return result;
	}
}
