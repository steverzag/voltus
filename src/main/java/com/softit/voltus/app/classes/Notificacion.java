package com.softit.voltus.app.classes;

import java.util.Calendar;
import java.util.Date;

import com.softit.voltus.app.model.ClientesEstado;
import com.softit.voltus.app.model.Notificaciones;
import com.softit.voltus.app.model.Servicios;

public class Notificacion {

	public static Notificaciones getNotificacion(ClientesEstado state) {

		if (!isNotificable(state))
			return null;
		String sms = createMessage(state);
		Date fechaVencimiento = (state.getPagoHasta() != null) ? Fechas.addTime(state.getPagoHasta(), Calendar.MONTH, 1)
				: null;

		return new Notificaciones(state.getId(), sms, fechaVencimiento, false);

	}

	private static boolean isNotificable(ClientesEstado state) {

		if (state.getPagoHasta() == null)
			return true;
		
		if(state.getFpago().equals(Servicios.F_P_DIARIO))
			return false;
		
		Date toDay = new Date();
		boolean between = toDay.after(Fechas.restTime(state.getPagoHasta(), Calendar.DAY_OF_YEAR, 3)) 
				&& toDay.before(Fechas.addTime(state.getPagoHasta(), Calendar.MONTH, 1));
		
		if (between) 
			return true;
		
		return false;
	}

	private static String createMessage(ClientesEstado state) {

		if (state.getPagoHasta() == null)
			return "No ha Pagado por Primera Vez su Membresia " + state.getServicio();
		int days = Fechas.getDaysTo(state.getPagoHasta());
		String notificacion = "La Membresia de " + state.getServicio() + " Queda Inactiva en " + Math.abs(days)
				+ " Dias";
		if (days > 0) {
			notificacion = notificacion.replace(" Queda ", " Quedo ");
			notificacion = notificacion.replace(" en ", " Hace ");
		} else if (days == 0)
			notificacion = notificacion.substring(0, notificacion.indexOf(" en ")) + " Hoy";
		return notificacion;
	}

	public static void addtNotificacion(ClientesEstado state) {

		boolean leido = (state.getNot() != null) ? state.getNot().isLeido() : false;
		Notificaciones not = getNotificacion(state);
		if (not != null) {
			not.setLeido(leido);
			state.setNot(not);
		} else
			state.removeNot();
	}

}
