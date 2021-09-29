package com.softit.voltus.app.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.softit.voltus.app.classes.Fechas;
import com.softit.voltus.app.classes.Notificacion;
import com.softit.voltus.app.classes.Paths;

public class PersistenceManager {

	private static PersistenceManager INSTANCE;
	private static EntityManagerFactory factory = Persistence.createEntityManagerFactory("voltusDB");
	
	public PersistenceManager() {
		Map<String, Object> props = factory.getProperties();
		props.put("hibernate.connection.url", Paths.getDBURL());
		for (String s : props.keySet()) {
			System.out.println(s + ": " + props.get(s));
		}
	}

	public static void main(String[] args) {
		PersistenceManager p = getPersistenceInstace();
	}

	public static PersistenceManager getPersistenceInstace() {

		if (INSTANCE == null)
			INSTANCE = new PersistenceManager();
		return INSTANCE;
	}

	public void addEntity(Object entity) {

		EntityManager em = factory.createEntityManager();

		em.getTransaction().begin();
		em.persist(entity);
		em.getTransaction().commit();
		em.close();
	}

	private Object get(Class<?> clase, String id) {

		EntityManager em = factory.createEntityManager();
		Object entity = em.find(clase, id);
		em.close();
		return entity;
	}

	private Object get(Class<?> clase, int id) {

		EntityManager em = factory.createEntityManager();
		Object entity = em.find(clase, id);
		em.close();
		return entity;
	}

	private Object get(Class<?> clase, Date date) {

		EntityManager em = factory.createEntityManager();
		Object entity = em.find(clase, date);
		em.close();
		return entity;
	}

	@SuppressWarnings("unused")
	private Object get(Class<?> clase, ClientesEstadoId id) {

		EntityManager em = factory.createEntityManager();
		Object entity = em.find(clase, id);
		em.close();
		return entity;
	}

	private Object getList(String entityName) {

		EntityManager em = factory.createEntityManager();
		@SuppressWarnings("unchecked")
		ArrayList<Object> list = (ArrayList<Object>) em.createQuery("from " + entityName).getResultList();
		em.close();

		return list;
	}

	public void updateEntity(Object entity) {

		EntityManager em = factory.createEntityManager();

		em.getTransaction().begin();
		em.merge(entity);
		em.getTransaction().commit();
		em.close();
	}

	public void removeEntity(Object entity) {

		EntityManager em = factory.createEntityManager();

		em.getTransaction().begin();
		entity = em.merge(entity);
		em.remove(entity);
		em.getTransaction().commit();
		em.close();
	}

	// ClienesInfoPersonal

	public int getClientsActives() {

		EntityManager em = factory.createEntityManager();
		Query query = em.createNativeQuery(
				"select count(*) from ClientesInfoPersonal as c where c.ci in (select ci from ClientesEstado as ce where strftime('%s', ce.pagoHasta / 1000, 'unixepoch', '+1 month') > strftime('%s', 'now')) ");
		Integer i = (Integer) query.getSingleResult();
		em.close();
		return i == null ? 0 : i;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<ClientesInfoPersonal> getClientsList() {

		return (ArrayList<ClientesInfoPersonal>) getList("ClientesInfoPersonal");
	}

	public ClientesInfoPersonal getClient(String ci) {

		return (ClientesInfoPersonal) get(ClientesInfoPersonal.class, ci);
	}

	public ArrayList<String> getClientsName() {

		ArrayList<String> clientsName = new ArrayList<>();
		this.getClientsList().forEach(client -> clientsName.add(client.getNombre() + "-" + client.getCi()));

		return clientsName;
	}

	@SuppressWarnings("unchecked")
	public List<Servicios> getClientesEstadoList(String servicio) {
		EntityManager em = factory.createEntityManager();
		String consult = "select c from ClientesEstado c where c.id.servicio like :servicio";
		List<Servicios> list = em.createQuery(consult).setParameter("servicio", servicio).getResultList();
		em.close();
		return list;
	}

	public ClientesEstado getCLientesEstado(String ci, String servicio) {

		EntityManager em = factory.createEntityManager();
		String consult = "select c from ClientesEstado c where c.id.ci like :ci and c.id.servicio like :servicio";

		ClientesEstado state = (ClientesEstado) em.createQuery(consult).setParameter("ci", ci)
				.setParameter("servicio", servicio).getSingleResult();
		em.close();
		return state;
	}

	// Servicios

	@SuppressWarnings("unchecked")
	public ArrayList<Servicios> getServiciosList() {

		return (ArrayList<Servicios>) getList("Servicios");
	}

	public Servicios getServicio(String servicio) {

		return (Servicios) get(Servicios.class, servicio);
	}

	public ArrayList<String> getServiciosNameList() {
		ArrayList<String> list = new ArrayList<>();
		getServiciosList().forEach(serv -> list.add(serv.getServicio()));
		return list;
	}

	// Operaciones

	@SuppressWarnings("unchecked")
	public ArrayList<Operaciones> getOperacionesList() {

		return (ArrayList<Operaciones>) getList("Operaciones order by fecha desc");
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Operaciones> getOperacionesListUntil(int month, int year) {
		GregorianCalendar fecha = new GregorianCalendar(year, month, 1);

		String consult = "SELECT op from Operaciones op where op.fecha < :fecha";
		EntityManager em = factory.createEntityManager();
		
		ArrayList<Operaciones> list = (ArrayList<Operaciones>) em.createQuery(consult).setParameter("fecha", fecha.getTime()).getResultList();
		em.close();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Operaciones> getOperacionesListAt(int month, int year) {
		GregorianCalendar desde = new GregorianCalendar(year, month, 1);
		GregorianCalendar hasta = new GregorianCalendar(year, month + 1, 1);

		String consult = "SELECT op from Operaciones op where op.fecha between :desde and :hasta";
		EntityManager em = factory.createEntityManager();
		ArrayList<Operaciones> list = (ArrayList<Operaciones>) em.createQuery(consult).setParameter("desde", desde.getTime())
				.setParameter("hasta", hasta.getTime()).getResultList();
		em.close();
		return list;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Operaciones> getOperacionesListAt(int month, int year, String id) {
		GregorianCalendar desde = new GregorianCalendar(year, month, 1);
		GregorianCalendar hasta = new GregorianCalendar(year, month + 1, 1);

		String consult = "select op from Operaciones op where op.id like :id and op.fecha between :desde and :hasta and op.cancelada = 0 order by op.fecha desc";
		EntityManager em = factory.createEntityManager();
		ArrayList<Operaciones> list = (ArrayList<Operaciones>) em.createQuery(consult).setParameter("id", id)
				.setParameter("desde", desde.getTime()).setParameter("hasta", hasta.getTime()).getResultList();
		em.close();
		return list;
	}

	public HashSet<String> getOperacinoesIdSet() {

		HashSet<String> opIds = new HashSet<>();

		getOperacionesList().forEach(item -> opIds.add(item.getId()));
		return opIds;
	}

	@SuppressWarnings("unchecked")
	public List<Operaciones> getOperacionesList(String client, String user, String op, Date desde, Date hasta) {

		String consult = "select op from Operaciones op";
		EntityManager em = factory.createEntityManager();

		desde = (desde == null ? getOperacionesMinDate() : desde);
		hasta = (hasta == null ? getOperacionesMaxDate() : hasta);

		if (client.length() > 0)
			consult += " where op.cliente like :client";
		else
			consult += " where op.cliente not like :client";
		if (user.length() > 0)
			consult += " and op.acceso like :user";
		else
			consult += " and op.acceso not like :user";
		if (op.length() > 0)
			consult += " and op.id like :op";
		else
			consult += " and op.id not like :op";

		consult += " and op.fecha between :desde and :hasta order by op.fecha desc";

		List<Operaciones> list = em.createQuery(consult).setParameter("client", client).setParameter("user", user)
				.setParameter("op", op).setParameter("desde", desde).setParameter("hasta", hasta).getResultList();
		em.close();

		return list;
	}

	public Date getOperacionesMaxDate() {

		String consult = "select max(fecha) from Operaciones";
		EntityManager em = factory.createEntityManager();
		Date d = (Date) em.createQuery(consult).getSingleResult();
		em.close();

		return d;
	}

	public Date getOperacionesMinDate() {

		String consult = "select min(fecha) from Operaciones";
		EntityManager em = factory.createEntityManager();
		Date d = (Date) em.createQuery(consult).getSingleResult();
		em.close();

		return d;
	}

	public Operaciones getOperacion(Date date) {

		return (Operaciones) get(Operaciones.class, date);
	}

	//OperaionesCompartidas
	
	public OperacionesCompartidas getOperacionCompartida(Date date) {

		return (OperacionesCompartidas) get(OperacionesCompartidas.class, date);
	}
	// Usuarios

	@SuppressWarnings("unchecked")
	public List<Usuario> getUsuariosList() {

		return (List<Usuario>) getList("Usuario");
	}

	// Asistencia

	public Asistencia getAsistencia(Date date) {

		return (Asistencia) get(Asistencia.class, date);
	}

	@SuppressWarnings("unchecked")
	public List<Asistencia> getAsistenciaList() {

		return (List<Asistencia>) getList("Asistencia order by entrada desc");
	}
	
	public Integer getSumAsistenciasAt(int month, int year) {
		
		GregorianCalendar desde = new GregorianCalendar(year, month, 1);
		GregorianCalendar hasta = new GregorianCalendar(year, month + 1, 1);

		String consult = "select sum(a) from (select count(entrada) a from Asistencia\r\n"
				+ " where entrada between :desde and :hasta and estado like 'Activo')";
		EntityManager em = factory.createEntityManager();
		Query query = em.createNativeQuery(consult);

		Integer i = (Integer) query.setParameter("desde", desde.getTime()).setParameter("hasta", hasta.getTime())
				.getSingleResult();
		em.close();
		
		return i == null ? 0 : i;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Asistencia> getAsistenciaListUntil(int month, int year) {
		GregorianCalendar fecha = new GregorianCalendar(year, month, 1);

		String consult = "SELECT a from Asistencia a where a.entrada < :fecha";
		EntityManager em = factory.createEntityManager();
		ArrayList<Asistencia> list = (ArrayList<Asistencia>) em.createQuery(consult).setParameter("fecha", fecha.getTime()).getResultList();
		em.close();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Asistencia> getAsistenciaList(String ci, String servicio, String estado, Date desde, Date hasta) {

		String consult = "select a from Asistencia a";
		EntityManager em = factory.createEntityManager();

		desde = (desde == null ? getAsistenciaMinDate() : desde);
		hasta = (hasta == null ? getAsistenciaMaxDate() : hasta);

		if (ci.length() > 0)
			consult += " where a.ci like :ci";
		else
			consult += " where a.ci not like :ci";
		if (servicio.length() > 0)
			consult += " and a.servicio like :servicio";
		else
			consult += " and a.servicio not like :servicio";
		if (estado.length() > 0)
			consult += " and a.estado like :estado";
		else
			consult += " and a.estado not like :estado";

		consult += " and a.entrada between :desde and :hasta order by a.entrada desc";
		List<Asistencia> list = em.createQuery(consult).setParameter("ci", ci).setParameter("servicio", servicio)
				.setParameter("estado", estado).setParameter("desde", desde).setParameter("hasta", hasta)
				.getResultList();
		em.close();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Asistencia> getEntradasSinCreditoList(String ci, String servicio, Date desde) {
		if (desde == null) {
			desde = getAsistenciaMinDate();
		}
		Date hasta = new Date();
		String consult = "select a from Asistencia a where a.ci like :ci and a.servicio like :servicio and a.estado not like 'Activo' and a.entrada between :desde and :hasta";
		EntityManager em = factory.createEntityManager();

		desde = (desde == null ? getAsistenciaMinDate() : desde);
		hasta = (hasta == null ? getAsistenciaMaxDate() : hasta);

		List<Asistencia> list = em.createQuery(consult).setParameter("ci", ci).setParameter("ci", ci)
				.setParameter("servicio", servicio).setParameter("desde", desde).setParameter("hasta", hasta)
				.getResultList();

		em.close();

		return list;
	}

	public Date getAsistenciaMaxDate() {

		String consult = "select max(entrada) from Asistencia";
		EntityManager em = factory.createEntityManager();
		Date d = (Date) em.createQuery(consult).getSingleResult();
		em.close();

		return d;
	}

	public Date getAsistenciaMinDate() {

		String consult = "select min(entrada) from Asistencia";
		EntityManager em = factory.createEntityManager();
		Date d = (Date) em.createQuery(consult).getSingleResult();
		em.close();

		return d;
	}
	// ClientesEnGym.

	public ClientesEnGym getClientesEnGym(String ci) {

		return (ClientesEnGym) get(ClientesEnGym.class, ci);
	}

	@SuppressWarnings("unchecked")
	public List<ClientesEnGym> getClientesEnGymList() {

		return (List<ClientesEnGym>) getList("ClientesEnGym");
	}

	public List<String> getClientesEnGymNameList() {
		ArrayList<String> clients = new ArrayList<>();
		getClientesEnGymList().forEach(c -> {
			String name = getClient(c.getCi()).getNombre();
			clients.add(name + "-" + c.getCi());
		});

		return clients;
	}

	// ArticulosAlquilados

	@SuppressWarnings("unchecked")
	public List<ArticulosAlquilados> getArticulosAlquiladosList() {

		return (List<ArticulosAlquilados>) getList("ArticulosAlquilados");
	}

	// Articulos

	@SuppressWarnings("unchecked")
	public List<Articulos> getArticulosList() {

		return (List<Articulos>) getList("Articulos");
	}

	@SuppressWarnings("unchecked")
	public List<String> getArticulosNameList() {

		ArrayList<String> articulos = new ArrayList<String>();
		((List<Articulos>) getList("Articulos")).forEach(art -> articulos.add(art.getArticulo()));
		return articulos;
	}

	public Articulos getArticulos(String articulo) {

		return (Articulos) get(Articulos.class, articulo);
	}

	// Notificaciones

	@SuppressWarnings("unchecked")
	public List<Notificaciones> getNotificacionesFromClientesEstado() {

		EntityManager em = factory.createEntityManager();
		String consult = "select c from ClientesEstado c where c.pagoHasta=null or c.pagoHasta between :desde and :hasta";
		Date hasta = Fechas.addTime(new Date(), Calendar.DAY_OF_YEAR, 3);
		Date desde = Fechas.restTime(new Date(), Calendar.MONTH, 1);

		List<ClientesEstado> states = em.createQuery(consult).setParameter("desde", desde).setParameter("hasta", hasta)
				.getResultList();
		ArrayList<Notificaciones> nots = new ArrayList<>();
		states.forEach(state -> {
			Notificaciones not = Notificacion.getNotificacion(state);
			if (not != null && !not.isLeido())
				nots.add(not);
		});
		em.close();
		return nots;
	}

	@SuppressWarnings("unchecked")
	public List<Notificaciones> getNotificacionesList() {

		checkForNotifications();
		return (List<Notificaciones>) this.getList("Notificaciones");
	}

	private void checkForNotifications() {
		List<ClientesInfoPersonal> clients = getClientsList();
		clients.forEach(client -> {
			client.getClientesEstado().forEach(state -> {
				Notificacion.addtNotificacion(state);
				updateEntity(state);
			});
		});
	}

	// caja

	public Caja getCaja() {

		return (Caja) get(Caja.class, 1);
	}

	// Rutas

	public Rutas getRuta(String id) {
		return (Rutas) get(Rutas.class, id);
	}

	// Cuadres

	@SuppressWarnings("unchecked")
	public ArrayList<CuadreMensual> getCuadreMensualList() {

		return (ArrayList<CuadreMensual>) getList("CuadreMensual");
	}

	@SuppressWarnings("unchecked")
	public ArrayList<String> getCuadreMensualNameList() {

		ArrayList<String> strList = new ArrayList<>();
		List<CuadreMensual> list = (ArrayList<CuadreMensual>) getList("CuadreMensual");
		for (int i = 0; i < list.size(); i++) {
			strList.add(list.get(i).getId());
		}
		return strList;
	}

	public ArrayList<CuadreMensual> getCuadresMensualesPendientes() {

		ArrayList<Operaciones> ops = getOperacionesList();
		ArrayList<CuadreMensual> cuadres = new ArrayList<>();
		ArrayList<CuadreMensual> cuadresAnteriores = getCuadreMensualList();
		int currentMonth = 0;
		for (int i = ops.size() - 1; i >= 0; i--) {
			if (Fechas.getMonth(ops.get(i).getFecha()) == Fechas.getMonth(new Date())
					&& Fechas.getYear(ops.get(i).getFecha()) == Fechas.getYear(new Date()))
				break;
			if (currentMonth == 0 && Fechas.getMonth(ops.get(i).getFecha()) == 0 && cuadres.size() == 0) {
				CuadreMensual c = new CuadreMensual();
				c.setId(Fechas.getMonthYearString(ops.get(i).getFecha()));
				if (!cuadresAnteriores.contains(c))
					cuadres.add(c);
			} else if (Fechas.getMonth(ops.get(i).getFecha()) != currentMonth) {
				currentMonth = Fechas.getMonth(ops.get(i).getFecha());
				CuadreMensual c = new CuadreMensual();
				c.setId(Fechas.getMonthYearString(ops.get(i).getFecha()));
				if (!cuadresAnteriores.contains(c))
					cuadres.add(c);
			}
		}
		return cuadres;
	}

	public CuadreMensual getCuadreMensual(String id) {

		return (CuadreMensual) get(CuadreMensual.class, id);
	}

	public Double getIngresosAt(int month, int year) {

		double d1 = getIngresosAlqAt(month, year);
		double d2 = getIngresosCMembAt(month, year);

		return d1 + d2;
	}

	public Double getIngresosCMembAt(int month, int year) {

		GregorianCalendar desde = new GregorianCalendar(year, month, 1);
		GregorianCalendar hasta = new GregorianCalendar(year, month + 1, 1);

		String consult = "SELECT sum(valor) from Operaciones where id like 'Cobro de Membrecia'"
				+ " and cancelada like 0 and fecha between :desde and :hasta";
		EntityManager em = factory.createEntityManager();
		
		Double d = (Double) em.createQuery(consult).setParameter("desde", desde.getTime())
				.setParameter("hasta", hasta.getTime()).getSingleResult();
		em.close();
		return d == null ? 0 : d;
	}

	public Double getIngresosAlqAt(int month, int year) {

		GregorianCalendar desde = new GregorianCalendar(year, month, 1);
		GregorianCalendar hasta = new GregorianCalendar(year, month + 1, 1);

		String consult = "SELECT sum(valor) from Operaciones where id like 'Alquiler de Articulo' and cancelada like 0 and fecha between :desde and :hasta";
		EntityManager em = factory.createEntityManager();
		Double d = (Double) em.createQuery(consult).setParameter("desde", desde.getTime())
				.setParameter("hasta", hasta.getTime()).getSingleResult();
		return d == null ? 0 : d;
	}

	public Double getGastosAt(int month, int year) {

		GregorianCalendar desde = new GregorianCalendar(year, month, 1);
		GregorianCalendar hasta = new GregorianCalendar(year, month + 1, 1);
		String consult = "SELECT sum(valor) from Operaciones where id like 'Gasto' and cancelada like 0 and fecha between :desde and :hasta";
		EntityManager em = factory.createEntityManager();
		Double d = (Double) em.createQuery(consult).setParameter("desde", desde.getTime())
				.setParameter("hasta", hasta.getTime()).getSingleResult();
		em.close();
		return d == null ? 0 : d;
	}
	
	public Double getValorCompOPCAt(int month, int year) {

		GregorianCalendar desde = new GregorianCalendar(year, month, 1);
		GregorianCalendar hasta = new GregorianCalendar(year, month + 1, 1);
		String consult = "SELECT sum(valorCompartido) from OperacionesCompartidas where cancelada like 0 and fecha between :desde and :hasta";
		EntityManager em = factory.createEntityManager();
		Double d = (Double) em.createQuery(consult).setParameter("desde", desde.getTime())
				.setParameter("hasta", hasta.getTime()).getSingleResult();
		em.close();
		return d == null ? 0 : d;
	}
	
	public Double getValorTotalOPCAt(int month, int year) {

		GregorianCalendar desde = new GregorianCalendar(year, month, 1);
		GregorianCalendar hasta = new GregorianCalendar(year, month + 1, 1);
		String consult = "SELECT sum(valorTotal) from OperacionesCompartidas where cancelada like 0 and fecha between :desde and :hasta";
		EntityManager em = factory.createEntityManager();
		Double d = (Double) em.createQuery(consult).setParameter("desde", desde.getTime())
				.setParameter("hasta", hasta.getTime()).getSingleResult();
		em.close();
		return d == null ? 0 : d;
	}

	@SuppressWarnings("unchecked")
	public List<Asistencia> getAsistenciaAt(int month, int year) {

		GregorianCalendar desde = new GregorianCalendar(year, month, 1);
		GregorianCalendar hasta = new GregorianCalendar(year, month + 1, 1);
		String consult = "select a from Asistencia a where a.entrada between :desde and :hasta";

		EntityManager em = factory.createEntityManager();
		List<Asistencia> asists = em.createQuery(consult).setParameter("desde", desde.getTime())
				.setParameter("hasta", hasta.getTime()).getResultList();
		em.close();
		return asists;
	}

	@SuppressWarnings("unchecked")
	public ActividadMensual getActividadMensualAt(int month, int year) {

		ActividadMensual actividad = new ActividadMensual();
		GregorianCalendar desde = new GregorianCalendar(year, month, 1);
		GregorianCalendar hasta = new GregorianCalendar(year, month + 1, 1);

		String consult = "SELECT  count(entrada) count, (strftime('%H', entrada / 1000, 'unixepoch', 'localtime')) hour "
				+ "from Asistencia a where a.entrada between :desde and :hasta group by hour order by count desc limit 3";

		EntityManager em = factory.createEntityManager();
		Query query = em.createNativeQuery(consult);

		try {
			List<Object[]> list = query.setParameter("desde", desde.getTime()).setParameter("hasta", hasta.getTime())
					.getResultList();
			actividad.setClientesHorario1((int) list.get(0)[0]);
			actividad.setClientesHorario2((int) list.get(1)[0]);
			actividad.setClientesHorario3((int) list.get(2)[0]);
			actividad.setHorarioConcurrido1((String) list.get(0)[1] + ":00");
			actividad.setHorarioConcurrido2((String) list.get(1)[1] + ":00");
			actividad.setHorarioConcurrido3((String) list.get(2)[1] + ":00");
		} catch (Exception e) {
		}
		em.close();
		return actividad;
	}

	@SuppressWarnings("unchecked")
	public int getDiasHabilesAt(int month, int year) {

		GregorianCalendar desde = new GregorianCalendar(year, month, 1);
		GregorianCalendar hasta = new GregorianCalendar(year, month + 1, 1);

		String consult = "select count(*) from Asistencia where entrada between :desde and :hasta group by strftime('%d', entrada / 1000, 'unixepoch', 'localtime')";

		EntityManager em = factory.createEntityManager();
		Query query = em.createNativeQuery(consult);

		List<Object[]> list = query.setParameter("desde", desde.getTime()).setParameter("hasta", hasta.getTime())
				.getResultList();
		em.close();
		return list.size();
	}

	public void clearDB(int month, int year) {

		GregorianCalendar gc = new GregorianCalendar(year, month, 1);
		EntityManager em = factory.createEntityManager();
		
		Session session = em.unwrap(Session.class);
		Transaction t = session.beginTransaction();
		Query q = session.createQuery("delete from Operaciones where fecha < :fecha");
		Query q1 = session.createQuery("delete from Asistencia where entrada < :fecha");
		Query q2 = session.createQuery("delete from OperacionesCompartidas where fecha < :fecha");
		q.setParameter("fecha", gc.getTime());
        q.executeUpdate();
        q1.setParameter("fecha", gc.getTime());
        q1.executeUpdate();
        q2.setParameter("fecha", gc.getTime());
        q2.executeUpdate();
        t.commit();
        session.close();
        em.close();
	}
}