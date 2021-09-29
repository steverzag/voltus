package com.softit.voltus.app.controllers;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import com.softit.voltus.app.classes.Cuadre;
import com.softit.voltus.app.classes.DataSaver;
import com.softit.voltus.app.classes.Paths;
import com.softit.voltus.app.model.Asistencia;
import com.softit.voltus.app.model.ClientesEnGym;
import com.softit.voltus.app.model.Notificaciones;
import com.softit.voltus.app.model.PersistenceManager;

import animatefx.animation.Bounce;
import animatefx.animation.FadeIn;
import animatefx.animation.Pulse;
import animatefx.animation.ZoomIn;
import animatefx.animation.ZoomOut;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class VoltusSysController implements Initializable {

	@FXML
	private Circle btnClose;

	@FXML
	private Circle btnMin;

	@FXML
	private Circle btnMax;

	@FXML
	private Button btnBuzon;

	@FXML
	private Button btnEntradaSalida;

	@FXML
	private Button btnClientes;

	@FXML
	private Button btnOperaciones;

	@FXML
	private Button btnSerivicios;

	@FXML
	private Button btnActivos;

	@FXML
	private Button btnAsistencia;

	@FXML
	private Button btnEstadisticas;

	@FXML
	private BorderPane pnlContent;

	@FXML
	private Label lblCash;

	@FXML
	private Button btnCash;

	@FXML
	private Button btnLogOut;

	@FXML
	private ImageView imgLogOut;

	@FXML
	private Label lblNots;

	@FXML
	private Label lblCierre;

	private PersistenceManager pm = PersistenceManager.getPersistenceInstace();
	private static VoltusSysController INSTANCE;
	private List<Notificaciones> nots;

	@FXML
	void btnCloseOnClicked(MouseEvent event) {
		List<ClientesEnGym> clients = pm.getClientesEnGymList();
		if (clients.size() > 0) {
			Dialogs dialog = new Dialogs();
			int decision = dialog.displayYesNoCancelChoice("Aun Quedan Clienes en el Gimnasio, Desea Darles Salida?",
					"icons8_user_groups_80px_1.png");
			if (decision == Dialogs.CANCEL)
				return;
			else if (decision == Dialogs.NO) {
				saveDB();
				System.exit(0);
			} else if (decision == Dialogs.YES) {
				salidaClientes();
				saveDB();
				System.exit(0);
			}
		} else {
			saveDB();
			System.exit(0);
		}

	}

	@FXML
	void btnLogOutOnAction(ActionEvent event) {
		LogInController.getInstance().show();
		((Stage) btnLogOut.getScene().getWindow()).close();
	}

	@FXML
	void btnOnAction(ActionEvent event) {
		Button butt = (Button) event.getSource();
		String name = butt.getText().replace(" y ", "");

		Parent root = null;
		if (butt.getParent() instanceof HBox)
			new Pulse(butt.getParent()).play();
		else
			new Pulse(butt).play();
		try {
			root = FXMLLoader.load(getClass().getResource(Paths.VIEWS_PATH + name + ".fxml"));
			new FadeIn(root).play();
			pnlContent.setCenter(root);

		} catch (Exception e) {

		}
	}

	private boolean b = false;

	@FXML
	void btnCashOnAction(ActionEvent event) {

		b = !b;
		lblCash.setVisible(true);
		if (b) {
			new ZoomIn(lblCash).play();
			btnCash.setTooltip(new Tooltip("Ocultar Efectivo en Caja"));
		} else {
			new ZoomOut(lblCash).play();
			btnCash.setTooltip(new Tooltip("Mostrar Efectivo en Caja"));
		}
	}

	@FXML
	void btnMinOnClicked() {
		Stage stage = (Stage) btnClose.getScene().getWindow();
		stage.setIconified(true);
	}

	@FXML
	void btnMaxOnClicked() {
		Stage stage = (Stage) btnClose.getScene().getWindow();
		boolean b = stage.isMaximized();
		stage.setMaximized(!b);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		boolean b = LogInController.getInstance().getctiveUser().isAdministrador();
		btnActivos.setVisible(b);
		btnSerivicios.setVisible(b);
		btnAsistencia.setVisible(b);
		btnEstadisticas.setVisible(b);
		btnCash.setTooltip(new Tooltip("Mostrar Efectivo en Caja"));
		btnLogOut.setTooltip(new Tooltip("Cerrar Sesion"));
		btnLogOut.setOnMouseEntered(
				e -> imgLogOut.setImage(new Image("com/softit/voltus/app/view/images/icons8_sign_out_50px_1.png")));
		btnLogOut.setOnMouseExited(
				e -> imgLogOut.setImage(new Image("com/softit/voltus/app/view/images/icons8_sign_out_50px.png")));
		setCash();
		checkForNotificaciones();
		checkForCierres();
	}

	public VoltusSysController() {

		INSTANCE = this;
	}

	public List<Notificaciones> getNots() {
		return nots;
	}

	public void setNots() {
		nots = pm.getNotificacionesList();
	}

	public static VoltusSysController getInstance() {

		return INSTANCE;
	}

	public void checkForNotificaciones() {

		int notsSize = 0;
		setNots();
		for (Notificaciones not : nots) {
			if (!not.isLeido())
				notsSize++;
		}
		if (notsSize < 99)
			lblNots.setText("" + notsSize);
		else
			lblNots.setText("" + 99);
	}

	public void checkForNotificaciones(int notsSize) {
		if (notsSize < 99)
			lblNots.setText("" + notsSize);
		else
			lblNots.setText("" + 99);
	}

	public void setCash() {
		double saldo = pm.getCaja().getSaldo();
		this.lblCash.setText("$ " + saldo);
		new Bounce(btnCash).play();
	}

	private void salidaClientes() {

		List<ClientesEnGym> clients = pm.getClientesEnGymList();
		clients.forEach(client -> {
			Asistencia a = pm.getAsistencia(client.getEntrada());
			a.setSalida(new Date());
			pm.updateEntity(a);
			pm.removeEntity(client);
		});
	}

	private void saveDB() {

		DataSaver saver = new DataSaver();
		saver.saveDB();
		saver.deleteOldSaves();
	}

	public void checkForCierres() {

		Cuadre c = new Cuadre();
		int i = c.getCuadresPendientes().size();
		if (i > 0)
			lblCierre.setVisible(true);
		else
			lblCierre.setVisible(false);

	}

}
