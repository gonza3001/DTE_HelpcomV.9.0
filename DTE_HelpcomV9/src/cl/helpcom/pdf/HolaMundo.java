package cl.helpcom.pdf;

/**
*@author Mauricio Rodríguez
 **/

import jargs.gnu.CmdLineParser;

public class HolaMundo {

	private static void printUsage() {
		System.err
				.println("Utilice: java cl.nic.dte.examples.GeneraPDF "
						+ "-p <plantilla.xsl> -o <resultado.pdf> <factura.xml>");
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

	ImpresoraTermica impresoraTermica = new ImpresoraTermica();
	String arbolCarpeta="/home/mau/Documentos/Termica/";
	String rutaTxt="/home/mau/Documentos/DTE/Palma/BASERespaldo/33-1.TXT";
	String TED="asdasd86s86d8a6s8a78dd68as7d60sd76a6sa60sd6a0d60as7d0a6sd60a6d0asd76a06sda";
	
//	
//	impresoraTermica.imprimirTicket(arbolCarpeta, rutaTxt,TED);
	

	}

}
