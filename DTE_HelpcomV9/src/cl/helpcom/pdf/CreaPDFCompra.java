package cl.helpcom.pdf;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.itextpdf.text.log.SysoLogger;

import jargs.gnu.CmdLineParser;
import cl.helpcom.dao.ConexionSqlWeb;
import cl.helpcom.itext.LeerXML;
import cl.helpcom.itext.PDFCompra;
import cl.helpcom.itext.PDFVenta;
import cl.helpcom.recursos.LectorDTEText;
import cl.helpcom.recursos.LectorFichero;
public class CreaPDFCompra {

	/**
	 * @author Mauricio Rodríguez G.
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

//		Properties propiedades = new Properties();
//	    InputStream entrada = null;
//		entrada = new FileInputStream("/usr/local/F_E/Configuraciones/Propiedades/configuracionServidor.properties");

		CmdLineParser parser = new CmdLineParser();
		CmdLineParser.Option docId = parser.addStringOption('d', "docId");
		CmdLineParser.Option emprId = parser.addStringOption('e', "empID");
		CmdLineParser.Option folio = parser.addStringOption('f', "Folio");
		
		LectorDTEText lectorDTEText = new LectorDTEText();
		LectorFichero lectorFichero = new LectorFichero();
		LeerXML leerXML = new LeerXML();
		PDFCompra pdfCompra = new PDFCompra();

		try {
			parser.parse(args);
		} catch (CmdLineParser.OptionException e) {
			System.exit(2);
		}

		String docStr = (String) parser.getOptionValue(docId);
		
		String empId = (String) parser.getOptionValue(emprId);
		String folioS = (String) parser.getOptionValue(folio);
		

		
		String raiz="/var/www/html/Centaurus/DTE/";

		ArrayList<String> arrayIdDoc = new ArrayList<String>();
		ArrayList<String> arrayEmisor = new ArrayList<String>();
		ArrayList<String> arrayReceptor = new ArrayList<String>();
		ArrayList<String> arrayTotales = new ArrayList<String>();
		ArrayList<ArrayList<String>> arrayDetalle = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> arrayReferencia = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> arrayILA = new ArrayList<ArrayList<String>>();
		ArrayList<String> arrayCaratula = new ArrayList<String>();
//		empId="3";//Entrada TERMINAL
		
		ConexionSqlWeb conexionSqlWeb = new ConexionSqlWeb();
		conexionSqlWeb.obtenerArchivoCompra(docStr, "/tmp/COMPRA"+empId+docStr+".xml");
		conexionSqlWeb.cerrarConexion();
		Files.setPosixFilePermissions(Paths.get("/tmp/COMPRA"+empId+docStr+".xml"), lectorFichero.permisos());
		
		String inXML="/tmp/COMPRA"+empId+docStr+".xml";//COMPRAnombreemp+id.xml
		//String inXML1=raiz+"/"+empId+"/Auxiliar/"+docId+empId+".xml";
		String pdfTemplate=raiz+empId+"/documentosPDF/plantillaCOMPRA.pdf";
        
		File file = new File(inXML);
		DocumentBuilderFactory dbf2 = DocumentBuilderFactory.newInstance();
		DocumentBuilder db2 = dbf2.newDocumentBuilder();
		Document doc2 = db2.parse(file);
		doc2.getDocumentElement().normalize();
		// System.out.println("Root element " +
		// doc.getDocumentElement().getNodeName());
		NodeList nodeLst2 = doc2.getElementsByTagName("DTE");
		arrayCaratula=leerXML.getCaratula(inXML);
		
		
		String outPDF = null;
		for (int i = 0; i < nodeLst2.getLength(); i++) {
		
			arrayIdDoc = leerXML.getIdDocPDF(nodeLst2.item(i));
	 		arrayEmisor = leerXML.getEmisorPDF(nodeLst2.item(i));
			arrayReceptor= leerXML.getReceptorPDF(nodeLst2.item(i));
			arrayTotales = leerXML.getTotalesPDF(nodeLst2.item(i));
			arrayDetalle= leerXML.getDetallesPDF(nodeLst2.item(i));
			arrayReferencia=leerXML.getReferenciasPDF(nodeLst2.item(i));
			
			//arrayILA=leerXML.getILA(inXML);
			
			Element doc = (Element) nodeLst2.item(i);
			NodeList nodeLst = doc.getElementsByTagName("Totales");
			
			arrayILA=leerXML.getILA(nodeLst.item(0));
			
			outPDF=lectorFichero.crearFicheroMMDDFlex(raiz+empId+"/documentosPDF/COMPRA", arrayIdDoc.get(2));
			//lectorFichero.borraDocumentoTXT(inXML); //Se borra archivo XML
			
	
			String ted="";
			ted= leerXML.obtenerTED(inXML);
			pdfCompra.creaPDF(pdfTemplate, outPDF, arrayIdDoc, arrayEmisor, arrayReceptor, arrayTotales, arrayDetalle,arrayReferencia,arrayILA, ted,arrayCaratula);
		}
		if (nodeLst2.getLength()>1){
			outPDF+="/"+docStr+folioS+".pdf";
		}else{
			outPDF+="/"+docStr+arrayIdDoc.get(1)+".pdf";
		}
		
		
		System.out.println(outPDF.replaceAll("/var/www/html",""));
			
		
}}
