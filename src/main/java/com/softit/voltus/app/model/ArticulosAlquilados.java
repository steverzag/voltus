package com.softit.voltus.app.model;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ArticulosAlquilados {
	
	@Embedded
	@Id
	private ArticulosAlquiladosId id;
	private int cantidad;
	
	@ManyToOne()
	@JoinColumn(name = "id_cliente", insertable= false, updatable = false)
	private ClientesEnGym cliente;
	
	public ArticulosAlquilados() {
		id = new ArticulosAlquiladosId();
	}

	public ArticulosAlquilados(String articulo, String id_cliente, int cantidad) {
		super();
		id = new ArticulosAlquiladosId(articulo, id_cliente);
		this.cantidad = cantidad;
	}
	
	public String getArticulo() {
		return id.getArticulo();
	}

	public void setArticulo(String articulo) {
		id.setArticulo(articulo);
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	

	public ClientesEnGym getCliente() {
		return cliente;
	}

	public void setCliente(ClientesEnGym clientes) {
		this.cliente = clientes;
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
		ArticulosAlquilados other = (ArticulosAlquilados) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public boolean joinArticulos(ArticulosAlquilados articulo) {
		
		if(articulo.getArticulo().equals(id.getArticulo())) {
			this.cantidad += articulo.cantidad;
			return true;
		}
		return false;
	}
	
	
	
	
	
	
	
}
