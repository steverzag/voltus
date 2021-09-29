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

public class YesNoCancelChoiceController {

    @FXML
    private Button btnSi;

    @FXML
    private ImageView img;

    @FXML
    private Text text;

    @FXML
    private Circle btnClose;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnNo;
    
    private static YesNoCancelChoiceController INSTANCE;
    
    @FXML
    void btnNoOnAction(ActionEvent event) {
    	Dialogs.getInstace().setDecision(Dialogs.NO);
    	Stage stage = (Stage) btnNo.getParent().getScene().getWindow();
    	stage.close();
    }

    @FXML
    void btnCancelarOnAction(ActionEvent event) {
    	Dialogs.getInstace().setDecision(Dialogs.CANCEL);
    	Stage stage = (Stage) btnNo.getParent().getScene().getWindow();
    	stage.close();
    }

    @FXML
    void btnCloseOnClicked(MouseEvent event) {
    	Dialogs.getInstace().setDecision(Dialogs.CANCEL);
    	Stage stage = (Stage) btnNo.getParent().getScene().getWindow();
    	stage.close();
    }

    @FXML
    void btnSiOnAction(ActionEvent event) {
    	Dialogs.getInstace().setDecision(Dialogs.YES);
    	Stage stage = (Stage) btnNo.getParent().getScene().getWindow();
    	stage.close();
    }
    
    public YesNoCancelChoiceController() {
		INSTANCE = this;
	}
    
    public static YesNoCancelChoiceController getInstance() {
		return INSTANCE;
	}

    public void init(String message, String imgName) {
    	text.setText(message);
		img.setImage(new Image("com/softit/voltus/app/view/images/" + imgName));
    }
}

