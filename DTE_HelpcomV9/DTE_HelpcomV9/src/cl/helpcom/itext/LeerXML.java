package cl.helpcom.itext;

import java.io.File;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import cl.helpcom.dao.ConexionSql1;
import cl.helpcom.recursos.Formato;

public class LeerXML {

	private Formato formato = new Formato();

	public String obtenerTED(String dte) {


		String TED = "<TED version=\"1.0\">";
		String Salida = "";

		try {
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(dte);
			doc.getDocumentElement().normalize();
			// System.out.println("Root element " +
			// doc.getDocumentElement().getNodeName());
			NodeList nodeLst = doc.getElementsByTagName("TED");
			// System.out.println("Information of all employees");

			for (int s = 0; s < nodeLst.getLength(); s++) {

				Node fstNode = nodeLst.item(s);

				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

					Element fstElmnt = (Element) fstNode;

					NodeList RENmElmntLst = fstElmnt.getElementsByTagName("RE");
					// NodeList RENmElmntLst = fstElmnt.getChildNodes();
					Element RENmElmnt = (Element) RENmElmntLst.item(0);
					NodeList RENm = RENmElmnt.getChildNodes();
					// System.out.println("<DD>"+ ((Node)
					// RENm.item(0)).getNodeValue() +"</DD>");

					String aux1= ((Node) RENm.item(0)).getNodeValue().replaceAll("&", "&amp;");

					TED += "<DD>" + "<RE>"
							+ aux1 + "</RE>";


					// System.out.println(TED);
					NodeList TDNmElmntLst = fstElmnt.getElementsByTagName("TD");
					Element TDNmElmnt = (Element) TDNmElmntLst.item(0);
					NodeList TDNm = TDNmElmnt.getChildNodes();
					// System.out.println("<TD>"+((Node)
					// TDNm.item(0)).getNodeValue()+"</TD>");
					String aux2= ((Node) TDNm.item(0)).getNodeValue();

					TED += "<TD>" + aux2+ "</TD>";

					NodeList FNmElmntLst = fstElmnt.getElementsByTagName("F");
					Element FNmElmnt = (Element) FNmElmntLst.item(0);
					NodeList FNm = FNmElmnt.getChildNodes();
					// System.out.println("F : "
					// + ((Node) FNm.item(0)).getNodeValue());

					String aux3=((Node) FNm.item(0)).getNodeValue();
					TED += "<F>" + aux3 + "</F>";

					NodeList FEmElmntLst = fstElmnt.getElementsByTagName("FE");
					Element FEmElmnt = (Element) FEmElmntLst.item(0);
					NodeList FEm = FEmElmnt.getChildNodes();
					// System.out.println("FE : " + ((Node)
					// FEm.item(0)).getNodeValue());

					String aux4 =((Node) FEm.item(0)).getNodeValue();
					TED += "<FE>" + aux4+ "</FE>";

					NodeList RRmElmntLst = fstElmnt.getElementsByTagName("RR");
					Element RRmElmnt = (Element) RRmElmntLst.item(0);
					NodeList RRm = RRmElmnt.getChildNodes();
					// System.out.println("RR : " + ((Node)
					// RRm.item(0)).getNodeValue());

					String aux5=((Node) RRm.item(0)).getNodeValue();
					TED += "<RR>" + aux5+ "</RR>";

					NodeList RSRmElmntLst = fstElmnt
							.getElementsByTagName("RSR");
					Element RSRmElmnt = (Element) RSRmElmntLst.item(0);
					NodeList RSRm = RSRmElmnt.getChildNodes();
					String aux6=((Node) RSRm.item(0)).getNodeValue();
					TED += "<RSR>" +formato.setCaracteresEspeciales(aux6)+ "</RSR>";

					NodeList MNTmElmntLst = fstElmnt
							.getElementsByTagName("MNT");
					Element MNTmElmnt = (Element) MNTmElmntLst.item(0);
					NodeList MNTm = MNTmElmnt.getChildNodes();

					String aux7=((Node) MNTm.item(0)).getNodeValue();
					TED += "<MNT>" + aux7+ "</MNT>";

					NodeList IT1ElmntLst = fstElmnt.getElementsByTagName("IT1");
					Element IT1mElmnt = (Element) IT1ElmntLst.item(0);
					NodeList IT1m = IT1mElmnt.getChildNodes();

					//String  it1=formato.setCaracteresEspeciales(((Node) IT1m.item(0)).getNodeValue());
					String  it1=((Node) IT1m.item(0)).getNodeValue().replaceAll("&", "&amp;");
					TED += "<IT1>" + it1
							+ "</IT1>";

					TED+="<CAF version=\"1.0\">";
					TED+="<DA>";
					NodeList REmElmntLst = fstElmnt.getElementsByTagName("RE");
					Element REmElmnt = (Element) REmElmntLst.item(0);
					NodeList REm = REmElmnt.getChildNodes();

					String aux8=((Node) REm.item(0)).getNodeValue();
					TED += "<RE>" +aux8
							+ "</RE>";

					NodeList RSmElmntLst = fstElmnt.getElementsByTagName("RS");
					Element RSmElmnt = (Element) RSmElmntLst.item(0);
					NodeList RSm = RSmElmnt.getChildNodes();

					String aux9= ((Node) RSm.item(0)).getNodeValue();
					TED += "<RS>" +formato.setCaracteresEspeciales(aux9)+ "</RS>";

					NodeList TDmElmntLst = fstElmnt.getElementsByTagName("TD");
					Element TDmElmnt = (Element) TDmElmntLst.item(0);
					NodeList TDm = TDmElmnt.getChildNodes();

					String aux10=((Node) TDm.item(0)).getNodeValue();

					TED += "<TD>" + aux10+ "</TD>";

					TED+="<RNG>";

					NodeList DmElmntLst = fstElmnt.getElementsByTagName("D");
					Element DmElmnt = (Element) DmElmntLst.item(0);
					NodeList Dm = DmElmnt.getChildNodes();
					String aux11=((Node) Dm.item(0)).getNodeValue();

					TED += "<D>" + aux11 + "</D>";


					NodeList HmElmntLst = fstElmnt.getElementsByTagName("H");
					Element HmElmnt = (Element) HmElmntLst.item(0);
					NodeList Hm = HmElmnt.getChildNodes();

					String aux12=((Node) Hm.item(0)).getNodeValue() ;
					TED += "<H>" + aux12+ "</H>";

					TED+="</RNG>";

					NodeList FAmElmntLst = fstElmnt.getElementsByTagName("FA");
					Element FAmElmnt = (Element) FAmElmntLst.item(0);
					NodeList FAm = FAmElmnt.getChildNodes();

					String aux13=((Node) FAm.item(0)).getNodeValue();

					TED += "<FA>" + aux13+ "</FA>";

					TED+="<RSAPK>";

					NodeList MmElmntLst = fstElmnt.getElementsByTagName("M");
					Element MmElmnt = (Element) MmElmntLst.item(0);
					NodeList Mm = MmElmnt.getChildNodes();

					String aux14=((Node) Mm.item(0)).getNodeValue();
					TED += "<M>" + aux14 + "</M>";


					NodeList EmElmntLst = fstElmnt.getElementsByTagName("E");
					Element EmElmnt = (Element) EmElmntLst.item(0);
					NodeList Em = EmElmnt.getChildNodes();

					String aux15=((Node) Em.item(0)).getNodeValue();
					TED += "<E>" + aux15 + "</E>";

					TED+="</RSAPK>";

					NodeList IDKmElmntLst = fstElmnt.getElementsByTagName("IDK");
					Element IDKmElmnt = (Element) IDKmElmntLst.item(0);
					NodeList IDKm = IDKmElmnt.getChildNodes();

					String aux16=((Node) IDKm.item(0)).getNodeValue();
					TED += "<IDK>" +aux16+ "</IDK>";

					TED+="</DA>";

					NodeList FRMAmElmntLst = fstElmnt
							.getElementsByTagName("FRMA");
					Element FRMAmElmnt = (Element) FRMAmElmntLst.item(0);
					NodeList FRMAm = FRMAmElmnt.getChildNodes();

					String aux17=((Node) FRMAm.item(0)).getNodeValue();
					TED += "<FRMA algoritmo=\"SHA1withRSA\">" + aux17+ "</FRMA>";

					TED+="</CAF>";

					NodeList TSTEDmElmntLst = fstElmnt.getElementsByTagName("TSTED");
					Element TSTEDmElmnt = (Element) TSTEDmElmntLst.item(0);
					NodeList TSTEDm = TSTEDmElmnt.getChildNodes();

					String aux18=((Node) TSTEDm.item(0)).getNodeValue();
					TED += "<TSTED>" +aux18+ "</TSTED>";

					TED+="</DD>";

					NodeList FRMTmElmntLst = fstElmnt
							.getElementsByTagName("FRMT");
					Element FRMTmElmnt = (Element) FRMTmElmntLst.item(0);
					NodeList FRMTm = FRMTmElmnt.getChildNodes();

					String aux19=((Node) FRMTm.item(0)).getNodeValue();
					TED += "<FRMT algoritmo=\"SHA1withRSA\">" + aux19 + "</FRMT>";

					TED+= "</TED>";

					return TED;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return TED;
	}

	public String getTipoDTE(String dte){

		String tipoDTE = "";
        String Salida = "";


		try {
            File file = new File(dte);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
        //    System.out.println("Root element " + doc.getDocumentElement().getNodeName());
            NodeList nodeLst = doc.getElementsByTagName("IdDoc");
        //    System.out.println("Information of all employees");

            for (int s = 0; s < nodeLst.getLength(); s++) {

                Node fstNode = nodeLst.item(s);

                if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element fstElmnt = (Element) fstNode;

                    NodeList tipoDTEmElmntLst = fstElmnt.getElementsByTagName("TipoDTE");
                    //NodeList RENmElmntLst = fstElmnt.getChildNodes();
                    Element tipoDTEmElmnt = (Element) tipoDTEmElmntLst.item(0);
                    NodeList RENm = tipoDTEmElmnt.getChildNodes();
                    //System.out.println("<DD>"+ ((Node) RENm.item(0)).getNodeValue() +"</DD>");
                    Salida = ((Node) RENm.item(0)).getNodeValue();

                											}
                }}catch (Exception e) {

				}


		return Salida;
	}
	
	public String obtenerEstadoWS(String xmlS) throws ParserConfigurationException, SAXException, IOException{

		String tipoDTE = "";
        String Salida = "";


		
            
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new StringBufferInputStream(xmlS));
            doc.getDocumentElement().normalize();
        //    System.out.println("Root element " + doc.getDocumentElement().getNodeName());
            NodeList nodeLst = doc.getElementsByTagName("SII:RESP_HDR");
        //    System.out.println("Information of all employees");

            for (int s = 0; s < nodeLst.getLength(); s++) {

                Node fstNode = nodeLst.item(s);

                if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element fstElmnt = (Element) fstNode;

                    NodeList tipoDTEmElmntLst = fstElmnt.getElementsByTagName("SII:ESTADO");
                    //NodeList RENmElmntLst = fstElmnt.getChildNodes();
                    Element tipoDTEmElmnt = (Element) tipoDTEmElmntLst.item(0);
                    NodeList RENm = tipoDTEmElmnt.getChildNodes();
                    //System.out.println("<DD>"+ ((Node) RENm.item(0)).getNodeValue() +"</DD>");
                    Salida = ((Node) RENm.item(0)).getNodeValue();

                											}
                }


		return Salida;
	}
	

	public String getFolio(String dte){

		String folio = "";
        String Salida = "";


		try {
            File file = new File(dte);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
        //    System.out.println("Root element " + doc.getDocumentElement().getNodeName());
            NodeList nodeLst = doc.getElementsByTagName("IdDoc");
        //    System.out.println("Information of all employees");

            for (int s = 0; s < nodeLst.getLength(); s++) {

                Node fstNode = nodeLst.item(s);

                if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element fstElmnt = (Element) fstNode;

                    NodeList foliomElmntLst = fstElmnt.getElementsByTagName("Folio");
                    //NodeList RENmElmntLst = fstElmnt.getChildNodes();
                    Element foliomElmnt = (Element) foliomElmntLst.item(0);
                    NodeList RENm = foliomElmnt.getChildNodes();
                    //System.out.println("<DD>"+ ((Node) RENm.item(0)).getNodeValue() +"</DD>");
                    Salida = ((Node) RENm.item(0)).getNodeValue();

                											}
                }}catch (Exception e) {

				}


		return Salida;
	}

	public String getEmpTipoFolio(String dte, Integer empRUT){

		String folio = "";
        String Salida = "'";

		try {
            File file = new File(dte);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nodeLst = doc.getElementsByTagName("IdDoc");

            for (int s = 0; s < nodeLst.getLength(); s++) {

                Node fstNode = nodeLst.item(s);

                if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element fstElmnt = (Element) fstNode;
                    Salida+=empRUT;

                    NodeList tipomElmntLst = fstElmnt.getElementsByTagName("TipoDTE");
                    Element tipomElmnt = (Element) tipomElmntLst.item(0);
                    NodeList TIPOm = tipomElmnt.getChildNodes();

                    Salida += ","+((Node) TIPOm.item(0)).getNodeValue();

                    NodeList foliomElmntLst = fstElmnt.getElementsByTagName("Folio");
                    Element foliomElmnt = (Element) foliomElmntLst.item(0);
                    NodeList RENm = foliomElmnt.getChildNodes();

                    Salida +=","+ ((Node) RENm.item(0)).getNodeValue();
                    Salida+="'";
                											}
                }}catch (Exception e) {

				}


		return Salida;
	}

	public ArrayList<String> getIdDoc(String inXML){

		String TED="";
		ArrayList<String> arrayIdDoc = new ArrayList<String>();

		try {
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(inXML);
			doc.getDocumentElement().normalize();
			// System.out.println("Root element " +
			// doc.getDocumentElement().getNodeName());
			NodeList nodeLst = doc.getElementsByTagName("IdDoc");
			// System.out.println("Information of all employees");

			for (int s = 0; s < nodeLst.getLength(); s++) {

				Node fstNode = nodeLst.item(s);

				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

					Element fstElmnt = (Element) fstNode;

					NodeList RENmElmntLst = fstElmnt.getElementsByTagName("TipoDTE");
					Element RENmElmnt = (Element) RENmElmntLst.item(0);
					NodeList RENm = RENmElmnt.getChildNodes();
					arrayIdDoc.add(((Node) RENm.item(0)).getNodeValue());

					NodeList TDNmElmntLst = fstElmnt.getElementsByTagName("Folio");
					Element TDNmElmnt = (Element) TDNmElmntLst.item(0);
					NodeList TDNm = TDNmElmnt.getChildNodes();
					arrayIdDoc.add(((Node) TDNm.item(0)).getNodeValue());

					NodeList FchEmisElmntLst = fstElmnt.getElementsByTagName("FchEmis");
					Element FchEmisElmnt = (Element) FchEmisElmntLst.item(0);
					NodeList FchEmis = FchEmisElmnt.getChildNodes();
					arrayIdDoc.add(((Node) FchEmis.item(0)).getNodeValue());

				}
			}
		}catch(Exception e){
			System.out.println("Se leyó Mal algun archivo del XML: "+e.getMessage());
		}
		return arrayIdDoc;
	}


	public ArrayList<String> getEmisor(String inXML){

		String TED="";
		ArrayList<String> arrayEmisor = new ArrayList<String>();

		try {
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(inXML);
			doc.getDocumentElement().normalize();
			NodeList nodeLst = doc.getElementsByTagName("Emisor");

			for (int s = 0; s < nodeLst.getLength(); s++) {

				Node fstNode = nodeLst.item(s);

				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

					Element fstElmnt = (Element) fstNode;

					try {
						NodeList RENmElmntLst = fstElmnt.getElementsByTagName("RUTEmisor");
						Element RENmElmnt = (Element) RENmElmntLst.item(0);
						NodeList RENm = RENmElmnt.getChildNodes();
						arrayEmisor.add(((Node) RENm.item(0)).getNodeValue());
					} catch (Exception e) {
						arrayEmisor.add("");
					}

					try {
						NodeList TDNmElmntLst = fstElmnt.getElementsByTagName("RznSoc");
						Element TDNmElmnt = (Element) TDNmElmntLst.item(0);
						NodeList TDNm = TDNmElmnt.getChildNodes();
						arrayEmisor.add(((Node) TDNm.item(0)).getNodeValue());

					} catch (Exception e) {
						arrayEmisor.add("");
					}
					try {
						NodeList FchEmisElmntLst = fstElmnt.getElementsByTagName("GiroEmis");
						Element FchEmisElmnt = (Element) FchEmisElmntLst.item(0);
						NodeList FchEmis = FchEmisElmnt.getChildNodes();
						arrayEmisor.add(((Node) FchEmis.item(0)).getNodeValue());

					} catch (Exception e) {
						arrayEmisor.add("");
					}
					try {
						NodeList ActecoElmntLst = fstElmnt.getElementsByTagName("Acteco");
						Element ActecoElmnt = (Element) ActecoElmntLst.item(0);
						NodeList Acteco = ActecoElmnt.getChildNodes();
						arrayEmisor.add(((Node) Acteco.item(0)).getNodeValue());
					} catch (Exception e) {
						arrayEmisor.add("");
					}

					try {
						NodeList DirOrigenElmntLst = fstElmnt.getElementsByTagName("DirOrigen");
						Element DirOrigenElmnt = (Element) DirOrigenElmntLst.item(0);
						NodeList DirOrigen = DirOrigenElmnt.getChildNodes();
						arrayEmisor.add(((Node) DirOrigen.item(0)).getNodeValue());
					} catch (Exception e) {
						arrayEmisor.add("");
					}

					try {
						NodeList CmnaOrigenElmntLst = fstElmnt.getElementsByTagName("CmnaOrigen");
						Element CmnaOrigenElmnt = (Element) CmnaOrigenElmntLst.item(0);
						NodeList CmnaOrigen = CmnaOrigenElmnt.getChildNodes();
						arrayEmisor.add(((Node) CmnaOrigen.item(0)).getNodeValue());

					} catch (Exception e) {
						arrayEmisor.add("");
					}
									}
			}
		}catch(Exception e){
			System.out.println("Se leyó Mal algun archivo del XML: "+e.getMessage());
		}
		return arrayEmisor;
	}

	public ArrayList<String> getReceptor(String inXML){

		String TED="";
		ArrayList<String> arrayReceptor = new ArrayList<String>();

		try {
//			File file = new File(inXML);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(inXML);
			doc.getDocumentElement().normalize();
			NodeList nodeLst = doc.getElementsByTagName("Receptor");

			for (int s = 0; s < nodeLst.getLength(); s++) {

				Node fstNode = nodeLst.item(s);

				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

					Element fstElmnt = (Element) fstNode;

					try {
						NodeList RENmElmntLst = fstElmnt.getElementsByTagName("RUTRecep");
						Element RENmElmnt = (Element) RENmElmntLst.item(0);
						NodeList RENm = RENmElmnt.getChildNodes();
						arrayReceptor.add(((Node) RENm.item(0)).getNodeValue());

					} catch (Exception e) {
						arrayReceptor.add("");
					}
					try {

						NodeList TDNmElmntLst = fstElmnt.getElementsByTagName("RznSocRecep");
						Element TDNmElmnt = (Element) TDNmElmntLst.item(0);
						NodeList TDNm = TDNmElmnt.getChildNodes();
						arrayReceptor.add(((Node) TDNm.item(0)).getNodeValue());

					} catch (Exception e) {
						arrayReceptor.add("");
					}
					try {
						NodeList FchEmisElmntLst = fstElmnt.getElementsByTagName("GiroRecep");
						Element FchEmisElmnt = (Element) FchEmisElmntLst.item(0);
						NodeList FchEmis = FchEmisElmnt.getChildNodes();
						arrayReceptor.add(((Node) FchEmis.item(0)).getNodeValue());

					} catch (Exception e) {
						arrayReceptor.add("");
					}

					try {
						NodeList ActecoElmntLst = fstElmnt.getElementsByTagName("DirRecep");
						Element ActecoElmnt = (Element) ActecoElmntLst.item(0);
						NodeList Acteco = ActecoElmnt.getChildNodes();
						arrayReceptor.add(((Node) Acteco.item(0)).getNodeValue());
					} catch (Exception e) {
						arrayReceptor.add("");
					}

					try {
						NodeList DirOrigenElmntLst = fstElmnt.getElementsByTagName("CmnaRecep");
						Element DirOrigenElmnt = (Element) DirOrigenElmntLst.item(0);
						NodeList DirOrigen = DirOrigenElmnt.getChildNodes();
						arrayReceptor.add(((Node) DirOrigen.item(0)).getNodeValue());

					} catch (Exception e) {
						arrayReceptor.add("");
					}
					try {
						NodeList CmnaOrigenElmntLst = fstElmnt.getElementsByTagName("CiudadRecep");
						Element CmnaOrigenElmnt = (Element) CmnaOrigenElmntLst.item(0);
						NodeList CmnaOrigen = CmnaOrigenElmnt.getChildNodes();
						arrayReceptor.add(((Node) CmnaOrigen.item(0)).getNodeValue());
					} catch (Exception e) {
						arrayReceptor.add("");
					}
				}
			}
		}catch(Exception e){
			System.out.println("Se leyó Mal algun archivo del XML: "+e.getMessage());
		}
		return arrayReceptor;
	}

	public ArrayList<String> getTotales(String inXML){

		String TED="";
		ArrayList<String> arrayTotales= new ArrayList<String>();

		try {
//			File file = new File(inXML);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(inXML);
			doc.getDocumentElement().normalize();
			NodeList nodeLst = doc.getElementsByTagName("Totales");

			for (int s = 0; s < nodeLst.getLength(); s++) {

				Node fstNode = nodeLst.item(s);

				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

					Element fstElmnt = (Element) fstNode;

					try {
						NodeList RENmElmntLst = fstElmnt.getElementsByTagName("MntNeto");
						Element RENmElmnt = (Element) RENmElmntLst.item(0);
						NodeList RENm = RENmElmnt.getChildNodes();
						arrayTotales.add(((Node) RENm.item(0)).getNodeValue());
					} catch (Exception e) {
						arrayTotales.add("");
					}

					try {
						NodeList TDNmElmntLst = fstElmnt.getElementsByTagName("MntExe");
						Element TDNmElmnt = (Element) TDNmElmntLst.item(0);
						NodeList TDNm = TDNmElmnt.getChildNodes();
						arrayTotales.add(((Node) TDNm.item(0)).getNodeValue());
					} catch (Exception e) {
						arrayTotales.add("");
					}

					try {
						NodeList FchEmisElmntLst = fstElmnt.getElementsByTagName("TasaIVA");
						Element FchEmisElmnt = (Element) FchEmisElmntLst.item(0);
						NodeList FchEmis = FchEmisElmnt.getChildNodes();
						arrayTotales.add(((Node) FchEmis.item(0)).getNodeValue());

					} catch (Exception e) {
						arrayTotales.add("");
					}

					try {
						NodeList ActecoElmntLst = fstElmnt.getElementsByTagName("IVA");
						Element ActecoElmnt = (Element) ActecoElmntLst.item(0);
						NodeList Acteco = ActecoElmnt.getChildNodes();
						arrayTotales.add(((Node) Acteco.item(0)).getNodeValue());
					} catch (Exception e) {
						arrayTotales.add("");
					}

					try {
						NodeList DirOrigenElmntLst = fstElmnt.getElementsByTagName("MntTotal");
						Element DirOrigenElmnt = (Element) DirOrigenElmntLst.item(0);
						NodeList DirOrigen = DirOrigenElmnt.getChildNodes();
						arrayTotales.add(((Node) DirOrigen.item(0)).getNodeValue());
					} catch (Exception e) {
						arrayTotales.add("");
					}

				}
			}
		}catch(Exception e){
			System.out.println("Se leyó Mal algun archivo del XML: "+e.getMessage());
		}

		return arrayTotales;
	}

	public ArrayList<ArrayList<String>> getDetalles(String inXML) throws ClassNotFoundException, SQLException, IOException{

		ArrayList<ArrayList<String>> arrayDetalle = new ArrayList<ArrayList<String>>();
		ArrayList<String> arrayLinDetalle= new ArrayList<String>();
		ConexionSql1 con = new ConexionSql1();

		try {
//			File fXmlFile = new File(inXML);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inXML);
			doc.getDocumentElement().normalize();
			//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			NodeList nList = doc.getElementsByTagName("Detalle");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					try {
						arrayLinDetalle.add(eElement.getElementsByTagName("NroLinDet").item(0).getTextContent());
					} catch (Exception e) {
						arrayLinDetalle.add("");
					}
					try {
						arrayLinDetalle.add(eElement.getElementsByTagName("IndExe").item(0).getTextContent());
					} catch (Exception e) {
						arrayLinDetalle.add("");
					}
					try {
						arrayLinDetalle.add(eElement.getElementsByTagName("NmbItem").item(0).getTextContent());
					} catch (Exception e) {
						arrayLinDetalle.add("");
					}
					try {
						arrayLinDetalle.add(eElement.getElementsByTagName("QtyItem").item(0).getTextContent());
					} catch (Exception e) {
						arrayLinDetalle.add("");
					}
					try {
						arrayLinDetalle.add(eElement.getElementsByTagName("PrcItem").item(0).getTextContent());
					} catch (Exception e) {
						arrayLinDetalle.add("");
					}
					try {
						arrayLinDetalle.add(eElement.getElementsByTagName("MontoItem").item(0).getTextContent());
					} catch (Exception e) {
						arrayLinDetalle.add("");
					}
					try {
						arrayLinDetalle.add(eElement.getElementsByTagName("DescuentoPct").item(0).getTextContent());
					} catch (Exception e) {

						arrayLinDetalle.add("");
					}
					try {
						arrayLinDetalle.add(eElement.getElementsByTagName("DescuentoMonto").item(0).getTextContent());
					} catch (Exception e) {
						arrayLinDetalle.add("");
					}

					arrayDetalle.add(arrayLinDetalle);
					arrayLinDetalle = new ArrayList<>();

					}

					}

				}catch(Exception e){
					System.out.println("Error al leer detalle "+e.getMessage());
					}
		return arrayDetalle;
				}

	public ArrayList<ArrayList<String>> getReferencias(String inXML) throws ClassNotFoundException, SQLException, IOException{

		ArrayList<ArrayList<String>> arrayReferencia = new ArrayList<ArrayList<String>>();
		ArrayList<String> arrayLinReferencia= new ArrayList<String>();
		ConexionSql1 con = new ConexionSql1();

		try {
//			File fXmlFile = new File(inXML);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inXML);
			doc.getDocumentElement().normalize();
			//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			NodeList nList = doc.getElementsByTagName("Referencia");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					try {
						arrayLinReferencia.add(eElement.getElementsByTagName("NroLinRef").item(0).getTextContent());
					} catch (Exception e) {
						arrayLinReferencia.add("");
					}
					try {
						arrayLinReferencia.add(eElement.getElementsByTagName("TpoDocRef").item(0).getTextContent());
					} catch (Exception e) {
						arrayLinReferencia.add("");
					}
					try {
						arrayLinReferencia.add(eElement.getElementsByTagName("FolioRef").item(0).getTextContent());
					} catch (Exception e) {
						arrayLinReferencia.add("");
					}
					try {
						arrayLinReferencia.add(eElement.getElementsByTagName("FchRef").item(0).getTextContent());
					} catch (Exception e) {
						arrayLinReferencia.add("");
					}
					try {
						arrayLinReferencia.add(eElement.getElementsByTagName("RazonRef").item(0).getTextContent());
					} catch (Exception e) {
						arrayLinReferencia.add("");
					}

					arrayReferencia.add(arrayLinReferencia);
					arrayLinReferencia = new ArrayList<>();

					}
					}

				}catch(Exception e){
					System.out.println("Error al leer referencia "+e.getMessage());
					}
		return arrayReferencia;
				}
	
public String getEstadoRespuestaWS(String in){
		
	if (in.equals("DOK")){
		return "DTE Recibido por SII";
	}else if (in.equals("DNK")){
		return "DTE Recibido por SII";
	}else if (in.equals("FAU")){
		return "DTE NO Recibido por SII";
	}else if (in.equals("FNA")){
		return "DTE NO Autorizado";
	}else if (in.equals("FAN")){
		return "DTE Anulado";
	}else if (in.equals("EMP")){
		return "Empresa NO Autorizada";
	}else if (in.equals("TMD")){
		return "Nota de Débito Modifica texto";
	}else if (in.equals("TMC")){
		return "Nota de Crédito Modifica texto";
	}else if (in.equals("MMD")){
		return "Nota de Débito Modifica Montos";
	}else if (in.equals("MMC")){
		return "Nota de Crédito Modifica Montos";
	}else if (in.equals("AND")){
		return "Nota de Débito Anula Documento";
	}else if (in.equals("ANC")){
		return "Nota de Crédito Anula Documento";
	}    
	return "";
	
	}


}
