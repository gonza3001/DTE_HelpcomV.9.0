/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.helpcom.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

import org.hibernate.result.Output;

import oracle.jdbc.oracore.TDSPatch;

import com.itextpdf.text.pdf.codec.Base64.InputStream;
import com.mysql.jdbc.Blob;
import com.mysql.jdbc.PreparedStatement;

public class ConexionSql1 {

    final String SUFIJO = "";
    private Conexion conexion;
    Connection con;

	public ConexionSql1() throws ClassNotFoundException, SQLException, IOException {
    	conexion = new Conexion();
    	this.con = conexion.getConexion();
	}

	/** CONSULTAR estado de un DTE
	 * @param codTipoDoc
	 * @param folio
	 * @return
	 * @throws SQLException
	 */
	public String getEstado(Integer codTipoDoc, Integer folio) throws SQLException {

        Statement comando = this.con.createStatement();
        System.out.println();
        ResultSet registro;
        String res="NO";

        registro = comando.executeQuery("SELECT tdo_id,doc_folio,doc_estado FROM documento_dte WHERE tdo_id="+codTipoDoc+" AND doc_folio="+folio);
        while (registro.next()) {
            if (registro.getString("doc_estado").equals("GENERADO")) {
            	res=registro.getString("doc_estado");

            }
        }
        return res;
    }
	public String getEstadosDTE(Integer codTipoDoc, Integer folio) throws SQLException {

        Statement comando = this.con.createStatement();
        ResultSet registro = null;
        String res="NO";
        Integer c;
        String Indice="";

        registro = comando.executeQuery("SELECT tdo_id,doc_folio,doc_estado FROM documento_dte WHERE doc_estado='GENERADO'");

        while (registro.next()) {

        	if (registro.getString("doc_estado").equals("GENERADO")) {
            	res=registro.getString("doc_estado");

            }
        }
        return res;
    }

	public String getDocumentoDTE() throws SQLException {

        Statement comando = this.con.createStatement();
        ResultSet registro = null;
        String res="NO";
        Integer c;
        String Indice="";

        registro = comando.executeQuery("SELECT doc_estado,doc_xml FROM documento_dte WHERE doc_estado='GENERADO' LIMIT 1");

        while (registro.next()) {

        	if (registro.getString("doc_estado").equals("GENERADO")) {
            	res=registro.getString("doc_xml");
            }
        }
        return res;
    }

public void addDocumentoTimbrado(Integer empID,Integer tdo_id,Integer doc_folio,String doc_rut_receptor,String doc_fecha_emision,String doc_razon_social,Integer doc_monto,String doc_xml_timbrado) throws SQLException {

        Statement comando = this.con.createStatement();
        String sql = "INSERT INTO dte_emi_documento (emp_id,tdo_id,doc_folio,doc_rut_receptor,doc_fecha_emision,doc_razon_social,doc_monto,doc_xml_timbrado) VALUES (?,?,?,?,?,?,?,?);";
        PreparedStatement statement = (PreparedStatement) this.con.prepareStatement(sql);

        statement.setInt(1, empID);
        statement.setInt(2, tdo_id);
        statement.setInt(3, doc_folio);
        statement.setString(4, doc_rut_receptor);
        statement.setString(5, doc_fecha_emision);
        statement.setString(6, doc_razon_social);
        statement.setInt(7, doc_monto);
        statement.setString(8, doc_xml_timbrado);

        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("A new user was inserted successfully!");
        }else {
        	System.out.println("NO se ha podido insertar el registro correctamente");
		}
}

	/**
	 * AGREGAR Registro de nuevo documento
	 * @param doc_id @param emp_id @param tdo_id @param folio @param doc_rut @param doc_fecha_emision
	 * @param doc_razon_social @param doc_monto @param doc_estado
	 *  @throws SQLException
	 */
	public void setEstado(Integer doc_id,Integer emp_id, Integer tdo_id, Integer folio,String doc_rut, String doc_fecha_emision,String doc_razon_social,Integer doc_monto,String doc_xml,String doc_estado) throws SQLException {

        Statement comando = this.con.createStatement();
        String sql = "INSERT INTO documento_dte (doc_id, emp_id, tdo_id, doc_folio, doc_rut_receptor, doc_fecha_emision,  doc_razon_social,  doc_monto,doc_xml, doc_estado) VALUES (?, ?, ?, ?,?, ?, ?, ?,?,?)";

        PreparedStatement statement = (PreparedStatement) this.con.prepareStatement(sql);
        statement.setInt(1, doc_id);
        statement.setInt(2, emp_id);
        statement.setInt(3, tdo_id);
        statement.setInt(4, folio);
        statement.setString(5, doc_rut);
        statement.setString(6, doc_fecha_emision);
        statement.setString(7, doc_razon_social);
        statement.setInt(8, doc_monto);
        statement.setString(9, doc_xml);
        statement.setString(10, doc_estado);

        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("A new user was inserted successfully!");
        }else {
        	System.out.println("NO se ha podido insertar el registro correctamente");
		}
}

    public void setDTE(Integer doc_id,Integer emp_id, Integer tdo_id, Integer folio,String doc_rut, String doc_fecha_emision,String doc_razon_social,Integer doc_monto,String doc_xml,String doc_estado) throws SQLException {
        Statement comando = this.con.createStatement();
        comando.executeUpdate("UPDATE  documento_dte set doc_id='"+doc_id+"',emp_id ='" + emp_id + "',doc_rut_receptor ='" + doc_rut + "',doc_fecha_emision ='" + doc_fecha_emision + "',doc_razon_social ='" + doc_razon_social + "',doc_monto ='" + doc_monto + "',doc_xml ='" + doc_xml + "',doc_estado ='" + doc_estado + "' where tdo_id=" + tdo_id +" AND doc_folio= "+folio );
    }

    //XML TIMBRADO
	public void addDTETimbrado(Integer emp_id, Integer tdo_id, Integer folio,String doc_rut, String doc_fecha_emision,String doc_razon_social,Integer doc_monto,String doc_xml_timbrado,String doc_estado,String filenamePDF) throws SQLException, FileNotFoundException {

        Statement comando = this.con.createStatement();
        String sql = "INSERT INTO dte_emi_documento (emp_id, tdo_id, doc_folio, doc_rut_receptor, doc_fecha_emision,  doc_razon_social, doc_monto,doc_xml_timbrado, doc_estado) VALUES (?, ?, ?, ?,?, ?, ?, ?,?)";

        PreparedStatement statement = (PreparedStatement) this.con.prepareStatement(sql);
        statement.setInt(1, emp_id);
        statement.setInt(2, tdo_id);
        statement.setInt(3, folio);
        statement.setString(4, doc_rut);
        statement.setString(5, doc_fecha_emision);
        statement.setString(6, doc_razon_social);
        statement.setInt(7, doc_monto);
        statement.setString(8, doc_xml_timbrado);
        statement.setString(9, doc_estado);

        //guardarZIP(filenamePDF, tdo_id,folio);//Guardar documentoPDF Binario BLOB

        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("A new user was inserted successfully!");
        }else {
        	System.out.println("NO se ha podido insertar el registro correctamente");
		}
}


	public void guardarZIP(String filename,Integer tipoDoc,Integer folioDoc) throws SQLException, FileNotFoundException{

		Calendar fecha = Calendar.getInstance();
		int anio = fecha.get(Calendar.YEAR);
		int mes = fecha.get(Calendar.MONTH) + 1;

		String updateSQL = "UPDATE dte_emi_documento SET doc_pdf_zip = ? WHERE tdo_id=? AND doc_folio= ?";
		PreparedStatement pstmt = (PreparedStatement) con.prepareStatement(updateSQL);
		// read the file
		//filename="/home/mau/Documentos/DTE/BYK/documentosPDF/2016/2/5619.pdf";

        File file = new File(filename+anio+"/"+mes+"/"+tipoDoc+folioDoc+".pdf");
        java.io.FileInputStream input = new java.io.FileInputStream(file);

        // set parameters
        System.out.println("PDF NAME "+filename);
        System.out.println("TIPO " + tipoDoc + " FOLIO " +folioDoc);
        //pstmt.setBinaryStream(1, input);
        pstmt.setBlob(1, input);
        pstmt.setInt(2, tipoDoc);
        pstmt.setInt(3, folioDoc);
        pstmt.executeUpdate();
	}
	public void addDocPlano(String filename,Integer tipoDoc,Integer folioDoc) throws SQLException, FileNotFoundException{

		String updateSQL = "UPDATE dte_emi_documento SET doc_doc_plano = ? WHERE tdo_id=? AND doc_folio= ?";
		PreparedStatement pstmt = (PreparedStatement) con.prepareStatement(updateSQL);

        File file = new File(filename);
        java.io.FileInputStream input = new java.io.FileInputStream(file);

        pstmt.setBlob(1, input);
        pstmt.setInt(2, tipoDoc);
        pstmt.setInt(3, folioDoc);
        pstmt.executeUpdate();
	}

	public void addTED(String ted,Integer tipoDoc,Integer folioDoc) throws SQLException, FileNotFoundException{

			String updateSQL = "UPDATE dte_emi_documento SET doc_ted='"+ted+"' WHERE tdo_id='"+tipoDoc+"' AND doc_folio= '"+folioDoc+"'";
			Statement comando = this.con.createStatement();
		    comando.executeUpdate(updateSQL);

	}

	public String getNombreArchivoCAF(int tipo, int folio) throws SQLException {

        Statement comando = this.con.createStatement();
        System.out.println();
        ResultSet registro;
        String res="NO";

        registro = comando.executeQuery("SELECT caf_nombre_archivo FROM dte_caf WHERE doc_tipo="+tipo+" AND "+folio+" BETWEEN caf_minimo AND caf_maximo");
        while (registro.next()) {
            	res=registro.getString("caf_nombre_archivo");
        }
        return res;
    }

	public void obtenerArchivo(String docId) throws SQLException, IOException {

		String pathnamePDF="C:/Users/Pathy Sandoval/Dropbox/helpcom/dte/pdf/documPDFBlob.pdf";
		Statement statement = con.createStatement();
		ResultSet resultSet;
		String sql = "SELECT dex_doc_pdf FROM dte_emi_documento WHERE doc_id = "+docId+"";

		resultSet = statement.executeQuery(sql);

		File file = new File(pathnamePDF);
		FileOutputStream fileOutputStream = new FileOutputStream(file);


		if (resultSet.next()){

			java.io.ByteArrayInputStream inputStream = (java.io.ByteArrayInputStream) resultSet.getBinaryStream("dex_doc_pdf");
			byte[] buffer = new byte[1024];
			while(inputStream.read(buffer)>0){
				fileOutputStream.write(buffer);
			}

		}

	}

public void cerrarConexion() throws SQLException {

	this.con.close();

}


}