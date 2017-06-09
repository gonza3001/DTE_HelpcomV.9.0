package cl.helpcom.dte.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import cl.helpcom.recursos.Formato;
import cl.helpcom.recursos.Validador;
import cl.nic.dte.util.Utilities;
import cl.sii.siiDte.DTEDefType;
import cl.sii.siiDte.DTEDefType.Documento.DscRcgGlobal;
import cl.sii.siiDte.DTEDefType.Documento.Encabezado.IdDoc;
import cl.sii.siiDte.DTEDefType.Documento.Encabezado.Receptor;
import cl.sii.siiDte.DTEDefType.Documento.Encabezado.Totales;
import cl.sii.siiDte.DTEDefType.Documento.Encabezado.Totales.ImptoReten;
import cl.sii.siiDte.DTEDefType.Documento.Referencia;
import cl.sii.siiDte.ImpAdicDTEType.Enum;
import cl.sii.siiDte.boletas.BOLETADefType.Documento.Detalle;
import cl.sii.siiDte.DTEDocument;
import cl.sii.siiDte.FechaType;
import cl.sii.siiDte.MedioPagoType;

/**
 * @author Mauricio Rodriguez
 *
 */
public class AgregarCamposDTE {


	private Formato formato = new Formato();
	private Validador validador = new Validador();

	/**
	 * @param arrayLineasA
	 * @return
	 */
	public DTEDefType.Documento.Encabezado.Emisor addEmisor(
			ArrayList<String> arrayEncabezado) {

		DTEDefType.Documento.Encabezado.Emisor em = DTEDefType.Documento.Encabezado.Emisor.Factory
				.newInstance();

		em.setRUTEmisor(formato.setCaracteresEspeciales(arrayEncabezado.get(11)));
		String rznSocEmi=formato.setCaracteresEspeciales(arrayEncabezado.get(12));
		rznSocEmi.replaceAll("&", "&amp;");
		em.setRznSoc(rznSocEmi);
		em.setGiroEmis(formato.setCaracteresEspeciales(arrayEncabezado.get(13)));
		em.setCmnaOrigen(formato.setCaracteresEspeciales(arrayEncabezado.get(18)));
		em.setDirOrigen(formato.setCaracteresEspeciales(arrayEncabezado.get(17)));
		// Actividad económica, se puede agregar mas de una
		em.addActeco(Integer.valueOf(arrayEncabezado.get(16)));

		return em;
	}

	/**
	 * @param doc
	 * @param arrayLineasA
	 * @param folio
	 * @param tipoDoc
	 */
	public void addIdDoc(DTEDocument doc, ArrayList<String> arrayEncabezado) {
		// IdDoc
		IdDoc iddoc = doc.getDTE().getDocumento().getEncabezado().addNewIdDoc();

		iddoc.setFolio(Integer.valueOf(arrayEncabezado.get(1)));
		doc.getDTE().getDocumento().setID("N" + Integer.valueOf(arrayEncabezado.get(0))+ iddoc.getFolio());
		iddoc.setTipoDTE(BigInteger.valueOf(Integer.valueOf(arrayEncabezado.get(0))));
		iddoc.xsetFchEmis(FechaType.Factory.newValue(arrayEncabezado.get(2)));
		
		if (arrayEncabezado.get(8).equals("1")){
					iddoc.setMntBruto(BigInteger.valueOf(Long.valueOf(arrayEncabezado.get(8))));/**Está ocupando monto BRUTO**/
				}
		Integer tipoDespacho = Integer.valueOf(arrayEncabezado.get(4));/**Ind Translado para GUIA DE DESPACHO**/
		if (!tipoDespacho.equals(0) & Integer.valueOf(arrayEncabezado.get(0)).equals(52)){/**Si es GUIA agregar tipo despacho**/
			iddoc.setTipoDespacho(BigInteger.valueOf(tipoDespacho));
		}
		Integer indTraslado = Integer.valueOf(arrayEncabezado.get(5));/**Ind Translado para GUIA DE DESPACHO**/
		if (!indTraslado.equals(0)){
			iddoc.setIndTraslado(BigInteger.valueOf(indTraslado));
		}
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 45);
		// iddoc.xsetFchCancel(FechaType.Factory.newValue(Utilities.fechaFormat.format(new
		
		Integer medioPago = Integer.valueOf(arrayEncabezado.get(10));
		if (!medioPago.equals(0)) {
			iddoc.setMedioPago(MedioPagoType.Enum.forString("LT"));
		}

		Integer frmPago = Integer.valueOf(arrayEncabezado.get(9));
		if (!frmPago.equals(0)) {// Si Exento != 0
			iddoc.setFmaPago(BigInteger.valueOf(frmPago));
		}
	}

	/**
	 * @param doc
	 * @param arrayLineasA
	 */
	public void addReceptor(DTEDocument doc, ArrayList<String> arrayEncabezado) {
		// Receptor
		Receptor recp = doc.getDTE().getDocumento().getEncabezado().addNewReceptor();

		recp.setRUTRecep(arrayEncabezado.get(21).toUpperCase()); // toUpperCase() Pasar -K a mayuscula

		// Razon Social
		char[] tamRznSocial = arrayEncabezado.get(22).toCharArray();
		if (tamRznSocial.length > 39) {
			String rznSocialRecep=formato.setCaracteresEspeciales(arrayEncabezado.get(22).substring(0, 39));
			rznSocialRecep.replaceAll("&", "&amp;");
			recp.setRznSocRecep(rznSocialRecep);
		} else {
			recp.setRznSocRecep(formato.setCaracteresEspeciales(arrayEncabezado.get(22)));
		}// Fin Razon Social

		// Giro Receptor
		char[] tamGiroRecep = arrayEncabezado.get(23).toCharArray();
		if (tamGiroRecep.length > 40) {
			recp.setGiroRecep(formato.setCaracteresEspeciales(arrayEncabezado.get(23).substring(0, 39)));
		} else {
			recp.setGiroRecep(formato.setCaracteresEspeciales(arrayEncabezado.get(23)));
		}// Fin Giro Receptor

		// Contacto
		char[] tamContactoRecep = arrayEncabezado.get(24).toCharArray();
		if (tamContactoRecep.length > 80) {
			recp.setContacto(formato.setCaracteresEspeciales(arrayEncabezado.get(24).substring(0, 79)));
		} else {
			recp.setContacto(formato.setCaracteresEspeciales(arrayEncabezado.get(24)));
		}// Contacto

		// Dir Receptor
		char[] tamDirRecep = arrayEncabezado.get(26).toCharArray();
		if (tamDirRecep.length > 70) {
			recp.setDirRecep(formato.setCaracteresEspeciales(arrayEncabezado.get(26).substring(0, 69)));
		} else {
			recp.setDirRecep(formato.setCaracteresEspeciales(arrayEncabezado.get(26)));
		}// Dir Receptor

		// Comuna Receptor
		char[] tamComunaRecep = arrayEncabezado.get(27).toCharArray();
		if (tamComunaRecep.length > 20) {
			recp.setCmnaRecep(formato.setCaracteresEspeciales(arrayEncabezado.get(27).substring(0, 19)));
		} else {
			recp.setCmnaRecep(formato.setCaracteresEspeciales(arrayEncabezado.get(27)));
		}// Comuna Receptor

		// Ciudad Receptor
		char[] tamCiudadRecep = arrayEncabezado.get(28).toCharArray();
		if (tamCiudadRecep.length > 20) {
			recp.setCiudadRecep(formato.setCaracteresEspeciales(arrayEncabezado.get(28).substring(0, 19)));
		} else {
			recp.setCiudadRecep(formato.setCaracteresEspeciales(arrayEncabezado.get(28)));
		}// Ciudad Receptor

		// recp.setContacto(arrayEncabezado.get(24));
		// recp.setDirRecep(arrayEncabezado.get(26));
		// recp.setCmnaRecep(arrayEncabezado.get(27));
		// recp.setCiudadRecep(arrayEncabezado.get(28));
	}

	/**
	 * @param doc
	 * @param arrayLineasA
	 */
	public void addTotales(DTEDocument doc, ArrayList<String> arrayEncabezado) {
			// Totales
			Totales tot = doc.getDTE().getDocumento().getEncabezado().addNewTotales();

			if (Double.valueOf(arrayEncabezado.get(46)) == 0.0) {
				tot.setMntTotal(Integer.valueOf(arrayEncabezado.get(46)));// MntTotal
			} else {
			Integer mntNeto=Integer.valueOf(arrayEncabezado.get(36));
			if(!mntNeto.equals(0)){
				tot.setMntNeto(mntNeto);
			}
			Integer exentInt = Integer.valueOf(arrayEncabezado.get(37));
			if (!exentInt.equals(0)) {// Si Exento != 0
				tot.setMntExe(exentInt);
			}
			Integer tasaIVA = Integer.valueOf(arrayEncabezado.get(38));
			if (!tasaIVA.equals(0) && !Integer.valueOf(arrayEncabezado.get(0)).equals(34)) {// Si Tasa IVA != 0 y distinto de exento
				tot.setTasaIVA(BigDecimal.valueOf(Integer.valueOf(arrayEncabezado.get(38))));
			}

			ImpAdicional impAdicional = new ImpAdicional();

			//IMPUESTO CARNE
			if (arrayEncabezado.get(48).equals("-1")){}else {
				String impuesto =arrayEncabezado.get(48);
				String arrayImpuesto[] = impuesto.split(";");
				cl.sii.siiDte.ImpAdicDTEType.Enum codImpAdic = codImpAdic= cl.sii.siiDte.ImpAdicDTEType.Enum.forInt(impAdicional.getPosImp(arrayImpuesto[2]));
				ImptoReten impRet = tot.addNewImptoReten();
				impRet.setTipoImp(codImpAdic);
				impRet.setTasaImp(BigDecimal.valueOf(Double.valueOf(arrayImpuesto[1].replace(",", "."))));
				impRet.setMontoImp(Long.valueOf(arrayImpuesto[0]));
			}
			//IMPUESTO HARINA
			if (arrayEncabezado.get(49).equals("-1")){}else {
				String impuesto =arrayEncabezado.get(49);
				String arrayImpuesto[] = impuesto.split(";");
				cl.sii.siiDte.ImpAdicDTEType.Enum codImpAdic = codImpAdic= cl.sii.siiDte.ImpAdicDTEType.Enum.forInt(impAdicional.getPosImp(arrayImpuesto[2]));
				ImptoReten impRet = tot.addNewImptoReten();
				impRet.setTipoImp(codImpAdic);
				impRet.setTasaImp(BigDecimal.valueOf(Double.valueOf(arrayImpuesto[1].replace(",", "."))));
				impRet.setMontoImp(Long.valueOf(arrayImpuesto[0]));
			}
			//IMPUESTO LICOR
			if (arrayEncabezado.get(50).equals("-1")){}else {
				String impuesto =arrayEncabezado.get(50);
				String arrayImpuesto[] = impuesto.split(";");
				cl.sii.siiDte.ImpAdicDTEType.Enum codImpAdic = codImpAdic= cl.sii.siiDte.ImpAdicDTEType.Enum.forInt(impAdicional.getPosImp(arrayImpuesto[2]));
				ImptoReten impRet = tot.addNewImptoReten();
				impRet.setTipoImp(codImpAdic);
				impRet.setTasaImp(BigDecimal.valueOf(Double.valueOf(arrayImpuesto[1].replace(",", "."))));
				impRet.setMontoImp(Long.valueOf(arrayImpuesto[0]));
			}
			//IMPUESTO VINO
			if (arrayEncabezado.get(51).equals("-1")){}else {
				String impuesto =arrayEncabezado.get(51);
				String arrayImpuesto[] = impuesto.split(";");
				cl.sii.siiDte.ImpAdicDTEType.Enum codImpAdic = codImpAdic= cl.sii.siiDte.ImpAdicDTEType.Enum.forInt(impAdicional.getPosImp(arrayImpuesto[2]));
				ImptoReten impRet = tot.addNewImptoReten();
				impRet.setTipoImp(codImpAdic);
				impRet.setTasaImp(BigDecimal.valueOf(Double.valueOf(arrayImpuesto[1].replace(",", "."))));
				impRet.setMontoImp(Long.valueOf(arrayImpuesto[0]));
			}
			//IMPUESTO SIN AZUCAR
			if (arrayEncabezado.get(52).equals("-1")){}else {
				String impuesto =arrayEncabezado.get(52);
				String arrayImpuesto[] = impuesto.split(";");
				cl.sii.siiDte.ImpAdicDTEType.Enum codImpAdic = codImpAdic= cl.sii.siiDte.ImpAdicDTEType.Enum.forInt(impAdicional.getPosImp(arrayImpuesto[2]));
				ImptoReten impRet = tot.addNewImptoReten();
				impRet.setTipoImp(codImpAdic);
				impRet.setTasaImp(BigDecimal.valueOf(Double.valueOf(arrayImpuesto[1].replace(",", "."))));
				impRet.setMontoImp(Long.valueOf(arrayImpuesto[0]));
			}
			//IMPUESTO CON AZUCAR
			if (arrayEncabezado.get(53).equals("-1")){}else {
				String impuesto =arrayEncabezado.get(53);
				String arrayImpuesto[] = impuesto.split(";");
				cl.sii.siiDte.ImpAdicDTEType.Enum codImpAdic = codImpAdic= cl.sii.siiDte.ImpAdicDTEType.Enum.forInt(impAdicional.getPosImp(arrayImpuesto[2]));
				ImptoReten impRet = tot.addNewImptoReten();
				impRet.setTipoImp(codImpAdic);
				impRet.setTasaImp(BigDecimal.valueOf(Double.valueOf(arrayImpuesto[1].replace(",", "."))));
				impRet.setMontoImp(Long.valueOf(arrayImpuesto[0]));
			}
			//TERPEL(Curacautin)
			if (arrayEncabezado.get(11).equals("83943200-3")){
				//IMPUESTO PETROLEO
				if (arrayEncabezado.get(55).equals("0")){}else {
					String impuesto =arrayEncabezado.get(55);
					String arrayImpuesto[] = impuesto.split(";");
					cl.sii.siiDte.ImpAdicDTEType.Enum codImpAdic = codImpAdic= cl.sii.siiDte.ImpAdicDTEType.Enum.forInt(impAdicional.getPosImp(arrayImpuesto[2]));
					ImptoReten impRet = tot.addNewImptoReten();
					impRet.setTipoImp(codImpAdic);
					impRet.setTasaImp(BigDecimal.valueOf(1));
					impRet.setMontoImp(Long.valueOf(arrayImpuesto[0]));
				}
				//IMPUESTO BENCINA

				if (arrayEncabezado.get(56).equals("0")){}else {
					String impuesto =arrayEncabezado.get(56);
					String arrayImpuesto[] = impuesto.split(";");
					cl.sii.siiDte.ImpAdicDTEType.Enum codImpAdic = codImpAdic= cl.sii.siiDte.ImpAdicDTEType.Enum.forInt(impAdicional.getPosImp(arrayImpuesto[2]));
					ImptoReten impRet = tot.addNewImptoReten();
					impRet.setTipoImp(codImpAdic);
					impRet.setTasaImp(BigDecimal.valueOf(1));
					impRet.setMontoImp(Long.valueOf(arrayImpuesto[0]));
				}

			}

			tot.setIVA(Long.valueOf(arrayEncabezado.get(39)));// 2368287,87
			tot.setMntTotal(Long.valueOf(arrayEncabezado.get(46)));// MntTotal
		}
	}

	/**
	 * @param doc
	 * @param arrayLineasA
	 * @param arrayLineasB
	 * @param arrayLineasR
	 * @param cantidadItem
	 * @param cantidadRef
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws ParseException
	 */
	public DTEDefType.Documento.Detalle[] addDTE33(DTEDocument doc,

			ArrayList<String> arrayEncabezado,
			ArrayList<ArrayList<String>> arrayDetalle)
			throws UnsupportedEncodingException, ParseException {

		// Agrego detalles
		DTEDefType.Documento.Detalle[] det = new DTEDefType.Documento.Detalle[arrayDetalle.size()];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Integer item = 0;
		int conExe = 1;
		DTEDefType.Documento.Detalle detalle = DTEDefType.Documento.Detalle.Factory.newInstance();

		for (item = 0; item < arrayDetalle.size(); item++) {

//				if (arrayDetalle.get(item).get(11).contains("1")){
//					//System.out.println(arrayDetalle.get(item).get(11));
//					//Si es Linea continua
//				}else{
				det[item] = DTEDefType.Documento.Detalle.Factory.newInstance();
				det[item].setNroLinDet(item + 1);

				String nomItem = new String(arrayDetalle.get(item).get(3).getBytes(), "ISO-8859-1");
				char[] arrayCharNom = nomItem.toCharArray();
				if (arrayCharNom.length > 80) {
					det[item].setNmbItem(formato.setCaracteresEspeciales(nomItem.substring(0, 79)));
				} else {
					det[item].setNmbItem(formato.setCaracteresEspeciales(nomItem));

				}
				Double prcItem = Double.parseDouble(arrayDetalle.get(item).get(7).replaceAll(",", "."));
				det[item].setQtyItem(BigDecimal.valueOf(Double.valueOf(arrayDetalle.get(item).get(5).replaceAll(",", "."))));// Cantidad
				if (!prcItem.equals(0.0)){
					det[item].setPrcItem(BigDecimal.valueOf(prcItem));// Precio
				}
				
				String nombreUnidad = new String(arrayDetalle.get(item).get(6).getBytes(), "ISO-8859-1");
				if (!nombreUnidad.equals("0")){
					det[item].setUnmdItem(nombreUnidad);
				}
				
				////CODIGO IMP ADICIONAL
				Double impAdic = Double.valueOf(arrayDetalle.get(item).get(11));

				if (!impAdic.equals(0.0)) {
					ImpAdicional impAdicional = new ImpAdicional();
					cl.sii.siiDte.ImpAdicDTEType.Enum[] codImpAdic= new cl.sii.siiDte.ImpAdicDTEType.Enum[1];
					codImpAdic[0]=cl.sii.siiDte.ImpAdicDTEType.Enum.forInt(impAdicional.getPosImp(arrayDetalle.get(item).get(11)));//posición de tabla
					det[item].setCodImpAdicArray(codImpAdic);
				}

				det[item].setMontoItem(Integer.valueOf(arrayDetalle.get(item).get(12)));// MntItem
				BigDecimal desc1 = BigDecimal.valueOf(Double.valueOf(arrayDetalle.get(item).get(8).replaceAll(",", ".")));// Desc1

				if (desc1.doubleValue() != 0.0) {
					det[item].setDescuentoPct(BigDecimal.valueOf(Double.valueOf(arrayDetalle.get(item).get(8).replaceAll(",", "."))));// PctDesc
					long mntDesc = Long.parseLong(arrayDetalle.get(item).get(9).replaceAll(",", "."));
					det[item].setDescuentoMonto(mntDesc);// Monto de Descuento
				}

				Double cREF = Double.valueOf(arrayDetalle.get(item).get(4));
				if (arrayDetalle.get(item).get(4).equals("1")) {
					det[item].setIndExe(BigInteger.valueOf(1));
//					conExe++;
				}

			}


		return det;
	}
	/**
	 * @param arrayLineasA
	 * @param arrayLineasB
	 * @param cantidadItem
	 * @param doc
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws ParseException
	 */
	public DTEDefType.Documento.Detalle[] addDTE61(
			ArrayList<String> arrayLineasA,
			ArrayList<ArrayList<String>> arrayDetalle)
			throws UnsupportedEncodingException, ParseException {

		int item = 0;
		int conExe = 1;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		// Agrego detalles
		DTEDefType.Documento.Detalle[] det = new DTEDefType.Documento.Detalle[arrayDetalle.size()];

		System.out.println("NOTA DE CREDITO ELECTRONICA");
		for (item = 0; item < arrayDetalle.size(); item++) {

//			if (arrayDetalle.get(item).get(11).contains("1")){
//				//Si es Linea continua
//			}else{
			det[item] = DTEDefType.Documento.Detalle.Factory.newInstance();
//			if (Double.valueOf(arrayLineasA.get(35)) == 0.0) {
			if (0.0 == 0.0) {

				det[item].setNroLinDet(item + 1);
				String nomItem = new String(arrayDetalle.get(item).get(3).getBytes(), "ISO-8859-1");
				char[] arrayCharNom = nomItem.toCharArray();
				if (arrayCharNom.length > 80) {
					det[item].setNmbItem(formato.setCaracteresEspeciales(nomItem.substring(0, 79)));
				} else {
					det[item].setNmbItem(formato.setCaracteresEspeciales(nomItem));
				}
				BigDecimal cantidadItem = BigDecimal.valueOf(Double.valueOf(arrayDetalle.get(item).get(5).replaceAll(",", ".")));// Desc1
				if (cantidadItem.doubleValue() != 0.0) {
					det[item].setQtyItem(BigDecimal.valueOf(Double.valueOf(arrayDetalle.get(item).get(5).replaceAll(",", "."))));// Cantidad
					det[item].setPrcItem(BigDecimal.valueOf(Double.valueOf(arrayDetalle.get(item).get(7).replaceAll(",", "."))));// Precio
				}

					//CODIGO IMP ADICIONAL
					Double impAdic = Double.valueOf(arrayDetalle.get(item).get(11));
					if (!impAdic.equals(0.0)) {
						ImpAdicional impAdicional = new ImpAdicional();
						cl.sii.siiDte.ImpAdicDTEType.Enum[] codImpAdic= new cl.sii.siiDte.ImpAdicDTEType.Enum[1];
						codImpAdic[0]=cl.sii.siiDte.ImpAdicDTEType.Enum.forInt(impAdicional.getPosImp(arrayDetalle.get(item).get(11)));//posición de tabla
						det[item].setCodImpAdicArray(codImpAdic);

					}

				det[item].setMontoItem(Integer.valueOf(arrayDetalle.get(item)
						.get(12)));
				BigDecimal desc1 = BigDecimal.valueOf(Double.valueOf(arrayDetalle.get(item).get(8).replaceAll(",", ".")));// Desc1

				if (desc1.doubleValue() != 0.0) {
					det[item].setDescuentoPct(BigDecimal.valueOf(Double.valueOf(arrayDetalle.get(item).get(8).replaceAll(",", "."))));// PctDesc
					long mntDesc = Long.parseLong(arrayDetalle.get(item).get(9).replaceAll(",", "."));
					det[item].setDescuentoMonto(mntDesc);// Monto de Descuento
				}

				Double cREF = Double.valueOf(arrayDetalle.get(item).get(4));
				if (!cREF.equals(0.0)) {
					det[item].setIndExe(BigInteger.valueOf(1));
//					conExe++;
				}

			} else {
				det[item].setNroLinDet(item + 1);
				Double cREF = Double.valueOf(arrayDetalle.get(item).get(12));
				if (!cREF.equals(0.0)) {
					det[item].setIndExe(BigInteger.valueOf(1));
//					conExe++;
				}
//				String nomItem = new String(arrayDetalle.get(item).get(3)
//						.getBytes("ISO-8859-1"), "ISO-8859-1");
				String nomItem = new String(arrayDetalle.get(item).get(3).getBytes(), "ISO-8859-1");
				det[item].setNmbItem(nomItem);
				det[item].setQtyItem(BigDecimal.valueOf(Double.valueOf(arrayDetalle.get(item).get(3).replaceAll(",", "."))));// Cantidad
				det[item].setPrcItem(BigDecimal.valueOf(Double.valueOf(arrayDetalle.get(item).get(5).replaceAll(",", "."))));// Precio
				det[item].setMontoItem(Integer.valueOf(arrayDetalle.get(item).get(11)));// Cambiar

				Double desc1 = Double.valueOf(arrayDetalle.get(item).get(7));
				if (!desc1.equals(0.0)) {
					det[item].setDescuentoPct(BigDecimal.valueOf(Double.valueOf(arrayDetalle.get(item).get(7).replaceAll(",", "."))));
					long mntDesc = Long.parseLong(arrayDetalle.get(item).get(9).replaceAll(",", "."));
					det[item].setDescuentoMonto(mntDesc);// Monto de
															// descuento
				}
			}
		}

		return det;

	}

	public DTEDefType.Documento.Detalle[] addDTE56(
			ArrayList<String> arrayLineasA,
			ArrayList<ArrayList<String>> arrayDetalle)
			throws UnsupportedEncodingException {
		int item = 0;
		int conExe = 1;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		// Agrego detalles
		DTEDefType.Documento.Detalle[] det = new DTEDefType.Documento.Detalle[arrayDetalle
				.size()];

		System.out.println("NOTA DE DEBITO ELECTRONICA");

		for (item = 0; item < arrayDetalle.size(); item++) {

//			if (arrayDetalle.get(item).get(11).contains("1")){
//				//Si es Linea continua
//					}else{
						det[item] = DTEDefType.Documento.Detalle.Factory.newInstance();
						if (0.0 == 0.0) {
							det[item].setNroLinDet(item + 1);
							String nomItem = new String(arrayDetalle.get(item).get(3).getBytes(), "ISO-8859-1");
							char[] arrayCharNom = nomItem.toCharArray();
							if (arrayCharNom.length > 80) {
								det[item].setNmbItem(formato.setCaracteresEspeciales(nomItem.substring(0, 79)));
							} else {
								det[item].setNmbItem(formato.setCaracteresEspeciales(nomItem));
							}

							BigDecimal cantidadItem = BigDecimal.valueOf(Double.valueOf(arrayDetalle.get(item).get(5).replaceAll(",", ".")));// Desc1
							if (cantidadItem.doubleValue() != 0.0) {
								det[item].setQtyItem(BigDecimal.valueOf(Double.valueOf(arrayDetalle.get(item).get(5).replaceAll(",", "."))));// Cantidad
								det[item].setPrcItem(BigDecimal.valueOf(Double.valueOf(arrayDetalle.get(item).get(7).replaceAll(",", "."))));// Precio
							}

							////CODIGO IMP ADICIONAL
							Double impAdic = Double.valueOf(arrayDetalle.get(item).get(11));
							if (!impAdic.equals(0.0)) {
								ImpAdicional impAdicional = new ImpAdicional();
								cl.sii.siiDte.ImpAdicDTEType.Enum[] codImpAdic= new cl.sii.siiDte.ImpAdicDTEType.Enum[1];
								codImpAdic[0]=cl.sii.siiDte.ImpAdicDTEType.Enum.forInt(impAdicional.getPosImp(arrayDetalle.get(item).get(11)));//posición de tabla
								det[item].setCodImpAdicArray(codImpAdic);

							}

							det[item].setMontoItem(Integer.valueOf(arrayDetalle.get(item).get(12)));

							BigDecimal desc1 = BigDecimal.valueOf(Double.valueOf(arrayDetalle.get(item).get(8).replaceAll(",", ".")));// Desc1
							if (desc1.doubleValue() != 0.0) {
								det[item].setDescuentoPct(BigDecimal.valueOf(Double.valueOf(arrayDetalle.get(item).get(8).replaceAll(",", "."))));// PctDesc
								long mntDesc = Long.parseLong(arrayDetalle.get(item).get(9).replaceAll(",", "."));
								det[item].setDescuentoMonto(mntDesc);// Monto de Descuento
							}

							Double cREF = Double.valueOf(arrayDetalle.get(item).get(4));
							if (!cREF.equals(0.0)) {
								det[item].setIndExe(BigInteger.valueOf(1));
//								conExe++;
							}

						} else {
							// System.out.println("ITEM:"+item);
							det[item].setNroLinDet(item + 1);
							Double cREF = Double.valueOf(arrayDetalle.get(item).get(12));
							if (!cREF.equals(0.0)) {
								det[item].setIndExe(BigInteger.valueOf(1));
//								conExe++;
							}

							String nomItem = new String(arrayDetalle.get(item).get(2)
									.getBytes("ISO-8859-1"), "ISO-8859-1");
							det[item].setNmbItem(nomItem);

							det[item].setQtyItem(BigDecimal.valueOf(Double.valueOf(arrayDetalle.get(item).get(3).replaceAll(",", "."))));// Cantidad
							det[item].setPrcItem(BigDecimal.valueOf(Double.valueOf(arrayDetalle.get(item).get(5).replaceAll(",", "."))));// Precio

							//CODIGO IMP ADICIONAL
							Double impAdic = Double.valueOf(arrayDetalle.get(item).get(11));
							if (!impAdic.equals(0.0)) {
								ImpAdicional impAdicional = new ImpAdicional();
								cl.sii.siiDte.ImpAdicDTEType.Enum[] codImpAdic= new cl.sii.siiDte.ImpAdicDTEType.Enum[1];
								codImpAdic[0]=cl.sii.siiDte.ImpAdicDTEType.Enum.forInt(impAdicional.getPosImp(arrayDetalle.get(item).get(11)));//posición de tabla
								det[item].setCodImpAdicArray(codImpAdic);
							}

							det[item].setMontoItem(Integer.valueOf(arrayDetalle.get(item).get(11)));// Cambiar

							Double desc1 = Double.valueOf(arrayDetalle.get(item).get(7));

							if (!desc1.equals(0.0)) {

								det[item].setDescuentoPct(BigDecimal.valueOf(Double.valueOf(arrayDetalle.get(item).get(7).replaceAll(",", "."))));
								long mntDesc = Long.parseLong(arrayDetalle.get(item).get(9).replaceAll(",", "."));
								det[item].setDescuentoMonto(mntDesc);// Monto de
																		// descuento
							}
						}
		}

		return det;
	}

	public DTEDefType.Documento.DscRcgGlobal[] addDscRecGlobal(
			ArrayList<ArrayList<String>> arrayDscRecGloabl) {

		// Item Descuento Global

		DscRcgGlobal[] dscGB = new DscRcgGlobal[arrayDscRecGloabl.size()];

		for (int cLinDsc = 0; cLinDsc < arrayDscRecGloabl.size(); cLinDsc++) {
			DscRcgGlobal dscGlobal = DscRcgGlobal.Factory.newInstance();
			dscGlobal.setNroLinDR(BigInteger.valueOf(Long.valueOf(arrayDscRecGloabl.get(cLinDsc).get(0))));
			dscGlobal.setTpoMov(dscGlobal.getTpoMov().forString(arrayDscRecGloabl.get(cLinDsc).get(1)));
			dscGlobal.setTpoValor(dscGlobal.getTpoValor().forString(arrayDscRecGloabl.get(cLinDsc).get(2)));

			if (arrayDscRecGloabl.get(cLinDsc).get(2).endsWith("%")){
				dscGlobal.setValorDR(BigDecimal.valueOf(Double.valueOf(arrayDscRecGloabl.get(cLinDsc).get(3).replaceAll(",", "."))));// Porcentaje Dscto

			}else if (arrayDscRecGloabl.get(cLinDsc).get(2).endsWith("$")){
				dscGlobal.setValorDR(BigDecimal.valueOf(Double.valueOf(arrayDscRecGloabl.get(cLinDsc).get(4).replaceAll(",", "."))));// Porcentaje Dscto

			}

			dscGB[cLinDsc] = dscGlobal;
		}

		return dscGB;
	}

	public DTEDefType.Documento.Referencia[] addReferencia(
			ArrayList<ArrayList<String>> arrayReferencia) throws ParseException {

		// AgregoREFERENCIAS
		DTEDefType.Documento.Referencia[] r = new DTEDefType.Documento.Referencia[arrayReferencia.size()];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		for (int item = 0; item < arrayReferencia.size(); item++) {
			Referencia ref = Referencia.Factory.newInstance();
			ref.setNroLinRef(Integer.valueOf(arrayReferencia.get(item).get(0)));
			ref.setTpoDocRef(arrayReferencia.get(item).get(1));// Tipo Doc

			Double indGlobal = Double.valueOf(arrayReferencia.get(item).get(2));
			if (!indGlobal.equals(0.0)) {
				ref.setIndGlobal(Integer.valueOf(arrayReferencia.get(item).get(2)));
			}

			ref.setFolioRef(arrayReferencia.get(item).get(3));// Folio

			Double rutOtr = Double.valueOf(arrayReferencia.get(item).get(4));
			if (!rutOtr.equals(0.0)) {
				ref.setRUTOtr(arrayReferencia.get(item).get(4));
			}

				ref.xsetFchRef(FechaType.Factory.newValue(Utilities.fechaFormat.format(sdf.parse(validador.formatofechaYYMMDDNuevo(arrayReferencia.get(item).get(5))))));
//			ref.xsetFchRef(FechaType.Factory.newValue(Utilities.fechaFormat.format(sdf.parse(arrayReferencia.get(item).get(5)))));


			Double codRef = Double.valueOf(arrayReferencia.get(item).get(6));
			if (!codRef.equals(0.0)) {
				ref.setCodRef(BigInteger.valueOf(Integer.valueOf(arrayReferencia.get(item).get(6))));
			}
			
			try {
				ref.setRazonRef(formato.setCaracteresEspeciales(arrayReferencia.get(item).get(7)));// Razon
			} catch (Exception e) {
				System.out.println("No hay razon de referencia");
			}
			
			r[item] = ref;
		}
		return r;
	}
}
