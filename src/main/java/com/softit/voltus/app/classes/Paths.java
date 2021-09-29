package com.softit.voltus.app.classes;

import java.io.File;
import java.net.MalformedURLException;

public class Paths {

	public static String VIEWS_PATH = "/com/softit/voltus/app/view/";

	public static String getDBURL() {
		String url = "";

		File f = new File("voltusDB.db");
		if (f.exists()) {
			try {
				url = f.toURI().toURL().toExternalForm();
				url = url.replace("file:/", "");
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return url;
	}
}
