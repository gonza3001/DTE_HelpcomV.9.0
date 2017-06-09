package cl.helpcom.pdf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

import com.itextpdf.text.DocumentException;

import cl.helpcom.itext.PDFTermica;
import cl.helpcom.recursos.LectorDTEText;
import cl.helpcom.recursos.LectorFichero;


public class ImpresoraTermica {
	private Properties propiedades = new Properties();
    private InputStream entrada = null;
	
    public void imprimirTicket(String arbolCarpeta,ArrayList<String> arrayEncabezado,ArrayList<ArrayList<String>> arrayDetalle,ArrayList<ArrayList<String>>arrayReferencia, ArrayList<ArrayList<String>> arrayDescRecGlob,String rutaDTE, int cantDetalle,String cedible) throws IOException, DocumentException, PrintException{
		
        try {
			entrada = new FileInputStream("/usr/local/F_E/Configuraciones/Propiedades/configuracionCreaDTE.properties");
			// cargamos el archivo de propiedades
	        propiedades.load(entrada);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}        
		PDFTermica creaPDFTermica = new PDFTermica();
		
		String archivoSalida = arbolCarpeta+"/"+arrayEncabezado.get(0)+arrayEncabezado.get(1)+".pdf";
		
		creaPDFTermica.creaPDF(archivoSalida, arrayEncabezado, arrayDetalle, arrayReferencia, arrayDescRecGlob,rutaDTE, arrayDetalle.size(), cedible);
		
		FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(archivoSalida);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (inputStream == null) {
            return;
        }
 
        DocFlavor docFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
        Doc document = new SimpleDoc(inputStream, docFormat, null);
 
        PrintRequestAttributeSet attributeSet = new HashPrintRequestAttributeSet();
 
        PrintService defaultPrintService = PrintServiceLookup.lookupDefaultPrintService();
 
 
        if (defaultPrintService != null) {
            DocPrintJob printJob = defaultPrintService.createPrintJob();
            try {
                printJob.print(document, attributeSet);
 
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("No existen impresoras instaladas");
        }
 
        inputStream.close();
	
	}
}
