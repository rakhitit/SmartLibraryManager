package com.smart.Controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.Dao.ContactRepository;
import com.smart.Dao.UserRepository;
import com.smart.Helper.Message;
import com.smart.entities.Contact;
import com.smart.entities.User;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserRepository userrepository;
	
	@Autowired
	ContactRepository contactRepository;
	
	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;
	
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String userName = principal.getName();
		 User user = userrepository.getUserByUserName(userName);
		  model.addAttribute("user",user); 
	}
	
  @GetMapping("/index")
  public String dashboard(Model model)
  {
	  model.addAttribute("title","Home"); 
	  return "normal/userdashboard";
  }
  
  //open add form controller
  @GetMapping("/addcontact")
  public String openAddContactForm(Model model, Principal principal)
  {
	   model.addAttribute("title","Add Contact"); 
	   model.addAttribute("contact",new Contact()); 
	   return "normal/add_contact_form";
  }
  
  @PostMapping("/process-contact")
  public String processContactForm(@ModelAttribute Contact contact,@RequestParam("profileImage") MultipartFile file,
		  Principal principal, HttpSession session)
  { 
	  try {
	  String name=principal.getName();
	  User user=userrepository.getUserByUserName(name);
	  
	  //processing and uploading file
	  if(file.isEmpty())
	  {
		contact.setImageUrl("contact.png");
		System.out.println("File is empty");  
	  }
	  
	  else { 
		  
		  contact.setImageUrl(file.getOriginalFilename());
		  File location =new ClassPathResource("/static/home/image/").getFile();
		  Path path=Paths.get(location.getAbsolutePath()+File.separator+file.getOriginalFilename());
		  
		  Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
		  System.out.println("Image is uploaded successfully");
	  }
	  user.getContacts().add(contact);
	  contact.setUser(user);
	  
	  this.userrepository.save(user);
	 // System.out.println("Contact data is "+contact );
	  System.out.println("Added to database");
	  session.setAttribute("message",new Message("Your contact is added!! Add more..","success"));
	  
	  } 
	  catch(Exception e)
	  {
		  System.out.println(e.getMessage());
		  session.setAttribute("message",new Message("Something went wrong !! try again..","danger"));
		  
	  }
	  return "normal/add_contact_form";
	  
  }
  
  @GetMapping("/view-contacts/{page}")
  public String viewcontacts(@PathVariable("page") int page, Model model, Principal principal)
  {
	  model.addAttribute("title","View contacts"); 
	  String name=principal.getName();
	  User user=this.userrepository.getUserByUserName(name);
	  //current page
	  //contact per page-5
	  PageRequest pageable =PageRequest.of(page, 3);
	  Page<Contact> contacts= this.contactRepository.findContactByUser(user.getId(),pageable);
	  model.addAttribute("contactdetail",contacts); 
	  model.addAttribute("currentPage",page);
	  model.addAttribute("totalPages",contacts.getTotalPages());
	  return "normal/view-contacts";
  }
  
  
  //showing particular contact detail
  
  @GetMapping("/{cId}/view-contacts")
  public String showcontact(@PathVariable("cId") int cid, Model model, Principal principal)
  {
	  System.out.println("CID "+cid);
	  String userName=principal.getName();
	  User userByUserName=this.userrepository.getUserByUserName(userName);
	  Optional<Contact> contact = this.contactRepository.findById(cid);
	 
	  Contact contactdetail=contact.get();
	  System.out.println("Contact new data 1 "+contactdetail);
	  if(userByUserName.getId()==contactdetail.getUser().getId())
	  {
		  System.out.println("Contact new data 2 "+contactdetail);
		  model.addAttribute("contactdetail","Contact page");  
		  model.addAttribute("contactdetail",contact.get());  
	  }
	  return "normal/contact_detail";  
  }
  
//open add form controller
  @GetMapping("/delete-contact/{cid}")
  public String deleteContactHandler(@PathVariable("cid") Integer cid,Model model, Principal principal, HttpSession session)
  {
	  System.out.println("Contact id is "+cid);
	  Contact contact = this.contactRepository.findById(cid).get();
	  User userByUserName=this.userrepository.getUserByUserName(principal.getName()); 
	  userByUserName.getContacts().remove(contact);
	  
	  if(userByUserName.getId()==contact.getUser().getId()) {
		  this.userrepository.save(userByUserName);
	 // contactdetail.setUser(null);
	 // contactRepository.delete(contact);
	  }
	  session.setAttribute("message", new Message("Contact deleted successfully..","success"));
	  return "redirect:/user/view-contacts/0";
  }
  
  
  @PostMapping("/update-contact/{cid}")
  public String updateContactHandler(@PathVariable("cid") int cid,Model model, Principal principal, 
		  HttpSession session)
  {  
	  model.addAttribute("title","Update Contact");
	  Contact contact = this.contactRepository.findById(cid).get();
	  model.addAttribute("contact",contact);
	  return "normal/update_form";  
  }
  
  //Update contact handler  
  @PostMapping("/process-update")
  public String updateHandler(@ModelAttribute Contact contact,@RequestParam("profileImage") MultipartFile file, 
		  Model m, HttpSession session, Principal p)
  {     
	  try {
		  
		  Contact oldcontactDetail=this.contactRepository.findById(contact.getCid()).get();
		  System.out.println("Contact Name "+contact.getName());
		  System.out.println("Contact ID "+contact.getCid());
		  
		  if(!file.isEmpty())
		  {
			//delete old photo
			  File deleteFile =new ClassPathResource("/static/home/image/").getFile();
			  File file1=new File(deleteFile,oldcontactDetail.getImageUrl());
			  file1.delete();
			  
			  
			  
			  File location =new ClassPathResource("/static/home/image/").getFile();
			  Path path=Paths.get(location.getAbsolutePath()+File.separator+file.getOriginalFilename());
			   
			  Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING); 
			  contact.setImageUrl(file.getOriginalFilename());
			  System.out.println("Image is uploaded successfully"); 
			  
		  }
		  
		  else {
			  contact.setImageUrl(oldcontactDetail.getImageUrl());
		  }
		  
		  User user=this.userrepository.getUserByUserName(p.getName());
		  contact.setUser(user);
		  this.contactRepository.save(contact);
		  session.setAttribute("message", new Message("Your contact is updated","success"));
	  }
	  catch(Exception e) {
		  e.printStackTrace();
	  }
	  
	  return "redirect:/user/"+contact.getCid()+"/view-contacts";  
  }
  
  //your profile
  @GetMapping("/profile")
  public String yourProfile(Model m) {
	 m.addAttribute("title","User Profile"); 
	return "normal/profile";  
  }
  
  //open settings handler
  @GetMapping("/settings")
  public String openSetting(Model model)
  {
	  model.addAttribute("title","settings");
	  return "normal/settings";
  }
  
  
  @PostMapping("/change-password")
  public String changepassword(@RequestParam("oldPassword") String oldPassword,
		  @RequestParam("newPassword") String newPassword,Principal p,
		  HttpSession session)
  {
	  System.out.println("OLD password "+oldPassword);
	  System.out.println("New password "+newPassword);
	  String userName=p.getName();
	  User u=this.userrepository.getUserByUserName(userName);
	  if(this.bcryptPasswordEncoder.matches(oldPassword, u.getPassword()))
	  {
		  u.setPassword(this.bcryptPasswordEncoder.encode(newPassword));
		  this.userrepository.save(u);
		  session.setAttribute("message", new Message("Your password is successfully changed","success"));
	  }
	  
	  else {
		  session.setAttribute("message", new Message("Validation with old password failed.Wrong old password!!","danger"));
		  return "redirect:/user/settings";
	  }
	 
	  return "redirect:/user/index";
  }
  

}
