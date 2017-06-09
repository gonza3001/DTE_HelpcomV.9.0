package cl.nic.dte.util;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Pinging {

	  public String getConnectionStatus () {
	        String conStatus = null;
	        try {
	            URL u = new URL("https://www.google.es/");
	            HttpsURLConnection huc = (HttpsURLConnection) u.openConnection();
	            huc.connect();
	            conStatus = "Online";
	        } catch (Exception e) {
	            conStatus = "Offline";
	        }
	        return conStatus;
	    }
}
