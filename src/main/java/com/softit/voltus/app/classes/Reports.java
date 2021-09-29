package com.softit.voltus.app.classes;

import java.sql.Connection;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class Reports {

	private JasperReport report;
	private JasperPrint print;
	private JasperViewer viewer;

	public Reports() {

	}

	public void createReport(Connection conn, String path) throws JRException {

		report = (JasperReport) JRLoader.loadObjectFromFile(path);
		print = JasperFillManager.fillReport(report, null, conn);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void createReport(String path, Map parameters, Connection conn) throws JRException {

		report = (JasperReport) JRLoader.loadObjectFromFile(path);
		print = JasperFillManager.fillReport(report, parameters, conn);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void createReport(String path, Map parameters, JRBeanCollectionDataSource ds) throws JRException {

		report = (JasperReport) JRLoader.loadObjectFromFile(path);
		print = JasperFillManager.fillReport(report, parameters, ds);
	}

	public void viewReport() {
		viewer = new JasperViewer(print, false);
		viewer.setVisible(true);
	}
	
	public void viewReport(String sourceFile) throws JRException {
		viewer = new JasperViewer(sourceFile, false);
		viewer.setVisible(true);
	}

	public void exportReport(String path) throws JRException {
		JasperExportManager.exportReportToPdfFile(print, path);
	}
}
