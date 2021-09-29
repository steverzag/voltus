package com.softit.voltus.app.classes;

import java.util.ArrayList;
import java.util.Date;
import com.softit.voltus.app.model.ActividadMensual;
import com.softit.voltus.app.model.CuadreMensual;
import com.softit.voltus.app.model.DesgloseFinaciero;
import com.softit.voltus.app.model.PersistenceManager;

public class Cuadre {
	
	private PersistenceManager pm = PersistenceManager.getPersistenceInstace();
	private ArrayList<CuadreMensual> cuadresPendientes;
	
	public Cuadre() {
		
	}
	
	public ArrayList<CuadreMensual> getCuadresPendientes() {
		generateCuadresPendientes();
		return cuadresPendientes;
	}
	
	public void createCuadreMensual(CuadreMensual c){
		
		String s = c.getId().replaceAll(" de ", "").replaceAll("[^\\sa-zA-Z]", "");
		int year = Integer.parseInt(c.getId().replaceAll("[^\\d]", ""));
		int month = Fechas.getMonth(s);
		
		c.setFechaCreacion(new Date());
		c.setDiasHabiles(pm.getDiasHabilesAt(month, year));
		c.setFinansas(generateFinansas(month, year));
		c.setActividad(generateActividad(month, year));
		
	}
	
	private ActividadMensual generateActividad(int month, int year) {
		
		int clientesActivos = 0;
		clientesActivos = pm.getClientsActives();
		ActividadMensual actividad = pm.getActividadMensualAt(month, year);
		actividad.setClientesActivos(clientesActivos);
		
 		return actividad;
	}

	private DesgloseFinaciero generateFinansas(int month, int year) {
				
		double ingresosPorArticulos = (pm.getIngresosAlqAt(month, year) != null) ? pm.getIngresosAlqAt(month, year) : 0;
		double ingresosPorServicios = (pm.getIngresosCMembAt(month, year) != null) ? pm.getIngresosCMembAt(month, year) : 0;
		double gastos = pm.getGastosAt(month, year);
		gastos+=pm.getValorCompOPCAt(month, year);
		return new DesgloseFinaciero(gastos, ingresosPorArticulos, ingresosPorServicios);
	}

	public void generateCuadresPendientes() {
		
		cuadresPendientes = pm.getCuadresMensualesPendientes();
		cuadresPendientes.forEach(c -> createCuadreMensual(c));
	}
	

}
