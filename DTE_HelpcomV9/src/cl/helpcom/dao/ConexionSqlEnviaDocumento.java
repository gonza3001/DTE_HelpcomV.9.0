package cl.helpcom.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPException;

import org.apache.xmlbeans.XmlException;
import org.xml.sax.SAXException;

import cl.helpcom.dte.method.EnviaDocumentov3;
import cl.nic.dte.net.ConexionSiiException;

import com.mysql.jdbc.PreparedStatement;

public class ConexionSqlEnviaDocumento {

    final String SUFIJO = "";
    private ConexionServer conexionServer;
    Connection conServer;

	public ConexionSqlEnviaDocumento() throws ClassNotFoundException, SQLException, IOException {
    	conexionServer = new ConexionServer();
    	this.conServer = conexionServer.getConexion();
	}


	public void setEstadoENVIADO(Integer idPkg) throws SQLException {
        Statement comando = this.conServer.createStatement();
        comando.executeUpdate("UPDATE dte_emi_documento a INNER JOIN dte_emi_paquetes_mov b USING(doc_id) SET doc_estado='ENVIADO' WHERE paq_id="+idPkg+"");
        if (comando!=null){
        	comando.close();
        }
	}
	public void setTrackID(Long trackId,Integer idPkg) throws SQLException {
		Statement comando = this.conServer.createStatement();
		try {
			comando.executeUpdate("UPDATE dte_emi_documento a INNER JOIN dte_emi_paquetes_mov b USING(doc_id) SET doc_track_id="+trackId.toString()+" WHERE paq_id="+idPkg+"");
	        if (comando!=null){
	        	comando.close();
	        }	
		} catch (Exception e) {
			System.out.println("Error al Guardar track ID:"+e);
		}
        
    }

	public void cerrarConexion() throws SQLException {
		conServer.close();
	}

	public void getIDEmpresas() throws SQLException, IOException, UnrecoverableKeyException, ClassNotFoundException, KeyStoreException, NoSuchAlgorithmException, CertificateException, InvalidAlgorithmParameterException, KeyException, UnsupportedOperationException, MarshalException, XMLSignatureException, SAXException, ParserConfigurationException, XmlException, SOAPException, ConexionSiiException, InterruptedException {

        Statement comando = this.conServer.createStatement();
        EnviaDocumentov3 enviaPkg = new EnviaDocumentov3();
        ResultSet registro;
        Integer emp_id=0;
        String emp_rut="";
        String emp_pass_cert="";
        registro = comando.executeQuery("SELECT DISTINCT emp_id, emp_pass_cert, emp_rut FROM dte_emi_documento INNER JOIN sys_empresa id USING (emp_id) WHERE doc_estado='EMPAQUETADO';");

        while (registro.next()) {

            	emp_id=registro.getInt("emp_id");
            	emp_rut=registro.getString("emp_rut");
            	emp_pass_cert=registro.getString("emp_pass_cert");

            	System.out.println("empresa "+emp_id);
            	enviaPkg.enviarDocumentoSII(emp_id, emp_pass_cert, emp_rut);

            	Thread.sleep(Long.valueOf(2000));// Esperar entre envios
        }
        
        if (comando!=null){
        	comando.close();
        }
        if (registro!=null){
        	registro.close();
        }
    }


}