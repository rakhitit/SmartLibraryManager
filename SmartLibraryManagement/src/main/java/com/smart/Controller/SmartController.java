package com.smart.Controller;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smart.Dao.UserRepository;
import com.smart.Helper.Message;
import com.smart.emailservice.EmailService;
import com.smart.entities.User;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class SmartController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private EmailService emailService;

	Random random = new Random(1000);

	@GetMapping("/test")
	@ResponseBody
	public User test() {
		User u = new User();
		u.setAbout("sadkfewfe");
		u.setEmail("rakhi.raikwar27@gmail.com");
		u.setName("Rakhi");
		userRepository.save(u);
		return u;
	}

	// @PreAuthorize("hasAuthority('ROLE_USER')")
	@RequestMapping("/home")
	public String home(Model model) {
		model.addAttribute("title", "Home - Smart Library Manager");
		return "home";
	}

	@RequestMapping("/home/about")
	public String about(Model model) {
		model.addAttribute("title", "About - Smart Library Manager");
		return "about";
	}

	// @PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@RequestMapping("/home/signup")
	public String signup(Model model) {
		model.addAttribute("title", "Register - Smart Library Manager");
		model.addAttribute("user", new User());
		return "signup";
	}

	@PostMapping("/home/do_register")
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,
			HttpSession session) {
		try {
			if (!agreement) {
				System.out.println("You have not agreed the terms and condition !!");
				throw new Exception("You have not agreed the terms and condition !!");
			}

			if (result.hasErrors()) {
				model.addAttribute("user", user);
				return "signup";
			}

			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			// model.addAttribute("user",user);
			System.out.println("User " + user);
			userRepository.save(user);
			model.addAttribute("user", new User());
			session.setAttribute("message", new Message("Successfully Registered", "Success"));
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went wrong !!" + e.getMessage(), "alert-danger"));
			return "signup";
		}
		return "signup";
	}

	/*
	 * @RequestMapping("/home/signin") public String customlogin(Model model) {
	 * model.addAttribute("title", "Login Page"); model.addAttribute("user", new
	 * User()); return "login"; }
	 */

	@RequestMapping("/home/forgot")
	public String openEmailForm() {
		return "forgot_email_form";
	}

	@PostMapping("/home/send_otp")
	public String sendOtp(@RequestParam("email") String email, HttpSession session) {
		System.out.println("Email is " + email);
		boolean flag = false;
		int otp = 0;

		try {
			SecureRandom number = SecureRandom.getInstanceStrong();
			otp = number.nextInt(999999);
			flag = emailService.sendEmail(otp,email);

		} catch (NoSuchAlgorithmException e) {
			session.setAttribute("message", "some issues with OTP generation");
			return "forgot_email_form";
		}
		if (flag) {
			session.setAttribute("myotp", otp);
			session.setAttribute("email",email);
			System.out.println("Email sent successfully");
			return "verify_otp";
		} else {
			System.out.println("Email not sent successfully");
			session.setAttribute("message", "Check senders mail information is correct!!");
			return "forgot_email_form";
		}

	}
	
	@PostMapping("/home/verify_otp")
	public String verifyOtp(@RequestParam("otp") int otp, HttpSession session) {
		int myotp= (int)session.getAttribute("myotp");
		System.out.println("myotp"+myotp);
		System.out.println("otp"+otp);
		String email=(String)session.getAttribute("email");
		if(otp==myotp)
		{
			User user=userRepository.getUserByUserName(email);
			if(user==null)
			{
				session.setAttribute("message","User is not valild!!. Please register the details");
				return "forgot_email_form";
			}
						
			return "password_change_form";
		}
		else {
			session.setAttribute("message","You have entered wrong OTP !!");
			return "verify_otp";
		}
		
	
	}
	
	
	@PostMapping("/home/change_password")
	public String changePassword(@RequestParam("newpassword") String newpassword, HttpSession session) {
		String email = (String) session.getAttribute("email");
		User user=this.userRepository.getUserByUserName(email);
		user.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
		this.userRepository.save(user);
		session.setAttribute("message","Password has changed successfully");
		return "redirect:/home/signin?change=password change successfully";

	}
	

}
