package com.softit.voltus.app.model;

import java.text.DateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Asistencia {
	@Column
	private String ci;
	@Id
	private Date entrada;
	@Column
	private Date salida;
	@Column
	private String servicio;
	@Column
	private String estado;

	@Transient
	private String cliente;
	@Transient
	private String entradaString;
	@Transient
	private String salidaString;
	@Transient
	private PersistenceManager pm = PersistenceManager.getPersistenceInstace();
	
	public Asistencia() {

	}

	public Asistencia(String ci, Date entrada, Date salida, String servicio, String estado) {
		super();
		this.ci = ci;
		this.entrada = entrada;
		this.salida = salida;
		this.servicio = servicio;
		this.estado = estado;
	}

	public String getCi() {
		return ci;
	}

	public void setCi(String ci) {
		this.ci = ci;
	}

	public Date getEntrada() {
		return entrada;
	}

	public void setEntrada(Date entrada) {
		this.entrada = entrada;
	}

	public Date getSalida() {
		return salida;
	}

	public void setSalida(Date salida) {
		this.salida = salida;
	}

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public String getCliente() {
		setCliente(ci);
		return this.cliente;
	}

	public void setCliente(String cliente) {
		try {
		this.cliente = pm.getClient(cliente).getNombre();
		}catch(Exception e) {
			this.cliente = ci;
		}
	}

	public String getEntradaString() {
		setEntradaString();
		return entradaString;
	}

	public void setEntradaString() {
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
		this.entradaString = df.format(entrada);
	}

	public String getSalidaString() {
		setSalidaString();
		return salidaString;
	}

	public void setSalidaString() {
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
		try {
		this.salidaString = df.format(salida);
		}catch(Exception e) {
			this.salidaString = "-----";
		}
	
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((entrada == null) ? 0 : entrada.hashCode());
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
		Asistencia other = (Asistencia) obj;
		if (entrada == null) {
			if (other.entrada != null)
				return false;
		} else if (!entrada.equals(other.entrada))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Asistencia [ci=" + ci + ", entrada=" + entrada.getTime() + ", salida=" + salida + ", servicio=" + servicio
				+ ", estado=" + estado + "]";
	}
	
	
}
