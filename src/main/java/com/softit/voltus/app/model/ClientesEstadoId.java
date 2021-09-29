package com.softit.voltus.app.model;

import java.io.Serializable;
import javax.persistence.Embeddable;

@Embeddable
public class ClientesEstadoId implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String ci;
	private String servicio;
	
	public ClientesEstadoId() {
		// TODO Auto-generated constructor stub
	}
	
	
	public ClientesEstadoId(String ci, String servicio) {
		super();
		this.ci = ci;
		this.servicio = servicio;
	}

	public String getCi() {
		return ci;
	}

	public void setCi(String ci) {
		this.ci = ci;
	}

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ci == null) ? 0 : ci.hashCode());
		result = prime * result + ((servicio == null) ? 0 : servicio.hashCode());
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
		ClientesEstadoId other = (ClientesEstadoId) obj;
		if (ci == null) {
			if (other.ci != null)
				return false;
		} else if (!ci.equals(other.ci))
			return false;
		if (servicio == null) {
			if (other.servicio != null)
				return false;
		} else if (!servicio.equals(other.servicio))
			return false;
		return true;
	}
	
	

}
