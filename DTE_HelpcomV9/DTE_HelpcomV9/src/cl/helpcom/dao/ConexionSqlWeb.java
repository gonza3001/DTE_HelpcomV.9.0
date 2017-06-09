package cl.helpcom.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;

import com.mysql.jdbc.PreparedStatement;

import cl.helpcom.recursos.LectorFichero;

public class ConexionSqlWeb {

    final String SUFIJO = "";
    private ConexionServerWeb conexionServer;
    Connection conServer;
    private String claveCert="";
    String nroResolucion;
   	String fchResolucion;
   	String username;
    String password;
    String emp_rut;
	String emp_rut_rep_legal;
	String nom_rep_legal;
	String tel_contacto;
	String user_name_tercero;
	String rut_tercero;
	String rzn_social_tercero;
	String tdo_id_vta;
	String doc_folio_vta;
	String doc_rut_receptor_vta;
	String doc_fecha_emision_vta;
	String doc_monto_vta;
	
	 
	
	

	public ConexionSqlWeb() throws ClassNotFoundException, SQLException, IOException {
    	conexionServer = new ConexionServerWeb();
    	this.conServer = conexionServer.getConexion();
	}

	public void cerrarConexion() throws SQLException {
		conServer.close();
	}

	/**
	 * 
	 * Se crea documento plano en la ruta : /usr/local/F_E/DTE/**emp_id/**docID.txt
	 * Se obtiene TED
	 * 
	 * @param docId ID de documento
	 * @param nombre destino de documento TXT
	 * @return TED del documento correspondiente
	 * @throws SQLException
	 * @throws IOException
	 */
	public String obtenerArchivoVenta(String docId, String destinoTXT,Integer emp_id) throws SQLException, IOException {

		Statement statement = conServer.createStatement();
		ResultSet resultSet;
		String sql = "SELECT dex_ted,dex_doc_plano " +
				     "FROM dte_emi_documento a INNER JOIN  dte_emi_xmls b USING(doc_id) " +
				     "WHERE a.doc_id = "+docId+" AND a.emp_id = "+emp_id;

		resultSet = statement.executeQuery(sql);
		File file = new File(destinoTXT);
//		file.setExecutable(true,false);
//		file.setReadable(true,false);
//		file.setWritable(true,false);
		
		Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
		perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_WRITE);
//        perms.add(PosixFilePermission.OWNER_EXECUTE);
        //add group permissions
        perms.add(PosixFilePermission.GROUP_READ);
        perms.add(PosixFilePermission.GROUP_WRITE);
//        perms.add(PosixFilePermission.GROUP_EXECUTE);
        //add others permissions
        perms.add(PosixFilePermission.OTHERS_READ);
        perms.add(PosixFilePermission.OTHERS_WRITE);
//        perms.add(PosixFilePermission.OTHERS_EXECUTE);

		FileOutputStream fileOutputStream = new FileOutputStream(file);
		Files.setPosixFilePermissions(Paths.get(destinoTXT), perms);
		String ted="";

		if (resultSet.next()){
			ted=resultSet.getString("dex_ted");
			java.io.ByteArrayInputStream inputStream = (java.io.ByteArrayInputStream) resultSet.getBinaryStream("dex_doc_plano");
			byte[] buffer = new byte[1];
			while(inputStream.read(buffer)>0){
				fileOutputStream.write(buffer);
			}
		}else{
			
			System.out.println("No se encuentra el registro indicado");
		}
	return ted;
	}

	public void obtenerArchivoCompra(String docId, String destinoXML) throws SQLException, IOException {

		Statement statement = conServer.createStatement();
		ResultSet resultSet;
		String sql = "SELECT dex_xml FROM dte_rec_xmls WHERE doc_id= "+docId;
		resultSet = statement.executeQuery(sql);
		File file = new File(destinoXML);
//		LectorFichero lectorFichero = new LectorFichero();		
		FileOutputStream fileOutputStream = new FileOutputStream(file);

//		Files.setPosixFilePermissions(Paths.get(destinoXML),lectorFichero.permisos());
		if (resultSet.next()){

			java.io.ByteArrayInputStream inputStream = (java.io.ByteArrayInputStream) resultSet.getBinaryStream("dex_xml");
			byte[] buffer = new byte[1];
			while(inputStream.read(buffer)>0){
				fileOutputStream.write(buffer);
			}
		}else{
			
			System.out.println("No se encuentra el registro indicado");
		}
		statement.close();
		resultSet.close();
	}
	
	/**
	 * Obtiene información de DTE de Venta
	 * @param docId
	 * @throws SQLException
	 * @throws IOException
	 */
	public void obtenerArchivoVenta(String docId) throws SQLException, IOException {

		Statement statement = conServer.createStatement();
		ResultSet resultSet;
		String sql = "SELECT doc_id,tdo_id,doc_folio,doc_rut_receptor,doc_fecha_emision,doc_monto FROM dte_emi_documento WHERE doc_id= "+docId;
		resultSet = statement.executeQuery(sql);

		if (resultSet.next()){
			this.setTdo_id_vta(resultSet.getString("tdo_id"));
			this.setDoc_folio_vta(resultSet.getString("doc_folio"));
			this.setDoc_rut_receptor_vta(resultSet.getString("doc_rut_receptor"));
			this.setDoc_fecha_emision_vta(resultSet.getString("doc_fecha_emision"));
			this.setDoc_monto_vta(resultSet.getString("doc_monto"));
			
		}else{	
			System.out.println("No se encuentra el registro indicado");
		}
		statement.close();
		resultSet.close();
	}
	
	/**
	 * @param docId ID del documento en bd
	 * @param destinoXML incluye extension xml
	 * @param emp_id ID EMP del documento en bd
	 * @throws SQLException
	 * @throws IOException
	 */
	public void obtenerArchivoTercero(String docId, String destinoXML,Integer emp_id) throws SQLException, IOException {

		Statement statement = conServer.createStatement();
		ResultSet resultSet;
		String sql = "SELECT dex_xml,doc_rut_emisor " +
				     "FROM dte_rec_documento a INNER JOIN  dte_rec_xmls b USING(doc_id) " +
				     "WHERE a.doc_id = "+docId+" AND a.emp_id = "+emp_id;

		resultSet = statement.executeQuery(sql);
		File file = new File(destinoXML);
		Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
		perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_WRITE);
        perms.add(PosixFilePermission.GROUP_READ);
        perms.add(PosixFilePermission.GROUP_WRITE);
        perms.add(PosixFilePermission.OTHERS_READ);
        perms.add(PosixFilePermission.OTHERS_WRITE);

		FileOutputStream fileOutputStream = new FileOutputStream(file);
		Files.setPosixFilePermissions(Paths.get(destinoXML), perms);

		
		if (resultSet.next()){
			
			java.io.ByteArrayInputStream inputStream = (java.io.ByteArrayInputStream) resultSet.getBinaryStream("dex_xml");
			this.setRut_tercero(resultSet.getString("doc_rut_emisor"));
			byte[] buffer = new byte[1];
			while(inputStream.read(buffer)>0){
				fileOutputStream.write(buffer);
			}
		}else{			
			System.out.println("No se encuentra el registro indicado");
		}
	}

	public void obtenerDatos(int empID) throws SQLException, IOException {

		Statement statement = conServer.createStatement();
		ResultSet resultSet;
		String sql = "SELECT emp_rut,emp_rut_rlegal,emp_nombre_rlegal,emp_telefono_contacto,emp_pass_cert, emp_nro_resolucion,emp_fch_resolucion,emp_ccsii_username,emp_ccsii_password FROM sys_empresa WHERE emp_id="+empID;

		resultSet = statement.executeQuery(sql);

		if (resultSet.next()){

			this.setEmp_rut(resultSet.getString("emp_rut"));
			this.setEmp_rut_rep_legal(resultSet.getString("emp_rut_rlegal"));
			this.setClaveCert(resultSet.getString("emp_pass_cert"));
			this.setNroResolucion(resultSet.getString("emp_nro_resolucion"));
			this.setFchResolucion(resultSet.getString("emp_fch_resolucion"));
			this.setUsername(resultSet.getString("emp_ccsii_username"));
			this.setPassword(resultSet.getString("emp_ccsii_password"));
			this.setNom_rep_legal(resultSet.getString("emp_nombre_rlegal"));
			this.setTel_contacto(resultSet.getString("emp_telefono_contacto"));

		}else{
			
			System.out.println("No se encuentra el registro indicado");
		}

	}
	
	
	public ArrayList<Integer> cambiarDocEstadoSII(int empID) throws SQLException, IOException {

		Statement statement = conServer.createStatement();
		ResultSet resultSet;
		String sql = "SELECT * FROM dte_emi_documento where emp_id="+empID+" and doc_estado='ENVIADO'";
		ArrayList<Integer> docID = new ArrayList<>();

		resultSet = statement.executeQuery(sql);

		while (resultSet.next()){
			
			docID.add(resultSet.getInt("doc_id"));
//			System.out.println(resultSet.getInt("doc_id"));

		}
//		resultSet.close();
//		statement.close();

		return docID;
	}
	
	
	
	public void obtenerDatosEmpIntercambio(String rutTercero) throws SQLException, IOException {

		Statement statement = conServer.createStatement();
		ResultSet resultSet;
		String sql = "SELECT emi_rut,emi_razon_social,emi_mail FROM sys_empresas_intercambio WHERE emi_rut='"+rutTercero+"'";

		resultSet = statement.executeQuery(sql);

		if (resultSet.next()){

			this.setUser_name_tercero(resultSet.getString("emi_mail"));
			this.setRzn_social_tercero(resultSet.getString("emi_razon_social"));

		}else{
			
			System.out.println("No se encuentra el registro indicado");
		}

	}
	
	public void updateCodigoEnvio(String doc_id,String cod_envio) throws SQLException {
		Statement comando = this.conServer.createStatement();
	    comando.executeUpdate("UPDATE dte_rec_documento SET doc_cod_envio='"+cod_envio+"' WHERE doc_id="+doc_id);
	    if (comando!=null){
	    	comando.close();
	    }
	    
	}
	public void updateEstadoAcuse(String doc_id,String estado) throws SQLException {
		Statement comando = this.conServer.createStatement();
	    comando.executeUpdate("UPDATE dte_rec_documento SET doc_estado='"+estado+"' WHERE doc_id="+doc_id);
	    if (comando!=null){
	    	comando.close();
	    }
	}
	
	/**
	 * Cambia Estado de la columna doc_estado con la respuesta entregado a traves de WS por SII
	 * @param doc_id
	 * @param estado
	 * @throws SQLException
	 */
	public void updateEstadoRespSII(String doc_id,String estado) throws SQLException {
		Statement comando = this.conServer.createStatement();
	    comando.executeUpdate("UPDATE dte_emi_documento SET doc_estado='"+estado+"' WHERE doc_id="+doc_id);
	    if (comando!=null){
	    	comando.close();
	    }
	}

	public String getClaveCert() {
		return claveCert;
	}

	public void setClaveCert(String claveCert) {
		this.claveCert = claveCert;
	}
	public String getNroResolucion() {
		return nroResolucion;
	}

	public void setNroResolucion(String nroResolucion) {
		this.nroResolucion = nroResolucion;
	}

	public String getFchResolucion() {
		return fchResolucion;
	}

	public void setFchResolucion(String fchResolucion) {
		this.fchResolucion = fchResolucion;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmp_rut() {
		return emp_rut;
	}

	public void setEmp_rut(String emp_rut) {
		this.emp_rut = emp_rut;
	}

	public String getEmp_rut_rep_legal() {
		return emp_rut_rep_legal;
	}

	public void setEmp_rut_rep_legal(String emp_rut_rep_legal) {
		this.emp_rut_rep_legal = emp_rut_rep_legal;
	}
	public String getNom_rep_legal() {
		return nom_rep_legal;
	}

	public void setNom_rep_legal(String nom_rep_legal) {
		this.nom_rep_legal = nom_rep_legal;
	}

	public String getTel_contacto() {
		return tel_contacto;
	}

	public void setTel_contacto(String tel_contacto) {
		this.tel_contacto = tel_contacto;
	}
	public String getUser_name_tercero() {
		return user_name_tercero;
	}

	public void setUser_name_tercero(String user_name_tercero) {
		this.user_name_tercero = user_name_tercero;
	}

	public String getRut_tercero() {
		return rut_tercero;
	}

	public void setRut_tercero(String rut_tercero) {
		this.rut_tercero = rut_tercero;
	}
	public String getRzn_social_tercero() {
		return rzn_social_tercero;
	}

	public void setRzn_social_tercero(String rzn_social_tercero) {
		this.rzn_social_tercero = rzn_social_tercero;
	}
	
	public String getTdo_id_vta() {
		return tdo_id_vta;
	}

	public void setTdo_id_vta(String tdo_id_vta) {
		this.tdo_id_vta = tdo_id_vta;
	}

	public String getDoc_folio_vta() {
		return doc_folio_vta;
	}

	public void setDoc_folio_vta(String doc_folio_vta) {
		this.doc_folio_vta = doc_folio_vta;
	}

	public String getDoc_rut_receptor_vta() {
		return doc_rut_receptor_vta;
	}

	public void setDoc_rut_receptor_vta(String doc_rut_receptor_vta) {
		this.doc_rut_receptor_vta = doc_rut_receptor_vta;
	}

	public String getDoc_fecha_emision_vta() {
		return doc_fecha_emision_vta;
	}

	public void setDoc_fecha_emision_vta(String doc_fecha_emision_vta) {
		this.doc_fecha_emision_vta = doc_fecha_emision_vta;
	}

	public String getDoc_monto_vta() {
		return doc_monto_vta;
	}

	public void setDoc_monto_vta(String doc_monto_vta) {
		this.doc_monto_vta = doc_monto_vta;
	}

}