package com.config.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.config.dao.UserDAO;
import com.config.dao.VideoDAO;
import com.config.exception.CreateUserException;
import com.config.model.User;
import com.config.model.Video;

@Controller
public class RegisterController {

	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String prepareNewAddress(Model model){
		model.addAttribute("user", new User());
		return "register";
	}

	@RequestMapping(value="/register", method=RequestMethod.POST)
	public String addReadyAddress(@ModelAttribute("user") User user, Model model){
		try {
			UserDAO.getInstance().registerUser(user.getUsername(), user.getPassword(), user.getEmail());
			
		} catch (CreateUserException e) {
			model.addAttribute("msg", e.getMessage());
			return "register";
		}
		List<Video> videos = VideoDAO.getInstance().getAllVideos();
		model.addAttribute("videos", videos);
		return "home";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(Model model, HttpSession ses, @RequestParam("username") String username,
			@RequestParam("password") String password) {

		if (UserDAO.getInstance().login(username, password)) {
			System.out.println("User exist");
			model.addAttribute("videos", VideoDAO.getInstance().getAllVideos());
			ses.setAttribute("user", UserDAO.getInstance().getUserByUsername(username));
			return "home";
		}

		System.out.println("User do not exist");
		model.addAttribute("msg", "Invalid username or password !");
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage(HttpSession ses) {
		ses.removeAttribute("user");

		return "login";
	}
	
	@RequestMapping(value="/isUsernameAllowed", method = RequestMethod.GET)
	public @ResponseBody Boolean isUsernameAllowed(@RequestParam("username") String username){
		User user = UserDAO.getInstance().getUserByUsername(username);
		Boolean alloed = user == null ? true : false; 
		return user == null;
	}
}
