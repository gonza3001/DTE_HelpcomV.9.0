/**
 *
 */
package cl.helpcom.dte;

import jargs.gnu.CmdLineParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlOptions;

import cl.helpcom.dao.ConexionSqlWeb;
import cl.helpcom.itext.EscribirXML;
import cl.helpcom.itext.LeerXML;
import cl.nic.dte.VerifyResult;
import cl.sii.siiDte.EnvioRecibosDocument;
import cl.sii.siiDte.ReciboDefType;
import cl.sii.siiDte.ReciboDocument;

/**
 * <p>
 * Esta clase se encarga de aplicar la firma digital sobre el archivo XML de
 * entrada que es un recibo de mercaderias. Recibe 4 argumentos:
 *
 * -ruta del archivo XML que tiene forma de recibo de mercaderias<br>
 * -ruta del certificado digital<br>
 * -clave del certificado digital<br>
 * -ruta del archivo en donde se debe escribir el XML firmado<br>
 *
 * @author Jose Urzua <a href="mailto:jose@urzua.cl">jose@urzua.cl</a>
 *
 */
public class GeneraReciboMercaderia {
	private static Logger logger = Logger
			.getLogger(GeneraReciboMercaderia.class);
	private static void printUsage() {
		logger
				.error("Utilice: java cl.helpcom.dte.GeneraReciboMercaderia "
						+ "-p <respuesta-sinfirma.xml> -c <certDigital.p12> -s <password> -o <respuesta-firmada.xml>");
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws SQLException
	 * @throws NumberFormatException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws IOException, NumberFormatException, SQLException, ClassNotFoundException {
		/*CmdLineParser parser = new CmdLineParser();
		CmdLineParser.Option certOpt = parser.addStringOption('c', "cert");
		CmdLineParser.Option passOpt = parser.addStringOption('s', "password");
		CmdLineParser.Option resultOpt = parser.addStringOption('o', "output");
		CmdLineParser.Option plantillaOpt = parser.addStringOption('p',
				"plantilla");

		try {
			parser.parse(args);
		} catch (CmdLineParser.OptionException e) {
			printUsage();
			System.exit(2);
		}

		String certS = (String) parser.getOptionValue(certOpt);
		String passS = (String) parser.getOptionValue(passOpt);
		String resultS = (String) parser.getOptionValue(resultOpt);
		String plantillaS = (String) parser.getOptionValue(plantillaOpt);

		if (certS == null || passS == null || resultS == null || plantillaS == null) {
			printUsage();
			System.exit(2);
		}*/
		Properties propiedades = new Properties();
	    InputStream entrada = null;
		entrada = new FileInputStream("/usr/local/F_E/Configuraciones/Propiedades/configuracionCreaDTE.properties");
        // cargamos el archivo de propiedades
        propiedades.load(entrada);

        String empID="30";

        ConexionSqlWeb conexionSqlWeb= new ConexionSqlWeb();

        conexionSqlWeb.obtenerDatos(Integer.valueOf(empID));

        LeerXML leerXML = new LeerXML();
        ArrayList<String> arrayReceptor = new ArrayList<>();
        ArrayList<String> arrayIdDoc = new ArrayList<>();
        ArrayList<String> arrayTotales = new ArrayList<>();
        //ArrayList<ArrayList<String>> arrayDetalle = new ArrayList<>();

        //Lee documento XML Intercambio.xml Obtiene valores
        arrayReceptor= leerXML.getReceptor("/usr/local/F_E/DTE/"+empID+"/DocumentoDTE/Intercambio/CONF/SII/Intercambio.xml");
        arrayIdDoc= leerXML.getIdDoc("/usr/local/F_E/DTE/"+empID+"/DocumentoDTE/Intercambio/CONF/SII/Intercambio.xml");
        arrayTotales= leerXML.getTotales("/usr/local/F_E/DTE/"+empID+"/DocumentoDTE/Intercambio/CONF/SII/Intercambio.xml");

        //Cambia datos de la plantilla
        EscribirXML escribirXML = new EscribirXML();
        escribirXML.cambiarMercaderia("/usr/local/F_E/DTE/"+empID+"/DocumentoDTE/Intercambio/CONF/entrada-recibo-mercaderias.xml",arrayReceptor,arrayIdDoc,arrayTotales);

        String certS = "/usr/local/F_E/DTE/"+empID+"/Certificado/Certificado.pfx";
		String passS = conexionSqlWeb.getClaveCert();
		String resultS = "/usr/local/F_E/DTE/"+empID+"/DocumentoDTE/Intercambio/ReciboMercaderia/Respuestamercaderias.xml";
		String plantillaS = "/usr/local/F_E/DTE/"+empID+"/DocumentoDTE/Intercambio/CONF/entrada-recibo-mercaderias.xml";

		HashMap<String, String> namespaces = new HashMap<String, String>();
		namespaces.put("", "http://www.sii.cl/SiiDte");
		XmlOptions opts = new XmlOptions();
		opts.setLoadSubstituteNamespaces(namespaces);

		EnvioRecibosDocument recibo = null;
		try {
			recibo = EnvioRecibosDocument.Factory.parse(new FileInputStream(plantillaS),
					opts);
		} catch (Exception e1) {
			logger
					.fatal("Error al cargar envio recibo de mercaderias de entrada",e1);
			System.exit(0);
		}

		Certificado cert = new Certificado();
		CertificadoLlave certLlave = cert.getCertificado(certS, passS);

		// firmar recibo
		try {
			ReciboDefType[] recibos = recibo.getEnvioRecibos().getSetRecibos().getReciboArray();
			ReciboDefType[] recibosFirmados = new ReciboDefType[recibos.length];
			int i=0;
			for (ReciboDefType r: recibos){
				logger.debug("Firmando ID recibo: "
						+ r.getDocumentoRecibo().getID());
				ReciboDocument recDoc = ReciboDocument.Factory.newInstance();
				recDoc.setRecibo(r);

				namespaces.put("", "http://www.sii.cl/SiiDte");
				opts.setSaveImplicitNamespaces(namespaces);
				opts.setLoadSubstituteNamespaces(namespaces);
				opts.setSavePrettyPrint();
				opts.setSavePrettyPrintIndent(0);

				try {
					recDoc = ReciboDocument.Factory.parse(recDoc
							.newInputStream(opts), opts);
				} catch (Exception e) {
					logger.error(
							"Error al obtener recibo con formato antes de firmar",
							e);
				}
				recDoc.getRecibo().sign(certLlave.getPKey(), certLlave.getX509());
				recibosFirmados[i++] = recDoc.getRecibo();
			}

			recibo.getEnvioRecibos().getSetRecibos().setReciboArray(recibosFirmados);
			// firmar envio recibo
			recibo.sign(certLlave.getPKey(), certLlave.getX509());
		} catch (Exception e) {
			logger.fatal("error al firmar recibo de mercaderias", e);
			System.out.println("error al firmar recibo de mercaderias");
			System.exit(0);
		}

		VerifyResult resl = recibo.verifyXML();
		if (!resl.isOk()) {
			System.out.println("Recibo de mercaderias: Estructura XML Incorrecta: ");
			logger.error("Recibo de mercaderias: Estructura XML Incorrecta: "
					+ resl.getMessage());
			return;
		}
		logger.debug("Recibo de mercaderias: Estructura XML OK");

		File archivoEnvio = new File(resultS);
		opts = new XmlOptions();
		opts.setCharacterEncoding("ISO-8859-1");
		try {
			recibo.save(archivoEnvio, opts);
		} catch (IOException e) {
			logger.fatal("Error al escribir archivo salida", e);
			System.exit(0);
		}
		logger.debug("Escrito recibo de mercaderias en archivo salida "+ resultS);
	}
}