package com.softit.voltus.app.controllers;

import java.io.IOException;
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

public class RecepcionController implements Initializable {

	@FXML
	private BorderPane pnlContainer;

	@FXML
	private Button btnEntrada;

	@FXML
	private Button btnAlquiler;

	@FXML
	private Button btnSalida;

	private Parent entrada = null;
	private Parent alquiler = null;
	private Parent salida = null;

	@FXML
	void btnAlquilerOnAction(ActionEvent event) {
		
		try {
			alquiler = FXMLLoader.load(getClass().getResource(Paths.VIEWS_PATH + "AlquilerArticulos.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		btnEntrada.getStyleClass().remove("button-nav-selectd");
		btnSalida.getStyleClass().remove("button-nav-selectd");
		btnAlquiler.getStyleClass().add("button-nav-selectd");
		
		pnlContainer.setCenter(alquiler);
		new FadeIn(alquiler).play();
	}

	@FXML
	void btnEntradaOnAction(ActionEvent event) {

		try {
			entrada = FXMLLoader.load(getClass().getResource(Paths.VIEWS_PATH + "Entrada.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		btnEntrada.getStyleClass().add("button-nav-selectd");
		btnSalida.getStyleClass().remove("button-nav-selectd");
		btnAlquiler.getStyleClass().remove("button-nav-selectd");
		
		pnlContainer.setCenter(entrada);
		new FadeIn(entrada).play();
	}

	@FXML
	void btnSalidaOnAction(ActionEvent event) {
		
		try {
			salida = FXMLLoader.load(getClass().getResource(Paths.VIEWS_PATH + "Salida.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		btnEntrada.getStyleClass().remove("button-nav-selectd");
		btnSalida.getStyleClass().add("button-nav-selectd");
		btnAlquiler.getStyleClass().remove("button-nav-selectd");
		
		pnlContainer.setCenter(salida);
		new FadeIn(salida).play();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		btnEntrada.fire();
	}

}
