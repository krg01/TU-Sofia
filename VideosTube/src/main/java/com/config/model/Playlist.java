package com.config.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Playlist {

	private long id ;
	private String user;
	private String name;
	private List<String> videos;//videoname
	private int count;
	
	public Playlist(String user, String name) {
		this.user = user;
		this.name = name;
		this.videos = new ArrayList<>();
		this.count = 0;
	}

	public long getId() {
		return id;
	}
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addVideoInPlaylist(String video){
		this.videos.add(video);
	}
	
	public List<String> getVideosFromPlaylist(){
		return Collections.unmodifiableList(videos);
	}
	
	public void removeVideoFromList(String video){
		this.videos.remove(video);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Playlist other = (Playlist) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	public int getCount() {
		return videos.size();
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public String getFirstVideo(){
		if(videos.size() < 1){
			return null;
		}
		
		return videos.get(0).trim();
	}
	
	public boolean isVideoInList(String videoName){
		for(String s: videos){
			if(s.trim().equals(videoName)){
				return true;
			}
		}
		return false;
	}
	
	public int getVideoIndex(String videoName){
		return videos.indexOf(videoName);
	}
	
	public String getVideoByIndex(int index){
		if(index<videos.size()){
			return videos.get(index);
		}
		return null;
	}
}
