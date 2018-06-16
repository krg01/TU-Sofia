package com.config.model;

import java.time.LocalDate;

public class History {

	private LocalDate date;
	private String videoName;
	
	public History(LocalDate date, String videoName) {
		super();
		this.date = date;
		this.videoName = videoName;
	}

	public LocalDate getDate() {
		return date;
	}

	public String getVideoName() {
		return videoName;
	}
	
	
	
}
