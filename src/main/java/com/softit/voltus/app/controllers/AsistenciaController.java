package com.softit.voltus.app.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

import com.softit.voltus.app.classes.Clientes;
import com.softit.voltus.app.model.Asistencia;
import com.softit.voltus.app.model.PersistenceManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class AsistenciaController implements Initializable{

    @FXML
    private TableView<Asistencia> tblAsistencia;

    @FXML
    private TableColumn<Asistencia, String> colCliente;

    @FXML
    private TableColumn<Asistencia, String> colEntrada;

    @FXML
    private TableColumn<Asistencia, String> colSalida;

    @FXML
    private TableColumn<Asistencia, String> colServicio;

    @FXML
    private TableColumn<Asistencia, String> colEstado;

    @FXML
    private TextField txtBuscar;

    @FXML
    private ComboBox<String> cbxServicio;

    @FXML
    private DatePicker dpkDesde;

    @FXML
    private DatePicker dpkHasta;

    @FXML
    private Button btnFiltrar;

    @FXML
    private ComboBox<String> cbxEstado;
    
    @FXML
    private Tooltip ttpAsistencia;
    
    PersistenceManager pm = PersistenceManager.getPersistenceInstace();

    @FXML
    void btnFiltrarOAction(ActionEvent event) {
    	if (pm.getClient(Clientes.getClientCI(txtBuscar.getText())) == null) {
			txtBuscar.setText("");
		}
		String ci = Clientes.getClientCI(txtBuscar.getText());
		String servicio = (cbxServicio.getValue() != null) ? cbxServicio.getValue() : "";
		String estado = (cbxEstado.getValue() != null) ? cbxEstado.getValue() : "";
    	Date desde = (dpkDesde.getValue() != null) ? parseDate(dpkDesde.getValue()) : null;
		Date hasta = (dpkHasta.getValue() != null) ? parseDate(dpkHasta.getValue()) : null;
		
    	ObservableList<Asistencia> asists = FXCollections.observableArrayList();
		asists.addAll(pm.getAsistenciaList(ci, servicio, estado, desde, hasta));
		initTable(asists);
    }

    @FXML
    void tblAsistenciaOnMouseMoved(MouseEvent event) {
    	
    }
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ObservableList<Asistencia> asists = FXCollections.observableArrayList();
		asists.addAll(pm.getAsistenciaList());
		initTable(asists);
		initComponets();
		//ttpAsistencia.setGraphic(new ImageView("com/softit/voltus/app/view/images/icons8_decision_80px_1.png"));
	}

	private void initComponets() {
		TextFields.bindAutoCompletion(txtBuscar, pm.getClientsName());
		cbxServicio.getItems().add("");
		cbxServicio.getItems().addAll(pm.getServiciosNameList());
		cbxEstado.getItems().add("");
		cbxEstado.getItems().addAll("Activo", "Activo sin Credito", "Inactivo");
	}

	private void initTable(ObservableList<Asistencia> asists) {
		
		colCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
		colEntrada.setCellValueFactory(new PropertyValueFactory<>("entradaString"));
		colSalida.setCellValueFactory(new PropertyValueFactory<>("salidaString"));
		colServicio.setCellValueFactory(new PropertyValueFactory<>("servicio"));
		colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
		
		tblAsistencia.setItems(asists);
	}
	
	private Date parseDate(LocalDate local) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.set(local.getYear(), local.getMonthValue() - 1, local.getDayOfMonth());

		return new Date(gc.getTimeInMillis());
	}

}

