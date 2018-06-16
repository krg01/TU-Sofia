package com.config.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import org.mockito.internal.util.collections.Iterables;

import com.config.model.Channel;
import com.config.model.Comment;
import com.config.model.User;
import com.config.model.Video;
import com.config.util.SendEmail;

public class VideoDAO {

	private static HashMap<String, Video> videos = new HashMap<String, Video>();
	private static VideoDAO instance;

	private Connection connection;

	private VideoDAO() {
	}

	public boolean uploadVideo(
			String name, 
			String category, 
			String description, 
			String uploader, 
			String address, String posterLocation) {
		
		LocalDate localDate = LocalDate.now();
		Date date = Date.valueOf(localDate);
		
		if (videos.containsKey(name)) {
			return false;
		} else if (addVideoInCollection(name, category, description, uploader, address, localDate,posterLocation)) {
			return saveVideoInDB(name, category, description, uploader, address, date,posterLocation);
		}
		return false;
	}

	public synchronized static VideoDAO getInstance(){
		if (instance == null) {
			instance = new VideoDAO();
			instance.loadVideos();
		}
		return instance;
	}

	public List<Video> getUserVideos(String userName){
		List<Video> userVideos = new ArrayList<Video>();
		for(Video v : videos.values()){
			if(v.getUploader().equals(userName)){
				userVideos.add(v);
			}
		}
		return userVideos;
	}
	
	public Video getVideoByName(String videoName){
		Video video = null;

		if(videos.containsKey(videoName)){
			video = videos.get(videoName);
		}
		return video;
	}

	public List<Video> getAllVideos(){
		Set<Video> vids = new TreeSet<>((v1,v2)->{
			
			if(v1.getView()==v2.getView())
			{
				if(v1.getLikes()==v2.getLikes()){
					return v2.getName().compareTo(v1.getName());
				}
				return v2.getLikes()-v1.getLikes();
			}
			return v2.getView()-v1.getView();
		
		});

		List<Video> results = new ArrayList<>();
		vids.addAll(videos.values());
		int max = 6;
		for (Iterator iterator = vids.iterator(); iterator.hasNext();) {
			Video video = (Video) iterator.next();
			results.add(video);
			if(--max <= 0){
				break;
			}
		}
		return  Collections.unmodifiableList(results);
	}
	
	private void loadVideos() {
		try {

			this.connection = DBManager.getInstance().getConnection();
			Statement st = connection.createStatement();
			ResultSet resultSet = st
					.executeQuery(
							"SELECT name, views, category, description, video_address, user_name, date_upload,poster FROM videos;");
			while (resultSet.next()) {
				Date input = resultSet.getDate("date_upload");
				
				LocalDate date = input.toLocalDate();
				Video video = new Video(resultSet.getString("name"), resultSet.getString("user_name"),
						resultSet.getString("category"), resultSet.getInt("views"), date,
						resultSet.getString("description"), resultSet.getString("video_address"),resultSet.getString("poster"));
				loadVideoLikes(video);
				loadVideoDislikes(video);
				loadVideoComments(video);
				videos.put(video.getName(), video);

			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	private void loadVideoComments(Video video) {
		
		List<Comment> videoComment = CommentDAO.getInstance().getCommentsForVideo(video.getName());
		for(Comment com : videoComment){
			video.addComment(com);
		}
		
	}

	private void loadVideoDislikes(Video video) {
		this.connection = DBManager.getInstance().getConnection();
		PreparedStatement st;
		try {
			String sql = "SELECT user_name FROM user_dislike_videos where video_name = ?;";
			st = connection.prepareStatement(sql);
			st.setString(1, video.getName());
			ResultSet rs = st
					.executeQuery();
			while(rs.next()){
				String userName = rs.getString("user_name");
				video.dislikeVideo(userName);
			}
		} catch (SQLException e) {
			System.out.println("Load video dislikes method in VIDEODAO " +e.getMessage());
		}
		
	}

	private void loadVideoLikes(Video video) {

		this.connection = DBManager.getInstance().getConnection();
		PreparedStatement st;
		try {
			String sql = "SELECT user_name FROM user_liked_videos where video_name = ?;";
			st = connection.prepareStatement(sql);
			st.setString(1, video.getName());
			ResultSet rs = st
					.executeQuery();
			while(rs.next()){
				String userName = rs.getString("user_name");
				video.likeVideo(userName);
			}
		} catch (SQLException e) {
			System.out.println("LOAD VIDEOS LIKES INT VIDEO DAO " +e.getMessage());
		}		
	}

	private boolean saveVideoInDB(
			String name, 
			String category, 
			String description, 
			String uploader, 
			String address,
			Date date, String posterLocation) {
		
		try {
			this.connection = DBManager.getInstance().getConnection();

			String sql = "insert into videos(name, views, "
					+ "date_upload, description, video_address, category, "
					+ "channel_name, user_name,poster) "
					+ "values(?,?,?,?,?,?,?,?,?);";
			PreparedStatement stm = connection.prepareStatement(sql);
			stm.setString(1, name);
			stm.setInt(2, 0);
			stm.setDate(3, date);
			stm.setString(4, description);
			stm.setString(5, address);
			stm.setString(6, category);
			stm.setString(7, uploader);
			stm.setString(8, uploader);
			stm.setString(9, posterLocation);

			stm.executeUpdate();
			sendEmail(uploader,name);
			return true;

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		} 
		
	}

	private void sendEmail(String uploader, String name) {
		User user = UserDAO.getInstance().getUserByUsername(uploader);
		List<User> emails = new ArrayList<>();
		
		for(Channel channel : user.getChannels()){
			String username = channel.getName();
			User u = UserDAO.getInstance().getUserByUsername(username);
			emails.add(u);
		}
		SendEmail send = new SendEmail(emails, name, user.getUsername());
		send.start();
	}

	private boolean addVideoInCollection(
			String name, 
			String category, 
			String description, 
			String uploader,
			String address, 
			LocalDate date, String poster) {
		
		Video video = new Video(name, uploader, category, 0, date, description, address,poster);
		videos.put(name, video);
		return true;
	}

	public List<Video> searchVideos(String name) {
		List<Video> serchResult = new ArrayList<>();
		for (Entry<String, Video> element : videos.entrySet()) {
			if(element.getKey().toLowerCase().contains(name.toLowerCase())){
				serchResult.add(element.getValue());
			}
		}

		return serchResult;
	}
	
	public void viewVideo(Video video){
		video.viewVideo();
		saveViewsInDB(video);
	}

	private void saveViewsInDB(Video video) {
		try {
			PreparedStatement stm = connection.prepareStatement("UPDATE videos set views=? where name=?;");
			
			stm.setInt(1, video.getView());
			stm.setString(2, video.getName());
			stm.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public List<Video> getRandomVideos(){
		ArrayList<Video> randomVideos = new ArrayList<>();
		randomVideos.addAll(videos.values());
		return randomVideos;
	}
	
	 public Video likeVideo(String videoName,User user){
		  String username=user.getUsername();
		  Video video = VideoDAO.getInstance().getVideoByName(videoName);
		  if(video==null){
		   System.out.println("Like video return null video");
		   return null;
		  }
		  if(video.isUserLikeVideo(username)){
		   
		   video.removeFromLike(username);
		   removeLikeFromDB(username,video.getName());
		   user.removeVideoFromLikedVideos(video);
		  }
		  else{
		   if(video.isUserDislikeVideo(username)){
		    video.removeFromDislike(username);
		    removeDisLikeFromDB(video.getName(),username);
		   }
		   video.likeVideo(username);
		   saveLikeInDB(video.getName(),username);
		   user.addVideoInLikedVideos(video);
		  }
		 
		  return video;
		 }

	public Video dislikeVideo(String videoName, String username) {
		Video video = VideoDAO.getInstance().getVideoByName(videoName);
		User user = UserDAO.getInstance().getUserByUsername(username);
		if(video==null){
			System.out.println("Like video return null video");
			return null;
		}
		if(video.isUserDislikeVideo(username)){
			video.removeFromDislike(username);
			removeDisLikeFromDB(username,video.getName());
		}
		else{
			if(video.isUserLikeVideo(username)){
				video.removeFromLike(username);
				removeLikeFromDB(username,video.getName());
				user.removeVideoFromLikedVideos(video);
			}
			video.dislikeVideo(username);
			saveDislikeInDB(video.getName(),username);
		}
		return video;
	}
	
	private void saveDislikeInDB(String name, String username) {
		this.connection = DBManager.getInstance().getConnection();
		try {
			PreparedStatement stm = connection.prepareStatement("INSERT INTO user_dislike_videos(user_name,video_name) VALUES(?,?); ");
			stm.setString(1, username);
			stm.setString(2, name);
			int rowsAffected =stm.executeUpdate();
			System.out.println("UPDATE user_disliked_videos "+ rowsAffected);
		} catch (SQLException e) {
			System.out.println("Save dislike indDB -"+e.getMessage());
		}
	}

	private void saveLikeInDB(String name, String username) {
		this.connection = DBManager.getInstance().getConnection();
		try {
			PreparedStatement stm = connection.prepareStatement("INSERT INTO user_liked_videos(user_name,video_name) VALUES(?,?); ");
			stm.setString(1, username);
			stm.setString(2, name);
			int rowsAffected =stm.executeUpdate();
			System.out.println("UPDATE user_liked_videos "+ rowsAffected);
		} catch (SQLException e) {
			System.out.println("Save like indDB -"+e.getMessage());
		}
	}

	private void removeDisLikeFromDB(String name, String username) {
		this.connection = DBManager.getInstance().getConnection();
		try {
			PreparedStatement stm = connection.prepareStatement("DELETE FROM user_dislike_videos where user_name=? AND video_name=?; ");
			stm.setString(1, username);
			stm.setString(2, name);
			stm.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Remove Dislike from dDB -"+e.getMessage());
		}
	}

	private void removeLikeFromDB(String username,String videoName) {
		this.connection = DBManager.getInstance().getConnection();
		try {
			PreparedStatement stm = connection.prepareStatement("DELETE FROM user_liked_videos where user_name=? AND video_name=?; ");
			stm.setString(1, username);
			stm.setString(2, videoName);
			stm.executeUpdate();
		} catch (SQLException e) {
			System.out.println("remove like from dDB -"+e.getMessage());
		}
	}
	
	public boolean isFreeVideoName(String videoName){
		return !videos.containsKey(videoName);
	}
	
	
}
