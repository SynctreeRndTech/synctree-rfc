package com.synctree.rfc;

import org.springframework.web.bind.annotation.RequestMapping;

public class MainController {
	 @RequestMapping(value="/")
	    public String main() {
	        return "index";
	    }
}
