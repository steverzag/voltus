package com.softit.voltus.app.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class VoltusMain extends Application{

	public static void main(String[] args) {
		launch(args);

	}

	private double x=0;
	private double y=0;
	
	@Override
	public void start(Stage stage) throws Exception {
		
		Parent root=FXMLLoader.load(getClass().getResource("/com/softit/voltus/app/view/LogIn.fxml"));
			
		Scene scene=new Scene(root);
		stage.setScene(scene);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.show();
		
		root.setOnMousePressed(e -> {
			x=e.getSceneX();
			y=e.getSceneY();
		});
		
		root.setOnMouseDragged(e -> {
			
			stage.setX(e.getScreenX()-x);
			stage.setY(e.getScreenY()-y);
			
		});
		
	}

}
