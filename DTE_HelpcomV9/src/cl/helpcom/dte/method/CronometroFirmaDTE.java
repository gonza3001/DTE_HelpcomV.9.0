package cl.helpcom.dte.method;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.util.*;

import cl.helpcom.dao.ConexionSqlFirma;
import cl.nic.dte.util.Pinging;

public class CronometroFirmaDTE extends Thread { //una clase que hereda de la clase Thread
	private Properties propiedades = new Properties();
    private InputStream entrada = null;
    private ServerSocket  sSocket;
    public CronometroFirmaDTE(){// Contructor porque la clase es heredada
        super();
    }

    public void run() {
    try {

        entrada = new FileInputStream("/usr/local/F_E/Configuraciones/Propiedades/configuracionServidor.properties");
        // cargamos el archivo de propiedades
        propiedades.load(entrada);

        try {
        	Pinging ping = new Pinging();

        	for (;;){
        		if (ping.getConnectionStatus().equals("Online")){
            		//Se crea una conxeion c/cierto tiempo
            		ConexionSqlFirma sqlFirma = new ConexionSqlFirma();
            		sSocket = new ServerSocket(1331);
            		System.out.println("Ejecutando FirmaDTE");
        			sqlFirma.getIDEmpresas();//Firma los documentos por empresa
                   	sqlFirma.cerrarConexionServer();
                   	
                   	sSocket.close();
        		}
        		try {
        			System.out.println("CronometroFirmaDTE.run()");
    				Thread.sleep(Long.valueOf(propiedades.getProperty("TIEMPO_ESPERA_FIRMA_DTE")));
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