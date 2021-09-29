package com.softit.voltus.app.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.persistence.*;

import com.softit.voltus.app.classes.Clientes;

@Entity
public class ClientesInfoPersonal {
	
	@Id
	@Column(updatable = true)
	private String ci;
	private String nombre;
	private String sexo;
	private String direccion;
	private String telCasa;
	private String telMovil;
	private String telEmerg;
	private String ocupacion;
	private String imgUrl;
	
	@Transient
	private int edad;
	@Transient
	private int servicesTotal;
	@Transient
	private int activeServices;
	@Transient
	private String estado;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ci")
	private ClientesInfoTecnica infoTecnica;
	
	@OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private List<ClientesEstado> clientesEstado = new ArrayList<ClientesEstado>();
	public ClientesInfoPersonal() {
		
	}
	
	public ClientesInfoPersonal(String ci, String nombre, String sexo, String direccion, String telCasa,
			String telMovil, String telEmerg, String ocupacion, String imgUrl, ClientesInfoTecnica infoTecnica) {
		
		this.ci = ci;
		this.nombre = nombre;
		this.sexo = sexo;
		this.direccion = direccion;
		this.telCasa = telCasa;
		this.telMovil = telMovil;
		this.telEmerg = telEmerg;
		this.ocupacion = ocupacion;
		this.imgUrl = imgUrl;
		this.infoTecnica = infoTecnica;
	}
	
	private Date getBornDate(String CI) {

		int year = Integer.parseInt(CI.substring(0, 2));
		year += 2000;
		int month = Integer.valueOf(CI.substring(2, 4)) - 1;
		int day = Integer.valueOf(CI.substring(4, 6));
		GregorianCalendar gc = new GregorianCalendar(year, month, day);

		Date bornDate = gc.getTime();

		int compare = bornDate.compareTo(new Date());
		if (compare >= 0) {
			gc = new GregorianCalendar(year - 100, month, day);
			bornDate = gc.getTime();
		}
		return bornDate;
	}
	
	public int getEdad() {
		setEdad(getBornDate(ci));
		return edad;
	}

	private void setEdad(Date bornDate) {
		int edad = 0;

		GregorianCalendar actualGC = new GregorianCalendar();
		GregorianCalendar bornGC = new GregorianCalendar();
		bornGC.setTime(bornDate);

		edad = actualGC.get(Calendar.YEAR) - bornGC.get(Calendar.YEAR);
		int restaMonth = actualGC.get(Calendar.MONTH) - bornGC.get(Calendar.MONTH);
		int restaDay = actualGC.get(Calendar.DAY_OF_MONTH) - bornGC.get(Calendar.DAY_OF_MONTH);

		if (restaMonth < 0) {
			edad -= 1;
		} else if (restaMonth == 0 && restaDay < 0) {
			edad -= 1;
		}

		this.edad = edad;
	}

	public String getCi() {
		return ci;
	}

	public void setCi(String ci) {
		this.ci = ci;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelCasa() {
		return telCasa;
	}

	public void setTelCasa(String telCasa) {
		this.telCasa = telCasa;
	}

	public String getTelMovil() {
		return telMovil;
	}

	public void setTelMovil(String telMovil) {
		this.telMovil = telMovil;
	}

	public String getTelEmerg() {
		return telEmerg;
	}

	public void setTelEmerg(String telEmerg) {
		this.telEmerg = telEmerg;
	}

	public String getOcupacion() {
		return ocupacion;
	}

	public void setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		try {
		this.imgUrl = imgUrl;
		}catch (Exception e) {
		}
	}

	public ClientesInfoTecnica getInfoTecnica() {
		return infoTecnica;
	}
	
	public void setInfoTecnica(ClientesInfoTecnica infoTecnica) {
		this.infoTecnica = infoTecnica;
	}
	
	public List<ClientesEstado> getClientesEstado() {
		return clientesEstado;
	}
	
	public void setClientesEstado(List<ClientesEstado> clientesEstado) {
		this.clientesEstado = clientesEstado;
	}
	
	

	
	public int getServicesTotal() {
		try {
			servicesTotal = clientesEstado.size();
		} catch (Exception e) {
			return 0;
		}
		
		return servicesTotal;
	}

	public int getActiveServices() {
		activeServices = 0;
		try {
			for (int j = 0; j < clientesEstado.size(); j++) {
				if(clientesEstado.get(j).isActivo())
					activeServices++;
			}
		} catch (Exception e) {
			return 0;
		}
		
		return activeServices;
	}
	
	public String getEstado() {
		if(Clientes.isActivo(this))
			estado = "Activo";
		else
			estado = "Inactivo";
		return estado;
	}

	@Override
	public String toString() {
		return "ClientesInfoPersonal [ci=" + ci + ", nombre=" + nombre + ", sexo=" + sexo + ", direccion=" + direccion
				+ ", telCasa=" + telCasa + ", telMovil=" + telMovil + ", telEmerg=" + telEmerg + ", ocupacion="
				+ ocupacion + ", imgUrl=" + imgUrl + ", edad=" + edad + ", infoTecnica=" + infoTecnica
				+ ", clientesEstado=" + getClientesEstado() + "]";
	}
	
	public void addClientesEstado(ClientesEstado state){
		if(!clientesEstado.contains(state)) {
		state.setClient(this);
		clientesEstado.add(state);
		}
	}
	public void removeClientesEstado(ClientesEstado state) {
		if(clientesEstado.contains(state)) {
			clientesEstado.remove(state);
			state.setClient(null);
			}
	}

	public void update(ClientesInfoPersonal newClient) {
		
		this.ci = newClient.ci;
		this.nombre = newClient.nombre;
		this.sexo = newClient.sexo;
		this.direccion = newClient.direccion;
		this.telCasa = newClient.telCasa;
		this.telMovil = newClient.telMovil;
		this.telEmerg = newClient.telEmerg;
		this.ocupacion = newClient.ocupacion;
		this.imgUrl = newClient.imgUrl;
		this.infoTecnica = newClient.infoTecnica;
		this.clientesEstado = newClient.clientesEstado;
		
	}
	
}
