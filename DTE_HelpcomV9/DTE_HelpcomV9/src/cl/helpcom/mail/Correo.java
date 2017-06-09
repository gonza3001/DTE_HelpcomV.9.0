package cl.helpcom.mail;
import java.net.PasswordAuthentication;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Ejemplo de envio de un correo de texto con adjunto con javamail
 * @author Mauricio Rodriguez
  */
public class Correo{
	/**
	 * @param tpo_doc  	Tipo de Documento
	 * @param emp_id  	ID de la Empresa
	 * @param emp_rut	RUT de la Empresa
	 * @param fchEmis	Fecha Emision del Documento
	 * @param folio		Folio del documento
	 * @param mntTotal	Monto Total del Documento
	 */
	public String enviarCorreo(String out,String nombreDocumento,String userName, String password,String titulo,String mensaje,String userNameTercero){
        try
        {
          // se obtiene el objeto Session. La configuracion es para
          // una cuenta de gmail.

//        	String origen="envio.dte.helpcom@gmail.com";
        	String origen=userName;
//        	String destino="maumauromau@gmail.com";
        	String destino=userNameTercero;
        	String pass=password;
//        	String pass="helpcom0170";

        	

        	String rutaAdjunto="";
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.port", "587");
            props.setProperty("mail.smtp.user", destino);
            props.setProperty("mail.smtp.auth", "true");
            props.setProperty("mail.smtp.starttls.enable","true");

            Session session = Session.getDefaultInstance(props, null);
            
            // Se compone la parte del texto
            BodyPart texto = new MimeBodyPart();
            texto.setText(mensaje);

            // Se compone el adjunto con XML
            BodyPart adjuntoXML = new MimeBodyPart();
            //adjuntoXML.setDataHandler(new DataHandler(new FileDataSource(lectorFichero.leerFicheroFirmado("/usr/local/F_E/DTE/"+emp_id+"/Firmados", fch_emis, tdo_id, doc_folio))));
            adjuntoXML.setDataHandler(new DataHandler(new FileDataSource(out)));
            
            
            //ERROR_ los datos de doc_folio y tdo_id se estaban sumando en vez de concatenarse 
            adjuntoXML.setFileName(nombreDocumento);

            // Una MultiParte para agrupar texto e imagen.C:\Users\Administrador_1\Desktop\practica\DTE\28\DocumentoDTE\EnvioTercero
            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(texto);
            multiParte.addBodyPart(adjuntoXML);//1 adjunto

            // Se compone el correo, dando to, from, subject y el C:\Users\Administrador_1\Desktop\practica\DTE\28\DocumentoDTE\EnvioTercero
            // contenido.
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(destino));
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(destino));
            message.setSubject(titulo);
            message.setContent(multiParte);

            // Se envia el correo.
            Transport t = session.getTransport("smtp");
            t.connect(origen, pass);
            t.sendMessage(message, message.getAllRecipients());
            t.close();
            //Se utiliza para verificar que el correo fue enviado. 
            return "ENVIO COMPLETADO";
        }
        catch (Exception e)
        {
            e.printStackTrace();
           return "ENVIO INCOMPLET0";
        }
    }
	
	
	public Transport conexionCuenta(String origen, String pass,String destino) throws MessagingException{
		
		 Properties props = new Properties();
         props.put("mail.smtp.host", "smtp.gmail.com");
         props.setProperty("mail.smtp.starttls.enable", "true");
         props.setProperty("mail.smtp.port", "587");
         props.setProperty("mail.smtp.user", destino);
         props.setProperty("mail.smtp.auth", "true");
         props.setProperty("mail.smtp.starttls.enable","true");
         
         Session session = Session.getDefaultInstance(props, null);
         
         Transport t = session.getTransport("smtp");
         t.connect(origen, pass);
//         t.sendMessage(message, message.getAllRecipients());
//         t.close();
         
         return t;
		
	}
}

