package com.softit.voltus.app.model;

import javax.persistence.*;

@Entity
@Table(name = "DesglosesFianacieros")
public class DesgloseFinaciero {
	
	@Id
	private String id;
	private double gastos;
	private double ingresosPorArticulos;
	private double ingresosPorServicios;
	
	@OneToOne()
	@JoinColumn(name = "id", insertable = false, nullable = false)
	private CuadreMensual cuadre;
	
	@Transient
	private double ingresosTotales;
	
	@Transient
	private double ganancia;
	
	public DesgloseFinaciero() {
		
	}

	public DesgloseFinaciero(double gastos, double ingresosPorArticulos,
			double ingresosPorServicios) {
		super();
		this.gastos = gastos;
		this.ingresosPorArticulos = ingresosPorArticulos;
		this.ingresosPorServicios = ingresosPorServicios;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getGastos() {
		return gastos;
	}

	public void setGastos(double gastos) {
		this.gastos = gastos;
	}

	public double getIngresosPorArticulos() {
		return ingresosPorArticulos;
	}

	public void setIngresosPorArticulos(double ganaciasPorArticulos) {
		this.ingresosPorArticulos = ganaciasPorArticulos;
	}

	public double getIngresosPorServicios() {
		return ingresosPorServicios;
	}

	public void setIngresosPorServicios(double ganaciasPorServicios) {
		this.ingresosPorServicios = ganaciasPorServicios;
	}

	public CuadreMensual getCuadre() {
		return cuadre;
	}

	public void setCuadre(CuadreMensual cuadre) {
		this.cuadre = cuadre;
	}
	
	public double getIngresosTotales() {
		setIngresosTotales();
		return ingresosTotales;
	}
	
	private void setIngresosTotales() {
		this.ingresosTotales = ingresosPorArticulos + ingresosPorServicios;
	}
	
	public double getGanancia() {
		setIngresosTotales();
		setGanancia();
		return ganancia;
	}
	
	private void setGanancia() {
		this.ganancia = ingresosTotales - gastos;
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
		DesgloseFinaciero other = (DesgloseFinaciero) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
