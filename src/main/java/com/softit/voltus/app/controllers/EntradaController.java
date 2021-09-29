package com.softit.voltus.app.controllers;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

import com.softit.voltus.app.model.Asistencia;
import com.softit.voltus.app.model.Caja;
import com.softit.voltus.app.model.ClientesEnGym;
import com.softit.voltus.app.model.ClientesEstado;
import com.softit.voltus.app.model.ClientesInfoPersonal;
import com.softit.voltus.app.model.Operaciones;
import com.softit.voltus.app.model.OperacionesCompartidas;
import com.softit.voltus.app.model.PersistenceManager;
import com.softit.voltus.app.model.Servicios;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class EntradaController implements Initializable {
	@FXML
	private StackPane pnlStack;

	@FXML
	private AnchorPane pnlEntrada;

	@FXML
	private Button btnEntrada;

	@FXML
	private TextField txtBuscar;

	@FXML
	private Button btnBuscar;

	@FXML
	private ImageView imgFoto;

	@FXML
	private TableView<ClientesEstado> tblServicios;

	@FXML
	private TableColumn<ClientesEstado, String> colServicio;

	@FXML
	private TableColumn<ClientesEstado, String> colPago;

	@FXML
	private TableColumn<ClientesEstado, String> colActivo;

	@FXML
	private ComboBox<String> cbxPago;

	@FXML
	private Button btnMemb;

	@FXML
	private Label lblCash;

	@FXML
	private Label lblInfo;

	@FXML
	private Label lblDetails;

	private PersistenceManager pm = PersistenceManager.getPersistenceInstace();
	private ClientesInfoPersonal client;
	private static EntradaController INSTANCE;

	@FXML
	void btnBuscarOnAction(ActionEvent event) {
		clear(false);
		client = pm.getClient(getClientCI());
		initClient();
	}

	@FXML
	void txtBuscarOnAction(ActionEvent event) {
		client = pm.getClient(getClientCI());
		initClient();
	}

	@FXML
	void btnMembOnAction(ActionEvent event) {

		if (!isItErros()) {
			cobrarMembresia();
			tblServicios.refresh();
		}
	}

	@FXML
	void btnEntradaOnAction(ActionEvent event) {

		if (!isItAsistenciaError()) {
			darAsistencia();
			clear(true);
		}
	}

	@FXML
	void cbxPagoOnAction(ActionEvent event) {

		try {
			ClientesEstado state = tblServicios.getSelectionModel().getSelectedItem();
			Servicios service = pm.getServicio(state.getServicio());
			String fPago = cbxPago.getSelectionModel().getSelectedItem();
			lblCash.setText("$ " + service.getvalorServicio(fPago));
		} catch (Exception e) {
		}
	}

	@FXML
	void tblServiciosOnClicked(MouseEvent event) {

		if (tblServicios.getSelectionModel().getSelectedItem() != null) {
			ClientesEstado state = tblServicios.getSelectionModel().getSelectedItem();
			Servicios service = pm.getServicio(state.getServicio());

			cbxPago.setDisable(false);
			btnEntrada.setDisable(false);
			btnMemb.setDisable(false);
			lblInfo.setVisible(false);
			lblDetails.setVisible(false);

			cbxPago.getItems().removeAll(cbxPago.getItems());
			cbxPago.getItems().addAll(service.getFormasPago());
			cbxPago.getSelectionModel().select(state.getFpago());

			lblCash.setText("$ " + service.getvalorServicio(cbxPago.getSelectionModel().getSelectedItem()));
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		TextFields.bindAutoCompletion(txtBuscar, pm.getClientsName());

	}

	public EntradaController() {
		INSTANCE = this;
	}

	private String getClientCI() {
		String clientName = txtBuscar.getText();
		return clientName.substring(clientName.indexOf('-') + 1);
	}

	private void initClient() {

		if (client != null) {
			ObservableList<ClientesEstado> states = FXCollections.observableArrayList();
			states.addAll(client.getClientesEstado());

			colServicio.setCellValueFactory(new PropertyValueFactory<>("servicio"));
			colPago.setCellValueFactory(new PropertyValueFactory<>("fpago"));
			colActivo.setCellValueFactory(new PropertyValueFactory<>("activostring"));
			tblServicios.setItems(states);
			try {
				imgFoto.setImage(new Image(client.getImgUrl()));
				String s = new URL(client.getImgUrl()).toURI().getPath();
				if (!new File(s).exists())
					imgFoto.setImage(new Image("com/softit/voltus/app/view/images/default-user.jpg"));
			} catch (Exception e) {
				imgFoto.setImage(new Image("com/softit/voltus/app/view/images/default-user.jpg"));
			}
		}
	}

	private void cobrarMembresia() {

		ClientesEstado state = tblServicios.getSelectionModel().getSelectedItem();
		List<Asistencia> list = pm.getEntradasSinCreditoList(client.getCi(), state.getServicio(), state.getPagoHasta());
		state.getPayd(cbxPago.getValue());
		Servicios service = pm.getServicio(state.getServicio());

		String id = Operaciones.COBRO_MEMB;
		String user = LogInController.getInstance().getctiveUser().getUsuario();
		double valor = service.getvalorServicio(cbxPago.getValue());
		double valorComp = service.getvalorServicioComp(cbxPago.getValue());
		String observacion = state.getServicio() + " - " + cbxPago.getValue();
		Date fecha = new Date();
		Operaciones op = new Operaciones(id, user, valor, client.getCi(), observacion, fecha, false);

		if(valorComp > 0)
			pm.addEntity(new OperacionesCompartidas(state.getServicio(), valorComp, valor, fecha));
		Caja c = pm.getCaja();
		c.setSaldo(op.getOperationCash(c.getSaldo()));
		c.getSaldo();

		tblServicios.refresh();
		pm.addEntity(op);
		pm.updateEntity(state);
		VoltusSysController.getInstance().checkForNotificaciones();
		pm.updateEntity(c);
		VoltusSysController.getInstance().setCash();

		if (list.size() > 0) {
			lblDetails.setVisible(true);
			lblDetails.setOnMouseClicked(e -> initDetailsAlert(list));
		} else {
			lblDetails.setVisible(false);
		}
		lblInfo.setVisible(true);
		lblInfo.setText("Membresia Valida Hasta " + state.getPagoHastaString());

	}

	@SuppressWarnings("unchecked")
	private void initDetailsAlert(List<Asistencia> list) {

		ObservableList<Asistencia> l = FXCollections.observableArrayList(list);

		TableView<Asistencia> tblAsistencia = new TableView<>();
		TableColumn<Asistencia, String> entrada = new TableColumn<>("Entrada");
		TableColumn<Asistencia, String> salida = new TableColumn<>("Salida");
		entrada.setCellValueFactory(new PropertyValueFactory<>("ci"));
		salida.setCellValueFactory(new PropertyValueFactory<>("salidaString"));

		tblAsistencia.setItems(l);
		tblAsistencia.getColumns().addAll(entrada, salida);
		tblAsistencia.setPrefHeight(80);
		tblAsistencia.setPrefWidth(200);

		Dialogs d = new Dialogs();
		String message = "Este Cliente ha Ingresado " + l.size() + " Veces sin Credito Desde su Ultimo Pago.";

		if (l.size() == 1)
			message = message.replace("Veces", "Vez");
		
		d.displayAlertWithDetails(message, "icons8_to_do_80px.png", "Asistencias sin Credito", tblAsistencia);
	}

	private boolean isItErros() {

		boolean error = false;
		return error;
	}

	private void darAsistencia() {

		String ci = client.getCi();
		String nombre = client.getNombre();
		Date entrada = new Date();
		String imgurl = client.getImgUrl();
		ClientesEstado state = tblServicios.getSelectionModel().getSelectedItem();
		String servicio = state.getServicio();
		String estado = state.getActivostring();

		Asistencia asist = new Asistencia(ci, entrada, null, servicio, estado);
		ClientesEnGym ClinetIn = new ClientesEnGym(ci, nombre, imgurl, entrada, servicio, estado);

		pm.addEntity(asist);
		pm.addEntity(ClinetIn);

	}

	private boolean isItAsistenciaError() {

		for (ClientesEnGym c : pm.getClientesEnGymList()) {
			if (c.getCi().equals(client.getCi())) {
				Dialogs dialogs = new Dialogs();
				dialogs.displayAlert("Este Cliente ya se Encuentra en el Gimnacio", "icons8_clone_80px.png");
				return true;
			}
		}
		ClientesEstado state = tblServicios.getSelectionModel().getSelectedItem();

		if (!state.getActivostring().equals("Activo")) {
			Dialogs dialogs = new Dialogs();
			return dialogs.displayChoice("Membresia Expirada, Desea Darle Entrada Igualmente?",
					"icons8_no_cash_80px.png");
		}
		return false;
	}

	public static EntradaController getInstance() {
		return INSTANCE;
	}

	public void clear(boolean removeName) {

		if (removeName == true)
			txtBuscar.setText("");
		tblServicios.getItems().removeAll(tblServicios.getItems());
		imgFoto.setImage(null);
		cbxPago.getItems().removeAll(cbxPago.getItems());
		cbxPago.setPromptText("Forma de Pago");
		client = null;
		lblCash.setText("");
		cbxPago.setDisable(true);
		btnEntrada.setDisable(true);
		btnMemb.setDisable(true);
		lblInfo.setVisible(false);
		lblDetails.setVisible(false);
	}
}
