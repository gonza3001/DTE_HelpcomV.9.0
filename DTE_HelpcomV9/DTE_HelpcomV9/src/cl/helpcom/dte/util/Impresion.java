package cl.helpcom.dte.util;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Sides;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.jpedal.PdfDecoder;

public class Impresion {

	/**
	 * Retrieve the specified Print Service; will return null if not found.
	 * @return
	 * @throws PrintException
	 * @throws IOException
	 */


	/**
	 *
	 * Imprime documento
	 * @param impresora
	 * @param documento
	 * @throws PrintException
	 * @throws IOException
	 */
	public void imprimirPDF (String impresora, String documento) throws PrintException, IOException{

		DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PAGEABLE;
	    PrintRequestAttributeSet patts = new HashPrintRequestAttributeSet();
	    patts.add(Sides.DUPLEX);
	    PrintService[] ps = PrintServiceLookup.lookupPrintServices(flavor, patts);
	    if (ps.length == 0) {
	        throw new IllegalStateException("No Printer found");
	    }
	    //System.out.println("Available printers: " + Arrays.asList(ps));
	    PrintService myService = null;
	    for (PrintService printService : ps) {
	        if (printService.getName().equals(impresora)) {
	            myService = printService;
	            break;
	        }
	    }

	    if (myService == null) {
	        throw new IllegalStateException("Printer not found");
	    }

	    FileInputStream fis = new FileInputStream(documento);
	    Doc pdfDoc = new SimpleDoc(fis, DocFlavor.INPUT_STREAM.AUTOSENSE, null);
	    DocPrintJob printJob = myService.createPrintJob();
	    printJob.print(pdfDoc, new HashPrintRequestAttributeSet());
	    fis.close();
	}



}
