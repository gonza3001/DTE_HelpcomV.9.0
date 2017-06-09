package cl.helpcom.dte.method;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.util.Properties;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPException;

import org.apache.xmlbeans.XmlException;
import org.xml.sax.SAXException;

import cl.helpcom.dao.ConexionSqlEnviaDocumento;
import cl.helpcom.recursos.LectorDTEText;
import cl.helpcom.recursos.LectorFichero;
import cl.nic.dte.net.ConexionSii;
import cl.nic.dte.net.ConexionSiiException;
import cl.nic.dte.util.Utilities;
import cl.sii.siiDte.RECEPCIONDTEDocument;

public class EnviaDocumentov3 {

	private LectorFichero lectorFichero = new LectorFichero();

public void enviarDocumentoSII(Integer idEmp,String passS, String rutEmp) throws ClassNotFoundException, SQLException, IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, InvalidAlgorithmParameterException, KeyException, UnsupportedOperationException, MarshalException, XMLSignatureException, SAXException, ParserConfigurationException, XmlException, SOAPException, ConexionSiiException {

	Properties propiedades = new Properties();
	InputStream entrada = null;
	entrada = new FileInputStream("/usr/local/F_E/Configuraciones/Propiedades/configuracionServidor.properties");
	propiedades.load(entrada);
	ConexionSqlEnviaDocumento sqlEnviaDoc = new ConexionSqlEnviaDocumento();

	String compaS = rutEmp;
	String raiz=propiedades.getProperty("RUTA_RAIZ");
	String certS = raiz+"/"+ idEmp +"/"+propiedades.getProperty("RUTA_CERTIFICADO");
	String rutaEnvio= raiz+"/"+ idEmp+"/"+propiedades.getProperty("RUTA_EMPAQUETADO");
	String rutaEnvioRespaldo= raiz+"/"+ idEmp+"/"+propiedades.getProperty("RUTA_ENVIO_RESPALDO");
	//String enviadorS = (String) parser.getOptionValue(enviadorOpt);
	Integer trackID=0;

	ConexionSii con = new ConexionSii();
	// leo certificado y llave privada del archivo pkcs12
	KeyStore ks = KeyStore.getInstance("PKCS12");
	ks.load(new FileInputStream(certS), passS.toCharArray());
	String alias = ks.aliases().nextElement();
//	System.out.println("Usando certificado " + alias
//			+ " del archivo PKCS12: " + certS);

	X509Certificate x509 = (X509Certificate) ks.getCertificate(alias);
	PrivateKey pKey = (PrivateKey) ks.getKey(alias, passS.toCharArray());

	try {
		String token = con.getToken(pKey, x509);
		System.out.println("Token: " + token);
		String enviadorS = Utilities.getRutFromCertificate(x509);
		//leer documento PKG a enviar
		String documentoPkg = lectorFichero.leerDocumentoTXT(rutaEnvio);
		Long track=null;
		
		
		String nroPkg ="";
		if (documentoPkg.length()>0){//Si hay algun documento para enviar en la carpeta ENVIO
			nroPkg =lectorFichero.getNombreArchivo(documentoPkg);//obtiene el nombre del archivo en Envio
		
	//		RECEPCIONDTEDocument recp = con.uploadEnvioCertificacion(enviadorS, compaS, new File(documentoPkg), token);
			System.out.println("\nEnviador:"+enviadorS+"\nCompania: "+compaS+"\nRutaDoc:"+documentoPkg+"\n Token: "+token);
			RECEPCIONDTEDocument recp = con.uploadEnvioProduccion(enviadorS, compaS, new File(documentoPkg), token);
			
			//borrar documento enviado
			String respaldoPkg=raiz+"/"+ idEmp +"/"+propiedades.getProperty("RUTA_ENVIO_RESPALDO");
			
			System.out.println(recp.xmlText());
			
			/*Funcion Momentanea para capturar track ID*/
			String xmlText=recp.xmlText();
			int inicio = xmlText.indexOf("<siid:TRACKID>");
			xmlText= xmlText.substring(inicio + 1);
			xmlText = xmlText.replaceAll("siid:TRACKID>", "");
			xmlText = xmlText.replaceAll("</</siid:RECEPCIONDTE>", "");
			xmlText = xmlText.replaceAll("</", "");
			xmlText = xmlText.replaceAll("siid:RECEPCIONDTE>", "");
			xmlText = xmlText.replaceAll(" ", "");
			xmlText = xmlText.replaceAll("\\s","");
			/*Funcion Momentanea para capturar track ID*/
			
//			trackID= recp.getRECEPCIONDTE().getTRACKIDArray(0);ORGINAL 
			sqlEnviaDoc.setEstadoENVIADO(Integer.valueOf(nroPkg));
//			sqlEnviaDoc.setTrackID(track,Integer.valueOf(nroPkg));ORGINAL
			sqlEnviaDoc.setTrackID(Long.valueOf(xmlText),Integer.valueOf(nroPkg));
			System.out.println("Saved track ID");
			lectorFichero.moverFichero(documentoPkg, respaldoPkg);
			
		}
		sqlEnviaDoc.cerrarConexion();
		
	} catch (Exception e) {
		System.out.println("No se pudo obtener TOKEN desde SII" + e);
	}
	
}
}
