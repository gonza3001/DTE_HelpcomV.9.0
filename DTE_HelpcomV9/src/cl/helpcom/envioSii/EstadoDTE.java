/**
 * Copyright [2009] [NIC Labs]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the 	License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or 
 * agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 **/

package cl.helpcom.envioSii;

import jargs.gnu.CmdLineParser;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.xmlbeans.XmlOptions;

import cl.helpcom.dao.ConexionSqlWeb;
import cl.helpcom.estadoSII.QueryEstDte;
import cl.helpcom.itext.LeerXML;
import cl.helpcom.recursos.LectorFichero;
import cl.nic.dte.net.ConexionSii;
import cl.nic.dte.net.ConexionSiiModificada;
import cl.nic.dte.util.Utilities;
import cl.sii.siiDte.DTEDocument;
import cl.sii.xmlSchema.RESPUESTADocument;

public class EstadoDTE {

	private static void printUsage() {
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		CmdLineParser parser = new CmdLineParser();
		CmdLineParser.Option empID = parser.addStringOption('e', "empID");
//		CmdLineParser.Option docID = parser.addStringOption('d', "docID");
//		CmdLineParser.Option enviadorOpt = parser.addStringOption('e',
//				"consultador");

		try {
			parser.parse(args);
		} catch (CmdLineParser.OptionException e) {
			printUsage();
			System.exit(2);
		}

		ConexionSqlWeb conexionSqlWeb = new ConexionSqlWeb();
		
		String empIDS = (String) parser.getOptionValue(empID);
//		String docIDS = (String) parser.getOptionValue(docID);
		ArrayList<Integer> indicesID= new ArrayList<>();
		String respuestaADTE="";
		LeerXML leerXML = new LeerXML();
		
		
		
		conexionSqlWeb.obtenerDatos(Integer.valueOf(empIDS));	//OBTIENE DATOS EMPRESA
		indicesID=conexionSqlWeb.cambiarDocEstadoSII(Integer.valueOf(empIDS));
		System.out.println("INDICES POR FIRMAR :"+indicesID.size());
		
		for (int i = 0; i < indicesID.size(); i++) {
			
			conexionSqlWeb.obtenerArchivoVenta(indicesID.get(i).toString()); 			//OBTIENE INFO DTE VENTA
			String[] otherArgs = parser.getRemainingArgs();

			HashMap<String, String> namespaces = new HashMap<String, String>();
			namespaces.put("", "http://www.sii.cl/SiiDte");
			XmlOptions opts = new XmlOptions();
			opts.setLoadSubstituteNamespaces(namespaces);

			ConexionSiiModificada con = new ConexionSiiModificada();

			// leo certificado y llave privada del archivo pkcs12
			KeyStore ks = KeyStore.getInstance("PKCS12");
			ks.load(new FileInputStream("/usr/local/F_E/DTE/"+empIDS+"/Certificado/Certificado.pfx"), conexionSqlWeb.getClaveCert().toCharArray());
			String alias = ks.aliases().nextElement();
//			System.out.println("Usando certificado " + alias);

			X509Certificate x509 = (X509Certificate) ks.getCertificate(alias);
			PrivateKey pKey = (PrivateKey) ks.getKey(alias, conexionSqlWeb.getClaveCert().toCharArray());

			LectorFichero fichero = new LectorFichero();
			String token = con.getToken(pKey, x509);

//			System.out.println("Token: " + token);
			String enviadorS = Utilities.getRutFromCertificate(x509);
			
			ArrayList<String> datosDTE = new ArrayList<>();
			datosDTE.add(conexionSqlWeb.getEmp_rut());				//EMP RUT EMISOR
			datosDTE.add(conexionSqlWeb.getDoc_rut_receptor_vta());	//EMP RUT RECEPTOR
			datosDTE.add(conexionSqlWeb.getTdo_id_vta());			//TIPO DOCUMENTO
			datosDTE.add(conexionSqlWeb.getDoc_folio_vta());		//FOLIO
			datosDTE.add(fichero.invertirFecha(conexionSqlWeb.getDoc_fecha_emision_vta()));//FECHA EMISION
			datosDTE.add(conexionSqlWeb.getDoc_monto_vta());		//MONTO
			
			RESPUESTADocument resp = con.getEstadoDTEProduccion(enviadorS, token,datosDTE);

			opts.setSavePrettyPrintIndent(2);
			opts.setSavePrettyPrint();
			
//			resp.save(System.out, opts);	
			
			respuestaADTE=resp.getRESPUESTA().toString();
			//OBTIENE LA RESPUESTA Y LA TRANSFORMA A FRASE
			System.out.println(leerXML.getEstadoRespuestaWS(leerXML.obtenerEstadoWS(respuestaADTE)));
			conexionSqlWeb.updateEstadoRespSII(indicesID.get(i).toString(), leerXML.getEstadoRespuestaWS(leerXML.obtenerEstadoWS(respuestaADTE)));
			Thread.sleep(Long.valueOf("1000"));
			
		}	
	}
}
