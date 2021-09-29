package com.softit.voltus.app.classes;

import java.util.Date;
import com.softit.voltus.app.model.PersistenceManager;

public class Asistence {

	private static PersistenceManager pm = PersistenceManager.getPersistenceInstace();
	public static int getEntradasSinCreditos(String ci, String servicio, Date pagoHasta) {
		return pm.getEntradasSinCreditoList(ci,servicio, pagoHasta).size();
	}

}
