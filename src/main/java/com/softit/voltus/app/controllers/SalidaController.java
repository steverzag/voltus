package com.softit.voltus.app.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;

import com.softit.voltus.app.model.ArticulosAlquilados;
import com.softit.voltus.app.model.Asistencia;
import com.softit.voltus.app.model.ClientesEnGym;
import com.softit.voltus.app.model.PersistenceManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class SalidaController implements Initializable {

	@FXML
	private ImageView imgFoto1;

	@FXML
	private Button btnSalida;

	@FXML
	private TreeTableView<ClientesEnGym> trtClientesEnGym;

	@FXML
	private TreeTableColumn<ClientesEnGym, String> trcCliente;

	@FXML
	private TreeTableColumn<ClientesEnGym, ImageView> trcImagen;

	@FXML
	private TreeTableColumn<ClientesEnGym, String> trcEntrada;

	@FXML
	private TreeTableColumn<ClientesEnGym, String> trcServicio;

	@FXML
	private TreeTableColumn<ClientesEnGym, String> trcEstado;

	@FXML
	private TableView<ArticulosAlquilados> tblArticulosAlqs;

	@FXML
	private TableColumn<ArticulosAlquilados, String> colArticulosAlqs;

	@FXML
	private TableColumn<ArticulosAlquilados, Integer> colCantidadAlqs;

	@FXML
	void btnSalidaOnAction(ActionEvent event) {

		ObservableList<TreeItem<ClientesEnGym>> gymClients = FXCollections.observableArrayList();
		gymClients.addAll(trtClientesEnGym.getSelectionModel().getSelectedItems());
		try {
		gymClients.forEach(item -> {
			if (item.getValue().getCi() == null) {
				item.getChildren().forEach(child -> trtClientesEnGym.getSelectionModel().select(child));
				gymClients.remove(item);
			}
		});
		}catch(ConcurrentModificationException e) {
		}
		gymClients.forEach(item -> {
			try {
				Asistencia a = pm.getAsistencia(item.getValue().getEntrada());
				a.setSalida(new Date());
				pm.updateEntity(a);
				pm.removeEntity(item.getValue());
				clients.remove(item.getValue());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		trtClientesEnGym.getRoot().getChildren().removeAll(gymClients);
		initTableArticulos(new ArrayList<ArticulosAlquilados>());
		initTableGym();
		trtClientesEnGym.refresh();
	}

	private PersistenceManager pm = PersistenceManager.getPersistenceInstace();
	private List<ClientesEnGym> clients;
	private HashSet<String> services;

	private static SalidaController INSTANCE;

	@FXML
	void trtClientesEnGymOnClicked(MouseEvent event) {

		ObservableList<TreeItem<ClientesEnGym>> gymClients = trtClientesEnGym.getSelectionModel().getSelectedItems();
		ArrayList<ArticulosAlquilados> arts = new ArrayList<>();

		gymClients.forEach(item -> {
			if (item.getValue().getCi() == null) {
				item.getChildren().forEach(child -> trtClientesEnGym.getSelectionModel().select(child));
			}
		});
		gymClients.forEach(item -> {
			item.getValue().getArticulos().forEach(art -> {
				ArticulosAlquilados articulo = new ArticulosAlquilados();
				articulo.setArticulo(art.getArticulo());
				articulo.setCantidad(art.getCantidad());
				arts.add(articulo);
			});
		});
		arts.forEach(articulo -> {
			arts.forEach(art -> {
				if (articulo != art && articulo.joinArticulos(art)) {
					art.setCantidad(0);
				}
			});
		});
		for (int i = arts.size() - 1; i >= 0; i--) {
			if (arts.get(i).getCantidad() == 0)
				arts.remove(i);
		}
		initTableArticulos(arts);
		tblArticulosAlqs.refresh();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initTableGym();
	}

	public SalidaController() {
		INSTANCE = this;
	}

	public void initTableGym() {

		clients = pm.getClientesEnGymList();
		services = new HashSet<>();

		ClientesEnGym clientesEnGym = new ClientesEnGym();
		clientesEnGym.setNombre("Clientes en el Gimnnasio");
		TreeItem<ClientesEnGym> itemClients = new TreeItem<>(clientesEnGym);

		trcCliente.setCellValueFactory(new TreeItemPropertyValueFactory<>("nombre"));
		trcImagen.setCellValueFactory(new TreeItemPropertyValueFactory<>("img"));
		trcEntrada.setCellValueFactory(new TreeItemPropertyValueFactory<>("fecha"));
		trcServicio.setCellValueFactory(new TreeItemPropertyValueFactory<>("servicio"));
		trcEstado.setCellValueFactory(new TreeItemPropertyValueFactory<>("estado"));

		clients.forEach(client -> {
			services.add(client.getServicio());
		});

		services.forEach(service -> {
			ClientesEnGym serv = new ClientesEnGym();
			serv.setNombre(service);
			serv.setImgurl("com/softit/voltus/app/view/images/icons8_gym_80px.png");
			TreeItem<ClientesEnGym> item = new TreeItem<>(serv);

			clients.forEach(client -> {
				if (client.getServicio().equals(serv.getNombre())) {
					TreeItem<ClientesEnGym> c = new TreeItem<>(client);
					item.getChildren().add(c);
				}
			});
			itemClients.getChildren().add(item);
		});
		trtClientesEnGym.setRoot(itemClients);
		trtClientesEnGym.setShowRoot(false);
		trtClientesEnGym.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}

	private void initTableArticulos(ArrayList<ArticulosAlquilados> articulos) {

		ObservableList<ArticulosAlquilados> list = FXCollections.observableArrayList();
		list.addAll(articulos);
		colArticulosAlqs.setCellValueFactory(new PropertyValueFactory<>("articulo"));
		colCantidadAlqs.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

		tblArticulosAlqs.setItems(list);

	}

	public static SalidaController getInstance() {
		return INSTANCE;
	}

	public void clear() {
		initTableGym();
		tblArticulosAlqs.getItems().removeAll(tblArticulosAlqs.getItems());
	}

}
