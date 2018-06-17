package com.config.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.config.dao.ChannelDAO;
import com.config.dao.UserDAO;

import com.config.model.User;

@Controller
public class UsersController {

	
	@RequestMapping(value="subscribe", method=RequestMethod.POST)
	public @ResponseBody String subscribe(HttpSession ses, HttpServletRequest req){
		
		String channelName = req.getParameter("channel");
		
		if(ses.getAttribute("user") == null){
			return "No user";
		}
		User user = (User)ses.getAttribute("user");
	
		String result = ChannelDAO.getInstance().subscribeChannel(user.getUsername(), channelName);
		
		return result;
	}

	
	@RequestMapping(value = "/myChannel/{username}", method = RequestMethod.GET)
	@ResponseBody
	public void getProfilePic(@PathVariable("username") String username, HttpSession ses, HttpServletResponse resp,
			Model model) {

		String userPic = UserDAO.getInstance().getUserByUsername(username).getProfilePic();
		if (userPic == null) {
			System.out.println("NO PIC");
			return;
		}
		File file = new File("C:/DEV/profilePic/" + userPic);

		try {
			Files.copy(file.toPath(), resp.getOutputStream());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
