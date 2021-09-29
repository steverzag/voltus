package com.softit.voltus.app.model;

import javax.persistence.*;

@Entity
public class ClientesInfoTecnica {

	@Id
	private String ci;
	private double cuello;
	private double cintura;
	private double cadera;
	private double peso;
	private double talla;
	private int frecCard;
	private int frecResp;
	private String meta;
	
	public ClientesInfoTecnica() {
		// TODO Auto-generated constructor stub
	}

	public ClientesInfoTecnica(String ci, double cuello, double cintura, double cadera, double peso, double talla,
			int frecCard, int frecResp, String meta) {
		super();
		this.ci = ci;
		this.cuello = cuello;
		this.cintura = cintura;
		this.cadera = cadera;
		this.peso = peso;
		this.talla = talla;
		this.frecCard = frecCard;
		this.frecResp = frecResp;
		this.meta = meta;
	}

	public String getCi() {
		return ci;
	}

	public void setCi(String ci) {
		this.ci = ci;
	}

	public double getCuello() {
		return cuello;
	}

	public void setCuello(double cuello) {
		this.cuello = cuello;
	}

	public double getCintura() {
		return cintura;
	}

	public void setCintura(double cintura) {
		this.cintura = cintura;
	}

	public double getCadera() {
		return cadera;
	}

	public void setCadera(double cadera) {
		this.cadera = cadera;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

	public double getTalla() {
		return talla;
	}

	public void setTalla(double talla) {
		this.talla = talla;
	}

	public int getFrecCard() {
		return frecCard;
	}

	public void setFrecCard(int frecCard) {
		this.frecCard = frecCard;
	}

	public int getFrecResp() {
		return frecResp;
	}

	public void setFrecResp(int frecResp) {
		this.frecResp = frecResp;
	}

	public String getMeta() {
		return meta;
	}

	public void setMeta(String meta) {
		this.meta = meta;
	}
	
	
}
