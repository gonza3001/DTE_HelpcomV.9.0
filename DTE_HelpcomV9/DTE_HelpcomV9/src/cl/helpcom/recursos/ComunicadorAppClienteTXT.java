package cl.helpcom.recursos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.Ostermiller.util.StringTokenizer;
import com.itextpdf.text.log.Logger;

/**
 * @author Mauricio Rodriguez
 *
 */
public class ComunicadorAppClienteTXT {

	private Integer cantidadItem;
	private Integer cantidadReferencias;
	private Logger log ;
	private Integer cantidadTotales=0;
	private Integer cantidadSegmentos=0;
	private Integer cantidadDetallesTotales=0;
	private Integer[] tipoDocumento= {29,30,32,33,34,35,38,39,40,41,43,45,46,48,53,55,56,60,61,101,102,103,104,105,106,108,109,110,111,112,175,180,185,900,901,902,903,904,905,906,907,909,910,911,914,918,919,920,921,922,924,500,501};
	//BOLETA
	private Integer cantidadTotalesConsumo=0;
	private Integer cantidadDetalleConsumoAnulado=0;
	private Integer cantidadDetalleConsumo=0;
	
	public ComunicadorAppClienteTXT() {

	}

	public String[] FormatoA(String base, String[] arrayLineasA) {

		arrayLineasA = new String[60];
		System.out.println("la ruta es " + base);
		File f = new File(base);
		BufferedReader entrada = null;

		try {
			entrada = new BufferedReader(new FileReader(f));
			String linea = null;
			int i = 0;
			int inicio = 0;
			int fin = 0;
			Integer espacio = 29;// cantidad de espacios entre Etiqueta y
									// descripcion
			int contadorA = 0;
			int contadorBx = 0;
			int contadorBy = 0;

			while (entrada.ready()) {

				linea = entrada.readLine();
				String palabra = "";
				inicio = linea.indexOf(" ");
				String datosAaux;
				String[] datosBaux = new String[14];

				// TIPO H
				if (linea.substring(0, inicio + 1).equals("H: ")) {
					// System.out.println(linea.substring(2).trim());// Muestra
					// los
					// comentarios
				}
				// TIPO A
				if (linea.substring(0, inicio + 1).equals("A: ")) {

					try {
						datosAaux = linea.substring(21).trim(); // leer desde la
																// posicion 21

						arrayLineasA[contadorA] = datosAaux;// almacenar

						// System.out.println("TipoDTE:" +
						// arrayLineasA[contadorA] + "**");
						contadorA++;

					} catch (Exception e) {

						System.out.println("Error de sintaxis en A:");

					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				entrada.close();
			} catch (IOException e1) {
			}
		}

		return arrayLineasA;
	}

	public String[][] FormatoB(String base, String[][] arrayLineasB) {

		arrayLineasB = new String[25][20];
		File f = new File(base);
		BufferedReader entrada = null;

		try {

			entrada = new BufferedReader(new FileReader(f));
			String linea = null;

			int i = 0;
			int inicio = 0;
			int fin = 0;
			Integer espacio = 29;// cantidad de espacios entre Etiqueta y
									// descripcion
			int contadorBx = 0;
			int contadorBy = 0;

			while (entrada.ready()) {

				linea = entrada.readLine();
				String palabra = "";

				inicio = linea.indexOf(" ");

				String[] datosBaux = new String[14];

				// TIPO BB
				if (linea.substring(0, inicio + 1).equals("B: ")) {

					try {

						datosBaux[0] = linea.substring(3, 7).trim();
						datosBaux[1] = linea.substring(10, 35).trim();
						datosBaux[2] = linea.substring(36, 136).trim();
						datosBaux[3] = linea.substring(137, 147).trim();
						datosBaux[4] = linea.substring(148, 158).trim();
						datosBaux[5] = linea.substring(159, 176).trim();
						datosBaux[6] = linea.substring(177, 194).trim();
						datosBaux[7] = linea.substring(195, 205).trim();
						datosBaux[8] = linea.substring(206, 216).trim();
						datosBaux[9] = linea.substring(217, 234).trim();
						datosBaux[10] = linea.substring(235, 243).trim();
						datosBaux[11] = linea.substring(244, 261).trim();
						// datosBaux[12] = linea.substring(262,264).trim();

						contadorBx = 0;

						// System.out.println("\n PRODUCTO"+ (contadorBy+1));
						while (contadorBx < 12) {

							arrayLineasB[contadorBy][contadorBx] = datosBaux[contadorBx];

//							System.out.println("Dato" + (contadorBx + 1) + ": "+ arrayLineasB[contadorBy][contadorBx]);

							contadorBx++;
						}
						contadorBy++;
						this.setCantidadItem(contadorBy);

					} catch (Exception e) {

						System.out.println("Error de sintaxis en B:");

					}
				}

				else {

				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				entrada.close();
			} catch (IOException e1) {
			}
		}

		return arrayLineasB;
	}

	public String[][] FormatoR(String base, String[][] arrayLineasR) {

		arrayLineasR = new String[25][20];
		File f = new File(base);
		BufferedReader entrada = null;

		try {

			entrada = new BufferedReader(new FileReader(f));
			String linea = null;

			int i = 0;
			int inicio = 0;
			int fin = 0;
			Integer espacio = 29;// cantidad de espacios entre Etiqueta y
									// descripcion
			int contadorRx = 0;
			int contadorRy = 0;

			while (entrada.ready()) {

				linea = entrada.readLine();
				String palabra = "";

				inicio = linea.indexOf(" ");

				String[] datosRaux = new String[14];

				// TIPO R
				if (linea.substring(0, inicio + 1).equals("R: ")) {

					try {

						datosRaux[0] = linea.substring(3, 7).trim(); // Corr
						datosRaux[1] = linea.substring(10, 20).trim(); // Tipo
																		// Sii
						datosRaux[2] = linea.substring(21, 39).trim(); // Folio
						datosRaux[3] = linea.substring(42, 52).trim(); // Fecha
						datosRaux[4] = linea.substring(53, 63).trim(); // Cod
																		// Ref
						datosRaux[5] = linea.substring(64, 109).trim(); // Razon
																		// Ref

						contadorRx = 0;

						// System.out.println("\n PRODUCTO"+ (contadorBy+1));
						while (contadorRx < 6) {

							arrayLineasR[contadorRy][contadorRx] = datosRaux[contadorRx];

							// System.out.println("Dato"+(contadorRx+1)+": "+arrayLineasR[contadorRy][contadorRx]);

							contadorRx++;
						}
						contadorRy++;

						this.setCantidadReferencias(contadorRy);
					} catch (Exception e) {

						System.out.println("Error de sintaxis en R:");

					}
				}

				else {

				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				entrada.close();
			} catch (IOException e1) {
			}
		}

		return arrayLineasR;
	}
	/**
	 * @param base Documento plano a leer
	 * @param arrayLineas Arreglo donde se almacenarán los valores obtenidos del Documento Planos
	 * @param nomLinea Letra de la linea que se espera leer
	 * @return Se retorna un arreglo con los valores obtenidos de Caratula,Totales y Detalles del Libro
	 * @throws IOException
	 */
	public ArrayList<ArrayList<String>>  formatoLibroCVLineas(String base,ArrayList<ArrayList<String>> arrayLineas,String nomLinea)throws IOException {

		File fileBase = new File(base);
		BufferedReader entrada = null;
		String linea = "";
		int contToken = 0;
		int contY=0;
		StringTokenizer st;
		entrada = new BufferedReader(new FileReader(fileBase));


		try{
			while (entrada.ready()) {

			linea = entrada.readLine();//Capturo la linea
			//System.out.println("linea"+ linea);
			
			if(linea.substring(0, 1).equals(nomLinea)){
				st = new StringTokenizer(linea.substring(2), ";");//separador, lee desde la 2da posicion
			
				arrayLineas.add(new ArrayList<String>());
				while (st.hasMoreTokens()) {
					String linea2 =st.nextToken();
					
					arrayLineas.get(contY).add(linea2);
				   
					
					//System.out.println("Salida///:::: "+ arrayLineas.get(contY).add());
				
				contToken++;
				}
				contY++;
				contToken=0;

				if (linea.substring(0, 1).equals("T")){
					this.setCantidadTotales(contY);
				}
				if (linea.substring(0, 1).equals("D")){
					this.setCantidadDetallesTotales(contY);
				}
				if (linea.substring(0, 1).equals("S")){
					this.setCantidadSegmentos(contY);
				}
			}


			}

		}
		catch(Exception e){

		}

		return arrayLineas;
	}
	
	public ArrayList<ArrayList<String>>  formatoBoletasConsumoFolio(String base,ArrayList<ArrayList<String>> arrayLineas,String nomLinea)throws IOException {

		File fileBase = new File(base);
		BufferedReader entrada = null;
		String linea = "";
		int contToken = 0;
		int contY=0;
		StringTokenizer st;
		entrada = new BufferedReader(new FileReader(fileBase));


		try{
			while (entrada.ready()) {

			linea = entrada.readLine();//Capturo la linea
			//System.out.println("linea"+ linea);
			
			if(linea.substring(0, 1).equals(nomLinea)){
				st = new StringTokenizer(linea.substring(2), ";");//separador, lee desde la 2da posicion
			
				arrayLineas.add(new ArrayList<String>());
				while (st.hasMoreTokens()) {
					String linea2 =st.nextToken();
					
					arrayLineas.get(contY).add(linea2);
				   
					
					//System.out.println("Salida///:::: "+ arrayLineas.get(contY).add());
				
				contToken++;
				}
				contY++;
				contToken=0;

				
				if (linea.substring(0, 1).equals("T")){
					this.setCantidadTotalesConsumo(contY);
				}
				if (linea.substring(0, 1).equals("A")){
					this.setCantidadDetalleConsumoAnulado(contY);
				}
				if (linea.substring(0, 1).equals("D")){
					this.setCantidadDetalleConsumo(contY);
				}
			}


			}

		}
		catch(Exception e){

		}

		return arrayLineas;
	}
	
	public String[] separarCodImp(String codigos){
		String aux=codigos;
		String[] datos = aux.split(",");
		
		return datos;
	};
	public String[] separarCodImpTasa(String codigos){
		String aux=codigos;
		String[] datos = aux.split(",");
		
		return datos;
	};
	public String[] separarMntImp(String montos){
		String aux=montos;
		String[] datos = aux.split(",");

		return datos;
	};
	
	
	

	public Integer getCantidadItem() {
		return cantidadItem;
	}

	public void setCantidadItem(Integer cantidadItem) {
		this.cantidadItem = cantidadItem;
	}

	public Integer getCantidadReferencias() {
		return cantidadReferencias;
	}

	public void setCantidadReferencias(Integer cantidadReferencias) {
		this.cantidadReferencias = cantidadReferencias;
	}

	public int getCantidadProductos(String[][] arrayB) {

		return 1;
	}

	public Integer getCantidadTotales() {

		return cantidadTotales;
	}

	public void setCantidadTotales(Integer cantidadTotales) {
		this.cantidadTotales = cantidadTotales;
	}

	public Integer getCantidadDetallesTotales() {
		return cantidadDetallesTotales;
	}

	public void setCantidadDetallesTotales(Integer cantidadDetallesTotales) {
		this.cantidadDetallesTotales = cantidadDetallesTotales;
	}
	public Integer getCantidadSegmentos() {
		return cantidadSegmentos;
	}

	public void setCantidadSegmentos(Integer cantidadSegmentos) {
		this.cantidadSegmentos = cantidadSegmentos;
	}
	
	public Integer getCantidadTotalesConsumo() {
		return cantidadTotalesConsumo;
	}

	public void setCantidadTotalesConsumo(Integer cantidadTotalesConsumo) {
		this.cantidadTotalesConsumo = cantidadTotalesConsumo;
	}


	public Integer getCantidadDetalleConsumo() {
		return cantidadDetalleConsumo;
	}

	public void setCantidadDetalleConsumo(Integer cantidadDetalleConsumo) {
		this.cantidadDetalleConsumo = cantidadDetalleConsumo;
	}
	public Integer getCantidadDetalleConsumoAnulado() {
		return cantidadDetalleConsumoAnulado;
	}

	public void setCantidadDetalleConsumoAnulado(Integer cantidadDetalleConsumoAnulado) {
		this.cantidadDetalleConsumoAnulado = cantidadDetalleConsumoAnulado;
	}

}










