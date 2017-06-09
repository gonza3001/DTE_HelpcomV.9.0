/**
 * Copyright [2009] [NIC Labs]
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 *
 */
package cl.nic.dte.examples;

import jargs.gnu.CmdLineParser;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlOptions;

import cl.nic.dte.VerifyResult;
import cl.nic.dte.util.Utilities;
import cl.sii.siiDte.AUTORIZACIONDocument;
import cl.sii.siiDte.AutorizacionType;
import cl.sii.siiDte.DTEDefType;
import cl.sii.siiDte.DTEDefType.Documento.DscRcgGlobal;
import cl.sii.siiDte.DTEDefType.Documento.Encabezado.IdDoc;
import cl.sii.siiDte.DTEDefType.Documento.Encabezado.Receptor;
import cl.sii.siiDte.DTEDefType.Documento.Encabezado.Totales;
import cl.sii.siiDte.DTEDefType.Documento.Referencia;
import cl.sii.siiDte.DTEDocument;
import cl.sii.siiDte.EnvioDTEDocument;
import cl.sii.siiDte.EnvioDTEDocument.EnvioDTE.SetDTE.Caratula.SubTotDTE;
import cl.sii.siiDte.FechaType;
import cl.sii.siiDte.MedioPagoType;

public class GeneraFactura3 {

	private static void printUsage() {
		System.err
				.println("Utilice: java cl.nic.dte.examples.GeneraFacturaCompleta "
						+ "-a <caf.xml> -p <plantilla.xml> -c <certDigital.p12> -s <password> -o <resultado.xml>");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		CmdLineParser parser = new CmdLineParser();
		CmdLineParser.Option certOpt = parser.addStringOption('c', "cert");
		CmdLineParser.Option passOpt = parser.addStringOption('s', "password");
		CmdLineParser.Option resultOpt = parser.addStringOption('o', "output");
		CmdLineParser.Option cafOpt = parser.addStringOption('a',"autorizacion");
		CmdLineParser.Option plantillaOpt = parser.addStringOption('p',"plantilla");


		/* XML FACTURA */
		String certS = "certificados/cerBYK.pfx";
		String passS = "1189";
		String resultS = "facturas/factura1.xml";
		String cafS = "autorizacion/FoliosBYK.xml";
		String plantillaS = "ejemplos/penvio.xml";//
		
		/* PDF FACTURA */
		String resultSPDF = "facturas/facturaPDF.pdf";
		String plantillaSPDF = "ejemplos/pdf.xsl";

		String idS = "F102T33";// Folio+TipoDoc
		
		String folioS = "20";
		String tipoDocS ="33";
		
		
		DTEDocument doc;
		int tipoDoc = Integer.valueOf(tipoDocS);
		int folio = Integer.valueOf(folioS);

		
		/* Datos Receptor */

		String recepS = "60803000-K";

		/*		try {
					parser.parse(args);
				} catch (CmdLineParser.OptionException e) {
					printUsage();
					System.exit(2);
				}
		*/		
		
		
		/*
		 * String certS = (String) parser.getOptionValue(certOpt); String passS
		 * = (String) parser.getOptionValue(passOpt); String resultS = (String)
		 * parser.getOptionValue(resultOpt); String cafS = (String)
		 * parser.getOptionValue(cafOpt); String plantillaS = (String)
		 * parser.getOptionValue(plantillaOpt);
		 */

		if (certS == null || passS == null || resultS == null || cafS == null
				|| plantillaS == null) {
			printUsage();
			System.exit(2);
		}

		System.out.println("******** Genera Documento ********");

		
		/* EMISOR */		
		doc = DTEDocument.Factory.newInstance(); //doc = DTEDocument.Factory.parse... Leer archivo
		doc.addNewDTE();
		doc.getDTE().setVersion(new BigDecimal("1.0"));
		doc.getDTE().addNewDocumento();
		doc.getDTE().getDocumento().addNewEncabezado();		
	
		DTEDefType.Documento.Encabezado.Emisor em = DTEDefType.Documento.Encabezado.Emisor.Factory.newInstance();		
		
		em.setRUTEmisor("79716930-7");
		em.setRznSoc("SOC COMERCIALIZADORA B Y K LIMITADA");
		em.setGiroEmis("VENTA O COMPRAVENTA AL POR MENOR DE VEHICULOS AUTOMOTORES NUEVOS O USA");
		em.setCmnaOrigen("TEMUCO");
		em.setDirOrigen("TEMUCO");

		// actividad econ�mica, se puede agregar mas de una
		em.addActeco(501020);
		doc.getDTE().getDocumento().getEncabezado().setEmisor(em);

		// IdDoc
		IdDoc iddoc = doc.getDTE().getDocumento().getEncabezado().addNewIdDoc();
		iddoc.setFolio(folio);
		doc.getDTE().getDocumento().setID("N" + iddoc.getFolio());
		iddoc.setTipoDTE(BigInteger.valueOf(tipoDoc));

		iddoc.xsetFchEmis(FechaType.Factory.newValue(Utilities.fechaFormat
				.format(new Date())));

		// iddoc.setIndServicio(BigInteger.valueOf 3));
		iddoc.setFmaPago(BigInteger.valueOf(1));

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 45);
		iddoc.xsetFchCancel(FechaType.Factory.newValue(Utilities.fechaFormat
				.format(new Date())));

	     /**
         * ****************
         * codigos medios de pago
         *
         * 1=cheque 2=letra 3=efectivo 4=Pago a Cta. Corriente 5=tarjeta de
         * credito 6=cheque a Fecha 7=Otro
         *
         *******************
         */
		iddoc.setMedioPago(MedioPagoType.Enum.forString("LT"));
		 /**
         * *****************
         * codigos forma de pago
         *
         * 1=efectivo 2= credito 3=otro
         *
         * se pueden definir mas ****************
         */
		iddoc.setFmaPago(BigInteger.valueOf(2));

		// Receptor
		Receptor recp = doc.getDTE().getDocumento().getEncabezado().addNewReceptor();

		recp.setRUTRecep("55555555-5");
		recp.setRznSocRecep("Warner Brothers");
		recp.setGiroRecep("Divertimento");
		recp.setContacto("Pinky y Cerebro");
		recp.setDirRecep("Embajada USA");
		recp.setCmnaRecep("Vitacura");
		recp.setCiudadRecep("Santiago");
		
//		recp.setRUTRecep("60803000-K");
//	    recp.setRznSocRecep("Servicio de Impuestos Internos");
//	    recp.setGiroRecep("GOBIERNO CENTRAL Y ADMINISTRACION PUB.");
//	    recp.setContacto("Director Impuestos Internos");
//		//recp.setDirRecep("Teatinos 120");
	    //recp.setCmnaRecep("Santiago");
	    //recp.setCiudadRecep("Santiago");
		

		// Totales
		Totales tot = doc.getDTE().getDocumento().getEncabezado().addNewTotales();
		
	   	tot.setMntNeto(1201625);
        tot.setTasaIVA(BigDecimal.valueOf(19));
        tot.setIVA(228309);//2368287,87
        tot.setMntTotal(1429934);
        
        // Descuento recargo global
        DscRcgGlobal dscGlobal = DscRcgGlobal.Factory.newInstance();
        
        
        
        // Agrego detalles
        DTEDefType.Documento.Detalle[] det = new DTEDefType.Documento.Detalle[2];
        
        det[0] = DTEDefType.Documento.Detalle.Factory.newInstance();
        det[0].setNroLinDet(1);
        det[0].setNmbItem("Caj�n AFECTO");
        det[0].setQtyItem(BigDecimal.valueOf(177));//cantidad
        det[0].setPrcItem(BigDecimal.valueOf(3999));
        det[0].setMontoItem(707823);
        
       // det[0].setDescuentoPct(BigDecimal.valueOf(12));//Descuento ITEM  **det[0].setDescuentoPct(12)**
       // det[0].setDescuentoMonto(820331);

        det[1] = DTEDefType.Documento.Detalle.Factory.newInstance();
        det[1].setNroLinDet(2);
        det[1].setNmbItem("Relleno AFECTO");
        det[1].setQtyItem(BigDecimal.valueOf(74));//cantidad
        det[1].setPrcItem(BigDecimal.valueOf(6673));
        det[1].setMontoItem(493802);
        
        //det[1].setDescuentoPct(BigDecimal.valueOf(29));//descuento por ITEM   
        //det[1].setDescuentoMonto(1632288);
   
        
		doc.getDTE().getDocumento().setDetalleArray(det);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		// REFERENCIAS
		/*Referencia ref = Referencia.Factory.newInstance();
		ref.setNroLinRef(1);
		ref.setTpoDocRef("801");
		ref.setFolioRef("OC-1");
		ref.xsetFchRef(FechaType.Factory.newValue(Utilities.fechaFormat
				.format(sdf.parse("2014-10-17"))));
		Referencia[] refArray = new Referencia[1];
		refArray[0] = ref;
		
		doc.getDTE().getDocumento().setReferenciaArray(refArray);
		*/

		// Leo la autorizacion y timbro
		// Debo meter el namespace porque SII no lo genera
		HashMap<String, String> namespaces = new HashMap<String, String>();
		namespaces.put("", "http://www.sii.cl/SiiDte");
		XmlOptions opts = new XmlOptions();
		opts.setLoadSubstituteNamespaces(namespaces);

		AutorizacionType auth = AUTORIZACIONDocument.Factory.parse(
				new File(cafS), opts).getAUTORIZACION();

		doc.getDTE().timbrar(auth.getCAF(), auth.getPrivateKey(null));

		// leo certificado y llave privada del archivo pkcs12
		KeyStore ks = KeyStore.getInstance("PKCS12");
		ks.load(new FileInputStream(certS), passS.toCharArray());
		String alias = ks.aliases().nextElement();
		System.out.println("Usando certificado " + alias
				+ " del archivo PKCS12: " + certS);

		X509Certificate x509 = (X509Certificate) ks.getCertificate(alias);
		PrivateKey pKey = (PrivateKey) ks.getKey(alias, passS.toCharArray());

		// Le doy un formato bonito
		opts.setSavePrettyPrint();
		opts.setSavePrettyPrintIndent(0);
		opts.setCharacterEncoding("ISO-8859-1");
		opts.setSaveImplicitNamespaces(namespaces);

		doc = DTEDocument.Factory.parse(doc.newInputStream(opts), opts);

		// firmo
		doc.getDTE().sign(pKey, x509);

		opts = new XmlOptions();
		opts.setCharacterEncoding("ISO-8859-1");
		opts.setSaveImplicitNamespaces(namespaces);
		doc.save(new File(resultS), opts);

		/*
		 * //GENERA PDF Utilities.generatePDF(new FileInputStream(resultS), new
		 * FileInputStream(plantillaSPDF), new FileOutputStream( resultSPDF));
		 */

		// Construyo Envio
		opts = new XmlOptions();
		opts.setCharacterEncoding("ISO-8859-1");
		opts.setLoadSubstituteNamespaces(namespaces);

		EnvioDTEDocument envio = EnvioDTEDocument.Factory.newInstance(opts);
		

		
		envio.addNewEnvioDTE().addNewSetDTE();
		// Debo agregar el schema location (Sino SII rechaza)
		XmlCursor cursor = envio.newCursor();
		if (cursor.toFirstChild()) {
			cursor.setAttributeText(new QName(
					"http://www.w3.org/2001/XMLSchema-instance",
					"schemaLocation"),
					"http://www.sii.cl/SiiDte EnvioDTE_v10.xsd");
		}

		// leo certificado y llave privada del archivo pkcs12
		// KeyStore ks = KeyStore.getInstance("PKCS12");
		ks.load(new FileInputStream(certS), passS.toCharArray());
		// String alias = ks.aliases().nextElement();
		System.out.println("Usando certificado " + alias
				+ " del archivo PKCS12: " + certS);

		// X509Certificate x509 = (X509Certificate) ks.getCertificate(alias);
		String enviadorS = Utilities.getRutFromCertificate(x509);
		// PrivateKey pKey = (PrivateKey) ks.getKey(alias, passS.toCharArray());

		// Asigno un ID
		envio.getEnvioDTE().getSetDTE().setID(idS);
		envio.getEnvioDTE().setVersion(new BigDecimal("1.0"));

		cl.sii.siiDte.EnvioDTEDocument.EnvioDTE.SetDTE.Caratula car = envio
				.getEnvioDTE().getSetDTE().addNewCaratula();

		car.setVersion(new BigDecimal("1.0"));
		car.setRutEmisor(em.getRUTEmisor());
		car.setRutReceptor(recepS);
		car.setRutEnvia(enviadorS);
		car.xsetFchResol(FechaType.Factory.newValue(Utilities.fechaFormat
				.format(sdf.parse("2015-02-20"))));
		car.setNroResol(0);

		// documentos a enviar
		// HashMap<String, String> namespaces = new HashMap<String, String>();
		namespaces.put("", "http://www.sii.cl/SiiDte");
		// XmlOptions opts = new XmlOptions();
		opts = new XmlOptions();
		opts.setCharacterEncoding("ISO-8859-1");
		opts.setLoadSubstituteNamespaces(namespaces);

		
		DTEDefType[] dtes = new DTEDefType[1];
		
		
		// DTEDefType[] dtes = new DTEDefType[otherArgs.length];
		
		 HashMap<Integer, Integer> hashTot = new HashMap<Integer, Integer>();
		for (int i = 0; i < 1; i++) {// for (int i = 0; i < otherArgs.length;
										// i++) {

			dtes[i] = DTEDocument.Factory.parse(
					new FileInputStream("facturas/factura1.xml"), opts)
					.getDTE();
			// armar hash para totalizar por tipoDTE
			if (hashTot.get(dtes[i].getDocumento().getEncabezado().getIdDoc()
					.getTipoDTE().intValue()) != null) {
				hashTot.put(
						dtes[i].getDocumento().getEncabezado().getIdDoc()
								.getTipoDTE().intValue(),
						hashTot.get(dtes[i].getDocumento().getEncabezado()
								.getIdDoc().getTipoDTE().intValue()) + 1);
			} else {
				hashTot.put(dtes[i].getDocumento().getEncabezado().getIdDoc()
						.getTipoDTE().intValue(), 1);
			}

			// if
			// (!dtes[i].getDocumento().getEncabezado().getIdDoc().getTipoDTE()
			// .equals(BigInteger.valueOf(tipo))) {
			// System.err.println("Documento folio: "
			// + dtes[i].getDocumento().getEncabezado().getIdDoc()
			// .getFolio() + " no corresponde al tipo");
			// System.exit(3);
			// }
		}

		SubTotDTE[] subtDtes = new SubTotDTE[hashTot.size()];
		int i = 0;
		for (Integer tipo : hashTot.keySet()) {
			SubTotDTE subt = SubTotDTE.Factory.newInstance();
			subt.setTpoDTE(new BigInteger(tipo.toString()));
			subt.setNroDTE(new BigInteger(hashTot.get(tipo).toString()));
			subtDtes[i] = subt;
			i++;
		}

		// cl.sii.siiDte.EnvioDTEDocument.EnvioDTE.SetDTE.Caratula.SubTotDTE
		// subt =
		// cl.sii.siiDte.EnvioDTEDocument.EnvioDTE.SetDTE.Caratula.SubTotDTE.Factory
		// .newInstance();
		// subt.setTpoDTE(BigInteger.valueOf(tipo));
		// subt.setNroDTE(BigInteger.valueOf(otherArgs.length));
		
		car.setSubTotDTEArray(subtDtes);

		// Le doy un formato bonito (debo hacerlo antes de firmar para no
		// afectar los DTE internos)
		opts = new XmlOptions();
		opts.setSavePrettyPrint();
		opts.setSavePrettyPrintIndent(0);
		opts.setUseDefaultNamespace();
		opts.setSaveSuggestedPrefixes(namespaces);
//		opts.setSaveImplicitNamespaces(namespaces);


		envio = EnvioDTEDocument.Factory.parse(envio.newInputStream(opts));

		envio.getEnvioDTE().getSetDTE().setDTEArray(dtes);

		// firmo
		envio.sign(pKey, x509);

		opts = new XmlOptions();
		opts.setCharacterEncoding("ISO-8859-1");
		//opts.setSaveImplicitNamespaces(namespaces);
		envio.save(new File("facturas/envio1.xml"), opts);

		System.out.println("******** Verifica Factura ********");

		VerifyResult resl = doc.getDTE().verifyTimbre();
		if (!resl.isOk()) {
			System.out.println("Documento: Timbre Incorrecto: "
					+ resl.getMessage());
		} else {
			System.out.println("Documento: Timbre OK");
		}

		resl = doc.getDTE().verifyXML();
		if (!resl.isOk()) {
			System.out.println("Documento: Estructura XML Incorrecta: "
					+ resl.getMessage());
		} else {
			System.out.println("Documento: Estructura XML OK");
		}

		resl = doc.getDTE().verifySignature();
		if (!resl.isOk()) {
			System.out.println("Documento: Firma XML Incorrecta: "
					+ resl.getMessage());
		} else {
			System.out.println("Documento: Firma XML OK");
		}

		// VERIFICACION ENVIO
		resl = envio.verifyXML();
		if (!resl.isOk()) {
			System.out.println("Envio: Estructura XML Incorrecta: "
					+ resl.getMessage());
		} else {
			System.out.println("Envio: Estructura XML OK");
		}

		resl = envio.verifySignature();
		if (!resl.isOk()) {
			System.out.println("Envio: Firma XML Incorrecta: "
					+ resl.getMessage());
		} else {
			System.out.println("Envio: Firma XML OK");
		}

	}

}

