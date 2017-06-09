/**
 * 
 */
package cl.helpcom.dte;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * Esta clase se encarga de realizar todas las operaciones de certificados
 * digitales
 * 
 * @author Jose Urzua <a href="mailto:jose@urzua.cl">jose@urzua.cl</a>
 * 
 */
public class Certificado {
	private static Logger logger = Logger.getLogger(Certificado.class);

	/**
	 * Obtiene el certificado digital asociado a este rut desde la base de datos
	 * y lo carga en un objeto CertificadoLlave
	 * 
	 * @param rutEmisor
	 * @return
	 * @throws LogicaException
	 */
	public CertificadoLlave getCertificado(String pathCertificado, String clave) {
		CertificadoLlave certLlave = null;
		try {
			// ya se tiene certificado, ahora se debe cargar
			certLlave = new CertificadoLlave();
			KeyStore ks;
			X509Certificate x509 = null;
			PrivateKey pKey = null;
			ks = KeyStore.getInstance("PKCS12"); 
			ks.load(new FileInputStream(pathCertificado), clave.toCharArray());

			String alias = (String) ks.aliases().nextElement();
			logger.debug("Usando certificado " + alias
					+ " del archivo PKCS12: " + pathCertificado);

			x509 = (X509Certificate) ks.getCertificate(alias);
			pKey = (PrivateKey) ks.getKey(alias, clave.toCharArray());
			certLlave.setX509(x509);
			certLlave.setPKey(pKey);
		} catch (Exception e) {
			logger.error(e);
		}
		return certLlave;
	}
	
	/**
	 * Obtiene el RUT desde un certificado digital. Busca en la extension
	 * 2.5.29.17
	 * 
	 * @param x509
	 * @return
	 */
	private String getRutFromCertificate(X509Certificate x509) {
		String rut = null;
		Pattern p = Pattern.compile("[\\d]{6,8}-[\\dkK]");
	
		Matcher m = p.matcher(new String(x509.getExtensionValue("2.5.29.17")));
		if (m.find())
			rut = m.group();
		
		return rut;

	}

}
