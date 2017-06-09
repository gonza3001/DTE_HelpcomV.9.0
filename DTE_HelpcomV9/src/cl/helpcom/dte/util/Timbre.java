package cl.helpcom.dte.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.apache.xmlbeans.XmlOptions;

import cl.helpcom.dao.ConexionSql1;
import cl.helpcom.itext.CreaPDFTest1;
import cl.helpcom.itext.LeerXML;
import cl.helpcom.pdf.ImpresoraTermica;
import cl.helpcom.recursos.LectorDTEText;
import cl.helpcom.recursos.LectorFichero;
import cl.nic.dte.VerifyResult;
import cl.sii.siiDte.AUTORIZACIONDocument;
import cl.sii.siiDte.AutorizacionType;
import cl.sii.siiDte.DTEDocument;

public class Timbre {
	public void timbrarDTE(Integer tipoDTE,Integer folio,ArrayList<String> arrayEncabezado,ArrayList<ArrayList<String>> arrayDetallePDF,ArrayList<ArrayList<String>> arrayReferencia,ArrayList<ArrayList<String>> arrayDscGlobales,String nomDocPlano) throws IOException{

		Properties propiedades = new Properties();
		InputStream entrada = null;
		entrada = new FileInputStream("/usr/local/F_E/Configuraciones/Propiedades/configuracionCreaDTE.properties");

		propiedades.load(entrada);
		LectorFichero lectorFichero = new LectorFichero();
		String baseRespaldo = propiedades.getProperty("RUTA_BASE_RESPALDO").toString();
		String tipoImpresora = propiedades.getProperty("TIPO_IMPRESORA").toString();
		String plantilla = propiedades.getProperty("RUTA_RESULTADO_DTE").toString()+"/sinTimbrar/"+tipoDTE+folio+".xml";
		//String archivoCaf = propiedades.getProperty("RUTA_CAF").toString()+tipoDTE+".xml";
		String archivoCaf = propiedades.getProperty("RUTA_CAF").toString();
		String dteTimbrado = lectorFichero.crearFicheroDTE(propiedades.getProperty("RUTA_RESULTADO_DTE").toString() + "/dte")+"/"+tipoDTE+folio+".xml";
		// Archivos de PDF
		String rutaPDF = propiedades.getProperty("RUTA_RESULTADO_PDF").toString();
		String rutaPDFCedible = propiedades.getProperty("RUTA_RESULTADO_PDF_CEDIBLE").toString();
		String rutaPDFBlob=propiedades.getProperty("RUTA_RESULTADO_PDF").toString();

		rutaPDF = lectorFichero.crearFicheroDTE(rutaPDF);//Crea Sub carpetas Anio,Mes
		rutaPDFCedible = lectorFichero.crearFicheroDTE(rutaPDFCedible);
		String resultadoPDF = rutaPDF;
		String resultadoPDFCedible = rutaPDFCedible;
		String templatePDF = propiedades.getProperty("RUTA_TEMPLATE_PDF").toString();

		//LectorDTEText lectorDTEText= new LectorDTEText();
		//arrayDetallePDF=lectorDTEText.colocarDblCrm(arrayDetallePDF);

		HashMap<String, String> namespaces = new HashMap<String, String>();
		namespaces.put("", "http://www.sii.cl/SiiDte");
		XmlOptions opts = new XmlOptions();
		opts.setLoadSubstituteNamespaces(namespaces);

		try {

			ConexionSql1 conexionSql1 = new ConexionSql1();
			archivoCaf+=conexionSql1.getNombreArchivoCAF(tipoDTE, folio);//Obtiene Nombre del CAF

			AutorizacionType caf = AUTORIZACIONDocument.Factory.parse(new File(archivoCaf), opts).getAUTORIZACION();
			// Construyo DTE a partir de archivo de entrada
			cl.sii.siiDte.DTEDocument doc = cl.sii.siiDte.DTEDocument.Factory.parse(new File(plantilla), opts);
			doc.getDTE().timbrar(caf.getCAF(), caf.getPrivateKey(null));

			opts = new XmlOptions();
			opts.setSaveImplicitNamespaces(namespaces);
			opts.setLoadSubstituteNamespaces(namespaces);
			opts.setSavePrettyPrint();
			opts.setSavePrettyPrintIndent(0);

			doc = DTEDocument.Factory.parse(doc.newInputStream(opts), opts);

			doc.save(new File(dteTimbrado),opts);
			System.out.println(dteTimbrado);
//			System.out.println("Escrito DTE timbrado: "+dteTimbrado + "/" + tipoDTE + folio + ".xml");
			CreaPDFTest1 creaPDF = new CreaPDFTest1();
			ImpresoraTermica impresoraTermica = new ImpresoraTermica();
			
			if (tipoImpresora.equals("1")){
				//Usar plantilla
					if (tipoDTE==33 || tipoDTE==34){
						String rutaTemplate=templatePDF;//guarda la ruta para que no se resetee
						rutaTemplate+="plantilla5661.pdf";
						creaPDF.creaPDF(rutaTemplate, resultadoPDF, arrayEncabezado,arrayDetallePDF, arrayReferencia,arrayDscGlobales,dteTimbrado,arrayDetallePDF.size(),"");
						rutaTemplate=templatePDF;
						rutaTemplate+="plantilla33.pdf";
						creaPDF.creaPDF(rutaTemplate, resultadoPDFCedible, arrayEncabezado,arrayDetallePDF, arrayReferencia,arrayDscGlobales,dteTimbrado,arrayDetallePDF.size(),"CEDIBLE");
					}else if (tipoDTE==56 || tipoDTE==61){
						templatePDF+="plantilla5661.pdf";
						creaPDF.creaPDF(templatePDF, resultadoPDF, arrayEncabezado,arrayDetallePDF, arrayReferencia,arrayDscGlobales,dteTimbrado,arrayDetallePDF.size(),"");
					}else if (tipoDTE==52){
						String rutaTemplate=templatePDF;//guarda la ruta para que no se resetee
						rutaTemplate+="plantilla5661.pdf";
						creaPDF.creaPDF(rutaTemplate, resultadoPDF, arrayEncabezado,arrayDetallePDF, arrayReferencia,arrayDscGlobales,dteTimbrado,arrayDetallePDF.size(),"");
						rutaTemplate=templatePDF;
						rutaTemplate+="plantilla33.pdf";
						creaPDF.creaPDF(rutaTemplate, resultadoPDFCedible, arrayEncabezado,arrayDetallePDF, arrayReferencia,arrayDscGlobales,dteTimbrado,arrayDetallePDF.size(),"CUADRUPLICADO COBRO EJECUTIVO-CEDIBLE CON SU FACTURA");
					}
			}else if(tipoImpresora.equals("2")){
					//Usar plantilla
					System.out.println("TERMICAS");
					if (tipoDTE==33 || tipoDTE==34){
						impresoraTermica.imprimirTicket(resultadoPDF, arrayEncabezado,arrayDetallePDF, arrayReferencia,arrayDscGlobales,dteTimbrado,arrayDetallePDF.size(),"");
						impresoraTermica.imprimirTicket(resultadoPDF, arrayEncabezado,arrayDetallePDF, arrayReferencia,arrayDscGlobales,dteTimbrado,arrayDetallePDF.size(),"CEDIBLE");
					}
					else if (tipoDTE==56 || tipoDTE==61){
						impresoraTermica.imprimirTicket(resultadoPDF, arrayEncabezado,arrayDetallePDF, arrayReferencia,arrayDscGlobales,dteTimbrado,arrayDetallePDF.size(),"");
					}else if (tipoDTE==52){
						impresoraTermica.imprimirTicket(resultadoPDF, arrayEncabezado,arrayDetallePDF, arrayReferencia,arrayDscGlobales,dteTimbrado,arrayDetallePDF.size(),"");
						impresoraTermica.imprimirTicket(resultadoPDF, arrayEncabezado,arrayDetallePDF, arrayReferencia,arrayDscGlobales,dteTimbrado,arrayDetallePDF.size(),"CUADRUPLICADO COBRO EJECUTIVO-CEDIBLE CON SU FACTURA");
					}
				}	
				
			String salidaPDF = resultadoPDF+"/"+arrayEncabezado.get(0)+arrayEncabezado.get(1)+".pdf";
			VerifyResult resl = doc.getDTE().verifyTimbre();

//			if (!resl.isOk()) {
//				System.out.println("Revise DTE");
//				System.out.println(resl.getMessage());
//			} else {
				// CRUD BD
				try {

					System.out.println("Entrando a la base de datos");
//					ConexionSql1 conexionSql1 = new ConexionSql1();
					LeerXML getTED = new LeerXML();
					conexionSql1.addDTETimbrado(Integer.valueOf(propiedades.getProperty("EMPRESA_ID").toString()), Integer.valueOf(arrayEncabezado.get(0)), Integer.valueOf(arrayEncabezado.get(1)), arrayEncabezado.get(21), arrayEncabezado.get(2), arrayEncabezado.get(22), Integer.valueOf(arrayEncabezado.get(46)), doc.toString(), "EMITIDO",salidaPDF);
					conexionSql1.addTED(getTED.obtenerTED(dteTimbrado), Integer.valueOf(arrayEncabezado.get(0)), Integer.valueOf(arrayEncabezado.get(1)));
					conexionSql1.addDocPlano(baseRespaldo+"/"+nomDocPlano, Integer.valueOf(arrayEncabezado.get(0)), Integer.valueOf(arrayEncabezado.get(1)));
					conexionSql1.cerrarConexion();

				} catch (Exception e) {
					System.err.println("No hay acceso a la base de datos " + e);
				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
