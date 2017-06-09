package cl.helpcom.dte.method;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.net.ServerSocket;
import java.text.ParseException;

import java.util.Properties;

import org.apache.xmlbeans.XmlException;

import cl.helpcom.recursos.LectorFichero;
import cl.nic.dte.util.Pinging;


public class CronometroCreaDTE extends Thread { //una clase que hereda de la clase Thread
	private Properties propiedades = new Properties();
    private InputStream entrada = null;
    private File fileTmp= null;
    private File directorioBase= null;
    private CreaDTEv3 dte= new CreaDTEv3();
    private ServerSocket  sSocket;
    private LectorFichero lectorFichero = new LectorFichero();

    public CronometroCreaDTE(){// Contructor porque la clase es heredada
        super();
    }

    public void run() {

    try {

    	entrada = new FileInputStream("/usr/local/F_E/Configuraciones/Propiedades/configuracionCreaDTE.properties");
        // cargamos el archivo de propiedades
        propiedades.load(entrada);

        Pinging ping = new Pinging();
           /**PID**/
//           String id = ManagementFactory.getRuntimeMXBean().getName();
//		   String[] ids = id.split("@");
//		   System.out.println("NUMERO DE PROCESO CREA: "+Integer.parseInt(ids[0]));
//		   System.out.println("ANT"+propiedades.getProperty("PID_CREA_DTE"));
//		   propiedades.setProperty("PID_CREA_DTE", ""+Integer.parseInt(ids[0]));
//		   propiedades.store(new FileWriter("/usr/local/F_E/Configuraciones/Propiedades/configuracionCreaDTE.properties"),"");

//        try {
        	for(;;){
        	 directorioBase= new File(propiedades.getProperty("RUTA_BASE"));
      		 File[] ficheros = directorioBase.listFiles();
      		 for(int a=0;a<ficheros.length;a++){
	             if (ficheros.length>0){//Si hay archivos BASE

	            	 dte.leerZ();
	            	 if (dte.getZetaFin().equals(true)){
		            	   sSocket = new ServerSocket(1239);//instancia una ejecucion
		              	   dte.crearDTE();
		              	   sSocket.close();
		             }else {
		            	 Thread.sleep(Long.valueOf("1000"));
					}
	            }
      		 }
      		try {
      			System.out.println("Esperando Base...");
				Thread.sleep(Long.valueOf(propiedades.getProperty("TIEMPO_ESPERA_CREA_DTE")));
			} catch (InterruptedException e) {
				System.out.println("Error en Hilo");
				e.printStackTrace();
			}
      		 }
//        } catch (Exception ex) {
//        	 //lectorFichero.crearFicheroError(propiedades.getProperty("RUTA_ERROR"),"\t"+"[CREA DTE] \t"+ dte.getDocumento()+"\t"+ ex.getMessage());//ruta - Nombre archivo - msj error
//             System.out.println("ERROR 1:"+ ex.getMessage());//Imprima el error
//             System.out.println("ERROR 2:"+ ex.getLocalizedMessage());
//             System.out.println("ERROR 3:"+ ex.getCause());
//             System.out.println("ERROR 3:"+ ex.getStackTrace());
//             System.out.println("ERROR 3:"+ ex.getSuppressed());
//        }
    } catch (IOException | ParseException | XmlException ex) {
        ex.printStackTrace();
    } catch (NumberFormatException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
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