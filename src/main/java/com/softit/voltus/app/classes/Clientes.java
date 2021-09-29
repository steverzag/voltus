package com.softit.voltus.app.classes;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.softit.voltus.app.model.ClientesInfoPersonal;

public class Clientes {

	public static String getClientCI(String clientName) {

		return clientName.substring(clientName.indexOf('-') + 1);
	}

	public static boolean isValidCI(String CI) {

		boolean b = true;
		Set<Integer> month31 = new HashSet<Integer>();
		month31.addAll(get31sMonths());

		Set<Integer> month30 = new HashSet<Integer>();
		month30.addAll(get30sMonths());

		int year = Integer.valueOf(CI.substring(0, 2));
		int month = Integer.valueOf(CI.substring(2, 4));
		int day = Integer.valueOf(CI.substring(4, 6));

		if (day < 1 || month < 1 || month > 12) {
			b = false;
		} else if (month31.contains(month) && day > 31) {
			b = false;
		} else if (month30.contains(month) && day > 30) {
			b = false;
		} else if (month == 2 && day > 28) {
			if (day == 29 && year % 4 == 0) {
				b = true;
			}
		}
		return b;
	}

	private static HashSet<Integer> get30sMonths() {
		// TODO Auto-generated method stub
		HashSet<Integer> set = new HashSet<Integer>();
		set.add(4);
		set.add(6);
		set.add(9);
		set.add(11);

		return set;
	}

	private static HashSet<Integer> get31sMonths() {
		// TODO Auto-generated method stub
		HashSet<Integer> set = new HashSet<Integer>();
		set.add(1);
		set.add(3);
		set.add(5);
		set.add(7);
		set.add(8);
		set.add(10);
		set.add(12);

		return set;
	}

	public static boolean isActivo(ClientesInfoPersonal client) {

		Date d = null;
		
		for (int i = 0; i < client.getClientesEstado().size(); i++) {
			Date pagoHasta = client.getClientesEstado().get(i).getPagoHasta();
			if(pagoHasta == null)
				continue;
			if(d == null || pagoHasta.after(d))
				d = pagoHasta;
		}
		if(d == null || new Date().after(Fechas.addTime(d, Calendar.MONTH, 1)))
			return false;
		return true;
	}

}
