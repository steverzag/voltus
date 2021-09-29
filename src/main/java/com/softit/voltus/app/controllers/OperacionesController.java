package com.softit.voltus.app.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

import com.softit.voltus.app.classes.Clientes;
import com.softit.voltus.app.classes.Fechas;
import com.softit.voltus.app.model.Articulos;
import com.softit.voltus.app.model.ArticulosAlquilados;
import com.softit.voltus.app.model.Caja;
import com.softit.voltus.app.model.ClientesEnGym;
import com.softit.voltus.app.model.ClientesEstado;
import com.softit.voltus.app.model.Operaciones;
import com.softit.voltus.app.model.OperacionesCompartidas;
import com.softit.voltus.app.model.PersistenceManager;
import com.softit.voltus.app.model.Servicios;
import com.softit.voltus.app.model.Usuario;

import animatefx.animation.ZoomIn;
import animatefx.animation.ZoomOut;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class OperacionesController implements Initializable {

	@FXML
	private TableView<Operaciones> tblOperaciones;

	@FXML
	private TableColumn<Operaciones, String> colOps;

	@FXML
	private TableColumn<Operaciones, String> colUsers;

	@FXML
	private TableColumn<Operaciones, Double> colVal;

	@FXML
	private TableColumn<Operaciones, String> colClient;

	@FXML
	private TableColumn<Operaciones, String> colObs;

	@FXML
	private TableColumn<Operaciones, String> colDate;

	@FXML
	private TableColumn<Operaciones, String> colCanc;

	@FXML
	private TextField txtBuscar;

	@FXML
	private ComboBox<String> cbxUsuario;

	@FXML
	private ComboBox<String> cbxOperacion;

	@FXML
	private DatePicker dpkDesde;

	@FXML
	private DatePicker dpkHasta;

	@FXML
	private Button btnFiltrar;

	@FXML
	private Button btnEditarOp;
	
	@FXML
	private Label lblError;

	@FXML
	void btnEditarOpOnAction(ActionEvent event) {

		Operaciones op = tblOperaciones.getSelectionModel().getSelectedItem();
		if (!isItErrors(op))
			commitOperacion(op);

	}

	private boolean isItErrors(Operaciones op) {

		double saldo = pm.getCaja().getSaldo();
		if (op.isCancelada())
			saldo = op.getOperationCash(saldo);
		else
			saldo = op.getCancelOperationCash(saldo);
		if (saldo < 0) {
			lblError.setVisible(true);
			new ZoomIn(lblError).play();
			new ZoomOut(lblError).setDelay(new Duration(2000)).play();
			return true;
		}
		return false;
	}

	private void commitOperacion(Operaciones op) {

		boolean b = !op.isCancelada();
		lblError.setVisible(false);
		op.setCancelada(b);
		pm.updateEntity(op);
		tblOperaciones.refresh();

		if (op.getId().equals(Operaciones.ALQUILER_ART) && Fechas.isToDay(op.getFecha()))
			controlarArticulo(op); 
		if (op.getId().equals(Operaciones.COBRO_MEMB)) {
			controlarMembresia(op);
			commitOperacionComp(op.getFecha(), b);
		}

		Caja caja = pm.getCaja();
		if (!op.isCancelada())
			caja.setSaldo(op.getOperationCash(caja.getSaldo()));
		else
			caja.setSaldo(op.getCancelOperationCash(caja.getSaldo()));
		pm.updateEntity(caja);
		VoltusSysController.getInstance().setCash();
		

	}

	private void commitOperacionComp(Date fecha, boolean b) {
		
		OperacionesCompartidas oc = pm.getOperacionCompartida(fecha);
		if(oc != null){
			oc.setCancelada(b);
			pm.updateEntity(oc);
		}
	}

	private void controlarMembresia(Operaciones op) {

		try {
			String ci = op.getCliente();
			String servicio = op.getObservacion();
			ClientesEstado state = pm.getCLientesEstado(ci, servicio);
			Servicios service = pm.getServicio(servicio);
			String fPago = service.getFPago(op.getValor());
			state.getUnPayd(fPago);
			pm.updateEntity(state);
			VoltusSysController.getInstance().checkForNotificaciones();
		} catch (Exception e) {

		}
	}

	private void controlarArticulo(Operaciones op) {

		ClientesEnGym client = pm.getClientesEnGym(op.getCliente());

		if(client != null) {
		int length = 0;
		try {
			length = client.getArticulos().size();
		} catch (Exception e) {
		}

		for (int i = length; i > 0; i--) {
			ArticulosAlquilados art = client.getArticulos().get(i - 1);
			if (art.getArticulo().equals(op.getObservacion()) && op.isCancelada())
				client.removeArticulo(art);
		}
		if (!op.isCancelada()) {
			try {
				ArticulosAlquilados articuloAlq = new ArticulosAlquilados(op.getObservacion(), op.getCliente(), 0);
				Articulos articulo = pm.getArticulos(op.getObservacion());
				int cantidad = (int) (op.getValor() / articulo.getPrecio());
				articuloAlq.setCantidad(cantidad);
				client.addArticulo(articuloAlq);
			} catch (Exception e) {
			}
		}
		pm.updateEntity(client);
		}
	}

	@FXML
	void btnFiltrarOAction(ActionEvent event) {

		if (pm.getClient(Clientes.getClientCI(txtBuscar.getText())) == null) {
			txtBuscar.setText("");
		}
		String client = Clientes.getClientCI(txtBuscar.getText());
		String user = (cbxUsuario.getValue() == null) ? "" : cbxUsuario.getValue();
		String op = (cbxOperacion.getValue() == null) ? "" : cbxOperacion.getValue();

		Date desde = (dpkDesde.getValue() != null) ? parseDate(dpkDesde.getValue()) : null;
		Date hasta = (dpkHasta.getValue() != null) ? parseDate(dpkHasta.getValue()) : null;

		List<Operaciones> list = pm.getOperacionesList(client, user, op, desde, hasta);
		setUpTable(list);
	}

	@FXML
	void tblOperacionesOnClicked(MouseEvent event) {
		if (true)
			btnEditarOp.setDisable(false);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		List<Operaciones> list = pm.getOperacionesList();
		setUpTable(list);
		initControls();

	}

	private Date parseDate(LocalDate local) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.set(local.getYear(), local.getMonthValue() - 1, local.getDayOfMonth());

		return new Date(gc.getTimeInMillis());
	}

	private PersistenceManager pm = PersistenceManager.getPersistenceInstace();

	private void initControls() {

		TextFields.bindAutoCompletion(txtBuscar, pm.getClientsName());
		List<Usuario> user = pm.getUsuariosList();
		cbxUsuario.getItems().add("");
		user.forEach(u -> {
			cbxUsuario.getItems().add(u.getUsuario());
		});

		HashSet<String> opers = pm.getOperacinoesIdSet();
		cbxOperacion.getItems().add("");
		opers.forEach(s -> {
			cbxOperacion.getItems().add(s);
		});
		btnEditarOp.setVisible(LogInController.getInstance().getctiveUser().isAdministrador());
	}

	private void setUpTable(List<Operaciones> list) {

		ObservableList<Operaciones> ops = FXCollections.observableArrayList();
		ops.addAll(list);

		colOps.setCellValueFactory(new PropertyValueFactory<>("id"));
		colUsers.setCellValueFactory(new PropertyValueFactory<>("acceso"));
		colVal.setCellValueFactory(new PropertyValueFactory<>("valor"));
		colClient.setCellValueFactory(new PropertyValueFactory<>("clientName"));
		colObs.setCellValueFactory(new PropertyValueFactory<>("observacion"));
		colDate.setCellValueFactory(new PropertyValueFactory<>("formatFecha"));
		colCanc.setCellValueFactory(new PropertyValueFactory<>("canctostring"));
		tblOperaciones.setItems(ops);

	}
}
