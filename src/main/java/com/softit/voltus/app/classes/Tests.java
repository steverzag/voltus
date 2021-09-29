package com.softit.voltus.app.classes;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.UIManager;

import com.softit.voltus.app.model.Asistencia;
import com.softit.voltus.app.model.Operaciones;
import com.softit.voltus.app.model.SqLiteConnection;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class Tests extends Application{

	public static void main(String[] args) {
		
		launch(args);
	}

	public void test1() {
		
		
		
	}
	private void test2() {
		
		SqLiteConnection sqlite = SqLiteConnection.getConnectionInstance("voltusDB.db");
		Reports r = new Reports();
 
		List<Operaciones> gastos = new ArrayList<>();
		List<Operaciones> salarios = new ArrayList<>();

	
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
		parameters.put("dateDesde", (Long) 1598932800000L);
		parameters.put("dateHasta", (Long) 1601524800000L);
		parameters.put("diasHabiles", (Integer) 26);
		parameters.put("reportsDir", "reports" + File.separatorChar);
		parameters.put("IngresosMemb", (Double) 15356.0);
		parameters.put("ingresosArts", (Double) 426.0);
		parameters.put("dsSalarios", dsSalarios);
		parameters.put("dsGastos", dsGastos);
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			r.createReport("reports\\VoltusFinanazasReport.jasper", parameters, sqlite.getConnection());
			r.viewReport();
			r.createReport("reports\\VoltusParcialActivutyReport.jasper", parameters, sqlite.getConnection());
			r.viewReport();
		} catch (JRException e) {
		}catch (Exception e) {
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		List<Asistencia> list = new ArrayList<>();
		
		list.add(new Asistencia("93022010981", new Date(), new Date(), "Gimnacio", "Activo"));
		list.add(new Asistencia("93022890981", new Date(), new Date(), "Aerobios", "Activo"));
		ObservableList<Asistencia> l = FXCollections.observableArrayList(list);

		TableView<Asistencia> tblAsistencia = new TableView<>();
		TableColumn<Asistencia, String> entrada = new TableColumn<>("Entrada");
		TableColumn<Asistencia, String> salida = new TableColumn<>("Salida");
		entrada.setCellValueFactory(new PropertyValueFactory<>("entradaString"));
		salida.setCellValueFactory(new PropertyValueFactory<>("salidaString"));

		tblAsistencia.setItems(l);
		tblAsistencia.getColumns().addAll(entrada, salida);
		tblAsistencia.setPrefHeight(80);
		tblAsistencia.setPrefWidth(200);
		
		VBox box = new VBox();
		box.getChildren().add(tblAsistencia);
		Scene scene = new Scene(box);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
