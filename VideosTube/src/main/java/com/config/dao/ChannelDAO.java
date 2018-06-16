package com.config.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

import com.config.model.Channel;
import com.config.model.User;
import com.config.model.Video;

public class ChannelDAO {

	private static HashMap<String, Channel> channels = new HashMap<String, Channel>();// username
																						// -
																						// >channel

	private static ChannelDAO instance;

	private Connection connection;

	private ChannelDAO() {
	}

	public synchronized static ChannelDAO getInstance() {
		if (instance == null) {
			instance = new ChannelDAO();
			instance.loadChannels();
		}
		return instance;
	}

	private void loadChannels() {
		this.connection = DBManager.getInstance().getConnection();
		String sql = "Select name, description, user_name from channels";
		try {
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			while (rs.next()) {
				Channel channel = new Channel(rs.getString("user_name"), rs.getString("description"));
				loadChannelVideos(channel);
				loadSubscribes(channel);
				channels.put(channel.getName(), channel);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void loadSubscribes(Channel channel) {
		this.connection = DBManager.getInstance().getConnection();
		String sql = "Select user_name from subscribes where channel_name = ? ;";
		PreparedStatement stm;
		try {
			stm = connection.prepareStatement(sql);
			stm.setString(1, channel.getName());// channel name equals user name
			ResultSet rs = stm.executeQuery(sql);
			while (rs.next()) {
				channel.addUserInChannel(rs.getString("user_name"));
			}
		} catch (SQLException e) {
		}

	}

	public Channel getUserChannel(String username) {
		Channel channel = null;
		if (channels.containsKey(username)) {
			channel = channels.get(username);
		}
		return channel;
	}

	private void loadChannelVideos(Channel channel) {

		List<Video> videoInChannel = VideoDAO.getInstance().getUserVideos(channel.getName());
		for (Video v : videoInChannel) {
			channel.addVideoInChannel(v.getName());
		}
	}

	public void createChannel(String username) {

		addInCollection(username);
		addInDB(username);
	}

	private void addInDB(String username) {

		try {
			PreparedStatement stm = connection.prepareStatement("INSERT INTO channels(name,user_name) VALUES (?,?);");
			stm.setString(1, username);
			stm.setString(2, username);
			stm.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private void addInCollection(String username) {
		Channel channel = new Channel(username);
		channels.put(username, channel);
	}

	public String subscribeChannel(String username, String channelName) {
		Channel ch = channels.get(channelName);
		User user = UserDAO.getInstance().getUserByUsername(username);
		if (ch != null) {
			if (ch.checkUserSubscribe(username)) {
				ch.removeUserFromChannel(username);
				removeUserFromChannelDB(username, channelName);
				user.cancelSubscribeChannel(ch);
				return "Subscribe";
			} else {
				ch.addUserInChannel(username);
				addUserInChannelDB(username, channelName);
				user.subscribeChannel(ch);
				return "Unsubscribe";
			}
		}
		return "Error";
	}

	private void addUserInChannelDB(String username, String channelName) {
		try {
			PreparedStatement stm = connection
					.prepareStatement("INSERT INTO subscribes(channel_name,user_name) VALUES (?,?);");
			stm.setString(1, channelName);
			stm.setString(2, username);
			stm.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	private void removeUserFromChannelDB(String username, String channelName) {
		try {
			PreparedStatement stm = connection
					.prepareStatement("DELETE FROM subscribes where channel_name=? and user_name=? ;");
			stm.setString(1, channelName);
			stm.setString(2, username);
			stm.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}
}