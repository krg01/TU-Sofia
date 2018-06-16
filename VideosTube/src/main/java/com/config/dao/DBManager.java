package com.config.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class DBManager {

	private static DBManager instance;
	private Connection connection;
	
	private static final String DB_IP = "localhost";
	private static final String DB_PORT = "3306";
	private static final String DB_NAME = "youtube";
	private static final String DB_USERNAME = "root";
	private static final String DB_PASSWORD = "root";
	private static final String URL = "jdbc:mysql://"+DB_IP+":"+DB_PORT+"/"+DB_NAME;
	
	private DBManager(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	synchronized static DBManager getInstance(){
		if(instance == null){
			instance = new DBManager();
		}
		return instance;
	}
	
	Connection getConnection() {
		try {
			connection = DriverManager.getConnection(URL, DB_USERNAME, DB_PASSWORD);
			System.out.println("get connection");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
}