package com.config.model;

import java.security.CryptoPrimitive;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.config.dao.CommentDAO;
import com.config.dao.PlayListDAO;
import com.config.exception.CreateUserException;

public class User {
	public static final String DEFAULT_PROFILE_PICTURE = "defaulfProfilePic.png";

	private String username;
	private String password;
	private String profilePic;
	private String email;
	private Channel myChannel;
	private List<Channel> subscriptions;
	private TreeSet<History> history;
	private List<Video> likedVideos;
	private List<Playlist> playLists;

	public User(String name, String password, String email) throws CreateUserException {
		if (name.length() < 4 || name.length() > 12) {
			throw new CreateUserException("Username must be >4 and < 12");
		}
		setUsername(name);
		if (!isValidEmail(email)) {
			throw new CreateUserException("Invalid email !!!");
		}
		this.password = password;
		this.email = email;
		this.profilePic = DEFAULT_PROFILE_PICTURE;
		this.subscriptions = new ArrayList<>();
		this.history = new TreeSet<History>((v1, v2) -> v1.getDate().compareTo(v2.getDate()));
		this.likedVideos = new ArrayList<>();
		this.playLists = new ArrayList<>();

	}

	public User(String name, String password, String profilePic, String email) throws CreateUserException{
		this(name, password, email);
		if(!profilePic.equals(null)){
			this.setProfilePic(profilePic);
		}
	}
	
	public User() {
		this.profilePic = DEFAULT_PROFILE_PICTURE;
	}

	public void addVideoInLikedVideos(Video video) {
		this.likedVideos.add(video);
	}

	public void removeVideoFromLikedVideos(Video video) {
		this.likedVideos.remove(video);
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Video> getVideosWhereILike() {
		return Collections.unmodifiableList(this.likedVideos);
	}

	public void addInHistory(History his) {
		this.history.add(his);

	}

	public Set<History> getHistory() {
		return Collections.unmodifiableSet(this.history);
	}

	public void subscribeChannel(Channel channel) {
		this.subscriptions.add(channel);
	}

	public List<Channel> getChannels() {
		return Collections.unmodifiableList(this.subscriptions);
	}

	public Channel getMyChannel() {
		return myChannel;
	}

	public void cancelSubscribeChannel(Channel channel) {
		this.subscriptions.remove(channel);
	}

	public List<Playlist> getPlayLists() {
		return 	Collections.unmodifiableList(this.playLists);
	}

	public void setPlayLists(List<Playlist> playLists) {
		this.playLists = playLists;
	}

	public void setChannel(Channel ch) {
		this.myChannel = ch;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String name) {
		this.username = name;
	}

	public void setPassword(String password) {
		this.password = CryptWithMD5.cryptWithMD5(password);
	}
	
 	public List<Video> getLikedVideos() {
		return Collections.unmodifiableList(likedVideos);
	}

	public void setLikedVideos(List<Video> likedVideos) {
		this.likedVideos = likedVideos;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public String getEmail() {
		return email;
	}

	public boolean isValidPassword(String password){
		String criptPass = CryptWithMD5.cryptWithMD5(password);
		return this.password.equals(criptPass);
	}

	public static boolean isValidEmail(String enteredEmail) {
		String EMAIL_REGIX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(EMAIL_REGIX);
		Matcher matcher = pattern.matcher(enteredEmail);
		return ((!enteredEmail.isEmpty()) && (enteredEmail != null) && (matcher.matches()));

	}
	
	public boolean isSubscribeChannel(String channelName){
		for(Channel channel: this.subscriptions){
			if(channel.getName().equals(channelName)){
				return true;
			}
		}
		System.out.println("FALSE");
		return false;
	}

	public Playlist createPlaylist(String name){
		Playlist pl = new Playlist(this.username, name);
		this.playLists.add(pl);
		return pl;
	}

	public void createPlaylist(Set<Playlist> userPlayList) {
		for (Playlist playlist : userPlayList) {
			this.playLists.add(playlist);
		}	
	}
	
	public Playlist getUserPlaylist(String playlistName){
		
		for(Playlist list: this.playLists){
			if(list.getName().equals(playlistName)){
				return list;
			}
		}
		return null;
	}
	
	public boolean isVideoInList(String listName, String videoName){
		Playlist list =null;
		for(Playlist pl : playLists){
			if(pl.getName().equals(listName)){
				list = pl;
			}
		}
		return list.isVideoInList(videoName);
	}
	
	public boolean isLikeComment(String videoName, long comId){
		Comment com = CommentDAO.getInstance().getCommentById(videoName.trim(), comId);
		return com.isLikeComment(this.getUsername());
	}

	
	public String getPassword() {
		return password;
	}
}
