
package cl.helpcom.itext;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import javax.print.PrintException;
import cl.helpcom.dte.util.TipoDocumento;
import cl.helpcom.recursos.LectorFichero;
import cl.helpcom.recursos.Validador;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BarcodePDF417;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.LineSeparator;

public class PDFTermica {
	private Properties propiedades = new Properties();
    private InputStream entrada = null;
    private Validador validador = new Validador();

    public void creaPDF(String archivoSalida,ArrayList<String> arrayEncabezado,ArrayList<ArrayList<String>> arrayDetalle,ArrayList<ArrayList<String>> arrayReferencia,ArrayList<ArrayList<String>> arrayDscGlobal,String rutaDTE, int cItem,String cedible) throws IOException, DocumentException, PrintException{
    	
        entrada = new FileInputStream("/usr/local/F_E/Configuraciones/Propiedades/configuracionCreaDTE.properties");
        // cargamos el archivo de propiedades
        propiedades.load(entrada);

        /**PRUEBA**/
        String rutaLogo = propiedades.getProperty("RUTA_LOGO").toString();
    	
    	DecimalFormat formato = new DecimalFormat("#,###.##");
    	String rutE=arrayEncabezado.get(11);
        String [] rutEmisor=rutE.split("-");
        String tipoDTE = this.setTipoDocumento(arrayEncabezado.get(0));//TIPO DTE
        
        
        Document document = new Document();
        
        try{
            PdfWriter.getInstance(document, new FileOutputStream(archivoSalida));
            document.setMargins(0, 0, 0, 0);
            document.open();

            PdfPTable table = new PdfPTable(1);
            float[] f = new float[1];
            float[] f2 = new float[4];
            float[] f3 = new float[3];
            f[0]=200;
            table.setTotalWidth(f);
            table.setLockedWidth(true);
            
            Font font = new Font(FontFamily.HELVETICA, 12, Font.BOLD);
            Font font2 = new Font(FontFamily.HELVETICA, 10,Font.BOLD);
            Font font3 = new Font(FontFamily.HELVETICA, 8);
            Font font4 = new Font(FontFamily.HELVETICA, 8,Font.BOLD);
            
            Paragraph wrong = new Paragraph("R.U.T.: "+formato.format(Double.parseDouble(rutEmisor[0]))+ "- " + rutEmisor[1]+"\n\n"+tipoDTE+"\n\nN° "+arrayEncabezado.get(1),font);
            PdfPCell celdaFinal = new PdfPCell(new Paragraph(wrong));
            celdaFinal.setHorizontalAlignment(Element.ALIGN_CENTER);//
            celdaFinal.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celdaFinal.setBorderWidth (2F);
            celdaFinal.setUseBorderPadding(true);
            table.setHorizontalAlignment(0);
            celdaFinal.setColspan(1);
            table.addCell(celdaFinal);            
            document.add(table);
            
            PdfPTable table2 = new PdfPTable(1);//Datos Emisor
            PdfPTable table3 = new PdfPTable(1);//Datos Receptor
            PdfPTable table3_1 = new PdfPTable(1);//Datos Referencia
            PdfPTable table4 = new PdfPTable(4);
            PdfPTable table5 = new PdfPTable(3);
            PdfPTable table5_1 = new PdfPTable(1);
            PdfPTable table6 = new PdfPTable(1);
            PdfPTable table7 = new PdfPTable(1);
            
            
            f[0]=200;//ancho table
            f2[0]=0.3f;
            f2[1]=0.6f;
            f2[2]=0.4f;//ancho table
            f2[3]=1.8f;//ancho table
            f3[0]=0.5f;
            f3[1]=0.8f;
            f3[2]=1.75f;//ancho table
            
            
            table2.setTotalWidth(f);
            table2.setLockedWidth(true);
            table3.setTotalWidth(f);
            table3.setLockedWidth(true);
            table3_1.setTotalWidth(f);
            table3_1.setLockedWidth(true);
            table4.setTotalWidth(f2);
            table4.setLockedWidth(false);
            table5.setTotalWidth(f3);
            table5.setLockedWidth(false);
            table5_1.setTotalWidth(f);
            table5_1.setLockedWidth(true);
            table6.setTotalWidth(f);
            table6.setLockedWidth(true);
            table7.setTotalWidth(f);
            table7.setLockedWidth(true);
//            table4.setLockedWidth(true);
            
            //PALMA E HIJOS
            Paragraph wrong2;
            if (arrayEncabezado.get(11).equals("83943200-3")){
            	wrong2 = new Paragraph("S.I.I - VICTORIA",font);
            }else{
            	wrong2 = new Paragraph("S.I.I - "+ arrayEncabezado.get(19),font);
            }
            Image imagen = Image.getInstance(rutaLogo);
            Paragraph wrong4 = new Paragraph(arrayEncabezado.get(12),font2);/**Razon Social Emisor**/
            Paragraph wrong5 = new Paragraph("Giro: "+arrayEncabezado.get(13),font3);/**Giro Emisor**/
            Paragraph wrong6 = new Paragraph("Dirección: "+arrayEncabezado.get(17),font3);
            Paragraph wrong7 = new Paragraph("Ciudad: "+arrayEncabezado.get(18),font3);
            Paragraph wrong8 = new Paragraph("Comuna: "+arrayEncabezado.get(19)+"\n",font3);
//            Paragraph wrong3 = new Paragraph("IMAGEN");
            
            PdfPCell celdaFinal2 = new PdfPCell(new Paragraph(wrong2));
            PdfPCell celdaFinal3 = new PdfPCell();
            PdfPCell celdaFinal4 = new PdfPCell(new Paragraph(wrong4));
            PdfPCell celdaFinal5 = new PdfPCell(new Paragraph(wrong5));
            PdfPCell celdaFinal6 = new PdfPCell(new Paragraph(wrong6));
            PdfPCell celdaFinal7 = new PdfPCell(new Paragraph(wrong7));
            PdfPCell celdaFinal8 = new PdfPCell(new Paragraph(wrong8));
            imagen.scaleAbsolute(60, 60); //dimension de logo fijo999999
            imagen.setAlignment(Image.ALIGN_CENTER);
            celdaFinal3.addElement(imagen);
            
            celdaFinal2.setBorder(Rectangle.NO_BORDER);
            celdaFinal3.setBorder(Rectangle.NO_BORDER);
            celdaFinal4.setBorder(Rectangle.NO_BORDER);
            celdaFinal5.setBorder(Rectangle.NO_BORDER);
            celdaFinal6.setBorder(Rectangle.NO_BORDER);
            celdaFinal7.setBorder(Rectangle.NO_BORDER);
            celdaFinal8.setBorder(Rectangle.NO_BORDER);
            
            celdaFinal2.setHorizontalAlignment(Element.ALIGN_CENTER);//
            celdaFinal2.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            celdaFinal3.setHorizontalAlignment(Element.ALIGN_CENTER);//
            celdaFinal3.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celdaFinal4.setHorizontalAlignment(Element.ALIGN_LEFT);//
            
            table2.setHorizontalAlignment(0);
            table3.setHorizontalAlignment(0);
            table3_1.setHorizontalAlignment(0);
            table4.setHorizontalAlignment(0);
            table5.setHorizontalAlignment(0);
            table5_1.setHorizontalAlignment(0);
            table6.setHorizontalAlignment(0);
            table7.setHorizontalAlignment(0);
            
//            celdaFinal2.setColspan(2);
//            celdaFinal2.setColspan(3);
            
            table2.addCell(celdaFinal2);
            table2.addCell(celdaFinal3);
            table2.addCell(celdaFinal4);
            table2.addCell(celdaFinal5);
            table2.addCell(celdaFinal6);
            table2.addCell(celdaFinal7);
            table2.addCell(celdaFinal8);
            
            document.add(table2);
//            document.add(new LineSeparator());
//            document.add(new DottedLineSeparator());
            document.add(new Paragraph(" ")); 
            
            Paragraph wrong9 = new Paragraph("\nSeñor (es): "+arrayEncabezado.get(22),font3);/**Razon Social Receptor**/
            Paragraph wrong10 = new Paragraph("Giro: "+arrayEncabezado.get(23),font3);/**Giro Receptor**/
            Paragraph wrong14 = new Paragraph("Rut: "+arrayEncabezado.get(21),font3);/**Rut Receptor**/
            Paragraph wrong11 = new Paragraph("Dirección: "+arrayEncabezado.get(26),font3);/**Direccion**/
            Paragraph wrong12 = new Paragraph("Ciudad: "+arrayEncabezado.get(27),font3);/**Comuna**/
            Paragraph wrong13 = new Paragraph("Comuna: "+arrayEncabezado.get(28),font3);/**Ciudad**/
            Paragraph wrong14_1 = new Paragraph("",font3);/**BLANCO**/
            Paragraph wrong14_2 = new Paragraph("Fecha de Emisión: "+arrayEncabezado.get(2),font4);/**Fecha Emision**/
            
            PdfPCell celdaFinal9 = new PdfPCell(new Paragraph(wrong9));
            PdfPCell celdaFinal10 = new PdfPCell(new Paragraph(wrong10));
            PdfPCell celdaFinal11 = new PdfPCell(new Paragraph(wrong11));
            PdfPCell celdaFinal12 = new PdfPCell(new Paragraph(wrong12));
            PdfPCell celdaFinal13 = new PdfPCell(new Paragraph(wrong13));
            PdfPCell celdaFinal14 = new PdfPCell(new Paragraph(wrong14));
            PdfPCell celdaFinal14_1 = new PdfPCell(new Paragraph(wrong14_1));
            PdfPCell celdaFinal14_2 = new PdfPCell(new Paragraph(wrong14_2));
            
            celdaFinal9.setBorder(Rectangle.NO_BORDER);
            celdaFinal10.setBorder(Rectangle.NO_BORDER);
            celdaFinal11.setBorder(Rectangle.NO_BORDER);
            celdaFinal12.setBorder(Rectangle.NO_BORDER);
            celdaFinal13.setBorder(Rectangle.NO_BORDER);
            celdaFinal14.setBorder(Rectangle.NO_BORDER);
            celdaFinal14_1.setBorder(Rectangle.NO_BORDER);
            celdaFinal14_2.setBorder(Rectangle.NO_BORDER);
            
            celdaFinal9.setHorizontalAlignment(Element.ALIGN_LEFT);//
            celdaFinal10.setHorizontalAlignment(Element.ALIGN_LEFT);//
            celdaFinal11.setHorizontalAlignment(Element.ALIGN_LEFT);//
            celdaFinal12.setHorizontalAlignment(Element.ALIGN_LEFT);//
            celdaFinal13.setHorizontalAlignment(Element.ALIGN_LEFT);//
            celdaFinal14.setHorizontalAlignment(Element.ALIGN_LEFT);//
            celdaFinal14_2.setHorizontalAlignment(Element.ALIGN_LEFT);//
            table3.addCell(celdaFinal9);
            table3.addCell(celdaFinal10);
            table3.addCell(celdaFinal14);
            table3.addCell(celdaFinal11);
            table3.addCell(celdaFinal12);
            table3.addCell(celdaFinal13);
            table3.addCell(celdaFinal14_1);
            table3.addCell(celdaFinal14_2);
            
            document.add(table3);
            document.add(new Paragraph(" "));
            
            if (arrayReferencia.size()>0){
            	Paragraph wrong_ref = new Paragraph("Documentos de Referencia\n.",font4);
            	document.add(wrong_ref);
            	document.add(new LineSeparator());
            	
            	Paragraph wrong_ref_1 = new Paragraph("Tipo Documento: "+arrayReferencia.get(0).get(1),font3);
            	Paragraph wrong_ref_2 = new Paragraph("Folio: "+arrayReferencia.get(0).get(3)+"         Fecha: "+arrayReferencia.get(0).get(5),font3);
            	Paragraph wrong_ref_4 = new Paragraph("Razon: "+arrayReferencia.get(0).get(7),font3);
            	
            	PdfPCell celdaRef_1 = new PdfPCell(new Paragraph(wrong_ref_1));
            	PdfPCell celdaRef_2 = new PdfPCell(new Paragraph(wrong_ref_2));
            	PdfPCell celdaRef_4 = new PdfPCell(new Paragraph(wrong_ref_4));
            	
            	celdaRef_1.setBorder(Rectangle.NO_BORDER);
            	celdaRef_2.setBorder(Rectangle.NO_BORDER);
            	celdaRef_4.setBorder(Rectangle.NO_BORDER);
            	
            	table3_1.addCell(celdaRef_1);
            	table3_1.addCell(celdaRef_2);
            	table3_1.addCell(celdaRef_4);
            
            	document.add(table3_1);
            }
            document.add(new LineSeparator());
            
            int cGrilla=1; double totalDescuento=0;;int tamTextDet=77;
            Paragraph wrong15_1_1 = new Paragraph("CANT.",font3);/**CANT**/
        	Paragraph wrong15_1_2 = new Paragraph("P. UNIT",font3);/**P UNIT**/
        	Paragraph wrong15_1_3 = new Paragraph("TOTAL",font3);/**TOTAL**/
        	Paragraph wrong15_1_4 = new Paragraph(" ",font3);/**EN BLANCO**/
        	Paragraph wrong15_1_5 = new Paragraph("PLU",font3);/**PLU**/
        	Paragraph wrong15_1_6 = new Paragraph("DESCRIPCION",font3);/**DETALLE**/        
        	PdfPCell celdaFinal15_1_1 = new PdfPCell(new Paragraph(wrong15_1_1));
        	PdfPCell celdaFinal15_1_2 = new PdfPCell(new Paragraph(wrong15_1_2));
        	PdfPCell celdaFinal15_1_3 = new PdfPCell(new Paragraph(wrong15_1_3));
        	PdfPCell celdaFinal15_1_4 = new PdfPCell(new Paragraph(wrong15_1_4));
        	PdfPCell celdaFinal15_1_5 = new PdfPCell(new Paragraph(wrong15_1_5));
        	PdfPCell celdaFinal15_1_6 = new PdfPCell(new Paragraph(wrong15_1_6));

        	celdaFinal15_1_6.setColspan(3);
        	
        	celdaFinal15_1_1.setBorder(Rectangle.NO_BORDER);
        	celdaFinal15_1_2.setBorder(Rectangle.NO_BORDER);
        	celdaFinal15_1_3.setBorder(Rectangle.NO_BORDER);
        	celdaFinal15_1_4.setBorder(Rectangle.NO_BORDER);
        	celdaFinal15_1_5.setBorder(Rectangle.NO_BORDER);
        	celdaFinal15_1_6.setBorder(Rectangle.NO_BORDER);
        	celdaFinal15_1_3.setHorizontalAlignment(Element.ALIGN_RIGHT);//
        	  table4.addCell(celdaFinal15_1_1);
              table4.addCell(celdaFinal15_1_2);
              table4.addCell(celdaFinal15_1_3);
              table4.addCell(celdaFinal15_1_4);
              table4.addCell(celdaFinal15_1_5);
              table4.addCell(celdaFinal15_1_6);
              
              document.add(table4);
              document.add(new LineSeparator());
              document.add(new Paragraph(" "));

              table4 = new PdfPTable(4);
              table4.setTotalWidth(f2);
              table4.setLockedWidth(false);
              table4.setHorizontalAlignment(0);
              
            for(int c =0; c<arrayDetalle.size();c++){
//            	Paragraph wrong15 = new Paragraph("\n"+arrayDetalle.get(c).get(1)+"\t     "+arrayDetalle.get(c).get(3)+"\t      "+arrayDetalle.get(c).get(5)+"\t     $"+formato.format(Double.parseDouble(arrayDetalle.get(c).get(7)))+"\t     $"+formato.format(Double.parseDouble(arrayDetalle.get(c).get(12))),font3);
//            	Paragraph wrong15_1 = new Paragraph(arrayDetalle.get(c).get(5),font3);/**CANT**/
            	Paragraph wrong15_1 = new Paragraph(formato.format(Double.parseDouble(arrayDetalle.get(c).get(5))),font3);/**CANT**/
            	Paragraph wrong15_2 = new Paragraph("$ "+formato.format(Double.parseDouble(arrayDetalle.get(c).get(7))),font3);/**P UNIT**/
            	Paragraph wrong15_3 = new Paragraph("$ "+formato.format(Double.parseDouble(arrayDetalle.get(c).get(12))),font3);/**TOTAL**/
            	Paragraph wrong15_4 = new Paragraph("",font3);/**EN BLANCO**/
            	Paragraph wrong15_5 = new Paragraph(arrayDetalle.get(c).get(1),font3);/**PLU**/
            	Paragraph wrong15_6 = new Paragraph(arrayDetalle.get(c).get(3),font3);/**DETALLE**/
            	Paragraph wrong15_7 = new Paragraph("",font3);/**EN BLANCO**/
            	Paragraph wrong15_8 = new Paragraph("",font3);/**EN BLANCO**/
            	
            	PdfPCell celdaFinal15_1 = new PdfPCell(new Paragraph(wrong15_1));
            	PdfPCell celdaFinal15_2 = new PdfPCell(new Paragraph(wrong15_2));
            	PdfPCell celdaFinal15_3 = new PdfPCell(new Paragraph(wrong15_3));
            	PdfPCell celdaFinal15_4 = new PdfPCell(new Paragraph(wrong15_4));
            	PdfPCell celdaFinal15_5 = new PdfPCell(new Paragraph(wrong15_5));
            	PdfPCell celdaFinal15_6 = new PdfPCell(new Paragraph(wrong15_6));
            	celdaFinal15_6.setColspan(3);
            	
            	celdaFinal15_1.setBorder(Rectangle.NO_BORDER);
            	celdaFinal15_2.setBorder(Rectangle.NO_BORDER);
            	celdaFinal15_3.setBorder(Rectangle.NO_BORDER);
            	celdaFinal15_4.setBorder(Rectangle.NO_BORDER);
            	celdaFinal15_5.setBorder(Rectangle.NO_BORDER);
            	celdaFinal15_6.setBorder(Rectangle.NO_BORDER);
            	celdaFinal15_3.setHorizontalAlignment(Element.ALIGN_RIGHT);//
                table4.addCell(celdaFinal15_1);
                table4.addCell(celdaFinal15_2);
                table4.addCell(celdaFinal15_3);
                table4.addCell(celdaFinal15_4);
                table4.addCell(celdaFinal15_5);
                table4.addCell(celdaFinal15_6);
              
            }
            
            document.add(table4);
            document.add(new LineSeparator());
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            
            
//        	Paragraph wrong16 = new Paragraph("\n\nMONTO NETO   $ "+formato.format(Double.parseDouble(arrayEncabezado.get(36)))+"\nIVA (19%)    $ "+formato.format(Double.parseDouble(arrayEncabezado.get(39)))+"\nESPECIFICO $ "+formato.format(Double.parseDouble(arrayEncabezado.get(44)))+"\nMONTO TOTAL  $ "+formato.format(Double.parseDouble(arrayEncabezado.get(46))),font3);
            Paragraph wrong16_1 = new Paragraph("NETO",font4);
            Paragraph wrong16_2 = new Paragraph("$ "+formato.format(Double.parseDouble(arrayEncabezado.get(36))),font4);
            Paragraph wrong16_3 = new Paragraph("",font4);
            Paragraph wrong16_1_1 = new Paragraph("EXENTO",font4);
            Paragraph wrong16_2_1 = new Paragraph("$ "+formato.format(Double.parseDouble(arrayEncabezado.get(37))),font4);
            Paragraph wrong16_3_1 = new Paragraph("",font4);
            Paragraph wrong16_4 = new Paragraph("IVA (19%)",font4);
            Paragraph wrong16_5 = new Paragraph("$ "+formato.format(Double.parseDouble(arrayEncabezado.get(39))),font4);
            Paragraph wrong16_6 = new Paragraph("",font4);
            Paragraph wrong16_7 = new Paragraph("ESPECIFICO",font4);
            Paragraph wrong16_8 = new Paragraph("$ "+formato.format(Double.parseDouble(arrayEncabezado.get(44))),font4);
            Paragraph wrong16_9 = new Paragraph("",font4);
            Paragraph wrong16_10 = new Paragraph("MONTO TOTAL",font4);
            Paragraph wrong16_11 = new Paragraph("$ "+formato.format(Double.parseDouble(arrayEncabezado.get(46))),font4);
            Paragraph wrong16_12 = new Paragraph("",font4);
            
        	PdfPCell celdaFinal16_1 = new PdfPCell(new Paragraph(wrong16_1));
        	PdfPCell celdaFinal16_2 = new PdfPCell(new Paragraph(wrong16_2));
        	PdfPCell celdaFinal16_3 = new PdfPCell(new Paragraph(wrong16_3));
        	
        	PdfPCell celdaFinal16_1_1 = new PdfPCell(new Paragraph(wrong16_1_1));
        	PdfPCell celdaFinal16_2_1 = new PdfPCell(new Paragraph(wrong16_2_1));
        	PdfPCell celdaFinal16_3_1 = new PdfPCell(new Paragraph(wrong16_3_1));
        	
        	PdfPCell celdaFinal16_4 = new PdfPCell(new Paragraph(wrong16_4));
        	PdfPCell celdaFinal16_5 = new PdfPCell(new Paragraph(wrong16_5));
        	PdfPCell celdaFinal16_6 = new PdfPCell(new Paragraph(wrong16_6));
        	
        	PdfPCell celdaFinal16_7 = new PdfPCell(new Paragraph(wrong16_7));
        	PdfPCell celdaFinal16_8 = new PdfPCell(new Paragraph(wrong16_8));
        	PdfPCell celdaFinal16_9= new PdfPCell(new Paragraph(wrong16_9));
        	
        	PdfPCell celdaFinal16_10 = new PdfPCell(new Paragraph(wrong16_10));
        	PdfPCell celdaFinal16_11 = new PdfPCell(new Paragraph(wrong16_11));
        	PdfPCell celdaFinal16_12 = new PdfPCell(new Paragraph(wrong16_12));

        	
//        	celdaFinal16_1.setHorizontalAlignment(Element.ALIGN_RIGHT);//
//        	celdaFinal16_4.setHorizontalAlignment(Element.ALIGN_RIGHT);//
//        	celdaFinal16_7.setHorizontalAlignment(Element.ALIGN_RIGHT);//
//        	celdaFinal16_10.setHorizontalAlignment(Element.ALIGN_RIGHT);//
        	
        	celdaFinal16_2.setHorizontalAlignment(Element.ALIGN_RIGHT);//
        	celdaFinal16_2_1.setHorizontalAlignment(Element.ALIGN_RIGHT);//
        	celdaFinal16_5.setHorizontalAlignment(Element.ALIGN_RIGHT);//
        	celdaFinal16_8.setHorizontalAlignment(Element.ALIGN_RIGHT);//
        	celdaFinal16_11.setHorizontalAlignment(Element.ALIGN_RIGHT);//
        	
        	celdaFinal16_1.setBorder(Rectangle.NO_BORDER);
        	celdaFinal16_2.setBorder(Rectangle.NO_BORDER);
        	celdaFinal16_1_1.setBorder(Rectangle.NO_BORDER);
        	celdaFinal16_2_1.setBorder(Rectangle.NO_BORDER);
        	celdaFinal16_3_1.setBorder(Rectangle.NO_BORDER);
        	celdaFinal16_3.setBorder(Rectangle.NO_BORDER);
        	celdaFinal16_4.setBorder(Rectangle.NO_BORDER);
        	celdaFinal16_5.setBorder(Rectangle.NO_BORDER);
        	celdaFinal16_6.setBorder(Rectangle.NO_BORDER);
        	celdaFinal16_7.setBorder(Rectangle.NO_BORDER);
        	celdaFinal16_8.setBorder(Rectangle.NO_BORDER);
        	celdaFinal16_9.setBorder(Rectangle.NO_BORDER);
        	celdaFinal16_10.setBorder(Rectangle.NO_BORDER);
        	celdaFinal16_11.setBorder(Rectangle.NO_BORDER);
        	celdaFinal16_12.setBorder(Rectangle.NO_BORDER);
        	
        	int neto=Integer.valueOf(arrayEncabezado.get(36));
        	if (neto>0){
        		table5.addCell(celdaFinal16_1);
            	table5.addCell(celdaFinal16_2);
            	table5.addCell(celdaFinal16_3);	
        	}
        	int exento=Integer.valueOf(arrayEncabezado.get(37));
        	if (exento>0){
        		table5.addCell(celdaFinal16_1_1);
        		table5.addCell(celdaFinal16_2_1);
        		table5.addCell(celdaFinal16_3_1);
        	}
        	
        	table5.addCell(celdaFinal16_4);
        	table5.addCell(celdaFinal16_5);//IVA
        	table5.addCell(celdaFinal16_6);
        	
        	int especifico=Integer.valueOf(arrayEncabezado.get(44));
        	if (especifico>0){
	        	table5.addCell(celdaFinal16_7);
	        	table5.addCell(celdaFinal16_8);
	        	table5.addCell(celdaFinal16_9);
        	}
        	table5.addCell(celdaFinal16_10);
        	table5.addCell(celdaFinal16_11);
        	table5.addCell(celdaFinal16_12);
        	
        	document.add(table5);
        	
        	Paragraph wrong17_1 = new Paragraph("\n"+arrayEncabezado.get(47)+" PESOS",font3);
        	PdfPCell celdaFinal17_1 = new PdfPCell(new Paragraph(wrong17_1));
        	celdaFinal17_1.setBorder(Rectangle.NO_BORDER);
        	table5_1.addCell(celdaFinal17_1);
        	
        	document.add(table5_1);
        	
        	Paragraph wrong23 = new Paragraph("Nombre: ____________________________________ \n\nR.U.T: _______________ Firma: _________________ \n\nFecha: ______________ Recinto: ________________\n.",font3);
        	Paragraph wrong24 = new Paragraph("El acuse de recibo que se declara en este acto, de acuerdo a lo dispuesto en la letra b) del Art. 4°, y la letra c) del Art. 5° de la Ley 19.983, acredita que la entrega de mercaderías o servicio (s) prestado (s) ha (n) sido recibido (s)",font3);
        	PdfPCell celdaFinal23 = new PdfPCell(new Paragraph(wrong23));
        	PdfPCell celdaFinal24 = new PdfPCell(new Paragraph(wrong24));
        	table7.addCell(celdaFinal23);
        	table7.addCell(celdaFinal24);
        	
        	//escribe tabla cedible
        	if (cedible.length()>0){
        		document.add(table7);
        	}
        	
        	LeerXML getTED = new LeerXML();
        	
            BarcodePDF417 pdf417 = new BarcodePDF417();
            pdf417.setCodeRows(5);
            pdf417.setCodeColumns(18);
            pdf417.setErrorLevel(5);
            pdf417.setLenCodewords(999);
            pdf417.setOptions(BarcodePDF417.PDF417_FORCE_BINARY);
            pdf417.setText(getTED.obtenerTED(rutaDTE).getBytes("ISO-8859-1"));
            com.itextpdf.text.Image img = pdf417.getImage();
            img.scaleAbsolute(200, 78);            
            
            document.add(img);

            Paragraph wrong19 = new Paragraph("\nTimbre Electrónico SII",font3);
            Paragraph wrong20 = new Paragraph("Res. Nro "+/*propiedades.getProperty("NRO_RESOLUCION")*/80 +" de "+ 2016/*propiedades.getProperty("FCH_RESOLUCION").substring(0, 4)*/,font3);
            Paragraph wrong21 = new Paragraph("Verifique su documento en www.sii.cl",font3);
            Paragraph wrong22 = new Paragraph("\n",font4);
            
            if (cedible.length()>0){
            	wrong22 = new Paragraph("\n"+cedible,font4);
            }

            PdfPCell celdaFinal19 = new PdfPCell(new Paragraph(wrong19));
            PdfPCell celdaFinal20 = new PdfPCell(new Paragraph(wrong20));
            PdfPCell celdaFinal21 = new PdfPCell(new Paragraph(wrong21));
            PdfPCell celdaFinal22 = new PdfPCell(new Paragraph(wrong22));
            celdaFinal19.setBorder(Rectangle.NO_BORDER);
            celdaFinal20.setBorder(Rectangle.NO_BORDER);
            celdaFinal21.setBorder(Rectangle.NO_BORDER);
            celdaFinal22.setBorder(Rectangle.NO_BORDER);
            celdaFinal19.setHorizontalAlignment(Element.ALIGN_CENTER);//
            celdaFinal20.setHorizontalAlignment(Element.ALIGN_CENTER);//
            celdaFinal21.setHorizontalAlignment(Element.ALIGN_CENTER);//
            celdaFinal22.setHorizontalAlignment(Element.ALIGN_RIGHT);//
            
            table6.addCell(celdaFinal19);
            table6.addCell(celdaFinal20);
            table6.addCell(celdaFinal21);
            table6.addCell(celdaFinal22);
            
            
            document.add(table6);
            document.close();             
            
        }catch(Exception e)
        {
            System.err.println("Ocurrio un error al crear el archivo "+ e);
            System.exit(-1);
        }
        
        
        
/**PRUEBA**/
   
    }

    public String setTipoDocumento(String tipoDTE) throws UnsupportedEncodingException{

        String tipo="";

        if (tipoDTE.equals("33")){
            //tipo = "FACTURA ELECTRÓNICA";
        	tipo =  new String("FACTURA ELECTRÓNICA".getBytes(), "ISO-8859-1");
        }else if (tipoDTE.equals("56")){
            //tipo = "NOTA DE DÉBITO ELECTRÓNICA";
        	tipo =  new String("NOTA DE DÉBITO ELECTRÓNICA".getBytes(), "ISO-8859-1");
        }else if (tipoDTE.equals("61")){
            //tipo = "NOTA DE CRÉDITO ELECTRÓNICA";
            tipo =  new String("NOTA DE CRÉDITO ELECTRÓNICA".getBytes(), "ISO-8859-1");
        }else if (tipoDTE.equals("34")){
            //tipo = "NOTA DE CRÉDITO ELECTRÓNICA";
            tipo =  new String("FACTURA NO AFECTA O EXENTA ELECTRÓNICA".getBytes(), "ISO-8859-1");
        }else if (tipoDTE.equals("52")){
            //tipo = "NOTA DE CRÉDITO ELECTRÓNICA";
            tipo =  new String("GUÍA DE DESPACHO ELECTRÓNICA".getBytes(), "ISO-8859-1");
        }

        return tipo;
    }

}