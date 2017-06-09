/**
 * 
 */
package cl.helpcom.dte;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.codehaus.xfire.client.Client;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;



/**
 * Esta clase se encarga de realizar el proceso de autentificacion en el SII
 * 
 * @author Jose Urzua <a href="mailto:jose@urzua.cl">jose@urzua.cl</a>
 * 
 */
public class ComunicacionSII {
	private static Logger logger = Logger.getLogger(ComunicacionSII.class);

	/**
	 * Identifica de forma automática en el SII un contribuyente
	 * 
	 * @return token obtenido en la autentificacion
	 * @throws LogicaException
	 */

	@SuppressWarnings("unchecked")
	public String identificaContribuyente(CertificadoLlave certLlave)
			throws Exception {
		String token = null;
		Semilla semilla = new Semilla();

		ResourceBundle config = ResourceBundle.getBundle(
				"cl.carrasco.dte.carrasco", Locale.ENGLISH);
		Client cl;
		try {
			cl = new Client(new URL(config.getString("URL_SOLICITUD_SEMILLA")));

			Object[] results = cl.invoke("getSeed", new Object[] {});
			String xmlSemilla = (String) results[0];
			logger.debug("XML Semilla: " + xmlSemilla);

			SAXBuilder sax = new SAXBuilder();
			sax.setValidation(false);
			Document todoData = sax.build(new ByteArrayInputStream(xmlSemilla
					.getBytes()));

			// obtener semilla para hacer firma
			Element todoElem = todoData.getRootElement();
			List<Element> lista = todoElem.getChildren();
			for (int j = 0; j < lista.size(); j++) {
				Element elem = lista.get(j);
				if (elem.getName().equals("RESP_HDR")) {
					Element estado = elem.getChild("ESTADO");
					logger.debug("Estado Semilla: " + estado.getText());
				}
				if (elem.getName().equals("RESP_BODY")) {
					Element sem = elem.getChild("SEMILLA");
					semilla.setSemilla(sem.getText());
					logger.debug("Valor Semilla: " + sem.getText());
				}
			}

			semilla.sign(certLlave.getPKey(), certLlave.getX509(), null);
			logger.debug("Semilla firmada: " + semilla.getSignedSeed());

			// obtener Token

			cl = new Client(new URL(config
					.getString("URL_SOLICITUD_TOKEN")));
			results = cl.invoke("getToken", new Object[] { semilla
					.getSignedSeed() });
			logger.debug("token[0]: " + results[0]);

			sax = new SAXBuilder();
			sax.setValidation(false);
			todoData = sax.build(new ByteArrayInputStream(((String) results[0])
					.getBytes()));

			// obtener texto de token
			todoElem = todoData.getRootElement();
			lista = todoElem.getChildren();
			for (int j = 0; j < lista.size(); j++) {
				Element elem = lista.get(j);
				if (elem.getName().equals("RESP_HDR")) {
					Element estado = elem.getChild("ESTADO");
					logger.debug("Estado token: " + estado.getText());
					estado = elem.getChild("GLOSA");
					logger.debug("Glosa token: " + estado.getText());
				}
				if (elem.getName().equals("RESP_BODY")) {
					Element sem = elem.getChild("TOKEN");
					token = sem.getText();
					logger.debug("Valor token: " + sem.getText());
				}
			}
			return token;
		} catch (Exception e) {
			logger.error("Falló autentificacion automatica en SII: " + e);
			throw new Exception("Falló autentificacion automatica en SII: " + e);
		}
	}

	/**
	 * Consulta en el webservice del SII el estado de un DTE
	 * 
	 * @param rutConsultante
	 * @param rutEmisor
	 * @param rutReceptor
	 * @param tipoDTE
	 * @param folioDTE
	 * @param fechaEmision
	 * @param montoTotal
	 * @param token
	 * @return
	 */
	@SuppressWarnings("unchecked") 
	public EstadoDTESII consultaEstadoDTE(String rutConsultante,
			String rutEmisor, String rutReceptor, Integer tipoDTE,
			long folioDTE, Calendar fechaEmision, long montoTotal, String token) {
		EstadoDTESII estadoDte = new EstadoDTESII();

		Client cl;
		ResourceBundle config = ResourceBundle.getBundle(
				"cl.carrasco.dte.carrasco", Locale.ENGLISH);
		try {
			cl = new Client(new URL(config
					.getString("URL_CONSULTA_ESTADO_DTE")));

			Object[] results = cl.invoke("getEstDte", new Object[] {
					rutConsultante.substring(0,
							rutConsultante.length() - 2),
					rutConsultante.substring(
							rutConsultante.length() - 1,
							rutConsultante.length()),
					rutEmisor.substring(0,
							rutEmisor.length() - 2),
					rutEmisor.substring(
							rutEmisor.length() - 1,
							rutEmisor.length()),
					rutReceptor.substring(0,
							rutReceptor.length() - 2),
					rutReceptor.substring(
							rutReceptor.length() - 1,
							rutReceptor.length()), tipoDTE,
					folioDTE, fechaEmision, montoTotal, token });
			logger.debug("estadoDTE[0]: " + results[0]);

			SAXBuilder sax = new SAXBuilder();
			sax.setValidation(false);
			Document todoData = sax.build(new ByteArrayInputStream(
					((String) results[0]).getBytes()));
			Element todoElem = todoData.getRootElement();
			List<Element> lista = todoElem.getChildren();
			for (int j = 0; j < lista.size(); j++) {
				Element elem = lista.get(j);
				if (elem.getName().equals("RESP_HDR")) {
					Element estado = elem.getChild("ESTADO");
					logger.debug("Estado DTE: " + estado.getText());

					estadoDte.setEstado(estado.getText());
					estadoDte.setGlosa(elem.getChildText("GLOSA"));
					estadoDte.setErrCode(elem.getChildText("ERR_CODE"));
					estadoDte.setErrGlosa(elem.getChildText("GLOSA_ERR"));
					estadoDte.setNumeroAtencion(elem
							.getChildText("NUM_ATENCION"));

				}
			}

		} catch (MalformedURLException e) {
			logger.error("Error al consultar estado del DTE", e);
		} catch (Exception e) {
			logger.error("Error al consultar estado del DTE", e);
		}

		return estadoDte;
	}
}
