/**
 *
 */
package cl.helpcom.dte;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlDateTime;
import org.apache.xmlbeans.XmlOptions;
import cl.helpcom.dao.ConexionSqlWeb;
import cl.nic.dte.VerifyResult;
import cl.nic.dte.util.Utilities;
import cl.nic.dte.util.XMLUtil;
import cl.sii.siiDte.DTEDefType;
import cl.sii.siiDte.EnvioDTEDocument;
import cl.sii.siiDte.RespuestaDTEDocument;
import cl.sii.siiDte.RespuestaDTEDocument.RespuestaDTE;
import cl.sii.siiDte.RespuestaDTEDocument.RespuestaDTE.Resultado;
import cl.sii.siiDte.RespuestaDTEDocument.RespuestaDTE.Resultado.Caratula;
import cl.sii.siiDte.RespuestaDTEDocument.RespuestaDTE.Resultado.RecepcionEnvio;
import cl.sii.siiDte.RespuestaDTEDocument.RespuestaDTE.Resultado.RecepcionEnvio.RecepcionDTE;
import cl.sii.siiDte.RespuestaDTEDocument.RespuestaDTE.Resultado.ResultadoDTE;
/**
 * Esta clase se encarga de realizar la recepcion de DTEs a partir de un archivo
 * XML que llego de acuerdo al protocolo de intercambio entre contribuyentes.
 *
 * -PATH del archivo que contiene el envio recepcionado<br>
 * -PATH del directorio en donde quedaran los DTEs recibidos con "/" o "\\"
 * final<br>
 * -PATH del certificado<br>
 * -Password del certificado<br>
 * -Identificador de respuesta de envio (numero)<br>
 * -Validar en sii<br
 * -PATH del archivo en donde quedara el acuse de recibo en formato XML<br>
 *
 * @author Jose Urzua <a href="mailto:jose@urzua.cl">jose@urzua.cl</a>
 *
 */
public class RecepcionarOr {
	private static Logger logger = Logger.getLogger(Recepcionar.class);

	private static void printUsage() {
		logger.error("Utilice: java cl.helpcom.dte.Recepcionar "
				+ "-p <archivo-recibido.xml> -d <DIR salida DTEs> -c <certDigital.p12> "
				+ "-s <password> -i <ID respuestaEnvio> "
				+ "-v <S o N (validar SII)> -o <acuse-recibo-firmado.xml>");
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {

		/*CmdLineParser parser = new CmdLineParser();
		CmdLineParser.Option certOpt = parser.addStringOption('c', "cert");
		CmdLineParser.Option passOpt = parser.addStringOption('s', "password");
		CmdLineParser.Option resultOpt = parser.addStringOption('o', "output");
		CmdLineParser.Option plantillaOpt = parser.addStringOption('p',
				"plantilla");
		CmdLineParser.Option dirOpt = parser.addStringOption('d', "dir");
		CmdLineParser.Option idOpt = parser.addStringOption('i', "idrespuesta");
		CmdLineParser.Option validarOpt = parser
				.addStringOption('v', "validar");

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
		String dirS = (String) parser.getOptionValue(dirOpt);
		String idS = (String) parser.getOptionValue(idOpt);
		String validarS = (String) parser.getOptionValue(validarOpt);

		if (certS == null || passS == null || resultS == null
				|| plantillaS == null || dirS == null || idS == null
				|| validarS == null) {
			printUsage();
			System.exit(2);
		}
		*/
		Properties propiedades = new Properties();
	    InputStream entrada = null;
		entrada = new FileInputStream("/usr/local/F_E/Configuraciones/Propiedades/configuracionCreaDTE.properties");
        // cargamos el archivo de propiedades
        propiedades.load(entrada);

        String empID=propiedades.getProperty("EMPRESA_ID");
        String intercambioID=propiedades.getProperty("INTERCAMBIO_ID");

        System.out.println("EMP: "+empID);
        ConexionSqlWeb conexionSqlWeb= new ConexionSqlWeb();
        conexionSqlWeb.obtenerDatos(Integer.valueOf(empID));

		String certS = "/usr/local/F_E/DTE/"+empID+"/Certificado/Certificado.pfx";
		String passS = conexionSqlWeb.getClaveCert();
		String resultS = "/usr/local/F_E/DTE/"+empID+"/DocumentoDTE/Intercambio/RespuestaIntercambio/acuse-recibo-firmado.xml";
		String plantillaS = "/usr/local/F_E/DTE/"+empID+"/DocumentoDTE/Intercambio/CONF/SII/Intercambio.xml";
		String dirS = "/usr/local/F_E/DTE/"+empID+"/DocumentoDTE/Intercambio/DTEs/";
		String idS = "801961";
		String validarS = "N";

		System.out.println(certS+passS+resultS+plantillaS+dirS+idS);

//		String certS = "/usr/local/F_E/DTE/3/Certificado/Certificado.pfx";
//		String passS = "1189";
//		String resultS = "/home/mau/Documentos/DTE/Salus/documentosDTE/intercambio/RespuestaIntercambio/acuse-recibo-firmado.xml";
//		String plantillaS = "/home/mau/Documentos/DTE/Salus/documentosDTE/intercambio/CONF/SII/ENVIO_DTE_584215.xml";
//		String dirS = "/home/mau/Documentos/DTE/Salus/documentosDTE/intercambio/DTEs/";
//		String idS = propiedades.getProperty("INTERCAMBIO_ID");
//		String validarS = "N";

		ResourceBundle config = ResourceBundle.getBundle("cl.helpcom.dte.helpcom", Locale.ENGLISH);
		logger.debug("iniciando");
		EnvioDTEDocument envio = null;
		
		try {
			envio = EnvioDTEDocument.Factory.parse(new FileInputStream(
					plantillaS));

		} catch (Exception e1) {
			logger.fatal("Error al cargar archivo de envio recepcionado: "
					+ plantillaS, e1);
			System.exit(0);
		}

		// verificar firma y esquema
		VerifyResult resl = envio.verifyXML();
		boolean envioEsquemaOK = true;
		boolean envioFirmaOK = true;
		String errorEsquema = "";
		String errorFirma = "";
		if (!resl.isOk()) {
			logger.error("EnvioRecibido: Estructura XML Incorrecta: "
					+ resl.getMessage());
			errorEsquema = resl.getMessage();
			// flag responder rechazando envio: error esquema XML
			envioEsquemaOK = false;
		} else {
			logger.debug("EnvioRecibido: Estructura XML OK");

		}

		resl = envio.verifySignature();
		if (!resl.isOk()) {
			logger.error("EnvioRecibido: Firma XML Incorrecta: "
					+ resl.getMessage());
			System.out.println("Estructura 1");
			// flag responder rechazando envio: error firma XML
//			envioFirmaOK = false;
//			errorFirma = resl.getMessage();
		} else {
			logger.debug("EnvioRecibido: Firma XML OK");
			System.out.println("Firma OK");
		}

		boolean envioRutOK = true;
		// verificacion que RUT de receptor en caratula sea mi empresa
		String rutContribuyente = config.getString("RUT_COMPANIA");
		if (!rutContribuyente.equals(envio.getEnvioDTE().getSetDTE()
				.getCaratula().getRutReceptor())) {
			logger.error("Error, caratula de EnvioDTE recibido dice que rut de receptor es: "
					+ envio.getEnvioDTE().getSetDTE().getCaratula()
							.getRutReceptor()
					+ " el cual es distinto a nuestra empresa: "
					+ rutContribuyente);
			System.out.println("Error, caratula de EnvioDTE recibido dice que rut de receptor es: "
					+ envio.getEnvioDTE().getSetDTE().getCaratula()
					.getRutReceptor()
			+ " el cual es distinto a nuestra empresa: "
			+ rutContribuyente);
			envioRutOK = false;
		}

		Certificado cert = new Certificado();
		CertificadoLlave certLlave = cert.getCertificado(certS, passS);

		ComunicacionSII comSII = new ComunicacionSII();
		String token = null;

		ArrayList<RecepcionDTE> arrRecepcionDTE = new ArrayList<RecepcionDTE>();

		ArrayList<ResultadoDTE> resultados = new ArrayList<ResultadoDTE>();
		RecepcionEnvio rre = RecepcionEnvio.Factory.newInstance();

		File f = new File(plantillaS);
		rre.setNmbEnvio(f.getName());
		rre.xsetFchRecep(XmlDateTime.Factory
                .newValue(Utilities.fechaHoraFormat
                        .format(new Date()))); //XmlDateTime.Factory.newInstance());// setFchRecep(cal);
		rre.setCodEnvio(Long.parseLong(idS));
		rre.setEnvioDTEID(envio.getEnvioDTE().getSetDTE().getID());
		rre.setEstadoRecepEnv(0);
		rre.setRecepEnvGlosa("Envio Recibido Conforme.");

		if (envioFirmaOK && envioEsquemaOK && envioRutOK) {

			X509Certificate x509 = XMLUtil.getCertificate(envio.getEnvioDTE()
					.getSignature());
			logger.debug("Firmado por: "
					+ x509.getSubjectX500Principal().getName());
			for (DTEDefType dte : envio.getEnvioDTE().getSetDTE().getDTEArray()) {
				// resl = dte.verifySignature();
				x509 = XMLUtil.getCertificate(dte.getSignature());
				logger.debug("DTE ID " + dte.getDocumento().getID()
						+ " Firmado por: "
						+ x509.getSubjectX500Principal().getName());

				logger.debug("Por almacenar DTE en " + dirS);
				String nombreDTE = dirS
						+ "dte-"
						+ dte.getDocumento().getEncabezado().getEmisor()
								.getRUTEmisor()
						+ "-"
						+ dte.getDocumento().getEncabezado().getIdDoc()
								.getTipoDTE().toString()
						+ "-"
						+ dte.getDocumento().getEncabezado().getIdDoc()
								.getFolio() + ".xml";
				try {
					FileOutputStream fout = new FileOutputStream(nombreDTE);
					fout.write(dte.getBytes());
					fout.flush();
					fout.close();
				} catch (FileNotFoundException e) {
					logger.error("Error al guardar DTE en path " + nombreDTE, e);
					System.out.println("Estructura 3");
					System.exit(0);
				} catch (IOException e) {
					logger.error("Error al escribir DTE en path " + nombreDTE,
							e);
					System.out.println("Estructura 4");
					System.exit(0);
				}
				logger.debug("grabado DTE recibido en PATH: " + nombreDTE);

				boolean firmaOKDTE = true;
				if (!resl.isOk()) {
					logger.error("Validando DTE ID "
							+ dte.getDocumento().getID()
							+ " : Firma XML Incorrecta: " + resl.getMessage());
//					firmaOKDTE = false;
				} else {
					logger.debug("Validando DTE ID "
							+ dte.getDocumento().getID() + " : Firma XML OK");
				}
				boolean rutDTEOK = true;

				if (!rutContribuyente.equals(dte.getDocumento().getEncabezado()
						.getReceptor().getRUTRecep())) {
					logger.debug("Error, DTE id: "
							+ dte.getDocumento().getID()
							+ " folio: "
							+ dte.getDocumento().getEncabezado().getIdDoc()
									.getFolio()
							+ " tipo: "
							+ dte.getDocumento().getEncabezado().getIdDoc()
									.getTipoDTE().toString()
							+ " contiene RUT de receptor ["
							+ dte.getDocumento().getEncabezado().getReceptor()
									.getRUTRecep()
							+ "] que no corresponde a nuestra empresa ["
							+ rutContribuyente + "]");

					rutDTEOK = false;
				}
				// agregar RecepcionDTE
				RecepcionDTE rDTE = RecepcionDTE.Factory.newInstance();
				rDTE.setFolio(dte.getDocumento().getEncabezado().getIdDoc()
						.getFolio());
				rDTE.setTipoDTE(dte.getDocumento().getEncabezado().getIdDoc()
						.getTipoDTE());
				rDTE.setFchEmis(dte.getDocumento().getEncabezado().getIdDoc()
						.getFchEmis());
				rDTE.setRUTEmisor(dte.getDocumento().getEncabezado()
						.getEmisor().getRUTEmisor());
				rDTE.setRUTRecep(dte.getDocumento().getEncabezado()
						.getReceptor().getRUTRecep());
				rDTE.setMntTotal(dte.getDocumento().getEncabezado()
						.getTotales().getMntTotal());

				ResultadoDTE resDTE = ResultadoDTE.Factory.newInstance();
				resDTE.setFolio(dte.getDocumento().getEncabezado().getIdDoc()
						.getFolio());
				resDTE.setTipoDTE(dte.getDocumento().getEncabezado().getIdDoc()
						.getTipoDTE());
				resDTE.setFchEmis(dte.getDocumento().getEncabezado().getIdDoc()
						.getFchEmis());
				resDTE.setRUTEmisor(dte.getDocumento().getEncabezado()
						.getEmisor().getRUTEmisor());
				resDTE.setRUTRecep(dte.getDocumento().getEncabezado()
						.getReceptor().getRUTRecep());
				resDTE.setMntTotal(dte.getDocumento().getEncabezado()
						.getTotales().getMntTotal());
				// se asocia el ID del envio recepcionado en nuestra BD
				resDTE.setCodEnvio(new Long(idS));

				if (firmaOKDTE && rutDTEOK) {
					EstadoDTESII estadoDTE = null;
					// ver si en la configuracion se pide validar en SII
					if ("S".equals(validarS)) {
						int nroReintentos = 1;
						boolean respuestaRecibida = false;
						// consultar estado DTE en SII, se pregunta hasta
						// recibir la respuesta o hasta que se cumpla el numero
						// de reintentos (se reintenta solo si hay problemas con
						// el token)

						if (token == null) {
							logger.debug("iniciando autentificacion en SII");
							try {
								token = comSII
										.identificaContribuyente(certLlave);
							} catch (Exception e1) {
								logger.error("Imposible autentificarse con el SII, no podre validar DTEs recibidos, termino!.");
								System.exit(0);
							}
						}

						do {
							try {
								estadoDTE = comSII.consultaEstadoDTE(
										getRutCertificado(certLlave.getX509()),
										dte.getDocumento().getEncabezado()
												.getEmisor().getRUTEmisor(),
										dte.getDocumento().getEncabezado()
												.getReceptor().getRUTRecep(),
										dte.getDocumento().getEncabezado()
												.getIdDoc().getTipoDTE()
												.intValue(), dte.getDocumento()
												.getEncabezado().getIdDoc()
												.getFolio(), dte.getDocumento()
												.getEncabezado().getIdDoc()
												.getFchEmis(), dte
												.getDocumento().getEncabezado()
												.getTotales().getMntTotal(),
										token);
							} catch (Exception e) {
								logger.error("Imposible consultar por el DTE en el SII, no podre validar DTEs recibidos, termino!.");
								System.exit(0);
							}
							// revisar respuesta para ver si: se debe reobtener
							// token, o generar respuesta de acuerdo a
							// estado_err
							if ("001".equals(estadoDTE.getEstado())
									|| "002".equals(estadoDTE.getEstado())
									|| "003".equals(estadoDTE.getEstado())) {
								// problemas con el token
								try {
									token = comSII
											.identificaContribuyente(certLlave);
								} catch (Exception e) {
									logger.error("Imposible autentificarse con el SII, no podre validar DTEs recibidos, termino!.");
									System.exit(0);
								}
								nroReintentos++;
								logger.debug("Problemas con el token, intentando por "
										+ nroReintentos + " vez");

							} else {
								respuestaRecibida = true;
								logger.debug("Respuesta recibida desde el SII por estado del DTE");
							}
						} while (!respuestaRecibida || nroReintentos > 3);

						// termina la ejecucion en caso de error
						if (estadoDTE == null || nroReintentos > 3) {
							logger.error("Imposible autentificarse con el SII, no podre validar DTEs recibidos, termino!.");
							System.exit(0);
						}

					} else {
						// suponemos aceptado el DTE dado que no se quiso
						// consultar en el SII
						estadoDTE = new EstadoDTESII();
						estadoDTE.setEstado("DOK");
					}

					// el SII respondio, analizar si hay error
					// utilizar codigos de formato de intercambio
					if (!"DOK".equals(estadoDTE.getEstado())) {
						resDTE.setEstadoDTE(new Integer(2));
						resDTE.setEstadoDTEGlosa("DTE rechazado. Estado en SII: "
								+ estadoDTE.getEstado()
								+ ". Glosa en SII: "
								+ estadoDTE.getGlosa());

						rDTE.setEstadoRecepDTE(new Integer(99));
						rDTE.setRecepDTEGlosa("DTE rechazado. Estado en SII: "
								+ estadoDTE.getEstado()
								+ ". Glosa en SII: "
								+ estadoDTE.getGlosa());
					} else if (!rutContribuyente.equals(resDTE.getRUTRecep())) {
						resDTE.setEstadoDTE(new Integer(2));
						resDTE.setEstadoDTEGlosa("DTE rechazado - Error en RUT Receptor");

						rDTE.setEstadoRecepDTE(new Integer(3));
						rDTE.setRecepDTEGlosa("DTE No Recibido - Error en RUT Receptor");

					} else {
						// DTE validado
						resDTE.setEstadoDTE(new Integer(0));
						resDTE.setEstadoDTEGlosa("DTE Aceptado OK");

						rDTE.setEstadoRecepDTE(new Integer(0));
						rDTE.setRecepDTEGlosa("DTE Recibido OK");
					}
				} else {
					if (!firmaOKDTE) {
						resDTE.setEstadoDTE(new Integer(2));
						resDTE.setEstadoDTEGlosa("DTE rechazado - Error de Firma");

						rDTE.setEstadoRecepDTE(new Integer(1));
						rDTE.setRecepDTEGlosa("DTE No Recibido - Error de Firma");
					} else if (!rutDTEOK) {
						resDTE.setEstadoDTE(new Integer(2));
						resDTE.setEstadoDTEGlosa("DTE rechazado - Error en RUT Receptor");

						rDTE.setEstadoRecepDTE(new Integer(3));
						rDTE.setRecepDTEGlosa("DTE No Recibido - Error en RUT Receptor");
					}
				}
				resultados.add(resDTE);
				arrRecepcionDTE.add(rDTE);
			}
		} else {
			logger.debug("Envio no cumple con la firma o con esquema XML");
			// generar respuesta de envio malfirmado o mal esquema
//			// Calendar cal = Calendar.getInstance();
//			File f = new File(plantillaS);
//			rre.setNmbEnvio(f.getName());
//			rre.xsetFchRecep(XmlDateTime.Factory.newInstance());// setFchRecep(cal);
//			rre.setCodEnvio(Long.parseLong(idS));
//			rre.setEnvioDTEID(envio.getEnvioDTE().getSetDTE().getID());

			// revisar si es problema de esquema o de firma
			if (!envioEsquemaOK) {
				// error de esquema
				rre.setEstadoRecepEnv(1);
				rre.setRecepEnvGlosa("Envio Rechazado - Error de Schema: "
						+ errorEsquema);
			} else if (!envioFirmaOK) {
				rre.setEstadoRecepEnv(2);
				rre.setRecepEnvGlosa("Envio Rechazado - Error de Firma: "
						+ errorFirma);
			} else if (!envioRutOK) {
				rre.setEstadoRecepEnv(3);
				rre.setRecepEnvGlosa("Envio Rechazado - RUT Receptor No Corresponde");
			}
			logger.debug("Glosa respuesta Envio: " + rre.getRecepEnvGlosa());
		}

		// armar respuesta

		Caratula caratula = Caratula.Factory.newInstance();
		Resultado resultado = Resultado.Factory.newInstance();


		if (arrRecepcionDTE.size() > 0) {
			RecepcionDTE[] resultadoDTE = new RecepcionDTE[arrRecepcionDTE.size()];
			for (int i = 0; i < arrRecepcionDTE.size(); i++)
				resultadoDTE[i] = arrRecepcionDTE.get(i);
			rre.setRecepcionDTEArray(resultadoDTE);
			caratula.setNroDetalles(arrRecepcionDTE.size());
		} else {
			if (resultados.size() > 0) {
				ResultadoDTE[] resultadoDTE = new ResultadoDTE[resultados.size()];
				for (int i = 0; i < resultados.size(); i++)
					resultadoDTE[i] = resultados.get(i);
				resultado.setResultadoDTEArray(resultadoDTE);
				caratula.setNroDetalles(resultados.size());
			} else {
				caratula.setNroDetalles(1);
			}
		}

		caratula.setRutResponde(rutContribuyente);
		caratula.setRutRecibe(envio.getEnvioDTE().getSetDTE().getCaratula()
				.getRutEmisor());
		caratula.setIdRespuesta(new Long(idS));
		caratula.setVersion(new BigDecimal("1.0"));

		// nombre, fono, email de contacto para aclarar
		// dudas, se leen desde las properties del emisor
		caratula.setNmbContacto(config.getString("NOMBRE_CONTACTO_RESPUESTA"));
		caratula.setMailContacto(config.getString("EMAIL_CONTACTO_RESPUESTA"));
		caratula.setFonoContacto(config.getString("FONO_CONTACTO_RESPUESTA"));

		resultado.setCaratula(caratula);
		resultado.setID("RESP-" + idS);

//		if (!envioFirmaOK || !envioEsquemaOK || !envioRutOK) {
		if (!rre.isNil()){
			logger.debug("Se responde por el envio");
			RecepcionEnvio[] reArray = new RecepcionEnvio[1];
			reArray[0] = rre;
			resultado.setRecepcionEnvioArray(reArray);
		}

		RespuestaDTE respDTE = RespuestaDTE.Factory.newInstance();
		respDTE.setResultado(resultado);
		respDTE.setVersion(new BigDecimal("1.0"));

		RespuestaDTEDocument respuesta = RespuestaDTEDocument.Factory
				.newInstance();
		respuesta.setRespuestaDTE(respDTE);

		HashMap<String, String> namespaces = new HashMap<String, String>();
		namespaces.put("http://www.sii.cl/SiiDte", "");

		XmlOptions opts = new XmlOptions();
		opts.setSavePrettyPrint();
		opts.setSavePrettyPrintIndent(0);
		opts.setSaveSuggestedPrefixes(namespaces);
		opts.setCharacterEncoding("ISO-8859-1");

		XmlCursor cursor = respuesta.newCursor();
		if (cursor.toFirstChild()) {
			cursor.setAttributeText(new QName(
					"http://www.w3.org/2001/XMLSchema-instance",
					"schemaLocation"),
					"http://www.sii.cl/SiiDte RespuestaEnvioDTE_v10.xsd");
		}
		try {
			respuesta = RespuestaDTEDocument.Factory.parse(respuesta
					.newInputStream(opts));
		} catch (Exception e) {
			logger.error(
					"Error al obtener respuesta con formato antes de firmar", e);
		}

		try {
			logger.debug("Respuesta antes de firmar: "
					+ new String(respuesta.getBytes()));
			respuesta.sign(certLlave.getPKey(), certLlave.getX509());
		} catch (Exception e) {
			logger.fatal("Error al firmar respuesta", e);
			System.exit(0);
		}

		VerifyResult respuestal = respuesta.verifyXML();
		if (!respuestal.isOk()) {
			logger.error("Respuesta intercambio: Estructura XML Incorrecta: "
					+ respuestal.getMessage());
			return;
		}
		logger.debug("XML respuesta intercambio valido");


		// escribir respuesta
		try {
			FileOutputStream fout = new FileOutputStream(resultS);
			fout.write(respuesta.getBytes());
			fout.flush();
			fout.close();
			logger.debug("Escrita respuesta en " + resultS);
		} catch (FileNotFoundException e1) {
			logger.fatal("Error al crear archivo respuesta " + resultS);
			System.exit(0);
		} catch (IOException e) {
			logger.fatal("Error al escribir respuesta en " + resultS);
			System.exit(0);
		}
		logger.debug("OK validacion de archivo recepcionado.");
	}

	/**
	 * Retorna el rut del titular de un certificado digital.
	 *
	 * @param X509Certificate
	 * @return rut del titular del certificado
	 * @return null en caso que no exista
	 */
	private static String getRutCertificado(X509Certificate rst) {
		String rut = null;
		try {
			String eval = new String(rst.getExtensionValue("2.5.29.17"));
			Pattern p = Pattern.compile("[\\d]{6,8}-[\\dkK]");
			Matcher m = p.matcher(eval);
			if (m.find()) {
				rut = m.group();
				return rut;
			}
			return rut;
		} catch (Exception e) {
			return null;
		}
	}
}
