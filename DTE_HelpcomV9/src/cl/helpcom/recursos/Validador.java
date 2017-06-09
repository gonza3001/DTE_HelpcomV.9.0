package cl.helpcom.recursos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.sun.org.apache.bcel.internal.generic.RETURN;

import cl.nic.dte.examples.array;
import cl.nic.dte.util.Utilities;
import cl.sii.siiDte.FechaType;

/**
 * @author mau
 *
 */
public class Validador {

	public ComunicadorAppClienteTXT comTxt = new ComunicadorAppClienteTXT();

	/**
	 * @param arrayLineasCaratula Arreglo a validar los valores de La Caratula
	 */
	public void validaEntradaLCVCaratula(ArrayList<ArrayList<String>> arrayLineasCaratulas) {

		//Valida Vacio
		for (int c = 0; c < 2; c++) {
			//System.out.println("Validador-- "+arrayLineasCaratulas.get(0).get(c));
			if ((arrayLineasCaratulas.get(0).get(c)).trim().length() == 0
					|| arrayLineasCaratulas.get(0).get(c) == null) {

				System.out.println("Variable " + c
						+ " de la CARATULA se encuentra sin información");
			}
		}

		if (arrayLineasCaratulas.get(0).get(0).length() < 3
				|| arrayLineasCaratulas.get(0).get(10).length() > 10) {

			System.err
					.println("Rut Emisor Libro no cumple con tamaños Min 3, Max 10");

		}
		if (arrayLineasCaratulas.get(0).get(1).length() < 3
				|| arrayLineasCaratulas.get(0).get(1).length() > 10) {

			System.err.println("Rut Envia no cumple con tamaños Min 3, Max 10");

		}
		if (arrayLineasCaratulas.get(0).get(2).length() != 7) {
			System.err.println("Tamaño fecha Pediodo Tributario incorrecto");
		}

		this.validarFechaAAAAMM(arrayLineasCaratulas.get(0).get(2));

		if (arrayLineasCaratulas.get(0).get(3).length() != 10) {
			System.err.println("Tamaño fecha Resolución incorrecto");
		}

		this.validarFechaAAAAMMDD(arrayLineasCaratulas.get(0).get(3));

		/*if (arrayLineasCaratula[0][5] != "VENTA"|| arrayLineasCaratula[0][5] != "COMPRA"){
			System.err.println("Tipo Operacion	 no corresponde");
		}else if (arrayLineasCaratula[0][6] != "MENSUAL"|| arrayLineasCaratula[0][6] != "ESPECIAL"||arrayLineasCaratula[0][6] != "RECTIFICA"){
			System.err.println("Tipo Libro	 no corresponde");
		}
		else if (arrayLineasCaratula[0][7] != "PARCIAL"|| arrayLineasCaratula[0][7] != "FINAL"||arrayLineasCaratula[0][7] != "TOTAL"||arrayLineasCaratula[0][7] != "AJUSTE"){
			System.err.println("Tipo Envio	 no corresponde");
		}*/
		}
	
	
	
	public String formatofechaYYMMDDNuevo(String date){
		
		date= date.replaceAll("/", "-");
		String[] dateGuion = date.split("-");
		String newDate="";
		
		try {
			if (dateGuion[2].length()==4 & dateGuion.length==3){
				newDate=dateGuion[2]+"-"+dateGuion[1]+"-"+dateGuion[0];
				return newDate;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return date;
	}
	/**
	 * @param arrayLineasTotales Arreglo a validar los valores de los Totales
	 * @param cantTotales cantidad de Totales
	 */
	public void validaEntradaLCVTotales(ArrayList<ArrayList<String>> arrayLineasTotales,int cantTotales) {

		for(int x=0; x<cantTotales;x++){

		if(arrayLineasTotales.get(x).get(0).trim().length() == 0 || arrayLineasTotales.get(x).get(0) == null){
			System.err.println("Total Tipo Documento fila ("+x+",0) está vacío o nulo");
		}
		if(arrayLineasTotales.get(x).get(2).trim().length() == 0 || arrayLineasTotales.get(x).get(2) == null){
			System.err.println("Total Total Documentos fila ("+x+",2) está vacío o nulo");
		}
		if(arrayLineasTotales.get(x).get(5).trim().length() == 0 || arrayLineasTotales.get(x).get(5) == null){
			System.err.println("Total Monto Exento fila ("+x+",5) está vacío o nulo");
		}
		if(arrayLineasTotales.get(x).get(6).trim().length() == 0 || arrayLineasTotales.get(x).get(6) == null){
			System.err.println("Total Monto Neto fila ("+x+",6) está vacío o nulo");
		}
		if(arrayLineasTotales.get(x).get(8).trim().length() == 0 || arrayLineasTotales.get(x).get(8) == null){
			System.err.println("Total Monto IVA fila ("+x+",8) Monto IVA está vacío o nulo");
		}
		if(arrayLineasTotales.get(x).get(40).trim().length() == 0 || arrayLineasTotales.get(x).get(40) == null){
			System.err.println("Total Monto Total fila ("+x+",41) está vacío o nulo");
		}
		}
}

	/**
	 * @param arrayLineasTotales Arreglo a validar  los valores de los Detalles
	 * @param cantidadDetalles Cantidad de detalles del Libro
	 */
	public void validaEntradaLCVDetalles(ArrayList<ArrayList<String>> arrayLineasDetalles,int cantidadDetalles) {

		for(int x=0; x<cantidadDetalles;x++){
		if(arrayLineasDetalles.get(x).get(0).trim().length() == 0 || arrayLineasDetalles.get(x).get(0) == null){
			System.err.println("Detalles Tipo Doc file ("+x+",0) está vacío o nulo");
		}
		if(arrayLineasDetalles.get(x).get(3).trim().length() == 0 || arrayLineasDetalles.get(x).get(3) == null){
			System.err.println("Detalle Numero Documento fila ("+x+",3) está vacío o nulo");
		}}
	}
	/**
	 * @param fecha Valida formato de Fecha AÑO-MES
	 */
	public void validarFechaAAAAMM(String fecha) {

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			FechaType.Factory.newValue(Utilities.fechaFormat.format(sdf
					.parse(fecha)));

		} catch (Exception e) {

			System.err.println("Formato de fech incorrecto");
		}
	}

	/**
	 * @param fecha Valida formato de Fecha AÑO-MES-DIA
	 */
	public void validarFechaAAAAMMDD(String fecha) {

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			FechaType.Factory.newValue(Utilities.fechaFormat.format(sdf
					.parse(fecha)));

		} catch (Exception e) {

			System.err.println("Formato de fech incorrecto");
		}
	}

    public String[]  getFecha (String fechaS) {
    	String[] datosFecha = fechaS.split("-");
    	try {
	        
	        for (int i = 0; i < datosFecha.length; i++) {
	          //  System.out.println(datosFecha[i]);
	        }
	        Integer mes=  Integer.valueOf(datosFecha[1])-1;
	        datosFecha[1] = mes.toString();
	        
	        return datosFecha;
    	}
        catch (Exception e) {
			// TODO: handle exception
		}
        
        return datosFecha;
    }

    public String YYMMDDToDDMMYY (String fechaS) {
        
    	fechaS= fechaS.replaceAll("/","-");
    	String[] datosFecha = fechaS.split("-");
        String out="";
        
        try {
        	if (datosFecha[0].length()==4){
        		out=datosFecha[2]+"-"+datosFecha[1]+"-"+datosFecha[0];
            return out;
            }
		} catch (Exception e) {
			// TODO: handle exception
		}
        
        return fechaS;
    }


    public void validaEntradaEncabezado(ArrayList<String> arrayLineasEncabezado){

    		if(arrayLineasEncabezado.get(0).trim().length() == 0 || arrayLineasEncabezado.get(0)== null){
    			System.err.println("Encabezado falta TipoDTE");
    		}
    		if(arrayLineasEncabezado.get(1).trim().length() == 0 || arrayLineasEncabezado.get(1)== null){
    			System.err.println("Encabezado falta Folio");
    		}
    		if(arrayLineasEncabezado.get(2).trim().length() == 0 || arrayLineasEncabezado.get(2)== null){
    			System.err.println("Encabezado falta FechaEmis");
    		}
    		if(arrayLineasEncabezado.get(11).trim().length() == 0 || arrayLineasEncabezado.get(11)== null){
    			System.err.println("Encabezado falta RUTEmisor");
    		}
    		if(arrayLineasEncabezado.get(12).trim().length() == 0 || arrayLineasEncabezado.get(12)== null){
    			System.err.println("Encabezado falta RznSoc");
    		}
    		if(arrayLineasEncabezado.get(13).trim().length() == 0 || arrayLineasEncabezado.get(13)== null){
    			System.err.println("Encabezado falta GiroEmis");
    		}
    		if(arrayLineasEncabezado.get(16).trim().length() == 0 || arrayLineasEncabezado.get(16)== null){
    			System.err.println("Encabezado falta Acteco");
    		}
    		if(arrayLineasEncabezado.get(21).trim().length() == 0 || arrayLineasEncabezado.get(21)== null){
    			System.err.println("Encabezado falta RUTRecep");
    		}
    		if(arrayLineasEncabezado.get(22).trim().length() == 0 || arrayLineasEncabezado.get(22)== null){
    			System.err.println("Encabezado falta RznSocRecep");
    		}
    		if(arrayLineasEncabezado.get(46).trim().length() == 0 || arrayLineasEncabezado.get(46)== null){
    			System.err.println("Encabezado falta MntTotal");
    		}
    }
    public void validaEntradaDetalles(ArrayList<ArrayList<String>> arrayLineasDetalle,int cantidadDetalles){

    	for(int x=0; x<cantidadDetalles;x++){

			if(arrayLineasDetalle.get(x).get(0).trim().length() == 0 || arrayLineasDetalle.get(x).get(0) == null){
				System.err.println("Detalle NroLinDet ("+x+",0) está vacío o nulo");
			}
			if(arrayLineasDetalle.get(x).get(3).trim().length() == 0 || arrayLineasDetalle.get(x).get(3) == null){
				System.err.println("Detalle NmbItem ("+x+",3) está vacío o nulo");
			}
			if(arrayLineasDetalle.get(x).get(12).trim().length() == 0 || arrayLineasDetalle.get(x).get(12) == null){
				System.err.println("Detalle MontoItem ("+x+",12) está vacío o nulo");
			}
    		}
    	}
    public void validaEntradaReferencia(ArrayList<ArrayList<String>> arrayLineasReferencia,int cantidadReferencias){

    	for(int x=0; x<cantidadReferencias;x++){

			if(arrayLineasReferencia.get(x).get(0).trim().length() == 0 || arrayLineasReferencia.get(x).get(0) == null){
				System.err.println("Referencia NroLinRef ("+x+",0) está vacío o nulo");
			}
			if(arrayLineasReferencia.get(x).get(1).trim().length() == 0 || arrayLineasReferencia.get(x).get(1) == null){
				System.err.println("Referencia TpoDocRef ("+x+",1) está vacío o nulo");
			}
			if(arrayLineasReferencia.get(x).get(3).trim().length() == 0 || arrayLineasReferencia.get(x).get(3) == null){
				System.err.println("Referencia FolioRef ("+x+",3) está vacío o nulo");
			}
			if(arrayLineasReferencia.get(x).get(5).trim().length() == 0 || arrayLineasReferencia.get(x).get(5) == null){
				System.err.println("Referencia FchRef ("+x+",5) está vacío o nulo");
			}
    		}
    	}
    public void validaEntradaDscRcgGlobal(String[][] arrayLineasDscRGlobal,int cantidadDscRGlobal){

    	for(int x=0; x<cantidadDscRGlobal;x++){

			if(arrayLineasDscRGlobal[x][0].trim().length() == 0 || arrayLineasDscRGlobal[x][0] == null){
				System.err.println("Descuento Recarga NroLinDR("+x+",0) está vacío o nulo");
			}
			if(arrayLineasDscRGlobal[x][1].trim().length() == 0 || arrayLineasDscRGlobal[x][1] == null){
				System.err.println("Descuento Recarga TpoMov ("+x+",1) está vacío o nulo");
			}
			if(arrayLineasDscRGlobal[x][3].trim().length() == 0 || arrayLineasDscRGlobal[x][3] == null){
				System.err.println("Descuento Recarga TpoValor ("+x+",3) está vacío o nulo");
			}
			if(arrayLineasDscRGlobal[x][4].trim().length() == 0 || arrayLineasDscRGlobal[x][4] == null){
				System.err.println("Descuento Recarga ValorDR ("+x+",4) está vacío o nulo");
			}
    		}
    	}

}

