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
import java.util.Calendar;
import java.util.HashMap;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.xml.sax.SAXException;

import com.sun.org.apache.bcel.internal.generic.AALOAD;

import cl.helpcom.dte.util.AgregarCamposLibroBoleta;
import cl.sii.siiDte.boletas.EnvioBOLETADocument.EnvioBOLETA.SetDTE.Caratula;
import cl.sii.siiDte.consumofolios.ConsumoFoliosDocument;
import cl.sii.siiDte.libroCV.LibroCompraVentaDocument;
import cl.sii.siiDte.libroboletas.LibroBoletaDocument;
import cl.sii.siiDte.libroboletas.LibroBoletaDocument.LibroBoleta;
import cl.sii.siiDte.libroboletas.LibroBoletaDocument.LibroBoleta.EnvioLibro;
import cl.sii.siiDte.libroboletas.LibroBoletaDocument.LibroBoleta.EnvioLibro.ResumenPeriodo;

public class CrearLibroBoleta {

	public static void main(String[] args) throws IOException, XmlException, KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, InvalidAlgorithmParameterException, KeyException, MarshalException, XMLSignatureException, SAXException, ParserConfigurationException {

		String certificado="/home/mau/Dropbox/DTE/BYK/DATOS/CertificadoBYK.pfx";
		String pass="1189";
		
		// TODO Auto-generated method stub
		LibroBoletaDocument libBoleta = null;
		// libBoleta = LibroBoletaDocument.Factory.newInstance(new FileInputStream(libroPlantilla));
		libBoleta = LibroBoletaDocument.Factory.newInstance();
		LibroBoleta lBoleta = libBoleta.addNewLibroBoleta();
		libBoleta.getLibroBoleta().setVersion(new BigDecimal("1.0"));
		EnvioLibro envioLV = lBoleta.addNewEnvioLibro();
		libBoleta.getLibroBoleta().getEnvioLibro().setID("V2001-3");
		
		XmlCursor cursor = libBoleta.newCursor();
		
		ResumenPeriodo resumenPeriodo = envioLV.addNewResumenPeriodo();
		
//		AgregarCamposLibroBoleta camposLibro = new AgregarCamposLibroBoleta();
//		camposLibro.addDatosCartula(envioLibro, arrayLineasCaratula);
		
		Calendar cal = Calendar.getInstance();
		String[] date;
		
		cal.clear();
		cal.set(Calendar.YEAR, Integer.valueOf(2016));
		cal.set(Calendar.MONTH, Integer.valueOf(03));
		
//		cl.sii.siiDte.libroboletas.LibroBoletaDocument.LibroBoleta.EnvioLibro.Caratula caratula =  envioLV.addNewCaratula();
		libBoleta.getLibroBoleta().getEnvioLibro().addNewCaratula();
		libBoleta.getLibroBoleta().getEnvioLibro().getCaratula();
		libBoleta.getLibroBoleta().getEnvioLibro().getCaratula().setRutEmisorLibro("");
		libBoleta.getLibroBoleta().getEnvioLibro().getCaratula().setRutEnvia("");
		libBoleta.getLibroBoleta().getEnvioLibro().getCaratula().setPeriodoTributario(cal);
		libBoleta.getLibroBoleta().getEnvioLibro().getCaratula().setFchResol(cal);
		libBoleta.getLibroBoleta().getEnvioLibro().getCaratula().setNroResol(0);
		libBoleta.getLibroBoleta().getEnvioLibro().getCaratula().setTipoLibro(libBoleta.getLibroBoleta().getEnvioLibro().getCaratula().getTipoLibro().forString("TOTAL"));
		libBoleta.getLibroBoleta().getEnvioLibro().getCaratula().setTipoEnvio(libBoleta.getLibroBoleta().getEnvioLibro().getCaratula().getTipoEnvio().forString(""));
		libBoleta.getLibroBoleta().getEnvioLibro().getCaratula().setNroSegmento(1);
		libBoleta.getLibroBoleta().getEnvioLibro().getCaratula().setFolioNotificacion(1);

		libBoleta.getLibroBoleta().getEnvioLibro().addNewResumenPeriodo();
		libBoleta.getLibroBoleta().getEnvioLibro().getResumenPeriodo().addNewTotalesPeriodo();
		
		libBoleta.getLibroBoleta().getEnvioLibro().getResumenPeriodo().getTotalesPeriodoArray(0).setTpoDoc(BigInteger.valueOf(33));
		libBoleta.getLibroBoleta().getEnvioLibro().getResumenPeriodo().getTotalesPeriodoArray(0).setTotAnulado(Long.valueOf("1"));
		
		libBoleta.getLibroBoleta().getEnvioLibro().getResumenPeriodo().getTotalesPeriodoArray(0).addNewTotalesServicio();
		
		libBoleta.getLibroBoleta().getEnvioLibro().getResumenPeriodo().getTotalesPeriodoArray(0).getTotalesServicioArray(0).setTpoServ(1);
		libBoleta.getLibroBoleta().getEnvioLibro().getResumenPeriodo().getTotalesPeriodoArray(0).getTotalesServicioArray(0).setPeriodoDevengado(cal);//OPCIONAL
		libBoleta.getLibroBoleta().getEnvioLibro().getResumenPeriodo().getTotalesPeriodoArray(0).getTotalesServicioArray(0).setTotDoc(BigInteger.valueOf(555));
		libBoleta.getLibroBoleta().getEnvioLibro().getResumenPeriodo().getTotalesPeriodoArray(0).getTotalesServicioArray(0).setTotMntExe(Long.valueOf("123"));//OPCIONAL
		libBoleta.getLibroBoleta().getEnvioLibro().getResumenPeriodo().getTotalesPeriodoArray(0).getTotalesServicioArray(0).setTotMntNeto(Long.valueOf("123"));
		libBoleta.getLibroBoleta().getEnvioLibro().getResumenPeriodo().getTotalesPeriodoArray(0).getTotalesServicioArray(0).setTasaIVA(BigDecimal.valueOf(19));//OPCIONAL
		libBoleta.getLibroBoleta().getEnvioLibro().getResumenPeriodo().getTotalesPeriodoArray(0).getTotalesServicioArray(0).setTotMntIVA(Long.valueOf("123"));
		libBoleta.getLibroBoleta().getEnvioLibro().getResumenPeriodo().getTotalesPeriodoArray(0).getTotalesServicioArray(0).setTotMntTotal(Long.valueOf("123"));
		libBoleta.getLibroBoleta().getEnvioLibro().getResumenPeriodo().getTotalesPeriodoArray(0).getTotalesServicioArray(0).setTotMntNoFact(Long.valueOf("123"));//OPCIONAL
		libBoleta.getLibroBoleta().getEnvioLibro().getResumenPeriodo().getTotalesPeriodoArray(0).getTotalesServicioArray(0).setTotMntPeriodo(Long.valueOf("123"));//OPCIONAL
		libBoleta.getLibroBoleta().getEnvioLibro().getResumenPeriodo().getTotalesPeriodoArray(0).getTotalesServicioArray(0).setTotSaldoAnt(Long.valueOf("123"));//OPCIONAL
		libBoleta.getLibroBoleta().getEnvioLibro().getResumenPeriodo().getTotalesPeriodoArray(0).getTotalesServicioArray(0).setTotVlrPagar(Long.valueOf("123"));//OPCIONAL
//		//Falta Total Ticket Boleta
//		
//		cl.sii.siiDte.libroboletas.LibroBoletaDocument.LibroBoleta.EnvioLibro.Detalle detalle =  envioLV.addNewDetalle();
		libBoleta.getLibroBoleta().getEnvioLibro().addNewDetalle();
		
		libBoleta.getLibroBoleta().getEnvioLibro().getDetalleArray(0).setTpoDoc(BigInteger.valueOf(1));
		libBoleta.getLibroBoleta().getEnvioLibro().getDetalleArray(0).setFolioDoc(Long.valueOf("1"));
//		detalle.setAnulado(Enum n);
		libBoleta.getLibroBoleta().getEnvioLibro().getDetalleArray(0).setTpoServ(1);
		libBoleta.getLibroBoleta().getEnvioLibro().getDetalleArray(0).setFchEmiDoc(cal);
		libBoleta.getLibroBoleta().getEnvioLibro().getDetalleArray(0).setFchVencDoc(cal);
		libBoleta.getLibroBoleta().getEnvioLibro().getDetalleArray(0).setPeriodoDesde(cal);
		libBoleta.getLibroBoleta().getEnvioLibro().getDetalleArray(0).setPeriodoHasta(cal);
		libBoleta.getLibroBoleta().getEnvioLibro().getDetalleArray(0).setCdgSIISucur(12);
		libBoleta.getLibroBoleta().getEnvioLibro().getDetalleArray(0).setRUTCliente("1-9");
		libBoleta.getLibroBoleta().getEnvioLibro().getDetalleArray(0).setCodIntCli("1");
		libBoleta.getLibroBoleta().getEnvioLibro().getDetalleArray(0).setMntExe(Long.valueOf("123"));
		libBoleta.getLibroBoleta().getEnvioLibro().getDetalleArray(0).setMntTotal(Long.valueOf("123"));
		libBoleta.getLibroBoleta().getEnvioLibro().getDetalleArray(0).setMntNoFact(Long.valueOf("123"));
		libBoleta.getLibroBoleta().getEnvioLibro().getDetalleArray(0).setMntPeriodo(Long.valueOf("123"));
		libBoleta.getLibroBoleta().getEnvioLibro().getDetalleArray(0).setSaldoAnt(Long.valueOf("123"));
		libBoleta.getLibroBoleta().getEnvioLibro().getDetalleArray(0).setVlrPagar(Long.valueOf("123"));		
		//Falta Total Ticket Boleta
		
//		
		HashMap<String, String> namespaces = new HashMap<String, String>();
		namespaces.put("", "http://www.sii.cl/SiiDte");
		XmlOptions opts = new XmlOptions();
		opts.setLoadSubstituteNamespaces(namespaces);
		
		opts = new XmlOptions();
		opts.setSaveImplicitNamespaces(namespaces);
		opts.setLoadSubstituteNamespaces(namespaces);
		opts.setSavePrettyPrint();
		opts.setSavePrettyPrintIndent(0);
//
////		libBoleta = ConsumoFoliosDocument.Factory.parse(Li.newInputStream(opts), opts);
		libBoleta = LibroBoletaDocument.Factory.parse(libBoleta.newInputStream(opts),opts);

		opts = new XmlOptions();
		opts.setCharacterEncoding("ISO-8859-1");
		opts.setSaveImplicitNamespaces(namespaces);
//		
		// leo certificado y llave privada del archivo pkcs12
		KeyStore ks = KeyStore.getInstance("PKCS12");
		ks.load(new FileInputStream(certificado),pass.toCharArray());
		String alias = ks.aliases().nextElement();
		//System.out.println("Usando certificado " + alias	+ " del archivo PKCS12: " + certificado);

		X509Certificate cert = (X509Certificate) ks.getCertificate(alias);
		PrivateKey key = (PrivateKey) ks.getKey(alias,pass.toCharArray());

		System.out.println("Firmando digitalmente");
		
		libBoleta.sign(key, cert);
		opts = new XmlOptions();
		opts.setCharacterEncoding("ISO-8859-1");
		libBoleta.save(new File("/home/mau/Descargas/Boleta/Boleta/libBoleta.xml"),opts);
		
	}

}
