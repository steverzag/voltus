package com.softit.voltus.app.model;

import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "servicios")
public class Servicios {

	@Id
	@Column(unique = true)
	private String servicio = "";
	@Column(name = "diario")
	private double precioD = 0;
	@Column(name = "semanal")
	private double precioS = 0;
	@Column(name = "quincenal")
	private double precioQ = 0;
	@Column(name = "mensual")
	private double precioM = 0;
	@Column(name = "compD")
	private double precioCompD = 0;
	@Column(name = "compS")
	private double precioCompS = 0;
	@Column(name = "compQ")
	private double precioCompQ = 0;
	@Column(name = "compM")
	private double precioCompM = 0;
	private boolean compartido=false;
	
	@Transient
	private String compstring;
	@Transient
	public static final String F_P_DIARIO = "Diario";
	@Transient
	public static final String F_P_SEMANAL = "Semanal";
	@Transient
	public static final String F_P_QUINCENAL = "Quincenal";
	@Transient
	public static final String F_P_MENSUAL = "Mensual";
	
	public Servicios() {
		
	}

	public Servicios(String servicio, double precioD, double precioS, double precioQ, double precioM,
			double precioCompD, double precioCompS, double precioCompQ, double precioCompM, boolean compartido) {
		super();
		this.servicio = servicio;
		this.precioD = precioD;
		this.precioS = precioS;
		this.precioQ = precioQ;
		this.precioM = precioM;
		this.precioCompD = precioCompD;
		this.precioCompS = precioCompS;
		this.precioCompQ = precioCompQ;
		this.precioCompM = precioCompM;
		this.compartido = compartido;
	}
	
	public ArrayList<String> getFormasPago() {
		ArrayList<String> list = new ArrayList<String>();
		
		if(precioD>0) 
			list.add(F_P_DIARIO);
		if(precioS>0)
			list.add(F_P_SEMANAL);
		if(precioQ>0)
			list.add(F_P_QUINCENAL);
		if(precioM>0)
			list.add(F_P_MENSUAL);
		
		return list;
	}
	
	public String getFPago(double valor) {
		if(valor==precioD)
			return"Diario";
		else if(valor==precioS)
			return "Semanal";
		else if(valor==precioQ)
			return "Quincenal";
		else if(valor==precioM)
			return "Mensual";
		else
			return "";
		
	}

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public double getPrecioD() {
		return precioD;
	}

	public void setPrecioD(double precioD) {
		this.precioD = precioD;
	}

	public double getPrecioS() {
		return precioS;
	}

	public void setPrecioS(double precioS) {
		this.precioS = precioS;
	}

	public double getPrecioQ() {
		return precioQ;
	}

	public void setPrecioQ(double precioQ) {
		this.precioQ = precioQ;
	}

	public double getPrecioM() {
		return precioM;
	}

	public void setPrecioM(double precioM) {
		this.precioM = precioM;
	}

	public double getPrecioCompD() {
		return precioCompD;
	}

	public void setPrecioCompD(double precioCompD) {
		this.precioCompD = precioCompD;
	}

	public double getPrecioCompS() {
		return precioCompS;
	}

	public void setPrecioCompS(double precioCompS) {
		this.precioCompS = precioCompS;
	}

	public double getPrecioCompQ() {
		return precioCompQ;
	}

	public void setPrecioCompQ(double precioCompQ) {
		this.precioCompQ = precioCompQ;
	}

	public double getPrecioCompM() {
		return precioCompM;
	}

	public void setPrecioCompM(double precioCompM) {
		this.precioCompM = precioCompM;
	}

	public boolean isCompartido() {
		return compartido;
	}
	
	public void setCompartido(boolean compartido){
		this.compartido = compartido;
	}

	public void setCompartido() {
		if(precioCompD > 0 || precioCompS > 0 || precioCompQ > 0 || precioCompM > 0)
			compartido = true;
		else
			compartido = false;
	}

	@Override
	public String toString() {
		return "Servicios [servicio=" + servicio + ", precioD=" + precioD + ", precioS=" + precioS + ", precioQ="
				+ precioQ + ", precioM=" + precioM + ", precioCompD=" + precioCompD + ", precioCompS=" + precioCompS
				+ ", precioCompQ=" + precioCompQ + ", precioCompM=" + precioCompM + ", compartido=" + compartido + "]";
	}
	
	public String getCompstring() {
		setCompstring();
		return compstring;
	}
	
	public void setCompstring() {
		setCompartido();
		if(compartido)
			compstring = "Si";
		else
			compstring = "No";
	}

	public void update(Servicios newService) {
		
		this.servicio = newService.servicio;
		this.precioD = newService.precioD;
		this.precioS = newService.precioS;
		this.precioQ = newService.precioQ;
		this.precioM = newService.precioM;
		this.precioCompD = newService.precioCompD;
		this.precioCompS = newService.precioCompS;
		this.precioCompQ = newService.precioCompQ;
		this.precioCompM = newService.precioCompM;
		this.compartido = newService.compartido;
	}
	
	public double getvalorServicio(String fPago) {
		if(fPago.equals(F_P_DIARIO))
			return precioD;
		else if(fPago.equals(F_P_SEMANAL))
			return precioS;
		else if(fPago.equals(F_P_QUINCENAL))
			return precioQ;
		else if(fPago.equals(F_P_MENSUAL))
			return precioM;
		else
			return 0;
	}
	
	public double getvalorServicioComp(String fPago) {
		if(fPago.equals(F_P_DIARIO))
			return precioCompD;
		else if(fPago.equals(F_P_SEMANAL))
			return precioCompS;
		else if(fPago.equals(F_P_QUINCENAL))
			return precioCompQ;
		else if(fPago.equals(F_P_MENSUAL))
			return precioCompM;
		else
			return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		Servicios other = (Servicios) obj;
		if (servicio == null) {
			if (other.servicio != null)
				return false;
		} else if (!servicio.equals(other.servicio))
			return false;
		return true;
	}
}
