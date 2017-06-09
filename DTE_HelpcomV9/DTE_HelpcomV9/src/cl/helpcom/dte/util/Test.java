package cl.helpcom.dte.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
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

import cl.helpcom.recursos.LectorDTEText;
import cl.nic.dte.util.Utilities;


public class Test {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		LectorDTEText dteText = new LectorDTEText();
		String terceroSinDS="";
		
//		terceroSinDS=dteText.reemplazarTexto("/home/mau/Documentos/AUXILIAR/33189433.xml");
		dteText.escribirArchivo("/home/mau/Documentos/AUXILIAR/33189433.xml", terceroSinDS);
		
}}
