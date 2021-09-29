package com.softit.voltus.app.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import com.softit.voltus.app.classes.DataSaver;
import com.softit.voltus.app.classes.Fechas;
import com.softit.voltus.app.classes.Paths;
import com.softit.voltus.app.model.Asistencia;
import com.softit.voltus.app.model.ClientesEnGym;
import com.softit.voltus.app.model.PersistenceManager;
import com.softit.voltus.app.model.Usuario;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LogInController implements Initializable {

	@FXML
	private Pane pnlSignIn;

	@FXML
	private Pane pnlSignUp;

	@FXML
	private TextField txtUser;

	@FXML
	private PasswordField pasPass;

	@FXML
	private Button btnEntrar;

	@FXML
	private Circle btnClose;

	private PersistenceManager pm = PersistenceManager.getPersistenceInstace();
	private static LogInController INSTANCE;
	private double x = 0;
	private double y = 0;

	private Usuario activeUser;

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
			}
			else if (decision == Dialogs.YES) {
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
	void btnEntrarOnAction(ActionEvent event) {
		
		if (!isItErrors()) {
			
			startProgram();
			hide();
		}
	}

	@FXML
	void txtOnKeyTyped(KeyEvent event) {
		TextInputControl control = (TextInputControl) event.getTarget();
		control.getStyleClass().remove("text-error");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		checkClientesEnGym();

	}

	private void checkClientesEnGym() {
		List<ClientesEnGym> clients = pm.getClientesEnGymList();
		clients.forEach(client -> {
			if (!Fechas.isToDay(client.getEntrada())) {
				Asistencia a = pm.getAsistencia(client.getEntrada());
				a.setSalida(Fechas.addTime(client.getEntrada(), Calendar.HOUR, 2));
				pm.updateEntity(a);
				pm.removeEntity(client);
			}
		});

	}

	private void startProgram() {
		Stage stage = new Stage();
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource(Paths.VIEWS_PATH + "VoltusSys.fxml"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.show();

		root.setOnMousePressed(e -> {
			x = e.getSceneX();
			y = e.getSceneY();
		});

		root.setOnMouseDragged(e -> {
			if(!stage.isMaximized()) {
			stage.setX(e.getScreenX() - x);
			stage.setY(e.getScreenY() - y);
			}
		});
	}

	private void hide() {
		Stage stage = (Stage) btnClose.getScene().getWindow();
		stage.hide();
	}

	public void show() {
		Stage stage = (Stage) btnClose.getScene().getWindow();
		stage.show();
		txtUser.setText("");
		pasPass.setText("");
	}

	public LogInController() {
		INSTANCE = this;
	}

	private boolean isItErrors() {
		List<Usuario> users = pm.getUsuariosList();

		for (Usuario user : users) {
			if (txtUser.getText().equals(user.getUsuario())) {

				if (pasPass.getText().equals(user.getContrasena())) {
					activeUser = user;
					return false;
				} else {
					pasPass.getStyleClass().add("text-error");
					return true;
				}
			}
		}
		pasPass.getStyleClass().add("text-error");
		txtUser.getStyleClass().add("text-error");
		return true;
	}

	public static LogInController getInstance() {
		return INSTANCE;
	}

	public Usuario getctiveUser() {
		return activeUser;
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

}
