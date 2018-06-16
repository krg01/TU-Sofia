package com.config.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.config.dao.CommentDAO;
import com.config.dao.UserDAO;
import com.config.dao.VideoDAO;
import com.config.model.Channel;
import com.config.model.User;
import com.config.model.Video;

@Controller
public class PageController {

	@RequestMapping("/index.jsp")
	public String hello() {
	    return "home";
	}
	
	@RequestMapping(value="home", method=RequestMethod.GET)
	public String getHome(Model model){
		List<Video> videos = VideoDAO.getInstance().getAllVideos();
		model.addAttribute("videos", videos);
		return "home";
	}
	

	@RequestMapping(value="/*", method = RequestMethod.GET)
	public String getIndexPage(Model model){
		List<Video> videos = VideoDAO.getInstance().getAllVideos();
		model.addAttribute("videos", videos);
		return "home";
	}

	
	
}
