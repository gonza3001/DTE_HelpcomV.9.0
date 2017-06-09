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

import cl.nic.dte.net.ConexionSii;
import cl.nic.dte.util.Utilities;
import cl.sii.siiDte.AUTORIZACIONDocument;
import cl.sii.siiDte.AutorizacionType;
import cl.sii.siiDte.DTEDefType;
import cl.sii.siiDte.DTEDocument;
import cl.sii.siiDte.EnvioDTEDocument;
import cl.sii.siiDte.FechaType;
import cl.sii.siiDte.MedioPagoType;
import cl.sii.siiDte.RECEPCIONDTEDocument;
import jargs.gnu.CmdLineParser;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.io.FileCleaner;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.w3c.dom.Document;

public class GeneraFactura1 {

    private static void printUsage() {
        System.err
                .println("Utilice: java cl.nic.dte.examples.GeneraFactura "
                        + "-a <caf.xml> -p <plantilla.xml> -c <certDigital.p12> -s <password> -o <resultado.xml>");
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        // System.out
        CmdLineParser parser = new CmdLineParser();
        CmdLineParser.Option certOpt = parser.addStringOption('c', "cert");
        CmdLineParser.Option passOpt = parser.addStringOption('s', "password");
        CmdLineParser.Option resultOpt = parser.addStringOption('o', "output");
        CmdLineParser.Option cafOpt = parser.addStringOption('a',
                "autorizacion");
        CmdLineParser.Option plantillaOpt = parser.addStringOption('p',
                "plantilla");
    	
        /*XML FACTURA*/
        String certS = "ejemplos/LuisPalmaPerez.pfx";
        String passS = "7491";
        String resultS = "facturas/factura1.txt";
        String cafS = "autorizacion/caf-33.xml"; //Rango de Folios autorizados por SII
        String plantillaS = "ejemplos/pdoc.xml";//
        String folioS = "11"; //Folio autorizado para ocupar
       
        /*PDF FACTURA*/
        String resultSPDF = "facturas/facturaPDF.pdf";
        String plantillaSPDF = "ejemplos/pdf.xsl";

        String idS = "F102T33";//Folio+TipoDoc

        DTEDocument doc;
        int folio = Integer.valueOf(folioS);

        
        try {
            parser.parse(args);
        } catch (CmdLineParser.OptionException e) {
            printUsage();
            System.exit(2);
        }

        if (certS == null || passS == null || resultS == null || cafS == null
                || plantillaS == null) {
            printUsage();
            System.exit(2);
        }

        // Leo Autorizacion
        // Debo meter el namespace porque SII no lo genera
        HashMap<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("", "http://www.sii.cl/SiiDte");
        
        XmlOptions opts = new XmlOptions();
        opts.setLoadSubstituteNamespaces(namespaces);

        AutorizacionType caf= AUTORIZACIONDocument.Factory.parse(new File (cafS),opts).getAUTORIZACION();
        //AutorizacionType caf = AUTORIZACIONDocument.Factory.parse(new File(cafS), opts).getAUTORIZACION();
        
        /* EMISOR*/
        doc = DTEDocument.Factory.newInstance();
        doc.addNewDTE();
        doc.getDTE().addNewDocumento();
        doc.getDTE().getDocumento().addNewEncabezado();

        DTEDefType.Documento.Encabezado.Emisor em = DTEDefType.Documento.Encabezado.Emisor.Factory.newInstance();
        em.setRUTEmisor("85992100-0");
        em.setRznSoc("SEMILLAS S Z SOCIEDAD ANONIMA ");
        em.setGiroEmis("CULTIVO DE PAPAS, VENTA AL POR MAYOR DE OTROS PRODUCTOS N.C.P.");

        //actividad económica, se puede agregar mas de una
        em.addActeco(1);
        doc.getDTE().getDocumento().getEncabezado().setEmisor(em);

        // Construyo base a partir del template
        //doc = DTEDocument.Factory.parse(new File(plantillaS), opts);
        // leo certificado y llave privada del archivo pkcs12
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(new FileInputStream(certS), passS.toCharArray());
        String alias = ks.aliases().nextElement();
        System.out.println("Usando certificado " + alias
                + " del archivo PKCS12: " + certS);

        X509Certificate cert = (X509Certificate) ks.getCertificate(alias);
        PrivateKey key = (PrivateKey) ks.getKey(alias, passS.toCharArray());

        doc.getDTE().setVersion(BigDecimal.ONE);
        DTEDefType.Documento.Encabezado.IdDoc iddoc = doc.getDTE().getDocumento().getEncabezado().addNewIdDoc();
        iddoc.setFolio(folio);
        doc.getDTE().getDocumento().setID("N" + iddoc.getFolio());
        iddoc.setTipoDTE(BigInteger.valueOf(33));

        iddoc.xsetFchEmis(FechaType.Factory.newValue(Utilities.fechaFormat
                .format(new Date())));

        iddoc.setIndServicio(BigInteger.valueOf(3));
        iddoc.setFmaPago(BigInteger.valueOf(1));

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 45);
        iddoc.xsetFchCancel(FechaType.Factory.newValue(Utilities.fechaFormat
                .format(new Date())));

        /**
         * ****************d
         * codigos medios de pago
         *
         * 1=cheque 2=letra 3=efectivo 4=Pago a Cta. Corriente 5=tarjeta de
         * credito 6=cheque a Fecha 7=Otro
         *
         *******************
         */
        iddoc.setMedioPago(MedioPagoType.Enum.forString("LT"));//Efectivo

        /**
         * *****************
         * codigos forma de pago
         *
         * 1=efectivo 2= credito 3=otro
         *
         * se pueden definir mas ****************
         */
        iddoc.setFmaPago(BigInteger.valueOf(1));//Efectivo

        // Receptor
        DTEDefType.Documento.Encabezado.Receptor recp = doc.getDTE().getDocumento().getEncabezado()
                .addNewReceptor();

        recp.setRUTRecep("60803000-K");
        recp.setRznSocRecep("Servicio de Impuestos Internos");
        recp.setGiroRecep("GOBIERNO CENTRAL Y ADMINISTRACION PUB.");
        recp.setContacto("Director Impuestos Internos");
	//recp.setDirRecep("Teatinos 120");
        //recp.setCmnaRecep("Santiago");
        //recp.setCiudadRecep("Santiago");

        // Totales
        DTEDefType.Documento.Encabezado.Totales tot = doc.getDTE().getDocumento().getEncabezado().addNewTotales();
        tot.setMntNeto(553448);
        tot.setTasaIVA(BigDecimal.valueOf(19));
        tot.setIVA(105155);
        tot.setMntTotal(658603);

        // Agrego detalles
        DTEDefType.Documento.Detalle[] det = new DTEDefType.Documento.Detalle[2];
        
        det[0] = DTEDefType.Documento.Detalle.Factory.newInstance();
        det[0].setNroLinDet(1);
        det[0].setNmbItem("Cajón AFECTO");
        det[0].setQtyItem(BigDecimal.valueOf(146));
        det[0].setPrcItem(BigDecimal.valueOf(2228));
        det[0].setMontoItem(325288);

        det[1] = DTEDefType.Documento.Detalle.Factory.newInstance();
        det[1].setNroLinDet(1);
        det[1].setNmbItem("Relleno AFECTO");
        det[1].setQtyItem(BigDecimal.valueOf(62));
        det[1].setPrcItem(BigDecimal.valueOf(3680));
        det[1].setMontoItem(228160);

        doc.getDTE().getDocumento().setDetalleArray(det);

        // Timbro [obteniendo el key del CAF]
        doc.getDTE().timbrar(caf.getCAF(), caf.getPrivateKey(null));

        // antes de firmar le doy formato a los datos
        opts = new XmlOptions();
        opts.setSaveImplicitNamespaces(namespaces);
        opts.setLoadSubstituteNamespaces(namespaces);
        opts.setSavePrettyPrint();
        opts.setSavePrettyPrintIndent(0);

        //releo el doc para que se reflejen los cambios de formato
        doc = DTEDocument.Factory.parse(doc.newInputStream(opts), opts);

        // Firmo utilizando Private Key
        doc.getDTE().sign(key, cert);

        opts.setSavePrettyPrint();
        opts.setSavePrettyPrintIndent(0);
        opts.setCharacterEncoding("ISO-8859-1");
        opts.setSaveImplicitNamespaces(namespaces);

        // Releo formateado
        doc = DTEDocument.Factory.parse(doc.newInputStream(opts), opts);

        // firmo
        doc.getDTE().sign(key, cert);

        //(new FileOutputStream(resultS)).write(doc.getDTE().sign(key, cert));
        //TEST
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        em.save(bos);

        // Guardo
        opts = new XmlOptions();
        opts.setCharacterEncoding("ISO-8859-1");
        opts.setSaveImplicitNamespaces(namespaces);
        //doc.save(new File(resultS), opts);//Guardado Original

        //QUITAR PREFIJO DS: 
        FolderDate dataFol = new FolderDate();
       // String sDateFol = dataFol.folderYear();
        //String rutaTxt = sDateFol + "/factura1.txt";

        try {
            //doc.save(new File(resultS), opts);//Guardado Original
            doc.save(new File("facturas/F33T33.xml"), opts);
        } catch (Exception e) {
            System.out.println("No se pudo crear el archivo TXT - DTE");
            return;
        }
      
        //GENERA PDF
        Utilities.generatePDF(new FileInputStream("facturas/F33T33.xml"),
                new FileInputStream(plantillaSPDF), new FileOutputStream(
                        resultSPDF));
        
        
        //GENERO ENVIO
        // Construyo Envio
		System.out.println("ÑAÑA:");
                EnvioDTEDocument envio = EnvioDTEDocument.Factory.parse(new FileInputStream("ejemplos/penvio.xml"));
                
		// Debo agregar el schema location (Sino SII rechaza)
		XmlCursor cursor = envio.newCursor();
                
		if (cursor.toFirstChild()) {
			cursor.setAttributeText(new QName(
					"http://www.w3.org/2001/XMLSchema-instance",
					"schemaLocation"),
					"http://www.sii.cl/SiiDte EnvioDTE_v10.xsd");  
		}//Agrega los encabezados de Envio

		 //leo certificado y llave privada del archivo pkcs12
		 ks = KeyStore.getInstance("PKCS12");
		ks.load(new FileInputStream(certS), passS.toCharArray());
		alias = ks.aliases().nextElement();
		System.out.println("Usando certificado " + alias
				+ " del archivo PKCS12: " + certS);

		X509Certificate x509 = (X509Certificate) ks.getCertificate(alias);
		String enviadorS = Utilities.getRutFromCertificate(x509);
		PrivateKey pKey = (PrivateKey) ks.getKey(alias, passS.toCharArray());
                
		// Asigno un ID
		envio.getEnvioDTE().getSetDTE().setID(idS);//F33T33 Folio+TipoDocumento

		cl.sii.siiDte.EnvioDTEDocument.EnvioDTE.SetDTE.Caratula car = envio
				.getEnvioDTE().getSetDTE().getCaratula();
                
		car.setRutReceptor("60803000-K");
		car.setRutEnvia("85992100-0");

		// documentos a enviar
		namespaces = new HashMap<String, String>();
		namespaces.put("", "http://www.sii.cl/SiiDte");
		opts = new XmlOptions();
		opts.setLoadSubstituteNamespaces(namespaces);

		DTEDefType[] dtes = new DTEDefType[1];
		HashMap<Integer, Integer> hashTot = new HashMap<Integer, Integer>();
		
               
                for (int i = 0; i < 1; i++) {

                        dtes[i] = DTEDocument.Factory.parse( new FileInputStream("facturas/F33T33.xml"), opts).getDTE();
			
                        // armar hash para totalizar por tipoDTE
			if (hashTot.get(dtes[i].getDocumento().getEncabezado().getIdDoc()
					.getTipoDTE().intValue()) != null) {
                            
				hashTot.put(dtes[i].getDocumento().getEncabezado().getIdDoc()
						.getTipoDTE().intValue(), hashTot.get(dtes[i]
						.getDocumento().getEncabezado().getIdDoc().getTipoDTE()
						.intValue()) + 1);
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

		EnvioDTEDocument.EnvioDTE.SetDTE.Caratula.SubTotDTE[] subtDtes = new EnvioDTEDocument.EnvioDTE.SetDTE.Caratula.SubTotDTE[hashTot.size()];
              
		int i = 0;
		for (Integer tipo : hashTot.keySet()) {
                  
			EnvioDTEDocument.EnvioDTE.SetDTE.Caratula.SubTotDTE subt = EnvioDTEDocument.EnvioDTE.SetDTE.Caratula.SubTotDTE.Factory.newInstance();
			subt.setTpoDTE(new BigInteger(tipo.toString()));
			subt.setNroDTE(new BigInteger(hashTot.get(tipo).toString()));
			subtDtes[i] = subt;

			i++;
		}
                
		cl.sii.siiDte.EnvioDTEDocument.EnvioDTE.SetDTE.Caratula.SubTotDTE
		subt = cl.sii.siiDte.EnvioDTEDocument.EnvioDTE.SetDTE.Caratula.SubTotDTE.Factory.newInstance();
		subt.setTpoDTE(BigInteger.valueOf(33));
		subt.setNroDTE(BigInteger.valueOf(1));
                
                
		car.setSubTotDTEArray(subtDtes);

		// Le doy un formato bonito (debo hacerlo antes de firmar para no
		// afectar los DTE internos)
		opts = new XmlOptions();
		opts.setSavePrettyPrint();
		opts.setSavePrettyPrintIndent(0);

                
		envio = EnvioDTEDocument.Factory.parse(envio.newInputStream(opts));

		envio.getEnvioDTE().getSetDTE().setDTEArray(dtes);

		// firmo
               
               //System.out.println("pKey ->"+pKey+"\n"+"x509 ->"+x509+"\n");
		
                envio.sign(pKey, x509); //Error al firmar
      System.out.println("PAso firma");
      opts = new XmlOptions();
		opts.setCharacterEncoding("ISO-8859-1");
               
		envio.save(new File("facturas/fac_sin_firmar.xml"), opts);
                    
	//ENVIA DOCUMENTO
                
 /*               ConexionSii con = new ConexionSii();
                
        // leo certificado y llave privada del archivo pkcs12
		ks = KeyStore.getInstance("PKCS12");
		ks.load(new FileInputStream(certS), passS.toCharArray());
		alias = ks.aliases().nextElement();
		System.out.println("Usando certificado " + alias
				+ " del archivo PKCS12: " + certS);
                
                 x509 = (X509Certificate) ks.getCertificate(alias);
		 pKey = (PrivateKey) ks.getKey(alias, passS.toCharArray());

		String token = con.getToken(pKey, x509);

		System.out.println("Token: " + token);
		
		 enviadorS = Utilities.getRutFromCertificate(x509);

		RECEPCIONDTEDocument recp = con.uploadEnvioCertificacion(enviadorS, compaS, new File("jnjn.xml"), token);

		System.out.println(recp.xmlText());
  */              
    }
}
