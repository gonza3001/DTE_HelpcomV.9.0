package cl.helpcom.dte.method;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.util.Properties;

import org.apache.commons.ssl.Ping;

import cl.helpcom.dao.ConexionSqlEnviaDocumento;
import cl.helpcom.dao.ConexionSqlGeneraEnvio;
import cl.helpcom.recursos.LectorFichero;
import cl.nic.dte.util.Pinging;

public class CronometroEnviaDocumento extends Thread { //una clase que hereda de la clase Thread
	private Properties propiedades = new Properties();
    private InputStream entrada = null;
    private File fileTmp= null;
    private File directorioEnvios= null;
    private ServerSocket  sSocket;

    public CronometroEnviaDocumento(){// Contructor porque la clase es heredada
        super();
    }

    public void run() {

    try {
        entrada = new FileInputStream("/usr/local/F_E/Configuraciones/Propiedades/configuracionServidor.properties");
        // cargamos el archivo de propiedades
        propiedades.load(entrada);

        try {
        	for(;;){
        	Pinging ping = new Pinging();
        		if (ping.getConnectionStatus().equals("Online")){

        				   sSocket = new ServerSocket(1333);
			           	   System.out.println("CronometroEnviaDocumento.run()");
			        	   ConexionSqlEnviaDocumento enviaDocumento = new ConexionSqlEnviaDocumento();
			        	   enviaDocumento.getIDEmpresas();
			        	   sSocket.close();
			        	   //generaEnvioSql.getIDEmpresas();

			           }else{
			        	   System.out.println("Revise conexiona Internet");
			           }
        		try {
    				Thread.sleep(Long.valueOf(propiedades.getProperty("TIEMPO_ENVIO")));
    			} catch (InterruptedException e) {
    				System.out.println("Error en Hilo");
    				e.printStackTrace();
    			}
        	}


        } catch (Exception ex) {
             System.out.println(ex.getMessage());//Imprima el error
        }

    } catch (IOException ex) {
        ex.printStackTrace();
    } finally {
        if (entrada != null) {
            try {
                entrada.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    }
    }