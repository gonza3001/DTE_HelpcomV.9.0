
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

public class PDFVenta {
	private Properties propiedades = new Properties();
    private InputStream entrada = null;
    private Validador validador = new Validador();

    public void creaPDF(String pdfTempleStr,String archivoSalida,ArrayList<String> arrayEncabezado,ArrayList<ArrayList<String>> arrayDetalle,ArrayList<ArrayList<String>> arrayReferencia,ArrayList<ArrayList<String>> arrayDscGlobal,String TED, int cItem,String cedible,String nroResolucion,String fchResolucion) throws IOException, DocumentException, PrintException{
    	
        archivoSalida = archivoSalida+"/"+arrayEncabezado.get(0)+arrayEncabezado.get(1)+".pdf";
        LectorFichero lectorFichero = new LectorFichero();
        String tipoDTE = this.setTipoDocumento(arrayEncabezado.get(0));
        PdfReader pdfTemplate = new PdfReader(pdfTempleStr);//Entrada3
        
        FileOutputStream pdfSalida = new FileOutputStream(archivoSalida);//Salida
        
        PdfStamper stamper = new PdfStamper(pdfTemplate, pdfSalida);
        Files.setPosixFilePermissions(Paths.get(archivoSalida), lectorFichero.permisos());
        
        stamper.setFormFlattening(true);
        DecimalFormat formato = new DecimalFormat("#,###.##");

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
        stamper.getAcroFields().setField("rutEmisor", formato.format(Double.parseDouble(rutEmisor[0])).replaceAll(",", ".")+ "- " + rutEmisor[1] );

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

        String medPagoPDF= new String(arrayEncabezado.get(55).getBytes(), "ISO-8859-1");
        stamper.getAcroFields().setField("condicionesPago", medPagoPDF);
        stamper.getAcroFields().setField("codCliente", "");
        String rutR=arrayEncabezado.get(21);
        String [] rutReceptor=rutR.split("-");

        stamper.getAcroFields().setField("rutReceptor", formato.format(Double.parseDouble(rutReceptor[0])).replaceAll(",", ".")+"-"+rutReceptor[1]);
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
		            stamper.getAcroFields().setField("cantidad"+cGrilla,arrayDetalle.get(c).get(5));
		            stamper.getAcroFields().setField("cod"+cGrilla,arrayDetalle.get(c).get(1));
		            String detalleAux=new String (arrayDetalle.get(c).get(3).substring(0, tamTextDet).getBytes(), "ISO-8859-1");
		            stamper.getAcroFields().setField("det"+cGrilla,detalleAux);

		            String detalle= new String(arrayDetalle.get(c).get(3).getBytes(), "ISO-8859-1");
		            String dtos=arrayDetalle.get(c).get(9);
		            double descuentos=Double.parseDouble(dtos);
		            totalDescuento=totalDescuento+descuentos;
		            String descuento="";
		            if (dtos=="0"){
			            	descuento=" 0%"+"  $0";
			            	stamper.getAcroFields().setField("dtos"+cGrilla,descuento);
			            }else {
			            	descuento=""+arrayDetalle.get(c).get(8)+"%"+"  $"+formato.format(Double.parseDouble(dtos)).replaceAll(",", ".");
			            	stamper.getAcroFields().setField("dtos"+cGrilla,descuento);
						}

			            String punitario=arrayDetalle.get(c).get(7);
			            stamper.getAcroFields().setField("punitario"+cGrilla,formato.format(Double.parseDouble(punitario)).replaceAll(",", "."));
			            String totalGrid=arrayDetalle.get(c).get(12);
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
		            stamper.getAcroFields().setField("cant"+cGrilla,arrayDetalle.get(c).get(0));
		            stamper.getAcroFields().setField("cantidad"+cGrilla,arrayDetalle.get(c).get(5));
		            stamper.getAcroFields().setField("cod"+cGrilla,arrayDetalle.get(c).get(1));
		            String detalle= new String(arrayDetalle.get(c).get(3).getBytes(), "ISO-8859-1");
		            stamper.getAcroFields().setField("det"+cGrilla,detalle);
		            String dtos=arrayDetalle.get(c).get(9);
		            double descuentos=Double.parseDouble(dtos);
		            totalDescuento=totalDescuento+descuentos;
		            String descuento="";
		            if (dtos=="0"){
			            	descuento=" 0%"+"  $0";
			            	stamper.getAcroFields().setField("dtos"+cGrilla,descuento);
			            }else {
			            	descuento=""+arrayDetalle.get(c).get(8)+"%"+"  $"+formato.format(Double.parseDouble(dtos)).replaceAll(",", ".");
			            	stamper.getAcroFields().setField("dtos"+cGrilla,descuento);
						}

			            String punitario=arrayDetalle.get(c).get(7);
			            stamper.getAcroFields().setField("punitario"+cGrilla,formato.format(Double.parseDouble(punitario)).replaceAll(",", "."));
			            String totalGrid=arrayDetalle.get(c).get(12);
			            stamper.getAcroFields().setField("totalGrid"+cGrilla,formato.format(Double.parseDouble(totalGrid)).replaceAll(",", "."));/**/
		            }

		            cGrilla++;

        }

        stamper.getAcroFields().setField("totalEnTexto", arrayEncabezado.get(47).toUpperCase());
        //IVA Excepcionales

        if((arrayEncabezado.get(48).equals("-1"))){

        	stamper.getAcroFields().setField("ivaCarne","0");
        }else{

            stamper.getAcroFields().setField("ivaCarne",arrayEncabezado.get(48));
        }

        if((arrayEncabezado.get(49).trim().equals("-1"))){
        	stamper.getAcroFields().setField("ivaHarina","0");
        }else{
            stamper.getAcroFields().setField("ivaHarina",arrayEncabezado.get(49));
        }

        if((arrayEncabezado.get(50).trim().equals("-1"))){
        	stamper.getAcroFields().setField("ivaLicor","0");
        }else{
            stamper.getAcroFields().setField("ivaLicor",arrayEncabezado.get(50));
        }

        if((arrayEncabezado.get(51).trim().equals("-1"))){
        	stamper.getAcroFields().setField("ivaVino","0");
        }else{
            stamper.getAcroFields().setField("ivaVino",arrayEncabezado.get(51));
        }

        if((arrayEncabezado.get(52).trim().equals("-1"))){
        	stamper.getAcroFields().setField("ivaCerveza","0");
        }else{
            stamper.getAcroFields().setField("ivaCerveza",arrayEncabezado.get(52));
        }

        if((arrayEncabezado.get(53).trim().equals("-1"))){
        	stamper.getAcroFields().setField("ivaSAzucar","0");
        }else{
            stamper.getAcroFields().setField("ivaSAzucar",arrayEncabezado.get(53));
        }

        if((arrayEncabezado.get(54).trim().equals("-1"))){
        	stamper.getAcroFields().setField("ivaCAzucar","0");
        }else{
            stamper.getAcroFields().setField("ivaCAzucar",arrayEncabezado.get(54));
        }

        //TOTALES
         //String totalDescuento=arrayEncabezado.get();
        if (!arrayDscGlobal.isEmpty()){
	        stamper.getAcroFields().setField("totalDescuento", formato.format(Double.parseDouble(arrayDscGlobal.get(0).get(4))).replaceAll(",", "."));
        }else{
        	stamper.getAcroFields().setField("totalDescuento", "0");
        }

        String totalExento="";
        if((arrayEncabezado.get(37)).trim().equals("") || (arrayEncabezado.get(37)).trim().equals("0") ){
            totalExento="0";
        }else{
            totalExento=arrayEncabezado.get(37);
        }
        stamper.getAcroFields().setField("totalExento", formato.format(Double.parseDouble(totalExento)).replaceAll(",", "."));

        String totalNeto="";
        if((arrayEncabezado.get(36)).trim().equals("") || (arrayEncabezado.get(36)).trim().equals("0") ){
            totalNeto="0";
        }else{
            totalNeto=arrayEncabezado.get(36);
        }
        stamper.getAcroFields().setField("totalNeto", formato.format(Double.parseDouble(totalNeto)).replaceAll(",", "."));

        String totalImpuesto="";
        if((arrayEncabezado.get(44)).trim().equals("") || (arrayEncabezado.get(44)).trim().equals("0") ){
             totalImpuesto="0";
        }else{
             totalImpuesto=arrayEncabezado.get(44);
        }
        stamper.getAcroFields().setField("totalImpuesto",formato.format(Double.parseDouble(totalImpuesto)).replaceAll(",", ".") );

        stamper.getAcroFields().setField("totalIVA", formato.format(Double.parseDouble(arrayEncabezado.get(39))).replaceAll(",", "."));
        stamper.getAcroFields().setField("totalTotal", formato.format(Double.parseDouble(arrayEncabezado.get(46))).replaceAll(",", "."));

        //Timbre resolucion
        stamper.getAcroFields().setField("NroRes", nroResolucion);
        stamper.getAcroFields().setField("AnioRes", fchResolucion);
        stamper.getAcroFields().setField("txtCedible", cedible);

            //Obtener TED
          BarcodePDF417 pdf417 = new BarcodePDF417();
          pdf417.setCodeRows(5);
          pdf417.setCodeColumns(18);
          pdf417.setErrorLevel(5);
          pdf417.setLenCodewords(999);
          pdf417.setOptions(BarcodePDF417.PDF417_FORCE_BINARY);
          pdf417.setText(TED.getBytes("ISO-8859-1"));
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