package cl.helpcom.dao;

import jargs.gnu.CmdLineParser;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;

import org.apache.xmlbeans.XmlException;

import cl.helpcom.dte.method.GeneraEnvioATercero;
import cl.helpcom.recursos.LectorFichero;

public class PrincipalGenPaqEmpresa {

	/**
	 * @param args
	 * @throws SQLException
	 * @throws XmlException
	 * @throws ParseException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws KeyStoreException
	 * @throws CertificateException
	 * @throws NoSuchAlgorithmException
	 * @throws UnrecoverableKeyException
	 */
	public static void main(String[] args) throws UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException, KeyStoreException, ClassNotFoundException, IOException, ParseException, XmlException, SQLException {

		CmdLineParser parser = new CmdLineParser();
		CmdLineParser.Option certOpt = parser.addStringOption('e', "emp");
		CmdLineParser.Option passOpt = parser.addStringOption('p', "pass");

		try {
			parser.parse(args);
		} catch (CmdLineParser.OptionException e) {
			System.exit(2);
		}

		String certS = (String) parser.getOptionValue(certOpt);
		String passS = (String) parser.getOptionValue(passOpt);
		System.out.println(certS);
		System.out.println(passS);

		ConexionSqlEnvioTercero sql = new ConexionSqlEnvioTercero();
		//Colocar ID Empresa y Clave Certificado
		sql.getXMLS_Recepcionados(Integer.valueOf(certS), passS);

	}

}
