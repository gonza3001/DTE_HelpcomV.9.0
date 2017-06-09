package cl.helpcom.recursos;

public class Formato {


	public String setCaracteresEspeciales(String in){

		//in=in.replaceAll("&","&amp;");
//		in=in.replaceAll("<","&lt;");
//		in=in.replaceAll(">","&gt;");
//		in=in.replaceAll("\"","&gt;");
//		in=in.replaceAll("\'","&apos;");
//		in=in.replaceAll("\\s"," ");//tabuladores
		in= this.quitaEspacios(in);
		in= in.replace("°", "");
		in= in.replace("'", "");

		return in;
	}

	   public String quitaEspacios(String texto) {
	        java.util.StringTokenizer tokens = new java.util.StringTokenizer(texto);
	        StringBuilder buff = new StringBuilder();
	        while (tokens.hasMoreTokens()) {
	            buff.append(" ").append(tokens.nextToken());
	        }
	        return buff.toString().trim();
	    }

	   public String datoAdicionalMaqfront(String arrayDA[] ){
		   String out1="";
		   for (int i = 0; i < arrayDA.length; i++) {
			   if (!arrayDA[i].equals("")){
				   out1+=arrayDA[i]+"\n";
			   }
		   }

		   return out1;
	   }

}
