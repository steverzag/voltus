package com.softit.voltus.app.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "CuadresMensuales")
public class CuadreMensual {
	@Id
	private String id;
	private Date FechaCreacion;
	private String pdfPath;
	private int diasHabiles;
	
	@OneToOne(mappedBy = "cuadre", cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id")
	private DesgloseFinaciero finansas;
	
	@OneToOne(mappedBy = "cuadre", cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id")
	private ActividadMensual actividad;
	
	public CuadreMensual() {
		// TODO Auto-generated constructor stub
	}

	public CuadreMensual(String id, Date fechaCreacion, String pdfPath, int diasHabiles) {
		super();
		this.id = id;
		FechaCreacion = fechaCreacion;
		this.pdfPath = pdfPath;
		this.diasHabiles = diasHabiles;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getFechaCreacion() {
		return FechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		FechaCreacion = fechaCreacion;
	}
	
	public String getPdfPath() {
		return pdfPath;
	}
	
	public void setPdfPath(String pdfPath) {
		this.pdfPath = pdfPath;
	}
	
	public int getDiasHabiles() {
		return diasHabiles;
	}

	public void setDiasHabiles(int diasHabiles) {
		this.diasHabiles = diasHabiles;
	}
	
	public DesgloseFinaciero getFinansas() {
		return finansas;
	}
	
	public void setFinansas(DesgloseFinaciero finansas) {
		finansas.setId(this.getId());
		this.finansas = finansas;
	}
	
	public ActividadMensual getActividad() {
		return actividad;
	}
	
	public void setActividad(ActividadMensual actividad) {
		actividad.setId(this.getId());
		this.actividad = actividad;
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
		CuadreMensual other = (CuadreMensual) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
