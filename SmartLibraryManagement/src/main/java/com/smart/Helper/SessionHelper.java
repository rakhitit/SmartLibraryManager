package com.smart.Helper;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;

@Component
public class SessionHelper {
	
	public void removeMessageFromSession() {
		try {
			RequestAttributes reqAtt = RequestContextHolder.getRequestAttributes();
			if (RequestContextHolder.getRequestAttributes() != null) {
			    HttpSession session = ((ServletRequestAttributes) reqAtt).getRequest().getSession();
			    session.removeAttribute("message");
			}
		}
		catch(Exception e)
		{
			
		}
	}

}
