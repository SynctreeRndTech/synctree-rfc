package com.synctree.dto;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

public class TestDTO {
	private String strTest;
	private HashMap<String, Object> hashTest;
	private ArrayList<Object> arrTest;
	

	public String getStrTest() {
		return strTest;
	}

	public void setStrTest(String strTest) {
		this.strTest = strTest;
	}

	public HashMap<String, Object> getHashTest() {
		return hashTest;
	}

	public void setHashTest(HashMap<String, Object> hashTest) {
		this.hashTest = hashTest;
	}

	public ArrayList<Object> getArrTest() {
		return arrTest;
	}

	public void setArrTest(ArrayList<Object> arrTest) {
		this.arrTest = arrTest;
	}

	@Override
	public String toString() {
		return "TestVO [strTest=" + strTest + ", hashTest=" + hashTest + ", arrTest=" + arrTest + "]";
	}

}