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

import com.mysql.jdbc.PreparedStatement;

import cl.helpcom.dte.method.GeneraEnviov3;
import cl.helpcom.dte.util.Firma;

public class ConexionSqlGeneraEnvio {

	   final String SUFIJO = "";
	   private ConexionServer ConexionServer;
	   Connection conServer;

		public ConexionSqlGeneraEnvio() throws ClassNotFoundException, SQLException, IOException {
	    	ConexionServer = new ConexionServer();
	    	this.conServer = ConexionServer.getConexion();
		}

		/**
		 * Busca Empresas que tengas Documentos Firmados para empaquetarlos
		 * @throws SQLException
		 * @throws IOException
		 */
		public void getIDEmpresas() throws SQLException, IOException {

	        Statement comando = this.conServer.createStatement();
	        ResultSet registro;
	        Integer emp_id=0;
	        String emp_pass_cert="";
	        String emp_rut="";
	        String emp_fch_resolucion="";
	        Integer emp_nro_resolucion=0;
	        registro = comando.executeQuery("SELECT DISTINCT emp_id, emp_pass_cert, emp_rut, emp_fch_resolucion, emp_nro_resolucion FROM dte_emi_documento INNER JOIN sys_empresa id USING (emp_id) WHERE doc_estado='FIRMADO';");

	        while (registro.next()) {

	            	emp_id=registro.getInt("emp_id");
	            	emp_pass_cert=registro.getString("emp_pass_cert");
	            	emp_rut=registro.getString("emp_rut");
	            	emp_fch_resolucion =registro.getString("emp_fch_resolucion");
	            	emp_nro_resolucion =registro.getInt("emp_nro_resolucion");
	            	try {
	            		GeneraEnviov3 generaEnviov3 = new GeneraEnviov3();
	 		           	generaEnviov3.generarEnvio(emp_id,emp_pass_cert,emp_rut, emp_fch_resolucion, emp_nro_resolucion);
	            	}catch(Exception e){
	            		System.out.println("No se ha podido empaquetar Documento :\n"+e);
	            	}
	        }
	        
	        if (comando!=null){
	        	comando.close();
	        }
	        if (registro!=null){
	        	registro.close();
	        }
	    }


		//
		public void addPaquete(Integer empID) throws SQLException {

	        Statement comando = this.conServer.createStatement();
	        String sql = "INSERT INTO dte_emi_paquetes (emp_id,paq_fecha,paq_hora) VALUES ("+empID+",CURDATE(),CURTIME());";
	        PreparedStatement statement = (PreparedStatement) this.conServer.prepareStatement(sql);
	        int rowsInserted = statement.executeUpdate();

	        if (rowsInserted > 0) {
	            System.out.println("A new user was inserted successfully!");
	        }else {
	        	System.out.println("NO se ha podido insertar el registro correctamente");
			}
	        
	        if (comando!=null){
	        	comando.close();
	        }
	        if (statement!=null){
	        	statement.close();
	        }
	}

		public void addRelacionPaquete(String entrada) throws SQLException {

	        Statement comando = this.conServer.createStatement();
	        String sql = "INSERT INTO dte_emi_paquetes_mov (paq_id,doc_id) (SELECT (SELECT MAX(paq_id) FROM dte_emi_paquetes),doc_id FROM dte_emi_documento WHERE CONCAT(emp_id,',',tdo_id,',',doc_folio) IN "+entrada+")";

	        PreparedStatement statement = (PreparedStatement) this.conServer.prepareStatement(sql);

	        int rowsInserted = statement.executeUpdate();
	        if (rowsInserted > 0) {
	            System.out.println("A new user was inserted successfully!");
	        }else {
	        	System.out.println("NO se ha podido insertar el registro correctamente");
			}
	        
	        if (comando!=null){
	        	comando.close();
	        }
	        if (statement!=null){
	        	statement.close();
	        }
	}

		public Integer getMaxPkg() throws SQLException, IOException {

	        Statement comando = this.conServer.createStatement();
	        ResultSet registro;
	        Integer maxpkg=0;

	        registro = comando.executeQuery("SELECT MAX(paq_id) as maxpkg FROM dte_emi_paquetes");

	        while (registro.next()) {

	            	maxpkg=registro.getInt("maxpkg");
	        }
	        
	        if (comando!=null){
	        	comando.close();
	        }
	        if (registro!=null){
	        	registro.close();
	        }
	        return maxpkg;
	    }





		/**
		 *
		 * Actualiza todos los documentos con estado "FIRMADO" pertenecientes a una empresa
		 * @param emp_id
		 * @throws SQLException
		 */
		public void updateEstado(Integer emp_id, String tipo, String folio) throws SQLException {
			Statement comando = this.conServer.createStatement();
		    comando.executeUpdate("UPDATE dte_emi_documento SET doc_estado='EMPAQUETADO' WHERE emp_id='"+emp_id+"' AND tdo_id='"+tipo+"' AND doc_folio='"+folio+"'");
		    if (comando!=null){
		    	comando.close();
		    }
		    
		}
		public void updateEstadoEnvioTercero(Integer emp_id, String tipo, String folio) throws SQLException {
			Statement comando = this.conServer.createStatement();
		    comando.executeUpdate("UPDATE dte_emi_documento SET doc_envio_tercero='EMPAQUETADO' WHERE emp_id='"+emp_id+"' AND tdo_id='"+tipo+"' AND doc_folio='"+folio+"'");
		    if (comando!=null){
		    	comando.close();
		    }
		    
		}
		/**
		 * Cierra conexion de Mysql
		 * @throws SQLException
		 */
		public void cerrarConexionServer() throws SQLException {
			if (conServer!=null){
				conServer.close();}

		}
}