package com.softit.voltus.app.controllers;

import java.io.IOException;

import com.softit.voltus.app.classes.Paths;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Dialogs {

	private Stage stage;
	private Parent root;
	private double x = 0;
	private double y = 0;
	private boolean choice = false;
	private static Dialogs INSTANCE;
	
	public static int YES = 0;
	public static int NO = 1;
	public static int CANCEL = 2;
	private int decision;

	public Dialogs() {
		stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initStyle(StageStyle.TRANSPARENT);
		INSTANCE = this;
	}

	public void displayAlert(String message, String imgName) {
		try {
			root = FXMLLoader.load(getClass().getResource(Paths.VIEWS_PATH + "Alert.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		initRoot();
		AlertController.getInstance().init(message, imgName);

		stage.showAndWait();
	}

	public void displayAlertWithDetails(String message, String imgName, String detailsCaption, Node detailsNode) {
		
		try {
			root = FXMLLoader.load(getClass().getResource(Paths.VIEWS_PATH + "AlertWithDetails.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		initRoot();
		AlertWithDetailsController.getInstance().init(message, imgName, detailsCaption, detailsNode);

		stage.showAndWait();
	}
	
	public boolean displayChoice(String message, String imgName) {
		try {
			root = FXMLLoader.load(getClass().getResource(Paths.VIEWS_PATH + "Choice.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		initRoot();
		ChoiceController.getInstance().init(message, imgName);
		stage.showAndWait();
		return choice;
	}
	
	public int displayYesNoCancelChoice(String message, String imgName) {
		try {
			root = FXMLLoader.load(getClass().getResource(Paths.VIEWS_PATH + "YesNoCancelChoice.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		initRoot();
		YesNoCancelChoiceController.getInstance().init(message, imgName);
		stage.showAndWait();
		return decision;
	}

	public static Dialogs getInstace() {

		return INSTANCE;
	}
	
	private void initRoot() {

		Scene scene = new Scene(root);
		stage.setScene(scene);

		root.setOnMousePressed(e -> {
			x = e.getSceneX();
			y = e.getSceneY();
		});

		root.setOnMouseDragged(e -> {
			stage.setX(e.getScreenX() - x);
			stage.setY(e.getScreenY() - y);
		});
	}

	public boolean isChoice() {
		return choice;
	}

	public void setChoice(boolean choice) {
		this.choice = choice;
	}
	
	public int getDecision() {
		return decision;
	}
	
	public void setDecision(int decision) {
		this.decision = decision;
	}
}
