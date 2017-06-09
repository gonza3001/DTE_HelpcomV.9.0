package cl.helpcom.dte.method;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import cl.helpcom.dao.ConexionSqlGeneraEnvio;
import cl.helpcom.recursos.LectorFichero;
import cl.nic.dte.VerifyResult;
import cl.sii.siiDte.DTEDefType;
import cl.sii.siiDte.DTEDocument;
import cl.sii.siiDte.EnvioDTEDocument;
import cl.sii.siiDte.EnvioDTEDocument.EnvioDTE;
import cl.sii.siiDte.EnvioDTEDocument.EnvioDTE.SetDTE;
import cl.sii.siiDte.EnvioDTEDocument.EnvioDTE.SetDTE.Caratula;
import cl.sii.siiDte.EnvioDTEDocument.EnvioDTE.SetDTE.Caratula.SubTotDTE;
import cl.sii.siiDte.FechaType;

public class GeneraEnvioATercero {

	public void generarEnvio(ArrayList<String> datosEmp,String rutReceptor,String rutaFirmado,String nombreArchivo,String extensionDS) throws IOException, NoSuchAlgorithmException, CertificateException, KeyStoreException, UnrecoverableKeyException, ParseException, XmlException, ClassNotFoundException, SQLException {

		Integer emp_id=Integer.valueOf(datosEmp.get(6));
		String passCert=datosEmp.get(3);
		String empRut=datosEmp.get(0);
		String fchResol=datosEmp.get(4);
		Integer NroResol=Integer.valueOf(datosEmp.get(5));
		String tpoDoc="";
		String folio="";

		Properties propiedades = new Properties();
		InputStream entrada = null;
		entrada = new FileInputStream("/usr/local/F_E/Configuraciones/Propiedades/configuracionServidor.properties");
		propiedades.load(entrada);
		LectorFichero ficheros = new LectorFichero();
		ArrayList <String> directoriosArray= new ArrayList<>();

		String rutaRaiz=propiedades.getProperty("RUTA_RAIZ").toString();
		String certS = rutaRaiz+"/"+emp_id+"/"+propiedades.getProperty("RUTA_CERTIFICADO").toString();
		String passS = passCert;
		String rutEmisor = empRut;	//consulta rutEnviador
//		String rutReceptor = "60803000-K";  //consulta rutReceptor
		String fechaResolucion = fchResol;//co�nsulta resolucion BYK 2015-02-20
		//Directorio de documento firmado
		//Directorio donde quedar�n los documentos empaquetados
		String envioFirmado = rutaRaiz+"/"+emp_id+"/" +propiedades.getProperty("RUTA_EMPAQUETADO_TERCERO").toString()+extensionDS;
		int nroResol = NroResol; // siempre cero en ambiente de pruebas consulta nroResolucion

		// leo certificado y llave privada del archivo pkcs12
		KeyStore ks = KeyStore.getInstance("PKCS12");
		ks.load(new FileInputStream(certS), passS.toCharArray());
		String alias = ks.aliases().nextElement();
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
		caratula.xsetFchResol(FechaType.Factory.newValue(sdf.format(sdf.parse(fechaResolucion))));
		caratula.setNroResol(nroResol);
		caratula.setVersion(new BigDecimal("1.0"));

		SetDTE setDTE = SetDTE.Factory.newInstance();

		EnvioDTE envioDTE = EnvioDTE.Factory.newInstance();
		envioDTE.setVersion(new BigDecimal("1.0"));

		SimpleDateFormat sdfId = new SimpleDateFormat("yyyyMMddHHmmss");

		// documentos a enviar
		opts = new XmlOptions();
		namespaces.put("", "http://www.sii.cl/SiiDte");
		opts.setLoadSubstituteNamespaces(namespaces);

		ArrayList<String> pathProcesar = new ArrayList<String>();
		// Nombre del archivo a empaquetar SOLO
		pathProcesar.add(rutaFirmado);

		if (pathProcesar.size()>0){
		DTEDefType[] dtes = new DTEDefType[pathProcesar.size()];
		HashMap<Integer, Integer> hashTot = new HashMap<Integer, Integer>();

		ConexionSqlGeneraEnvio conexionSqlGeneraEnvio = new ConexionSqlGeneraEnvio();
		LectorFichero lectorFichero = new LectorFichero();

		String out="(";

		for (int i = 0; i < pathProcesar.size(); i++) {
			dtes[i] = DTEDocument.Factory.parse(new FileInputStream(pathProcesar.get(i)), opts).getDTE();
			directoriosArray.add( pathProcesar.get(i));
			// armar hash para totalizar por tipoDTE

			if (hashTot.get(dtes[i].getDocumento().getEncabezado().getIdDoc().getTipoDTE().intValue()) != null) {
				hashTot.put(dtes[i].getDocumento().getEncabezado().getIdDoc().getTipoDTE().intValue(),
						hashTot.get(dtes[i].getDocumento().getEncabezado().getIdDoc().getTipoDTE().intValue()) + 1);
			} else {
				hashTot.put(dtes[i].getDocumento().getEncabezado().getIdDoc().getTipoDTE().intValue(), 1);
				tpoDoc=dtes[i].getDocumento().getEncabezado().getIdDoc().getTipoDTE().toString();
				folio=String.valueOf(dtes[i].getDocumento().getEncabezado().getIdDoc().getFolio());
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
			cursor.setAttributeText(new QName("http://www.w3.org/2001/XMLSchema-instance","schemaLocation"),"http://www.sii.cl/SiiDte EnvioDTE_v10.xsd");
		}

		envio.getEnvioDTE().getSetDTE().setID("env-" + sdfId.format(new Date()));
		opts = new XmlOptions();
		opts.setSavePrettyPrint();
		opts.setSavePrettyPrintIndent(0);
		opts.setUseDefaultNamespace();
		opts.setSaveSuggestedPrefixes(namespaces);

		envio = EnvioDTEDocument.Factory.parse(envio.newInputStream(opts), opts);
		envio.getEnvioDTE().getSetDTE().setDTEArray(dtes);

		
		// firmo
		try {
			envio.sign(pKey, x509);
		} catch (Exception e) {
			System.out.println("error al firmar el envio: "+ e.getLocalizedMessage());
			System.exit(0);
		}

		VerifyResult resl = envio.verifyXML();

		Calendar Calendario = Calendar.getInstance();

        String anio = Integer.toString(Calendario.get(Calendar.YEAR));
        String mes = Integer.toString(Calendario.get(Calendar.MONTH) + 1);
        String dia = Integer.toString(Calendario.get(Calendar.DATE));
        String hora = Integer.toString(Calendario.get(Calendar.HOUR));
        String minuto = Integer.toString(Calendario.get(Calendar.MINUTE));
        String segundo = Integer.toString(Calendario.get(Calendar.SECOND));

		File archivoEnvio = new File(envioFirmado+ "/"+nombreArchivo);

		opts = new XmlOptions();
		opts.setCharacterEncoding("ISO-8859-1");

		XmlOptionCharEscapeMap escapes = new XmlOptionCharEscapeMap();
		
		try {
			escapes.addMapping('\'', XmlOptionCharEscapeMap.PREDEF_ENTITY);
			escapes.addMapping('&', XmlOptionCharEscapeMap.PREDEF_ENTITY);
			opts.setSaveSubstituteCharacters(escapes);
		} catch (XmlException e3) {
			System.out.println("Error al definir entidades predefinidas en XML");
		}

		try {
			envio.save(archivoEnvio, opts);
			//Guardar Registro ENVIADO a Tercero
			conexionSqlGeneraEnvio.updateEstadoEnvioTercero(emp_id, tpoDoc, folio);

		} catch (IOException e) {
			System.out.println("Error al escribir archivo salida: " + e);
			System.exit(0);
		}

		resl = envio.verifySignature();
		if (!resl.isOk()) {
			System.out.println("EnvioDTE: Firma XML Incorrecta: "+ resl.getMessage());
			return;
		}

	}
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