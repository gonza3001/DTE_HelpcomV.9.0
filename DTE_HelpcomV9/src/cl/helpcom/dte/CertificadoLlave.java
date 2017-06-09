/**
 * 
 */
package cl.helpcom.dte;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import cl.sii.siiDte.RUTType;

/**
 * Esta clase se encarga de mantener los atributos de un certificado con su
 * clave
 * 
 * @author Jose Urzua <a href="mailto:jose@urzua.cl">jose@urzua.cl</a>
 * 
 */
public class CertificadoLlave {
	private X509Certificate x509 = null;
	private PrivateKey pKey = null;
	private RUTType rut = null;

	public X509Certificate getX509() {
		return x509;
	}

	public void setX509(X509Certificate x509) {
		this.x509 = x509;
	}

	public PrivateKey getPKey() {
		return pKey;
	}

	public void setPKey(PrivateKey key) {
		pKey = key;
	}

	public RUTType getRut() {
		return rut;
	}

	public void setRut(RUTType rut) {
		this.rut = rut;
	}
}
