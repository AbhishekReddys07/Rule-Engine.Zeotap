package com.RuleEngine.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class WebController {
	
	@GetMapping("/create")
	public String getMethodcreate() {
		return "create";
	}
	
	@GetMapping("/combine")
	public String getMethodcombine() {
	return "combine"; 
	}
	@GetMapping("/evaluate")
	public String callhtml() {
		return "evaluate";
	}
	
	@GetMapping("/")
	public String all() {
		return "all";
		
	}

}
