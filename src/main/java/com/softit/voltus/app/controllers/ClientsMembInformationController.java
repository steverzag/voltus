package com.softit.voltus.app.controllers;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.softit.voltus.app.model.ClientesEstado;
import com.softit.voltus.app.model.ClientesInfoPersonal;
import com.softit.voltus.app.model.PersistenceManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class ClientsMembInformationController implements Initializable {

	@FXML
	private TableView<ClientesInfoPersonal> tblUsers;

	@FXML
	private TableColumn<ClientesInfoPersonal, String> colClient;

	@FXML
	private TableColumn<ClientesInfoPersonal, Integer> colClientMembs;

	@FXML
	private TableColumn<ClientesInfoPersonal, Integer> colMembActs;

	@FXML
	private TableColumn<ClientesInfoPersonal, String> colEstado;

	@FXML
	private TableView<ClientesEstado> tblMembs;

	@FXML
	private TableColumn<ClientesEstado, String> colMemb;

	@FXML
	private TableColumn<ClientesEstado, String> colMembEstado;

	@FXML
	private TableColumn<ClientesEstado, String> colObserb;
	
    @FXML
    private Label lblTotalClients;

    @FXML
    private Label lblClientsActs;

	private PersistenceManager pm = PersistenceManager.getPersistenceInstace();

	@FXML
	void btnDelMembOnAction(ActionEvent event) {

		ClientesEstado state = tblMembs.getSelectionModel().getSelectedItem();
		if(state != null) {
		Dialogs dialog = new Dialogs();
		int desition = dialog.displayYesNoCancelChoice("Seguro Desea Remover Esta Membresia?",
				"icons8_remove_80px_1.png");
		if (desition == Dialogs.YES) {
			ClientesInfoPersonal client = state.getClient();
			client.removeClientesEstado(state);
			pm.updateEntity(client);
			tblMembs.getItems().remove(state);
			tblMembs.refresh();
		}
		}
	}

	@FXML
	void btnDelUserOnAction(ActionEvent event) {
		ClientesInfoPersonal client = tblUsers.getSelectionModel().getSelectedItem();
		if(client != null) {
		Dialogs dialog = new Dialogs();
		int desition = dialog.displayYesNoCancelChoice("Seguro Desea Eliminar Este Cliente?",
				"icons8_remove_80px_1.png");
		if (desition == Dialogs.YES) {
			eliminarFoto(client.getImgUrl());
			pm.removeEntity(client);
			tblUsers.getItems().remove(client);
			tblUsers.refresh();
			
		}
		}
	}
	
	@FXML
	void tblUsersOnClicked(MouseEvent event) {
		List<ClientesEstado> states = null;
		try {
		states = tblUsers.getSelectionModel().getSelectedItem().getClientesEstado();
		}catch(Exception e) {
			return;
		}
		initTableMembs(states);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		initTableUsers();
		initLabels();
	}

	private void initLabels() {
		int clientesActivos = pm.getClientsActives();
		lblTotalClients.setText("Total de Clientes:  " + tblUsers.getItems().size());
		lblClientsActs.setText("Clientes Activos: " + clientesActivos);
	}

	private void initTableUsers() {
		
		ObservableList<ClientesInfoPersonal> clients = FXCollections.observableArrayList(pm.getClientsList());
		colClient.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		colClientMembs.setCellValueFactory(new PropertyValueFactory<>("servicesTotal"));
		colMembActs.setCellValueFactory(new PropertyValueFactory<>("activeServices"));
		colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
		
		tblUsers.setItems(clients);
	}
	
	private void initTableMembs(List<ClientesEstado> states) {
		
		ObservableList<ClientesEstado> list = FXCollections.observableArrayList(states);
		colMemb.setCellValueFactory(new PropertyValueFactory<>("membresia"));
		colMembEstado.setCellValueFactory(new PropertyValueFactory<>("activostring"));
		colObserb.setCellValueFactory(new PropertyValueFactory<>("pagoHastaString"));
		tblMembs.setItems(list);
	}

	private void eliminarFoto(String imgUrl) {
		String path = "";
		try {
			path = new URL(imgUrl).toURI().getPath();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File f = new File(path);
		if(f.exists())
			f.delete();
	}
}
