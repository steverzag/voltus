package com.softit.voltus.app.controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ResourceBundle;

import com.softit.voltus.app.model.CuadreMensual;
import com.softit.voltus.app.model.PersistenceManager;
import com.softit.voltus.app.model.Rutas;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

public class CuadreMensualBoardController implements Initializable {

	@FXML
	private Label lblFecha;

	@FXML
	private PieChart pieIngresos;

	@FXML
	private Label lblDiasH;

	@FXML
	private Label lblClientesAct;

	@FXML
	private Label lblHorario1;

	@FXML
	private Label lblHorario2;

	@FXML
	private Label lblHorario3;

	@FXML
	private Button btnVerPDF;

	private static CuadreMensualBoardController INSTANCE;
	private CuadreMensual cuadre;
	private PersistenceManager pm = PersistenceManager.getPersistenceInstace();

	@FXML
	void btnVerPDFOnAction(ActionEvent event) {

		try {
			File pathFinanzas = new File(
					cuadre.getPdfPath() + File.separatorChar + cuadre.getId() + " - Finanzas.pdf");
			File pathActividad = new File(
					cuadre.getPdfPath() + File.separatorChar + cuadre.getId() + " - Activity.pdf");

			if (pathActividad.exists())
				Desktop.getDesktop().open(pathActividad);
			if (pathFinanzas.exists())
				Desktop.getDesktop().open(pathFinanzas);
			else {
				Dialogs dialogs = new Dialogs();
				int desition = dialogs.displayYesNoCancelChoice("El Archivo no Existe o Cambio su Ruta. Desea Proporcionarle Una?", "icons8_nothing_found_80px.png");
				if(desition == Dialogs.YES) {
					setPDFDir();
				}
			}

		} catch (IOException ex) {

		}

	}
	
	private void setPDFDir() {

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
		Window w = lblClientesAct.getScene().getWindow();

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
		cuadre.setPdfPath(path);
		pm.updateEntity(ruta);
		pm.updateEntity(cuadre);
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public CuadreMensualBoardController() {
		INSTANCE = this;
	}

	public void setCuadre(CuadreMensual cuadre) {
		this.cuadre = cuadre;
		init();
	}

	private void init() {
		DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);

		lblFecha.setText(lblFecha.getText() + df.format(cuadre.getFechaCreacion()));
		lblDiasH.setText(lblDiasH.getText() + cuadre.getDiasHabiles());
		lblClientesAct.setText(lblClientesAct.getText() + cuadre.getActividad().getClientesActivos());
		String h1 = cuadre.getActividad().getHorarioConcurrido1() + " - "
				+ ((int) cuadre.getActividad().getClientesHorario1() / cuadre.getDiasHabiles()) + " clientes (prom.)";
		String h2 = cuadre.getActividad().getHorarioConcurrido2() + " - "
				+ ((int) cuadre.getActividad().getClientesHorario2() / cuadre.getDiasHabiles()) + " clientes (prom.)";
		String h3 = cuadre.getActividad().getHorarioConcurrido3() + " - "
				+ ((int) cuadre.getActividad().getClientesHorario3() / cuadre.getDiasHabiles()) + " clientes (prom.)";
		lblHorario1.setText(h1);
		lblHorario2.setText(h2);
		lblHorario3.setText(h3);

		double ingArt = cuadre.getFinansas().getIngresosPorArticulos();
		double ingServ = cuadre.getFinansas().getIngresosPorServicios();
		double gastos = cuadre.getFinansas().getGastos();
		ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
				new PieChart.Data("Gastos: " + gastos, gastos),
				new PieChart.Data("Ing. por Articulos: " + ingArt, ingArt),
				new PieChart.Data("Ing. por Servicios: " + ingServ, ingServ));
		pieIngresos.setStartAngle(90);
		pieIngresos.setData(pieData);
	}

	public static CuadreMensualBoardController getInstance() {
		return INSTANCE;
	}
}
