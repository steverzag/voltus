package com.softit.voltus.app.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AlertController implements Initializable{

    @FXML
    private Button btnAceptar;

    @FXML
    private ImageView img;

    @FXML
    private Text text;

    @FXML
    private Circle btnClose;

    private static AlertController INSTANCE;
    @FXML
    void btnAceptarOnAction(ActionEvent event) {
    	Stage stage = (Stage) btnClose.getParent().getScene().getWindow();
    	stage.close();
    }

    @FXML
    void btnCloseOnClicked(MouseEvent event) {
    	Stage stage = (Stage) btnClose.getParent().getScene().getWindow();
    	stage.close();
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	public AlertController() {
		INSTANCE = this;
	}
	
	public static AlertController getInstance() {
		return INSTANCE;
	}
	public void init(String message, String imgName) {
		text.setText(message);
		img.setImage(new Image("com/softit/voltus/app/view/images/" + imgName));
	}

}

