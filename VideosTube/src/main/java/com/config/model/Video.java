package com.config.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Video {

	private String name; 
	private String uploader;
	private String category;
	private TreeSet<Comment> comments;
	private String description;
	private	HashSet<String> likes; //username ->User
	private HashSet<String> dislikes ; 
	private int view;
	private String address;
	private LocalDate date;
	private String poster;
	public Video(String name, String uploader, String category, int view, LocalDate date, String description,String address, String poster) {
		this.name = name;
		this.uploader = uploader;
		this.address = address;
		this.category = category;
		this.view = view;
		this.date = date;
		this.description = description;
		this.poster=poster;
		this.likes = new HashSet<>();
		this.dislikes = new HashSet<>();
		this.comments = new TreeSet<Comment>((v1,v2)-> v2.getDateInDate().compareTo(v1.getDateInDate()));
	}
	
	public String getPoster(){
		return this.poster;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUploader() {
		return uploader;
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getView() {
		return this.view;
	}

	public void setView(int view) {
		this.view = view;
	}
	
	public int viewVideo(){
		return this.view++;
	}
	
	public int getLikes(){
		return likes.size();
	}
	
	public int getDislikes(){
		return dislikes.size();
	}
	
	public int commentsCount(){
		return comments.size();
	}

	public boolean isUserLikeVideo(String user){
		return likes.contains(user);
	}
	
	public boolean isUserDislikeVideo(String user){
		return dislikes.contains(user);
	}
	
	public void likeVideo(String user){
		likes.add(user);
	}
	
	public void dislikeVideo(String user){
			this.dislikes.add(user);
	}
	
	public void removeFromDislike(String user){
		dislikes.remove(user);
	}
	
	public void removeFromLike(String user){
		likes.remove(user);
	}
	
	public void addComment(Comment com){
		this.comments.add(com);
	}

	public void removeComment(Comment com ){
		this.comments.remove(com);
	}
	
	public Set<Comment> showVideoComments(){
		return (Set<Comment>) Collections.unmodifiableSet(comments);
	}

	public Set<Comment> getVideoComments(){
		Set<Comment> comments = new TreeSet<Comment>((v1,v2)-> v1.getDateInDate().compareTo(v2.getDateInDate()));
		comments.addAll(this.comments);
		return comments;
	}
	
	public String getComments(){
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		ArrayList<Comment> comments = new ArrayList<Comment>();
		comments.addAll(this.comments);
		return comments.toString();
	}

}
