package cl.helpcom.dte.util;

import java.util.ArrayList;

public class TipoDocumento {



	public String tipoDocumentoTexto(String inTpoDoc){
		String out="SET";
		int [] tpo = {33,34,52,56,61,30,32,45,50,55,60,110,111,112,101,104,106,39,41,46,35,38,103,40,43,801,802,803,804,805,806,808,809,810,811,812,813,814,815,901};
		String[] tpoText={"Factura Electr�nica",
				"Factura No Afecta o Exenta Electr�nica",
				"Gu�a de Despacho Electr�nica",
				"Nota de D�bito Electr�nica",
				"Nota de Cr�dito Electr�nica",
				"Factura",
				"Factura de venta bienes y servicios no afectos o exentos de IVA",
				"Factura de Compra",
				"Gu�a de Despacho",
				"Nota de D�bito",
				"Nota de Cr�dito",
				"Factura de Exportaci�n Electr�nica",
				"Nota de D�bito de Exportaci�n Electr�nica",
				"Nota de Cr�dito de Exportaci�n Electr�nica",
				"Factura de Exportaci�n",
				"Nota de D�bito de Exportaci�n",
				"Nota de Cr�dito de Exportaci�n",
				"Boleta Electr�nica",
				"Boleta Exenta Electr�nica",
				"Factura de Compra Electr�nica",
				"Boleta",
				"Boleta exenta",
				"Liquidaci�n",
				"Liquidaci�n Factura",
				"Liquidaci�n-Factura Electr�nica",
				"Orden de Compra",
				"Nota de pedido",
				"Contrato",
				"Resoluci�n",
				"Proceso ChileCompra",
				"Ficha ChileCompra",
				"DUS",
				"B/L (Conocimiento de embarque)",
				"AWB (Air Will Bill)",
				"MIC/DTA",
				"Carta de Porte",
				"Resoluci�n del SNA donde califica Servicios de Exportaci�n",
				"Pasaporte",
				"Certificado de Dep�sito Bolsa Prod. Chile",
				"Vale de Prenda Bolsa Prod. Chile",
				"Factura de ventas a empresas del territorio preferencial ( Res. Ex. N� 1057, del 25.04.85)"};

			for (int i = 0; i < tpo.length; i++) {
				if (inTpoDoc.equals(String.valueOf(tpo[i]))){
					out= tpoText[i];
				}
			}

		return out;
	}

}
