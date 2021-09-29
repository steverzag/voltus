package com.softit.voltus.app.model;

import java.text.DateFormat;
import java.util.Date;
import javax.persistence.*;

@Entity
public class Operaciones {

	private String id;
	private String acceso;
	private double valor;
	private String cliente;
	private String observacion;
	@Id
	private Date fecha;
	private boolean cancelada;
	
	@Transient
	private String clientName;
	@Transient
	private String formatFecha;
	@Transient
	private String canctostring;
	@Transient
	private PersistenceManager pm = PersistenceManager.getPersistenceInstace();
	
	public static final String COBRO_MEMB = "Cobro de Membrecia";
	public static final String ALQUILER_ART = "Alquiler de Articulo";
	public static final String EXTRACCION = "Extraccion";
	public static final String DEPOSITO = "Deposito";
	public static final String GASTO = "Gasto";

	public Operaciones() {
		// TODO Auto-generated constructor stub
	}

	public Operaciones(String id, String acceso, double valor, String cliente, String observacion, Date fecha,
			boolean cancelada) {
		super();
		this.id = id;
		this.acceso = acceso;
		this.valor = valor;
		this.cliente = cliente;
		this.observacion = observacion;
		this.fecha = fecha;
		this.cancelada = cancelada;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAcceso() {
		return acceso;
	}

	public void setAcceso(String acceso) {
		this.acceso = acceso;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public boolean isCancelada() {
		return cancelada;
	}

	public void setCancelada(boolean cancelada) {
		this.cancelada = cancelada;
	}

	public String getCanctostring() {
		setCanctostring();
		return canctostring;
	}

	public void setCanctostring() {

		this.canctostring = ((cancelada == true) ? "Si" : "No");
	}

	public String getClientName() {
		setClientName(cliente);
		return clientName;
	}

	public void setClientName(String ci) {
		try {
			this.clientName = pm.getClient(ci).getNombre();
		} catch (Exception e) {
			this.clientName = cliente;
		}
	}

	public String getFormatFecha() {
		setFormatFecha();
		return formatFecha;
	}

	public void setFormatFecha() {

		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
		this.formatFecha = df.format(fecha);
	}

	@Override
	public String toString() {
		return "Operaciones [id=" + id + ", acceso=" + acceso + ", valor=" + valor + ", cliente=" + cliente
				+ ", observacion=" + observacion + ", cancelada=" + cancelada + ", clientName=" + clientName
				+ ", formatFecha=" + formatFecha + "]";
	}
	
	public double getOperationCash(double caja) {

		if (id.equals(Operaciones.COBRO_MEMB) || id.equals(Operaciones.DEPOSITO) || id.equals(Operaciones.ALQUILER_ART))
			return caja + valor;
		return caja - valor;
	}

	public double getCancelOperationCash(double caja) {
		if (id.equals(Operaciones.COBRO_MEMB) || id.equals(Operaciones.DEPOSITO) || id.equals(Operaciones.ALQUILER_ART))
			return caja - valor;
		return caja + valor;
	}
}
