package com.synctree.framewok.rfc;

import java.util.HashMap;

public class RemoteFunctionDTO {

		public String str1;
		public String str2;
		public String startDate;
		public String endDate;
		
		//보험료 조회
		public HashMap<String, Object> req;
		
		
		@Override
		public String toString() {
			return "RemoteFunctionDTO [param1=" + str1 + ", param2=" + str2 + "]";
		}
		
		
}
