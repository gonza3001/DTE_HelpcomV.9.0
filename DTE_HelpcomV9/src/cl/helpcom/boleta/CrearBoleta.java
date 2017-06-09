package cl.helpcom.boleta;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import cl.sii.siiDte.DTEDocument;
import cl.sii.siiDte.FechaType;
import cl.sii.siiDte.boletas.EnvioBOLETADocument;
import cl.sii.siiDte.boletas.EnvioBOLETADocument.EnvioBOLETA;

public class CrearBoleta {

	public static void main(String[] args) {

		EnvioBOLETADocument boleta;		
		boleta= EnvioBOLETADocument.Factory.newInstance();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		boleta.addNewEnvioBOLETA();
		boleta.getEnvioBOLETA().setVersion(new BigDecimal("1.0"));
		boleta.getEnvioBOLETA().addNewSetDTE();
		
		boleta.getEnvioBOLETA().getSetDTE().addNewCaratula();
		boleta.getEnvioBOLETA().getSetDTE().getCaratula().setRutEmisor("");
		boleta.getEnvioBOLETA().getSetDTE().getCaratula().setRutEnvia("");
		boleta.getEnvioBOLETA().getSetDTE().getCaratula().setRutReceptor("");
//		boleta.getEnvioBOLETA().getSetDTE().getCaratula().setFchResol(FechaType.Factory.newValue(sdf.format(sdf.parse(""))));
		boleta.getEnvioBOLETA().getSetDTE().getCaratula().setNroResol(1);
		
		boleta.getEnvioBOLETA().getSetDTE().getCaratula().addNewSubTotDTE();
		boleta.getEnvioBOLETA().getSetDTE().getCaratula().getSubTotDTEArray();
		
		//Agregar subtotales con
		
		
		

	}

}
