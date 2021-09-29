package com.softit.voltus.app.classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DataSaver {

	public DataSaver() {

	}

	public void saveDB() {

		Date toDay = new Date();
		DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);

		File from = new File("voltusDB.db");
		File to = new File("saves\\" + df.format(toDay) + ".db");
		try {
			copyFile(from, to);
		} catch (IOException e) {
		}
	}

	public void copyFile(File from, File to) throws IOException {

		FileInputStream inputStream = new FileInputStream(from);
		FileOutputStream outputStream = new FileOutputStream(to);

		byte[] buffer = new byte[1024];

		int length;
		while ((length = inputStream.read(buffer)) > 0) {
			outputStream.write(buffer, 0, length);
		}

		inputStream.close();
		outputStream.close();
	}

	public void deleteOldSaves() {

		File[] files = new File("saves").listFiles();
		ArrayList<File> filesToKeep = new ArrayList<>();

		DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
		for (int i = 0; i < 7; i++) {
			GregorianCalendar gc = new GregorianCalendar();
			gc.set(Calendar.DAY_OF_YEAR, gc.get(Calendar.DAY_OF_YEAR) - i);
			filesToKeep.add(new File("saves\\" + df.format(gc.getTime()) + ".db"));
		}
		try {
			for (int i = 0; i < files.length; i++) {
				if (!filesToKeep.contains(files[i]))
					files[i].delete();
			}
		} catch (Exception e) {
		}

	}
}
