package cl.helpcom.boleta;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.xml.sax.SAXException;

import cl.helpcom.recursos.ComunicadorAppClienteTXT;
import cl.helpcom.recursos.LectorDTEText;
import cl.helpcom.recursos.LectorFichero;
import cl.helpcom.recursos.Validador;
import cl.nic.dte.util.Utilities;
import cl.sii.siiDte.DTEDocument;
import cl.sii.siiDte.FechaType;
import cl.sii.siiDte.boletas.EnvioBOLETADocument;
import cl.sii.siiDte.consumofolios.ConsumoFoliosDocument;
import cl.sii.siiDte.consumofolios.ConsumoFoliosDocument.ConsumoFolios;
import cl.sii.siiDte.consumofolios.ConsumoFoliosDocument.ConsumoFolios.DocumentoConsumoFolios.Resumen.RangoAnulados;
import cl.sii.siiDte.consumofolios.ConsumoFoliosDocument.ConsumoFolios.DocumentoConsumoFolios.Resumen.RangoUtilizados;

public class CrearEnvioConsumoFolio {
	public static void main(String[] args) throws XmlException, IOException, ParseException, NoSuchAlgorithmException, CertificateException, KeyStoreException, UnrecoverableKeyException, InvalidAlgorithmParameterException, KeyException, MarshalException, XMLSignatureException, SAXException, ParserConfigurationException {
	
		LectorFichero ficheroBase = new LectorFichero();
		
		String baseTXT = "/home/mau/Descargas/Boleta/Boleta/consumoFolio.txt";
		String certificado="/home/mau/Dropbox/DTE/BYK/DATOS/CertificadoBYK.pfx";
		String pass="1189";
		
		ArrayList<ArrayList<String>>  arrayCaratula = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>>  arrayTotales = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>>  arrayDetalleConsumo = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>>  arrayDetalleConsumoAnulado = new ArrayList<ArrayList<String>>();
		
		ComunicadorAppClienteTXT c = new ComunicadorAppClienteTXT();
		
		arrayCaratula=c.formatoBoletasConsumoFolio(baseTXT, arrayCaratula, "C");
		arrayTotales=c.formatoBoletasConsumoFolio(baseTXT, arrayTotales, "T");
		arrayDetalleConsumo=c.formatoBoletasConsumoFolio(baseTXT, arrayDetalleConsumo, "D");
		arrayDetalleConsumoAnulado=c.formatoBoletasConsumoFolio(baseTXT, arrayDetalleConsumoAnulado, "A");
		
		
		ConsumoFoliosDocument doc;
		doc=ConsumoFoliosDocument.Factory.newInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
//		date=sdf.parse("2015-01-02");	
//		Calendar cal = sdf.getCalendar();
			
		Calendar cal;
		
		doc.addNewConsumoFolios();
		doc.getConsumoFolios().setVersion(new BigDecimal("1.0"));
		
		doc.getConsumoFolios().addNewDocumentoConsumoFolios();
		doc.getConsumoFolios().getDocumentoConsumoFolios().setID("N134");
		//Caratula
		doc.getConsumoFolios().getDocumentoConsumoFolios().addNewCaratula();
		doc.getConsumoFolios().getDocumentoConsumoFolios().getCaratula().setRutEmisor(arrayCaratula.get(0).get(0));
		doc.getConsumoFolios().getDocumentoConsumoFolios().getCaratula().setRutEnvia(arrayCaratula.get(0).get(1));
		date=sdf.parse(arrayCaratula.get(0).get(2));
		cal = sdf.getCalendar();
		doc.getConsumoFolios().getDocumentoConsumoFolios().getCaratula().setFchResol(cal);
		doc.getConsumoFolios().getDocumentoConsumoFolios().getCaratula().setNroResol(0);
		date=sdf.parse(arrayCaratula.get(0).get(4));
		cal = sdf.getCalendar();
		doc.getConsumoFolios().getDocumentoConsumoFolios().getCaratula().setFchInicio(cal);
		date=sdf.parse(arrayCaratula.get(0).get(5));
		cal = sdf.getCalendar();
		doc.getConsumoFolios().getDocumentoConsumoFolios().getCaratula().setFchFinal(cal);
		doc.getConsumoFolios().getDocumentoConsumoFolios().getCaratula().setSecEnvio(1);
		doc.getConsumoFolios().getDocumentoConsumoFolios().getCaratula().setTmstFirmaEnv(cal);
		
		//Resumen Totales
		for (int i = 0; i < c.getCantidadTotalesConsumo(); i++) {
		
			doc.getConsumoFolios().getDocumentoConsumoFolios().addNewResumen();
			doc.getConsumoFolios().getDocumentoConsumoFolios().getResumenArray(i);
			doc.getConsumoFolios().getDocumentoConsumoFolios().getResumenArray(i).setTipoDocumento(new BigInteger(arrayTotales.get(i).get(0)));
			doc.getConsumoFolios().getDocumentoConsumoFolios().getResumenArray(i).setMntNeto(Long.valueOf(arrayTotales.get(i).get(1)));
			doc.getConsumoFolios().getDocumentoConsumoFolios().getResumenArray(i).setMntIva(Long.valueOf(arrayTotales.get(i).get(2)));
			doc.getConsumoFolios().getDocumentoConsumoFolios().getResumenArray(i).setTasaIVA(new BigDecimal(arrayTotales.get(i).get(3)));
			doc.getConsumoFolios().getDocumentoConsumoFolios().getResumenArray(i).setMntExento(Long.valueOf(arrayTotales.get(i).get(4)));
			doc.getConsumoFolios().getDocumentoConsumoFolios().getResumenArray(i).setMntTotal(Long.valueOf(arrayTotales.get(i).get(5)));
			//Cantidad de documentos UTILIZADOS-EMITIDOS-ANULADOS
			doc.getConsumoFolios().getDocumentoConsumoFolios().getResumenArray(i).setFoliosUtilizados(1);
			doc.getConsumoFolios().getDocumentoConsumoFolios().getResumenArray(i).setFoliosEmitidos(1);
			doc.getConsumoFolios().getDocumentoConsumoFolios().getResumenArray(i).setFoliosAnulados(1);
			
			//Rango de Folios Utilizados
			if (c.getCantidadDetalleConsumo()>0){
				doc.getConsumoFolios().getDocumentoConsumoFolios().getResumenArray(i).addNewRangoUtilizados();
				RangoUtilizados[] rangoUtilizados = new RangoUtilizados[c.getCantidadDetalleConsumo()];

				for (int j = 0; j < c.getCantidadDetalleConsumo(); j++) {
					rangoUtilizados[j] = RangoUtilizados.Factory.newInstance();
					rangoUtilizados[j].setInicial(Long.valueOf(arrayDetalleConsumo.get(j).get(0)));
					rangoUtilizados[j].setFinal(Long.valueOf(arrayDetalleConsumo.get(j).get(1)));
				}
				doc.getConsumoFolios().getDocumentoConsumoFolios().getResumenArray(i).setRangoUtilizadosArray(rangoUtilizados);
			}
			
			//Rango de Folios Anulados
			if (c.getCantidadDetalleConsumoAnulado()>0){
				RangoAnulados[] rangoAnulados = new RangoAnulados[c.getCantidadDetalleConsumoAnulado()];
				doc.getConsumoFolios().getDocumentoConsumoFolios().getResumenArray(i).addNewRangoAnulados();
				for (int j = 0; j < c.getCantidadDetalleConsumoAnulado(); j++) {		
					rangoAnulados[j] = RangoAnulados.Factory.newInstance();	
					rangoAnulados[j].setInicial(Long.valueOf(arrayDetalleConsumoAnulado.get(j).get(0)));
					rangoAnulados[j].setFinal(Long.valueOf(arrayDetalleConsumoAnulado.get(j).get(1)));
				}
				doc.getConsumoFolios().getDocumentoConsumoFolios().getResumenArray(i).setRangoAnuladosArray(rangoAnulados);
			}
		}
				
		HashMap<String, String> namespaces = new HashMap<String, String>();
		namespaces.put("", "http://www.sii.cl/SiiDte");
		XmlOptions opts = new XmlOptions();
		opts.setLoadSubstituteNamespaces(namespaces);
		
		opts = new XmlOptions();
		opts.setSaveImplicitNamespaces(namespaces);
		opts.setLoadSubstituteNamespaces(namespaces);
		opts.setSavePrettyPrint();
		opts.setSavePrettyPrintIndent(0);

		doc = ConsumoFoliosDocument.Factory.parse(doc.newInputStream(opts), opts);

		opts = new XmlOptions();
		opts.setCharacterEncoding("ISO-8859-1");
		opts.setSaveImplicitNamespaces(namespaces);
		
		// leo certificado y llave privada del archivo pkcs12
		KeyStore ks = KeyStore.getInstance("PKCS12");
		ks.load(new FileInputStream(certificado),pass.toCharArray());
		String alias = ks.aliases().nextElement();
		//System.out.println("Usando certificado " + alias	+ " del archivo PKCS12: " + certificado);

		X509Certificate cert = (X509Certificate) ks.getCertificate(alias);
		PrivateKey key = (PrivateKey) ks.getKey(alias,pass.toCharArray());

		System.out.println("Firmando digitalmente");
//		doc.getDTE().sign(key, cert);
		
		doc.sign(key, cert);

		opts = new XmlOptions();
		opts.setCharacterEncoding("ISO-8859-1");
		opts.setSaveImplicitNamespaces(namespaces);
		
		doc.save(new File("/home/mau/Descargas/Boleta/Boleta/conFolio.xml"),opts);
	
		
}}
