package com.softit.voltus.app.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import com.softit.voltus.app.model.Articulos;
import com.softit.voltus.app.model.Caja;
import com.softit.voltus.app.model.Operaciones;
import com.softit.voltus.app.model.PersistenceManager;
import com.softit.voltus.app.model.Usuario;

import animatefx.animation.FadeIn;
import animatefx.animation.FadeOut;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.converter.DoubleStringConverter;

public class ActivosCajaController implements Initializable {

	@FXML
	private ComboBox<String> cbxOperacion;

	@FXML
	private TextField txtValor;

	@FXML
	private TextField txtObservacion;

	@FXML
	private CheckBox chkBox;

	@FXML
	private Button btnAceptarOp;

	@FXML
	private TableView<Articulos> tblArticulos;

	@FXML
	private TableColumn<Articulos, String> colArticulo;

	@FXML
	private TableColumn<Articulos, Double> colPrecio;

	@FXML
	private MenuItem itmEliminar;

	@FXML
	private HBox pnlPrecios;

	@FXML
	private TextField txtArticulo;

	@FXML
	private TextField txtPrecio;

	@FXML
	private Button btnAddArticulo;

	@FXML
	private Button btnSave;

	@FXML
	private TableView<Usuario> tblUsuarios;

	@FXML
	private TableColumn<Usuario, String> colAcceso;

	@FXML
	private TableColumn<Usuario, String> colUsuario;

	@FXML
	private MenuItem itmEliminarUsuario;

	@FXML
	private MenuItem itmVer;

	@FXML
	private TextField txtUsuario;

	@FXML
	private PasswordField pasPassword;

	@FXML
	private PasswordField pasPassword2;

	@FXML
	private TextField txtPass;

	@FXML
	private TextField txtPass2;

	@FXML
	private Button btnSaveUser;

	@FXML
	private Button btnAddEditUser;

	private PersistenceManager pm = PersistenceManager.getPersistenceInstace();
	private ArrayList<Articulos> removedArticulos = new ArrayList<>();
	private ArrayList<Usuario> removedUsuarios = new ArrayList<>();

	@FXML
	void btnAceptarOpOnAction(ActionEvent event) {

		Operaciones op = getOperacion();
		if (!isItOpsErros(op)) {
			saveOperation(op);
			clearOperation();
		}
	}

	@FXML
	void btnAddArticuloOnAction(ActionEvent event) {
		if (!isItErrors()) {
			addArticulo();
			clearArticulo();
		}

	}

	@FXML
	void btnSaveOnAction(ActionEvent event) {

		ArrayList<Articulos> list = (ArrayList<Articulos>) pm.getArticulosList();

		tblArticulos.getItems().forEach(item -> {
			if (list.contains(item))
				pm.updateEntity(item);
			else
				pm.addEntity(item);
		});
		removedArticulos.forEach(item -> pm.removeEntity(item));
		removedArticulos.removeAll(removedArticulos);
	}

	@FXML
	void itmEliminarOnAction(ActionEvent event) {
		Articulos articulo = tblArticulos.getSelectionModel().getSelectedItem();
		removedArticulos.add(articulo);
		tblArticulos.getItems().remove(articulo);
	}

	@FXML
	void itmVerOnAction(ActionEvent event) {
		Usuario user = tblUsuarios.getSelectionModel().getSelectedItem();
		txtUsuario.setText(user.getUsuario());
		pasPassword.setText(user.getContrasena());
		pasPassword2.setText(user.getContrasena());
		txtPass.setText(user.getContrasena());
		txtPass2.setText(user.getContrasena());
	}

	@FXML
	void tglOnAction(ActionEvent event) {

		ToggleButton button = (ToggleButton) event.getTarget();

		boolean b = button.isSelected();
		pasPassword.setVisible(!b);
		pasPassword2.setVisible(!b);
		txtPass.setVisible(b);
		txtPass2.setVisible(b);

		if (b == true) {
			pasPassword.toBack();
			pasPassword2.toBack();
		} else {
			pasPassword.toFront();
			pasPassword2.toFront();
		}
	}

	@FXML
	void itmEliminarUsuarioOnAction(ActionEvent event) {
		Usuario user = tblUsuarios.getSelectionModel().getSelectedItem();
		removedUsuarios.add(user);
		tblUsuarios.getItems().remove(user);
		removedUsuarios.add(user);
	}

	@FXML
	void txtOnKeyTyped(KeyEvent event) {

		TextInputControl input = (TextInputControl) event.getTarget();
		input.getStyleClass().remove("text-error");
		if (input == txtPass || input == txtPass2 || input == pasPassword || input == pasPassword2)
			textSyncro(input);
	}

	@FXML
	void cbxOperacionOnAction(ActionEvent event) {

		cbxOperacion.getStyleClass().remove("text-error");
		opcionGasto();
	}

	@FXML
	void btnAddEditUserOnAction(ActionEvent event) {
		if (!isItUserError()) {
			String usuario = txtUsuario.getText();
			String contrasena = pasPassword.getText();
			Usuario user = new Usuario(usuario, "Trabajador", contrasena);
			if (tblUsuarios.getItems().contains(user)) {
				tblUsuarios.getItems().forEach(item -> {
					if (item.equals(user))
						item.setContrasena(contrasena);
				});
			} else
				tblUsuarios.getItems().add(user);
		}
	}

	@FXML
	void btnSaveUserOnAction(ActionEvent event) {

		if (!isItUserError()) {
			ArrayList<Usuario> list = (ArrayList<Usuario>) pm.getUsuariosList();
			tblUsuarios.getItems().forEach(item -> {
				if (list.contains(item))
					pm.updateEntity(item);
				else
					pm.addEntity(item);
			});
			removedUsuarios.forEach(item -> pm.removeEntity(item));
			removedUsuarios.removeAll(removedUsuarios);
			tblUsuarios.refresh();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		initTables();
		cbxOperacion.getItems().addAll(Operaciones.DEPOSITO, Operaciones.EXTRACCION, Operaciones.GASTO);
		onlyNumbers(txtPrecio);
		onlyNumbers(txtValor);

	}

	private void initTables() {

		ObservableList<Articulos> artsList = FXCollections.observableArrayList(pm.getArticulosList());
		colArticulo.setCellValueFactory(new PropertyValueFactory<>("articulo"));
		colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));

		colPrecio.setCellFactory(TextFieldTableCell.<Articulos, Double>forTableColumn(new DoubleStringConverter() {
			@Override
			public Double fromString(String value) {

				try {
					return Double.parseDouble(value);
				} catch (NumberFormatException e) {
					return 0.0;
				}
			}
		}));

		colPrecio.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Articulos, Double>>() {

			@Override
			public void handle(CellEditEvent<Articulos, Double> event) {
				double d = 0;
				try {
					d = event.getNewValue();
				} catch (Exception e) {
					d = event.getOldValue();
				}
				event.getRowValue().setPrecio(d);
			}
		});

		tblArticulos.setItems(artsList);

		ObservableList<Usuario> usersList = FXCollections.observableArrayList(pm.getUsuariosList());
		colAcceso.setCellValueFactory(new PropertyValueFactory<>("acceso"));
		colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));

		tblUsuarios.setItems(usersList);
	}

	private void addArticulo() {

		String articulo = txtArticulo.getText();
		double precio = (txtPrecio.getText().length() > 0) ? Double.parseDouble(txtPrecio.getText()) : 0;
		Articulos art = new Articulos(articulo, precio);

		if (!tblArticulos.getItems().contains(art))
			tblArticulos.getItems().add(art);
	}

	private boolean isItErrors() {

		if (txtArticulo.getText().length() == 0) {
			txtArticulo.getStyleClass().add("text-error");
			return true;
		}
		return false;
	}

	private void clearOperation() {
		txtValor.setText("");
		txtObservacion.setText("");
	}

	private boolean isItUserError() {

		boolean error = false;

		if (txtUsuario.getText().length() == 0) {
			txtUsuario.getStyleClass().add("text-error");
			error = true;
		}
		if (!pasPassword.getText().equals(pasPassword2.getText())) {
			pasPassword.getStyleClass().add("text-error");
			pasPassword2.getStyleClass().add("text-error");			
			txtPass.getStyleClass().add("text-error");			
			txtPass2.getStyleClass().add("text-error");			
			error = true;
		}
		return error;
	}

	private boolean isItOpsErros(Operaciones op) {

		boolean error = false;
		double saldo = pm.getCaja().getSaldo();

		if (op.getValor() == 0 || op.getOperationCash(saldo) < 0) {
			txtValor.getStyleClass().add("text-error");
			error = true;
		}
		if (cbxOperacion.getSelectionModel().getSelectedItem() == null) {
			cbxOperacion.getStyleClass().add("text-error");
			error = true;
		}
		return error;
	}

	private Operaciones getOperacion() {

		String id = cbxOperacion.getSelectionModel().getSelectedItem();
		String acceso = LogInController.getInstance().getctiveUser().getUsuario();
		double valor = (txtValor.getText().equals("")) ? 0 : Double.parseDouble(txtValor.getText());
		String observacion = txtObservacion.getText();
		Operaciones op = new Operaciones(id, acceso, valor, "", observacion, new Date(), false);

		return op;
	}

	private void saveOperation(Operaciones op) {

		pm.addEntity(op);

		boolean b = cbxOperacion.getSelectionModel().getSelectedItem().equals(Operaciones.GASTO);
		if (b && !chkBox.isSelected())
			return;

		Caja caja = pm.getCaja();
		caja.setSaldo(op.getOperationCash(caja.getSaldo()));
		pm.updateEntity(caja);
		VoltusSysController.getInstance().setCash();

	}

	private void onlyNumbers(TextField textField) {

		textField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.equals(""))
				return;
			try {
				Double.parseDouble(newValue);
			} catch (Exception e) {
				textField.setText(oldValue);
			}
		});
	}

	private void clearArticulo() {
		txtArticulo.setText("");
		txtPrecio.setText("");
	}

	private void textSyncro(TextInputControl input) {

		String s = input.getText();
		StackPane pane = (StackPane) input.getParent();
		pane.getChildren().forEach(child -> {
			TextInputControl in = (TextInputControl) child;
			if(in != input)
			((TextInputControl) child).setText(s);
		});
		pasPassword.getStyleClass().remove("text-error");
		pasPassword2.getStyleClass().remove("text-error");			
		txtPass.getStyleClass().remove("text-error");			
		txtPass2.getStyleClass().remove("text-error");	
	}

	private void opcionGasto() {

		if (cbxOperacion.getSelectionModel().getSelectedItem().equals(Operaciones.GASTO)) {
			chkBox.setVisible(true);
			new FadeIn(chkBox).play();
		} else {
			FadeOut out = new FadeOut(chkBox);
			out.setOnFinished(e -> chkBox.setVisible(false));
			out.play();
		}
	}
}
