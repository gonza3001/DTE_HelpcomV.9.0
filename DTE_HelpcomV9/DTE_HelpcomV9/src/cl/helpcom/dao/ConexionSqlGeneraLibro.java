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

import com.mysql.jdbc.PreparedStatement;

import cl.helpcom.dte.method.GeneraEnviov3;
import cl.helpcom.dte.util.Firma;

public class ConexionSqlGeneraLibro {

	   final String SUFIJO = "";
	   private ConexionServer ConexionServer;
	   Connection conServer;

		public ConexionSqlGeneraLibro() throws ClassNotFoundException, SQLException, IOException {
	    	ConexionServer = new ConexionServer();
	    	this.conServer = ConexionServer.getConexion();
		}

		
		public void addLibro(Integer empID,String estado,String tipoLibro,String periodo,String libXML) throws SQLException {

	        Statement comando = this.conServer.createStatement();
	        String sql = "INSERT INTO dte_libro (emp_id,lib_estado,lib_tipo_libro,lib_periodo,lib_xml) VALUES ("+empID+",'"+estado+"','"+tipoLibro+"','"+periodo+"','"+libXML+"');";
	        PreparedStatement statement = (PreparedStatement) this.conServer.prepareStatement(sql);
	        int rowsInserted = statement.executeUpdate();

	        if (rowsInserted > 0) {
//	            System.out.println("A new user was inserted successfully!");
	        }else {
//	        	System.out.println("NO se ha podido insertar el registro correctamente");
			}
	        
	        if (comando!=null){
	        	comando.close();
	        }
	        if (statement!=null){
	        	statement.close();
	        }
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

		
		
		
		/**
		 * Cierra conexion de Mysql
		 * @throws SQLException
		 */
		public void cerrarConexionServer() throws SQLException {
			if (conServer!=null){
				conServer.close();}

		}
}