package com.synctree.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InsureDTO {
	
	//보험료 조회
	private String startDate;
	private String endDate;
    private String birthDate;
    private String gender;
    private String prodType;
    private long period;
    private int age;
    
    //보험 가입
    private String totalPrice;
    private String payMethod;
    private String couponUseYn;
    private String contractStatusCode;
    private String custID;
    private String custNameKo;
    private String custNameEn;
    private String registNum;
    private String phoneNum;
    private String email;
    
    private ArrayList<HashMap<String, String>> result;
    
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getProdType() {
		return prodType;
	}
	public void setProdType(String prodType) {
		this.prodType = prodType;
	}
	public long getPeriod() {
		return period;
	}
	public void setPeriod(long period) {
		this.period = period;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	public String getCouponUseYn() {
		return couponUseYn;
	}
	public void setCouponUseYn(String couponUseYn) {
		this.couponUseYn = couponUseYn;
	}
	public String getContractStatusCode() {
		return contractStatusCode;
	}
	public void setContractStatusCode(String contractStatusCode) {
		this.contractStatusCode = contractStatusCode;
	}
	public String getCustID() {
		return custID;
	}
	public void setCustID(String custID) {
		this.custID = custID;
	}
	public String getCustNameKo() {
		return custNameKo;
	}
	public void setCustNameKo(String custNameKo) {
		this.custNameKo = custNameKo;
	}
	public String getCustNameEn() {
		return custNameEn;
	}
	public void setCustNameEn(String custNameEn) {
		this.custNameEn = custNameEn;
	}
	public String getRegistNum() {
		return registNum;
	}
	public void setRegistNum(String registNum) {
		this.registNum = registNum;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public ArrayList<HashMap<String, String>> getResult() {
		return result;
	}
	public void setResult(ArrayList<HashMap<String, String>> result) {
		this.result = result;
	}
	
	@Override
	public String toString() {
		return "InsureDTO [startDate=" + startDate + ", endDate=" + endDate + ", birthDate=" + birthDate + ", gender="
				+ gender + ", prodType=" + prodType + ", period=" + period + ", age=" + age + ", totalPrice="
				+ totalPrice + ", payMethod=" + payMethod + ", couponUseYn=" + couponUseYn + ", contractStatusCode="
				+ contractStatusCode + ", custID=" + custID + ", custNameKo=" + custNameKo + ", custNameEn="
				+ custNameEn + ", registNum=" + registNum + ", phoneNum=" + phoneNum + ", email=" + email + ", result="
				+ result + "]";
	}
}
