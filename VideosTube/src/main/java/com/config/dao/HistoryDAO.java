package com.config.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.config.model.History;


public class HistoryDAO {
	private static HashMap<String, Set<String>> history = new HashMap<String, Set<String>>();// username - > list watched videos

	private static HistoryDAO instance;

	private Connection connection;

	private HistoryDAO() {
	}

	public synchronized static HistoryDAO getInstance() {
		if (instance == null) {

			instance = new HistoryDAO();
			instance.loadHistory();
		}
		return instance;
	}

	private void loadHistory() {
		
		this.connection = DBManager.getInstance().getConnection();
		String sql ="Select user_name,video_name from histories";
		try {
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			while(rs.next()){
				String username = rs.getString("user_name");
				String videoName = rs.getString("video_name");
				if(!history.containsKey(username)){
					history.put(username, new HashSet<>());
				}
				history.get(username).add(videoName);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void saveInHistory(String username, String videoName){
		if(!history.containsKey(username)){
			history.put(username, new HashSet<>());
		}
		history.get(username).add(videoName);
		saveInDB(username,videoName);
	}

	private void saveInDB(String username, String videoName) {
		this.connection = DBManager.getInstance().getConnection();
		String sql = "Insert into histories (user_name,video_name) VALUES (?,?);";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);
			stm.setString(1, username);
			stm.setString(2, videoName);
			stm.executeUpdate();
			System.out.println("Added in History - " +username +" watched - " +videoName);
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	public Set<String> getUserHistory(String username){
		return Collections.unmodifiableSet(history.get(username));
	}
}
