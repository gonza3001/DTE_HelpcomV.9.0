package cl.helpcom.dte.util;

import java.util.ArrayList;
import java.util.Iterator;

import com.Ostermiller.util.StringTokenizer;
import com.itextpdf.text.log.SysoLogger;

import cl.sii.siiDte.ImpAdicDTEType.Enum;

public class ImpAdicional {


	public int getPosImp(String inImpuesto){
		ArrayList<String> tablaImpuestos = new ArrayList<>();

		tablaImpuestos.add("14");
		tablaImpuestos.add("15");
		tablaImpuestos.add("16");
		tablaImpuestos.add("17");
		tablaImpuestos.add("18");
		tablaImpuestos.add("19");
		tablaImpuestos.add("23");
		tablaImpuestos.add("24");
		tablaImpuestos.add("25");
		tablaImpuestos.add("26");
		tablaImpuestos.add("27");
		tablaImpuestos.add("28");
		tablaImpuestos.add("30");
		tablaImpuestos.add("31");
		tablaImpuestos.add("32");
		tablaImpuestos.add("33");
		tablaImpuestos.add("34");
		tablaImpuestos.add("35");
		tablaImpuestos.add("36");
		tablaImpuestos.add("37");
		tablaImpuestos.add("38");
		tablaImpuestos.add("39");
		tablaImpuestos.add("40");
		tablaImpuestos.add("41");
		tablaImpuestos.add("44");
		tablaImpuestos.add("45");
		tablaImpuestos.add("46");
		tablaImpuestos.add("47");
		tablaImpuestos.add("48");
		tablaImpuestos.add("49");
		tablaImpuestos.add("50");
		tablaImpuestos.add("51");
		tablaImpuestos.add("52");
		tablaImpuestos.add("53");
		tablaImpuestos.add("271");
		tablaImpuestos.add("301");
		tablaImpuestos.add("321");
		tablaImpuestos.add("331");
		tablaImpuestos.add("341");
		tablaImpuestos.add("361");
		tablaImpuestos.add("371");
		tablaImpuestos.add("481");

		Iterator<String> it = tablaImpuestos.iterator();
		int c=0;
    	while (it.hasNext()) {
    		if(inImpuesto.equals(tablaImpuestos.get(c))){
    			return c+1;
    		}
    		it.next();
    		c++;
    	}
    	System.out.println("No se encuentra IMPUESTO en la base de datos. Por favor comunicarse con su proveedor de servicio");
    	return 0;
	}
	



}
