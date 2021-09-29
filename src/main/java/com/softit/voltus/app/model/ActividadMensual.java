package com.softit.voltus.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class ActividadMensual {

	@Id
	private String id;
	@Column
	private int clientesActivos;
	@Column
	private int clientesInactivos;
	@Column
	private String horarioConcurrido1;
	@Column
	private String horarioConcurrido2;
	@Column
	private String horarioConcurrido3;
	@Column
	private int clientesHorario1;
	@Column
	private int clientesHorario2;
	@Column
	private int clientesHorario3;
	
	@OneToOne()
	@JoinColumn(name = "id", insertable = false, nullable = false)
	private CuadreMensual cuadre;
	
	public ActividadMensual() {
		
	}

	public ActividadMensual(String id, int clientesActivos, int clientesInactivos, String horarioConcurrido1,
			String horarioConcurrido2, String horarioConcurrido3, int clientesHorario1, int clientesHorario2, int clintesHorario3) {
		super();
		this.id = id;
		this.clientesActivos = clientesActivos;
		this.clientesInactivos = clientesInactivos;
		this.horarioConcurrido1 = horarioConcurrido1;
		this.horarioConcurrido2 = horarioConcurrido2;
		this.horarioConcurrido3 = horarioConcurrido3;
		this.clientesHorario1 = clientesHorario1;
		this.clientesHorario2 = clientesHorario2;
		this.clientesHorario3 = clintesHorario3;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getClientesActivos() {
		return clientesActivos;
	}

	public void setClientesActivos(int clientesActivos) {
		this.clientesActivos = clientesActivos;
	}

	public int getClientesInactivos() {
		return clientesInactivos;
	}

	public void setClientesInactivos(int clientesInactivos) {
		this.clientesInactivos = clientesInactivos;
	}

	public String getHorarioConcurrido1() {
		return horarioConcurrido1;
	}

	public void setHorarioConcurrido1(String horarioConcurrido1) {
		this.horarioConcurrido1 = horarioConcurrido1;
	}

	public String getHorarioConcurrido2() {
		return horarioConcurrido2;
	}

	public void setHorarioConcurrido2(String horarioConcurrido2) {
		this.horarioConcurrido2 = horarioConcurrido2;
	}

	public String getHorarioConcurrido3() {
		return horarioConcurrido3;
	}

	public void setHorarioConcurrido3(String horarioConcurrido3) {
		this.horarioConcurrido3 = horarioConcurrido3;
	}
	
	public int getClientesHorario1() {
		return clientesHorario1;
	}

	public void setClientesHorario1(int clientesHorario1) {
		this.clientesHorario1 = clientesHorario1;
	}

	public int getClientesHorario2() {
		return clientesHorario2;
	}

	public void setClientesHorario2(int clientesHorario2) {
		this.clientesHorario2 = clientesHorario2;
	}

	public int getClientesHorario3() {
		return clientesHorario3;
	}

	public void setClientesHorario3(int clientesHorario3) {
		this.clientesHorario3 = clientesHorario3;
	}
	
	public CuadreMensual getCuadre() {
		return cuadre;
	}

	public void setCuadre(CuadreMensual cuadre) {
		this.cuadre = cuadre;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		ActividadMensual other = (ActividadMensual) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ActividadMensual [id=" + id + ", clientesActivos=" + clientesActivos + ", clientesInactivos="
				+ clientesInactivos + ", horarioConcurrido1=" + horarioConcurrido1 + ", horarioConcurrido2="
				+ horarioConcurrido2 + ", horarioConcurrido3=" + horarioConcurrido3 + ", clientesHorario1="
				+ clientesHorario1 + ", clientesHorario2=" + clientesHorario2 + ", clientesHorario3=" + clientesHorario3
				+ ", cuadre=" + cuadre + "]";
	}
	
	
}
