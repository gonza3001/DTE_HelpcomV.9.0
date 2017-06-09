/**
 *
 */
package cl.helpcom.dte;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.sql.SQLException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.keys.content.X509Data;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.Text;
import org.jdom2.input.DOMBuilder;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.SAXException;

/**
 * Esta clase se encarga de representar una semilla para la autentificacion
 * automatica en el SII
 *
 * @author Jose Urzua <a href="mailto:jose@urzua.cl">jose@urzua.cl</a>
 *
 */
public class Semilla {
	private String semilla;
	private Element firma = null;
	private String signedSeed;

	public Element getDOMElement(String tagName, Namespace ns) {

		Element root = new Element(tagName, ns);
		Element item = new Element("Item", ns);

		Element sem = new Element("Semilla", ns);
		sem.addContent(new Text(getSemilla()));

		item.addContent(sem);

		root.addContent(item);

		if (getFirma() != null)
			root.addContent((Element) getFirma().clone());

		return (root);
	}

	public Document genDocument(Namespace ns) throws InvalidKeyException,
			SignatureException, NoSuchAlgorithmException {
		return (new Document(getDOMElement("getToken", ns)));
	}

	static {
		org.apache.xml.security.Init.init();
	}

	@SuppressWarnings("unchecked")
	public void sign(PrivateKey pKey, X509Certificate cert, Namespace ns)
			throws JDOMException, InvalidKeyException,
			NoSuchAlgorithmException, SignatureException, XMLSecurityException,
			SQLException, ParserConfigurationException, IOException,
			SAXException, Exception {

		Constants.setSignatureSpecNSprefix("");

		String baseUri = null;
		if (ns != null)
			baseUri = ns.getURI();

		String alg = pKey.getAlgorithm();
		if (!alg.equals(cert.getPublicKey().getAlgorithm()))
			throw (new Exception("error algoritmo firma 1"));

		javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory
				.newInstance();
		dbf.setNamespaceAware(true);
		javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		getNiceXML(genDocument(ns), out);

		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		org.w3c.dom.Document doc = db.parse(in);

		org.w3c.dom.Element root = doc.getDocumentElement();
		XMLSignature sig = null;

		if (alg.equals("RSA")) {
			if (pKey != null)
				if (!((RSAPrivateKey) pKey).getModulus().equals(
						((RSAPublicKey) cert.getPublicKey()).getModulus()))
					throw (new Exception("error algoritmo firma 2"));
			sig = new XMLSignature(doc, baseUri,
					XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA1);
		} else if (alg.equals("DSA")) {
			if (!(((DSAPrivateKey) pKey).getParams().getG().equals(
					((DSAPublicKey) cert.getPublicKey()).getParams().getG())
					&& ((DSAPrivateKey) pKey).getParams().getP().equals(
							((DSAPublicKey) cert.getPublicKey()).getParams()
									.getP()) && ((DSAPrivateKey) pKey)
					.getParams().getQ().equals(
							((DSAPublicKey) cert.getPublicKey()).getParams()
									.getQ())))
				throw (new Exception("error algoritmo firma 3"));
			sig = new XMLSignature(doc, baseUri,
					XMLSignature.ALGO_ID_SIGNATURE_DSA);
		} else
			throw (new Exception("error algortimo firma 4"));

		root.appendChild(sig.getElement());
		
		Transforms trans = new Transforms(doc);
		trans.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);

		sig.addDocument("", trans);
		X509Data xdata = new X509Data(doc);
		xdata.addCertificate(cert);

		sig.getKeyInfo().addKeyValue(cert.getPublicKey());
		sig.getKeyInfo().add(xdata);
		sig.sign(pKey);

		// ByteArrayOutputStream bous = new ByteArrayOutputStream();
		DOMBuilder docIn = new DOMBuilder();
		Document domdoc = docIn.build(doc);

		XMLOutputter dout = new XMLOutputter();
		Format fmt = Format.getRawFormat();
		fmt.setEncoding("ISO-8859-1");
		fmt.setLineSeparator("\n");
		fmt.setExpandEmptyElements(true);
		fmt.setIndent(null);
		dout.setFormat(fmt);

		this.signedSeed = dout.outputString(domdoc);

		SAXBuilder sax = new SAXBuilder();
		sax.setValidation(false);
		Document todoFirmado = sax.build(new StringReader(this.signedSeed));

		Element todoElem = todoFirmado.getRootElement();

		List<Element> lista = todoElem.getChildren();

		Element elemSig = lista.get(1);
		setFirma(elemSig);
	}

	public String getSemilla() {
		return semilla;
	}

	public void setSemilla(String semilla) {
		this.semilla = semilla;
	}

	public Element getFirma() {
		return firma;
	}

	public void setFirma(Element firma) {
		this.firma = firma;
	}

	public static void getNiceXML(Document doc, OutputStream out) {
		XMLOutputter dout = new XMLOutputter();
		Format fmt = Format.getRawFormat();
		fmt.setEncoding("ISO-8859-1");
		fmt.setLineSeparator("\n");
		fmt.setExpandEmptyElements(true);
		fmt.setIndent(null);
		dout.setFormat(fmt);
		try {
			dout.output(doc, out);
			out.flush();
		} catch (java.io.IOException e) {
		}
	}
	
	public String getSignedSeed() {
		return signedSeed;
	}

	public void setSignedSeed(String signedSeed) {
		this.signedSeed = signedSeed;
	}
}
