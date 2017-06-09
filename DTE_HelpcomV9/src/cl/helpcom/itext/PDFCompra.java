
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
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BarcodePDF417;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class PDFCompra {
	private Properties propiedades = new Properties();
    private InputStream entrada = null;
    private Validador validador = new Validador();

    public void creaPDF(String pdfTempleStr,String archivoSalida,ArrayList<String> arrayIdDoc,ArrayList<String> arrayEmisor,ArrayList<String> arrayReceptor,ArrayList<String> arrayTotales,ArrayList<ArrayList<String>> arrayDetalle,ArrayList<ArrayList<String>> arrayReferencia,ArrayList<ArrayList<String>> arrayILA,String ted,ArrayList<String> arrayCaratula) throws IOException, DocumentException, PrintException{
//    	System.out.println(pdfTempleStr);
//        entrada = new FileInputStream("/usr/local/F_E/Configuraciones/Propiedades/configuracionCreaDTE.properties");
        // cargamos el archivo de propiedades
//        propiedades.load(entrada);
        archivoSalida = archivoSalida+"/"+arrayIdDoc.get(0)+arrayIdDoc.get(1)+".pdf";
        LectorFichero lectorFichero = new LectorFichero();//**
        String tipoDTE = this.setTipoDocumento(arrayIdDoc.get(0));

        
        PdfReader pdfTemplate = new PdfReader(pdfTempleStr);//Entrada3
        FileOutputStream pdfSalida = new FileOutputStream(archivoSalida);//Salida
        Files.setPosixFilePermissions(Paths.get(archivoSalida), lectorFichero.permisos());//permisos a PDF
        
        PdfStamper stamper = new PdfStamper(pdfTemplate, pdfSalida);
        stamper.setFormFlattening(true);

        DecimalFormat formato = new DecimalFormat("#,###.###");

        //Info Emisor InfoEmisor
        String nomEmpresa = new String(arrayEmisor.get(1).getBytes(), "ISO-8859-1");
        stamper.getAcroFields().setField("rznSocialEmi", nomEmpresa);
        String giroEmpresa = new String(arrayEmisor.get(2).getBytes(), "ISO-8859-1");
        stamper.getAcroFields().setField("giroEmi", "Giro: "+giroEmpresa);
        String direccionEmisor= new String(arrayEmisor.get(4).getBytes(), "ISO-8859-1");
        stamper.getAcroFields().setField("direccionEmi","Dirrección: "+direccionEmisor);
        String comunaEmisor= new String(arrayEmisor.get(5).getBytes(), "ISO-8859-1");
        stamper.getAcroFields().setField("comunaEmi","Comuna: "+ comunaEmisor);

        String rutE=arrayEmisor.get(0);
        String [] rutEmisor=rutE.split("-");
        stamper.getAcroFields().setField("rutEmisor", formato.format(Double.parseDouble(rutEmisor[0])).replaceAll(",", ".")+ "- " + rutEmisor[1]);

        //Recuadro ROJO
        stamper.getAcroFields().setField("tipoDocumento", tipoDTE);
        stamper.getAcroFields().setField("folio", arrayIdDoc.get(1));
        stamper.getAcroFields().setField("comuna",  arrayEmisor.get(4));
        stamper.getAcroFields().setField("locacionSI", arrayEmisor.get(4));

        //Info Receptor
        stamper.getAcroFields().setField("fecha", validador.YYMMDDToDDMMYY(arrayIdDoc.get(2)));
        String nomReceptor= new String(arrayReceptor.get(1).getBytes(), "ISO-8859-1");
        stamper.getAcroFields().setField("nomReceptor", nomReceptor);
        String direccionReceptor= new String(arrayReceptor.get(3).getBytes(), "ISO-8859-1");
        stamper.getAcroFields().setField("direccionReceptor", direccionReceptor);
        String giroReceptor= new String(arrayReceptor.get(2).getBytes(), "ISO-8859-1");
        stamper.getAcroFields().setField("giroReceptor", giroReceptor);

//        String medPagoPDF= new String(arrayEncabezado.get(55).getBytes(), "ISO-8859-1");
//        stamper.getAcroFields().setField("condicionesPago", medPagoPDF);
        //stamper.getAcroFields().setField("codCliente", "");
        String rutR=arrayReceptor.get(0);
        String [] rutReceptor=rutR.split("-");

        stamper.getAcroFields().setField("rutReceptor", formato.format(Double.parseDouble(rutReceptor[0])).replaceAll(",", ".")+"-"+rutReceptor[1]);
        String comunaReceptor= new String(arrayReceptor.get(4).getBytes(), "ISO-8859-1");
        stamper.getAcroFields().setField("comunaReceptor", comunaReceptor);
        String ciudadReceptor= new String(arrayReceptor.get(5).getBytes(), "ISO-8859-1");
        stamper.getAcroFields().setField("ciudadReceptor", ciudadReceptor);
        //String vendedor= new String(arrayR.get(20).getBytes(), "ISO-8859-1");
//        if (vendedor.equals("0")){
//        	stamper.getAcroFields().setField("vendedor", "");
//        }else {
//        	stamper.getAcroFields().setField("vendedor", vendedor);
//		}


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
			            stamper.getAcroFields().setField("folioReferencia"+i, arrayReferencia.get(i).get(2));
			            stamper.getAcroFields().setField("fechaReferencia"+i, validador.YYMMDDToDDMMYY(arrayReferencia.get(i).get(3)));
			            String motivoReferencia= new String(arrayReferencia.get(i).get(4).getBytes(), "ISO-8859-1");
			            if (motivoReferencia.length()>46){
			            	stamper.getAcroFields().setField("motivoReferencia"+i, motivoReferencia.substring(i,45));
			            }else {
			            	stamper.getAcroFields().setField("motivoReferencia"+i, motivoReferencia);
						}
			            if (i>1){i=999;}
        			}
        }
        //Info Totales
        int cGrilla=1; double totalDescuento=0;;int tamTextDet=78;
        for(int c =0; c<arrayDetalle.size();c++){
        	if (arrayDetalle.get(c).get(2).length()>tamTextDet){//Si es mayor a 62
        		//PRIMERA LINEA
        			stamper.getAcroFields().setField("cant"+cGrilla,arrayDetalle.get(c).get(0));
		            stamper.getAcroFields().setField("cantidad"+cGrilla,arrayDetalle.get(c).get(3));
		            stamper.getAcroFields().setField("cod"+cGrilla,"");
		            String detalleAux=new String (arrayDetalle.get(c).get(2).substring(0, tamTextDet).getBytes(), "ISO-8859-1");
		            stamper.getAcroFields().setField("det"+cGrilla,detalleAux);

		            String detalle= new String(arrayDetalle.get(c).get(2).getBytes(), "ISO-8859-1");

		            String dtos=arrayDetalle.get(c).get(7);

		            double descuentos=Double.parseDouble(dtos);
		            totalDescuento=totalDescuento+descuentos;
		            String descuento="";
		            if (dtos==""){
			            	descuento=" 0%"+"  $0";
			            	stamper.getAcroFields().setField("dtos"+cGrilla,descuento);
			        }else {
			            	descuento=formato.format(Double.parseDouble(dtos)).replaceAll(",",".");
			            	stamper.getAcroFields().setField("dtos"+cGrilla,descuento);
						}

			            String punitario=arrayDetalle.get(c).get(4);
			            stamper.getAcroFields().setField("punitario"+cGrilla,formato.format(Double.parseDouble(punitario)).replaceAll(",", "."));
			            String totalGrid=arrayDetalle.get(c).get(5);
			            stamper.getAcroFields().setField("totalGrid"+cGrilla,formato.format(Double.parseDouble(totalGrid)).replaceAll(",", "."));/**/

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
			    	 while(detalle.length()>tamTextDet){
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
		            stamper.getAcroFields().setField("cant"+cGrilla,arrayDetalle.get(c).get(3));
		            stamper.getAcroFields().setField("cantidad"+cGrilla,arrayDetalle.get(c).get(3));
		            stamper.getAcroFields().setField("cod"+cGrilla,"");
		            String detalle= new String(arrayDetalle.get(c).get(2).getBytes(), "ISO-8859-1");
		            stamper.getAcroFields().setField("det"+cGrilla,detalle);

		            String dtos=arrayDetalle.get(c).get(7);
		            
		            try {
			            double descuentos=Double.parseDouble(dtos);
			            totalDescuento=totalDescuento+descuentos;
					} catch (Exception e) {
						double descuentos=0;
					}
		            
		            String descuento="";
		            if (dtos==""){
			            	descuento=" $0";
			            	stamper.getAcroFields().setField("dtos"+cGrilla,descuento);
			            }else {
			            	descuento="$"+formato.format(Double.parseDouble(dtos));
			            	stamper.getAcroFields().setField("dtos"+cGrilla,descuento);
						}

			            String punitario=arrayDetalle.get(c).get(4);
			            stamper.getAcroFields().setField("punitario"+cGrilla,formato.format(Double.parseDouble(punitario)).replaceAll(",", "."));
			            String totalGrid=arrayDetalle.get(c).get(5);
			            stamper.getAcroFields().setField("totalGrid"+cGrilla,formato.format(Double.parseDouble(totalGrid)).replaceAll(",","."));/**/
		            }

		            cGrilla++;

        }
        //stamper.getAcroFields().setField("totalEnTexto", arrayEncabezado.get(47).toUpperCase());
        //IVA Excepcionales
//        if((arrayEncabezado.get(48).equals("-1"))){
//
//        	stamper.getAcroFields().setField("ivaCarne","0");
//        }else{
//
//            stamper.getAcroFields().setField("ivaCarne",arrayEncabezado.get(48));
//        }
//
//        if((arrayEncabezado.get(49).trim().equals("-1"))){
//        	stamper.getAcroFields().setField("ivaHarina","0");
//        }else{
//            stamper.getAcroFields().setField("ivaHarina",arrayEncabezado.get(49));
//        }
//
//        if((arrayEncabezado.get(50).trim().equals("-1"))){
//        	stamper.getAcroFields().setField("ivaLicor","0");
//        }else{
//            stamper.getAcroFields().setField("ivaLicor",arrayEncabezado.get(50));
//        }
//
//        if((arrayEncabezado.get(51).trim().equals("-1"))){
//        	stamper.getAcroFields().setField("ivaVino","0");
//        }else{
//            stamper.getAcroFields().setField("ivaVino",arrayEncabezado.get(51));
//        }
//
//        if((arrayEncabezado.get(52).trim().equals("-1"))){
//        	stamper.getAcroFields().setField("ivaCerveza","0");
//        }else{
//            stamper.getAcroFields().setField("ivaCerveza",arrayEncabezado.get(52));
//        }
//
//        if((arrayEncabezado.get(53).trim().equals("-1"))){
//        	stamper.getAcroFields().setField("ivaSAzucar","0");
//        }else{
//            stamper.getAcroFields().setField("ivaSAzucar",arrayEncabezado.get(53));
//        }
//
//        if((arrayEncabezado.get(54).trim().equals("-1"))){
//        	stamper.getAcroFields().setField("ivaCAzucar","0");
//        }else{
//            stamper.getAcroFields().setField("ivaCAzucar",arrayEncabezado.get(54));
//        }

        //TOTALES
         //String totalDescuento=arrayEncabezado.get();
//        if (!arrayDscGlobal.isEmpty()){
//	        stamper.getAcroFields().setField("totalDescuento", formato.format(Double.parseDouble(arrayDscGlobal.get(0).get(4))));
//        }else{
//        	stamper.getAcroFields().setField("totalDescuento", "0");
//        }

        String totalExento="";
        if((arrayTotales.get(1)).trim().equals("") || (arrayTotales.get(1)).trim().equals("0") ){
            totalExento="0";
        }else{
            totalExento=arrayTotales.get(1);
        }
        stamper.getAcroFields().setField("totalExento", formato.format(Double.parseDouble(totalExento)));

        String totalNeto="";
        if((arrayTotales.get(0)).trim().equals("") || (arrayTotales.get(0)).trim().equals("0") ){
            totalNeto="0";
        }else{
            totalNeto=arrayTotales.get(0);
        }
        stamper.getAcroFields().setField("totalNeto", formato.format(Double.parseDouble(totalNeto)).replaceAll(",", "."));

//        String totalImpuesto="";
//        if((arrayEncabezado.get(44)).trim().equals("") || (arrayEncabezado.get(44)).trim().equals("0") ){
//             totalImpuesto="0";
//        }else{
//             totalImpuesto=arrayEncabezado.get(44);
//        }
        //stamper.getAcroFields().setField("totalImpuesto",formato.format(Double.parseDouble(totalImpuesto)) );

        stamper.getAcroFields().setField("totalIVA", formato.format(Double.parseDouble(arrayTotales.get(3))).replaceAll(",", "."));
        stamper.getAcroFields().setField("totalTotal", formato.format(Double.parseDouble(arrayTotales.get(4))).replaceAll(",", "."));

        //Timbre resolucion
//        stamper.getAcroFields().setField("NroRes", propiedades.getProperty("NRO_RESOLUCION"));
//        stamper.getAcroFields().setField("AnioRes", propiedades.getProperty("FCH_RESOLUCION").substring(0, 4));
          stamper.getAcroFields().setField("txtCedible", "");

            //Obtener TED
          BarcodePDF417 pdf417 = new BarcodePDF417();
          pdf417.setCodeRows(5);
          pdf417.setCodeColumns(18);
          pdf417.setErrorLevel(5);
          pdf417.setLenCodewords(999);
          pdf417.setOptions(BarcodePDF417.PDF417_FORCE_BINARY);
          pdf417.setText(ted.getBytes("ISO-8859-1"));
          com.itextpdf.text.Image img = pdf417.getImage();
          img.scaleAbsolute(184, 72);
          img.setAbsolutePosition(55f, 70f);
          PdfContentByte content = stamper.getOverContent(1);
          content.addImage(img);
          stamper.close();
          pdfTemplate.close();
          
          
    }

    public String setTipoDocumento(String tipoDTE) throws UnsupportedEncodingException{

        String tipo="";

        if (tipoDTE.equals("33")){
        	tipo =  new String("FACTURA ELECTRÓNICA".getBytes(), "ISO-8859-1");
        }else if (tipoDTE.equals("56")){
        	tipo =  new String("NOTA DE DÉBITO ELECTRÓNICA".getBytes(), "ISO-8859-1");
        }else if (tipoDTE.equals("61")){
            tipo =  new String("NOTA DE CRÉDITO ELECTRÓNICA".getBytes(), "ISO-8859-1");
        }else if (tipoDTE.equals("34")){
            tipo =  new String("FACTURA NO AFECTA O EXENTA ELECTRÓNICA".getBytes(), "ISO-8859-1");
        }else if (tipoDTE.equals("52")){
            tipo =  new String("GUÍA DE DESPACHO ELECTRÓNICA".getBytes(), "ISO-8859-1");
        }

        return tipo;
    }

}