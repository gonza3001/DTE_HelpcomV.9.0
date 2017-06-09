package cl.helpcom.dte.method;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.util.Properties;

import cl.helpcom.dao.ConexionSqlFirma;
import cl.helpcom.dao.ConexionSqlGeneraEnvio;
import cl.nic.dte.util.Pinging;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;


public class CronometroGeneraEnvio extends Thread { //una clase que hereda de la clase Thread
	private Properties propiedades = new Properties();
    private InputStream entrada = null;
    private ServerSocket  sSocket;
    public CronometroGeneraEnvio(){// Contructor porque la clase es heredada
        super();
    }
    public void run() {

    try {

        entrada = new FileInputStream("/usr/local/F_E/Configuraciones/Propiedades/configuracionServidor.properties");
        // cargamos el archivo de propiedades
        propiedades.load(entrada);

        try {
        	Pinging ping = new Pinging();
        		for(;;){
		            	if (ping.getConnectionStatus().equals("Online")){
		            	   ConexionSqlGeneraEnvio sqlGeneraEnvio = new ConexionSqlGeneraEnvio();
		            	   sSocket = new ServerSocket(1332);
		            	   System.out.println("Ejecutando Genera_Envio_DTE");
		            	   sqlGeneraEnvio.getIDEmpresas();
		            	   sqlGeneraEnvio.cerrarConexionServer();
		            	   sSocket.close();
		            	}
            	try {
          			System.out.println("CronometroGeneraEnvio.run()");
    				Thread.sleep(Long.valueOf(propiedades.getProperty("TIEMPO_ESPERA_GENERA_DTE")));
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