package cl.helpcom.recursos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * @author mau
 *
 */
public class LectorFichero {

	private int tipoDte;
	private int folioDte;

	private String nomDocumento="";


	public String leerDocumentoTXT(String rutaDoctumentoTXT ){

		String rutaOUT="";
		File dir = new File(rutaDoctumentoTXT);
		String[] ficheros = dir.list();

			if (ficheros == null)
				  System.out.println("No hay ficheros en el directorio especificado");
				else {
				  for (int aux=0;aux<ficheros.length;aux++)

				  rutaOUT= rutaDoctumentoTXT+ficheros[aux];
				  //System.out.println(ficheros[aux]);
				}
		return rutaOUT;
	}
	public String respuestaAcuseRecibo( String numero){

		ArrayList<String> out = new ArrayList<>();
		out.add("Envio Recibido Conforme");
		out.add("Envio Rechazado - Error de Schema");
		out.add("Envio Rechazado - Error de Firma");
		out.add("Envio Rechazado - RUT Receptor No Corresponde");
		out.set(90, "Envio Rechazado - Archivo Repetido");
		out.set(91, "Envio Rechazado - Archivo Ilegible");
		out.set(99, "Envio Rechazado - Otros");
		
		String out1="";
		try {
			out1=out.get(Integer.valueOf(numero));
		} catch (Exception e) {
			out1=out.get(1);
		}
		return out1;
	}

	/**
	 * Cambiar de destino documento
	 * @param rutaDocumentoPkg
	 * @param destino
	 */
	public void moverFichero(String rutaDocumentoPkg,String destino){

		File dir = new File(rutaDocumentoPkg);
		dir.renameTo(new File (destino + dir.getName()));
	}


	/**
	 * Borra documento
	 * @param rutaDoctumentoTXT ruta del documento a borrar
	 */
	public void borraDocumentoTXT(String rutaDoctumentoTXT){

		File dir = new File(rutaDoctumentoTXT);
		dir.delete();

	}
	public void crearTemporal(String rutaDocumentoTemporal){

		File dir = new File(rutaDocumentoTemporal);
		dir.mkdir();

	}
	public void borrarTemporal (String rutaDocumentoTemporal){

		File dir = new File(rutaDocumentoTemporal);
		dir.delete();

	}
	/**
	 * Utilizado para crear carpeta tmp al desglozar de acuse
	 * @param rutaDocumentoTemporal
	 */
	public void crearTemporalAcuse(String rutaDocumentoTemporal){

		File dir = new File(rutaDocumentoTemporal);

		if (dir.exists()){
			
		}else {
			dir.mkdir();
		}
		

	}

	 /**
	  * Se crean carpetas con formato MM DD actuales
	 * @param rutaFolder uri Destino (sin /)
	 * @return
	 */
	public String crearFicheroDTE (String rutaFolder){

		 	Calendar fecha = Calendar.getInstance();
			int ano = fecha.get(Calendar.YEAR);
			int mes = fecha.get(Calendar.MONTH) + 1;
			rutaFolder = rutaFolder +"/"+ ano + "/" + mes;

			File folder = new File(rutaFolder);

			if (folder.exists()) {
				//carpeta correcta

			} else {
				// folder.mkdir();
				folder.mkdirs();
				//Se crea una nueva carpeta
			}

		 return rutaFolder;
	 }
	 /**
	  * Se crean carpetas con formato AA MM definidos por usuario
	 * @param rutaFolder Ruta de destino sin /
	 * @param fecha Entrada de fecha en formato AA-MM-DD
	 * @return
	 * @throws IOException 
	 */
	public String crearFicheroMMDDFlex (String rutaFolder,String fecha) throws IOException{

		String aux="";
		String anio="";String mes="";
		aux=fecha;
		anio= fecha.substring(0,fecha.indexOf("-"));//mm-dd
		aux=aux.substring(aux.indexOf("-")+1);
		mes=aux.substring(0,fecha.indexOf("-")-2);

		rutaFolder = rutaFolder +"/"+ anio + "/" + Integer.valueOf(mes);

		File folder = new File(rutaFolder);

		if (folder.exists()) {
				//carpeta correcta
			} else {
				// folder.mkdir();
				folder.mkdirs();
				Files.setPosixFilePermissions(Paths.get(rutaFolder), this.permisos());
				//Se crea una nueva carpeta
			}

		 return rutaFolder;
	 }
	
	/**
	 * Para asignar permisos a los directorios
	 * @return
	 */
	public Set<PosixFilePermission> permisos(){
		
		Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
		perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_WRITE);
        perms.add(PosixFilePermission.OWNER_EXECUTE);
        //add group permissions
        perms.add(PosixFilePermission.GROUP_READ);
        perms.add(PosixFilePermission.GROUP_WRITE);
        perms.add(PosixFilePermission.GROUP_EXECUTE);
        //add others permissions
        perms.add(PosixFilePermission.OTHERS_READ);
        perms.add(PosixFilePermission.OTHERS_WRITE);
        perms.add(PosixFilePermission.OTHERS_EXECUTE);
		
        return perms;
	}



	/**
	 *
	 * Lee una fecha y encuentra la ruta de la carpeta /AA/MM/
	 * ejemplo 2016/7/
	 * @param rutaFolder
	 * @param fecha
	 * @return
	 */
	public String leerFechaARutaAAMM (String fecha){

		String aux="";
		String out="";

		String anio="";String mes="";
		aux=fecha;
		anio= fecha.substring(0,fecha.indexOf("-"));//mm-dd
		aux=aux.substring(aux.indexOf("-")+1);
		mes=aux.substring(0,fecha.indexOf("-")-2);
		out = anio + "/" + Integer.valueOf(mes)+"/";

		 return out;
	 }
	/**
	 * Invierte una fecha en formato AAAMMMDD a formato DDMMAAA
	 */
	public String invertirFecha(String fecha){
		
		String[] array = fecha.split("-");
		
		fecha=array[2]+"-"+array[1]+"-"+array[0];
		
		return fecha;
		
	}
	public void crearFicheroError (String rutaFolder, String mensaje) throws IOException{

			Calendar fecha = Calendar.getInstance();
		 	int ano = fecha.get(Calendar.YEAR);
			int mes = fecha.get(Calendar.MONTH) + 1;
			int dia = fecha.get(Calendar.DAY_OF_MONTH);
			int hora = fecha.get(Calendar.HOUR_OF_DAY);
			int min = fecha.get(Calendar.MINUTE);
			int seg = fecha.get(Calendar.SECOND);

			String fechaS = ano + "/" + mes+ "/"+dia+"   "+hora+ ":" + min + ":" + seg;

		 	File folder = new File(rutaFolder);
			BufferedWriter bw = new BufferedWriter(new FileWriter(folder,true));

			if (folder.exists()) {
				//carpeta correcta
				bw.write(fechaS+ "\t" +mensaje + "\n");
				bw.close();
			} else {
				// folder.mkdir();
				folder.mkdirs();
				//Se crea una nueva carpeta
				bw.write(fechaS+ "\t" +mensaje + "\n");
				bw.close();
			}


	 }

	/**
	 * Obtiene el Tipo de documento con el nombre del fichero
	 * @param nombre
	 */
	public String obtenerTipo(String nombre){

		if (nombre.substring(0,1).equals("9")){
			nombre= nombre.substring(1,3);
		}else {
			nombre =nombre.substring(0, 2);
		}
		return nombre;
	}

	public String obtenerFolio(String nombre){
		int pos=0;
		if (nombre.substring(0,1).equals("9")){
			nombre =nombre.substring(3);//se borra9569
			pos = nombre.indexOf(".");
			nombre=nombre.substring(0,pos);
		}else{
			nombre =nombre.substring(2);
			pos = nombre.indexOf(".");
			nombre=nombre.substring(0,pos);
		}
		return nombre;
	}



	 public String fileToString(String file) throws IOException{
		 StringBuilder content = new StringBuilder();

		 try (BufferedReader reader = Files.newBufferedReader(Paths.get(file), Charset.defaultCharset())) {

		 String line = null;

		 while ((line = reader.readLine()) != null) {

		 content.append(line).append("\n");

		 }

		 return content.toString();
		 }

	 }

	 public String getNombreArchivo(String in){
		
		 String out="";
		 String array[] = in.split("/");
		 out=array[array.length-1];//obtiene el nombre del archivo
		 out=out.substring(0,out.indexOf("."));
		 
		 return out;
	 }


	 public void borrarDirectoriosArray(ArrayList<String> directorios){
		 File a;
		 System.out.println(directorios.size());
		 for (String dir :directorios ){
			 a = new File(dir);
			 a.delete();
		 }

	 }

	 private int getTipoDte() {
			return tipoDte;
		}

		private void setTipoDte(int tipoDte) {
			this.tipoDte = tipoDte;
		}

		private int getFolioDte() {
			return folioDte;
		}

		private void setFolioDte(int folioDte) {
			this.folioDte = folioDte;
		}


}
