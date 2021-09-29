package com.softit.voltus.app.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.softit.voltus.app.model.PersistenceManager;
import com.softit.voltus.app.model.Servicios;

import animatefx.animation.AnimationFX;
import animatefx.animation.FadeIn;
import animatefx.animation.FadeOut;
import animatefx.animation.SlideInDown;
import animatefx.animation.SlideOutUp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.util.converter.DoubleStringConverter;

public class ServiciosController implements Initializable {

	@FXML
	private TableView<Servicios> tblServicios;

	@FXML
	private MenuItem itmEliminar;

	@FXML
	private TableColumn<Servicios, String> colServicio;

	@FXML
	private TableColumn<Servicios, Double> colDiario;

	@FXML
	private TableColumn<Servicios, Double> colSemanal;

	@FXML
	private TableColumn<Servicios, Double> colQuincenal;

	@FXML
	private TableColumn<Servicios, Double> colMensual;

	@FXML
	private TableColumn<Servicios, Double> colCompDiario;

	@FXML
	private TableColumn<Servicios, Double> colCompSemanal;

	@FXML
	private TableColumn<Servicios, Double> colCompQuincenal;

	@FXML
	private TableColumn<Servicios, Double> colCompMensual;

	@FXML
	private TableColumn<Servicios, String> colCompartido;

	@FXML
	private HBox pnlPrecios;

	@FXML
	private TextField txtServicio;

	@FXML
	private TextField txtDiario;

	@FXML
	private TextField txtSemanal;

	@FXML
	private TextField txtQuincenal;

	@FXML
	private TextField txtMensual;

	@FXML
	private CheckBox chbServicioComp;

	@FXML
	private Button btnAddServicio;

	@FXML
	private HBox pnlPreciosComp;

	@FXML
	private TextField txtCompDiario;

	@FXML
	private TextField txtCompSemanal;

	@FXML
	private TextField txtCompQuincenal;

	@FXML
	private TextField txtCompMensual;

	@FXML
	private Button btnSave;

	private PersistenceManager pm = PersistenceManager.getPersistenceInstace();
	private ArrayList<Servicios> removed = new ArrayList<>();

	@FXML
	void btnAddServicioOnAction(ActionEvent event) {
		if (!isItErrors()) {
			tblServicios.getItems().add(getServicio());
			clearTextFields();
		}
	}

	@FXML
	void btnSaveOnAction(ActionEvent event) {

		ArrayList<Servicios> services = pm.getServiciosList();

		tblServicios.getItems().forEach(item -> {
			if (services.contains(item))
				pm.updateEntity(item);
			else
				pm.addEntity(item);
		});
		removed.forEach(item -> {
			pm.removeEntity(item);
			pm.getClientesEstadoList(item.getServicio()).forEach(state -> pm.removeEntity(state));
		});
	}

	@FXML
	void itmEliminarOnAction(ActionEvent event) {

		Servicios service = tblServicios.getSelectionModel().getSelectedItem();
		tblServicios.getItems().remove(service);
		removed.add(service);

	}

	@FXML
	void chbServicioCompOnAction(ActionEvent event) {

		AnimationFX slide = null;
		pnlPreciosComp.setVisible(true);
		if (chbServicioComp.isSelected()) {
			slide = new SlideInDown(pnlPreciosComp);
			slide.play();
			new FadeIn(pnlPreciosComp).play();
		} else {
			slide = new SlideOutUp(pnlPreciosComp);
			slide.setOnFinished(e -> pnlPreciosComp.setVisible(false));
			slide.play();
			new FadeOut(pnlPreciosComp).play();
		}

		txtCompDiario.setText("");
		txtCompSemanal.setText("");
		txtCompQuincenal.setText("");
		txtCompMensual.setText("");
	}

	@FXML
	void txtServicioOnKeyTyped(KeyEvent event) {
		txtServicio.getStyleClass().remove("text-error");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initTable();
		// pnlPreciosComp.visibleProperty().bind(chbServicioComp.selectedProperty());
		tblServicios.setEditable(true);
		colServicio.setEditable(false);

	}

	private void initTable() {

		ObservableList<Servicios> list = FXCollections.observableArrayList();
		list.addAll(pm.getServiciosList());

		colServicio.setCellValueFactory(new PropertyValueFactory<>("servicio"));
		colDiario.setCellValueFactory(new PropertyValueFactory<>("precioD"));
		colSemanal.setCellValueFactory(new PropertyValueFactory<>("precioS"));
		colQuincenal.setCellValueFactory(new PropertyValueFactory<>("precioQ"));
		colMensual.setCellValueFactory(new PropertyValueFactory<>("precioM"));
		colCompDiario.setCellValueFactory(new PropertyValueFactory<>("precioCompD"));
		colCompSemanal.setCellValueFactory(new PropertyValueFactory<>("precioCompS"));
		colCompQuincenal.setCellValueFactory(new PropertyValueFactory<>("precioCompQ"));
		colCompMensual.setCellValueFactory(new PropertyValueFactory<>("precioCompM"));
		colCompartido.setCellValueFactory(new PropertyValueFactory<>("compstring"));

		doubleTextCellFactory(colDiario);
		doubleTextCellFactory(colSemanal);
		doubleTextCellFactory(colQuincenal);
		doubleTextCellFactory(colMensual);
		doubleTextCellFactory(colCompDiario);
		doubleTextCellFactory(colCompSemanal);
		doubleTextCellFactory(colCompQuincenal);
		doubleTextCellFactory(colCompMensual);

		editCommits();
		tblServicios.setItems(list);

	}

	private void editCommits() {

		colDiario.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Servicios, Double>>() {

			@Override
			public void handle(CellEditEvent<Servicios, Double> event) {
				double d = 0;
				try {
					d = event.getNewValue();
				} catch (Exception e) {
					d = event.getOldValue();
				}
				event.getRowValue().setPrecioD(d);

			}
		});
		colSemanal.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Servicios, Double>>() {

			@Override
			public void handle(CellEditEvent<Servicios, Double> event) {
				double d = 0;
				try {
					d = event.getNewValue();
				} catch (Exception e) {
					d = event.getOldValue();
				}
				event.getRowValue().setPrecioS(d);
			}
		});
		colQuincenal.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Servicios, Double>>() {

			@Override
			public void handle(CellEditEvent<Servicios, Double> event) {
				double d = 0;
				try {
					d = event.getNewValue();
				} catch (Exception e) {
					d = event.getOldValue();
				}
				event.getRowValue().setPrecioQ(d);
			}
		});
		colMensual.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Servicios, Double>>() {

			@Override
			public void handle(CellEditEvent<Servicios, Double> event) {
				double d = 0;
				try {
					d = event.getNewValue();
				} catch (Exception e) {
					d = event.getOldValue();
				}
				event.getRowValue().setPrecioM(d);
			}
		});
		colCompDiario.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Servicios, Double>>() {

			@Override
			public void handle(CellEditEvent<Servicios, Double> event) {
				double d = 0;
				try {
					d = event.getNewValue();
				} catch (Exception e) {
					d = event.getOldValue();
				}
				event.getRowValue().setPrecioCompD(d);
				tblServicios.refresh();
			}
		});
		colCompSemanal.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Servicios, Double>>() {

			@Override
			public void handle(CellEditEvent<Servicios, Double> event) {
				double d = 0;
				try {
					d = event.getNewValue();
				} catch (Exception e) {
					d = event.getOldValue();
				}
				event.getRowValue().setPrecioCompS(d);
			}
		});
		colCompQuincenal.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Servicios, Double>>() {

			@Override
			public void handle(CellEditEvent<Servicios, Double> event) {
				double d = 0;
				try {
					d = event.getNewValue();
				} catch (Exception e) {
					d = event.getOldValue();
				}
				event.getRowValue().setPrecioCompQ(d);
			}
		});
		colCompMensual.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Servicios, Double>>() {

			@Override
			public void handle(CellEditEvent<Servicios, Double> event) {
				double d = 0;
				try {
					d = event.getNewValue();
				} catch (Exception e) {
					d = event.getOldValue();
				}
				event.getRowValue().setPrecioCompM(d);
			}
		});

	}

	private void doubleTextCellFactory(TableColumn<Servicios, Double> column) {

		column.setCellFactory(TextFieldTableCell.<Servicios, Double>forTableColumn(new DoubleStringConverter() {
			@Override
			public Double fromString(String value) {

				try {
					return Double.parseDouble(value);
				} catch (NumberFormatException e) {
					return 0.0;
				}
			}
		}));
	}

	private void clearTextFields() {

		pnlPrecios.getChildren().forEach(child -> {
			if (child instanceof TextField)
				((TextField) child).setText("");
		});
		pnlPreciosComp.getChildren().forEach(child -> {
			if (child instanceof TextField)
				((TextField) child).setText("");
		});

	}

	private boolean isItErrors() {

		boolean error = false;
		if (txtServicio.getText().equals("")) {
			error = true;
			txtServicio.getStyleClass().add("text-error");
		}
		return error;
	}

	private Servicios getServicio() {

		String servicio = txtServicio.getText();
		double precioD = (txtDiario.getText().equals("")) ? 0 : Double.parseDouble(txtDiario.getText());
		double precioS = (txtSemanal.getText().equals("")) ? 0 : Double.parseDouble(txtSemanal.getText());
		double precioQ = (txtQuincenal.getText().equals("")) ? 0 : Double.parseDouble(txtQuincenal.getText());
		double precioM = (txtMensual.getText().equals("")) ? 0 : Double.parseDouble(txtMensual.getText());
		double precioCompD = (txtCompDiario.getText().equals("")) ? 0 : Double.parseDouble(txtCompDiario.getText());
		double precioCompS = (txtCompSemanal.getText().equals("")) ? 0 : Double.parseDouble(txtCompSemanal.getText());
		double precioCompQ = (txtCompQuincenal.getText().equals("")) ? 0
				: Double.parseDouble(txtCompQuincenal.getText());
		double precioCompM = (txtCompMensual.getText().equals("")) ? 0 : Double.parseDouble(txtCompMensual.getText());
		boolean compartido = chbServicioComp.isSelected();

		Servicios service = new Servicios(servicio, precioD, precioS, precioQ, precioM, precioCompD, precioCompS,
				precioCompQ, precioCompM, compartido);
		return service;
	}
}
