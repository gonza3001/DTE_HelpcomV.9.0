/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.helpcom.dao;

import cl.sii.siiDte.AUTORIZACIONDocument;
import cl.sii.siiDte.AutorizacionType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;


import org.xml.sax.SAXException;

/**
 *
 * @author Mauricio Rodriguez
 */
public class Conexion {

    private static Connection conexion;
    static String bd = "Base_de_Datos";
    static String user = "Usuario_Base_de_Datos";
    static String password = "Contraseña";
    static String server = "jdbc:mysql://localhost/" + bd;
    Properties propiedades = new Properties();

    //static String rut="";
    static boolean debug = true;

    public Conexion() throws ClassNotFoundException, SQLException, IOException {


    	Properties propiedades = new Properties();
		InputStream entrada = null;

//		entrada = new FileInputStream("Configuraciones/confMysqlLocal.properties");
		entrada = new FileInputStream("/usr/local/F_E/Configuraciones/Propiedades/configuracionMysql.properties");

		// cargamos el archivo de propiedades
		propiedades.load(entrada);

        server = propiedades.getProperty("SERVER").toString();
        bd = propiedades.getProperty("BASE_DATOS").toString();
        user = propiedades.getProperty("USUARIO").toString();
        password = propiedades.getProperty("PASSWORD").toString();

        Class.forName("com.mysql.jdbc.Driver");

        this.conexion = DriverManager.getConnection(server+"/"+bd, user, password);
        //conexion = DriverManager.getConnection(server, user, password);
       // System.out.println("Conexión a Servidor: \t" + server +"\t ESTABLECIDA");

    }

    public Connection getConexion() {
        return conexion;
    }

    public void cerrar(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
                System.out.print("No es posible cerrar la Conexion");
            }
        }
    }

    public void cerrar(java.sql.Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (Exception e) {
            }
        }
    }

    public void destruir() {

        if (conexion != null) {

            try {
                conexion.close();
            } catch (Exception e) {
            }
        }
    }
}
