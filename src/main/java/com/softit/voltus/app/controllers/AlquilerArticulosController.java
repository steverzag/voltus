package com.softit.voltus.app.controllers;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

import com.softit.voltus.app.classes.Clientes;
import com.softit.voltus.app.model.ArticulosAlquilados;
import com.softit.voltus.app.model.Caja;
import com.softit.voltus.app.model.ClientesEnGym;
import com.softit.voltus.app.model.Operaciones;
import com.softit.voltus.app.model.PersistenceManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AlquilerArticulosController implements Initializable {

	@FXML
	private TextField txtBuscar;

	@FXML
	private Button btnBuscar;

	@FXML
	private ImageView imgFoto1;

	@FXML
	private TableView<ArticulosAlquilados> tblArticulos;

	@FXML
	private TableColumn<ArticulosAlquilados, String> colArticulos;

	@FXML
	private TableColumn<ArticulosAlquilados, Integer> colCantidad;

	@FXML
	private TableView<ArticulosAlquilados> tblArticulosAlqs;

	@FXML
	private TableColumn<ArticulosAlquilados, String> colArticulosAlqs;

	@FXML
	private TableColumn<ArticulosAlquilados, Integer> colCantidadAlqs;

	@FXML
	private ComboBox<String> cbxArticulo;

	@FXML
	private Spinner<Integer> spnCantidad;

	@FXML
	private Button btnAddArt;

	@FXML
	private Button btnDelArt;

	@FXML
	private Button btnAceptar;

	@FXML
	private Label lblCash;

	private PersistenceManager pm = PersistenceManager.getPersistenceInstace();
	private ClientesEnGym client;
	private static AlquilerArticulosController INSTANCE;
	private List<String> clientList = (List<String>) pm.getClientesEnGymNameList();

	@FXML
	void btnBuscarOnAction(ActionEvent event) {
		initClient();
	}

	@FXML
	void txtBuscarOnAction(ActionEvent event) {
		initClient();
	}

	@FXML
	void btnAceptarOnAction(ActionEvent event) {

		tblArticulos.getItems().forEach(item -> {
			
			for (ArticulosAlquilados art : client.getArticulos()) {
				if (item.equals(art)) {
					art.setCantidad(art.getCantidad() + item.getCantidad());
				}
			}
			if (!client.getArticulos().contains(item)) {
				client.getArticulos().add(item);
			}
		});
		pm.updateEntity(client);
		saveOperacion();
		initTables(client);
		setPrice();
		resetArticulosAdder();
	}

	private void saveOperacion() {
		tblArticulos.getItems().forEach(item -> {
			
			String id = Operaciones.ALQUILER_ART;
			String acceso = LogInController.getInstance().getctiveUser().getUsuario();
			String observacion = item.getArticulo();
			double valor = (pm.getArticulos(observacion).getPrecio()) * item.getCantidad();
			String cliente = client.getCi();
			Operaciones op = new Operaciones(id , acceso, valor, cliente, observacion, new Date(), false);
			
			Caja caja = pm.getCaja();
			caja.setSaldo(op.getOperationCash(caja.getSaldo()));
			
			pm.addEntity(op);
			pm.updateEntity(caja);
			VoltusSysController.getInstance().setCash();
		});
		
	}

	@FXML
	void btnAddArtOnAction(ActionEvent event) {

		if (!isItErrors()) {
			ArticulosAlquilados art = new ArticulosAlquilados(cbxArticulo.getValue(), client.getCi(), spnCantidad.getValue());
			art.setCliente(client);
			if (!tblArticulos.getItems().contains(art))
				tblArticulos.getItems().add(art);
			setPrice();
			resetArticulosAdder();
		}
	}

	@FXML
	void btnDelArtOnAction(ActionEvent event) {

		tblArticulos.getItems().remove(tblArticulos.getSelectionModel().getSelectedItem());
		setPrice();
		resetArticulosAdder();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		init();
	}

	public AlquilerArticulosController() {
		INSTANCE = this;
	}

	private void init() {

		cbxArticulo.getItems().addAll(pm.getArticulosNameList());
		TextFields.bindAutoCompletion(txtBuscar, clientList);
		spnCantidad.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 1));
	}

	private void initTables(ClientesEnGym client) {

		ObservableList<ArticulosAlquilados> articulosAlquilados = FXCollections.observableArrayList();
		articulosAlquilados.addAll(client.getArticulos());
		colArticulosAlqs.setCellValueFactory(new PropertyValueFactory<>("articulo"));
		colCantidadAlqs.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
		tblArticulosAlqs.setItems(articulosAlquilados);
		tblArticulosAlqs.refresh();

		ObservableList<ArticulosAlquilados> articulos = FXCollections.observableArrayList();
		colArticulos.setCellValueFactory(new PropertyValueFactory<>("articulo"));
		colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
		tblArticulos.setItems(articulos);
		tblArticulos.refresh();
	}

	public void clear() {
		txtBuscar.setText("");
		clientList = (List<String>) pm.getClientesEnGymNameList();
		tblArticulos.getItems().removeAll(tblArticulos.getItems());
		tblArticulosAlqs.getItems().removeAll(tblArticulosAlqs.getItems());
		client = null;
		imgFoto1.setImage(null);
	}

	public static AlquilerArticulosController getInstance() {
		return INSTANCE;
	}

	private void initClient() {
		String ci = Clientes.getClientCI(txtBuscar.getText());
		client = pm.getClientesEnGym(ci);
		if (client != null) {
			initTables(client);
			try {
				imgFoto1.setImage(new Image(client.getImgurl()));
				String s = new URL(client.getImgurl()).toURI().getPath();
				if (!new File(s).exists())
					imgFoto1.setImage(new Image("com/softit/voltus/app/view/images/default-user.jpg"));
			} catch (Exception e) {
				imgFoto1.setImage(new Image("com/softit/voltus/app/view/images/default-user.jpg"));
			}
		}
		cbxArticulo.setDisable(false);
		btnAddArt.setDisable(false);
		spnCantidad.setDisable(false);
		btnDelArt.setDisable(false);
		btnAceptar.setDisable(false);
	}

	private boolean isItErrors() {
		boolean error = false;

		if (spnCantidad.getValue() == 0)
			error = true;
		if (cbxArticulo.getSelectionModel().getSelectedItem() == null)
			error = true;
		return error;
	}

	private void setPrice() {

		double precioTotal = 0;
		for (ArticulosAlquilados art : tblArticulos.getItems()) {
			int cantidad = art.getCantidad();
			double precio = pm.getArticulos(art.getArticulo()).getPrecio();

			precioTotal += precio * cantidad;
		}
		lblCash.setText("$ " + +precioTotal);
	}

	private void resetArticulosAdder() {

		cbxArticulo.getItems().removeAll(cbxArticulo.getItems());
		cbxArticulo.getItems().addAll(pm.getArticulosNameList());
		tblArticulos.getItems().forEach(item -> cbxArticulo.getItems().remove(item.getArticulo()));
		cbxArticulo.setPromptText("Articulo");
		spnCantidad.getValueFactory().setValue(1);
		
	}
}
