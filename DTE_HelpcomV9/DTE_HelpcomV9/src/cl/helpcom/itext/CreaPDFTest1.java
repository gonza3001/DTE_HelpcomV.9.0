
package cl.helpcom.itext;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import javax.print.PrintException;

import cl.helpcom.dte.util.Impresion;
import cl.helpcom.dte.util.TipoDocumento;
import cl.helpcom.recursos.Formato;
import cl.helpcom.recursos.Validador;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BarcodePDF417;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class CreaPDFTest1 {
	private Properties propiedades = new Properties();
    private InputStream entrada = null;
    private Validador validador = new Validador();

    public void creaPDF(String pdfTempleStr,String archivoSalida,ArrayList<String> arrayEncabezado,ArrayList<ArrayList<String>> arrayDetalle,ArrayList<ArrayList<String>> arrayReferencia,ArrayList<ArrayList<String>> arrayDscGlobal,String rutaDTE, int cItem,String cedible) throws IOException, DocumentException, PrintException{

        entrada = new FileInputStream("/usr/local/F_E/Configuraciones/Propiedades/configuracionCreaDTE.properties");

        // cargamos el archivo de propiedades
        propiedades.load(entrada);
        archivoSalida = archivoSalida+"/"+arrayEncabezado.get(0)+arrayEncabezado.get(1)+".pdf";
        String tipoDTE = this.setTipoDocumento(arrayEncabezado.get(0));

        PdfReader pdfTemplate = new PdfReader(pdfTempleStr);//Entrada3
        FileOutputStream pdfSalida = new FileOutputStream(archivoSalida);//Salida
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfStamper stamper = new PdfStamper(pdfTemplate, pdfSalida);
        stamper.setFormFlattening(true);

        DecimalFormat formato = new DecimalFormat("#,###.###");
        Formato formatoTxt = new Formato();

        //Info Emisor InfoEmisor
        String nomEmpresa = new String(arrayEncabezado.get(12).getBytes(), "ISO-8859-1");
        stamper.getAcroFields().setField("nomEmpresa", nomEmpresa);
        String giroEmpresa = new String(arrayEncabezado.get(13).getBytes(), "ISO-8859-1");
        stamper.getAcroFields().setField("giroEmpresa", giroEmpresa);
        String direccionEmisor= new String(arrayEncabezado.get(17).getBytes(), "ISO-8859-1");
        stamper.getAcroFields().setField("direccionEmisor", direccionEmisor);
        String comunaEmisor= new String(arrayEncabezado.get(18).getBytes(), "ISO-8859-1");
        stamper.getAcroFields().setField("comunaEmisor", comunaEmisor);

        String rutE=arrayEncabezado.get(11);
        String [] rutEmisor=rutE.split("-");
        stamper.getAcroFields().setField("rutEmisor", formato.format(Double.parseDouble(rutEmisor[0]))+ "- " + rutEmisor[1] );

        stamper.getAcroFields().setField("tipoDocumento", tipoDTE);
        stamper.getAcroFields().setField("folio", arrayEncabezado.get(1));
        stamper.getAcroFields().setField("comuna",  arrayEncabezado.get(18));
        stamper.getAcroFields().setField("locacionSI", arrayEncabezado.get(19));

        //Info Receptor
        stamper.getAcroFields().setField("fecha", validador.YYMMDDToDDMMYY(arrayEncabezado.get(2)));
        String nomReceptor= new String(arrayEncabezado.get(22).getBytes(), "ISO-8859-1");
        stamper.getAcroFields().setField("nomReceptor", nomReceptor);
        String direccionReceptor= new String(arrayEncabezado.get(26).getBytes(), "ISO-8859-1");
        stamper.getAcroFields().setField("direccionReceptor", direccionReceptor);
        String giroReceptor= new String(arrayEncabezado.get(23).getBytes(), "ISO-8859-1");
        stamper.getAcroFields().setField("giroReceptor", giroReceptor);

        String medPagoPDF= new String(arrayEncabezado.get(54).getBytes(), "ISO-8859-1");
        stamper.getAcroFields().setField("condicionesPago", medPagoPDF);
        stamper.getAcroFields().setField("codCliente", "");
        String rutR=arrayEncabezado.get(21);
        String [] rutReceptor=rutR.split("-");

        stamper.getAcroFields().setField("rutReceptor", formato.format(Double.parseDouble(rutReceptor[0]))+"-"+rutReceptor[1]);
        String comunaReceptor= new String(arrayEncabezado.get(27).getBytes(), "ISO-8859-1");
        stamper.getAcroFields().setField("comunaReceptor", comunaReceptor);
        String ciudadReceptor= new String(arrayEncabezado.get(28).getBytes(), "ISO-8859-1");
        stamper.getAcroFields().setField("ciudadReceptor", ciudadReceptor);
        String vendedor= new String(arrayEncabezado.get(20).getBytes(), "ISO-8859-1");
        if (vendedor.equals("0")){
        	stamper.getAcroFields().setField("vendedor", "");
        }else {
        	stamper.getAcroFields().setField("vendedor", vendedor);
		}

        if(arrayReferencia.size()>0){
        	int c=0;
        	Iterator<ArrayList<String>> it = arrayReferencia.iterator();
        	while (it.hasNext()) {
        		c++;
        		it.next();
        	}
//        	if (arrayReferencia.get(0).get(1).equals("SET") & c==1){
//        		//SET DE PRUEBA CON 1 REGISTRO
//        	}else{
        			for (int i = 0; i < c; i++) {
			            /*REFERENCIAS*/
			        	TipoDocumento tipoDocumento = new TipoDocumento();
			            stamper.getAcroFields().setField("docReferencia"+i, tipoDocumento.tipoDocumentoTexto(arrayReferencia.get(i).get(1)));
			            stamper.getAcroFields().setField("folioReferencia"+i, arrayReferencia.get(i).get(3));
			            stamper.getAcroFields().setField("fechaReferencia"+i, validador.YYMMDDToDDMMYY(arrayReferencia.get(i).get(5)));
			            String motivoReferencia= new String(arrayReferencia.get(i).get(7).getBytes(), "ISO-8859-1");
			            if (motivoReferencia.length()>46){
			            	stamper.getAcroFields().setField("motivoReferencia"+i, motivoReferencia.substring(i,45));
			            }else {
			            	stamper.getAcroFields().setField("motivoReferencia"+i, motivoReferencia);
						}
			            if (i>1){i=999;}
        			}
        }

        //Info Totales
        int cGrilla=1; double totalDescuento=0;;int tamTextDet=77;
        for(int c =0; c<arrayDetalle.size();c++){
        	if (arrayDetalle.get(c).get(3).length()>tamTextDet){//Si es mayor a 62
        		//PRIMERA LINEA
        			stamper.getAcroFields().setField("cant"+cGrilla,arrayDetalle.get(c).get(0));
//        			 String punitario=arrayDetalle.get(c).get(7);
//			            stamper.getAcroFields().setField("punitario"+cGrilla,formato.format(Double.parseDouble(punitario)));
        			String canti=arrayDetalle.get(c).get(5);
		            stamper.getAcroFields().setField("cantidad"+cGrilla,formato.format(Double.parseDouble(canti)));
		            stamper.getAcroFields().setField("cod"+cGrilla,arrayDetalle.get(c).get(1));
		            String detalleAux=new String (arrayDetalle.get(c).get(3).substring(0, tamTextDet).getBytes(), "ISO-8859-1");
		            stamper.getAcroFields().setField("det"+cGrilla,detalleAux);
		            stamper.getAcroFields().setField("mec"+cGrilla,arrayDetalle.get(c).get(10));

		            String detalle= new String(arrayDetalle.get(c).get(3).getBytes(), "ISO-8859-1");
		            String dtos=arrayDetalle.get(c).get(9);
		            double descuentos=Double.parseDouble(dtos);
		            totalDescuento=totalDescuento+descuentos;
		            String descuento="";
		            if (dtos=="0"){
			            	descuento=" 0%"+"  $0";
			            	stamper.getAcroFields().setField("dtos"+cGrilla,descuento);
			            }else {
			            	descuento=""+arrayDetalle.get(c).get(8)+"%"+"  $"+formato.format(Double.parseDouble(dtos));
			            	stamper.getAcroFields().setField("dtos"+cGrilla,descuento);
						}

			            String punitario=arrayDetalle.get(c).get(7);
			            stamper.getAcroFields().setField("punitario"+cGrilla,formato.format(Double.parseDouble(punitario)));
			            String totalGrid=arrayDetalle.get(c).get(12);
			            stamper.getAcroFields().setField("totalGrid"+cGrilla,formato.format(Double.parseDouble(totalGrid)));/**/

			            detalle=detalle.substring(tamTextDet);//resetear string
			     if (detalle.length()<tamTextDet){
			    	 	cGrilla++;
			    	 	stamper.getAcroFields().setField("cant"+cGrilla,"");
			            stamper.getAcroFields().setField("cantidad"+cGrilla,"");
			            stamper.getAcroFields().setField("cod"+cGrilla,"");
			            detalle= new String(detalle.getBytes(), "ISO-8859-1");
			            stamper.getAcroFields().setField("det"+cGrilla,detalle);
			            stamper.getAcroFields().setField("dtos"+cGrilla,"");
				        stamper.getAcroFields().setField("punitario"+cGrilla,"");
				        stamper.getAcroFields().setField("totalGrid"+cGrilla,"");/**/
			     }else{
			    	 while(detalle.length()>=tamTextDet){
			    		 cGrilla++;
			    		 stamper.getAcroFields().setField("cant"+cGrilla,"");
				         stamper.getAcroFields().setField("cantidad"+cGrilla,"");
				         stamper.getAcroFields().setField("cod"+cGrilla,"");
				         detalleAux=detalle;
				         detalleAux= new String(detalleAux.substring(0, tamTextDet).getBytes(), "ISO-8859-1");
				         stamper.getAcroFields().setField("det"+cGrilla,detalleAux);
				         stamper.getAcroFields().setField("dtos"+cGrilla,"");
					     stamper.getAcroFields().setField("punitario"+cGrilla,"");
					     stamper.getAcroFields().setField("totalGrid"+cGrilla,"");/**/
					     detalle=detalle.substring(tamTextDet);//resetear string

			    		 if (detalle.length()<=tamTextDet){
			    			 cGrilla++;
			    			 System.out.println("tercera fila"+detalle);
			    			 stamper.getAcroFields().setField("cant"+cGrilla,"");
					         stamper.getAcroFields().setField("cantidad"+cGrilla,"");
					         stamper.getAcroFields().setField("cod"+cGrilla,"");
					         detalle= new String(detalle.getBytes(), "ISO-8859-1");
					         stamper.getAcroFields().setField("det"+cGrilla,detalle);
					         stamper.getAcroFields().setField("dtos"+cGrilla,"");
						     stamper.getAcroFields().setField("punitario"+cGrilla,"");
						     stamper.getAcroFields().setField("totalGrid"+cGrilla,"");/**/
			    		 }
			    	 }
			     }
        	}else{
		            stamper.getAcroFields().setField("cant"+cGrilla,arrayDetalle.get(c).get(0));
		            String canti=arrayDetalle.get(c).get(5);
		            stamper.getAcroFields().setField("cantidad"+cGrilla,formato.format(Double.parseDouble(canti)));
		            stamper.getAcroFields().setField("cod"+cGrilla,arrayDetalle.get(c).get(1));
		            String detalle= new String(arrayDetalle.get(c).get(3).getBytes(), "ISO-8859-1");
		            stamper.getAcroFields().setField("det"+cGrilla,detalle);
		            stamper.getAcroFields().setField("mec"+cGrilla,arrayDetalle.get(c).get(10));
		            String dtos=arrayDetalle.get(c).get(9);
		            double descuentos=Double.parseDouble(dtos);
		            totalDescuento=totalDescuento+descuentos;
		            String descuento="";
		            if (dtos=="0"){
			            	descuento=" 0%"+"  $0";
			            	stamper.getAcroFields().setField("dtos"+cGrilla,descuento);
			            }else {
			            	descuento=""+arrayDetalle.get(c).get(8)+"%"+"  $"+formato.format(Double.parseDouble(dtos));
			            	stamper.getAcroFields().setField("dtos"+cGrilla,descuento);
						}

			            String punitario=arrayDetalle.get(c).get(7);
			            stamper.getAcroFields().setField("punitario"+cGrilla,formato.format(Double.parseDouble(punitario)));
			            String totalGrid=arrayDetalle.get(c).get(12);
			            stamper.getAcroFields().setField("totalGrid"+cGrilla,formato.format(Double.parseDouble(totalGrid)));/**/
		            }

		            cGrilla++;

        }




        stamper.getAcroFields().setField("totalEnTexto", arrayEncabezado.get(47).toUpperCase());
        //IVA Excepcionales

        if((arrayEncabezado.get(48).equals("-1"))){
        	stamper.getAcroFields().setField("ivaCarne","0");
        }else{
        	String ilaCarne =arrayEncabezado.get(48);
			String arrayILA[] = ilaCarne.split(";");
			stamper.getAcroFields().setField("ivaCarne",formato.format(Double.parseDouble(arrayILA[0])));
        }

        if((arrayEncabezado.get(49).trim().equals("-1"))){
        	stamper.getAcroFields().setField("ivaHarina","0");
        }else{
        	String ilaHarina =arrayEncabezado.get(49);
			String arrayILA[] = ilaHarina.split(";");
			stamper.getAcroFields().setField("ivaHarina",formato.format(Double.parseDouble(arrayILA[0])));
        }

        if((arrayEncabezado.get(50).trim().equals("-1"))){
        	stamper.getAcroFields().setField("ivaLicor","0");
        }else{
        	String ilaLicor =arrayEncabezado.get(50);
			String arrayILA[] = ilaLicor.split(";");
			stamper.getAcroFields().setField("ivaLicor",formato.format(Double.parseDouble(arrayILA[0])));
        }

        if((arrayEncabezado.get(51).trim().equals("-1"))){
        	stamper.getAcroFields().setField("ivaVino","0");
        }else{
        	String ilaVino =arrayEncabezado.get(51);
			String arrayILA[] = ilaVino.split(";");
			stamper.getAcroFields().setField("ivaVino",formato.format(Double.parseDouble(arrayILA[0])));
        }

        stamper.getAcroFields().setField("ivaCerveza","0");
//        if((arrayEncabezado.get(52).trim().equals("-1"))){
//        	stamper.getAcroFields().setField("ivaCerveza","0");
//        }else{
//        	String ilaVino =arrayEncabezado.get(52);
//			String arrayILA[] = ilaVino.split(";");
//			stamper.getAcroFields().setField("ivaCerveza",arrayILA[0]);
//        }

        if((arrayEncabezado.get(52).trim().equals("-1"))){
        	stamper.getAcroFields().setField("ivaSAzucar","0");
        }else{
        	String ilaSAzucar =arrayEncabezado.get(52);
			String arrayILA[] = ilaSAzucar.split(";");
			stamper.getAcroFields().setField("ivaSAzucar",formato.format(Double.parseDouble(arrayILA[0])));
        }

        if((arrayEncabezado.get(53).trim().equals("-1"))){
        	stamper.getAcroFields().setField("ivaCAzucar","0");
        }else{
        	String ilaCAzucar =arrayEncabezado.get(53);
			String arrayILA[] = ilaCAzucar.split(";");
			stamper.getAcroFields().setField("ivaCAzucar",formato.format(Double.parseDouble(arrayILA[0])));
        }

        //TOTALES
         //String totalDescuento=arrayEncabezado.get();
        if (!arrayDscGlobal.isEmpty()){
	        stamper.getAcroFields().setField("totalDescuento", formato.format(Double.parseDouble(arrayDscGlobal.get(0).get(4))));
        }else{
        	stamper.getAcroFields().setField("totalDescuento", "0");
        }

        String totalExento="";
        if((arrayEncabezado.get(37)).trim().equals("") || (arrayEncabezado.get(37)).trim().equals("0") ){
            totalExento="0";
        }else{
            totalExento=arrayEncabezado.get(37);
        }
        stamper.getAcroFields().setField("totalExento", formato.format(Double.parseDouble(totalExento)));

        String totalNeto="";
        if((arrayEncabezado.get(36)).trim().equals("") || (arrayEncabezado.get(36)).trim().equals("0") ){
            totalNeto="0";
        }else{
            totalNeto=arrayEncabezado.get(36);
        }
        stamper.getAcroFields().setField("totalNeto", formato.format(Double.parseDouble(totalNeto)));

        String totalImpAdicional="";
        if((arrayEncabezado.get(44)).trim().equals("") || (arrayEncabezado.get(44)).trim().equals("0") ){
            totalImpAdicional="0";
        }else{
            totalImpAdicional=arrayEncabezado.get(44);
        }
        stamper.getAcroFields().setField("impuestoAdicional", formato.format(Double.parseDouble(totalImpAdicional)));

        String totalImpuesto="";
        if((arrayEncabezado.get(44)).trim().equals("") || (arrayEncabezado.get(44)).trim().equals("0") ){
             totalImpuesto="0";
        }else{
             totalImpuesto=arrayEncabezado.get(44);
        }
        			//DATOS ADICIONALES MAQFRONT
        			if (arrayEncabezado.get(11).equals("78848330-9")){
        				//Si hay datos adicionales
        				if(!(arrayEncabezado.get(55)).trim().equals("0")){
        					String datoAdicional =arrayEncabezado.get(55);
        					String arrayDA[] = datoAdicional.split(";");
        					stamper.getAcroFields().setField("datoadic",formatoTxt.datoAdicionalMaqfront(arrayDA));
        				}
        			}
        			//DATOS ADICIONALES MONTANA y TERREMOTO
        			if (arrayEncabezado.get(11).equals("76099183-K") || arrayEncabezado.get(11).equals("76130058-K")){
        				//Si hay datos adicionales
        				if(!(arrayEncabezado.get(56)).trim().equals("0") && !(arrayEncabezado.get(56)).trim().equals("0")){
        					String datoAdicional1 =arrayEncabezado.get(55);
        					String datoAdicional2 =arrayEncabezado.get(56);

        					String arrayDA1[] = datoAdicional1.split(";");
        					String arrayDA2[] = datoAdicional2.split(";");

        					int contDA=0;
        					int linComienzo=8;
        					int cantDetalle=arrayDA1.length+8;

        					String titulo="";
        					String detalle="";
        					for (int c=linComienzo;c<cantDetalle;c++){

	        					titulo= new String(arrayDA1[contDA].getBytes(), "ISO-8859-1");
	        					detalle= new String(arrayDA2[contDA].getBytes(), "ISO-8859-1");
	        		            stamper.getAcroFields().setField("det"+c,titulo+":  "+detalle);
	        		            contDA++;
        					}
        				}
        			}
        			//DATOS ADICIONALES 2 MONTANA 2
        			if (arrayEncabezado.get(11).equals("76099183-K") || arrayEncabezado.get(11).equals("76130058-K")){
        				//Si hay datos adicionales
        				if(!(arrayEncabezado.get(57)).trim().equals("0")){
        						String datoAdicional3 =arrayEncabezado.get(57);
	        					String titulo= new String(datoAdicional3.getBytes(), "ISO-8859-1");
	        		            stamper.getAcroFields().setField("det"+23,titulo);
        				}
        			}
        			//DATOS ADICIONALES 3 //TERREMOTO 
        			if (arrayEncabezado.get(11).equals("76130058-K")){
        			
        			//Si hay datos adicionales
        				if(!(arrayEncabezado.get(57)).trim().equals("0")){
        					String datoAdicional1 =arrayEncabezado.get(57);
        					String arrayDA1[] = datoAdicional1.split(";");

        					int contDA=arrayDA1.length-1;
        					String texto="";
        					Integer fin=26-arrayDA1.length;
        					for (int c=26;c>fin;c--){
	        					texto= new String(arrayDA1[contDA].getBytes(), "ISO-8859-1");
	        		            stamper.getAcroFields().setField("det"+c,texto);
	        		            contDA--;
        					}
        				}
        			}
        			//DATOS ADICIONALES PALMA TERPEL
        			if (arrayEncabezado.get(11).equals("83943200-3")){
        				//Si hay datos adicionales
        				if(!(arrayEncabezado.get(57)).trim().equals("0")){
        						String datoAdicional1 =arrayEncabezado.get(57);
	        					String titulo= new String(datoAdicional1.getBytes(), "ISO-8859-1");
	        		            stamper.getAcroFields().setField("datoadic",titulo);
        				}
        			}

        stamper.getAcroFields().setField("totalImpuesto",formato.format(Double.parseDouble(totalImpuesto)) );
        stamper.getAcroFields().setField("totalIVA", formato.format(Double.parseDouble(arrayEncabezado.get(39))));
        stamper.getAcroFields().setField("totalTotal", formato.format(Double.parseDouble(arrayEncabezado.get(46))));

        //Timbre resolucion
        stamper.getAcroFields().setField("NroRes", propiedades.getProperty("NRO_RESOLUCION"));
        stamper.getAcroFields().setField("AnioRes", propiedades.getProperty("FCH_RESOLUCION").substring(0, 4));
        stamper.getAcroFields().setField("txtCedible", cedible);

            //Obtener TED
          LeerXML getTED = new LeerXML();
          BarcodePDF417 pdf417 = new BarcodePDF417();
          pdf417.setCodeRows(5);
          pdf417.setCodeColumns(18);
          pdf417.setErrorLevel(5);
          pdf417.setLenCodewords(999);
          pdf417.setOptions(BarcodePDF417.PDF417_FORCE_BINARY);
          System.out.println("RUTA DTE: "+rutaDTE);
          pdf417.setText(getTED.obtenerTED(rutaDTE).getBytes("ISO-8859-1"));
          com.itextpdf.text.Image img = pdf417.getImage();
          img.scaleAbsolute(184, 72);
          img.setAbsolutePosition(55f, 70f);
          PdfContentByte content = stamper.getOverContent(1);
          content.addImage(img);
          stamper.close();
          pdfTemplate.close();

//          if (estadoImpresora.equals("SI")){
//          //Se imprime Documento
//          Impresion impresion= new Impresion();
//          impresion.imprimirPDF(nombreImpresora, archivoSalida);
//          }
    }

    public String setTipoDocumento(String tipoDTE) throws UnsupportedEncodingException{

        String tipo="";

        if (tipoDTE.equals("33")){
            //tipo = "FACTURA ELECTR�NICA";
        	tipo =  new String("FACTURA ELECTR�NICA".getBytes(), "ISO-8859-1");
        }else if (tipoDTE.equals("56")){
            //tipo = "NOTA DE D�BITO ELECTR�NICA";
        	tipo =  new String("NOTA DE D�BITO ELECTR�NICA".getBytes(), "ISO-8859-1");
        }else if (tipoDTE.equals("61")){
            //tipo = "NOTA DE CR�DITO ELECTR�NICA";
            tipo =  new String("NOTA DE CR�DITO ELECTR�NICA".getBytes(), "ISO-8859-1");
        }else if (tipoDTE.equals("34")){
            //tipo = "NOTA DE D�BITO ELECTR�NICA";
        	tipo =  new String("FACTURA NO AFECTA O EXENTA ELECTR�NICA".getBytes(), "ISO-8859-1");
        }else if (tipoDTE.equals("52")){
            //tipo = "NOTA DE D�BITO ELECTR�NICA";
        	tipo =  new String("GU�A DE DESPACHO ELECTR�NICA".getBytes(), "ISO-8859-1");
        }

        return tipo;
    }

}