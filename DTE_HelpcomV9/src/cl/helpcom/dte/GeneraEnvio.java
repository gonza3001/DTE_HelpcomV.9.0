/**
 * Copyright [2009] [NIC Labs]
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the 	License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or
 * agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 **/

package cl.helpcom.dte;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptionCharEscapeMap;
import org.apache.xmlbeans.XmlOptions;

import cl.nic.dte.VerifyResult;
import cl.sii.siiDte.DTEDefType;
import cl.sii.siiDte.DTEDocument;
import cl.sii.siiDte.EnvioDTEDocument;
import cl.sii.siiDte.EnvioDTEDocument.EnvioDTE;
import cl.sii.siiDte.EnvioDTEDocument.EnvioDTE.SetDTE;
import cl.sii.siiDte.EnvioDTEDocument.EnvioDTE.SetDTE.Caratula;
import cl.sii.siiDte.EnvioDTEDocument.EnvioDTE.SetDTE.Caratula.SubTotDTE;
import cl.sii.siiDte.FechaType;

/**
 * @author Mauricio Rodriguez <a href="mailto:mrodriguez@helpcom.cl"></a>
 *
 * Esta clase se encarga de generar un EnvioDTE segun las exigencias del SII. El
 * RUT del enviador lo deduce desde el certificado digital. El subtotal de DTE
 * se calcula a partir de los DTE de entrada.
 *
 */
public class GeneraEnvio {
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		Properties propiedades = new Properties();
		InputStream entrada = null;
		entrada = new FileInputStream("Configuraciones/configuracionServidor.properties");
		propiedades.load(entrada);
		//LectorFichero ficheros = new LectorFichero();
		//LeerXML lectorXML = new LeerXML();
		String certS = propiedades.getProperty("RUTA_CERTIFICADO").toString();
		String passS = propiedades.getProperty("PASS_CERTIFICADO").toString();
		String rutEmisor = "79716930-7";	//consulta rutEnviador
		String rutReceptor = "60803000-K";  //consulta rutReceptor
		String fechaResolucion = "2015-02-20";//coñnsulta resolucion
		String directorioDTES = "/home/mau/Documentos/DTE/BYK/documentosDTE/dteServidor/Empaquetar";
		String envioFirmado = "/home/mau/Documentos/DTE/BYK/documentosDTE/dteServidor/envio/envioFirmado.xml";
		int nroResol = 0; // siempre cero en ambiente de pruebas consulta nroResolucion
		// leo certificado y llave privada del archivo pkcs12
		KeyStore ks = KeyStore.getInstance("PKCS12");
		ks.load(new FileInputStream(certS), passS.toCharArray());
		String alias = ks.aliases().nextElement();
		System.out.println("Usando certificado " + alias
				+ " del archivo PKCS12: " + certS);
		X509Certificate x509 = (X509Certificate) ks.getCertificate(alias);
		PrivateKey pKey = (PrivateKey) ks.getKey(alias, passS.toCharArray());

		HashMap<String, String> namespaces = new HashMap<String, String>();
		namespaces.put("", "http://www.sii.cl/SiiDte");
		XmlOptions opts = new XmlOptions();
		opts.setLoadSubstituteNamespaces(namespaces);
		HashMap<String, String> namespacesEnv = new HashMap<String, String>();
		namespacesEnv.put("", "http://www.sii.cl/SiiDte");
		XmlOptions optsEnv = new XmlOptions();
		optsEnv.setLoadSubstituteNamespaces(namespacesEnv);

		EnvioDTEDocument envio = EnvioDTEDocument.Factory.newInstance(optsEnv);
		Caratula caratula = Caratula.Factory.newInstance();
		caratula.setRutEmisor(rutEmisor);
		caratula.setRutReceptor(rutReceptor);
		caratula.setRutEnvia(getRutCertificado(x509));

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		caratula.xsetFchResol(FechaType.Factory.newValue(sdf.format(sdf
				.parse(fechaResolucion))));
		caratula.setNroResol(nroResol);
		caratula.setVersion(new BigDecimal("1.0"));

		SetDTE setDTE = SetDTE.Factory.newInstance();

		EnvioDTE envioDTE = EnvioDTE.Factory.newInstance();
		envioDTE.setVersion(new BigDecimal("1.0"));

		SimpleDateFormat sdfId = new SimpleDateFormat("yyyyMMddHHmmss");

		// documentos a enviar
		namespaces.put("", "http://www.sii.cl/SiiDte");
		opts = new XmlOptions();
		opts.setLoadSubstituteNamespaces(namespaces);

		// leer directorio
		File dir = new File(directorioDTES);
		String[] children = dir.list();

		if (children == null) {
			System.out.println("No hay DTEs que empaquetar en directorio "+ dir.getAbsolutePath());
			return;
		}

		java.util.Arrays.sort(children);
		ArrayList<String> pathProcesar = new ArrayList<String>();

		for (int i = 0; i < children.length; i++) {
			// Get filename of file or directory
			if (children[i].endsWith(".xml"))
				pathProcesar.add(dir.getAbsolutePath() + "/" + children[i]);
		}

		System.out.println("Por empaquetar " + pathProcesar.size() + " DTEs");
		DTEDefType[] dtes = new DTEDefType[pathProcesar.size()];
		HashMap<Integer, Integer> hashTot = new HashMap<Integer, Integer>();
		java.util.Arrays.sort(children);
		for (int i = 0; i < pathProcesar.size(); i++) {
			System.out.println("Agregando documento " + pathProcesar.get(i));
			dtes[i] = DTEDocument.Factory.parse(
					new FileInputStream(pathProcesar.get(i)), opts).getDTE();
			// armar hash para totalizar por tipoDTE
			if (hashTot.get(dtes[i].getDocumento().getEncabezado().getIdDoc()
					.getTipoDTE().intValue()) != null) {
				hashTot.put(
						dtes[i].getDocumento().getEncabezado().getIdDoc()
								.getTipoDTE().intValue(),
						hashTot.get(dtes[i].getDocumento().getEncabezado()
								.getIdDoc().getTipoDTE().intValue()) + 1);
			} else {
				hashTot.put(dtes[i].getDocumento().getEncabezado().getIdDoc()
						.getTipoDTE().intValue(), 1);
			}
		}

		SubTotDTE[] subtDtes = new SubTotDTE[hashTot.size()];
		int i = 0;
		for (Integer tipo : hashTot.keySet()) {
			SubTotDTE subt = SubTotDTE.Factory.newInstance();
			subt.setTpoDTE(new BigInteger(tipo.toString()));
			subt.setNroDTE(new BigInteger(hashTot.get(tipo).toString()));
			subtDtes[i] = subt;
			i++;
		}

		caratula.setSubTotDTEArray(subtDtes);

		setDTE.setCaratula(caratula);
		envioDTE.setSetDTE(setDTE);

		envio.setEnvioDTE(envioDTE);

		// Debo agregar el schema location (Sino SII rechaza)
		XmlCursor cursor = envio.newCursor();
		if (cursor.toFirstChild()) {
			cursor.setAttributeText(new QName(
					"http://www.w3.org/2001/XMLSchema-instance",
					"schemaLocation"),
					"http://www.sii.cl/SiiDte EnvioDTE_v10.xsd");
		}

		envio.getEnvioDTE().getSetDTE()
				.setID("env-" + sdfId.format(new Date()));

		opts = new XmlOptions();
		opts.setSavePrettyPrint();
		opts.setSavePrettyPrintIndent(0);
		opts.setUseDefaultNamespace();
		opts.setSaveSuggestedPrefixes(namespaces);

		envio = EnvioDTEDocument.Factory
				.parse(envio.newInputStream(opts), opts);

		envio.getEnvioDTE().getSetDTE().setDTEArray(dtes);

		// firmo
		try {
			envio.sign(pKey, x509);
		} catch (Exception e) {
			System.out.println("error al firmar el envio: "
					+ e.getLocalizedMessage());
			System.exit(0);
		}

		VerifyResult resl = envio.verifyXML();
//		if (!resl.isOk()) {
//			System.out.println("EnvioDTE: Estructura XML Incorrecta: "
//					+ resl.getMessage());
//			return;
//		}

		System.out.println("EnvioDTE: Estructura XML OK");



		File archivoEnvio = new File(envioFirmado);

		opts = new XmlOptions();
		opts.setCharacterEncoding("ISO-8859-1");

		XmlOptionCharEscapeMap escapes = new XmlOptionCharEscapeMap();
		try {
			escapes.addMapping('\'', XmlOptionCharEscapeMap.PREDEF_ENTITY);
			escapes.addMapping('&', XmlOptionCharEscapeMap.PREDEF_ENTITY);
			opts.setSaveSubstituteCharacters(escapes);
		} catch (XmlException e3) {
			System.out
					.println("Error al definir entidades predefinidas en XML");
		}

		try {
			envio.save(archivoEnvio, opts);
		} catch (IOException e) {
			System.out.println("Error al escribir archivo salida: " + e);
			System.exit(0);
		}
		System.out.println("Escrito archivo salida " + envioFirmado);

		resl = envio.verifySignature();
		if (!resl.isOk()) {
			System.out.println("EnvioDTE: Firma XML Incorrecta: "
					+ resl.getMessage());
			return;
		}

		System.out.println("EnvioDTE: Firma XML OK");
	}

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
