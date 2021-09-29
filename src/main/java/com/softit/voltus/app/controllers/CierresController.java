package com.softit.voltus.app.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.softit.voltus.app.classes.Cuadre;
import com.softit.voltus.app.classes.Paths;
import com.softit.voltus.app.model.CuadreMensual;
import com.softit.voltus.app.model.PersistenceManager;

import animatefx.animation.FadeIn;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class CierresController implements Initializable {

	@FXML
	private ComboBox<String> cbxCuadres;

	@FXML
	private Label lblPendiente;
	
	@FXML
	private Button btnGenerar;
	
	@FXML
    private HBox hbxCuadrePendiente;

	@FXML
	private BorderPane pnlDashBoard;

	private PersistenceManager pm = PersistenceManager.getPersistenceInstace();
	private List<CuadreMensual> cuadres;
	private List<CuadreMensual> cuadresPendientes;
	private static CierresController INSTANCE;

	@FXML
	void btnGenerarOnAction(ActionEvent event) {
		
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource(Paths.VIEWS_PATH + "CierreGenerator.fxml"));
			CierreGeneratorController cierre = CierreGeneratorController.getInstance();
			cierre.setCuadre(cuadresPendientes.get(0));
			new FadeIn(root).play();
			pnlDashBoard.setCenter(root);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void cbxCuadresOnAction(ActionEvent event) {

		Parent root = null;
		if(cbxCuadres.getSelectionModel().getSelectedItem().equals("Global")) {
			try {
				root = FXMLLoader.load(getClass().getResource(Paths.VIEWS_PATH + "GlobalEstadistics.fxml"));
				new FadeIn(root).play();
				pnlDashBoard.setCenter(root);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			root = FXMLLoader.load(getClass().getResource(Paths.VIEWS_PATH + "CuadreMensualBoard.fxml"));
			CuadreMensualBoardController board = CuadreMensualBoardController.getInstance();
			board.setCuadre(pm.getCuadreMensual(cbxCuadres.getSelectionModel().getSelectedItem()));
			new FadeIn(root).play();
			pnlDashBoard.setCenter(root);
		} catch (Exception e) {
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		cuadres = pm.getCuadreMensualList();
		cbxCuadres.getItems().add("Global");
		for (int i = 0; i < cuadres.size(); i++) {
			cbxCuadres.getItems().add(cuadres.get(i).getId());
		}
		cbxCuadres.getSelectionModel().select("Global");
		cbxCuadresOnAction(null);
		Cuadre c = new Cuadre();
		cuadresPendientes = c.getCuadresPendientes();
		if(cuadresPendientes.size() > 0) {
			lblPendiente.setText("Cuadre Pendiente: " + cuadresPendientes.get(0).getId());
			hbxCuadrePendiente.setVisible(true);
		}
	}

	public CierresController() {
		INSTANCE = this;
	}

	public static CierresController getInstance() {

		return INSTANCE;
	}
}
