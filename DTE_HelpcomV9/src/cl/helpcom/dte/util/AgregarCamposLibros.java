package cl.helpcom.dte.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import cl.helpcom.recursos.ComunicadorAppClienteTXT;
import cl.helpcom.recursos.Validador;
import cl.sii.siiDte.libroCV.LibroCompraVentaDocument;
import cl.sii.siiDte.libroCV.LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro;
import cl.sii.siiDte.libroCV.LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.Caratula;
import cl.sii.siiDte.libroCV.LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.Detalle;
import cl.sii.siiDte.libroCV.LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.Detalle.IVANoRec;
import cl.sii.siiDte.libroCV.LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.Detalle.OtrosImp;
import cl.sii.siiDte.libroCV.LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.ResumenPeriodo;
import cl.sii.siiDte.libroCV.LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.ResumenPeriodo.TotalesPeriodo.TotIVANoRec;
import cl.sii.siiDte.libroCV.LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.ResumenPeriodo.TotalesPeriodo.TotOtrosImp;
import cl.sii.siiDte.libroCV.LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.ResumenSegmento;
import cl.sii.siiDte.libroboletas.LibroBoletaDocument.LibroBoleta.EnvioLibro.ResumenSegmento.TotalesSegmento;

/**
 * @author Mauricio Rodriguez
 *
 */
public class AgregarCamposLibros {

	private ComunicadorAppClienteTXT txt = new ComunicadorAppClienteTXT();


	/**
	 * @param envioLibro
	 * @param arrayLineasCaratula
	 */
	public void addDatosCartula(EnvioLibro envioLibro,ArrayList<ArrayList<String>> arrayLineasCaratula){

		Calendar cal = Calendar.getInstance();
		String[] date;
		Validador val = new Validador();

		Caratula caratula = envioLibro.addNewCaratula();
		try{
			caratula.setRutEmisorLibro(arrayLineasCaratula.get(0).get(0));//Requerido
		} catch (Exception e) {
			System.out.println("2@helpcom@EL LIBRO NO SE GENERÓ CORRECTAMENTE ERROR: Al leer DATO CARATULA [RUT EMISOR] , CAUSA: "+e.getCause()+"@helpcom@<br>SIN RUTA");
			System.exit(1);
		}
		try{
			caratula.setRutEnvia(arrayLineasCaratula.get(0).get(1));//Requerido
		} catch (Exception e) {
			System.out.println("2@helpcom@EL LIBRO NO SE GENERÓ CORRECTAMENTE ERROR: Al leer DATO CARATULA [RUT ENVIA] , CAUSA: "+e.getCause()+"@helpcom@<br>SIN RUTA");
			System.exit(1);
		}
		date=val.getFecha(arrayLineasCaratula.get(0).get(2));

		cal.clear();
		
		try{	
			cal.set(Calendar.YEAR, Integer.valueOf(date[0]));
			cal.set(Calendar.MONTH, Integer.valueOf(date[1]));
			caratula.setPeriodoTributario(cal);//Requerido
		} catch (Exception e) {
			System.out.println("2@helpcom@EL LIBRO NO SE GENERÓ CORRECTAMENTE ERROR: Al leer DATO CARATULA [PERIODO TRIBUTARIO] , CAUSA: "+e.getCause()+"@helpcom@<br>SIN RUTA");
			System.exit(1);
		}
		date=val.getFecha(arrayLineasCaratula.get(0).get(3));//Settear el campo date
		cal.clear();
		try{
			cal.set(Integer.valueOf(date[0]), Integer.valueOf(date[1]), Integer.valueOf(date[2]));
			caratula.setFchResol(cal);//Requerido
		} catch (Exception e) {
			System.out.println("2@helpcom@EL LIBRO NO SE GENERÓ CORRECTAMENTE ERROR: Al leer DATO CARATULA [FECHA RESOLUCION] , CAUSA: "+e.getCause()+"@helpcom@<br>SIN RUTA");
			System.exit(1);
		}
		try{
			caratula.setNroResol(Integer.valueOf(arrayLineasCaratula.get(0).get(4)));//Requerido
		} catch (Exception e) {
			System.out.println("2@helpcom@EL LIBRO NO SE GENERÓ CORRECTAMENTE ERROR: Al leer DATO CARATULA [NUMERO RESOLUCION] , CAUSA: "+e.getCause()+"@helpcom@<br>SIN RUTA");
			System.exit(1);
		}
		try{
			caratula.setTipoOperacion(caratula.getTipoOperacion().forString(arrayLineasCaratula.get(0).get(5)));//Requerido Enum[COMPRA-VENTA]
		} catch (Exception e) {
			System.out.println("2@helpcom@EL LIBRO NO SE GENERÓ CORRECTAMENTE ERROR: Al leer DATO CARATULA [TIPO OPERACION] , CAUSA: "+e.getCause()+"@helpcom@<br>SIN RUTA");
			System.exit(1);
		}
		try{
			caratula.setTipoLibro(caratula.getTipoLibro().forString(arrayLineasCaratula.get(0).get(6)));//Requerido Enum[MENSUAL-ESPECIAL-RECTIFICA]
		} catch (Exception e) {
			System.out.println("2@helpcom@EL LIBRO NO SE GENERÓ CORRECTAMENTE ERROR: Al leer DATO CARATULA [TIPO LIBRO] , CAUSA: "+e.getCause()+"@helpcom@<br>SIN RUTA");
			System.exit(1);
		}
		try{
			caratula.setTipoEnvio(caratula.getTipoEnvio().forString(arrayLineasCaratula.get(0).get(7)));//Requerido Enum[PARCIAL-FINAL-TOTAL-AJUSTE]
		} catch (Exception e) {
			System.out.println("2@helpcom@EL LIBRO NO SE GENERÓ CORRECTAMENTE ERROR: Al leer DATO CARATULA [TIPO ENVIO] , CAUSA: "+e.getCause()+"@helpcom@<br>SIN RUTA");
			System.exit(1);
		}
		if (arrayLineasCaratula.get(0).get(8).trim().length()>0){
			caratula.setNroSegmento(Integer.valueOf(arrayLineasCaratula.get(0).get(8)));
		}
		if (arrayLineasCaratula.get(0).get(9).trim().length()>0){
			caratula.setFolioNotificacion((Integer.valueOf(arrayLineasCaratula.get(0).get(9))));
		}
		if (arrayLineasCaratula.get(0).get(10).trim().length()>0){
			caratula.setCodAutRec(arrayLineasCaratula.get(0).get(10));
		}
	}

	/**
	 * @param resumenPeriodo
	 * @param arrayLineasTotales
	 * @param cantTotales
	 */
	public void addDatosTotales(ResumenPeriodo resumenPeriodo,ArrayList<ArrayList<String>>arrayLineasTotales,int cantTotales){

				for(int x=0;x<cantTotales;x++){
				cl.sii.siiDte.libroCV.LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.ResumenPeriodo.TotalesPeriodo totalesPeriodo = resumenPeriodo.addNewTotalesPeriodo();

				try {
					totalesPeriodo.setTpoDoc(totalesPeriodo.getTpoDoc().valueOf(Integer.valueOf(arrayLineasTotales.get(x).get(0))));//Requerido
				} catch (Exception e) {
					System.out.println("2@helpcom@EL LIBRO NO SE GENERÓ CORRECTAMENTE ERROR: Al leer DATO TOTALES [NRO:"+x+ ", TIPO DOCUMENTO] , CAUSA: "+e.getCause()+"@helpcom@<br>SIN RUTA");
					System.exit(1);
				}
				
				if (arrayLineasTotales.get(x).get(1).trim().length()>0){
					totalesPeriodo.setTpoImp(Integer.valueOf(arrayLineasTotales.get(x).get(1)));
				}
				try{
					if (arrayLineasTotales.get(x).get(2).trim().length()>0){
						totalesPeriodo.setTotDoc(Integer.valueOf(arrayLineasTotales.get(x).get(2)));//Requerido [29,30,32,33,34,35,38,39,40...
					}else{
						System.out.println("2@helpcom@EL LIBRO NO SE GENERÓ CORRECTAMENTE ERROR: Al leer DATO TOTALES [NRO:"+x+ ", TOTAL DOCUMENTOS] , CAUSA: Debe ser > 0 @helpcom@<br>SIN RUTA");
						System.exit(1);
					}
				} catch (Exception e) {
					System.out.println("2@helpcom@EL LIBRO NO SE GENERÓ CORRECTAMENTE ERROR: Al leer DATO TOTALES [NRO:"+x+ ", TOTAL DOCUMENTOS] , CAUSA: "+e.getCause()+"@helpcom@<br>SIN RUTA");
					System.exit(1);
				}
				
				if (arrayLineasTotales.get(x).get(3).trim().length()>0){
					totalesPeriodo.setTotAnulado(Long.valueOf(arrayLineasTotales.get(x).get(3)));
				}
				if (arrayLineasTotales.get(x).get(4).trim().length()>0){
					totalesPeriodo.setTotOpExe(Long.valueOf(arrayLineasTotales.get(x).get(4)));
				}
				try{
					totalesPeriodo.setTotMntExe(Integer.valueOf(arrayLineasTotales.get(x).get(5)));//Requerido
				} catch (Exception e) {
					System.out.println("2@helpcom@EL LIBRO NO SE GENERÓ CORRECTAMENTE ERROR: Al leer DATO TOTALES [NRO:"+x+ ", MONTO EXENTO] , CAUSA: "+e.getCause()+"@helpcom@<br>SIN RUTA");
					System.exit(1);
				}
				try{
					totalesPeriodo.setTotMntNeto(Integer.valueOf(arrayLineasTotales.get(x).get(6)));//Requerido
				} catch (Exception e) {
					System.out.println("2@helpcom@EL LIBRO NO SE GENERÓ CORRECTAMENTE ERROR: Al leer DATO TOTALES [NRO:"+x+ ", MONTO NETO] , CAUSA: "+e.getCause()+"@helpcom@<br>SIN RUTA");
					System.exit(1);
				}
				if (arrayLineasTotales.get(x).get(7).trim().length()>0){
					totalesPeriodo.setTotOpIVARec(Long.valueOf(arrayLineasTotales.get(x).get(7)));
				}
				try{
					totalesPeriodo.setTotMntIVA(Integer.valueOf(arrayLineasTotales.get(x).get(8)));//Requerido
				} catch (Exception e) {
					System.out.println("2@helpcom@EL LIBRO NO SE GENERÓ CORRECTAMENTE ERROR: Al leer DATO TOTALES [NRO:"+x+ ", MONTO IVA] , CAUSA: "+e.getCause()+"@helpcom@<br>SIN RUTA");
					System.exit(1);
				}
				if (arrayLineasTotales.get(x).get(9).trim().length()>0){
					totalesPeriodo.setTotOpActivoFijo(Long.valueOf(arrayLineasTotales.get(x).get(9)));
				}
				if (arrayLineasTotales.get(x).get(10).trim().length()>0){
					totalesPeriodo.setTotMntActivoFijo(Integer.valueOf(arrayLineasTotales.get(x).get(10)));
				}
				if (arrayLineasTotales.get(x).get(11).trim().length()>0){
					totalesPeriodo.setTotMntIVAActivoFijo(Integer.valueOf(arrayLineasTotales.get(x).get(11)));
				}

				if (arrayLineasTotales.get(x).get(13).trim().length()>0){
					try{
						ArrayList<TotIVANoRec> totIvaNoRec = new ArrayList<TotIVANoRec>();
						//Inicio ciclo
						TotIVANoRec oi = TotIVANoRec.Factory.newInstance();
						oi.setCodIVANoRec((new BigInteger(arrayLineasTotales.get(x).get(13))));
						oi.setTotOpIVANoRec(((Long.valueOf(arrayLineasTotales.get(x).get(14)))));
						oi.setTotMntIVANoRec(((Long.valueOf(arrayLineasTotales.get(x).get(15)))));
	
						totIvaNoRec.add(oi);
						//Fin ciclo
						TotIVANoRec[] array = new TotIVANoRec[totIvaNoRec.size()];
						totIvaNoRec.toArray(array);
						totalesPeriodo.setTotIVANoRecArray(array);
					} catch (Exception e) {
						System.out.println("2@helpcom@EL LIBRO NO SE GENERÓ CORRECTAMENTE ERROR: Al leer DATO TOTALES [NRO:"+x+ ", IVA NO REC] , CAUSA: "+e.getCause()+"@helpcom@<br>SIN RUTA");
						System.exit(1);
					}
				}
				//codIVANoRec (13)
				//totOpIVANoRec (14)
				//totMntIVANoRec (15)
				if (arrayLineasTotales.get(x).get(16).trim().length()>0){
					totalesPeriodo.setTotOpIVAUsoComun(Long.valueOf(arrayLineasTotales.get(x).get(16)));
				}
				if (arrayLineasTotales.get(x).get(17).trim().length()>0){
					totalesPeriodo.setTotIVAUsoComun(Integer.valueOf(arrayLineasTotales.get(x).get(17)));
				}
				if (arrayLineasTotales.get(x).get(18).trim().length()>0){
					totalesPeriodo.setFctProp(java.math.BigDecimal.valueOf(Double.valueOf(arrayLineasTotales.get(x).get(18))));
				}
				if (arrayLineasTotales.get(x).get(19).trim().length()>0){
					totalesPeriodo.setTotCredIVAUsoComun(Long.valueOf(arrayLineasTotales.get(x).get(19)));
				}
				if (arrayLineasTotales.get(x).get(20).trim().length()>0){
					totalesPeriodo.setTotIVAFueraPlazo(Long.valueOf(arrayLineasTotales.get(x).get(20)));
				}
				if (arrayLineasTotales.get(x).get(21).trim().length()>0){
					totalesPeriodo.setTotIVAPropio((Long.valueOf(arrayLineasTotales.get(x).get(21))));
				}
				if (arrayLineasTotales.get(x).get(22).trim().length()>0){
					totalesPeriodo.setTotIVATerceros(Long.valueOf(arrayLineasTotales.get(x).get(22)));
				}
				if (arrayLineasTotales.get(x).get(23).trim().length()>0){
					totalesPeriodo.setTotLey18211(Long.valueOf(arrayLineasTotales.get(x).get(23)));
				}


				if (arrayLineasTotales.get(x).get(25).trim().length()>0){

					try{
						ArrayList<TotOtrosImp> otrosImpTot = new ArrayList<TotOtrosImp>();
						//Inicio ciclo
						String[] codImpuestoArreglo=txt.separarCodImp(arrayLineasTotales.get(x).get(25));
						String [] montoImpuestoArreglo=txt.separarCodImp(arrayLineasTotales.get(x).get(26));
						
						for (int i = 0; i < codImpuestoArreglo.length; i++) {
							
							TotOtrosImp oi = TotOtrosImp.Factory.newInstance();	
							oi.setCodImp(new BigInteger(codImpuestoArreglo[i]));
							oi.setTotMntImp(Long.valueOf(montoImpuestoArreglo[i]));
							otrosImpTot.add(oi);
							
						}
						
						//Fin ciclo
						TotOtrosImp[] array = new TotOtrosImp[otrosImpTot.size()];
						otrosImpTot.toArray(array);
	
						totalesPeriodo.setTotOtrosImpArray(array);
					} catch (Exception e) {
						System.out.println("2@helpcom@EL LIBRO NO SE GENERÓ CORRECTAMENTE ERROR: Al leer DATO TOTALES [NRO:"+x+ ", OTROS IMPUESTOS] , CAUSA: "+e.getCause()+"@helpcom@<br>SIN RUTA");
						System.exit(1);
					}
				}

				/**CodImp (25)
				*totMntImp (26)
				*FctImpAdic (27)
				*totCredImp (28)
				*/
				if (arrayLineasTotales.get(x).get(29).trim().length()>0){
					totalesPeriodo.setTotImpSinCredito(Long.valueOf(arrayLineasTotales.get(x).get(29)));
				}
				if (arrayLineasTotales.get(x).get(30).trim().length()>0){
					totalesPeriodo.setTotOpIVARetTotal((Long.valueOf(arrayLineasTotales.get(x).get(30))));
				}
				if (arrayLineasTotales.get(x).get(31).trim().length()>0){
					totalesPeriodo.setTotOpIVARetTotal((Long.valueOf(arrayLineasTotales.get(x).get(31))));
				}
				if (arrayLineasTotales.get(x).get(32).trim().length()>0){
					totalesPeriodo.setTotOpIVARetParcial(Long.valueOf(Long.valueOf(arrayLineasTotales.get(x).get(32))));
				}
				if (arrayLineasTotales.get(x).get(33).trim().length()>0){
					totalesPeriodo.setTotIVARetParcial(Long.valueOf(Long.valueOf(arrayLineasTotales.get(x).get(33))));
				}
				if (arrayLineasTotales.get(x).get(34).trim().length()>0){
					totalesPeriodo.setTotCredEC((Long.valueOf(Long.valueOf(arrayLineasTotales.get(x).get(34)))));
				}
				if (arrayLineasTotales.get(x).get(35).trim().length()>0){
					totalesPeriodo.setTotDepEnvase(((Long.valueOf(Long.valueOf(arrayLineasTotales.get(x).get(35))))));
				}
				/*if (arrayLineasTotales[x][36].trim().length()>0){
					totalesPeriodo.setTotLiquidaciones(totLiquidaciones);
				}
				*totValComNeto (37)
				*totValComExe	(38)
				*totValComIVA	(39)
				*/
				try{
					totalesPeriodo.setTotMntTotal(Integer.valueOf(arrayLineasTotales.get(x).get(40)));//Requerido
				} catch (Exception e) {
					System.out.println("2@helpcom@EL LIBRO NO SE GENERÓ CORRECTAMENTE ERROR: Al leer DATO TOTALES [NRO:"+x+ ", MONTO TOTAL] , CAUSA: "+e.getCause()+"@helpcom@<br>SIN RUTA");
					System.exit(1);
				}
				if (arrayLineasTotales.get(x).get(41).trim().length()>0){
					totalesPeriodo.setTotOpIVANoRetenido(Long.valueOf(arrayLineasTotales.get(x).get(41)));
				}
				if (arrayLineasTotales.get(x).get(42).trim().length()>0){
					totalesPeriodo.setTotIVANoRetenido(Long.valueOf(arrayLineasTotales.get(x).get(42)));
				}
				if (arrayLineasTotales.get(x).get(43).trim().length()>0){
					totalesPeriodo.setTotMntNoFact(Long.valueOf(arrayLineasTotales.get(x).get(43)));
				}
				if (arrayLineasTotales.get(x).get(44).trim().length()>0){
					totalesPeriodo.setTotMntPeriodo(Long.valueOf(arrayLineasTotales.get(x).get(44)));
				}
				if (arrayLineasTotales.get(x).get(45).trim().length()>0){
					totalesPeriodo.setTotPsjNac(Long.valueOf(arrayLineasTotales.get(x).get(45)));
				}
				if (arrayLineasTotales.get(x).get(46).trim().length()>0){
					totalesPeriodo.setTotPsjInt(Long.valueOf(arrayLineasTotales.get(x).get(46)));
				}
				if (arrayLineasTotales.get(x).get(47).trim().length()>0){
					totalesPeriodo.setTotTabPuros((Long.valueOf(arrayLineasTotales.get(x).get(47))));
				}
				if (arrayLineasTotales.get(x).get(48).trim().length()>0){
					totalesPeriodo.setTotTabCigarrillos(((Long.valueOf(arrayLineasTotales.get(x).get(48)))));
				}
				if (arrayLineasTotales.get(x).get(49).trim().length()>0){
					totalesPeriodo.setTotTabElaborado(((Long.valueOf(arrayLineasTotales.get(x).get(49)))));
				}
				if (arrayLineasTotales.get(x).get(50).trim().length()>0){
					totalesPeriodo.setTotImpVehiculo((((Long.valueOf(arrayLineasTotales.get(x).get(50))))));
				}
				}
	}



	/**
	 * @param envioLibro
	 * @param arrayLineasDetalles
	 * @param cantTotales
	 */
	public void addDatosDetalle(EnvioLibro envioLibro,ArrayList<ArrayList<String>> arrayLineasDetalles,int cantTotales){

		Calendar cal = Calendar.getInstance();
		String[] date;
		Validador val = new Validador();

	

			for(int x=0;x<cantTotales;x++){
			try {
				Detalle detalle = envioLibro.addNewDetalle();
				try{
//					if (arrayLineasDetalles.get(x).get(0).equals("33") && arrayLineasDetalles.get(x).get(21).equals("0") && !arrayLineasDetalles.get(x).get(4).equals("A") && !arrayLineasDetalles.get(x).get(27).equals("0")){
//						
//						System.out.println("2@helpcom@EL LIBRO NO SE GENERÓ CORRECTAMENTE ERROR: Al leer DATO DETALLE [DTE TIPO 33, MONTO NETO DEBE SER > 0  [FOLIO: "+arrayLineasDetalles.get(x).get(3)+" ]] <br> VALOR CAPTURADO:"+arrayLineasDetalles.get(x).get(27)+"@helpcom@<br>SIN RUTA");
//						
//						System.exit(1);
//					}else if (arrayLineasDetalles.get(x).get(0).equals("33") && arrayLineasDetalles.get(x).get(22).equals("0") && !arrayLineasDetalles.get(x).get(4).equals("A") && !arrayLineasDetalles.get(x).get(27).equals("0")){
//						System.out.println("2@helpcom@EL LIBRO NO SE GENERÓ CORRECTAMENTE ERROR: Al leer DATO DETALLE [DTE TIPO 33, MONTO IVA DEBE SER > 0  [FOLIO: "+arrayLineasDetalles.get(x).get(3)+" ]] @helpcom@<br>SIN RUTA");
//						System.exit(1);
//					}else if (arrayLineasDetalles.get(x).get(0).equals("34") && !arrayLineasDetalles.get(x).get(22).equals("0") && !arrayLineasDetalles.get(x).get(4).equals("A") && !arrayLineasDetalles.get(x).get(27).equals("0")){
//						System.out.println("2@helpcom@EL LIBRO NO SE GENERÓ CORRECTAMENTE ERROR: Al leer DATO DETALLE [DTE TIPO 34, MONTO IVA DEBE SER = 0  [FOLIO: "+arrayLineasDetalles.get(x).get(3)+" ]] @helpcom@<br>SIN RUTA");
//						System.exit(1);
//					}

						detalle.setTpoDoc(detalle.getTpoDoc().valueOf(Integer.valueOf(arrayLineasDetalles.get(x).get(0))));//Requerido
					
				} catch (Exception e) {
					System.out.println("2@helpcom@EL LIBRO NO SE GENERÓ CORRECTAMENTE ERROR: Al leer DATO DETALLE [TIPO DOCUMENTO [FOLIO: "+arrayLineasDetalles.get(x).get(3)+" ]] , CAUSA: "+e.getCause()+"@helpcom@<br>SIN RUTA");
					System.exit(1);
				}
				if (arrayLineasDetalles.get(x).get(1).trim().length()>0){
					detalle.setEmisor(Integer.valueOf(arrayLineasDetalles.get(x).get(1)));
				}
				if (arrayLineasDetalles.get(x).get(2).trim().length()>0){
					detalle.setIndFactCompra(Integer.valueOf(arrayLineasDetalles.get(x).get(2)));
				}
				try{
					detalle.setNroDoc(Long.valueOf(arrayLineasDetalles.get(x).get(3)));//Requerido
				} catch (Exception e) {
					System.out.println("2@helpcom@EL LIBRO NO SE GENERÓ CORRECTAMENTE ERROR: Al leer DATO DETALLE [NUMERO DOCUMENTO [FOLIO: "+arrayLineasDetalles.get(x).get(3)+" ]] , CAUSA: "+e.getCause()+"@helpcom@<br>SIN RUTA");
					System.exit(1);
				}
				if (arrayLineasDetalles.get(x).get(4).trim().length()>0){
					detalle.setAnulado(detalle.getAnulado().forString(arrayLineasDetalles.get(x).get(4)));
				}
				if (arrayLineasDetalles.get(x).get(5).trim().length()>0){
					detalle.setOperacion(detalle.getOperacion().valueOf(Integer.valueOf(arrayLineasDetalles.get(x).get(5))));
				}
				if (arrayLineasDetalles.get(x).get(6).trim().length()>0){
					detalle.setTpoImp(Integer.valueOf(arrayLineasDetalles.get(x).get(6)));
				}
				
				try{
					detalle.setTasaImp(BigDecimal.valueOf((Double.valueOf((arrayLineasDetalles.get(x).get(7))))));//Requerido
				} catch (Exception e) {
					System.out.println("2@helpcom@EL LIBRO NO SE GENERÓ CORRECTAMENTE ERROR: Al leer DATO DETALLE [TASA IMPUESTO [FOLIO: "+arrayLineasDetalles.get(x).get(3)+" ]] , CAUSA: "+e.getCause()+"@helpcom@<br>SIN RUTA");
					System.exit(1);
				}
			
				if (arrayLineasDetalles.get(x).get(8).trim().length()>0){
					detalle.setNumInt(arrayLineasDetalles.get(x).get(8));
				}
				if (arrayLineasDetalles.get(x).get(9).trim().length()>0){
					detalle.setIndServicio(BigInteger.valueOf(Long.valueOf(arrayLineasDetalles.get(x).get(9))));
				}
				if (arrayLineasDetalles.get(x).get(10).trim().length()>0){
					detalle.setIndSinCosto(BigInteger.valueOf(Long.valueOf(arrayLineasDetalles.get(x).get(10))));
				}
				try {
						date=val.getFecha(arrayLineasDetalles.get(x).get(11).replaceAll("/", "-"));
						cal.clear();
						cal.set(Integer.valueOf(date[0]), Integer.valueOf(date[1]), Integer.valueOf(date[2]));
						detalle.setFchDoc(cal);
				} catch (Exception e) {
					System.out.println("2@helpcom@EL LIBRO NO SE GENERÓ CORRECTAMENTE ERROR: Al leer DATO DETALLE [FECHA DOCUMENTO [FOLIO: "+arrayLineasDetalles.get(x).get(3)+" ]] , CAUSA: "+e.getCause()+"@helpcom@<br>SIN RUTA");
					System.exit(1);
				}	
				if (arrayLineasDetalles.get(x).get(12).trim().length()>0){
					detalle.setCdgSIISucur(Integer.valueOf(arrayLineasDetalles.get(x).get(12)));
				}
				if (arrayLineasDetalles.get(x).get(13).trim().length()>0){
					detalle.setRUTDoc(arrayLineasDetalles.get(x).get(13));
				}
				if (arrayLineasDetalles.get(x).get(14).trim().length()>0){
					detalle.setRznSoc(arrayLineasDetalles.get(x).get(14));
				}
				/*if (arrayLineasDetalles[x][15].trim().length()>0){
					detalle.setExtranjero(extranjero);
				}
					NumId (16)
					Nacionalidad (17)
				*/
				if (arrayLineasDetalles.get(x).get(18).trim().length()>0){
					detalle.setTpoDocRef(BigInteger.valueOf(Long.valueOf(arrayLineasDetalles.get(x).get(18))));
				}
				if (arrayLineasDetalles.get(x).get(19).trim().length()>0){
					detalle.setFolioDocRef(Integer.valueOf(arrayLineasDetalles.get(x).get(19)));
				}
				if (arrayLineasDetalles.get(x).get(20).trim().length()>0){
					detalle.setMntExe(Long.valueOf(arrayLineasDetalles.get(x).get(20)));
				}
				if (arrayLineasDetalles.get(x).get(21).trim().length()>0){
					detalle.setMntNeto(Long.valueOf(arrayLineasDetalles.get(x).get(21)));
				}
				if (arrayLineasDetalles.get(x).get(22).trim().length()>0){
					detalle.setMntIVA(Long.valueOf(arrayLineasDetalles.get(x).get(22)));
				}
				if (arrayLineasDetalles.get(x).get(23).trim().length()>0){
					detalle.setMntActivoFijo(Long.valueOf(arrayLineasDetalles.get(x).get(23)));
				}
				if (arrayLineasDetalles.get(x).get(24).trim().length()>0){
					detalle.setMntIVAActivoFijo(Long.valueOf(arrayLineasDetalles.get(x).get(24)));
				}
		
				if (arrayLineasDetalles.get(x).get(26).trim().length()>0){
					try {
						
						ArrayList<IVANoRec> ivaNoRec = new ArrayList<IVANoRec>();
						//Inicio ciclo
						IVANoRec oi = IVANoRec.Factory.newInstance();
						oi.setCodIVANoRec(new BigInteger(arrayLineasDetalles.get(x).get(26)));
						oi.setMntIVANoRec(Long.valueOf(arrayLineasDetalles.get(x).get(27)));
						ivaNoRec.add(oi);
						//Fin ciclo
						IVANoRec[] array = new IVANoRec[ivaNoRec.size()];
						ivaNoRec.toArray(array);
						detalle.setIVANoRecArray(array);
					} catch (Exception e) {
						System.out.println("2@helpcom@EL LIBRO NO SE GENERÓ CORRECTAMENTE ERROR: Al leer DATO DETALLE [IVA NO REC [FOLIO: "+arrayLineasDetalles.get(x).get(3)+" ]] , CAUSA: "+e.getCause()+"@helpcom@<br>SIN RUTA");
						System.exit(1);
					}
				}
				//codIVANoRec 26
				//MntIVANoRec 27
		
				if (arrayLineasDetalles.get(x).get(28).trim().length()>0){
					detalle.setIVAUsoComun(Long.valueOf(arrayLineasDetalles.get(x).get(28)));
				}
				if (arrayLineasDetalles.get(x).get(29).trim().length()>0){
					detalle.setIVAFueraPlazo(Long.valueOf(arrayLineasDetalles.get(x).get(29)));
				}
				if (arrayLineasDetalles.get(x).get(30).trim().length()>0){
					detalle.setIVAPropio(Long.valueOf(arrayLineasDetalles.get(x).get(30)));
				}
				if (arrayLineasDetalles.get(x).get(31).trim().length()>0){
					detalle.setIVATerceros(Long.valueOf(arrayLineasDetalles.get(x).get(31)));
				}
				if (arrayLineasDetalles.get(x).get(32).trim().length()>0){
					detalle.setLey18211(Long.valueOf(arrayLineasDetalles.get(x).get(32)));
				}
		
				if (arrayLineasDetalles.get(x).get(34).trim().length()>0){
		
				try {
					
					ArrayList<OtrosImp> otrosImpDet = new ArrayList<OtrosImp>();
					String[] codImpuestoArreglo=txt.separarCodImp(arrayLineasDetalles.get(x).get(34));
					String[] codImpuestoTasa=txt.separarCodImpTasa(arrayLineasDetalles.get(x).get(35));
					String [] montoImpuestoArreglo=txt.separarMntImp(arrayLineasDetalles.get(x).get(36));  
					
					for (int i = 0; i < codImpuestoArreglo.length; i++) {
						//Inicio ciclo
		//				System.out.println("Lin :"+codImpuestoArreglo[i]+","+codImpuestoTasa[i]+","+montoImpuestoArreglo[i]+";");
						
						OtrosImp oi = OtrosImp.Factory.newInstance();
						oi.setCodImp(new BigInteger(codImpuestoArreglo[i]));
						oi.setTasaImp(new BigDecimal(codImpuestoTasa[i]));
						oi.setMntImp(Long.parseLong(montoImpuestoArreglo[i]));
						otrosImpDet.add(oi);
					}
					
					//Fin ciclo
					OtrosImp[] array = new OtrosImp[otrosImpDet.size()];
					otrosImpDet.toArray(array);
					detalle.setOtrosImpArray(array);
				} catch (Exception e) {
					System.out.println("2@helpcom@EL LIBRO NO SE GENERÓ CORRECTAMENTE ERROR: Al leer DATO DETALLE [IMPUESTO [FOLIO: "+arrayLineasDetalles.get(x).get(3)+" ]] , CAUSA: "+e.getCause()+"@helpcom@<br>SIN RUTA");
					System.exit(1);
				}
				}
				/*CodImp (34)
				TasaImp(35)
				MntImp(36)
				*/
				if (arrayLineasDetalles.get(x).get(37).trim().length()>0){
					detalle.setMntSinCred(Long.valueOf(arrayLineasDetalles.get(x).get(37)));
				}
				if (arrayLineasDetalles.get(x).get(38).trim().length()>0){
					detalle.setIVARetTotal(Long.valueOf(arrayLineasDetalles.get(x).get(38)));
				}
				if (arrayLineasDetalles.get(x).get(39).trim().length()>0){
					detalle.setIVARetParcial(Long.valueOf(arrayLineasDetalles.get(x).get(39)));
				}
				if (arrayLineasDetalles.get(x).get(40).trim().length()>0){
					detalle.setCredEC(Long.valueOf(arrayLineasDetalles.get(x).get(40)));
				}
				if (arrayLineasDetalles.get(x).get(41).trim().length()>0){
					detalle.setDepEnvase(Long.valueOf(arrayLineasDetalles.get(x).get(41)));
				}
				/*if (arrayLineasDetalles[x][42].trim().length()>0){
					detalle.setLiquidaciones(liquidaciones);
				}
				rutEmisor (43)
				valComNeto (44)
				ValComExe (45)
				valComIVA (46)
				*/
				if (arrayLineasDetalles.get(x).get(47).trim().length()>0){
					detalle.setMntTotal(Long.valueOf(arrayLineasDetalles.get(x).get(47)));
				}
				if (arrayLineasDetalles.get(x).get(48).trim().length()>0){
					detalle.setIVANoRetenido(Long.valueOf(arrayLineasDetalles.get(x).get(48)));
				}
				if (arrayLineasDetalles.get(x).get(49).trim().length()>0){
					detalle.setMntNoFact(Long.valueOf(arrayLineasDetalles.get(x).get(49)));
				}
				if (arrayLineasDetalles.get(x).get(50).trim().length()>0){
					detalle.setMntPeriodo(Long.valueOf(arrayLineasDetalles.get(x).get(50)));
				}
				if (arrayLineasDetalles.get(x).get(51).trim().length()>0){
					detalle.setPsjNac(Long.valueOf(arrayLineasDetalles.get(x).get(51)));
				}
				if (arrayLineasDetalles.get(x).get(52).trim().length()>0){
					detalle.setPsjInt(Long.valueOf(arrayLineasDetalles.get(x).get(52)));
				}
				if (arrayLineasDetalles.get(x).get(53).trim().length()>0){
					detalle.setTabPuros(Long.valueOf(arrayLineasDetalles.get(x).get(53)));
				}
				if (arrayLineasDetalles.get(x).get(54).trim().length()>0){
					detalle.setTabCigarrillos(Long.valueOf(arrayLineasDetalles.get(x).get(54)));
				}
				if (arrayLineasDetalles.get(x).get(55).trim().length()>0){
					detalle.setTabElaborado(Long.valueOf(arrayLineasDetalles.get(x).get(55)));
				}
				if (arrayLineasDetalles.get(x).get(56).trim().length()>0){
					detalle.setImpVehiculo(Long.valueOf(arrayLineasDetalles.get(x).get(56)));
				}
			} catch (Exception e) {
				System.out.println("2@helpcom@EL LIBRO NO SE GENERÓ CORRECTAMENTE ERROR: Al leer DATO DETALLE"+e.getMessage()+ "CAUSA: "+e.getCause()+"@helpcom@<br>SIN RUTA");
				System.exit(1);
			}
		}//fin For
	
	}

	public void addDatosResumenSegmento(ResumenSegmento resumenSegmento,ArrayList<ArrayList<String>> arrayLineasTotalesSegmento,int cantTotalesSegmento){

		for(int x=0;x<cantTotalesSegmento;x++){
		cl.sii.siiDte.libroCV.LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.ResumenSegmento.TotalesSegmento totalesSegmento = resumenSegmento.addNewTotalesSegmento();

		totalesSegmento.setTpoDoc(BigInteger.valueOf(Long.valueOf(arrayLineasTotalesSegmento.get(x).get(0))));//REQUERIO
		if (arrayLineasTotalesSegmento.get(x).get(1).trim().length()>0){
			totalesSegmento.setTpoImp(Integer.valueOf(arrayLineasTotalesSegmento.get(x).get(1)));
		}
		totalesSegmento.setTotDoc(Long.valueOf(arrayLineasTotalesSegmento.get(x).get(2)));//REQUERIDO
		if (arrayLineasTotalesSegmento.get(x).get(3).trim().length()>0){
			totalesSegmento.setTotAnulado(Integer.valueOf(arrayLineasTotalesSegmento.get(x).get(3)));
		}
		if (arrayLineasTotalesSegmento.get(x).get(4).trim().length()>0){
			totalesSegmento.setTotOpExe(Integer.valueOf(arrayLineasTotalesSegmento.get(x).get(4)));
		}
		totalesSegmento.setTotMntExe(Long.valueOf(arrayLineasTotalesSegmento.get(x).get(5)));//REQUERIDO
		totalesSegmento.setTotMntNeto(Long.valueOf(arrayLineasTotalesSegmento.get(x).get(6)));//REQUERIDO
		if (arrayLineasTotalesSegmento.get(x).get(7).trim().length()>0){
			totalesSegmento.setTotOpIVARec(Long.valueOf(arrayLineasTotalesSegmento.get(x).get(7)));
		}
		totalesSegmento.setTotMntIVA(Long.valueOf(arrayLineasTotalesSegmento.get(x).get(8)));//REQUERIDO
		if (arrayLineasTotalesSegmento.get(x).get(9).trim().length()>0){
			totalesSegmento.setTotOpActivoFijo(Long.valueOf(arrayLineasTotalesSegmento.get(x).get(9)));
		}
		if (arrayLineasTotalesSegmento.get(x).get(10).trim().length()>0){
			totalesSegmento.setTotMntActivoFijo(Long.valueOf(arrayLineasTotalesSegmento.get(x).get(10)));
		}
		if (arrayLineasTotalesSegmento.get(x).get(11).trim().length()>0){
			totalesSegmento.setTotMntIVAActivoFijo(Long.valueOf(arrayLineasTotalesSegmento.get(x).get(11)));
		}
		// arrayLineasTotalesSegmento[x][12]) NODO
//		if (arrayLineasTotalesSegmento[x][13].trim().length()>0){
//			totalesSegmento.setTotOpIVARec(Long.valueOf(arrayLineasTotalesSegmento[x][13]));//REQUERIDO DE 12
//		}if (arrayLineasTotalesSegmento[x][14].trim().length()>0){
//			totalesSegmento.setTotOpIVARec(Long.valueOf(arrayLineasTotalesSegmento[x][14]));
//		}
//		if (arrayLineasTotalesSegmento[x][15].trim().length()>0){
//			totalesSegmento.setTotOpIVARec(Long.valueOf(arrayLineasTotalesSegmento[x][15]));//REQUERIDO DE 12
//		}
		if (arrayLineasTotalesSegmento.get(x).get(16).trim().length()>0){
			totalesSegmento.setTotOpIVAUsoComun(Long.valueOf(arrayLineasTotalesSegmento.get(x).get(16)));
		}
		if (arrayLineasTotalesSegmento.get(x).get(17).trim().length()>0){
			totalesSegmento.setTotIVAUsoComun(Long.valueOf(arrayLineasTotalesSegmento.get(x).get(17)));
		}
		if (arrayLineasTotalesSegmento.get(x).get(18).trim().length()>0){
			totalesSegmento.setTotIVAFueraPlazo(Long.valueOf(arrayLineasTotalesSegmento.get(x).get(18)));
		}
		if (arrayLineasTotalesSegmento.get(x).get(19).trim().length()>0){
			totalesSegmento.setTotIVAPropio(Long.valueOf(arrayLineasTotalesSegmento.get(x).get(19)));
		}
		if (arrayLineasTotalesSegmento.get(x).get(20).trim().length()>0){
			totalesSegmento.setTotIVATerceros(Long.valueOf(arrayLineasTotalesSegmento.get(x).get(20)));
		}
		if (arrayLineasTotalesSegmento.get(x).get(21).trim().length()>0){
			totalesSegmento.setTotLey18211(Long.valueOf(arrayLineasTotalesSegmento.get(x).get(21)));
		}
		//arrayLineasTotalesSegmento[x][22] NODO
//		if (arrayLineasTotalesSegmento[x][23].trim().length()>0){
//			totalesSegmento.setTotOpIVAUsoComun(Long.valueOf(arrayLineasTotalesSegmento[x][23]));
//		}
//		if (arrayLineasTotalesSegmento[x][24].trim().length()>0){
//			totalesSegmento.setTotOpIVAUsoComun(Long.valueOf(arrayLineasTotalesSegmento[x][24]));
//		}
		if (arrayLineasTotalesSegmento.get(x).get(25).trim().length()>0){
			totalesSegmento.setTotImpSinCredito(Long.valueOf(arrayLineasTotalesSegmento.get(x).get(25)));
		}
		if (arrayLineasTotalesSegmento.get(x).get(26).trim().length()>0){
			totalesSegmento.setTotOpIVARetTotal(Long.valueOf(arrayLineasTotalesSegmento.get(x).get(26)));
		}
		if (arrayLineasTotalesSegmento.get(x).get(27).trim().length()>0){
			totalesSegmento.setTotIVARetTotal(Long.valueOf(arrayLineasTotalesSegmento.get(x).get(27)));
		}
		if (arrayLineasTotalesSegmento.get(x).get(28).trim().length()>0){
			totalesSegmento.setTotOpIVARetParcial(Long.valueOf(arrayLineasTotalesSegmento.get(x).get(28)));
		}
		if (arrayLineasTotalesSegmento.get(x).get(29).trim().length()>0){
			totalesSegmento.setTotIVARetParcial(Long.valueOf(arrayLineasTotalesSegmento.get(x).get(29)));
		}
		if (arrayLineasTotalesSegmento.get(x).get(30).trim().length()>0){
			totalesSegmento.setTotCredEC(Long.valueOf(arrayLineasTotalesSegmento.get(x).get(30)));
		}
		if (arrayLineasTotalesSegmento.get(x).get(31).trim().length()>0){
			totalesSegmento.setTotDepEnvase(Long.valueOf(arrayLineasTotalesSegmento.get(x).get(31)));
		}
		//arrayLineasTotalesSegmento[x][32] NODO TotLiquidaciones
//		if (arrayLineasTotalesSegmento[x][33].trim().length()>0){
//			totalesSegmento.setTotOpIVARetParcial(Long.valueOf(arrayLineasTotalesSegmento[x][33]));
//		}
//		if (arrayLineasTotalesSegmento[x][34].trim().length()>0){
//			totalesSegmento.setTotOpIVARetParcial(Long.valueOf(arrayLineasTotalesSegmento[x][34]));
//		}
//		if (arrayLineasTotalesSegmento[x][35].trim().length()>0){
//			totalesSegmento.setTotOpIVARetParcial(Long.valueOf(arrayLineasTotalesSegmento[x][35]));
//		}

		totalesSegmento.setTotMntTotal(Long.valueOf(arrayLineasTotalesSegmento.get(x).get(36)));//REQUERIDO

		if (arrayLineasTotalesSegmento.get(x).get(37).trim().length()>0){
			totalesSegmento.setTotOpIVANoRetenido(Long.valueOf(arrayLineasTotalesSegmento.get(x).get(37)));
		}
		if (arrayLineasTotalesSegmento.get(x).get(38).trim().length()>0){
			totalesSegmento.setTotIVANoRetenido(Long.valueOf(arrayLineasTotalesSegmento.get(x).get(38)));
		}
		if (arrayLineasTotalesSegmento.get(x).get(39).trim().length()>0){
			totalesSegmento.setTotMntNoFact(Long.valueOf(arrayLineasTotalesSegmento.get(x).get(39)));
		}
		if (arrayLineasTotalesSegmento.get(x).get(40).trim().length()>0){
			totalesSegmento.setTotMntPeriodo(Long.valueOf(arrayLineasTotalesSegmento.get(x).get(40)));
		}
		if (arrayLineasTotalesSegmento.get(x).get(41).trim().length()>0){
			totalesSegmento.setTotPsjNac(Long.valueOf(arrayLineasTotalesSegmento.get(x).get(41)));
		}
		if (arrayLineasTotalesSegmento.get(x).get(42).trim().length()>0){
			totalesSegmento.setTotPsjInt(Long.valueOf(arrayLineasTotalesSegmento.get(x).get(42)));
		}
		if (arrayLineasTotalesSegmento.get(x).get(43).trim().length()>0){
			totalesSegmento.setTotTabPuros(Long.valueOf(arrayLineasTotalesSegmento.get(x).get(43)));
		}
		if (arrayLineasTotalesSegmento.get(x).get(44).trim().length()>0){
			totalesSegmento.setTotTabCigarrillos(Long.valueOf(arrayLineasTotalesSegmento.get(x).get(44)));
		}
		if (arrayLineasTotalesSegmento.get(x).get(45).trim().length()>0){
			totalesSegmento.setTotTabElaborado(Long.valueOf(arrayLineasTotalesSegmento.get(x).get(45)));
		}
		}
	}

}
