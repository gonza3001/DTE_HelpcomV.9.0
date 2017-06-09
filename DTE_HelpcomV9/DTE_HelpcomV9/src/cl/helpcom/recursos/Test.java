package cl.helpcom.recursos;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.sql.SQLException;
import java.util.ArrayList;

import cl.helpcom.dao.ConexionSqlFirma;
import cl.helpcom.itext.LeerXML;
import cl.nic.dte.examples.array;

public class Test {

	/**
	 * @param args
	 * @throws IOException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */ 
	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {

		String textXML=" <siid:TIMESTAMP>2017-05-11 15:00:55</siid:TIMESTAMP><siid:STATUS>0</siid:STATUS><siid:TRACKID>2153363292</siid:TRACKID></siid:RECEPCIONDTE>";
		int inicio = textXML.indexOf("<siid:TRACKID>");
		textXML= textXML.substring(inicio + 1);
		textXML = textXML.replaceAll("siid:TRACKID>", "");
		textXML = textXML.replaceAll("</</siid:RECEPCIONDTE>", "");
		System.out.println(textXML);
		
		
	}
	}

