package cl.helpcom.itext;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class EscribirXML {

	public void cambiarMercaderia(String inXML,ArrayList<String> arrayReceptor,ArrayList<String> arrayIdDoc,ArrayList<String> arrayTotales) {

	   try {
		String filepath = inXML;
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(filepath);

		// Get the root element
		Node company = doc.getFirstChild();

		// Get the staff element by tag name directly
		try {
			Node staff = doc.getElementsByTagName("RutResponde").item(0);
			staff.setTextContent(arrayReceptor.get(0));
		} catch (Exception e) {

		}

		try {
			Node staff = doc.getElementsByTagName("NmbContacto").item(0);


			if (arrayReceptor.get(1).length()>38){
				staff.setTextContent(arrayReceptor.get(1).substring(0, 38));
			}else{
				staff.setTextContent(arrayReceptor.get(1));
			}

		} catch (Exception e) {

		}

		try {
			Node staff = doc.getElementsByTagName("TipoDoc").item(0);
			staff.setTextContent(arrayIdDoc.get(0));
		} catch (Exception e) {

		}

		try {
			Node staff = doc.getElementsByTagName("Folio").item(0);
			staff.setTextContent(arrayIdDoc.get(1));
		} catch (Exception e) {

		}

		try {
			Node staff = doc.getElementsByTagName("FchEmis").item(0);
			staff.setTextContent(arrayIdDoc.get(2));
		} catch (Exception e) {

		}

		try {
			Node staff = doc.getElementsByTagName("RUTRecep").item(0);
			staff.setTextContent(arrayReceptor.get(0));
		} catch (Exception e) {

		}

		try {
			Node staff = doc.getElementsByTagName("MntTotal").item(0);
			staff.setTextContent(arrayTotales.get(4));
		} catch (Exception e) {

		}






		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(filepath));
		transformer.transform(source, result);

		System.out.println("Done");

	   } catch (ParserConfigurationException pce) {
		pce.printStackTrace();
	   } catch (TransformerException tfe) {
		tfe.printStackTrace();
	   } catch (IOException ioe) {
		ioe.printStackTrace();
	   } catch (SAXException sae) {
		sae.printStackTrace();
	   }
	}
	public void cambiarDatosRespuesta(String cod_envio,String inXML,ArrayList<String> arrayReceptor,ArrayList<String> arrayEmisor,ArrayList<String> arrayIdDoc,ArrayList<String> arrayTotales,String estadoDTE,String estadoGlosa) {

		
		   try {
			String filepath = inXML;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);

			// Get the root element
			Node company = doc.getFirstChild();

			// Get the staff element by tag name directly
			try {
				Node staff = doc.getElementsByTagName("RutResponde").item(0);
				staff.setTextContent(arrayReceptor.get(0));
			} catch (Exception e) {

			}
			try {
				Node staff = doc.getElementsByTagName("RutRecibe").item(0);
				staff.setTextContent(arrayEmisor.get(0));
			} catch (Exception e) {

			}

			try {
				Node staff = doc.getElementsByTagName("NmbContacto").item(0);
				if (arrayReceptor.get(1).length()>38){
					System.out.println(arrayReceptor.get(1).length());
				staff.setTextContent(arrayReceptor.get(1).substring(0, 38));
				}else{
					staff.setTextContent(arrayReceptor.get(1));
				}
			} catch (Exception e) {

			}

			try {
				Node staff = doc.getElementsByTagName("TipoDoc").item(0);
				staff.setTextContent(arrayIdDoc.get(0));
			} catch (Exception e) {

			}

			try {
				Node staff = doc.getElementsByTagName("Folio").item(0);
				staff.setTextContent(arrayIdDoc.get(1));
			} catch (Exception e) {

			}

			try {
				Node staff = doc.getElementsByTagName("FchEmis").item(0);
				staff.setTextContent(arrayIdDoc.get(2));
			} catch (Exception e) {

			}

			try {
				Node staff = doc.getElementsByTagName("RUTRecep").item(0);
				staff.setTextContent(arrayReceptor.get(0));
			} catch (Exception e) {

			}
			try {
				Node staff = doc.getElementsByTagName("RUTEmisor").item(0);
				staff.setTextContent(arrayEmisor.get(0));
			} catch (Exception e) {

			}
			try {
				Node staff = doc.getElementsByTagName("CodEnvio").item(0);
				staff.setTextContent(cod_envio);
			} catch (Exception e) {

			}

			try {
				Node staff = doc.getElementsByTagName("MntTotal").item(0);
				staff.setTextContent(arrayTotales.get(4));
			} catch (Exception e) {

			}
			try {
				if (estadoDTE.equals("0") || estadoDTE.equals("1")){
					Node staff = doc.getElementsByTagName("EstadoDTE").item(0);
					staff.setTextContent("0");
					estadoGlosa="Envío Recibido Conforme";
				}else{
					Node staff = doc.getElementsByTagName("EstadoDTE").item(0);
					staff.setTextContent("1");
				}
			} catch (Exception e) {

			}
			
			try {
				Node staff = doc.getElementsByTagName("EstadoDTEGlosa").item(0);
				staff.setTextContent(estadoGlosa.replaceAll("@h@", " "));
			} catch (Exception e) {

			}

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filepath));
			transformer.transform(source, result);


		   } catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		   } catch (TransformerException tfe) {
			tfe.printStackTrace();
		   } catch (IOException ioe) {
			ioe.printStackTrace();
		   } catch (SAXException sae) {
			sae.printStackTrace();
		   }
		}

}
