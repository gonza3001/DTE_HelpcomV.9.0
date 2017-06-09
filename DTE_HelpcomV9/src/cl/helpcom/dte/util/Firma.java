package cl.helpcom.dte.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.xmlbeans.XmlOptions;

import cl.helpcom.dte.method.GeneraEnvioATercero;
import cl.helpcom.itext.LeerXML;
import cl.helpcom.recursos.LectorFichero;
import cl.nic.dte.VerifyResult;
import cl.sii.siiDte.DTEDocument;

public class Firma {

	public String firmarDTE(ArrayList<String> datosEmp,String rutReceptor,String xml_firmado,Integer tdo_id,Integer doc_folio,Integer emp_id,String passCert,String fchEmision) throws IOException {

		System.out.println("Entrando a Empresa"+emp_id+"... PASS:"+passCert);
		Properties propiedades = new Properties();
		InputStream entrada = null;
		entrada = new FileInputStream("/usr/local/F_E/Configuraciones/Propiedades/configuracionServidor.properties");
		propiedades.load(entrada);
		LectorFichero ficheros = new LectorFichero();
		LeerXML lectorXML = new LeerXML();
		//Obtener de BD

		String dteTimbrado = xml_firmado;
		GeneraEnvioATercero envioTercero = new GeneraEnvioATercero();

		String rutaRaiz=propiedades.getProperty("RUTA_RAIZ").toString();
		String certificado = rutaRaiz+"/"+emp_id+"/"+propiedades.getProperty("RUTA_CERTIFICADO").toString();
		String claveCertificado =passCert;
		String archivoFirmado =rutaRaiz+"/"+emp_id+"/"+propiedades.getProperty("RUTA_RESULTADO_DTE_FIRMADO").toString();
		String archivoAuxiliar =rutaRaiz+"/"+emp_id+"/"+propiedades.getProperty("RUTA_ARCHIVO_AUXILIAR").toString();
		String archivoEmpaquetar=rutaRaiz+"/"+emp_id+"/"+propiedades.getProperty("RUTA_DIR_EMPAQUETAR").toString();
		String nroResol="";
		String fchResol="";

//		archivoFirmado = ficheros.crearFicheroDTE(archivoFirmado);
		archivoFirmado = ficheros.crearFicheroMMDDFlex(archivoFirmado, fchEmision);
		// /usr/local/F_E/DTE/1/Certificado
		// /usr/local/F_E/DTE/1/DocumentoDTE
		// /usr/local/F_E/DTE/1/Empaquetar
		// /usr/local/F_E/DTE/1/Certificado

		String[] lines = dteTimbrado.split(System.getProperty("line.separator"));
		dteTimbrado="";

		for (int i = 0; i < lines.length; i++) {
			dteTimbrado+=lines[i].replaceAll("  ", "")+"\n";
		}

		File a = new File(archivoAuxiliar+tdo_id+doc_folio+"Aux.xml");
		FileUtils.writeStringToFile(a, dteTimbrado);
		//Se obtienen del dte timbrado
		String tipoDTE=tdo_id.toString();
		String folio =doc_folio.toString();

		HashMap<String, String> namespaces = new HashMap<String, String>();
		namespaces.put("", "http://www.sii.cl/SiiDte");

		XmlOptions opts = new XmlOptions();
		opts.setSavePrettyPrint();
		opts.setLoadSubstituteNamespaces(namespaces);
		opts.setSavePrettyPrintIndent(0);
		opts.setCharacterEncoding("ISO-8859-1");
		opts.setSaveImplicitNamespaces(namespaces);


		try {

			//cl.sii.siiDte.DTEDocument doc = cl.sii.siiDte.DTEDocument.Factory.parse(new File(archivoAuxiliar+tdo_id+doc_folio+"Aux.xml"), opts);
			cl.sii.siiDte.DTEDocument doc = cl.sii.siiDte.DTEDocument.Factory.parse(dteTimbrado,opts);

			opts = new XmlOptions();
			opts.setSaveImplicitNamespaces(namespaces);
			opts.setLoadSubstituteNamespaces(namespaces);
			opts.setSavePrettyPrint();
			opts.setSavePrettyPrintIndent(0);


			doc = DTEDocument.Factory.parse(doc.newInputStream(opts), opts);

			//DTEDocument doc = DTEDocument.Factory.parse(new File("/home/mau/Documentos/DTE/BYK/documentosDTE/dteServidor/"+tdo_id+doc_folio+"Aux.xml"),opts);

			// leo certificado y llave privada del archivo pkcs12
			KeyStore ks = KeyStore.getInstance("PKCS12");
			ks.load(new FileInputStream(certificado),claveCertificado.toCharArray());
			String alias = ks.aliases().nextElement();
			//System.out.println("Usando certificado " + alias	+ " del archivo PKCS12: " + certificado);

			X509Certificate cert = (X509Certificate) ks.getCertificate(alias);
			PrivateKey key = (PrivateKey) ks.getKey(alias,claveCertificado.toCharArray());

			System.out.println("Firmando digitalmente");
			doc.getDTE().sign(key, cert);

			// Guardo
			//System.out.println("Almacenando DTE en " + archivoFirmado+"/"+tipoDTE+folio+".xml");
			opts = new XmlOptions();
			opts.setCharacterEncoding("ISO-8859-1");
			opts.setSaveImplicitNamespaces(namespaces);

			if (tipoDTE.equals("56")){
				tipoDTE="956";//añadirle un 9 para que cambie orden de empaquetado

			}

			doc.save(new File(archivoFirmado+"/"+tipoDTE+folio+".xml"), opts);//Firmados

			doc.save(new File(archivoEmpaquetar+"/"+tipoDTE+folio+".xml"), opts);//Empaquetar
			//Empaquetar A Tercero
			try {
				envioTercero.generarEnvio(datosEmp,rutReceptor ,archivoFirmado+"/"+tipoDTE+folio+".xml",tipoDTE+folio+".xml");
			} catch (Exception e) {
				System.out.println("Error en el envio a Tercero, documento "+tipoDTE+folio+".xml");
			}

			VerifyResult resl = doc.getDTE().verifyTimbre();
			//a.delete();//Borrar Archivo

			if (!resl.isOk()) {
				System.out.println("Documento: Timbre Incorrecto: "
						+ resl.getMessage());
			} else {
				System.out.println("Documento: Timbre OK");
			}

			resl = doc.getDTE().verifyXML();
			if (!resl.isOk()) {
				System.out.println("Documento: Estructura XML Incorrecta: "
						+ resl.getMessage());
			} else {
				System.out.println("Documento: Estructura XML OK");
			}

			resl = doc.getDTE().verifySignature();
			if (!resl.isOk()) {
				System.out.println("Documento: Firma XML Incorrecta: "
						+ resl.getMessage());
			} else {
				System.out.println("Documento: Firma XML OK");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		String res ="";
		res=ficheros.fileToString(archivoEmpaquetar+"/"+tipoDTE+folio+".xml");

		return res;
	}
}//End Class
