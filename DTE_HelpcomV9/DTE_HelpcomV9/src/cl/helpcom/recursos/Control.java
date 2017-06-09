package cl.helpcom.recursos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;
/**
 * @author Mauricio Rodriguez
 */
public class Control {

 public void abrirPuerto(){
	 ServerSocket  sSocket;

	 try {
		 sSocket = new ServerSocket(1335);
		 System.out.println("Es la primera instancia de la aplicación...");

		} catch (IOException x) {
		  System.out.println("Otra instancia de la aplicación se está ejecutando...");
		}
 }

}//--> fin clase
