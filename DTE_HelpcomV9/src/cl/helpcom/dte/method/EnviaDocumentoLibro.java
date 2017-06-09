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

public class EnviaDocumentoLibro {

	private LectorFichero lectorFichero = new LectorFichero();

public String enviarDocumentoSII(Integer idEmp,String passS, String rutEmp,String rutaLibro,String tipoEnvio) throws ClassNotFoundException, SQLException, IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, InvalidAlgorithmParameterException, KeyException, UnsupportedOperationException, MarshalException, XMLSignatureException, SAXException, ParserConfigurationException, XmlException, SOAPException, ConexionSiiException {

	ConexionSqlEnviaDocumento sqlEnviaDoc = new ConexionSqlEnviaDocumento();

	String compaS = rutEmp;
	String raiz="/usr/local/F_E/DTE";
	String certS = raiz+"/"+ idEmp +"/Certificado/Certificado.pfx";
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

	String token = con.getToken(pKey, x509);
		//System.out.println("Token: " + token);
		String enviadorS = Utilities.getRutFromCertificate(x509);
		
		if (tipoEnvio.equals("1")){
			//CERTIFICACION
			RECEPCIONDTEDocument recp = con.uploadEnvioCertificacion(enviadorS, compaS, new File(rutaLibro), token);
			sqlEnviaDoc.cerrarConexion();
			return "<br>Se ha enviado Libro en ambiente de CERTIFICACION<br>";
		}else if (tipoEnvio.equals("2")){
			//PRODUCCION
			RECEPCIONDTEDocument recp = con.uploadEnvioProduccion(enviadorS, compaS, new File(rutaLibro), token);
			sqlEnviaDoc.cerrarConexion();
			return "<br>Se ha enviado Libro en ambiente de PRODUCCION <br>";
		}else {
			sqlEnviaDoc.cerrarConexion();
			return "ENTRADA -X: "+tipoEnvio+"<br>";	
		}

//		System.out.println(recp.xmlText());

	}

}

