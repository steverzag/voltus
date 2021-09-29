package com.softit.voltus.app.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AlertWithDetailsController {

    @FXML
    private Button btnAceptar;

    @FXML
    private ImageView img;

    @FXML
    private Text text;

    @FXML
    private Circle btnClose;

    @FXML
    private Button btnDatails;

    @FXML
    private ImageView imgDetails;

    @FXML
    private VBox vbxDetails;

    @FXML
    private Label lblDetailsCaption;
    
    private static AlertWithDetailsController INSTANCE;

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

    @FXML
    void btnDatailsOnAction(ActionEvent event) {
    	Parent root = btnClose.getParent();
    	boolean b = vbxDetails.isVisible();
    	vbxDetails.setVisible(!b);
    	if(b) {
    		root.resize(375, 200);
    		root.getScene().getWindow().setHeight(200);
    		imgDetails.setImage(new Image("com/softit/voltus/app/view/images/icons8_expand_arrow_25px.png"));
    	}else {
    		root.resize(375, 200 + vbxDetails.getHeight());
    		root.getScene().getWindow().setHeight(275 + vbxDetails.getHeight());
    		imgDetails.setImage(new Image("com/softit/voltus/app/view/images/icons8_collapse_arrow_25px_1.png"));
    	}
    	
    }
    
    public AlertWithDetailsController() {
    	INSTANCE = this;
	}
    
    public void init(String message, String imgName, String detailsCaption, Node detailsNode) {
		text.setText(message);
		img.setImage(new Image("com/softit/voltus/app/view/images/" + imgName));
		lblDetailsCaption.setText(detailsCaption);
		vbxDetails.getChildren().add(detailsNode);
	}
    
    public static AlertWithDetailsController getInstance() {
    	return INSTANCE;
    }

}

