package com.synctree.mapper;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.synctree.dto.InsureDTO;

@Mapper
@Repository
public interface InsureMapper {
	
	static final Logger logger = LogManager.getLogger(InsureMapper.class);

	public ArrayList<HashMap<String, String>> retrievePremium(InsureDTO dto);
	public ArrayList<String> retrieveCustId(InsureDTO dto);
	public Integer insertNewCustomerInfo(InsureDTO dto);
	public Integer takeOutInsurance(InsureDTO dto);
	public ArrayList<HashMap<String, Object>> retrieveProdName(InsureDTO dto);

}
