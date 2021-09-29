package com.softit.voltus.app.controllers;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.UIManager;

import com.softit.voltus.app.classes.Fechas;
import com.softit.voltus.app.classes.Reports;
import com.softit.voltus.app.model.CuadreMensual;
import com.softit.voltus.app.model.Operaciones;
import com.softit.voltus.app.model.PersistenceManager;
import com.softit.voltus.app.model.Rutas;
import com.softit.voltus.app.model.SqLiteConnection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;
import javafx.util.converter.DoubleStringConverter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class CierreGeneratorController implements Initializable {

	@FXML
	private TableView<Operaciones> tblGastos;

	@FXML
	private TableColumn<Operaciones, Double> colValorGastos;

	@FXML
	private TableColumn<Operaciones, String> colObsGastos;

	@FXML
	private TableView<Operaciones> tblSalarios;

	@FXML
	private TableColumn<Operaciones, String> colAsalariado;

	@FXML
	private TableColumn<Operaciones, Double> colValorSalario;

	@FXML
	private Label lblGastos;

	@FXML
	private Button btnAceptar;

	@FXML
	private Button btnVista;

	@FXML
	private Button btnAddSalario;

	@FXML
	private Button btnAddGasto;

	@FXML
	private TextField txtAsalariado;

	@FXML
	private TextField txtUbicacion;

	@FXML
	private TextField txtObs;
	
	@FXML
	private ImageView imgGif;

	private static CierreGeneratorController INSTANCE;
	private PersistenceManager pm = PersistenceManager.getPersistenceInstace();
	private CuadreMensual cuadre;
	private int month;
	private int year;

	@FXML
	void btnAceptarOnAction(ActionEvent event) {

		
		if (saveReports()) {
			updateAndSaveCuadre();
			pm.clearDB(month, year);
			EstadisticasController.getInstance().initialize(null, null);
			VoltusSysController.getInstance().checkForCierres();
		}
	}

	@FXML
	void btnVistaOnAction(ActionEvent event) {

		SqLiteConnection sqlite = SqLiteConnection.getConnectionInstance("voltusDB.db");
		Reports r = new Reports();
		Map<String, Object> parameters = getReportParameters();
 
		//imgGif.setVisible(true);
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			r.createReport("reports\\VoltusFinanazasReport.jasper", parameters, sqlite.getConnection());
			r.viewReport();
			if(wasPastMonth()) {
				r.createReport("reports\\VoltusActivityReport.jasper", parameters, sqlite.getConnection());
				r.viewReport();
			}else {
				r.createReport("reports\\VoltusParcialActivutyReport.jasper", parameters, sqlite.getConnection());
				r.viewReport();
			}
		} catch (JRException e) {
		}catch (Exception e) {
		}
		//imgGif.setVisible(false);
	}

	@FXML
	void btnAddGastoOnAction(ActionEvent event) {
		Operaciones op = new Operaciones();
		op.setObservacion(txtObs.getText());
			tblGastos.getItems().add(op);
			tblGastos.refresh();
			txtObs.setText("");
		
	}

	@FXML
	void btnDelGastoOnAction(ActionEvent event) {
		tblGastos.getItems().remove(tblGastos.getSelectionModel().getSelectedIndex());
		tblGastos.refresh();
	}

	@FXML
	void btnAddSalarioOnAction(ActionEvent event) {
		Operaciones op = new Operaciones();
		op.setObservacion(txtAsalariado.getText());
			tblSalarios.getItems().add(op);
			tblSalarios.refresh();
			txtAsalariado.setText("");
		
	}

	@FXML
	void btnDelSalarioOnAction(ActionEvent event) {
		tblSalarios.getItems().remove(tblSalarios.getSelectionModel().getSelectedIndex());
		tblSalarios.refresh();
	}

	@FXML
	void btnUbicacionOnAction(ActionEvent event) {
		setSaveDir();
	}

	@FXML
	void txtUbicacinoOnTyped(KeyEvent event) {
		txtUbicacion.getStyleClass().remove("text-error");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initTables();
		txtUbicacion.setText(pm.getRuta("pdf").getPath());
	}

	public CierreGeneratorController() {
		INSTANCE = this;
	}

	private void initTables() {
		ObservableList<Operaciones> gastos = FXCollections.observableArrayList();
		ObservableList<Operaciones> salarios = FXCollections.observableArrayList();
		colAsalariado.setCellValueFactory(new PropertyValueFactory<>("observacion"));
		colValorSalario.setCellValueFactory(new PropertyValueFactory<>("valor"));
		colObsGastos.setCellValueFactory(new PropertyValueFactory<>("observacion"));
		colValorGastos.setCellValueFactory(new PropertyValueFactory<>("valor"));

		tblSalarios.setItems(salarios);
		tblGastos.setItems(gastos);
		tblSalarios.setEditable(true);
		tblGastos.setEditable(true);

		doubleTextCellFactory(colValorGastos);
		doubleTextCellFactory(colValorSalario);
	}

	private void doubleTextCellFactory(TableColumn<Operaciones, Double> column) {

		column.setCellFactory(TextFieldTableCell.<Operaciones, Double>forTableColumn(new DoubleStringConverter() {
			@Override
			public Double fromString(String value) {

				try {
					return Double.parseDouble(value);
				} catch (NumberFormatException e) {
					return 0.0;
				}
			}
		}));
		column.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Operaciones, Double>>() {

			@Override
			public void handle(CellEditEvent<Operaciones, Double> event) {
				double d = 0;
				try {
					d = event.getNewValue();
				} catch (Exception e) {
					d = event.getOldValue();
				}
				event.getRowValue().setValor(d);
			}
		});
	}

	public static CierreGeneratorController getInstance() {

		return INSTANCE;
	}

	public void setCuadre(CuadreMensual cuadre) {

		this.cuadre = cuadre;
		String s = cuadre.getId().replaceAll(" de ", "").replaceAll("[^\\sa-zA-Z]", "");
		this.year = Integer.parseInt(cuadre.getId().replaceAll("[^\\d]", ""));
		this.month = Fechas.getMonth(s);

	}

	private boolean saveReports() {

		Map<String, Object> parameters = getReportParameters();
		SqLiteConnection sqlite = SqLiteConnection.getConnectionInstance("voltusDB.db");
		Reports r = new Reports();

		String path = txtUbicacion.getText();
		try {
			File f = new File(path);
			if (!f.exists()) {
				txtUbicacion.getStyleClass().add("text-error");
				return false;
			}
		} catch (Exception e) {
			txtUbicacion.getStyleClass().add("text-error");
			return false;
		}
		

		//imgGif.setVisible(true);
		try {
			r.createReport("reports" + File.separatorChar + "VoltusFinanazasReport.jasper", parameters,
					sqlite.getConnection());
			r.exportReport(path + File.separatorChar
					+ Fechas.getMonthYearString(new GregorianCalendar(year, month, 1).getTime()) + " - Finanzas.pdf");
			if (wasPastMonth()) {
				r.createReport("reports" + File.separatorChar + "VoltusActivityReport.jasper", parameters,
						sqlite.getConnection());
				r.exportReport(path + File.separatorChar
						+ Fechas.getMonthYearString(new GregorianCalendar(year, month, 1).getTime())
						+ " - Actividad.pdf");
			}else {
				r.createReport("reports" + File.separatorChar + "VoltusParcialActivutyReport.jasper", parameters, sqlite.getConnection());
				r.exportReport(path + File.separatorChar
						+ Fechas.getMonthYearString(new GregorianCalendar(year, month, 1).getTime())
						+ " - Actividad.pdf");
			}
		} catch (JRException e) {
			Dialogs dialogs = new Dialogs();
			dialogs.displayAlert("Algo ha Ido Mal, por Favor Intentelo Mas Tarde.", "icons8_error_80px.png");
			return false;
		}

		cuadre.setPdfPath(txtUbicacion.getText());
		//imgGif.setVisible(false);
		return true;
	}

	private boolean wasPastMonth() {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(Fechas.restTime(new Date(), Calendar.MONTH, 1));
		int mes = gc.get(Calendar.MONTH);
		int ano = gc.get(Calendar.YEAR);
		if (month == mes && year == ano)
			return true;
		return false;
	}

	private void updateAndSaveCuadre() {

		double gastos = cuadre.getFinansas().getGastos();

		for (Operaciones op : tblSalarios.getItems()) {
			gastos += op.getValor();
		}
		for (Operaciones op : tblGastos.getItems()) {
			gastos += op.getValor();
		}

		cuadre.getFinansas().setGastos(gastos);
		if (!wasPastMonth()) {
			cuadre.getActividad().setClientesActivos(0);
			cuadre.getActividad().setClientesInactivos(0);
		}
		pm.addEntity(cuadre);
	}

	private Map<String, Object> getReportParameters() {

		GregorianCalendar desde = new GregorianCalendar(year, month, 1);
		GregorianCalendar hasta = new GregorianCalendar(year, month + 1, 1);

		List<Operaciones> gastos = new ArrayList<>();
		List<Operaciones> salarios = new ArrayList<>();

		tblGastos.getItems().forEach(item -> {
			gastos.add(item);
		});
		gastos.addAll(pm.getOperacionesListAt(month, year, Operaciones.GASTO));
		tblSalarios.getItems().forEach(item -> {
			salarios.add(item);
		});

		if (salarios.size() == 0) {
			Operaciones op = new Operaciones();
			op.setObservacion("");
			salarios.add(op);
		}

		if (gastos.size() == 0) {
			Operaciones op = new Operaciones();
			op.setObservacion("");
			gastos.add(op);
		}
		
		JRBeanCollectionDataSource dsSalarios = new JRBeanCollectionDataSource(salarios);
		JRBeanCollectionDataSource dsGastos = new JRBeanCollectionDataSource(gastos);
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("dateDesde", (Long) desde.getTimeInMillis());
		parameters.put("dateHasta", (Long) hasta.getTimeInMillis());
		parameters.put("diasHabiles", pm.getDiasHabilesAt(month, year));
		parameters.put("reportsDir", "reports" + File.separatorChar);
		parameters.put("IngresosMemb", (Double) cuadre.getFinansas().getIngresosPorServicios());
		parameters.put("ingresosArts", (Double) cuadre.getFinansas().getIngresosPorArticulos());
		parameters.put("dsSalarios", dsSalarios);
		parameters.put("dsGastos", dsGastos);
		parameters.put("totalAsistencias", (Integer) pm.getSumAsistenciasAt(month, year));

		return parameters;
	}

	private void setSaveDir() {

		DirectoryChooser file = new DirectoryChooser();
		Rutas ruta = pm.getRuta("pdf");
		String path = ruta.getPath();
		File f = null;
		try {
			f = new File(path);
			if (f.exists())
				file.setInitialDirectory(f);
		} catch (Exception e) {
		}
		Window w = lblGastos.getScene().getWindow();

		try {
			f = file.showDialog(w).getAbsoluteFile();
		} catch (Exception e) {
			return;
		}

		String url = "";
		try {
			url = f.toURI().toURL().toExternalForm();
			path = f.getAbsolutePath();
			ruta.setPath(path);
			ruta.setUrl(url);
		} catch (MalformedURLException e) {
		} catch (SecurityException e) {
		}
		pm.updateEntity(ruta);
		txtUbicacion.setText(path);
		txtUbicacion.getStyleClass().remove("text-error");
	}
}
