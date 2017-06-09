package cl.helpcom.Libro;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlOptions;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import cl.helpcom.dao.ConexionSqlWeb;
import cl.helpcom.dte.util.AgregarCamposLibros;
import cl.helpcom.dte.util.FirmaLibro;
import cl.helpcom.recursos.ComunicadorAppClienteTXT;
import cl.helpcom.recursos.LectorFichero;
import cl.helpcom.recursos.Validador;
import cl.sii.siiDte.libroCV.LibroCompraVentaDocument;
import cl.sii.siiDte.libroCV.LibroCompraVentaDocument.LibroCompraVenta;
import cl.sii.siiDte.libroCV.LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro;
import cl.sii.siiDte.libroCV.LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.ResumenPeriodo;
import cl.sii.siiDte.libroCV.LibroCompraVentaDocument.LibroCompraVenta.EnvioLibro.ResumenSegmento;
import jargs.gnu.CmdLineParser;
	
public class GeneraLibroCompraVenta {

	private static void printUsage() {
		System.err.println("Utilice: java cl.nic.dte.examples.Ge	neraFacturaCompleta "
						+ "-a <caf.xml> -p <plantilla.xml> -c <certDigital.p12> -s <password> -o <resultado.xml>");
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		CmdLineParser parser = new CmdLineParser();
		CmdLineParser.Option emprID = parser.addStringOption('e', "empId");
		CmdLineParser.Option nombDoc = parser.addStringOption('n', "nomDoc");
		CmdLineParser.Option tipoEnvio = parser.addStringOption('x', "tpoEnv");//1 Certificacion || 2 Produccion
		
		try {
			parser.parse(args);
		
		LectorFichero lectorFichero = new LectorFichero();
		String empID = (String) parser.getOptionValue(emprID);
		String nomDoc = (String) parser.getOptionValue(nombDoc);
		String tipoEnvioS = (String) parser.getOptionValue(tipoEnvio);
//		
//		String empID="29";
//		String tipoEnvioS="1";
		
		String passS="";
		String certS = "/usr/local/F_E/DTE/"+empID+"/Certificado/Certificado.pfx";//Ruta Certificado
		String resultadoSinFirma = "/tmp/LBR"+empID+".xml";//Libro sin Firmar
		String resultadoFirmado = "/var/www/html/Centaurus/DTE/"+empID+"/Libros";
		String outPDF="";
		String rutaLibro="/var/www/html/Centaurus/DTE/"+empID+"/BASELIBRO/"+nomDoc;
//		String rutaLibro="/home/mau/Documentos/DTE/Ideal/BASELIBRO/Libro.txt";
		String libroPlantilla = "/var/www/html/Centaurus/entradaLibro.xml";
		
		ConexionSqlWeb conexionSqlWeb = new ConexionSqlWeb();
		int empresaINT=Integer.valueOf(empID);
		conexionSqlWeb.obtenerDatos(empresaINT);		
		passS=conexionSqlWeb.getClaveCert();
		
		ComunicadorAppClienteTXT c = new ComunicadorAppClienteTXT();
		Validador val = new Validador();
		
		ArrayList<ArrayList<String>> arrayLineasCaratulas= new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> arrayLineasTotal= new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> arrayLineasDetalle= new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> arrayLineasSegmentos= new ArrayList<ArrayList<String>>();

		// Lee y llena los campo
		arrayLineasCaratulas = c.formatoLibroCVLineas(rutaLibro,arrayLineasCaratulas, "C");
		arrayLineasTotal= c.formatoLibroCVLineas(rutaLibro,arrayLineasTotal, "T");
		arrayLineasDetalle = c.formatoLibroCVLineas(rutaLibro,arrayLineasDetalle, "D");
		arrayLineasSegmentos = c.formatoLibroCVLineas(rutaLibro,arrayLineasSegmentos, "S");
		// Valida los campos
		val.validaEntradaLCVCaratula(arrayLineasCaratulas);
		val.validaEntradaLCVTotales(arrayLineasTotal, c.getCantidadTotales());
		val.validaEntradaLCVDetalles(arrayLineasDetalle,c.getCantidadDetallesTotales());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		/* XML FACTURA */
		LibroCompraVentaDocument libCV;
//		System.out.println("********Genera Documento Libro Compra venta********");

		/* LibroCV */
		libCV = LibroCompraVentaDocument.Factory.parse(new FileInputStream(libroPlantilla));
		// libCV = LibroCompraVentaDocument.Factory.newInstance();
		LibroCompraVenta libroCompraVenta = libCV.getLibroCompraVenta();
		libCV.getLibroCompraVenta().setVersion(new BigDecimal("1.0"));
		EnvioLibro envioLibro = libroCompraVenta.addNewEnvioLibro();
		libCV.getLibroCompraVenta().getEnvioLibro().setID("V2001-3");

		XmlCursor cursor = libCV.newCursor();

		ResumenPeriodo resumenPeriodo = envioLibro.addNewResumenPeriodo();
		
		AgregarCamposLibros addCampos = new AgregarCamposLibros();
		// Agregar CARATULA
		
		addCampos.addDatosCartula(envioLibro, arrayLineasCaratulas);
		// Agregar TOTALES
		
		addCampos.addDatosTotales(resumenPeriodo,arrayLineasTotal,c.getCantidadTotales());
		// Agregar RESUMEN SEGMENTO
		
		outPDF=lectorFichero.crearFicheroMMDDFlex(resultadoFirmado, arrayLineasCaratulas.get(0).get(3));
		outPDF+="/"+arrayLineasCaratulas.get(0).get(5)+"_"+arrayLineasCaratulas.get(0).get(6)+"_"+arrayLineasCaratulas.get(0).get(7)+".xml";
		
		if (arrayLineasSegmentos.size()>0) {
			if (arrayLineasSegmentos.get(0).get(0)!=null) {
				 ResumenSegmento resumenSegmento = envioLibro.addNewResumenSegmento();
				 addCampos.addDatosResumenSegmento(resumenSegmento,arrayLineasSegmentos, c.getCantidadSegmentos());
			}
		}
		
		// Agregar DETALLES
		addCampos.addDatosDetalle(envioLibro, arrayLineasDetalle,c.getCantidadDetallesTotales());
		
		XmlOptions opts = new XmlOptions();
		opts = new XmlOptions();
		opts.setSavePrettyPrint();
		opts.setSavePrettyPrintIndent(0);
		opts.setCharacterEncoding("ISO-8859-1");
//		System.out.println(libCV.toString());
		libCV = LibroCompraVentaDocument.Factory.parse(libCV.newInputStream(opts), (opts));
		// CARPETA AUXILIAR DTE XML SIN TIMBRE
		File archivoEnvio = new File(resultadoSinFirma);
		libCV.save(archivoEnvio, opts);
		
		FirmaLibro firmaLibro = new FirmaLibro();
		String mensaje=firmaLibro.firmarLibro(certS, passS, resultadoSinFirma,outPDF,empresaINT,"ENVIADO",arrayLineasCaratulas.get(0).get(5),arrayLineasCaratulas.get(0).get(2),"xmlasdas",tipoEnvioS);
		
		System.out.println("1@helpcom@LIBRO ENVIADO CORRECTAMENTE<br>"+mensaje+"@helpcom@"+outPDF);
		
		}catch (CmdLineParser.OptionException e) {
			System.out.println("2@helpcom@EL LIBRO NO SE GENERÓ CORRECTAMENTE ERROR: "+e.getMessage()+"@helpcom@<br>SIN RUTA");
			System.exit(1);
		}
	}
}