package com.softit.voltus.app.model;

import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

@Entity
public class ClientesEnGym{
	
	@Id
	private String ci;
	private String nombre;
	private String imgurl;
	private Date entrada;
	private String servicio;
	private String estado;

	@Transient
	private String fecha;
	@Transient
	private ImageView img = new ImageView();
	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private List<ArticulosAlquilados> articulos = new ArrayList<ArticulosAlquilados>();

	public ClientesEnGym() {
		// TODO Auto-generated constructor stub
	}

	public ClientesEnGym(String ci, String nombre, String imgurl, Date entrada, String servicio, String estado) {
		super();
		this.ci = ci;
		this.nombre = nombre;
		this.imgurl = imgurl;
		this.entrada = entrada;
		this.servicio = servicio;
		this.estado = estado;
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

	public Date getEntrada() {
		return entrada;
	}

	public void setEntrada(Date entrada) {
		this.entrada = entrada;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String isEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public void setArticulos(List<ArticulosAlquilados> articulos) {
		this.articulos = articulos;
	}

	public void addArticulo(ArticulosAlquilados articulo) {

		if (!articulos.contains(articulo)) {
			articulo.setCliente(this);
			articulos.add(articulo);
		}
	}

	public void removeArticulo(ArticulosAlquilados articulo) {
		if (articulos.contains(articulo)) {
			articulos.remove(articulo);
			articulo.setCliente(null);
		}
	}

	public List<ArticulosAlquilados> getArticulos() {
		return articulos;
	}

	public String getFecha() {
		if (entrada != null) {
			setFecha();
			return fecha;
		}
		return "";
	}

	public void setFecha() {

		DateFormat df = DateFormat.getTimeInstance(DateFormat.SHORT);
		this.fecha = df.format(entrada);
	}

	public ImageView getImg() {
		setImg();
		return img;
	}

	public void setImg() {
		img.setFitHeight(40);
		try {
		img.setImage(new Image(imgurl));
		String s = new URL(imgurl).toURI().getPath();
		if (!new File(s).exists())
			img.setImage(new Image("com/softit/voltus/app/view/images/icons8_decision_80px_1.png"));
		}catch(Exception e) {
			img.setImage(new Image("com/softit/voltus/app/view/images/icons8_decision_80px_1.png"));
		}
		img.setPreserveRatio(true);
	}

	public String getEstado() {
		return estado;
	}
	
	

}
