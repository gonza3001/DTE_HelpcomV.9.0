package cl.helpcom.recursos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.Ostermiller.util.StringTokenizer;

public class LectorDTEText {

/*private	String[] arrayEncabezadoID={"TipoDTE","Folio","FchEmis","IndNoRebaja","TipoDespacho","IndTraslado","TpoImpresion","IndServicio","MntBruto","FmaPago","FmaPagExp","FchCancel","MntCancel","SaldoInsol","MntPagos","FchPago","MntPago","GlosaPagos","PeriodoDesde","PeriodoHasta","MedioPago","TpoCtaPago","NumCtaPago","BcoPago","TermPagoCdg","TermPagoGlosa","TermPagoDias","FchVenc"};
private	String[] arrayEncabezadoEmisor={"RUTEmisor","RznSoc","GiroEmis","Telefono","CorreoEmis","Acteco","GuiaExport","CdgTraslado","FolioAut","FchAut","Sucursal","CdgSIISucur","DirOrigen","CmnaOrigen","CiudadOrigen","CdgVendedor","IdAdicEmisor"};
private	String[] arrayEncabezadoReceptor={"RUTRecep","CdgIntRecep","RznSocRecep","Extranjero","NumId","Identificacion","GiroRecep","Contacto","CorreoRecep","DirRecep","CmnaRecep","CiudadRecep","DirPostal","CmnaPostal","CiudadPostal"};
private	String[] arrayEncabezadoTransporte={"Patente","RUTTrans","RUTChofer","NombreChofer","DirDest","CmnaDest","CiudadDest","Aduana"};
private	String[] arrayEncabezadoTotales={"MntNeto","MntExe","MntBase","MntMargenCom","TasaIVA","IVA","IVAProp","IVATerc","ImptoReten","TipoImp","TasaImp","MontoImp","IVANoRet","CredEC","GrntDep","Comisiones","ValComNeto","ValComExe","ValComIVA","MntTotal","MontoNF","MontoPeriodo","SaldoAnterior","VlrPagar"};
private	String[] arrayEncabezadoOtraMoneda={"TpoMoneda","TpoCambio","MntNetoOtrMnda","MntExeOtrMnda","MntFaeCarneOtrMnda","MntMargComOtrMnda","IVAOtrMnda","ImpRetOtrMnda","IVANoRetOtrMnda","MntTotOtrMnda"};
//DETALLE
private String[] arrayDetalle={"NroLinDet","CdgItem","TpoCodigo","VlrCodigo","IndExe","Retenedor","IndAgente","MntBaseFaena","MntMargComer","PrcConsFinal","NmbItem","DscItem","QtyRef","UnmdRef","PrcRef","QtyItem","Subcantidad","SubQty","SubCod","FchElabor","FchVencim","UnmdItem","PrcItem","OtrMnda","PrcOtrMon","Moneda","FctConv","DscOtrMnda","RecargoOtrMnda","MontoItemOtrMnda","DescuentoPct","DescuentoMonto","SubDscto","ValorDscto","RecargoPct","RecargoMonto","SubRecargo","TipoRecargo","ValorRecargo","CodImpAdic","MontoItem"};
*/
	private String[] arrayEncabezado={"TipoDTE","Folio","FchEmis","IndNoRebaja","TipoDespacho","IndTraslado","TpoImpresion","IndServicio","MntBruto","FmaPago","MedioPago","RUTEmisor","RznSoc","GiroEmis","Telefono","CorreoEmisor","Acteco","DirOrigen","CmnaOrigen","CiudadOrigen","CdgVendedor","RUTRecep","RznSocRecep","GiroRecep","Contacto","CorreoRecep","DirRecep","CmnaRecep","CiudadRecep","Patente","RUTTrans","RUTChofer","NombreChofer","DirDest","CmnaDest","CiudadDest","MntNeto","MntExe","TasaIVA","IVA","IVAProp","IVATerc","TipoImp","TasaImp","MontoImp","IVANoRet","MntTotal","VlrPagar","IvaCarne","IvaHarina","IvaLicor","IvaVino","IvaCerveza","IvaSAzucar","IvaCAzucar","MedioPagoPDF","DatoAdic1","DatoAdic2","DatoAdic3","DatoAdic4","DatoAdic5","DatoAdic6","DatoAdic7","DatoAdic8"};
	private int cantidadDetalles;
	private int cantidadReferencias;
	private int cantidadDescuentos;
	private Boolean zeta=false;

	/**
	 * Lee la informacion del documento plano con tipo de formato vertical
	 * @param base Documento plano
	 * @param nomLinea Tipo de formato A
	 * @param salida Arreglo[][] vacio
	 * @return Arreglo[][] con informacion del documento plano
	 * @throws FileNotFoundException
	 */
	public ArrayList<String> formatoA_DTEs(String base,String nomLinea,ArrayList<String> salida2) throws FileNotFoundException{

		File fileBase = new File(base);
		BufferedReader entrada = null;
		String linea = "";
		StringTokenizer st;
		entrada = new BufferedReader(new FileReader(fileBase));
		String auxVariable; //se almacena permanentemente el nombre de la variable

		try{
			while (entrada.ready()) {
			linea = entrada.readLine();//Capturo la linea
			if(linea.substring(0, 1).equals(nomLinea)){
				auxVariable = linea.substring(3, 22).trim();
				for (int i = 0; i < arrayEncabezado.length; i++) {
					if (auxVariable.equals(arrayEncabezado[i])){
						salida2.add(linea.substring(22));

					}
				}}
			if (linea.substring(0, 1).equals("Z")){
				this.setZeta(true);//tiene fin de línea
			}
			}

		}catch(Exception e){}

		return salida2;
		}

	public ArrayList<ArrayList<String>> formatoB_DTEs(String base,ArrayList<ArrayList<String>> arrayLineas,String nomLinea)
			throws IOException {

		File fileBase = new File(base);
		BufferedReader entrada = null;
		String linea = "";
		int contToken = 0;
		int contY=0;
		StringTokenizer st;
		entrada = new BufferedReader(new FileReader(fileBase));

		Iterator<ArrayList<String>> iteraArrayLista = arrayLineas.iterator();

		try{
			while (entrada.ready()) {
			linea = entrada.readLine();//Capturo la linea
			ArrayList<String> lineaDetalle= new ArrayList<String>();
			if(linea.substring(0, 1).equals(nomLinea)){
				st = new StringTokenizer(linea.substring(2), ";");//separador, lee desde la 2da posicion
				while (st.hasMoreTokens()) {

							lineaDetalle.add(st.nextToken().replaceAll("\"", ""));
							contToken++;

				}

				arrayLineas.add(lineaDetalle);
				contY++;
				contToken=0;

				if (linea.substring(0, 1).equals("B")){
					this.setCantidadDetalles(contY);
				}
				if (linea.substring(0, 1).equals("R")){
					this.setCantidadReferencias(contY);
				}

			}
			}
		}
		catch(Exception e){

		}

		return arrayLineas;
	}

	public void formatoZ(String base) throws FileNotFoundException {

		File fileBase = new File(base);
		BufferedReader entrada = null;
		String linea = "";
		entrada = new BufferedReader(new FileReader(fileBase));
		try{
			while (entrada.ready()) {
			linea = entrada.readLine();//Capturo la linea
			if(linea.substring(0, 1).equals("Z")){
				this.setZeta(true);//tiene fin de línea
			}}

		}catch(Exception e){}
		//return z;
		}

	public ArrayList<ArrayList<String>> quitarDblCrm(ArrayList<ArrayList<String>> in){

		int x=0;
		int y=0;
		Iterator<ArrayList<String>> it = in.iterator();

    	while (it.hasNext()) {
    		for (int i = 0; i < 5; i++) {
    			in.get(y).set(x, in.get(y).get(x).replaceAll("dblCrm", "\""));
    			x++;
			}
    		y++;
    		x=0;
    		it.next();
    	}

		return in;
	}

	public ArrayList<ArrayList<String>> colocarDblCrm(ArrayList<ArrayList<String>> in){

		int x=0;
		int y=0;
		Iterator<ArrayList<String>> it = in.iterator();

    	while (it.hasNext()) {
    		for (int i = 0; i < 5; i++) {
    			in.get(y).set(x, in.get(y).get(x).replaceAll("dblCrm", "\""));
    			x++;
			}
    		y++;
    		x=0;
    		it.next();
    	}

		return in;
	}

	public int getCantidadDetalles() {
		return cantidadDetalles;
	}

	public void setCantidadDetalles(int cantidadDetalles) {
		this.cantidadDetalles = cantidadDetalles;
	}

	public int getCantidadReferencias() {
		return cantidadReferencias;
	}

	public void setCantidadReferencias(int cantidadReferencias) {
		this.cantidadReferencias = cantidadReferencias;
	}

	public int getCantidadDescuentos() {
		return cantidadDescuentos;
	}

	public void setCantidadDescuentos(int cantidadDescuentos) {
		this.cantidadDescuentos = cantidadDescuentos;
	}

	public Boolean getZeta() {
		return zeta;
	}

	public void setZeta(Boolean zeta) {
		this.zeta = zeta;
	}


}
