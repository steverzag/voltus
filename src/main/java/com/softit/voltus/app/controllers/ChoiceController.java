package com.softit.voltus.app.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ChoiceController {

    @FXML
    private Button btnAceptar;

    @FXML
    private ImageView img;

    @FXML
    private Text text;

    @FXML
    private Circle btnClose;

    @FXML
    private Button btnCancelar;

    public static ChoiceController INSTANCE;
    @FXML
    void btnAceptarOnAction(ActionEvent event) {
    	Dialogs.getInstace().setChoice(false);
    	Stage stage = (Stage) btnAceptar.getParent().getScene().getWindow();
    	stage.close();
    }

    @FXML
    void btnCancelarOnAction(ActionEvent event) {
    	Dialogs.getInstace().setChoice(true);
    	Stage stage = (Stage) btnAceptar.getParent().getScene().getWindow();
    	stage.close();
    }

    @FXML
    void btnCloseOnClicked(MouseEvent event) {
    	Dialogs.getInstace().setChoice(true);
    	Stage stage = (Stage) btnAceptar.getParent().getScene().getWindow();
    	stage.close();
    }
    
    public ChoiceController() {
		INSTANCE = this;
	}
    
    public static ChoiceController getInstance() {
		return INSTANCE;
	}
    
    public void init(String message, String imgName) {
    	text.setText(message);
		img.setImage(new Image("com/softit/voltus/app/view/images/" + imgName));
	}

}

