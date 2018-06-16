package com.config.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.config.model.Comment;
import com.config.model.User;
import com.config.model.Video;
import com.config.util.LocalDateTimeConverter;

public class CommentDAO {

	private static HashMap<String, List<Comment>> comments = new HashMap<String, List<Comment>>();// videoName
																									// -
																									// list
																									// with
																									// comment

	private static CommentDAO instance;

	private Connection connection;

	private CommentDAO() {
	}

	public synchronized static CommentDAO getInstance() {
		if (instance == null) {
			instance = new CommentDAO();
			instance.loadComments();

		}
		return instance;
	}

	private void loadComments() {
		try {

			this.connection = DBManager.getInstance().getConnection();
			Statement st = DBManager.getInstance().getConnection().createStatement();
			ResultSet resultSet = st.executeQuery("SELECT id,text,comment_date,video_name,user_name FROM comments ;");
			while (resultSet.next()) {
				Timestamp ts = resultSet.getTimestamp("comment_date");
				LocalDateTime dateAndTime = LocalDateTimeConverter.convertToEntityAttribute(ts);
				Comment com = new Comment(resultSet.getLong("id"), resultSet.getString("user_name"),
						resultSet.getString("text"), dateAndTime, resultSet.getString("video_name"));

				loadCommentsLikes(com);
				String videoName = resultSet.getString("video_name");
				if (!comments.containsKey(videoName)) {
					comments.put(videoName, new ArrayList<Comment>());
				}
				comments.get(videoName).add(com);
			}
		} catch (SQLException e) {
			System.out.println("load comments error " + e.getMessage());

		}
	}

	private void loadCommentsLikes(Comment com) {

		this.connection = DBManager.getInstance().getConnection();
		String sql = "Select username from comment_likes join users on comment_likes.user_name ="
				+ " users.username where comment_id = ?  ";
		PreparedStatement st;
		try {
			st = connection.prepareStatement(sql);
			st.setLong(1, com.getId());
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				com.addUserInLikesComment(rs.getString("username"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int likeComment(String user, String videoName, long comID) {
		Comment com = getCommentById(videoName, comID);
		if (com.isLikeComment(user)) {
			com.removeUserFromLikesComment(user);
			deleteComFromDB(com, user);
		} else {
			com.addUserInLikesComment(user);
			saveCommentLikeInDB(com, user);
		}

		return com.getLikes();

	}

	private void saveCommentLikeInDB(Comment com, String user) {
		this.connection = DBManager.getInstance().getConnection();
		String sql = "Insert INTO comment_likes(user_name,comment_id) VALUES(?,?);";
		try {
			PreparedStatement st = connection.prepareStatement(sql);
			st.setString(1, user);
			st.setLong(2, com.getId());
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void deleteComFromDB(Comment com, String user) {
		this.connection = DBManager.getInstance().getConnection();
		String sql = "Delete From comment_likes where user_name = ? and comment_id = ?";
		try {
			PreparedStatement st = connection.prepareStatement(sql);
			st.setString(1, user);
			st.setLong(2, com.getId());
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Comment saveComment(User user, String text, Video video) {
		this.connection = DBManager.getInstance().getConnection();
		String sql = "Insert INTO comments(text,comment_date,video_name,user_name) VALUES (?,?,?,?)";
		LocalDateTime localDateTime = LocalDateTime.now();
		Timestamp date = LocalDateTimeConverter.convertToDatabaseColumn(localDateTime);
		try {
			PreparedStatement st = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			st.setString(1, text);
			st.setTimestamp(2, date);
			st.setString(3, video.getName());
			st.setString(4, user.getUsername());

			st.executeUpdate();

			ResultSet rs = st.getGeneratedKeys();
			rs.next();
			long commentId = rs.getInt(1);
			Comment com = new Comment(commentId, user.getUsername(), text, localDateTime, video.getName());
			
			if (!comments.containsKey(video.getName())) {

				comments.put(video.getName(), new ArrayList<Comment>());
			}

			comments.get(video.getName()).add(com);
			video.addComment(com);
			return com;
		} catch (SQLException e) {
			System.out.println("Comment save error -- " + e.getMessage());
		}
		return null;
	}

	public List<Comment> getCommentsForVideo(String videoName) {
		if (!comments.containsKey(videoName)) {
			comments.put(videoName, new ArrayList<>());
		}
		return Collections.unmodifiableList(comments.get(videoName));
	}

	public Comment getCommentById(String videoName, long comID) {
		List<Comment> commentsVideo = getCommentsForVideo(videoName.trim());
		for (Comment com : commentsVideo) {
			if (com.getId() == comID) {
				return com;
			}
		}
		return null;
	}

}
