package com.softit.voltus.app.classes;

import java.util.ArrayList;

import com.softit.voltus.app.model.PersistenceManager;

public class Servicio {
	
	private static PersistenceManager pm = PersistenceManager.getPersistenceInstace();
	
	public static ArrayList<String> getServicioFormasDePago(String servicio){
		
		return pm.getServicio(servicio).getFormasPago();
	}
	public static double getPagoDeServicio() {
		double pago = 0;
		
		return pago;
	}

}
