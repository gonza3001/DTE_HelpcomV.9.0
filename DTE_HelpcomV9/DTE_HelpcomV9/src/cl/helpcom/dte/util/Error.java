package cl.helpcom.dte.util;

import java.util.Calendar;

public class Error {



	public void llenarError(){

		String out="";
		Calendar fechaActual = Calendar.getInstance();

		out+=fechaActual.get(Calendar.MINUTE)+"/";
		out+=fechaActual.get(Calendar.HOUR_OF_DAY)+"/";
		out+=fechaActual.get(Calendar.MONTH+1)+"/";
		out+=fechaActual.get(Calendar.YEAR);

	}
}
