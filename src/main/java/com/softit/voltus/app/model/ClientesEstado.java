package com.softit.voltus.app.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.softit.voltus.app.classes.Asistence;
import com.softit.voltus.app.classes.Fechas;

@Entity
public class ClientesEstado implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Embedded
	private ClientesEstadoId id;
	private String fpago;
	private Date pagoDesde;
	private Date pagoHasta;

	@Transient
	private boolean activo;
	@Transient
	private String activostring;
	@Transient
	private String pagoHastaString;
	@Transient
	private String membresia;
	
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "state", orphanRemoval = true)
	@JoinColumns({ @JoinColumn(name = "ci"), @JoinColumn(name = "servicio") })
	private Notificaciones not;

	@ManyToOne()
	@JoinColumn(name = "ci", insertable = false, updatable = false)
	private ClientesInfoPersonal client;

	public ClientesEstado() {
		this.id = new ClientesEstadoId();
	}

	public ClientesEstado(ClientesEstadoId id, String fPago, Date pagoDesde, Date pagoHasta) {
		super();
		this.id = new ClientesEstadoId();
		;
		this.fpago = fPago;
		this.pagoDesde = pagoDesde;
		this.pagoHasta = pagoHasta;
	}

	public void setId(ClientesEstadoId id) {
		this.id = id;
	}

	public ClientesEstadoId getId() {
		return id;
	}

	public void setActivo() {

		if (pagoHasta != null) {
			GregorianCalendar limitDate = new GregorianCalendar();
			limitDate.setTime(pagoHasta);
			limitDate.set(Calendar.MONTH, limitDate.get(Calendar.MONTH) + 1);

			int compare = new Date().compareTo(limitDate.getTime());
			this.activo = ((compare < 0) ? true : false);
		} else
			this.activo = false;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public boolean isActivo() {
		setActivo();
		return this.activo;
	}

	public String getFpago() {
		return fpago;
	}

	public void setFpago(String fPago) {
		this.fpago = fPago;
	}

	public Date getPagoDesde() {
		return pagoDesde;
	}

	public void setPagoDesde(Date pagoDesde) {
		this.pagoDesde = pagoDesde;
	}

	public Notificaciones getNot() {
		return not;
	}

	public void setNot(Notificaciones not) {

		this.not = not;
		if (not != null)
			this.not.setState(this);

	}

	public void removeNot() {

		if (not != null) {
			not.setState(null);
			this.setNot(null);
		}
	}

	public boolean containsNot(Notificaciones not) {
		return this.not == not;
	}

	public String getCi() {
		return id.getCi();
	}

	public void setCi(String ci) {
		this.id.setCi(ci);
	}

	public String getServicio() {
		return id.getServicio();
	}

	public void setServicio(String servicio) {
		this.id.setServicio(servicio);
	}
	
	public String getMembresia() {
		return id.getServicio();
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
		ClientesEstado other = (ClientesEstado) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Date getPagoHasta() {
		return pagoHasta;
	}

	public void setPagoHasta(Date pagoHasta) {
		this.pagoHasta = pagoHasta;
	}

	public ClientesInfoPersonal getClient() {
		return client;
	}

	public void setClient(ClientesInfoPersonal client) {
		this.client = client;
	}

	public String getActivostring() {
		setActivostring();
		return activostring;
	}

	public void setActivostring() {
		this.activostring = (isActivo() == true) ? "Activo" : "Inactivo";
		if (activo) {
			int compare = new Date().compareTo(pagoHasta);
			if (compare > 0)
				this.activostring += " Sin Credito";
		}
	}

	public String getPagoHastaString() {
		setPagoHastaString();
		return pagoHastaString;
	}
	
	public void setPagoHastaString() {
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
		try {
			this.pagoHastaString = df.format(pagoHasta);
		} catch (Exception e) {
			this.pagoHastaString = "-----";
		}
		
	}
	@Override
	public String toString() {
		return "ClientesEstado [ci=" + id.getCi() + ", servicio=" + id.getServicio() + ", fPago=" + fpago + ", activo="
				+ isActivo() + "]";
	}

	public void getPayd(String value) {
		pagoDesde = new Date();
		if (value.equals(Servicios.F_P_DIARIO)) {
			updatePagoHasta(Calendar.DAY_OF_YEAR, 1);
		} else if (value.equals(Servicios.F_P_SEMANAL)) {
			updatePagoHasta(Calendar.DAY_OF_YEAR, 7);
		} else if (value.equals(Servicios.F_P_QUINCENAL)) {
			updatePagoHasta(Calendar.DAY_OF_YEAR, 15);
		} else {
			updatePagoHasta(Calendar.MONTH, 1);
		}
	}

	public void getUnPayd(String value) {
		if (value.equals(""))
			value = fpago;
		if (value.equals(Servicios.F_P_DIARIO))
			cancelPagoHasta(Calendar.DAY_OF_YEAR, 1);
		else if (value.equals(Servicios.F_P_SEMANAL))
			cancelPagoHasta(Calendar.DAY_OF_YEAR, 7);
		else if (value.equals(Servicios.F_P_QUINCENAL))
			cancelPagoHasta(Calendar.DAY_OF_YEAR, 15);
		else if (value.equals(Servicios.F_P_MENSUAL))
			cancelPagoHasta(Calendar.MONTH, 1);

	}

	private void updatePagoHasta(int calendarField, int value) {

		int rest = Asistence.getEntradasSinCreditos(id.getCi(), id.getServicio(), pagoHasta);
		if (activo) {
			pagoHasta = Fechas.addTime(pagoHasta, calendarField, value);
		} else {
			pagoHasta = Fechas.addTime(new Date(), calendarField, value);
		}
		pagoHasta = Fechas.restTime(pagoHasta, Calendar.DAY_OF_YEAR, rest);
	}

	private void cancelPagoHasta(int calendarField, int value) {

		pagoHasta = Fechas.restTime(pagoHasta, calendarField, value);
		pagoDesde = Fechas.restTime(pagoDesde, calendarField, value);
	}
}
