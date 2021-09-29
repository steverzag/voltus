package com.softit.voltus.app.model;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Entity;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

public class ORMLitePersistenceManager {
	private final String url = "jdbc:sqlite:voltusDB.db";
	private ConnectionSource connectionSource;
	@SuppressWarnings("rawtypes")
	private HashMap<Class, Dao> daoMap = new HashMap<>();
	
	public ORMLitePersistenceManager() {
		try {
			connectionSource = new JdbcConnectionSource(url);
			initDaoMap();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void initDaoMap() throws SQLException {
		
		Dao<Caja, Integer> cajaDao = DaoManager.createDao(connectionSource, Caja.class);
		Dao<ClientesInfoPersonal, String> clientesDao = DaoManager.createDao(connectionSource, ClientesInfoPersonal.class);
		Dao<ActividadMensual, String> actividadDao = DaoManager.createDao(connectionSource, ActividadMensual.class);
		Dao<Asistencia, Date> asistenciaDao = DaoManager.createDao(connectionSource, Asistencia.class);
		
		daoMap.put(Caja.class, cajaDao);
		daoMap.put(ClientesInfoPersonal.class, clientesDao);
		daoMap.put(ActividadMensual.class, actividadDao);
		daoMap.put(Asistencia.class, asistenciaDao);
		
		Caja c1 = cajaDao.queryForAll().get(0);
		System.out.println(c1.getSaldo());
		
	}
	
	@SuppressWarnings({ "unchecked" })
	private void addEntity(Object entiy) throws SQLException {
		
		daoMap.get(Entity.class).create(entiy);
	}
	@SuppressWarnings("unchecked")
	private Object get(Class<?> c, String id) throws SQLException {
		
		return daoMap.get(c).queryForId(id);
	}
	
	@SuppressWarnings("unchecked")
	private Object get(Class<Asistencia> c, Date date) throws SQLException {
		
		return daoMap.get(c).queryForId(date);
	}
	
	private List<?> getList(Class<?> c) throws SQLException{
		return daoMap.get(c).queryForAll();
	}
	
	//------------------------------
	
	public ClientesInfoPersonal getClientesInfoPersonal(String ci) throws SQLException {
		
		return (ClientesInfoPersonal) get(ClientesInfoPersonal.class, ci);
	}
	
	@SuppressWarnings("unchecked")
	public List<ClientesInfoPersonal> getClientesInfoPersonalList() throws SQLException{
		
		return (List<ClientesInfoPersonal>) getList(ClientesInfoPersonal.class);
	}
		
	public ActividadMensual getActividadMensual(String id) throws SQLException {
		
		return (ActividadMensual) get(ActividadMensual.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<ActividadMensual> getActividadMensualList() throws SQLException{
		
		return (List<ActividadMensual>) getList(ActividadMensual.class);
	}
	
	
	public Asistencia getAsistencia(Date date) throws SQLException {
		
		return (Asistencia) get(Asistencia.class, date);
	}
	
	@SuppressWarnings("unchecked")
	public List<Asistencia> getAsistenciaList() throws SQLException {
		
		return (List<Asistencia>) getList(Asistencia.class);
	}

	public static void main(String[] args) {
		ORMLitePersistenceManager orm = new ORMLitePersistenceManager();
		try {
			System.out.println(orm==null);
			orm.getClientesInfoPersonal("93022010981");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}
