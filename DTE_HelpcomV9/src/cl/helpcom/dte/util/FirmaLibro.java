package cl.helpcom.dte.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import java.util.ArrayList;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPException;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.xml.sax.SAXException;

import com.itextpdf.text.pdf.codec.Base64.InputStream;

import cl.helpcom.dao.ConexionSqlGeneraLibro;
import cl.helpcom.dte.method.EnviaDocumentoLibro;
import cl.nic.dte.VerifyResult;
import cl.nic.dte.net.ConexionSiiException;
import cl.sii.siiDte.libroCV.LibroCompraVentaDocument;

public class FirmaLibro {

public String firmarLibro(String certificado,String claveCertificado,String pathXmlLibroSinFirma,String pathXmlLibroFirmado,Integer emp_id,String estado,String tipoLibro,String periodo, String libroXML,String tipoEnvio) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException, ClassNotFoundException, SQLException, InvalidAlgorithmParameterException, KeyException, UnsupportedOperationException, MarshalException, XMLSignatureException, SAXException, ParserConfigurationException, XmlException, SOAPException, ConexionSiiException{


	LibroCompraVentaDocument libro = null;
	
	try {
		libro = LibroCompraVentaDocument.Factory.parse(new FileInputStream(pathXmlLibroSinFirma));
	} catch (Exception e1) {

//		System.out.println("Error al cargar envio de entrada: "+e1.getLocalizedMessage());
		e1.printStackTrace();
//		System.exit(0);

	}
				// leo certificado y llave privada del archivo pkcs12
				KeyStore ks = KeyStore.getInstance("PKCS12");
//				System.out.println("ruta certificado: "+certificado);
				ks.load(new FileInputStream(certificado),claveCertificado.toCharArray());
				String alias = ks.aliases().nextElement();
//				System.out.println("Usando certificado " + alias+ " del archivo PKCS12: " + certificado);

				X509Certificate cert = (X509Certificate) ks.getCertificate(alias);
				PrivateKey key = (PrivateKey) ks.getKey(alias,claveCertificado.toCharArray());
	String salidaCMD="";
				
	try {
		libro.sign(key, cert);
//		salidaCMD+="Firma XML OK<br>";
	} catch (Exception e) {
//		salidaCMD+="Error al firmar el Libro: "+e.getLocalizedMessage()+"@";
		e.printStackTrace();
//		System.exit(0);
	}

	VerifyResult resl = libro.verifyXML();
//	if (!resl.isOk()) {
//		System.out.println("LibroCompraVenta: Estructura XML Incorrecta: "
//				+ resl.getMessage());
//		return;
//	}
//	salidaCMD+="Estructura XML OK<br>";

	File archivoEnvio = new File(pathXmlLibroFirmado);
	
	XmlOptions opts = new XmlOptions();
	opts.setCharacterEncoding("ISO-8859-1");
	try {
		libro.save(archivoEnvio, opts);
	} catch (IOException e) {
		//System.out.println("Error al escribir archivo salida: "+e.getLocalizedMessage());
		//System.exit(0);
	}

	 String cadena;
	 String out="";
     FileReader f = new FileReader(pathXmlLibroFirmado);
     BufferedReader b = new BufferedReader(f);
     while((cadena = b.readLine())!=null) {
         out+=cadena+"\n";
     }
    b.close();
 	
    ConexionSqlGeneraLibro conexionSqlGeneraLibro = new ConexionSqlGeneraLibro(); 
	ArrayList<String> datosEmp  = new ArrayList<>();
	datosEmp= conexionSqlGeneraLibro.getDatosEmpresa(emp_id);
	
	conexionSqlGeneraLibro.addLibro(emp_id, estado, tipoLibro, periodo, out);
	
	salidaCMD+=pathXmlLibroFirmado;
//	System.out.println(salidaCMD);
	
	EnviaDocumentoLibro enviaDocumentoLibro = new EnviaDocumentoLibro();
	//CERTIFICACION-PRODUCCION
	String msjeTipoEnvio=enviaDocumentoLibro.enviarDocumentoSII(emp_id, datosEmp.get(3), datosEmp.get(0), pathXmlLibroFirmado,tipoEnvio);
	
	return msjeTipoEnvio;
	
}

}

