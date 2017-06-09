package cl.helpcom.dte.method;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;

import cl.helpcom.dte.util.AgregarCamposDTE;
import cl.helpcom.dte.util.Timbre;
import cl.helpcom.recursos.LectorDTEText;
import cl.helpcom.recursos.LectorFichero;
import cl.helpcom.recursos.Validador;
import cl.sii.siiDte.DTEDocument;

public class CreaDTEv3 {

	private String documento="";
	private LectorFichero ficheroBase = new LectorFichero();
	private Boolean zetaFin;


	public void crearDTE() throws IOException, ParseException, XmlException{
		Properties propiedades = new Properties();
		InputStream entrada = null;
		entrada = new FileInputStream("/usr/local/F_E/Configuraciones/Propiedades/configuracionCreaDTE.properties");
		// cargamos el archivo de propiedades
		propiedades.load(entrada);

//		Validador val = new Validador();
		ArrayList<String> arrayEncabezado = new ArrayList<String>();
		ArrayList<ArrayList<String>> arrayDetalle = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> arrayReferencia = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> arrayDescRecGlob = new ArrayList<ArrayList<String>>();

		LectorDTEText lector = new LectorDTEText();

		//Llenar arreglos
		arrayEncabezado=lector.formatoA_DTEs(this.getDocumento(), "A",arrayEncabezado);
		arrayDetalle=lector.formatoB_DTEs(this.getDocumento(), arrayDetalle, "B");
		arrayDescRecGlob=lector.formatoB_DTEs(this.getDocumento(), arrayDescRecGlob, "C");
		arrayReferencia=lector.formatoB_DTEs(this.getDocumento(), arrayReferencia, "D");

		String baseRespaldoTXT = propiedades.getProperty("RUTA_BASE_RESPALDO");
		String nombreDocPlano= this.getDocumento().substring(1+this.getDocumento().lastIndexOf('/'));

		ficheroBase.moverFichero(this.getDocumento(), baseRespaldoTXT+"/");
		// Borra directorio Base
//		ficheroBase.borraDocumentoTXT(baseTXT);

		//Validar campos obligatorios
		//Quitados por problemas de INDEX SIZE
//		val.validaEntradaEncabezado(arrayEncabezado);
//		val.validaEntradaDetalles(arrayDetalle, lector.getCantidadDetalles());
//		val.validaEntradaReferencia(arrayReferencia, lector.getCantidadReferencias());

		// Crear Archivo temporal
		ficheroBase.crearTemporal(propiedades.getProperty("RUTA_TEMPORAL"));

		/* XML FACTURA */
		String resultS = propiedades.getProperty("RUTA_RESULTADO_DTE").toString();

		String folioS = arrayEncabezado.get(1); // Folio autorizado por SII para ocupar
		String tipoDocS = arrayEncabezado.get(0);//
		DTEDocument doc;
		AgregarCamposDTE addCampos = new AgregarCamposDTE();
		int tipoDoc = Integer.valueOf(tipoDocS);
		int folio = Integer.valueOf(folioS);

		doc = DTEDocument.Factory.newInstance();
		doc.addNewDTE();
		doc.getDTE().setVersion(new BigDecimal("1.0"));
		doc.getDTE().addNewDocumento();
		doc.getDTE().getDocumento().addNewEncabezado();

		//Agregar EMISOR
		doc.getDTE().getDocumento().getEncabezado().setEmisor(addCampos.addEmisor(arrayEncabezado));
		//Agregar IDDOC
		addCampos.addIdDoc(doc, arrayEncabezado);
		//Agregar RECEPTOR
		addCampos.addReceptor(doc, arrayEncabezado);
		//Agregar TOTALES
		addCampos.addTotales(doc, arrayEncabezado);

		// Descuento recargo globals
		// Obtiene la cantidad de Items que tiene el detalle
		int cantidadItem = lector.getCantidadDetalles();

		//Agregar DOC33
		if (Double.valueOf(arrayEncabezado.get(0)) == 33 || Double.valueOf(arrayEncabezado.get(0)) == 34 || Double.valueOf(arrayEncabezado.get(0)) == 52) {
			doc.getDTE().getDocumento().setDetalleArray(addCampos.addDTE33(doc, arrayEncabezado, arrayDetalle)); // Agrega Detalles
			doc.getDTE().getDocumento().setDscRcgGlobalArray(addCampos.addDscRecGlobal(arrayDescRecGlob));// Agrega Dsc Global
			doc.getDTE().getDocumento().setReferenciaArray(addCampos.addReferencia(arrayReferencia));// Agrega Referencias

		} else if (Double.valueOf(arrayEncabezado.get(0)) == 61) {
			doc.getDTE().getDocumento().setDetalleArray(addCampos.addDTE61(arrayEncabezado, arrayDetalle)); // Agrega Detalles
			doc.getDTE().getDocumento().setDscRcgGlobalArray(addCampos.addDscRecGlobal(arrayDescRecGlob));// Agrega Dsc Global
			doc.getDTE().getDocumento().setReferenciaArray(addCampos.addReferencia(arrayReferencia));// Agrega Referencias

		} else if (Double.valueOf(arrayEncabezado.get(0)) == 56) {
			doc.getDTE().getDocumento().setDetalleArray(addCampos.addDTE56(arrayEncabezado, arrayDetalle)); // Agrega Detalles
			doc.getDTE().getDocumento().setDscRcgGlobalArray(addCampos.addDscRecGlobal(arrayDescRecGlob));// Agrega Dsc Global
			doc.getDTE().getDocumento().setReferenciaArray(addCampos.addReferencia(arrayReferencia));// Agrega Referencias

		}
		

		// Leo la autorizacion y timbro
		// Debo meter el namespace porque SII no lo genera
		HashMap<String, String> namespaces = new HashMap<String, String>();
		namespaces.put("", "http://www.sii.cl/SiiDte");
		XmlOptions opts = new XmlOptions();
		opts.setLoadSubstituteNamespaces(namespaces);

		opts = new XmlOptions();
		opts.setSaveImplicitNamespaces(namespaces);
		opts.setLoadSubstituteNamespaces(namespaces);
		opts.setSavePrettyPrint();
		opts.setSavePrettyPrintIndent(0);

		doc = DTEDocument.Factory.parse(doc.newInputStream(opts), opts);
		// doc.getDTE().timbrar(auth.getCAF(), auth.getPrivateKey(null));

		opts = new XmlOptions();
		opts.setCharacterEncoding("ISO-8859-1");
		opts.setSaveImplicitNamespaces(namespaces);

		try {
			// CARPETA AUXILIAR DTE XML SIN TIMBRE
			doc.save(new File(resultS + "/sinTimbrar/" + arrayEncabezado.get(0) + arrayEncabezado.get(1)+ ".xml"),opts);
		} catch (Exception e) {
			System.err.print("Ha surgido un error al Guardar el documento DTE \nNo se ha guardado el documento");
		}
		//Timbra documento y almacena respuesta BD
		Timbre tim = new Timbre();
		tim.timbrarDTE(Integer.valueOf(arrayEncabezado.get(0)),Integer.valueOf(arrayEncabezado.get(1)),arrayEncabezado,arrayDetalle,arrayReferencia,arrayDescRecGlob,nombreDocPlano);
		// Borra fichero temporal
		ficheroBase.borrarTemporal(propiedades.getProperty("RUTA_TEMPORAL"));
	}

	public void leerZ() throws IOException{

		Properties propiedades = new Properties();
		InputStream entrada = null;
		entrada = new FileInputStream("/usr/local/F_E/Configuraciones/Propiedades/configuracionCreaDTE.properties");
		// cargamos el archivo de propiedades
				propiedades.load(entrada);
				// Leer ficheros Bases
				String baseTXT = ficheroBase.leerDocumentoTXT(propiedades.getProperty("RUTA_BASE"));
				System.out.println(baseTXT);
				this.setDocumento(baseTXT);
				LectorDTEText lector = new LectorDTEText();
				lector.formatoZ(this.getDocumento());
				setZetaFin(lector.getZeta());
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String docu) {
		this.documento = docu;
	}

	public Boolean getZetaFin() {
		return zetaFin;
	}



	public void setZetaFin(Boolean zetaFin) {
		this.zetaFin = zetaFin;
	}


}
