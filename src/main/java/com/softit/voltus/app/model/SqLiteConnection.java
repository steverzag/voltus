package com.softit.voltus.app.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class SqLiteConnection {

	private static SqLiteConnection INSTANCE = null;
	private Connection con = null;
	private String dataBasePath = null;
	private PreparedStatement pS = null;
	private Statement stm = null;

	private SqLiteConnection(String dataBasePath) {
		this.dataBasePath = dataBasePath;
		connecting();
		try {
			stm = con.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static SqLiteConnection getConnectionInstance(String dataBasePath) {
		if (INSTANCE == null) {
			INSTANCE = new SqLiteConnection(dataBasePath);
		}

		return INSTANCE;
	}
	
	public void reConnect() {
		closeConnection();
		connecting();
		try {
			stm = con.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Connection getConnection() {

		return con;
	}

	public PreparedStatement getPreparedStatement(String consult) {
		
		try {
			pS = con.prepareStatement(consult);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pS;
	}

	public Statement getStatement() {

		return stm;
	}

	private void connecting() {
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:" + dataBasePath);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void closeConnection() {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
