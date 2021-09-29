package com.softit.voltus.app.controllers;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.softit.voltus.app.classes.Clientes;
import com.softit.voltus.app.classes.Notificacion;
import com.softit.voltus.app.model.ClientesEstado;
import com.softit.voltus.app.model.ClientesInfoPersonal;
import com.softit.voltus.app.model.ClientesInfoTecnica;
import com.softit.voltus.app.model.PersistenceManager;
import com.softit.voltus.app.model.Rutas;
import com.softit.voltus.app.model.Servicios;

import animatefx.animation.FadeInLeft;
import animatefx.animation.FadeInRight;
import animatefx.animation.FadeOutLeft;
import animatefx.animation.FadeOutRight;
import animatefx.animation.ZoomIn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;
import javafx.util.Callback;

public class InfoClienteController implements Initializable {

	@FXML
	private AnchorPane pnlInfoPer;

	@FXML
	private ImageView imgFoto;

	@FXML
	private TextField txtNombre;

	@FXML
	private TextField txtCI;

	@FXML
	private TextField txtDireccion;

	@FXML
	private TextField txtTelCasa;

	@FXML
	private TextField txtTelEmerg;

	@FXML
	private TextField txtTelMovil;

	@FXML
	private TextField txtOcup;

	@FXML
	private Button btnSig;

	@FXML
	private Label lblSexo;

	@FXML
	private RadioButton rdbMale;

	@FXML
	private ToggleGroup sexo;

	@FXML
	private RadioButton rdbFemale;

	@FXML
	private Button btnEditar;

	@FXML
	private Button btnEliminar;

	@FXML
	private AnchorPane pnlInfoTec;

	@FXML
	private TextField txtCuello;

	@FXML
	private TextField txtCintura;

	@FXML
	private TextField txtCadera;

	@FXML
	private TextField txtPeso;

	@FXML
	private TextField txtTalla;

	@FXML
	private TextField txtFrecCard;

	@FXML
	private TextField txtFrecResp;

	@FXML
	private Button btnAtras;

	@FXML
	private TextField txtMeta;

	@FXML
	private TableView<ClientesEstado> tblServicios;

	@FXML
	private TableColumn<ClientesEstado, String> colServicio;

	@FXML
	private TableColumn<ClientesEstado, String> colFPago;

	@FXML
	private TableColumn<ClientesEstado, String> colEstado;

	@FXML
	private HBox pnlEditing;

	@FXML
	private ComboBox<String> cbxServicio;

	@FXML
	private Button btnAddServicio;

	@FXML
	private Button btnDelServicio;

	@FXML
	private Button btnFinalizar;

	@FXML
	private Label lblEdad;

	private PersistenceManager pm = PersistenceManager.getPersistenceInstace();

	@FXML
	void btnEditarOnAction(ActionEvent event) {

		setUpInsertingMode(true);
		txtCI.setEditable(false);
	}

	@FXML
	void btnFinalizarOnAction() {

		ClientesInfoPersonal client = getClient();
		if (!isItErrors()) {

			try {
				saveClient(client);
				setClient(getClient());
			} catch (Exception e) {
				if (e.getMessage().equals("Error while committing the transaction")) {
					txtCI.getStyleClass().add("text-error");
					btnAtrasOnAction(null);
				}
			}
			setUpInsertingMode(false);
			btnAtrasOnAction(null);
			ClientesController.getInstance().initClientList();
			VoltusSysController.getInstance().checkForNotificaciones();
		}
	}

	@FXML
	void btnEliminarOnAction(ActionEvent event) {

		ClientesInfoPersonal client = getClient();
		pm.removeEntity(client);
		VoltusSysController.getInstance().checkForNotificaciones();
		ClientesController.getInstance().getAddClientButton().fire();
		eliminarFoto(client.getImgUrl());
	}

	@FXML
	void btnAtrasOnAction(ActionEvent event) {

		new FadeOutRight(pnlInfoTec).play();
		new FadeInLeft(pnlInfoPer).play();
		pnlInfoPer.toFront();
	}

	@FXML
	void btnSigOnAction(ActionEvent event) {
		pnlInfoTec.setVisible(true);
		new FadeOutLeft(pnlInfoPer).play();
		new FadeInRight(pnlInfoTec).play();
		pnlInfoTec.toFront();

	}

	@FXML
	void btnAddServicioOnAction(ActionEvent event) {
		String servicio = cbxServicio.getValue();
		if (servicio != null) {
			ClientesEstado state = new ClientesEstado();
			String fPago = pm.getServicio(servicio).getFormasPago().get(0);
			state.setServicio(servicio);
			state.setFpago(fPago);
			tblServicios.getItems().add(state);
			setUpPnlEditing();
			tblServicios.getStyleClass().remove("table-error");
		}
	}

	@FXML
	void btnDelServicioOnAction(ActionEvent event) {

		tblServicios.getItems().remove(tblServicios.getSelectionModel().getSelectedItem());
		setUpPnlEditing();
	}

	@FXML
	void imgFotoOnClicked(MouseEvent event) {

		if(imgFoto.getCursor() == null)
			return;
		if (imgFoto.getCursor().equals(Cursor.HAND)) {
			FileChooser file = new FileChooser();
			Rutas ruta = pm.getRuta("img");
			String path = ruta.getPath();
			try {
				File f = new File(path);
				if (f.exists())
					file.setInitialDirectory(f);
			} catch (Exception e) {
			}
			file.setSelectedExtensionFilter(new ExtensionFilter("Imagenes", "jpg", "png"));
			Window w = imgFoto.getScene().getWindow();
			File f = file.showOpenDialog(w).getAbsoluteFile();
			String url = "";

			try {
				url = f.toURI().toURL().toExternalForm();
				String s = f.getAbsolutePath();
				path = s.substring(0, s.lastIndexOf("\\"));
			} catch (MalformedURLException e) {
			} catch (SecurityException e) {
			}
			ruta.setPath(path);
			ruta.setUrl(url);
			pm.updateEntity(ruta);

			initImgFoto(url);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		firewall();
		boolean b = setLookedClient();
		setUpInsertingMode(!b);
		initImgFoto(getClient().getImgUrl());
		errorCleaner();
		pnlInfoPer.toFront();
		pnlInfoTec.setVisible(false);
		new ZoomIn(pnlInfoPer).play();
	}

	private void initImgFoto(String url) {

		try {
			imgFoto.setImage(new Image(url));
			String s = new URL(url).toURI().getPath();
			if (!new File(s).exists())
				imgFoto.setImage(new Image("com/softit/voltus/app/view/images/default-user.jpg"));
		} catch (Exception e) {
		}
	}

	private boolean setLookedClient() {

		ClientesController cc = ClientesController.getInstance();
		ClientesInfoPersonal client = cc.getClient();
		try {
			client.toString();
		} catch (Exception e) {
			return false;
		}
		try {
			imgFoto.setImage(new Image(client.getImgUrl()));
			String s = new URL(client.getImgUrl()).toURI().getPath();
			if (!new File(s).exists())
				imgFoto.setImage(new Image("com/softit/voltus/app/view/images/default-user.jpg"));
		} catch (Exception e) {
		}
		ClientesInfoTecnica tec = client.getInfoTecnica();
		txtNombre.setText(client.getNombre());
		txtCI.setText(client.getCi());
		lblEdad.setText("Edad: " + client.getEdad());
		txtDireccion.setText(client.getDireccion());
		txtOcup.setText(client.getOcupacion());
		txtTelCasa.setText(client.getTelCasa());
		txtTelMovil.setText(client.getTelMovil());
		txtTelEmerg.setText(client.getTelEmerg());
		txtCuello.setText((tec.getCuello() == 0 ? "" : "" + tec.getCuello()));
		txtCintura.setText((tec.getCintura() == 0 ? "" : "" + tec.getCintura()));
		txtCadera.setText((tec.getCadera() == 0 ? "" : "" + tec.getCadera()));
		txtTalla.setText((tec.getTalla() == 0 ? "" : "" + tec.getTalla()));
		txtPeso.setText((tec.getPeso() == 0 ? "" : "" + tec.getPeso()));
		txtFrecCard.setText((tec.getFrecCard() == 0 ? "" : "" + tec.getFrecCard()));
		txtFrecResp.setText((tec.getFrecResp() == 0 ? "" : "" + tec.getFrecResp()));
		txtMeta.setText(tec.getMeta());

		if (client.getSexo().equals("M"))
			rdbMale.setSelected(true);
		else
			rdbFemale.setSelected(true);

		setUpTableServicios(client);
		setUpPnlEditing();

		return true;

	}

	private boolean setClient(ClientesInfoPersonal client) throws Exception {

		try {
			client.toString();
		} catch (Exception e) {
			return false;
		}
		ClientesInfoTecnica tec = client.getInfoTecnica();
		txtNombre.setText(client.getNombre());
		txtCI.setText(client.getCi());
		lblEdad.setText("Edad: " + client.getEdad());
		txtDireccion.setText(client.getDireccion());
		txtOcup.setText(client.getOcupacion());
		txtTelCasa.setText(client.getTelCasa());
		txtTelMovil.setText(client.getTelMovil());
		txtTelEmerg.setText(client.getTelEmerg());
		txtCuello.setText((tec.getCuello() == 0 ? "" : "" + tec.getCuello()));
		txtCintura.setText((tec.getCintura() == 0 ? "" : "" + tec.getCintura()));
		txtCadera.setText((tec.getCadera() == 0 ? "" : "" + tec.getCadera()));
		txtTalla.setText((tec.getTalla() == 0 ? "" : "" + tec.getTalla()));
		txtPeso.setText((tec.getPeso() == 0 ? "" : "" + tec.getPeso()));
		txtFrecCard.setText((tec.getFrecCard() == 0 ? "" : "" + tec.getFrecCard()));
		txtFrecResp.setText((tec.getFrecResp() == 0 ? "" : "" + tec.getFrecResp()));
		txtMeta.setText(tec.getMeta());
		setUpTableServicios(client);

		if (client.getSexo().equals("M"))
			rdbMale.setSelected(true);
		else
			rdbFemale.setSelected(true);

		setUpPnlEditing();

		return true;

	}

	private void setUpPnlEditing() {

		ArrayList<Servicios> services = pm.getServiciosList();
		cbxServicio.getItems().removeAll(cbxServicio.getItems());
		services.forEach(service -> {
			cbxServicio.getItems().add(service.getServicio());
		});
		tblServicios.getItems().forEach(item -> {
			cbxServicio.getItems().remove(item.getServicio());
		});

	}

	private void setUpTableServicios(ClientesInfoPersonal client) {

		ObservableList<ClientesEstado> services = FXCollections.observableArrayList();
		services.addAll(client.getClientesEstado());

		colServicio.setCellValueFactory(new PropertyValueFactory<>("servicio"));
		colFPago.setCellValueFactory(new PropertyValueFactory<>("fpago"));
		colEstado.setCellValueFactory(new PropertyValueFactory<>("activostring"));

		colFPago.setCellFactory(new Callback<TableColumn<ClientesEstado, String>, TableCell<ClientesEstado, String>>() {

			@Override
			public TableCell<ClientesEstado, String> call(TableColumn<ClientesEstado, String> param) {

				ComboBoxTableCell<ClientesEstado, String> box = new ComboBoxTableCell<ClientesEstado, String>() {
					public void startEdit() {
						String servicio = getTableRow().getItem().getServicio();
						ArrayList<String> ary = pm.getServicio(servicio).getFormasPago();
						getItems().removeAll(getItems());
						getItems().addAll(ary);
						super.startEdit();
					};
				};
				box.setAlignment(Pos.CENTER);
				return box;
			}
		});

		colFPago.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ClientesEstado, String>>() {

			@Override
			public void handle(CellEditEvent<ClientesEstado, String> event) {
				ClientesEstado state = event.getRowValue();
				String fPago = event.getNewValue();
				String servicio = state.getServicio();
				ArrayList<String> list = pm.getServicio(servicio).getFormasPago();
				if (list.contains(fPago))
					state.setFpago(fPago);
			}
		});
		tblServicios.setItems(services);
	}

	private void setUpInsertingMode(boolean b) {

		pnlInfoPer.getChildren().forEach(child -> {
			if (child instanceof TextInputControl) {
				((TextInputControl) child).setTooltip(new Tooltip(((TextInputControl) child).getPromptText()));
				((TextInputControl) child).setEditable(b);
			} else if (child instanceof RadioButton) {
				((RadioButton) child).setDisable(!b);
			}

		});
		pnlInfoTec.getChildren().forEach(child -> {
			if (child instanceof TextInputControl) {
				((TextInputControl) child).setTooltip(new Tooltip(((TextInputControl) child).getPromptText()));
				((TextInputControl) child).setEditable(b);
			}
		});
		pnlEditing.setVisible(b);
		if (b) {
			ClientesInfoPersonal client = (pm.getClient(txtCI.getText()) != null ? pm.getClient(txtCI.getText())
					: new ClientesInfoPersonal());
			setUpTableServicios(client);
			setUpPnlEditing();
		}
		setImageUpdatable(b);
		tblServicios.setEditable(b);
		lblEdad.setVisible(!b);
		btnEditar.setVisible(!b);
		btnEliminar.setVisible(!b);
		btnFinalizar.setVisible(b);
	}

	private void setImageUpdatable(boolean b) {

		if (b) {
			imgFoto.setCursor(Cursor.HAND);
		}
	}

	private void firewall() {

		onlyDigits(txtCI, 11);
		onlyDigits(txtTelCasa);
		onlyDigits(txtTelEmerg);
		onlyDigits(txtTelMovil);
		onlyNumbers(txtCuello);
		onlyNumbers(txtCintura);
		onlyNumbers(txtCadera);
		onlyNumbers(txtTalla);
		onlyNumbers(txtPeso);
		onlyDigits(txtFrecCard);
		onlyDigits(txtFrecResp);
	}

	private void onlyDigits(TextField textfield) {

		textfield.textProperty().addListener((observable, oldValue, newValue) -> {

			if (newValue.matches("\\d*"))
				return;
			textfield.setText(newValue.replaceAll("[^\\d]", ""));
		});
	}

	private void onlyDigits(TextField textfield, int length) {

		textfield.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.length() > length) {
				textfield.setText(oldValue);
				return;
			}
			if (newValue.matches("\\d*"))
				return;
			textfield.setText(newValue.replaceAll("[^\\d]", ""));
		});
	}

	private void onlyNumbers(TextField textfield) {

		textfield.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.equals(""))
				return;
			try {
				Double.parseDouble(newValue);
			} catch (Exception e) {
				textfield.setText(oldValue);
			}
		});
	}

	private ClientesInfoPersonal getClient() {

		ClientesInfoTecnica tec = new ClientesInfoTecnica();
		tec.setCi(txtCI.getText());
		tec.setCuello(txtCuello.getText().length() > 0 ? Double.valueOf(txtCuello.getText()) : 0);
		tec.setCintura(txtCintura.getText().length() > 0 ? Double.valueOf(txtCintura.getText()) : 0);
		tec.setCadera(txtCadera.getText().length() > 0 ? Double.valueOf(txtCadera.getText()) : 0);
		tec.setPeso(txtPeso.getText().length() > 0 ? Double.valueOf(txtPeso.getText()) : 0);
		tec.setTalla(txtTalla.getText().length() > 0 ? Double.valueOf(txtTalla.getText()) : 0);
		tec.setFrecCard(txtFrecCard.getText().length() > 0 ? Integer.valueOf(txtFrecCard.getText()) : 0);
		tec.setFrecResp(txtFrecResp.getText().length() > 0 ? Integer.valueOf(txtFrecResp.getText()) : 0);
		tec.setMeta(txtMeta.getText());

		ClientesInfoPersonal client = new ClientesInfoPersonal();
		String imgUrl = (imgFoto.getImage().getUrl().endsWith("default-user.jpg")) ? "" : imgFoto.getImage().getUrl();
		client.setCi(txtCI.getText());
		client.setNombre(txtNombre.getText());
		client.setDireccion(txtDireccion.getText());
		client.setOcupacion(txtOcup.getText());
		client.setSexo((rdbMale.isSelected() ? "M" : "F"));
		client.setTelCasa(txtTelCasa.getText());
		client.setTelMovil(txtTelMovil.getText());
		client.setTelEmerg(txtTelEmerg.getText());
		client.setImgUrl(imgUrl);
		client.setInfoTecnica(tec);
		tblServicios.getItems().forEach(item -> {
			item.setCi(txtCI.getText());
			client.addClientesEstado(item);
		});

		return client;
	}

	private void saveClient(ClientesInfoPersonal client) {
		if (txtCI.isEditable())
			pm.addEntity(client);
		else {
			List<ClientesEstado> states = pm.getClient(txtCI.getText()).getClientesEstado();
			client.getClientesEstado().forEach(item -> {
				states.forEach(st -> {
					if (!client.getClientesEstado().contains(st)) {
						pm.removeEntity(st);
					}
				});
				if (states.contains(item))
					pm.updateEntity(item);
				else
					pm.addEntity(item);
				Notificacion.addtNotificacion(item);
			});

			pm.updateEntity(client);
		}
	}

	private boolean isItErrors() {
		boolean error = false;
		boolean informationError = false;
		if (txtCI.getText().length() != 11 || !Clientes.isValidCI(txtCI.getText())) {
			error = true;
			informationError = true;
			txtCI.getStyleClass().add("text-error");
		}
		if (txtNombre.getText().length() == 0) {
			error = true;
			informationError = true;
			txtNombre.getStyleClass().add("text-error");
		}
		if (txtDireccion.getText().length() == 0) {
			error = true;
			informationError = true;
			txtDireccion.getStyleClass().add("text-error");
		}
		if (!rdbFemale.isSelected() && !rdbMale.isSelected()) {
			error = true;
			informationError = true;
			lblSexo.getStyleClass().add("label-error");
		}

		if (informationError)
			btnAtrasOnAction(null);

		return error;
	}

	private void errorCleaner() {

		txtNombre.setOnKeyTyped(e -> txtNombre.getStyleClass().remove("text-error"));
		txtCI.setOnKeyTyped(e -> txtCI.getStyleClass().remove("text-error"));
		txtDireccion.setOnKeyTyped(e -> txtDireccion.getStyleClass().remove("text-error"));
		rdbFemale.setOnAction(e -> lblSexo.getStyleClass().remove("label-error"));
		rdbMale.setOnAction(e -> lblSexo.getStyleClass().remove("label-error"));

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
