/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.helpcom.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import cl.helpcom.dte.method.GeneraEnvioATercero;
import cl.helpcom.dte.util.Firma;

public class ConexionSqlFirma {

	   final String SUFIJO = "";
	   private ConexionServer ConexionServer;
	   Connection conServer;

		public ConexionSqlFirma() throws ClassNotFoundException, SQLException, IOException {
	    	ConexionServer = new ConexionServer();
	    	this.conServer = ConexionServer.getConexion();
		}

	public void getIDEmpresas() throws SQLException, IOException {

        Statement comando = this.conServer.createStatement();
        ResultSet registro;
        Integer emp_id=0;
        String emp_pass_cert="";
        registro = comando.executeQuery("SELECT DISTINCT emp_id, emp_pass_cert FROM dte_emi_documento INNER JOIN sys_empresa id USING (emp_id) WHERE doc_estado='RECEPCIONADO';");

        while (registro.next()) {

            	emp_id=registro.getInt("emp_id");
            	emp_pass_cert=registro.getString("emp_pass_cert");
            	System.out.println("empresa "+emp_id);
            	this.getXMLS_Recepcionados(emp_id,emp_pass_cert);
        }
        
        if (comando!=null){
			comando.close();
		}
        if (registro!=null){
        	registro.close();
		}
    }

	public void getXMLS_Recepcionados(Integer emp_id, String passCert) throws SQLException, IOException {
        Statement comando = this.conServer.createStatement();
        ResultSet registro;
        String xml_timbrado="";
        String xml_firmado="";
        Integer doc_folio=0;
        Integer tdo_id=0;
        String rut_receptor="";
        String fecha_emision="";
        Firma firma= new Firma();


        registro = comando.executeQuery("SELECT doc_rut_receptor, dex_xml_timbrado,tdo_id,doc_folio,dex_id,doc_fecha_emision FROM dte_emi_documento doc INNER JOIN dte_emi_xmls  xml USING(doc_id) WHERE emp_id='"+emp_id+"' AND doc_estado='RECEPCIONADO';");
        while (registro.next()) {
        	xml_timbrado=registro.getString("dex_xml_timbrado");
        	tdo_id=registro.getInt("tdo_id");
        	doc_folio=registro.getInt("doc_folio");
        	rut_receptor= registro.getString("doc_rut_receptor");
        	fecha_emision= registro.getString("doc_fecha_emision");

        	try {
        			xml_firmado=firma.firmarDTE(getDatosEmpresa(emp_id),rut_receptor,xml_timbrado,tdo_id,doc_folio,emp_id,passCert,fecha_emision);//Obtengo xml_firmado
        			this.updateXMLFirmado(xml_firmado, tdo_id, doc_folio, emp_id);//Guardo xmlFirmado
        			this.updateEstadoAFirmado(tdo_id, doc_folio, emp_id);//Cambiar el estado a FIRMADO
        	}catch (Exception e) {
        		System.out.println("No se ha Firmado documento :"+tdo_id+doc_folio +"\n"+e);
        	}

        	try {
//				envioTercero.generarEnvio(emp_id, passCert, "2014-08-22", 0, xml_firmado);
			} catch (Exception e) {
				System.out.println("No se ha Empaquetado el documento a Tercero:"+tdo_id+doc_folio +"\n"+e);
			}
        }
        
        if (comando!=null){
			comando.close();
		}
        if (registro!=null){
        	registro.close();
		}
    }

	public void updateXMLFirmado(String xml_firmado,Integer tipo_doc,Integer doc_folio,Integer emp_id) throws SQLException {
		Statement comando = this.conServer.createStatement();
	    comando.executeUpdate("UPDATE dte_emi_xmls xml INNER JOIN dte_emi_documento dte USING(doc_id) SET dex_xml_firmado='"+xml_firmado+"' WHERE doc_folio='"+doc_folio+"' AND tdo_id='"+tipo_doc+"' AND emp_id='"+emp_id+"'");
	    if (comando!=null){
//			ConexionServer.cerrar(comando);
			comando.close();
		}
	    
	}

	public void updateEstadoAFirmado(Integer tipo_doc,Integer doc_folio,Integer emp_id) throws SQLException {
		Statement comando = this.conServer.createStatement();
	    comando.executeUpdate("UPDATE dte_emi_documento SET doc_estado='FIRMADO' WHERE doc_folio='"+doc_folio+"' AND tdo_id='"+tipo_doc+"' AND emp_id='"+emp_id+"'");
	    if (comando!=null){
//			ConexionServer.cerrar(comando);
			comando.close();
		}
	    
	    
	}

	public String getEstadoRECEPCIONADO(Integer emp_id) throws SQLException, IOException {

        Statement comando = this.conServer.createStatement();
        ResultSet registro;
        String estado="";
        registro = comando.executeQuery("SELECT DISTINCT doc_estado FROM dte_emi_documento WHERE emp_id='"+emp_id+"' AND doc_estado ='RECEPCIONADO'");
        while (registro.next()) {
            	estado=registro.getString("doc_estado");
            	System.out.println("empresa "+emp_id);
        }
        if (comando!=null){
//			ConexionServer.cerrar(comando);
        	comando.close();
		}
        if (registro!=null){
//			ConexionServer.cerrar(comando);
        	registro.close();
        	
		}
        
        return estado;
    }

	public ArrayList<String> getDatosEmpresa(Integer emp_id) throws SQLException, IOException {

        Statement comando = this.conServer.createStatement();
        ArrayList<String> datosEmp  = new ArrayList<>();
        ResultSet registro;

        registro = comando.executeQuery("SELECT emp_id,emp_rut, emp_nombre, emp_ccsii_username,emp_pass_cert,emp_fch_resolucion,emp_nro_resolucion FROM sys_empresa WHERE emp_id="+emp_id);

        while (registro.next()) {

            	datosEmp.add(registro.getString("emp_rut"));			//0
            	datosEmp.add(registro.getString("emp_nombre"));			//1
            	datosEmp.add(registro.getString("emp_ccsii_username"));	//2
            	datosEmp.add(registro.getString("emp_pass_cert"));	//3
            	datosEmp.add(registro.getString("emp_fch_resolucion"));	//4
            	datosEmp.add(registro.getString("emp_nro_resolucion"));	//5
            	datosEmp.add(registro.getString("emp_id"));				//6
        }
        
        if (comando!=null){
        	comando.close();
		}
        if (registro!=null){
        	registro.close();
		}
        
        return datosEmp;
    }

	public void cerrarConexionServer() throws SQLException {
		if (conServer!=null){
			conServer.close();
		}
	}
}