package cl.helpcom.pdf;

import java.io.File;
import java.util.ArrayList;
import jargs.gnu.CmdLineParser;
import cl.helpcom.dao.ConexionSqlWeb;
import cl.helpcom.itext.PDFVenta;
import cl.helpcom.recursos.LectorDTEText;
import cl.helpcom.recursos.LectorFichero;
public class CreaPDFVenta {

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
		CmdLineParser.Option emprID = parser.addStringOption('e', "empID");
		LectorDTEText lectorDTEText = new LectorDTEText();
		LectorFichero lectorFichero = new LectorFichero();

		try {
			parser.parse(args);
		} catch (CmdLineParser.OptionException e) {
			System.exit(2);
		}

		String docStr = (String) parser.getOptionValue(docId);
		String empID = (String) parser.getOptionValue(emprID);

//		docStr="10"; //ID de documento
//		/**COMENTAR**/ empID="11";	//Empresa
//		docStr="123";
		
		String rutaTxt="/tmp/Vta"+empID+docStr+".txt"; //Ruta donde se crea el archivo plano en la carpeta raiz de la empresa
		ConexionSqlWeb conexionSqlWeb = new ConexionSqlWeb();		
		String ted=conexionSqlWeb.obtenerArchivoVenta(docStr, rutaTxt,Integer.valueOf(empID));
		conexionSqlWeb.obtenerDatos(Integer.valueOf(empID));
		
		String nroResol=conexionSqlWeb.getNroResolucion();
		String fchResol=conexionSqlWeb.getFchResolucion();
		
//		/**COMENTAR**/ ted="<TED version=\"1.0\"><DD><RE>76392555-2</RE><TD>52</TD><F>15</F><FE>2017-01-23</FE><RR>77349320-0</RR><RSR>COMERCIAL AMAR HERMANOS Y COMPAÑIA LIMI</RSR><MNT>3827552</MNT><IT1>ITEM 1</IT1><CAF version=\"1.0\"><DA><RE>76392555-2</RE><RS>DISTRIBUIDORA ABACO LIMITADA</RS><TD>52</TD><RNG><D>1</D><H>200</H></RNG><FA>2017-01-23</FA><RSAPK><M>tClxpDWQyEvln9ylNlYqvRyUaiWR+pmgIFA9tRbXvINKIpLMHKdq7GapYoYMG7FXOKBqNeH2Tqgh7ATm154Ziw==</M><E>Aw==</E></RSAPK><IDK>100</IDK></DA><FRMA algoritmo=\"SHA1withRSA\">dLunr9SF+2shPH34j/UbsAtOQYlQgSCrcdM391i48+ULfU8Z65ImClVgQjyaeMRJ2zVv2zq7IYq3t6pZ5dwAOg==</FRMA></CAF><TSTED>2017-01-23T14:10:09</TSTED></DD><FRMT algoritmo=\"SHA1withRSA\">He8F9of78OOhV8qhMQAXd1UPXfGdBCJmXmzzFm9UK7pvjP0k/aM//VCoJluze/UkBCBWSLBCPO34GLMCa58C2g==</FRMT></TED>";
//		/**COMENTAR**/ rutaTxt="/home/mau/Documentos/DTE/Abaco/CASO3.txt";
		
		ArrayList<String> arrayEncabezado = new ArrayList<String>();	
		ArrayList<ArrayList<String>> arrayDetalle = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> arrayReferencia = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> arrayDescRecGlob = new ArrayList<ArrayList<String>>();

		arrayEncabezado=lectorDTEText.formatoA_DTEs(rutaTxt, "A",arrayEncabezado);		//ENCABEZADO
		arrayDetalle= lectorDTEText.formatoB_DTEs(rutaTxt, arrayDetalle, "B");			//DETALLES
		arrayDescRecGlob= lectorDTEText.formatoB_DTEs(rutaTxt, arrayDescRecGlob, "C");	//DSC REC GLOBAL
		arrayReferencia=lectorDTEText.formatoB_DTEs(rutaTxt, arrayReferencia, "D");	//REFERENCIAS

		String raizWeb="/var/www/html/Centaurus/DTE/";
		String rutaPDFTemplate = raizWeb+empID+"/documentosPDF";//Template		
		String archivoSalida=lectorFichero.crearFicheroMMDDFlex(raizWeb+empID+"/documentosPDF/VENTA", arrayEncabezado.get(2));
		String archivoSalidaCEDIBLE=lectorFichero.crearFicheroMMDDFlex(raizWeb+empID+"/documentosPDF/VENTA/CEDIBLE", arrayEncabezado.get(2));

		PDFVenta creaPDFVenta= new PDFVenta();
		String rutaTemplate="";
		
//		/**COMENTAR**/ rutaPDFTemplate="/home/mau/Documentos/DTE/Abaco/documentosPDF";//ejecutar y factura
		
		String sFichero = archivoSalida +"/"+arrayEncabezado.get(0)+arrayEncabezado.get(1)+".pdf";
		String salidaWeb="";
		File f = new File(sFichero);
		//Verificar si existe el documento para no crearlo nuevamente
		if (f.isFile() || f.exists()) {
			salidaWeb=archivoSalida+"/"+arrayEncabezado.get(0)+arrayEncabezado.get(1)+".pdf";
			System.out.println(salidaWeb.replaceAll("/var/www/html", ""));
		}else{
				//	Usar plantilla
				if (arrayEncabezado.get(0).equals("33") || arrayEncabezado.get(0).equals("34") || arrayEncabezado.get(0).equals("52")){
					rutaTemplate=rutaPDFTemplate;//guarda la ruta para que no se resetee
					rutaTemplate+="/plantilla5661.pdf";
					creaPDFVenta.creaPDF(rutaTemplate, archivoSalida, arrayEncabezado, arrayDetalle, arrayReferencia, arrayDescRecGlob, ted, arrayDetalle.size(), "",nroResol,fchResol);
					rutaTemplate=rutaPDFTemplate;
					rutaTemplate+="/plantilla33.pdf";
					creaPDFVenta.creaPDF(rutaTemplate, archivoSalidaCEDIBLE, arrayEncabezado, arrayDetalle, arrayReferencia, arrayDescRecGlob, ted, arrayDetalle.size(), "CEDIBLE",nroResol,fchResol);
				}else if (arrayEncabezado.get(0).equals("56") || arrayEncabezado.get(0).equals("61")){
					rutaTemplate=rutaPDFTemplate;//guarda la ruta para que no se resetee
					rutaTemplate+="/plantilla5661.pdf";
					creaPDFVenta.creaPDF(rutaTemplate, archivoSalida, arrayEncabezado, arrayDetalle, arrayReferencia, arrayDescRecGlob, ted, arrayDetalle.size(), "",nroResol,fchResol);
				}else if (arrayEncabezado.get(0).equals("52") || arrayEncabezado.get(0).equals("61")){
					rutaTemplate=rutaPDFTemplate;//guarda la ruta para que no se resetee
					rutaTemplate+="/plantilla5661.pdf";
					creaPDFVenta.creaPDF(rutaTemplate, archivoSalida, arrayEncabezado, arrayDetalle, arrayReferencia, arrayDescRecGlob, ted, arrayDetalle.size(), "",nroResol,fchResol);
					rutaTemplate=rutaPDFTemplate;
					rutaTemplate+="/plantilla33.pdf";
					creaPDFVenta.creaPDF(rutaTemplate, archivoSalidaCEDIBLE, arrayEncabezado, arrayDetalle, arrayReferencia, arrayDescRecGlob, ted, arrayDetalle.size(), "CUADRUPLICADO COBRO EJECUTIVO-CEDIBLE CON SU FACTURA",nroResol,fchResol);
				}
				
				salidaWeb=archivoSalida+"/"+arrayEncabezado.get(0)+arrayEncabezado.get(1)+".pdf";
			
				try {
					
					System.out.println(salidaWeb.replaceAll("/var/www/html", ""));
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		
	}		
}
