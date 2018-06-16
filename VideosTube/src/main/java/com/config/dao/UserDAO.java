package com.config.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import com.config.exception.CreateUserException;
import com.config.model.Channel;
import com.config.model.Playlist;
import com.config.model.User;
import com.config.model.Video;

public class UserDAO {

	private static HashMap<String, User> users = new HashMap<String, User>();// username -> user
																																			
	private static UserDAO instance;

	private Connection connection;

	private UserDAO() {
	}

	public synchronized static UserDAO getInstance() {
		if (instance == null) {

			instance = new UserDAO();
			instance.loadUsers();
		}
		return instance;
	}

	private void loadUsers() {
		this.connection = DBManager.getInstance().getConnection();
		
		try {
			Statement st = connection.createStatement();
			ResultSet resultSet = st.executeQuery("SELECT username, password, profilePic, email FROM users;");
			while (resultSet.next()) {
				User user = new User(
						resultSet.getString("username"),
						resultSet.getString("password"),
						resultSet.getString("profilePic"),
						resultSet.getString("email"));
				loadChannel(user);
				loadAbonatedChannels(user);
				loadLikedVideos(user);
				loadPlaylists(user);
				users.put(resultSet.getString("username"), user);
				}	
			} 
		catch (SQLException e1) {
			
		} catch (CreateUserException e) {
			System.out.println(e.getMessage());
		}
		

	}

	private void loadPlaylists(User user) {
		Set<Playlist> list = PlayListDAO.getInstance().getUserPlayList(user.getUsername());
		if(list != null){
			user.createPlaylist(list);
		}
	}

	private void loadLikedVideos(User user) {
		
		String sql = "Select video_name from user_liked_videos where user_name = ?";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);
			stm.setString(1, user.getUsername());
			ResultSet rs = stm.executeQuery();
			while(rs.next()){
				user.addVideoInLikedVideos(VideoDAO.getInstance().getVideoByName(rs.getString("video_name")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void loadChannel(User user) {
		Channel channel = ChannelDAO.getInstance().getUserChannel(user.getUsername());
		user.setChannel(channel);
	}

	private void loadAbonatedChannels(User user) {

		
		
		try {
			PreparedStatement st = connection.prepareStatement("SELECT channel_name FROM subscribes where user_name = ?;");
			st.setString(1, user.getUsername());
			ResultSet resultSet = st.executeQuery();
			while(resultSet.next()){
			
				Channel channel = ChannelDAO.getInstance().getUserChannel(resultSet.getString("channel_name"));
				user.subscribeChannel(channel);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public User getUserByUsername(String username) {
		User user = null;
		if (users.containsKey(username)) {
			user = users.get(username);
		}
		return user;
	}

	public boolean registerUser(String name, String password, String email) throws CreateUserException {
		String userPic = User.DEFAULT_PROFILE_PICTURE;
		System.out.println("Registe user in DAO");
		if(name.length()>12||name.length()<4){
			throw new CreateUserException("Username must be >4 and < 12");
		}
		if (users.containsKey(name)) {
			throw new CreateUserException("User with this username already exist !");
		} else if (addInCollection(name, password, email)) {
			return addInDB(name, password, userPic, email);
		}

		return false;
	}

	private boolean addInDB(String name, String password, String profilePic, String email) {

		try {
			this.connection = DBManager.getInstance().getConnection();

			String sql = "insert into users(username,password,profilePic,email) values(?,?,?,?);";
			PreparedStatement stm = connection.prepareStatement(sql);
			stm.setString(1, name);
			stm.setString(2, password);
			stm.setString(3, profilePic);
			stm.setString(4, email);

			stm.executeUpdate();

			connection.close();
			ChannelDAO.getInstance().createChannel(name);
			return true;

		} catch (SQLException e) {
			System.out.println("Pri loadvane na userite " +e.getMessage());
			return false;
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean addInCollection(String name, String password, String email) throws CreateUserException {
		User user = new User(name, password, email);
		users.put(name, user);
		return true;
	}
	
	public boolean login(String username , String password){
		if(users.containsKey(username)){
			if(users.get(username).isValidPassword(password)){
				return true;
			}
		}
		return false;
	}
	
	public List<User> searchUsers(String name){
		List<User> results = new ArrayList<>();
		for (Entry<String, User> user : users.entrySet()) {
			if(user.getKey().toLowerCase().contains(name.toLowerCase())){
				results.add(user.getValue());
			}
		}
		return results;
	}

	public boolean changeProfilePicture(String fileName, String username) {
		User user = getUserByUsername(username);
		if(user == null){
			System.out.println("Can't change picture. User is NULL.");
			return false;
		}
		user.setProfilePic(fileName);
				
		try {
			this.connection = DBManager.getInstance().getConnection();

			String sql = "UPDATE users SET profilePic = (?) WHERE username = (?);";
			PreparedStatement stm = connection.prepareStatement(sql);
			stm.setString(1, fileName);
			stm.setString(2, username);

			stm.executeUpdate();

			return true;

		} catch (SQLException e) {
			System.out.println("INVALID DB LOGIN " + e.getMessage());
			return false;
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean isUserExist(String username){
		return users.containsKey(username);
	}
}

/*
* TODO: Video page : махаме report, google (g+); Share on -> Like video; Views в ляво; Махаме линка от датата
* 	Profile pic на коментарите, бутон [+Add To] (да изкарва листовете на логнатия потребител -> 
* 	да избереш къде да добавиш видеото) 
*	Login page: съобщение за невалидни данни, да връща в същата страница
*	My Channel page: при достъп от друг акаунт да променя страницата, 
*	playlist да има бутон [create playlist]
*	Search: No Results
*	Play list:
*	Subscribe for channel
*	
*/
