package com.softit.voltus.app.model;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

@Entity
public class Notificaciones implements Serializable {

	private static final long serialVersionUID = 7L;

	@Id
	@Embedded
	private ClientesEstadoId id;
	private String notificacion;
	private Date fechaVencimiento;
	private boolean leido;

	@OneToOne()
	@JoinColumns({ @JoinColumn(name = "servicio"), @JoinColumn(name = "ci") })
	private ClientesEstado state;
	@Transient
	private String nombreCliente;
	@Transient
	private ImageView img = new ImageView();

	public Notificaciones() {
	}

	public Notificaciones(ClientesEstadoId id, String notificacion, Date fechaVencimiento, boolean leido) {
		super();
		this.id = id;
		this.notificacion = notificacion;
		this.fechaVencimiento = fechaVencimiento;
		this.leido = leido;
	}

	public ClientesEstadoId getId() {
		return id;
	}

	public void setId(ClientesEstadoId id) {
		this.id = id;
	}

	public String getNotificacion() {
		return notificacion;
	}

	public void setNotificacion(String notificacion) {
		this.notificacion = notificacion;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public boolean isLeido() {
		return leido;
	}

	public void setLeido(boolean leido) {
		this.leido = leido;
	}

	public ImageView getImg() {
		String imgUrl = getState().getClient().getImgUrl();
		try {
			img.setImage(new Image(imgUrl));
			String s = new URL(imgUrl).toURI().getPath();
			if (!new File(s).exists())
				img.setImage(new Image("com/softit/voltus/app/view/images/icons8_decision_80px_1.png"));

		} catch (Exception e) {
			img.setImage(new Image("com/softit/voltus/app/view/images/icons8_decision_80px_1.png"));
		}

		img.setPreserveRatio(true);
		img.setFitHeight(60);
		return img;
	}

	public ClientesEstado getState() {
		return state;
	}

	public void setState(ClientesEstado state) {
		this.state = state;
	}

	public String getNombreCliente() {

		return getState().getClient().getNombre();
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
		Notificaciones other = (Notificaciones) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		DateFormat df = DateFormat.getDateInstance(Calendar.SHORT);

		String s = (fechaVencimiento == null) ? "null" : df.format(fechaVencimiento);
		return "Notificaciones [notificacion=" + notificacion + ", fechaVencimiento=" + s + ", nombreCliente="
				+ id.getCi() + "]";
	}

}
