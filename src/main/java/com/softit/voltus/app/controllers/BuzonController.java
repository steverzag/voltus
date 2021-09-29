package com.softit.voltus.app.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.softit.voltus.app.model.Notificaciones;
import com.softit.voltus.app.model.PersistenceManager;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

public class BuzonController implements Initializable {

	@FXML
	private TableView<Notificaciones> tblNotificaciones;

	@FXML
	private TableColumn<Notificaciones, Boolean> colCheck;

	@FXML
	private TableColumn<Notificaciones, String> colCliente;

	@FXML
	private TableColumn<Notificaciones, ImageView> colImg;

	@FXML
	private TableColumn<Notificaciones, String> colNotificacion;

	private PersistenceManager pm = PersistenceManager.getPersistenceInstace();
	private List<Notificaciones> notificaciones = pm.getNotificacionesList();

	@FXML
	private Button btnDel;

	@FXML
	private ComboBox<String> cbxMostrar;

	@FXML
	void cbxMostrarOnAction(ActionEvent event) {
		
			initTable();
	}

	@FXML
	void btnDelOnAction(ActionEvent event) {
		tblNotificaciones.getItems().forEach(i -> {
			pm.updateEntity(i);
		});
		initTable();
		VoltusSysController.getInstance().checkForNotificaciones();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		cbxMostrar.getItems().addAll("Todos", "Leidos", "No Leidos");
		cbxMostrar.getSelectionModel().select("No Leidos");
		initTable();
	}

	private List<Notificaciones> getNotificacionesLeidas(boolean b) {
		ArrayList<Notificaciones> list = new ArrayList<>();
		notificaciones.forEach(not -> {
			if(not.isLeido() == b) {
				list.add(not);
			}
		});
		return list;
	}
	

	private void initTable() {
		ObservableList<Notificaciones> list = FXCollections.observableArrayList();
		
		String s = cbxMostrar.getSelectionModel().getSelectedItem();
		if(s.equals("Leidos"))
			list.addAll(getNotificacionesLeidas(true));
		else if(s.equals("No Leidos"))
			list.addAll(getNotificacionesLeidas(false));
		else
			list.addAll(notificaciones);

		colCliente.setCellValueFactory(new PropertyValueFactory<>("nombreCliente"));
		colImg.setCellValueFactory(new PropertyValueFactory<>("img"));
		colNotificacion.setCellValueFactory(new PropertyValueFactory<>("notificacion"));

		colCheck.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Notificaciones, Boolean>, ObservableValue<Boolean>>() {

					@Override
					public ObservableValue<Boolean> call(CellDataFeatures<Notificaciones, Boolean> param) {
						Notificaciones not = param.getValue();
						SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(not.isLeido());
						booleanProp.addListener(new ChangeListener<Boolean>() {

							@Override
							public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
									Boolean newValue) {
								not.setLeido(newValue);
							}
						});
						return booleanProp;
					}
				});
		colCheck.setCellFactory(
				new Callback<TableColumn<Notificaciones, Boolean>, TableCell<Notificaciones, Boolean>>() {

					@Override
					public TableCell<Notificaciones, Boolean> call(TableColumn<Notificaciones, Boolean> param) {

						CheckBoxTableCell<Notificaciones, Boolean> cell = new CheckBoxTableCell<Notificaciones, Boolean>();
						cell.setAlignment(Pos.CENTER);
						return cell;
					}
				});
		colCheck.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Notificaciones, Boolean>>() {

			@Override
			public void handle(CellEditEvent<Notificaciones, Boolean> event) {
				Notificaciones not = event.getRowValue();
				not.setLeido(event.getNewValue());
			}
		});

		tblNotificaciones.setItems(list);
		tblNotificaciones.setEditable(true);
	}
}
