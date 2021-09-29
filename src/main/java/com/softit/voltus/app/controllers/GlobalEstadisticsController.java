package com.softit.voltus.app.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.softit.voltus.app.model.CuadreMensual;
import com.softit.voltus.app.model.PersistenceManager;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;

public class GlobalEstadisticsController implements Initializable {

	@FXML
	private BarChart<String, Integer> chartClients;

	@FXML
	private LineChart<String, Double> chartFinanzas;

	private PersistenceManager pm = PersistenceManager.getPersistenceInstace();
	private List<CuadreMensual> cuadres;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		cuadres = pm.getCuadreMensualList();
		initCharts();
		fixChartsWidth();
	}

	private void fixChartsWidth() {
		int widthAdding = 0;
		if (cuadres.size() > 8) {
			widthAdding = (cuadres.size() - 8) * 50;
		}
		chartClients.setPrefWidth(chartClients.getPrefWidth() + widthAdding);
		chartFinanzas.setPrefWidth(chartFinanzas.getPrefWidth() + widthAdding);
	}

	private void initCharts() {

		XYChart.Series<String, Integer> clientsData = new XYChart.Series<>();
		XYChart.Series<String, Double> ingresosData = new XYChart.Series<>();
		XYChart.Series<String, Double> gastosData = new XYChart.Series<>();
		XYChart.Series<String, Double> gananciaData = new XYChart.Series<>();

		ingresosData.setName("Ingresos");
		gastosData.setName("Gastos");
		gananciaData.setName("Ganacia");

		cuadres.forEach(cuadre -> {
			clientsData.getData().add(getClientsData(cuadre));
			ingresosData.getData().add(getIngresosData(cuadre));
			gastosData.getData().add(getGastosData(cuadre));
			gananciaData.getData().add(getGananciaData(cuadre));
		});
		chartClients.getData().add(clientsData);
		chartFinanzas.getData().add(ingresosData);
		chartFinanzas.getData().add(gananciaData);
		chartFinanzas.getData().add(gastosData);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Data<String, Integer> getClientsData(CuadreMensual cuadre) {

		return new XYChart.Data(cuadre.getId(), cuadre.getActividad().getClientesActivos());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Data<String, Double> getIngresosData(CuadreMensual cuadre) {

		double ingresos = cuadre.getFinansas().getIngresosTotales();
		return new XYChart.Data(cuadre.getId(), ingresos);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Data<String, Double> getGastosData(CuadreMensual cuadre) {

		return new XYChart.Data(cuadre.getId(), cuadre.getFinansas().getGastos());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Data<String, Double> getGananciaData(CuadreMensual cuadre) {
		double ganancia = cuadre.getFinansas().getGanancia();
		return new XYChart.Data(cuadre.getId(), ganancia);
	}

}