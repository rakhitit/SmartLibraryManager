package com.smart.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smart.entities.User;

@Controller
public class SignInCotroller {
	
	@RequestMapping("/home/login")
	public String customlogin(Model model) {
		System.out.println("Inside signin");
		System.out.println("Inside signin controller");
		model.addAttribute("title", "Login Page");
		model.addAttribute("user", new User());
		return "login";
	}

}
