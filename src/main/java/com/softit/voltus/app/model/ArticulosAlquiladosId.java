package com.softit.voltus.app.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class ArticulosAlquiladosId implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String articulo;
	private String id_cliente;
	
	public ArticulosAlquiladosId() {
		
	}

	public ArticulosAlquiladosId(String articulo, String id_cliente) {
		super();
		this.articulo = articulo;
		this.id_cliente = id_cliente;
	}

	public String getArticulo() {
		return articulo;
	}

	public void setArticulo(String articulo) {
		this.articulo = articulo;
	}

	public String getId_cliente() {
		return id_cliente;
	}

	public void setId_cliente(String id_cliente) {
		this.id_cliente = id_cliente;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((articulo == null) ? 0 : articulo.hashCode());
		result = prime * result + ((id_cliente == null) ? 0 : id_cliente.hashCode());
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
		ArticulosAlquiladosId other = (ArticulosAlquiladosId) obj;
		if (articulo == null) {
			if (other.articulo != null)
				return false;
		} else if (!articulo.equals(other.articulo))
			return false;
		if (id_cliente == null) {
			if (other.id_cliente != null)
				return false;
		} else if (!id_cliente.equals(other.id_cliente))
			return false;
		return true;
	}
	
	

}
