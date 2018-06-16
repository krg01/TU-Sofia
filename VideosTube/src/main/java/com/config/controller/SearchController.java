package com.config.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.config.dao.ChannelDAO;
import com.config.dao.CommentDAO;
import com.config.dao.PlayListDAO;
import com.config.dao.UserDAO;
import com.config.dao.VideoDAO;
import com.config.exception.CreateUserException;
import com.config.model.Comment;
import com.config.model.User;
import com.config.model.Video;

@Controller
public class SearchController {

	@RequestMapping(value = "/doSearch", method = RequestMethod.GET)
	public String searchBar(@RequestParam("search") String name, @RequestParam("type") String type, Model model) {

		if (type.equals("Video")) {
			model.addAttribute("videos", VideoDAO.getInstance().searchVideos(name));
			return "videos";
		} else if (type.equals("Channel")) {
			model.addAttribute("channels", UserDAO.getInstance().searchUsers(name));
			return "channels";
		} else {
			model.addAttribute("playlists", PlayListDAO.getInstance().searchPlaylists(name));
			return "playlists";
		}
	}

	@RequestMapping(value = "/searchChannel", method = RequestMethod.GET)
	public @ResponseBody List<User> searchChannel(
			@RequestParam("search") String name,
			@RequestParam("type") String type, 
			Model model) throws CreateUserException {

		List<User> res = UserDAO.getInstance().searchUsers(name);
		List<User> users = new ArrayList<>();
		for (User user : res) {
			User u = new User(user.getUsername(), user.getPassword(), user.getProfilePic(), user.getEmail());
			users.add(u);
		}

		return users;
	}

	
	
	@RequestMapping(value="/search", method = RequestMethod.GET)
	public String getSearchPage(){
		return "search";
	}
	
	
	
	
	@RequestMapping(value = "validateUsername", method = RequestMethod.GET)
	@ResponseBody
	public boolean isUserFree(HttpSession ses,HttpServletRequest req) {
		String username = req.getParameter("username");
		return UserDAO.getInstance().isUserExist(username);
	}
	
	@RequestMapping(value = "validateVideoName", method = RequestMethod.GET)
	@ResponseBody
	public boolean isVideoNameFree(HttpSession ses,HttpServletRequest req) {
		String videoName = req.getParameter("videoName");
		return VideoDAO.getInstance().isFreeVideoName(videoName);
	}
	
	
	@RequestMapping(value="/addPlaylist", method=RequestMethod.POST)
	public @ResponseBody String addVideoToPlaylist(HttpSession ses,HttpServletRequest req){
		String playlistName =req.getParameter("playlist");
		String videoName =req.getParameter("videoName");
		if(ses.getAttribute("user")==null){
			return "No user";
		}
		User user = (User)ses.getAttribute("user");

		return PlayListDAO.getInstance().addVideoInPlaylist(user.getUsername(), videoName, playlistName);
	}

	
	
	
	
}
