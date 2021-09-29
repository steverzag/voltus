package com.softit.voltus.app.model;

import java.util.Date;
import javax.persistence.*;

@Entity
@Table
public class OperacionesCompartidas {

	private String servicio;
	private double valorCompartido;
	private double valorTotal;
	private boolean cancelada;
	@Id
	private Date fecha;
	
	public OperacionesCompartidas() {
		// TODO Auto-generated constructor stub
	}

	public OperacionesCompartidas(String servicio, double valorCompartido, double valorTotal, Date fecha) {
		super();
		this.servicio = servicio;
		this.valorCompartido = valorCompartido;
		this.valorTotal = valorTotal;
		this.fecha = fecha;
		this.cancelada = false;
	}

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public double getValor() {
		return valorCompartido;
	}

	public void setValor(double valorCompartido) {
		this.valorCompartido = valorCompartido;
	}

	public double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public double getValorCompartido() {
		return valorCompartido;
	}

	public void setValorCompartido(double valorCompartido) {
		this.valorCompartido = valorCompartido;
	}

	public boolean isCancelada() {
		return cancelada;
	}

	public void setCancelada(boolean cancelada) {
		this.cancelada = cancelada;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OperacionesCompartidas other = (OperacionesCompartidas) obj;
		if (fecha == null) {
			if (other.fecha != null)
				return false;
		} else if (!fecha.equals(other.fecha))
			return false;
		return true;
	}
	
}
