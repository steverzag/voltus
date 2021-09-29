package com.softit.voltus.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "caja")
public class Caja {
	
	@Id
	int id;
	@Column
	private double saldo;
	
	public Caja() {
		
	}

	public Caja(double saldo) {
		super();
		this.saldo = saldo;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
	
}
