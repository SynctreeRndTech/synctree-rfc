package com.synctree.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.synctree.dto.InsureDTO;

public interface InsureService {
	
	public ArrayList<HashMap<String, String>> retrievePremium(InsureDTO dto);

	public ArrayList<HashMap<String, String>> takeOutInsurance(InsureDTO dto) throws SQLException;
}
