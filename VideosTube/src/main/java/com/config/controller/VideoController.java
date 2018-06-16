package com.config.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.config.dao.ChannelDAO;
import com.config.dao.CommentDAO;
import com.config.dao.PlayListDAO;
import com.config.dao.UserDAO;
import com.config.dao.VideoDAO;
import com.config.model.Comment;
import com.config.model.Playlist;
import com.config.model.User;
import com.config.model.Video;

@Controller
public class VideoController {

	@RequestMapping(value = "/video", method = RequestMethod.GET)
	public String video(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException {

		String listName = req.getParameter("name");
		String username = req.getParameter("username");
		if (listName != null && username != null) {
			User user = UserDAO.getInstance().getUserByUsername(username);
			Playlist pl = user.getUserPlaylist(listName);
			String videoname = pl.getFirstVideo();
			if (videoname == null) {
				model.addAttribute("playlists", user.getPlayLists());
				model.addAttribute("message", "Can't Load Empty Playlist");
				return "myChannel";
			}
			int index = 0;
			Video video = VideoDAO.getInstance().getVideoByName(videoname);
			VideoDAO.getInstance().viewVideo(video);
			model.addAttribute("video", video);
			List<Video> videosInList = new ArrayList<>();
			for (String videoName : pl.getVideosFromPlaylist()) {
				videosInList.add(VideoDAO.getInstance().getVideoByName(videoName.trim()));
			}
			model.addAttribute("index", index);
			model.addAttribute("listOwner", username);
			model.addAttribute("playlist", videosInList);
			model.addAttribute("listname", listName);
		} else {

			String videoname = req.getParameter("name").trim();
			Video video = VideoDAO.getInstance().getVideoByName(videoname);
			VideoDAO.getInstance().viewVideo(video);
			model.addAttribute("video", video);
		}
		return "video";
	}

	@RequestMapping(value = "videoPoster/{videoName}", method = RequestMethod.GET)
	@ResponseBody
	public void getVideoPoster(@PathVariable("videoName") String videoName, HttpSession ses, HttpServletResponse resp,
			Model model) {
		String videoPoster = VideoDAO.getInstance().getVideoByName(videoName).getPoster();
		if (videoPoster == null) {
			System.out.println("NO PIC");
			return;
		}
		File file = new File(videoPoster);

		try {
			Files.copy(file.toPath(), resp.getOutputStream());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	@RequestMapping(value = "nextVideo", method = RequestMethod.GET)
	public String playlist(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException {

		String username = req.getParameter("username").trim();
		String videoName = req.getParameter("name").trim();
		String listName = req.getParameter("listName").trim();

		Set<Playlist> pl = PlayListDAO.getInstance().getUserPlayList(username);
		Playlist playList = null;
		for (Playlist play : pl) {
			if (play.getName().equals(listName)) {
				playList = play;
			}
		}
		if (playList != null) {
			int videoIndex = playList.getVideoIndex(videoName);
			String nextVideo = playList.getVideoByIndex(videoIndex + 1);
			if (nextVideo == null) {
				nextVideo = playList.getFirstVideo();
			}
			Video video = VideoDAO.getInstance().getVideoByName(nextVideo);
			model.addAttribute("video", video);
			model.addAttribute("comments", video.showVideoComments());
		}
		return "video";
	}

	@RequestMapping(value = "/videoNew", method = RequestMethod.GET)
	public String newVideo(HttpServletRequest req, Model model) {

		String videoname = req.getParameter("name").trim();
		Video video = VideoDAO.getInstance().getVideoByName(videoname);
		VideoDAO.getInstance().viewVideo(video);
		model.addAttribute("video", video);
		model.addAttribute("comments", video.showVideoComments());
		return "nextVideo";
	}

	@RequestMapping(value = "/video/{video}", method = RequestMethod.GET)
	@ResponseBody
	public void videoAddress(@PathVariable("video") String videoName, HttpServletResponse resp, Model model) {

		Video video = VideoDAO.getInstance().getVideoByName(videoName);
		if (video == null) {
			System.out.println("NO SUCH VIDEO ");
			return;
		}

		File file = new File(video.getAddress());

		try {
			Files.copy(file.toPath(), resp.getOutputStream());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	@RequestMapping(value = "comments", method = RequestMethod.GET)
	public String getComments(Model model, HttpServletRequest req) {

		String videoName = req.getParameter("videoName").trim();
		model.addAttribute("videoName", videoName);
		model.addAttribute("comments", VideoDAO.getInstance().getVideoByName(videoName).showVideoComments());
		return "comments";
	}

	@RequestMapping(value = "/isVideoNameAllowed", method = RequestMethod.GET)
	public @ResponseBody Boolean isVideoNameAllowed(@RequestParam("videoName") String videoname) {

		return VideoDAO.getInstance().getVideoByName(videoname) == null ? true : false;
	}

	@RequestMapping(value = "video/like", method = RequestMethod.GET)
	public @ResponseBody Video likeVideo(HttpSession ses, HttpServletRequest req) {
		String videoName = req.getParameter("videoName").trim();
		if (ses.getAttribute("user") == null) {
			Video video = VideoDAO.getInstance().getVideoByName(videoName);
			return video;
		}

		User user = (User) ses.getAttribute("user");
		Video video = VideoDAO.getInstance().likeVideo(videoName, user);

		if (video == null) {
			System.out.println("VIDEO IS NULL");
			return null;
		}

		return video;
	}

	@RequestMapping(value = "video/dislike", method = RequestMethod.GET)
	public @ResponseBody Video dislikeVideo(HttpSession ses, HttpServletRequest req) {
		String videoName = req.getParameter("videoName").trim();
		if (ses.getAttribute("user") == null) {
			Video video = VideoDAO.getInstance().getVideoByName(videoName);
			return video;
		}

		User user = (User) ses.getAttribute("user");
		Video video = VideoDAO.getInstance().dislikeVideo(videoName, user.getUsername());

		if (video == null) {
			System.out.println("VIDEO IS NULL");
			return null;
		}
		return video;
	}

	@RequestMapping(value = "writeComment", method = RequestMethod.POST)
	public String commentVideo(Model model, HttpSession ses, HttpServletRequest req) {
		String comment = req.getParameter("commentText").trim();
		String videoName = req.getParameter("videoName").trim();
		User user = (User) ses.getAttribute("user");
		Video video = VideoDAO.getInstance().getVideoByName(videoName);
		Comment com = CommentDAO.getInstance().saveComment(user, comment, video);
		System.out.println("COOOOOMENT " + com.getVideoName());

		if (com == null) {
			System.out.println("VIDEO IS NULL");
			return null;
		}
		model.addAttribute("videoName",video.getName());
		model.addAttribute("comments", video.getVideoComments());
		
		return "comments";
	}

	@RequestMapping(value = "comment/like", method = RequestMethod.POST)
	public @ResponseBody long commentLike(HttpSession ses, HttpServletRequest req) {
		Long commentId = new Long(req.getParameter("commentId"));
		String videoName = req.getParameter("videoName").trim();
		if (ses.getAttribute("user") == null) {
			return 0;
		}
		User user = (User) ses.getAttribute("user");

		int commentLikes = CommentDAO.getInstance().likeComment(user.getUsername(), videoName, commentId);

		return commentLikes;
	}

}
