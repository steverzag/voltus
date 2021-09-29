package com.softit.voltus.app.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.softit.voltus.app.classes.Paths;

import animatefx.animation.FadeIn;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class EstadisticasController implements Initializable{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private BorderPane pnlContainer;

    @FXML
    private Button btnCierres;
    
    
    @FXML
    private Button btnClientsMemb;
    
    
    private static EstadisticasController INSTANCE;
    
        @FXML
    void btnCierresOnAction(ActionEvent event) {
    	Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource(Paths.VIEWS_PATH + "Cierres.fxml"));
			new FadeIn(root).play();
			pnlContainer.setCenter(root);
		} catch (Exception e) {
		}
		btnCierres.getStyleClass().add("button-nav-selectd");
		btnClientsMemb.getStyleClass().remove("button-nav-selectd");
    }
        
        @FXML
        void btnClientsMembOnAction(ActionEvent event) {
        	Parent root = null;
    		try {
    			root = FXMLLoader.load(getClass().getResource(Paths.VIEWS_PATH + "ClientsMembInformation.fxml"));
    			new FadeIn(root).play();
    			pnlContainer.setCenter(root);
    		} catch (Exception e) {
    		}
    		btnCierres.getStyleClass().removeAll("button-nav-selectd");
    		btnClientsMemb.getStyleClass().add("button-nav-selectd");
        }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		btnCierres.fire();
	}
	
	public EstadisticasController() {
		INSTANCE = this;
	}

	public static EstadisticasController getInstance() {
		return INSTANCE;
	}
}

