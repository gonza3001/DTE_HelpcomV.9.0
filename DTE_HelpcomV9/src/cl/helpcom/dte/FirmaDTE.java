/**
 *
 */
package cl.helpcom.dte;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Properties;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;

import cl.helpcom.dao.ConexionSqlFirma;
import cl.helpcom.itext.LeerXML;
import cl.helpcom.recursos.LectorFichero;
import cl.nic.dte.VerifyResult;
import cl.sii.siiDte.DTEDocument;
/**
 * @author Jose Urzua <a href="mailto:jose@urzua.cl">jose@urzua.cl</a>
 * @author Mauricio Rodriguez <a href="mailto:mrodriguez@helpcom.cl"></a>
 *
 */
public class FirmaDTE {
	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException, UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException, KeyStoreException, ParseException, XmlException {

		ConexionSqlFirma sqlFirma = new ConexionSqlFirma();
		sqlFirma.getIDEmpresas();//Firma los documentos por empresa

	}
}