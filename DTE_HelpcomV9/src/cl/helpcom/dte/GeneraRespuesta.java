package cl.helpcom.dte;

import jargs.gnu.CmdLineParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlOptions;

import cl.helpcom.dao.ConexionSqlWeb;
import cl.helpcom.itext.EscribirXML;
import cl.helpcom.itext.LeerXML;
import cl.helpcom.mail.Correo;
import cl.nic.dte.VerifyResult;
import cl.sii.siiDte.RespuestaDTEDocument;
import cl.sii.siiDte.consumofolios.ConsumoFoliosDocument.ConsumoFolios.DocumentoConsumoFolios.Caratula.Correlativo;

/**
 * <p>
 * Esta clase se encarga de aplicar la firma digital sobre el archivo XML de
 * entrada que puede ser una respuesta de aceptaciï¿½n comercial del DTE, rechazo
 * comercial o acuse de recibo de mercaderias. Recibe 4 argumentos:
 *
 * -ruta del archivo XML que tiene forma de respuesta<br>
 * -ruta del certificado digital<br>
 * -clave del certificado digital<br>
 * -ruta del archivo en donde se debe escribir el XML firmado<br>
 *
 * @author Jose Urzua <a href="mailto:jose@urzua.cl">jose@urzua.cl</a>
 *
 */
public class GeneraRespuesta {
	private static Logger logger = Logger.getLogger(GeneraRespuesta.class);

	private static void printUsage() {
		logger
				.error("Utilice: java cl.helpcom.dte.GeneraRespuesta "
						+ "-p <respuesta-sinfirma.xml> -c <certDigital.p12> -s <password> -o <respuesta-firmada.xml>");
	}
	/**
	 * @param args
	 * @throws IOException
	 * @throws SQLException
	 * @throws NumberFormatException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws NumberFormatException, SQLException, IOException, ClassNotFoundException {

		CmdLineParser parser = new CmdLineParser();
		CmdLineParser.Option empId = parser.addStringOption('e', "empId");
		CmdLineParser.Option docId = parser.addStringOption('d', "docId");
		CmdLineParser.Option estadoDTE = parser.addStringOption('s', "estadoDTE");
		CmdLineParser.Option estadoGlosa = parser.addStringOption('g', "estadoGlosa");
//		CmdLineParser.Option certOpt = parser.addStringOption('c', "cert");
//		CmdLineParser.Option passOpt = parser.addStringOption('s', "password");
//		CmdLineParser.Option resultOpt = parser.addStringOption('o', "output");
//		CmdLineParser.Option plantillaOpt = parser.addStringOption('p',
//				"plantilla");

		try {
			parser.parse(args);
		} catch (CmdLineParser.OptionException e) {
			printUsage();
			System.exit(2);
		}

//		String certS = (String) parser.getOptionValue(certOpt);
//		String passS = (String) parser.getOptionValue(passOpt);
//		String resultS = (String) parser.getOptionValue(resultOpt);
//		String plantillaS = (String) parser.getOptionValue(plantillaOpt);

		String empIdS = (String) parser.getOptionValue(empId);
		String docIdS = (String) parser.getOptionValue(docId);
		String estadoDTES = (String) parser.getOptionValue(estadoDTE);
		String estadoGlosaS = (String) parser.getOptionValue(estadoGlosa);


//      String empID=propiedades.getProperty("EMPRESA_ID");
        ConexionSqlWeb conexionSqlWeb= new ConexionSqlWeb();
        conexionSqlWeb.obtenerDatos(Integer.valueOf(empIdS));
        
        String rutaXML="/tmp/AcuseAceptacion"+empIdS+docIdS+".xml";
        conexionSqlWeb.obtenerArchivoTercero(docIdS, rutaXML,Integer.valueOf(empIdS));

        LeerXML leerXML = new LeerXML();
        ArrayList<String> arrayReceptor = new ArrayList<>();//Facturador Helpcom
        ArrayList<String> arrayEmisor = new ArrayList<>();//Tercero
        ArrayList<String> arrayIdDoc = new ArrayList<>();
        ArrayList<String> arrayTotales = new ArrayList<>();
        
        //Lee documento XML Intercambio.xml Obtiene valores
        arrayIdDoc= leerXML.getIdDoc(rutaXML);
        arrayReceptor= leerXML.getReceptor(rutaXML);
        arrayEmisor= leerXML.getEmisor(rutaXML);
        arrayTotales= leerXML.getTotales(rutaXML);

        String cod_envio=empIdS+docIdS;//Codigo de Envío
        
        EscribirXML escribirXML = new EscribirXML();
        escribirXML.cambiarDatosRespuesta(cod_envio,"/var/www/html/Centaurus/DTE/"+empIdS+"/Intercambio/entrada-respuesta.xml", arrayReceptor,arrayEmisor, arrayIdDoc, arrayTotales,estadoDTES,estadoGlosaS);

        String certS = "/usr/local/F_E/DTE/"+empIdS+"/Certificado/Certificado.pfx";
		String passS = conexionSqlWeb.getClaveCert();
		String resultS = "/var/www/html/Centaurus/DTE/"+empIdS+"/Intercambio/Aceptacion/respuesta"+empIdS+docIdS+".xml";
		String plantillaS = "/var/www/html/Centaurus/DTE/"+empIdS+"/Intercambio/entrada-respuesta.xml";

		if (certS == null || passS == null || resultS == null || plantillaS == null) {
			printUsage();
			System.exit(2);
		}

		RespuestaDTEDocument respuesta = null;
		logger.debug("iniciando");
		try {
			respuesta = RespuestaDTEDocument.Factory.parse(new FileInputStream(plantillaS));
		} catch (Exception e1) {
			logger.fatal("Error al cargar respuesta de entrada", e1);
			System.out.println("Error al cargar respuesta de entrada");
			System.exit(0);
		}
		Certificado cert = new Certificado();
		CertificadoLlave certLlave = cert.getCertificado(certS, passS);

		try {
			respuesta.sign(certLlave.getPKey(), certLlave.getX509());
		} catch (Exception e) {
			logger.fatal("error al firmar respuesta", e);
			System.out.println("error al firmar respuesta");
			System.exit(0);
		}

		VerifyResult resl = respuesta.verifyXML();
		if (!resl.isOk()) {
			logger.error("Respuesta: Estructura XML Incorrecta: "
					+ resl.getMessage());
			System.out.println("Respuesta: Estructura XML Incorrecta: "+resl.getMessage());
			return;
		}
		logger.debug("Respuesta: Estructura XML OK");

		File archivoEnvio = new File(resultS);
		XmlOptions opts = new XmlOptions();
		opts.setCharacterEncoding("ISO-8859-1");
		try {
			respuesta.save(archivoEnvio, opts);
			conexionSqlWeb.updateCodigoEnvio(docIdS,cod_envio);
			
			

		} catch (IOException e) {
			logger.fatal("Error al escribir archivo salida", e);
			System.out.println("Error al escribir archivo salida");
			System.exit(0);
		}

		logger.debug("Escrita respuesta en archivo salida " + resultS);
//		System.out.println("cod"+cod_envio);
		

		Correo correo= new Correo();
		String mensaje="Aceptacion Comercial: "+cod_envio;
    	String titulo="ACUSE ACEPTACION COMERCIAL";
    	conexionSqlWeb.obtenerDatosEmpIntercambio(arrayEmisor.get(0));
    	
		correo.enviarCorreo(resultS,"AC"+cod_envio+".xml",conexionSqlWeb.getUsername(),conexionSqlWeb.getPassword(),titulo,mensaje,conexionSqlWeb.getUser_name_tercero());
		
		conexionSqlWeb.updateEstadoAcuse(docIdS, "ACEPTACION COMERCIAL OK");
		conexionSqlWeb.cerrarConexion();//cierra conexion Mysql
		System.out.println("ACEPTACION COMERCIAL REALIZADA <br>");
		System.out.println("Documento enviado con éxito a "+arrayEmisor.get(1).toUpperCase()+" RUT: "+arrayEmisor.get(0).toUpperCase()+" Cod. de envio: "+cod_envio+" <br>" );
		
		
	}

}
