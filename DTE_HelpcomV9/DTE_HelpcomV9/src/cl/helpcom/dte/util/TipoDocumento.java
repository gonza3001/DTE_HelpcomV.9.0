package cl.helpcom.dte.util;

import java.util.ArrayList;

public class TipoDocumento {



	public String tipoDocumentoTexto(String inTpoDoc){
		String out="SET";
		int [] tpo = {33,34,52,56,61,30,32,45,50,55,60,110,111,112,101,104,106,39,41,46,35,38,103,40,43,801,802,803,804,805,806,808,809,810,811,812,813,814,815,901};
		String[] tpoText={"Factura Electrónica",
				"Factura No Afecta o Exenta Electrónica",
				"Guía de Despacho Electrónica",
				"Nota de Débito Electrónica",
				"Nota de Crédito Electrónica",
				"Factura",
				"Factura de venta bienes y servicios no afectos o exentos de IVA",
				"Factura de Compra",
				"Guía de Despacho",
				"Nota de Débito",
				"Nota de Crédito",
				"Factura de Exportación Electrónica",
				"Nota de Débito de Exportación Electrónica",
				"Nota de Crédito de Exportación Electrónica",
				"Factura de Exportación",
				"Nota de Débito de Exportación",
				"Nota de Crédito de Exportación",
				"Boleta Electrónica",
				"Boleta Exenta Electrónica",
				"Factura de Compra Electrónica",
				"Boleta",
				"Boleta exenta",
				"Liquidación",
				"Liquidación Factura",
				"Liquidación-Factura Electrónica",
				"Orden de Compra",
				"Nota de pedido",
				"Contrato",
				"Resolución",
				"Proceso ChileCompra",
				"Ficha ChileCompra",
				"DUS",
				"B/L (Conocimiento de embarque)",
				"AWB (Air Will Bill)",
				"MIC/DTA",
				"Carta de Porte",
				"Resolución del SNA donde califica Servicios de Exportación",
				"Pasaporte",
				"Certificado de Depósito Bolsa Prod. Chile",
				"Vale de Prenda Bolsa Prod. Chile",
				"Factura de ventas a empresas del territorio preferencial ( Res. Ex. N° 1057, del 25.04.85)"};

			for (int i = 0; i < tpo.length; i++) {
				if (inTpoDoc.equals(String.valueOf(tpo[i]))){
					out= tpoText[i];
				}
			}

		return out;
	}

}
