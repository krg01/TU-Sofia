package com.config.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.config.model.Playlist;
import com.config.model.User;

public class PlayListDAO {
	
	private static HashMap<String, Set<Playlist>> playlist = new HashMap<String, Set<Playlist>>();// username - > set playlist

	private static PlayListDAO instance;

	private Connection connection;

	private PlayListDAO() {
	}

	public synchronized static PlayListDAO getInstance() {
		if (instance == null) {

			instance = new PlayListDAO();
			instance.loadPlayList();
		}
		return instance;
	}

	private void loadPlayList() {
		
		this.connection = DBManager.getInstance().getConnection();
		try {
		
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery("Select name, user_name from playlists");
			while(rs.next()){
				String name = rs.getString("name");
				String username = rs.getString("user_name");
				Playlist pl = new Playlist(username, name);
				loadPlaylistVideos(pl);
				if(!playlist.containsKey(username)){
					playlist.put(username, new HashSet<>());
				}
				playlist.get(username).add(pl);
			}
		} catch (SQLException e1) {
			
		}
	}

	private void loadPlaylistVideos(Playlist pl) {
		
		String sql = "Select video_name FROM playlist_video where playlist_name = ?";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);
			stm.setString(1, pl.getName());
			ResultSet rs = stm.executeQuery();
			while(rs.next()){
				String video = rs.getString("video_name");
				pl.addVideoInPlaylist(video);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Set<Playlist> getUserPlayList(String username){
		if(playlist.containsKey(username)){
			return Collections.unmodifiableSet(playlist.get(username));
		}
		return new HashSet<Playlist>();
	}

	public boolean createPlaylist(String name, String username) {
		User user = UserDAO.getInstance().getUserByUsername(username);
		
		if(playlist.containsKey(username)){
			for (Playlist playlist : playlist.get(username)) {
				System.out.println(playlist.getName() + " --- "+ name);
				if(playlist.getName().equals(name)){
					return false;
				}
			}
		}else{
			playlist.put(username, new HashSet<>());
		}
		
		try {
			this.connection = DBManager.getInstance().getConnection();
			String query = "INSERT INTO playlists (name, user_name) VALUES (?, ?);";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setString(1, name);
			stm.setString(2, username);
			stm.executeUpdate();
			
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
				return false;
			}

		Playlist pl = user.createPlaylist(name);
		playlist.get(username).add(pl);
		
		return true;
	}

	public Set<Playlist> getPlaylists(String username){
		if(!playlist.containsKey(username)){
			return null;
		}
		return playlist.get(username);
	}
	
	public String addVideoInPlaylist(String username,String videoName,String playlistName){
		
		User user = UserDAO.getInstance().getUserByUsername(username);
		Playlist pl = user.getUserPlaylist(playlistName);
		if(pl.isVideoInList(videoName.trim())){
			pl.removeVideoFromList(videoName);
			removeVideoFromDB(username,videoName,playlistName);
			return "Removed from playlist";
		}
		else{
			pl.addVideoInPlaylist(videoName);
			addVideoInPlaylistDB(username,videoName,playlistName);	
			return "Added in playlist";}
		
		
	}

	private void addVideoInPlaylistDB(String username, String videoName, String playlistName) {
		this.connection = DBManager.getInstance().getConnection();
		try {
			PreparedStatement stm = connection.prepareStatement("INSERT INTO playlist_video(video_name, username,playlist_name) VALUES(?,?,?); ");
			stm.setString(1, videoName);
			stm.setString(2, username);
			stm.setString(3, playlistName);
			int rowsAffected =stm.executeUpdate();
			System.out.println("UPDATE playlist_video "+ rowsAffected);
		} catch (SQLException e) {
			System.out.println("Save dislike indDB -"+e.getMessage());
		}
		
	}

	private void removeVideoFromDB(String username, String videoName, String playlistName) {
		this.connection = DBManager.getInstance().getConnection();
		try {
			PreparedStatement stm = connection.prepareStatement("DELETE FROM playlist_video where playlist_name=? AND video_name=? AND username=? ; ");
			stm.setString(1, playlistName);
			stm.setString(2, videoName);
			stm.setString(3, username);
			stm.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Remove from dDB -"+e.getMessage());
		}
		
	}
	
	public List<Playlist> getAllPlaylists(){
		System.out.println(playlist.size() + " SIZE");
		
		List<Playlist> allPlaylists = new ArrayList<>();
		for (Set<Playlist> playlists : PlayListDAO.playlist.values()) {
			allPlaylists.addAll(playlists);
		}
		return allPlaylists;
	}

	public List<Playlist> searchPlaylists(String name) {
		List<Playlist> result = new ArrayList<>();
		for (Playlist pl : getAllPlaylists()) {
			if(pl.getName().toLowerCase().equals(name.toLowerCase())){
				result.add(pl);
			}
		}
		return result;
	}
}
