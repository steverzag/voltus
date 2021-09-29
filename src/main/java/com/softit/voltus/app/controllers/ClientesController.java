package com.softit.voltus.app.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.controlsfx.control.textfield.TextFields;
import com.softit.voltus.app.classes.Clientes;
import com.softit.voltus.app.classes.Paths;
import com.softit.voltus.app.model.ClientesInfoPersonal;
import com.softit.voltus.app.model.PersistenceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class ClientesController implements Initializable {

	@FXML
	private TextField txtBuscar;

	@FXML
	private Button btnBuscar;

	@FXML
	private Button btnAddClient;

	@FXML
	private BorderPane pnlContainer;

	private static ClientesController instance;

	PersistenceManager pm;

	public ClientesController() {
		instance = this;
	}
	
	@FXML
	void btnAddClientOnAction(ActionEvent event) {
		txtBuscar.setText("");
		insertClient();
	}

	@FXML
	void txtBuscarOnAction(ActionEvent event) {

		if (getClient() != null) {
			insertClient();
		}
	}

	@FXML
	void btnBuscarOnAction(ActionEvent event) {

		if (getClient() != null) {
			insertClient();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		pm = PersistenceManager.getPersistenceInstace();
		initClientList();

	}

	public void initClientList() {

		ArrayList<String> clientsName = pm.getClientsName();
		TextFields.bindAutoCompletion(txtBuscar, clientsName);
	}

	private void insertClient() {
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource(Paths.VIEWS_PATH + "InfoCliente.fxml"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		pnlContainer.setCenter(root);
	}

	public ClientesInfoPersonal getClient() {

		pm = new PersistenceManager();
		return pm.getClient(Clientes.getClientCI(txtBuscar.getText()));
	}

	public static ClientesController getInstance() {

		return instance;
	}

	public Button getAddClientButton() {
		return btnAddClient;
	}
}
